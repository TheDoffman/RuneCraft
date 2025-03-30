package hoffmantv.runeCraft.mobs;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class MobSpawnRestrictionListener implements Listener {

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        String spawnReason = event.getSpawnReason().toString();
        // Cancel spawn if the spawn reason is one of the natural ones.
        if (spawnReason.equals("NATURAL") ||
                spawnReason.equals("BREEDING") ||
                spawnReason.equals("DISPENSED") ||
                spawnReason.equals("SPAWNER") ||
                spawnReason.equals("CHUNK_GENERATION") ||
                spawnReason.equals("CHUNK_GEN")) {
            event.setCancelled(true);
        }
    }
    }
