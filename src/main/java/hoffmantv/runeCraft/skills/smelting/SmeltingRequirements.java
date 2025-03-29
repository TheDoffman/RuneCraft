package hoffmantv.runeCraft.skills.smelting;

import org.bukkit.Material;

public class SmeltingRequirements {

    /**
     * Returns the required smelting level for the given raw material.
     * Adjust these values as needed to balance the progression.
     *
     * @param rawMaterial The raw material to be smelted.
     * @return The minimum smelting level required.
     */
    public static int getRequiredLevel(Material rawMaterial) {
        switch (rawMaterial) {
            case IRON_ORE:
                return 1;
            case GOLD_ORE:
                return 10;
            case NETHER_QUARTZ_ORE:
                return 1;
            // Add additional cases for more smeltable materials if needed.
            default:
                return 1;
        }
    }

    /**
     * Returns the smelted result of the given raw material.
     *
     * @param rawMaterial The raw material to be smelted.
     * @return The Material that results from smelting, or null if not smeltable.
     */
    public static Material getSmeltedResult(Material rawMaterial) {
        switch (rawMaterial) {
            case IRON_ORE:
                return Material.IRON_INGOT;
            case GOLD_ORE:
                return Material.GOLD_INGOT;
            case NETHER_QUARTZ_ORE:
                return Material.QUARTZ;
            // Add additional cases for more materials if needed.
            default:
                return null;
        }
    }

    /**
     * Returns the XP reward for smelting the given raw material.
     * Adjust the XP values as needed.
     *
     * @param rawMaterial The raw material that was smelted.
     * @return The amount of experience awarded.
     */
    public static double getXpReward(Material rawMaterial) {
        switch (rawMaterial) {
            case IRON_ORE:
                return 10.0;
            case GOLD_ORE:
                return 15.0;
            case NETHER_QUARTZ_ORE:
                return 5.0;
            // Additional cases can be added here.
            default:
                return 0;
        }
    }
}