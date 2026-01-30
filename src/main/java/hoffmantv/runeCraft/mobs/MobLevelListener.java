package hoffmantv.runeCraft.mobs;

import hoffmantv.runeCraft.RuneCraft;
import hoffmantv.runeCraft.skills.combat.CombatLevelCalculator;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.metadata.FixedMetadataValue;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.Random;

public class MobLevelListener implements Listener {

    private final Random random = new Random();

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        // Process only living entities (exclude players).
        if (!(event.getEntity() instanceof LivingEntity)) return;
        if (event.getEntity() instanceof Player) return;

        LivingEntity mob = (LivingEntity) event.getEntity();

        FileConfiguration config = RuneCraft.getInstance().getConfig();
        int level = resolveLevel(mob, config);
        level = applyBiomeBonus(mob, level, config);

        if (shouldCancelHighLevelSpawn(mob, level, config)) {
            event.setCancelled(true);
            return;
        }

        boolean elite = isEliteSpawn(config);
        double healthMultiplier = computeMultiplier(level, config, "mobDefaults.healthMultiplierPerLevel", 0.02);
        double damageMultiplier = computeMultiplier(level, config, "mobDefaults.damageMultiplierPerLevel", 0.01);
        double armorMultiplier = computeMultiplier(level, config, "mobDefaults.armorMultiplierPerLevel", 0.005);
        if (elite) {
            healthMultiplier *= config.getDouble("mobLevels.elite.healthMultiplier", 1.5);
            damageMultiplier *= config.getDouble("mobLevels.elite.damageMultiplier", 1.3);
            armorMultiplier *= config.getDouble("mobLevels.elite.armorMultiplier", 1.2);
        }

        MobLevelData levelData = new MobLevelData(level, healthMultiplier, damageMultiplier, armorMultiplier, elite);

        // Store the level data as metadata on the mob.
        mob.setMetadata("mobLevelData", new FixedMetadataValue(RuneCraft.getInstance(), levelData));

        // Adjust the mob's maximum health using the health multiplier.
        AttributeInstance maxHealthAttr = mob.getAttribute(Attribute.MAX_HEALTH);
        if (maxHealthAttr != null) {
            double baseHealth = maxHealthAttr.getBaseValue();
            double newMaxHealth = baseHealth * levelData.getHealthMultiplier();
            maxHealthAttr.setBaseValue(newMaxHealth);
            mob.setHealth(newMaxHealth);
        }

        AttributeInstance attackDamageAttr = mob.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        if (attackDamageAttr != null) {
            double baseDamage = attackDamageAttr.getBaseValue();
            attackDamageAttr.setBaseValue(baseDamage * levelData.getDamageMultiplier());
        }

        AttributeInstance armorAttr = mob.getAttribute(Attribute.GENERIC_ARMOR);
        if (armorAttr != null) {
            double baseArmor = armorAttr.getBaseValue();
            armorAttr.setBaseValue(baseArmor * levelData.getArmorMultiplier());
        }

        // Set a custom name so the mob's level is visible above its head.
        ChatColor tierColor = getTierColor(level, config);
        String eliteTag = elite ? ChatColor.GOLD + "✦ " : "";
        mob.setCustomName(eliteTag + tierColor + "Lv " + levelData.getLevel());
        mob.setCustomNameVisible(true);
    }

    private int resolveLevel(LivingEntity mob, FileConfiguration config) {
        EntityType type = mob.getType();
        int minLevel = config.getInt("mobLevels.mobDefaults.minLevel", 1);
        int maxLevel = config.getInt("mobLevels.mobDefaults.maxLevel", 99);

        ConfigurationSection override = config.getConfigurationSection("mobLevels.mobOverrides." + type.name());
        if (override != null) {
            if (override.contains("fixedLevel")) {
                return clamp(override.getInt("fixedLevel", minLevel), 1, 99);
            }
            minLevel = override.getInt("minLevel", minLevel);
            maxLevel = override.getInt("maxLevel", maxLevel);
        }

        int level = random.nextInt(Math.max(1, maxLevel - minLevel + 1)) + minLevel;
        if (config.getBoolean("mobLevels.usePlayerScaling", true)) {
            Player nearest = getNearestPlayer(mob, config.getInt("mobLevels.playerRange", 24));
            if (nearest != null) {
                int variance = config.getInt("mobLevels.playerVariance", 6);
                int combat = CombatLevelCalculator.getCombatLevel(nearest);
                int scaled = combat + random.nextInt(variance * 2 + 1) - variance;
                level = clamp(scaled, minLevel, maxLevel);
            }
        }
        return clamp(level, 1, 99);
    }

    private int applyBiomeBonus(LivingEntity mob, int level, FileConfiguration config) {
        String biomeName = mob.getLocation().getBlock().getBiome().name();
        int bonus = config.getInt("mobLevels.biomeBonus." + biomeName, 0);
        return clamp(level + bonus, 1, 99);
    }

    private boolean isEliteSpawn(FileConfiguration config) {
        double chance = config.getDouble("mobLevels.elite.chance", 0.03);
        return random.nextDouble() < chance;
    }

    private double computeMultiplier(int level, FileConfiguration config, String path, double fallback) {
        double perLevel = config.getDouble("mobLevels." + path, fallback);
        return 1.0 + (level * perLevel);
    }

    private boolean shouldCancelHighLevelSpawn(LivingEntity mob, int level, FileConfiguration config) {
        int threshold = config.getInt("mobLevels.highLevelDensity.threshold", 50);
        if (level < threshold) {
            return false;
        }
        int radius = config.getInt("mobLevels.highLevelDensity.radius", 16);
        int maxCount = config.getInt("mobLevels.highLevelDensity.maxCount", 5);
        int count = 0;
        for (LivingEntity nearby : mob.getLocation().getNearbyLivingEntities(radius)) {
            if (nearby == mob) continue;
            if (nearby.hasMetadata("mobLevelData")) {
                Object metaValue = nearby.getMetadata("mobLevelData").get(0).value();
                if (metaValue instanceof MobLevelData data && data.getLevel() >= threshold) {
                    count++;
                    if (count >= maxCount) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private Player getNearestPlayer(LivingEntity mob, int range) {
        List<Player> players = mob.getWorld().getPlayers();
        Player nearest = null;
        double nearestDist = Double.MAX_VALUE;
        for (Player player : players) {
            double dist = player.getLocation().distanceSquared(mob.getLocation());
            if (dist <= range * range && dist < nearestDist) {
                nearest = player;
                nearestDist = dist;
            }
        }
        return nearest;
    }

    private ChatColor getTierColor(int level, FileConfiguration config) {
        int low = config.getInt("mobLevels.tiers.low", 9);
        int mid = config.getInt("mobLevels.tiers.mid", 29);
        int high = config.getInt("mobLevels.tiers.high", 59);
        if (level <= low) {
            return ChatColor.GRAY;
        }
        if (level <= mid) {
            return ChatColor.GREEN;
        }
        if (level <= high) {
            return ChatColor.YELLOW;
        }
        return ChatColor.RED;
    }

    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}