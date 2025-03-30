package hoffmantv.runeCraft.mobs;

import hoffmantv.runeCraft.RuneCraft;
import hoffmantv.runeCraft.skills.attack.AttackStats;
import hoffmantv.runeCraft.skills.attack.AttackStatsManager;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.BarColor;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class TurnBasedCombatSession extends BukkitRunnable {

    private final Player player;
    private final LivingEntity mob;
    private int turn = 0;
    // Maximum allowed distance (squared) between player and mob.
    private final double maxDistanceSquared = 64; // 8 blocks radius
    private final BossBar mobBar;

    // Static map to track active combat sessions per player.
    private static final Map<String, TurnBasedCombatSession> activeSessions = new HashMap<>();

    public TurnBasedCombatSession(Player player, LivingEntity mob) {
        this.player = player;
        this.mob = mob;
        mobBar = Bukkit.createBossBar(ChatColor.RED + "Mob Health", BarColor.RED, BarStyle.SOLID);
        mobBar.addPlayer(player);
        updateBossBar();
        player.sendMessage(ChatColor.GRAY + "You have entered turn-based combat!");
        activeSessions.put(player.getUniqueId().toString(), this);
    }

    public static boolean isInCombat(Player player) {
        return activeSessions.containsKey(player.getUniqueId().toString());
    }

    @Override
    public void run() {
        // End combat if either participant is dead or the distance is too great.
        if (player.isDead() || mob.isDead() ||
                player.getLocation().distanceSquared(mob.getLocation()) > maxDistanceSquared) {
            endCombat();
            cancel();
            return;
        }

        turn++;
        if (turn % 2 == 1) {
            performPlayerTurn();
        } else {
            performMobTurn();
        }

        // Update the boss bar after each turn.
        updateBossBar();
    }

    private void performPlayerTurn() {
        int damage = calculatePlayerDamage();

        // Apply damage to the mob.
        double mobHealth = mob.getHealth();
        mob.setHealth(Math.max(0, mobHealth - damage));
        player.sendMessage(ChatColor.GREEN + "You strike the mob for " + damage + " damage!");

        // Display damage numbers.
        DamageNumberUtil.spawnDamageNumber(mob, damage, RuneCraft.getInstance());

        // Award Attack XP.
        AttackStats attackStats = AttackStatsManager.getStats(player);
        if (attackStats != null) {
            attackStats.addExperience(damage, player);
        } else {
            player.sendMessage(ChatColor.RED + "Attack stats not loaded!");
        }

        if (mob.getHealth() <= 0) {
            player.sendMessage(ChatColor.GOLD + "You have defeated the mob!");
            endCombat();
            cancel();
        }
    }

    private void performMobTurn() {
        int damage = calculateMobDamage();
        double playerHealth = player.getHealth();
        player.setHealth(Math.max(0, playerHealth - damage));
        player.sendMessage(ChatColor.RED + "The mob attacks you for " + damage + " damage!");
        if (player.getHealth() <= 0) {
            player.sendMessage(ChatColor.DARK_RED + "You have been defeated by the mob!");
            endCombat();
            cancel();
        }
    }

    // Placeholder: Calculate player's damage.
    private int calculatePlayerDamage() {
        // For example: Damage between 5 and 9.
        return 5 + (int)(Math.random() * 5);
    }

    // Placeholder: Calculate mob's damage.
    private int calculateMobDamage() {
        // For example: Damage between 3 and 6.
        return 3 + (int)(Math.random() * 4);
    }

    private void updateBossBar() {
        double currentHealth = mob.getHealth();
        double maxHealth = mob.getAttribute(Attribute.MAX_HEALTH).getBaseValue();
        double progress = currentHealth / maxHealth;
        progress = Math.max(0, Math.min(progress, 1));
        mobBar.setProgress(progress);
        mobBar.setTitle(ChatColor.RED + "Mob Health: " + (int) currentHealth + "/" + (int) maxHealth);
    }

    private void endCombat() {
        player.sendMessage(ChatColor.GRAY + "Combat has ended.");
        mobBar.removeAll();
        activeSessions.remove(player.getUniqueId().toString());
    }
}