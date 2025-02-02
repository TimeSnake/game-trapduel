/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.game.trapduel.server;

import de.timesnake.basic.bukkit.core.server.MathHelper;
import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.ServerManager;
import de.timesnake.basic.bukkit.util.user.User;
import de.timesnake.basic.bukkit.util.user.event.UserDeathEvent;
import de.timesnake.basic.bukkit.util.user.scoreboard.Sideboard;
import de.timesnake.basic.bukkit.util.world.ExLocation;
import de.timesnake.basic.bukkit.util.world.ExWorld;
import de.timesnake.basic.loungebridge.util.game.TmpGame;
import de.timesnake.basic.loungebridge.util.server.EndMessage;
import de.timesnake.basic.loungebridge.util.server.LoungeBridgeServer;
import de.timesnake.basic.loungebridge.util.server.LoungeBridgeServerManager;
import de.timesnake.basic.loungebridge.util.user.GameUser;
import de.timesnake.basic.loungebridge.util.user.KitManager;
import de.timesnake.database.util.game.DbGame;
import de.timesnake.database.util.game.DbTmpGame;
import de.timesnake.game.trapduel.main.GameTrapDuel;
import de.timesnake.game.trapduel.user.TrapDuelUser;
import de.timesnake.library.basic.util.Status;
import de.timesnake.library.chat.ExTextColor;
import de.timesnake.library.chat.Plugin;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


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

    this.generateUserSpawnPoints();
  }

  @Override
  protected TmpGame loadGame(DbGame dbGame, boolean loadWorlds) {
    return new TmpGame((DbTmpGame) dbGame, true) {
      @Override
      public KitManager<?> loadKitManager() {
        return new TrapDuelKitManager();
      }
    };
  }

  @Override
  public void onWorldLoad() {
    Server.getWorldManager().deleteWorld(this.gameWorld, true);
    this.gameWorld = Server.getWorldManager().createWorld(WORLD_NAME);
    this.generateUserSpawnPoints();
    gameWorld.setTime(1000);
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
          user.sendPluginMessage(Plugin.GAME,
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
            broadcastGameMessage(Component.text("The Peace-Time ends ", ExTextColor.PUBLIC)
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
    this.loadWorld();

    countdownPeaceRunning = false;
    countdownSwitchRunning = false;
    this.stopAfterStart = false;
  }

  @Override
  public boolean checkGameEnd() {
    return this.getUsers().size() <= 1;
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

  public int getNewSwitchCountdown() {
    return (int) (50 * MathHelper.getGaussian() * timeMultiplier + 10);
  }

  @EventHandler
  public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
    if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
      e.setCancelled(true);
    }
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

    new EndMessage()
        .winner(Server.getInGameUsers().stream().findAny().orElse(null))
        .send();

    LoungeBridgeServer.closeGame();
  }

  @Override
  public ExLocation getSpectatorSpawn() {
    return Server.getInGameUsers().iterator().hasNext() ? Server.getInGameUsers().iterator().next().getExLocation() :
        null;
  }
}
