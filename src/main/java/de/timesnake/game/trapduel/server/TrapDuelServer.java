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

package de.timesnake.game.trapduel.server;

import de.timesnake.basic.loungebridge.util.server.LoungeBridgeServer;
import org.bukkit.Location;

public class TrapDuelServer extends LoungeBridgeServer {

    public static void startSwitchCountdown() {
        server.startSwitchCountdown();
    }

    public static int getNewSwitchCountdown() {
        return server.getNewSwitchCountdown();
    }

    public static double getGaussian() {
        return server.getGaussian();
    }

    public static void generateUserSpawnPoints() {
        server.generateUserSpawnPoints();
    }

    public static Location getUserSpawnPoint() {
        return server.getUserSpawnPoint();
    }

    public static int getCountdownPeace() {
        return server.getCountdownPeace();
    }

    public static boolean isCountdownPeaceRunning() {
        return server.isCountdownPeaceRunning();
    }

    public static boolean isCountdownSwitchRunning() {
        return server.isCountdownSwitchRunning();
    }

    private static final TrapDuelServerManager server = TrapDuelServerManager.getInstance();


}
