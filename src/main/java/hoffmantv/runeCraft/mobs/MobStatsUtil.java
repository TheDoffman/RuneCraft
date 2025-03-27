// MobStatsUtil.java in package hoffmantv.runeCraft.mob
package hoffmantv.runeCraft.mobs;

import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import hoffmantv.runeCraft.RuneCraft;

public class MobStatsUtil {
    /**
     * Retrieves the mob's level from its persistent data.
     * If not present, returns 1.
     */
    public static int getMobLevel(LivingEntity mob) {
        PersistentDataContainer data = mob.getPersistentDataContainer();
        Integer level = data.get(RuneCraft.getInstance().getKey("mobLevel"), PersistentDataType.INTEGER);
        return (level != null) ? level : 1;
    }

    /**
     * Sets the mob's level in its persistent data.
     */
    public static void setMobLevel(LivingEntity mob, int level) {
        mob.getPersistentDataContainer().set(RuneCraft.getInstance().getKey("mobLevel"), PersistentDataType.INTEGER, level);
    }
}