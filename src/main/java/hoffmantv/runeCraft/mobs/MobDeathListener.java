// Updated MobDeathListener.java – remove the level from the bone's name.
package hoffmantv.runeCraft.mobs;

import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MobDeathListener implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        // Only handle mobs (non-player entities)
        if (!(event.getEntity() instanceof Player)) {
            LivingEntity mob = event.getEntity();
            // Clear default drops if needed.
            event.getDrops().clear();

            // Create a single bone item.
            ItemStack bone = new ItemStack(Material.BONE, 1);
            ItemMeta meta = bone.getItemMeta();

            // Determine the mob's name without the level tag.
            String mobName;
            if (mob.customName() != null) {
                // Serialize the custom name to plain text.
                String plainName = PlainTextComponentSerializer.plainText().serialize(mob.customName());
                // Remove the level prefix if present (e.g., "[Lv: 42] ").
                mobName = plainName.replaceFirst("^\\[Lv: \\d+\\]\\s*", "");
            } else {
                mobName = mob.getName();
            }
            meta.setDisplayName(mobName + " bone");
            bone.setItemMeta(meta);

            // Drop the bone.
            event.getDrops().add(bone);
        }
    }
}