package hoffmantv.runeCraft.mobs;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class DamageNumberUtil {

    public static void spawnDamageNumber(LivingEntity target, int damage, Plugin plugin) {
        // Spawn the ArmorStand at an offset: 1.5 blocks to the side and 2 blocks above the mob's head.
        Location loc = target.getLocation().clone().add(1.5, target.getHeight() + 2, 0);
        ArmorStand stand = target.getWorld().spawn(loc, ArmorStand.class, armorStand -> {
            armorStand.setVisible(false);
            armorStand.setGravity(false);
            armorStand.setMarker(true);
            // Prepend "Damage: " to clearly indicate damage.
            armorStand.setCustomName(ChatColor.RED + "Damage: " + damage);
            armorStand.setCustomNameVisible(true);
        });

        // Animate the ArmorStand: move it upward gradually over 20 ticks (1 second) then remove it.
        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks >= 20) {
                    stand.remove();
                    cancel();
                    return;
                }
                Location current = stand.getLocation();
                current.add(0, 0.1, 0);
                stand.teleport(current);
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
}