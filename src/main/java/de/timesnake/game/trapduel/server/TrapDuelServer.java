/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.game.trapduel.server;

import de.timesnake.basic.loungebridge.util.server.LoungeBridgeServer;
import org.bukkit.Location;

public class TrapDuelServer extends LoungeBridgeServer {

  public static Location getUserSpawnPoint() {
    return server.getUserSpawnPoint();
  }

  private static final TrapDuelServerManager server = TrapDuelServerManager.getInstance();


}
