/*
 * game-trapduel.main
 * Copyright (C) 2022 timesnake
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; If not, see <http://www.gnu.org/licenses/>.
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
