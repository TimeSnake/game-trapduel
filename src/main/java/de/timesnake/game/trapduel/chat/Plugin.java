/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.game.trapduel.chat;

import de.timesnake.library.basic.util.LogHelper;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Plugin extends de.timesnake.basic.bukkit.util.chat.Plugin {

  public static final Plugin TRAP_DUEL = new Plugin("TrapDuel", "TRD",
      LogHelper.getLogger("TrapDuel", Level.INFO));

  protected Plugin(String name, String code, Logger logger) {
    super(name, code, logger);
  }
}
