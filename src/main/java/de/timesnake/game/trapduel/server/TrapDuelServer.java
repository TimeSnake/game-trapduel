package de.timesnake.game.trapduel.server;

import de.timesnake.basic.loungebridge.util.server.LoungeBridgeServer;
import org.bukkit.Location;

public class TrapDuelServer extends LoungeBridgeServer {

    private static final TrapDuelServerManager server = TrapDuelServerManager.getInstance();

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

    public static void stopWithWin() {
        server.stopWithWin();
    }

}
