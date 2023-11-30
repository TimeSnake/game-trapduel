/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.game.trapduel.user;

import de.timesnake.basic.bukkit.util.world.ExLocation;
import de.timesnake.basic.loungebridge.util.user.GameUser;
import de.timesnake.game.trapduel.server.TrapDuelServer;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class TrapDuelUser extends GameUser {


  public TrapDuelUser(Player player) {
    super(player);
  }

  @Override
  public void onGameJoin() {
    super.onGameJoin();

    this.setGameMode(GameMode.SURVIVAL);

    this.setAllowFlight(true);
    this.setFlying(true);
    this.setGravity(false);
    this.setFlySpeed(0);

    this.setInvulnerable(true);
    this.getInventory().clear();
    if (TrapDuelServer.areKitsEnabled()) {
      this.applyKit();
    }
    Location location = TrapDuelServer.getUserSpawnPoint();
    location.getWorld().loadChunk(location.getChunk());
    this.teleport(location);
  }

  @Override
  public @Nullable ExLocation onGameRespawn() {
    return this.getExLocation();
  }
}
