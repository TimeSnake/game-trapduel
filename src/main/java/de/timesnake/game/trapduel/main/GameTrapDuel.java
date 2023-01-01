/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.game.trapduel.main;

import de.timesnake.basic.bukkit.util.Server;
import de.timesnake.basic.bukkit.util.ServerManager;
import de.timesnake.game.trapduel.server.TrapDuelServerManager;
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
            Server.printText(de.timesnake.game.trapduel.chat.Plugin.TRAP_DUEL, "Loading TrapDuel ...");
            GameTrapDuel.plugin = this;

            PluginManager pm = Bukkit.getPluginManager();
            pm.registerEvents(TrapDuelServerManager.getInstance(), this);

            TrapDuelServerManager.getInstance().onTrapDuelEnable();

            Server.printText(de.timesnake.game.trapduel.chat.Plugin.TRAP_DUEL, "TrapDuel loaded " + "successfully");
        }
    }

    public static Plugin getPlugin() {
        return plugin;
    }

}
