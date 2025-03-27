package hoffmantv.runeCraft.skills.combat;

import org.bukkit.Material;

public class SwordUtils {
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
                return 1.0; // fallback for unexpected items
        }
    }

    public static int getRequiredStrength(Material sword) {
        switch (sword) {
            case WOODEN_SWORD:
                return 1;
            case STONE_SWORD:
                return 10;
            case IRON_SWORD:
                return 20;
            case GOLDEN_SWORD:
                return 15;
            case DIAMOND_SWORD:
                return 30;
            case NETHERITE_SWORD:
                return 40;
            default:
                return 0;
        }
    }
}