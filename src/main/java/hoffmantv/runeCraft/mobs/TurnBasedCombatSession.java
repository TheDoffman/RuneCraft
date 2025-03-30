package hoffmantv.runeCraft.mobs;

import hoffmantv.runeCraft.RuneCraft;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TurnBasedCombatSession extends BukkitRunnable {

    private final Player player;
    private final LivingEntity mob;
    private int turn = 0;
    // Maximum allowed distance between player and mob (squared) during combat.
    private final double maxDistanceSquared = 64; // 8-block radius

    public TurnBasedCombatSession(Player player, LivingEntity mob) {
        this.player = player;
        this.mob = mob;
        // Optionally lock player movement here.
        player.sendMessage(ChatColor.GRAY + "You have entered turn-based combat!");
    }

    @Override
    public void run() {
        // End combat if either participant is dead or the player moves too far away.
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
    }

    private void performPlayerTurn() {
        int damage = calculatePlayerDamage();
        double mobHealth = mob.getHealth();
        mob.setHealth(Math.max(0, mobHealth - damage));
        player.sendMessage(ChatColor.GREEN + "You strike the mob for " + damage + " damage!");
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

    // Placeholder: Calculate player's damage based on strength, weapon, etc.
    private int calculatePlayerDamage() {
        return 5 + (int)(Math.random() * 5); // Damage between 5 and 9.
    }

    // Placeholder: Calculate mob's damage.
    private int calculateMobDamage() {
        return 3 + (int)(Math.random() * 4); // Damage between 3 and 6.
    }

    private void endCombat() {
        player.sendMessage(ChatColor.GRAY + "Combat has ended.");
        // Optionally unlock movement, clear combat state, etc.
    }
}