package hoffmantv.runeCraft.mobs;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class MobSpawnRestrictionListener implements Listener {

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        switch (event.getSpawnReason()) {
            case NATURAL, BREEDING, SPAWNER -> event.setCancelled(true);
            default -> {
                // Allow other spawn reasons.
            }
        }
    }
}
