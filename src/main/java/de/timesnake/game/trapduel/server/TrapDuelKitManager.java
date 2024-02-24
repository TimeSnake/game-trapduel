/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.game.trapduel.server;

import de.timesnake.basic.bukkit.util.user.inventory.ExItemStack;
import de.timesnake.basic.loungebridge.util.user.Kit;
import de.timesnake.basic.loungebridge.util.user.KitManager;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.List;

public class TrapDuelKitManager extends KitManager<Kit> {

  public static final Kit SPARKY = new Builder()
      .id(1)
      .name("Sparky")
      .material(Material.REDSTONE)
      .addDescription("", "§6§lItems:", "§f 32 Redstone", "§f 2 Sticky Piston",
          "§f 2 Repeater", "§f 2 Dispenser", "§f 1 Observer", "§f 32 Apple")
      .addItems(new ItemStack(Material.REDSTONE, 32),
          new ItemStack(Material.STICKY_PISTON, 2),
          new ItemStack(Material.REPEATER, 2),
          new ItemStack(Material.DISPENSER, 2),
          new ItemStack(Material.OBSERVER, 1),
          new ItemStack(Material.APPLE, 32))
      .build();

  public static final Kit BUILDER = new Builder()
      .id(2)
      .name("Builder")
      .material(Material.STONE)
      .addDescription("", "§6§lItems", "§f 64 Sand", "§f 4 Slime Block",
          "§f 1 Water Bucket", "§f 1 Diamond shovel", "§f 32 Apple")
      .addItems(new ItemStack(Material.SAND, 64),
          new ItemStack(Material.SLIME_BLOCK, 4),
          new ItemStack(Material.WATER_BUCKET, 1),
          new ItemStack(Material.DIAMOND_SHOVEL, 1),
          new ItemStack(Material.APPLE, 32))
      .build();

  public static final Kit MINER = new Builder()
      .id(3)
      .name("Miner")
      .material(Material.IRON_PICKAXE)
      .addDescription("", "§6§lItems:", "§f 1 Diamond Pickax",
          "§f 1 Lava Bucket", "§f 1 Furnace", "§f 2 Bedrock", "§f 32 Apple")
      .addItems(new ItemStack(Material.DIAMOND_PICKAXE, 1),
          new ItemStack(Material.LAVA_BUCKET, 1),
          new ItemStack(Material.FURNACE, 1),
          new ItemStack(Material.BEDROCK, 1),
          new ItemStack(Material.APPLE, 32))
      .build();

  public static final Kit DIVER = new Builder()
      .id(4)
      .name("Diver")
      .material(Material.WATER_BUCKET)
      .addDescription("", "§6§lItems:", "§f 1 Leather Helmet", "§7  Unbreakable",
          "§7  Aqua Affinity", "§7  Depth Strider 3", "§7  Respiration 3",
          "§f 5 Guardian Spawn Egg", "§f 32 Apple")
      .addItems(new ExItemStack(Material.LEATHER_HELMET).unbreakable()
              .addExEnchantment(Enchantment.OXYGEN, 3)
              .addExEnchantment(Enchantment.DEPTH_STRIDER, 3)
              .addExEnchantment(Enchantment.WATER_WORKER, 1),
          new ItemStack(Material.GUARDIAN_SPAWN_EGG, 5),
          new ItemStack(Material.APPLE, 32))
      .build();

  public static final Kit HOT_DIVER = new Builder()
      .id(5)
      .name("Hot Diver")
      .material(Material.LAVA_BUCKET)
      .addDescription("", "§6§lItems:", "§f 1 Leather Leggings", "§7  Unbreakable",
          "§7  Fire Protection 3", "§f 2 Lava Bucket", "§f 32 Apple")
      .addItems(new ExItemStack(Material.LEATHER_LEGGINGS).unbreakable()
              .addExEnchantment(Enchantment.PROTECTION_FIRE, 3),
          new ItemStack(Material.LAVA_BUCKET, 1),
          new ItemStack(Material.LAVA_BUCKET, 1),
          new ItemStack(Material.APPLE, 32))
      .build();

  public static final Kit BOMBER = new Builder()
      .id(6)
      .name("Bomber")
      .material(Material.TNT)
      .addDescription("", "§6§lItems:", "§f 16 TNT", "§f 32 Apple")
      .addItems(new ItemStack(Material.TNT, 16),
          new ItemStack(Material.APPLE, 32))
      .build();

  public static final Kit SANDMAN = new Builder()
      .id(7)
      .name("Sandman")
      .material(Material.SAND)
      .addDescription("", "§6§lItems:", "§f 32 Sand", "§f 64 Glass", "§f 32 Apple")
      .addItems(new ItemStack(Material.SAND, 64),
          new ItemStack(Material.GLASS, 64),
          new ItemStack(Material.APPLE, 32))
      .build();

  public static final Kit LUMBERJACK = new Builder()
      .id(8)
      .name("Lumberjack")
      .material(Material.IRON_AXE)
      .addDescription("", "§6§lItems:", "§f 48 Oak Wood", "§f 1 Iron Axe", "§f 32 Apple")
      .addItems(new ItemStack(Material.OAK_WOOD, 48),
          new ItemStack(Material.IRON_AXE),
          new ItemStack(Material.APPLE, 32))
      .build();

  public static final Kit SNOWMAN = new Builder()
      .id(9)
      .name("Snowman")
      .material(Material.SNOW_BLOCK)
      .addDescription("", "§6§lItems:", "§f 64 Snowblock", "§f 32 Carved-Pumpkin",
          "§f 16 Snowball",
          "§f 32 Apple")
      .addItems(new ItemStack(Material.SNOW_BLOCK, 64),
          new ItemStack(Material.CARVED_PUMPKIN, 32),
          new ItemStack(Material.SNOWBALL, 16),
          new ItemStack(Material.APPLE, 32))
      .build();

  public static final Kit SPIDERMAN = new Builder()
      .id(11)
      .name("Spiderman")
      .material(Material.COBWEB)
      .addDescription("", "§6§lItems:", "§f 16 Cobweb", "§f 8 Cavespider-Spawnegg",
          "§f 32 Apple")
      .addItems(new ItemStack(Material.COBWEB, 16),
          new ItemStack(Material.CAVE_SPIDER_SPAWN_EGG, 8),
          new ItemStack(Material.APPLE, 32))
      .build();

  public static final Kit ENDERMAN = new Builder()
      .id(12)
      .name("Enderman")
      .material(Material.ENDER_PEARL)
      .addDescription("", "§6§lItems:", "§f 16 Ender-Pearl", "§f 32 Apple")
      .addItems(new ItemStack(Material.ENDER_PEARL, 16),
          new ItemStack(Material.APPLE, 32))
      .build();

  public static final List<Kit> KITS = List.of(SPARKY, BUILDER, MINER, DIVER, HOT_DIVER, BOMBER,
      SANDMAN,
      LUMBERJACK, SNOWMAN, SPIDERMAN, ENDERMAN);

  public TrapDuelKitManager() {
    super(false);
  }

  @Override
  public Collection<Kit> getKits() {
    return KITS;
  }

  public static class Builder extends Kit.Builder<Builder> {

  }
}
