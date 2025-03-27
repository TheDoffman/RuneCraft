package hoffmantv.runeCraft.combat;

import org.bukkit.Material;

public class ArmorUtils {
    public static int getRequiredDefense(Material armor) {
        switch (armor) {
            case LEATHER_HELMET:
            case LEATHER_CHESTPLATE:
            case LEATHER_LEGGINGS:
            case LEATHER_BOOTS:
                return 1;
            case CHAINMAIL_HELMET:
            case CHAINMAIL_CHESTPLATE:
            case CHAINMAIL_LEGGINGS:
            case CHAINMAIL_BOOTS:
                return 10;
            case IRON_HELMET:
            case IRON_CHESTPLATE:
            case IRON_LEGGINGS:
            case IRON_BOOTS:
                return 20;
            case GOLDEN_HELMET:
            case GOLDEN_CHESTPLATE:
            case GOLDEN_LEGGINGS:
            case GOLDEN_BOOTS:
                return 15;
            case DIAMOND_HELMET:
            case DIAMOND_CHESTPLATE:
            case DIAMOND_LEGGINGS:
            case DIAMOND_BOOTS:
                return 30;
            case NETHERITE_HELMET:
            case NETHERITE_CHESTPLATE:
            case NETHERITE_LEGGINGS:
            case NETHERITE_BOOTS:
                return 40;
            default:
                return 0;
        }
    }
}