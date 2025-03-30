package hoffmantv.runeCraft.skills.defence;

import org.bukkit.Material;

public class ArmorRequirements {

    /**
     * Returns the required Defence level to equip the specified armor.
     * Adjust these values to balance your progression.
     *
     * Example values (OSRS-inspired):
     * - Leather armor: level 1
     * - Gold armor: level 10
     * - Chainmail armor: level 15
     * - Iron armor: level 20
     * - Diamond armor: level 40
     * - Netherite armor: level 50
     */
    public static int getRequiredDefence(Material armor) {
        switch (armor) {
            case LEATHER_HELMET:
            case LEATHER_CHESTPLATE:
            case LEATHER_LEGGINGS:
            case LEATHER_BOOTS:
                return 1;
            case GOLDEN_HELMET:
            case GOLDEN_CHESTPLATE:
            case GOLDEN_LEGGINGS:
            case GOLDEN_BOOTS:
                return 10;
            case CHAINMAIL_HELMET:
            case CHAINMAIL_CHESTPLATE:
            case CHAINMAIL_LEGGINGS:
            case CHAINMAIL_BOOTS:
                return 15;
            case IRON_HELMET:
            case IRON_CHESTPLATE:
            case IRON_LEGGINGS:
            case IRON_BOOTS:
                return 20;
            case DIAMOND_HELMET:
            case DIAMOND_CHESTPLATE:
            case DIAMOND_LEGGINGS:
            case DIAMOND_BOOTS:
                return 40;
            case NETHERITE_HELMET:
            case NETHERITE_CHESTPLATE:
            case NETHERITE_LEGGINGS:
            case NETHERITE_BOOTS:
                return 50;
            default:
                return 1;
        }
    }
}