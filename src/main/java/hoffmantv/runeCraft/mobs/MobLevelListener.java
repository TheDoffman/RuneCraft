package hoffmantv.runeCraft.mobs;

import hoffmantv.runeCraft.RuneCraft;
import org.bukkit.attribute.Attribute;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Random;

public class MobLevelListener implements Listener {

    private final Random random = new Random();

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        // Process only living entities (exclude players).
        if (!(event.getEntity() instanceof LivingEntity)) return;
        if (event.getEntity() instanceof Player) return;

        LivingEntity mob = (LivingEntity) event.getEntity();

        int level;
        // Lock cows to level 10, chickens to level 3, otherwise assign a random level between 1 and 99.
        if (mob.getType() == EntityType.COW) {
            level = 10;
        } else if (mob.getType() == EntityType.CHICKEN) {
            level = 3;
        } else {
            level = random.nextInt(99) + 1;
        }

        MobLevelData levelData = new MobLevelData(level);

        // Store the level data as metadata on the mob.
        mob.setMetadata("mobLevelData", new FixedMetadataValue(RuneCraft.getInstance(), levelData));

        // Adjust the mob's maximum health using the health multiplier.
        double baseHealth = mob.getAttribute(Attribute.MAX_HEALTH).getBaseValue();
        double newMaxHealth = baseHealth * levelData.getHealthMultiplier();
        mob.getAttribute(Attribute.MAX_HEALTH).setBaseValue(newMaxHealth);
        mob.setHealth(newMaxHealth);

        // Set a custom name so the mob's level is visible above its head.
        mob.setCustomName(ChatColor.GRAY + "Lv: " + levelData.getLevel());
        mob.setCustomNameVisible(true);
    }
}