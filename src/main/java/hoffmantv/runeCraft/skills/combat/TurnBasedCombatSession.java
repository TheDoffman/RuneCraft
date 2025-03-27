// Updated TurnBasedCombatSession.java – Added mob facing and player swing each tick
package hoffmantv.runeCraft.skills.combat;

import hoffmantv.runeCraft.RuneCraft;
import hoffmantv.runeCraft.mobs.MobStatsUtil;
import hoffmantv.runeCraft.mobs.MobUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class TurnBasedCombatSession {
    private final Player player;
    private final LivingEntity target;
    private final double baseDamage;
    private boolean combatActive;
    private final Random random;
    private final RuneCraft plugin;
    private BossBar bossBar; // Health bar for the mob

    public TurnBasedCombatSession(Player player, LivingEntity target, double baseDamage, RuneCraft plugin) {
        this.player = player;
        this.target = target;
        this.baseDamage = baseDamage;
        this.plugin = plugin;
        this.combatActive = true;
        this.random = new Random();
    }

    public void startCombat() {
        // Disable target movement if possible.
        if (target instanceof Creature) {
            ((Creature) target).setAI(false);
        }
        player.sendMessage("Combat initiated! You are locked in battle with " + target.getName());

        // Create a BossBar to display the target's health.
        bossBar = Bukkit.createBossBar(
                target.getName() + " Health: " + target.getHealth() + "/" + target.getMaxHealth(),
                BarColor.RED,
                BarStyle.SOLID
        );
        bossBar.addPlayer(player);
        updateBossBar();

        // Ensure mob's level is shown in its name (if applicable)
        if (!(target instanceof Player)) {
            CombatStats playerStats = PlayerCombatStatsManager.getStats(player);
            int playerCombatLevel = playerStats.getCombatLevel();
            int mobLevel = MobStatsUtil.getMobLevel(target);
            MobUtil.updateMobNameWithLevel(target, mobLevel, playerCombatLevel);
        }

        // Schedule a repeating task to simulate turn-based combat.
        new BukkitRunnable() {
            boolean playerTurn = true;
            @Override
            public void run() {
                if (!combatActive || player.isDead() || target.isDead()) {
                    endCombat();
                    cancel();
                    return;
                }
                // Make the player swing every tick.
                player.swingMainHand();

                // Make the mob face the player.
                if (!(target instanceof Player)) {
                    Location mobLoc = target.getLocation();
                    Location playerLoc = player.getLocation();
                    double dx = playerLoc.getX() - mobLoc.getX();
                    double dz = playerLoc.getZ() - mobLoc.getZ();
                    float yaw = (float) Math.toDegrees(Math.atan2(-dx, dz));
                    mobLoc.setYaw(yaw);
                    // Teleporting the mob updates its orientation.
                    target.teleport(mobLoc);
                }

                // Alternate turns between player and target.
                if (playerTurn) {
                    simulateAttack(player, target);
                } else {
                    simulateAttack(target, player);
                }
                playerTurn = !playerTurn;
            }
        }.runTaskTimer(plugin, 0L, 20L); // 20 ticks = 1 second per turn
    }

    private void simulateAttack(LivingEntity attacker, LivingEntity defender) {
        // Check if attacker is a player and defender is a mob (non-player).
        if (attacker instanceof Player && !(defender instanceof Player)) {
            Player attackingPlayer = (Player) attacker;
            CombatStats playerStats = PlayerCombatStatsManager.getStats(attackingPlayer);
            int playerCombatLevel = playerStats.getCombatLevel();
            int mobLevel = MobStatsUtil.getMobLevel(defender);
            // Update mob's name to show its level and proper color.
            MobUtil.updateMobNameWithLevel(defender, mobLevel, playerCombatLevel);

            double baseMissChance = 0.2;
            double damageMultiplier;
            double missChanceModifier;
            int diff = mobLevel - playerCombatLevel; // Positive if mob is higher.

            if (diff > 0) {
                // Mob is higher than player: player's attack is less effective and more likely to miss.
                damageMultiplier = 0.8;
                missChanceModifier = diff * 0.02; // Increase miss chance per level difference.
            } else if (diff < 0) {
                // Player is higher than mob: player's attack is more effective and less likely to miss.
                damageMultiplier = 1.2;
                missChanceModifier = -0.05; // Lower miss chance.
            } else {
                damageMultiplier = 1.0;
                missChanceModifier = 0.0;
            }
            double finalDamage = baseDamage * damageMultiplier;
            double finalMissChance = baseMissChance + missChanceModifier;
            if (random.nextDouble() < finalMissChance) {
                attackingPlayer.sendMessage("You swing but miss!");
                return;
            }
            defender.damage(finalDamage);
            if (defender.equals(target)) {
                showDamage(defender, finalDamage);
                updateBossBar();
            }
        }
        else if (attacker instanceof LivingEntity && defender instanceof Player) {
            Player defendingPlayer = (Player) defender;
            // Retrieve player's combat stats.
            CombatStats playerStats = PlayerCombatStatsManager.getStats(defendingPlayer);
            int playerCombatLevel = playerStats.getCombatLevel();
            int mobLevel = MobStatsUtil.getMobLevel(attacker);
            int diff = mobLevel - playerCombatLevel; // Positive if mob is higher.

            double baseMissChance = 0.2;
            double damageMultiplier;
            double missChanceModifier;
            if (diff > 0) {
                // Mob is higher than player: mob's attack is stronger and more likely to hit.
                damageMultiplier = 1.2;
                missChanceModifier = -0.05; // Lower miss chance.
            } else if (diff < 0) {
                // Mob is lower than player: mob's attack is weaker.
                damageMultiplier = 0.8;
                missChanceModifier = (-diff) * 0.02; // Increase miss chance.
            } else {
                damageMultiplier = 1.0;
                missChanceModifier = 0.0;
            }
            double finalDamage = baseDamage * damageMultiplier;
            double finalMissChance = baseMissChance + missChanceModifier;
            if (random.nextDouble() < finalMissChance) {
                defendingPlayer.sendMessage(attacker.getName() + " swings but misses!");
                return;
            }
            defender.damage(finalDamage);
            defendingPlayer.sendMessage(attacker.getName() + " hits you for " + finalDamage + " damage!");
        }
        else {
            // Default behavior if none of the above conditions apply.
            double missChance = 0.2;
            if (random.nextDouble() < missChance) {
                if (attacker instanceof Player) {
                    ((Player) attacker).sendMessage("You swing but miss!");
                }
                return;
            }
            defender.damage(baseDamage);
            if (attacker instanceof Player && defender.equals(target)) {
                showDamage(defender, baseDamage);
                updateBossBar();
            }
        }
    }

    private void updateBossBar() {
        double health = target.getHealth();
        double maxHealth = target.getMaxHealth();
        double progress = health / maxHealth;
        bossBar.setProgress(Math.max(0, Math.min(progress, 1.0)));
        // Show whole numbers for health.
        bossBar.setTitle(target.getName() + " Health: " + (int) health + "/" + (int) maxHealth);
    }

    private void showDamage(LivingEntity defender, double damage) {
        World world = defender.getWorld();
        // Position the display just above the entity's head.
        Location location = defender.getLocation().add(0, defender.getHeight() + 0.2, 0);
        // Spawn an invisible ArmorStand to display the damage.
        var damageDisplay = world.spawn(location, org.bukkit.entity.ArmorStand.class, armorStand -> {
            armorStand.setVisible(false);
            armorStand.setGravity(false);
            armorStand.setCustomName(ChatColor.RED + String.valueOf(damage));
            armorStand.setCustomNameVisible(true);
            armorStand.setMarker(true);
        });
        new BukkitRunnable() {
            @Override
            public void run() {
                damageDisplay.remove();
            }
        }.runTaskLater(plugin, 20L);
    }

    private double calculateXPReward(LivingEntity target) {
        // XP reward based on target's max health.
        return target.getMaxHealth() * 5.0;
    }

    public void endCombat() {
        combatActive = false;
        if (target.isDead()) {
            double xpReward = calculateXPReward(target);
            var stats = PlayerCombatStatsManager.getStats(player);
            stats.addKillExperience(xpReward, player);
            stats.save(player);
            hoffmantv.runeCraft.skills.PlayerSkillDataManager.saveData(plugin);
            var xpRewardBar = Bukkit.createBossBar(
                    ChatColor.GOLD + "XP Reward: " + xpReward,
                    BarColor.GREEN,
                    BarStyle.SOLID
            );
            xpRewardBar.addPlayer(player);
            xpRewardBar.setProgress(1.0);
            new BukkitRunnable() {
                @Override
                public void run() {
                    xpRewardBar.removeAll();
                }
            }.runTaskLater(plugin, 60L);
        }
        if (!target.isDead() && target instanceof Creature) {
            ((Creature) target).setAI(true);
        }
        if (bossBar != null) {
            bossBar.removeAll();
        }
        player.sendMessage("Combat session ended.");
        TurnBasedCombatManager.endSession(player);
    }

    public boolean isActive() {
        return combatActive;
    }
}