package hoffmantv.runeCraft.skills.strength;

import org.bukkit.Material;

public class SwordRequirements {

    /**
     * Returns the required Strength level to wield the given sword.
     * Adjust these values as needed for balance.
     *
     * @param sword The Material representing the sword.
     * @return The required Strength level.
     */
    public static int getRequiredStrength(Material sword) {
        switch (sword) {
            case WOODEN_SWORD:
                return 1;
            case STONE_SWORD:
                return 5;
            case GOLDEN_SWORD:
                return 7; // Golden swords might require a bit more finesse
            case IRON_SWORD:
                return 10;
            case DIAMOND_SWORD:
                return 20;
            case NETHERITE_SWORD:
                return 30;
            default:
                return 1; // Default requirement for unknown types.
        }
    }
}