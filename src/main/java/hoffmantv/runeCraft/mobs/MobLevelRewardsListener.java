package hoffmantv.runeCraft.mobs;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class MobLevelRewardsListener implements Listener {
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (!entity.hasMetadata("mobLevelData")) {
            return;
        }
        Object metaValue = entity.getMetadata("mobLevelData").get(0).value();
        if (!(metaValue instanceof MobLevelData data)) {
            return;
        }
        double multiplier = 1.0 + (data.getLevel() / 50.0);
        if (data.isElite()) {
            multiplier *= 1.5;
        }
        int baseExp = event.getDroppedExp();
        event.setDroppedExp((int) Math.max(1, Math.round(baseExp * multiplier)));
    }
}
