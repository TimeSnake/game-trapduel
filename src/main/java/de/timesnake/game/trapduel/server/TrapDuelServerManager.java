/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.game.trapduel.server;

import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.ServerManager;
import de.timesnake.basic.bukkit.util.exception.UnsupportedGroupRankException;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.event.UserDeathEvent;
import de.timesnake.basic.bukkit.util.user.scoreboard.Sideboard;
import de.timesnake.basic.bukkit.util.world.ExLocation;
import de.timesnake.basic.bukkit.util.world.ExWorld;
import de.timesnake.basic.game.util.game.Map;
import de.timesnake.basic.game.util.game.Team;
import de.timesnake.basic.game.util.game.TmpGame;
import de.timesnake.basic.loungebridge.util.server.LoungeBridgeServer;
import de.timesnake.basic.loungebridge.util.server.LoungeBridgeServerManager;
import de.timesnake.basic.loungebridge.util.user.GameUser;
import de.timesnake.basic.loungebridge.util.user.Kit;
import de.timesnake.basic.loungebridge.util.user.KitNotDefinedException;
import de.timesnake.database.util.game.DbGame;
import de.timesnake.database.util.game.DbKit;
import de.timesnake.database.util.game.DbMap;
import de.timesnake.database.util.game.DbTeam;
import de.timesnake.database.util.game.DbTmpGame;
import de.timesnake.game.trapduel.chat.Plugin;
import de.timesnake.game.trapduel.main.GameTrapDuel;
import de.timesnake.game.trapduel.user.TrapDuelUser;
import de.timesnake.library.basic.util.Loggers;
import de.timesnake.library.basic.util.Status;
import de.timesnake.library.chat.ExTextColor;
import de.timesnake.library.extension.util.chat.Chat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;


public class TrapDuelServerManager extends LoungeBridgeServerManager<TmpGame> implements Listener {

    public static final String WORLD_NAME = "trapduel";
    public static final List<Biome> BLOCKED_BIOMES = List.of(Biome.OCEAN, Biome.DEEP_OCEAN,
            Biome.DEEP_COLD_OCEAN,
            Biome.COLD_OCEAN, Biome.FROZEN_OCEAN, Biome.DEEP_FROZEN_OCEAN,
            Biome.DEEP_LUKEWARM_OCEAN,
            Biome.LUKEWARM_OCEAN, Biome.WARM_OCEAN);
    public static final Integer SWITCH_PEACE = 60 * 3;
    public static final Integer PEACE = 60 * 5;

    public static TrapDuelServerManager getInstance() {
        return (TrapDuelServerManager) ServerManager.getInstance();
    }

    private ArrayList<Location> userSpawnPoints = new ArrayList<>();

    private int countdownPeace;
    private boolean countdownPeaceRunning = false;
    private BukkitTask peaceTimeTask;

    private boolean countdownSwitchRunning = false;
    private BukkitTask switchTask;

    private float timeMultiplier = 1;

    private ExWorld gameWorld;

    private boolean stopAfterStart = false;

    @Override
    public TrapDuelUser loadUser(Player player) {
        return new TrapDuelUser(player);
    }

    @Override
    public Sideboard getGameSideboard() {
        return null;
    }

    public void onTrapDuelEnable() {
        super.onLoungeBridgeEnable();

        if (!this.areKitsEnabled()) {
            timeMultiplier = 2;
        }

        this.gameWorld = Server.getWorldManager().getWorld(WORLD_NAME);
        if (this.gameWorld != null) {
            Server.getWorldManager().deleteWorld(this.gameWorld, true);
        }
        this.gameWorld = Server.getWorldManager().createWorld(WORLD_NAME);
        Loggers.GAME.info("Game world created");

        this.generateUserSpawnPoints();
    }

    @Override
    protected TmpGame loadGame(DbGame dbGame, boolean loadWorlds) {
        return new TmpGame((DbTmpGame) dbGame, true) {
            @Override
            public Team loadTeam(DbTeam team) throws UnsupportedGroupRankException {
                return new Team(team);
            }

            @Override
            public Map loadMap(DbMap dbMap, boolean loadWorld) {
                return new Map(dbMap, loadWorld);
            }

            @Override
            public TrapDuelKit loadKit(DbKit dbKit) {
                int dbId = dbKit.getId();
                for (TrapDuelKit kit : TrapDuelKit.KITS) {
                    if (kit.getId().equals(dbId)) {
                        return kit;
                    }
                }
                return null;
            }
        };
    }


    @Override
    public void onGamePrepare() {
        new BukkitRunnable() {
            @Override
            public void run() {
                gameWorld.setTime(1000);
            }
        }.runTask(GameTrapDuel.getPlugin());
    }

    @Override
    public void onGameStart() {
        if (this.stopAfterStart) {
            this.stopGame();
            return;
        }

        for (User user : Server.getInGameUsers()) {
            user.setFlySpeed(1);
            user.setGravity(true);
            user.setFlying(false);
            user.setAllowFlight(false);
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                for (User user : Server.getInGameUsers()) {
                    Player p = user.getPlayer();
                    p.setInvulnerable(false);
                    user.sendPluginMessage(Plugin.TRAP_DUEL,
                            Component.text("You are now vulnerable!", ExTextColor.WARNING));
                }
            }
        }.runTaskLaterAsynchronously(GameTrapDuel.getPlugin(), 10 * 20);
        this.countdownPeace = (int) (PEACE * timeMultiplier);
        this.startPeaceCountdown();
    }

    @Override
    public void onGameUserQuit(GameUser user) {
        Player p = user.getPlayer();
        p.setInvulnerable(true);
        user.setStatus(Status.User.OUT_GAME);
        if (Server.getInGameUsers().size() <= 1) {
            this.stopGame();
        }
    }

    @Override
    public void onGameUserQuitBeforeStart(GameUser user) {
        this.stopAfterStart = true;
    }

    @Override
    public boolean isRejoiningAllowed() {
        return false;
    }

    public void startPeaceCountdown() {
        if (!this.isCountdownPeaceRunning()) {
            countdownPeaceRunning = true;

            this.peaceTimeTask = Server.runTaskTimerAsynchrony(() -> {
                switch (countdownPeace) {
                    case 120, 180, 300, 600 -> broadcastGameMessage(
                            Component.text("The Peace-Time ends in ", ExTextColor.PUBLIC)
                                    .append(Component.text(countdownPeace / 60 + " min",
                                            ExTextColor.VALUE)));
                    case 60 -> broadcastGameMessage(
                            Component.text("The Peace-Time ends in ", ExTextColor.PUBLIC)
                                    .append(Component.text("1 min", ExTextColor.VALUE)));
                    case 1 -> broadcastGameMessage(
                            Component.text("The Peace-Time ends in ", ExTextColor.PUBLIC)
                                    .append(Component.text("1 s", ExTextColor.VALUE)));
                    case 0 -> {
                        broadcastGameMessage(
                                Component.text("The Peace-Time ends ", ExTextColor.PUBLIC)
                                        .append(Component.text("now!", ExTextColor.WARNING)));
                        broadcastGameMessage(
                                Component.text("The Switch-Time begins!", ExTextColor.WARNING));
                        broadcastGameMessage(
                                Component.text("Be attentive and prepared!", ExTextColor.WARNING));
                        countdownPeaceRunning = false;
                        this.peaceTimeTask.cancel();
                        startSwitchCountdown();
                    }
                    default -> {
                        if (countdownPeace <= 10 || countdownPeace == 30) {
                            broadcastGameMessage(
                                    Component.text("The Peace-Time ends in ", ExTextColor.PUBLIC)
                                            .append(Component.text(countdownPeace + " s",
                                                    ExTextColor.VALUE)));
                        }
                    }
                }
                countdownPeace--;
            }, 0, 20, GameTrapDuel.getPlugin());
        }
    }

    public void startSwitchCountdown() {
        if (!this.isCountdownSwitchRunning()) {
            countdownSwitchRunning = true;

            this.switchTask = Server.runTaskLaterSynchrony(() -> {
                List<User> users = new ArrayList<>(Server.getInGameUsers());
                Collections.shuffle(users);

                Location firstLocation = users.get(0).getLocation();

                for (int i = 0; i < users.size(); i++) {
                    User user = users.get(i);

                    Vector vec = user.getPlayer().getVelocity();
                    boolean isSneaking = user.getPlayer().isSneaking();
                    boolean isSwimming = user.getPlayer().isSwimming();

                    //last user
                    if (user.equals(users.get(users.size() - 1))) {
                        firstLocation.getChunk().load(true);
                        firstLocation.getChunk().setForceLoaded(true);
                        user.teleport(firstLocation);
                    } else {
                        User toTp = users.get(i + 1);
                        toTp.getLocation().getChunk().load(true);
                        toTp.getLocation().getChunk().setForceLoaded(true);
                        user.teleport(toTp);

                    }

                    user.getPlayer().setVelocity(vec);
                    user.getPlayer().setSneaking(isSneaking);
                    user.getPlayer().setSwimming(isSwimming);
                }

                broadcastGameMessage(Component.text("Switched!", ExTextColor.WARNING));
                countdownSwitchRunning = false;
                countdownPeace = SWITCH_PEACE;
                startPeaceCountdown();
                this.switchTask.cancel();
            }, 20 * this.getNewSwitchCountdown(), GameTrapDuel.getPlugin());

        }
    }

    @Override
    public void onGameReset() {
        Server.getWorldManager().deleteWorld(this.gameWorld, true);
        this.gameWorld = Server.getWorldManager().createWorld(WORLD_NAME);

        this.generateUserSpawnPoints();

        countdownPeaceRunning = false;
        countdownSwitchRunning = false;
        this.stopAfterStart = false;

        Loggers.GAME.info("Reset successfully");
    }

    @Override
    public Sideboard getSpectatorSideboard() {
        return null;
    }

    @EventHandler
    public void onPlayerDeath(UserDeathEvent e) {
        User user = e.getUser();
        if (user.getStatus().equals(Status.User.IN_GAME)) {
            e.setBroadcastDeathMessage(false);
            this.broadcastGameMessage(user.getChatNameComponent()
                    .append(Component.text(" died", ExTextColor.WARNING)));
            e.setAutoRespawn(true);

            ((TrapDuelUser) user).joinSpectator();

            if (Server.getInGameUsers().size() <= 1) {
                this.stopGame();
            }
        } else {
            e.setBroadcastDeathMessage(false);
        }
    }

    @Override
    public Kit getKit(int index) throws KitNotDefinedException {
        for (TrapDuelKit kit : TrapDuelKit.KITS) {
            if (kit.getId().equals(index)) {
                return kit;
            }
        }
        throw new KitNotDefinedException(index);
    }

    public int getNewSwitchCountdown() {
        return (int) (50 * getGaussian() * timeMultiplier + 10);
    }

    public double getGaussian() {
        double u = 0, v = 0;
        while (u == 0) {
            u = Math.random(); //Converting [0,1) to (0,1)
        }
        while (v == 0) {
            v = Math.random();
        }
        double num = Math.sqrt(-2.0 * Math.log(u)) * Math.cos(2.0 * Math.PI * v);

        num = num / 10.0 + 0.5; // Translate to 0 -> 1
        if (num > 1 || num < 0) {
            num = getGaussian(); // resample between 0 and 1 if out of range
        }
        return num;
    }


    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        User user = Server.getUser(e.getPlayer());

        if (user == null) {
            return;
        }

        if (user.getStatus().equals(Status.User.OUT_GAME)) {
            e.setRespawnLocation(user.getLocation());
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            e.setCancelled(true);

        }
    }

    @Override
    public Kit[] getKits() {
        return TrapDuelKit.KITS;
    }


    public void generateUserSpawnPoints() {
        userSpawnPoints = new ArrayList<>();
        int maxPlayers = Server.getMaxPlayers();

        int spawnPoint = 2000;
        while (userSpawnPoints.size() < maxPlayers) {
            Location loc;
            do {
                loc = new ExLocation(gameWorld, spawnPoint, 255, spawnPoint);
                spawnPoint += 2000;
            } while (BLOCKED_BIOMES.contains(loc.getBlock().getBiome()));
            userSpawnPoints.add(loc);
            Loggers.GAME.info("Added spawn: " + spawnPoint);
        }
    }

    public Location getUserSpawnPoint() {
        Location loc = userSpawnPoints.get(0);
        userSpawnPoints.remove(loc);
        return loc;
    }

    public int getCountdownPeace() {
        return countdownPeace;
    }

    public boolean isCountdownPeaceRunning() {
        return countdownPeaceRunning;
    }

    public boolean isCountdownSwitchRunning() {
        return countdownSwitchRunning;
    }

    @Override
    public void onGameStop() {
        if (this.peaceTimeTask != null) {
            this.peaceTimeTask.cancel();
        }

        if (this.switchTask != null) {
            this.switchTask.cancel();
        }
        for (User user : Server.getInGameUsers()) {
            user.getPlayer().setInvulnerable(true);
            user.setGameMode(GameMode.ADVENTURE);
        }
        if (Server.getInGameUsers().size() == 1) {
            User winner = Server.getInGameUsers().iterator().next();
            this.broadcastGameMessage(Chat.getLongLineSeparator());
            this.broadcastGameMessage(winner.getChatNameComponent()
                    .append(Component.text(" wins", ExTextColor.PUBLIC)));
            this.broadcastGameMessage(Chat.getLongLineSeparator());

            LoungeBridgeServer.closeGame();
        }
    }


    @Override
    public Plugin getGamePlugin() {
        return Plugin.TRAP_DUEL;
    }

    @Override
    public ExLocation getSpectatorSpawn() {
        return Server.getInGameUsers().iterator().hasNext() ?
                Server.getInGameUsers().iterator().next().getExLocation() : null;
    }
}
