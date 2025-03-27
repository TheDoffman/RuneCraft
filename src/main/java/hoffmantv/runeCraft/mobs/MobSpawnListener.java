// Updated MobSpawnListener.java – ensure all mobs (including animals) get a colored level tag immediately on spawn.
package hoffmantv.runeCraft.mobs;

import hoffmantv.runeCraft.skilling.PlayerCombatStatsManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.Collection;
import java.util.Random;

public class MobSpawnListener implements Listener {
    private final Random random = new Random();

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        LivingEntity mob = event.getEntity();
        // Do not assign a level if the mob is a player.
        if (mob instanceof Player) return;

        // Allow level assignment for mobs if:
        //   - Spawn reason is NATURAL or SPAWN_EGG,
        //   - OR the mob is an instance of Animals (to cover animals spawned via chunk generation or other reasons).
        if (!(event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL ||
                event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER_EGG ||
                (mob instanceof Animals))) {
            return;
        }

        // Assign a level based on the type of mob.
        int level;
        if (mob instanceof Animals) {
            level = random.nextInt(10) + 1;
        } else {
            level = random.nextInt(99) + 1;
        }
        MobStatsUtil.setMobLevel(mob, level);

        // Determine color based on a nearby player's combat level if available.
        // If no nearby player is found, assume a default player level (e.g., 100) so that most mobs will appear green.
        int defaultPlayerLevel = 100;
        int playerCombatLevel = defaultPlayerLevel;
        Location mobLocation = mob.getLocation();
        Collection<Entity> nearbyEntities = mobLocation.getNearbyEntities(20, 20, 20);
        Player nearestPlayer = null;
        double nearestDistance = Double.MAX_VALUE;
        for (Entity entity : nearbyEntities) {
            if (entity instanceof Player) {
                double distance = entity.getLocation().distanceSquared(mobLocation);
                if (distance < nearestDistance) {
                    nearestDistance = distance;
                    nearestPlayer = (Player) entity;
                }
            }
        }
        if (nearestPlayer != null) {
            playerCombatLevel = PlayerCombatStatsManager.getStats(nearestPlayer).getCombatLevel();
        }

        // Determine color based on level difference.
        NamedTextColor color;
        if (level < playerCombatLevel) {
            color = NamedTextColor.GREEN;
        } else if (level <= playerCombatLevel + 5) {
            color = NamedTextColor.YELLOW;
        } else {
            color = NamedTextColor.RED;
        }

        // Set the mob's custom name to include its level with the determined color.
        mob.customName(Component.text("[Lv: " + level + "] " + mob.getName()).color(color));
        mob.setCustomNameVisible(true);
    }
}