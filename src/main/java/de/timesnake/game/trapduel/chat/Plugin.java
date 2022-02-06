package de.timesnake.game.trapduel.chat;

public class Plugin extends de.timesnake.basic.bukkit.util.chat.Plugin {

    public static final Plugin TRAP_DUEL = new Plugin("TrapDuel", "TRD");

    protected Plugin(String name, String code) {
        super(name, code);
    }
}
