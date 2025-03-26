// SwordUtils.java
package hoffmantv.runeCraft.combat;

import org.bukkit.Material;

public class SwordUtils {
    // Returns the base damage for a given sword type.
    public static double getSwordDamage(Material sword) {
        switch (sword) {
            case WOODEN_SWORD:
                return 4.0;
            case STONE_SWORD:
                return 5.0;
            case IRON_SWORD:
                return 6.0;
            case GOLDEN_SWORD:
                return 5.5;
            case DIAMOND_SWORD:
                return 7.0;
            case NETHERITE_SWORD:
                return 8.0;
            default:
                return 1.0; // Fallback damage for unexpected items.
        }
    }
}