package hoffmantv.runeCraft.mobs;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class MobSpawnRestrictionListener implements Listener {

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        // Only allow spawns with the CUSTOM reason.
        // This switch cancels the spawn if the spawn reason is one of the specified reasons.
                event.setCancelled(true);

        }
    }
