// Updated MobUtil.java in hoffmantv.runeCraft.mob
package hoffmantv.runeCraft.mobs;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.LivingEntity;

public class MobUtil {

    public static void updateMobNameWithLevel(LivingEntity mob, int mobLevel, int playerCombatLevel) {
        NamedTextColor color;
        if (mobLevel < playerCombatLevel) {
            color = NamedTextColor.GREEN;
        } else if (mobLevel <= playerCombatLevel + 5) {
            color = NamedTextColor.YELLOW;
        } else {
            color = NamedTextColor.RED;
        }

        // Retrieve the current custom name (if any) and determine the base name.
        String baseName;
        if (mob.customName() != null && PlainTextComponentSerializer.plainText().serialize(mob.customName()).startsWith("[Lv:")) {
            // Remove any existing level prefix using regex.
            baseName = PlainTextComponentSerializer.plainText().serialize(mob.customName()).replaceFirst("^\\[Lv: \\d+\\]\\s*", "");
            if (baseName.isEmpty()) {
                baseName = mob.getName();
            }
        } else {
            baseName = mob.getName();
        }

        Component newName = Component.text("[Lv: " + mobLevel + "] ").color(color)
                .append(Component.text(baseName));
        mob.customName(newName);
        mob.setCustomNameVisible(true);
    }
}