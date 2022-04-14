package de.timesnake.game.trapduel.server;

import de.timesnake.basic.bukkit.util.user.ExItemStack;
import de.timesnake.basic.loungebridge.util.user.Kit;
import de.timesnake.library.basic.util.Tuple;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TrapDuelKit extends Kit {

    public static final TrapDuelKit SPARKY = new TrapDuelKit(1, "Sparky", Material.REDSTONE,
            List.of("", "§6§lItems:", "§f 32 Redstone", "§f 2 Sticky Piston", "§f 2 Repeater", "§f 2 Dispenser", "§f " +
                    "1 Observer", "§f 32 Apple"),
            List.of(new ItemStack(Material.REDSTONE, 32), new ItemStack(Material.STICKY_PISTON, 2),
                    new ItemStack(Material.REPEATER, 2), new ItemStack(Material.DISPENSER, 2),
                    new ItemStack(Material.OBSERVER, 1), new ItemStack(Material.APPLE, 32)));

    public static final TrapDuelKit BUILDER = new TrapDuelKit(2, "Builder", Material.STONE,
            List.of("", "§6§lItems", "§f 64 Sand", "§f 4 Slime Block", "§f 1 Water Bucket", "§f 1 Diamond shovel",
                    "§f 32 Apple"),
            List.of(new ItemStack(Material.SAND, 64), new ItemStack(Material.SLIME_BLOCK, 4),
                    new ItemStack(Material.WATER_BUCKET, 1), new ItemStack(Material.DIAMOND_SHOVEL, 1),
                    new ItemStack(Material.APPLE, 32)));

    public static final TrapDuelKit MINER = new TrapDuelKit(3, "Miner", Material.IRON_PICKAXE,
            List.of("", "§6§lItems:", "§f 1 Diamond Pickax", "§f 1 Lava Bucket", "§f 1 Furnace", "§f 2 Bedrock", "§f " +
                    "32 Apple"),
            List.of(new ItemStack(Material.DIAMOND_PICKAXE, 1), new ItemStack(Material.LAVA_BUCKET, 1),
                    new ItemStack(Material.FURNACE, 1), new ItemStack(Material.BEDROCK, 1),
                    new ItemStack(Material.APPLE, 32)));

    public static final TrapDuelKit DIVER = new TrapDuelKit(4, "Diver", Material.WATER_BUCKET,
            List.of("", "§6§lItems:", "§f 1 Leather Helmet", "§7  Unbreakable", "§7  Aqua Affinity",
                    "§7  Depth Strider 3", "§7  Respiration 3", "§f 5 Guardian Spawn Egg", "§f 32 Apple"),
            List.of(new ExItemStack(Material.LEATHER_HELMET).unbreakable().addEnchantments(
                            new Tuple<>(Enchantment.OXYGEN, 3), new Tuple<>(Enchantment.DEPTH_STRIDER, 3),
                            new Tuple<>(Enchantment.WATER_WORKER, 1)), new ItemStack(Material.GUARDIAN_SPAWN_EGG, 5),
                    new ItemStack(Material.APPLE, 32)));

    public static final TrapDuelKit HOT_DIVER = new TrapDuelKit(5, "Hot Diver", Material.LAVA_BUCKET,
            List.of("", "§6§lItems:", "§f 1 Leather Leggings", "§7  Unbreakable", "§7  Fire Protection 3", "§f 2 Lava" +
                    " Bucket", "§f 32 Apple"),
            List.of(new ExItemStack(Material.LEATHER_LEGGINGS).unbreakable().addEnchantments(new Tuple<>(Enchantment.PROTECTION_FIRE, 3)),
                    new ItemStack(Material.LAVA_BUCKET, 1), new ItemStack(Material.LAVA_BUCKET, 1),
                    new ItemStack(Material.APPLE, 32)));

    public static final TrapDuelKit BOMBER = new TrapDuelKit(6, "Bomber", Material.TNT,
            List.of("", "§6§lItems:", "§f 16 TNT", "§f 32 Apple"), List.of(new ItemStack(Material.TNT, 16),
            new ItemStack(Material.APPLE, 32)));

    public static final TrapDuelKit SANDMAN = new TrapDuelKit(7, "Sandman", Material.SAND,
            List.of("", "§6§lItems:", "§f 32 Sand", "§f 64 Glass", "§f 32 Apple"),
            List.of(new ItemStack(Material.SAND, 64), new ItemStack(Material.GLASS, 64), new ItemStack(Material.APPLE
                    , 32)));

    public static final TrapDuelKit LUMBERJACK = new TrapDuelKit(8, "Lumberjack", Material.IRON_AXE,
            List.of("", "§6§lItems:", "§f 48 Oak Wood", "§f 1 Iron Axe", "§f 32 Apple"),
            List.of(new ItemStack(Material.OAK_WOOD, 48), new ItemStack(Material.IRON_AXE),
                    new ItemStack(Material.APPLE, 32)));

    public static final TrapDuelKit SNOWMAN = new TrapDuelKit(9, "Snowman", Material.SNOW_BLOCK,
            List.of("", "§6§lItems:", "§f 64 Snowblock", "§f 32 Carved-Pumpkin", "§f 16 Snowball", "§f 32 Apple"),
            List.of(new ItemStack(Material.SNOW_BLOCK, 64), new ItemStack(Material.CARVED_PUMPKIN, 32),
                    new ItemStack(Material.SNOWBALL, 16), new ItemStack(Material.APPLE, 32)));

    public static final TrapDuelKit SPIDERMAN = new TrapDuelKit(11, "Spiderman", Material.COBWEB,
            List.of("", "§6§lItems:", "§f 16 Cobweb", "§f 8 Cavespider-Spawnegg", "§f 32 Apple"),
            List.of(new ItemStack(Material.COBWEB, 16), new ItemStack(Material.CAVE_SPIDER_SPAWN_EGG, 8),
                    new ItemStack(Material.APPLE, 32)));

    public static final TrapDuelKit ENDERMAN = new TrapDuelKit(12, "Enderman", Material.ENDER_PEARL,
            List.of("", "§6§lItems:", "§f 16 Ender-Pearl", "§f 32 Apple"),
            List.of(new ItemStack(Material.ENDER_PEARL, 16), new ItemStack(Material.APPLE, 32)));

    public static final TrapDuelKit[] KITS = {SPARKY, BUILDER, MINER, DIVER, HOT_DIVER, BOMBER, SANDMAN, LUMBERJACK,
            SNOWMAN, SPIDERMAN, ENDERMAN};

    private TrapDuelKit(int id, String name, Material material, List<String> description, List<ItemStack> items) {
        super(id, name, material, description, items);
    }
}
