// Updated TurnBasedCombatSession.java with XP Rewards Based on Mob Killed
package hoffmantv.runeCraft.combat;

import hoffmantv.runeCraft.RuneCraft;
import hoffmantv.runeCraft.skilling.PlayerCombatStatsManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.ArmorStand;
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
        // If the attacker is a player, trigger the sword swing animation.
        if (attacker instanceof Player) {
            ((Player) attacker).swingMainHand();
        }

        double missChance = 0.2; // 20% chance to miss.
        if (random.nextDouble() < missChance) {
            if (attacker instanceof Player) {
                player.sendMessage("You swing but miss!");
            } else {
                player.sendMessage(target.getName() + " swings but misses!");
            }
            return;
        }
        // Apply base damage to the defender.
        defender.damage(baseDamage);

        // If the attacker is the player and the defender is the target, update the boss bar and show damage above head.
        if (attacker instanceof Player && defender.equals(target)) {
            showDamage(defender, baseDamage);
            updateBossBar();
        } else {
            // For attacks on the player, simply send a chat message.
            player.sendMessage(target.getName() + " hits you for " + baseDamage + " damage!");
        }
    }

    private void updateBossBar() {
        double health = target.getHealth();
        double maxHealth = target.getMaxHealth();
        double progress = health / maxHealth;
        bossBar.setProgress(Math.max(0, Math.min(progress, 1.0)));
        bossBar.setTitle(target.getName() + " Health: " + health + "/" + maxHealth);
    }

    private void showDamage(LivingEntity defender, double damage) {
        World world = defender.getWorld();
        // Position the display just above the entity's head.
        Location location = defender.getLocation().add(0, defender.getHeight() + 0.2, 0);
        // Spawn an invisible ArmorStand to display the red-colored damage.
        ArmorStand damageDisplay = world.spawn(location, ArmorStand.class, armorStand -> {
            armorStand.setVisible(false);
            armorStand.setGravity(false);
            armorStand.setCustomName(ChatColor.RED + String.valueOf(damage));
            armorStand.setCustomNameVisible(true);
            armorStand.setMarker(true);
        });

        // Remove the damage display after 1 second (20 ticks).
        new BukkitRunnable() {
            @Override
            public void run() {
                damageDisplay.remove();
            }
        }.runTaskLater(plugin, 20L);
    }

    private double calculateXPReward(LivingEntity target) {
        // A simple formula: XP reward based on target's max health multiplied by a constant.
        return target.getMaxHealth() * 5.0;
    }

    // Updated endCombat() method in TurnBasedCombatSession.java to display XP reward above the XP bar using a temporary BossBar
    public void endCombat() {
        combatActive = false;

        if (target.isDead()) {
            double xpReward = calculateXPReward(target);
            // Award XP through the skilling system
            PlayerCombatStatsManager.getStats(player).addExperience(xpReward);

            // Create a BossBar to display the XP reward at the top of the screen (above the XP bar, health, and food)
            BossBar xpRewardBar = Bukkit.createBossBar(ChatColor.GOLD + "XP Reward: " + xpReward, BarColor.GREEN, BarStyle.SOLID);
            xpRewardBar.addPlayer(player);
            xpRewardBar.setProgress(1.0);

            // Schedule removal of the XP reward bar after 3 seconds (60 ticks)
            new BukkitRunnable() {
                @Override
                public void run() {
                    xpRewardBar.removeAll();
                }
            }.runTaskLater(plugin, 60L);
        }

        // Re-enable target movement if applicable
        if (!target.isDead() && target instanceof Creature) {
            ((Creature) target).setAI(true);
        }

        // Remove the combat session BossBar if present
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