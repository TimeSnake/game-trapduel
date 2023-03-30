/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.game.trapduel.main;

import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.ServerManager;
import de.timesnake.game.trapduel.server.TrapDuelServerManager;
import de.timesnake.library.basic.util.Loggers;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class GameTrapDuel extends JavaPlugin {

    public static GameTrapDuel plugin;

    @Override
    public void onLoad() {
        ServerManager.setInstance(new TrapDuelServerManager());
    }

    @Override
    public void onEnable() {

        if (Server.getTask().startsWith("trapduel")) {
            Loggers.GAME.info("Loading TrapDuel ...");
            GameTrapDuel.plugin = this;

            PluginManager pm = Bukkit.getPluginManager();
            pm.registerEvents(TrapDuelServerManager.getInstance(), this);

            TrapDuelServerManager.getInstance().onTrapDuelEnable();

            Loggers.GAME.info("TrapDuel loaded successfully");
        }
    }

    public static Plugin getPlugin() {
        return plugin;
    }

}
