package hoffmantv.runeCraft.skills.smelting;

import org.bukkit.Material;

public class SmeltingRequirements {

    /**
     * Returns the required smelting level for the given raw material.
     * Adjust these values as needed.
     *
     * @param rawMaterial The raw material to be smelted.
     * @return The required smelting level.
     */
    public static int getRequiredLevel(Material rawMaterial) {
        switch (rawMaterial) {
            case IRON_ORE:
                return 1;
            case GOLD_ORE:
                return 10;
            case NETHER_QUARTZ_ORE:
                return 1;
            case COAL:
                return 1;
            case DIAMOND_ORE:
                return 20;
            case COPPER_ORE:
                return 1;
            case REDSTONE_ORE:
                return 5;
            case EMERALD_ORE:
                return 15;
            case LAPIS_ORE:
                return 5;
            default:
                if (rawMaterial.name().endsWith("_ORE")) {
                    return 1;
                }
                return 1;
        }
    }

    /**
     * Returns the smelted result of the given raw material.
     * Explicit ore types are handled first.
     * For ores not explicitly handled, this method attempts to replace "_ORE" with "_INGOT".
     *
     * @param rawMaterial The raw material to be smelted.
     * @return The resulting Material after smelting, or the raw material if no conversion is found.
     */
    public static Material getSmeltedResult(Material rawMaterial) {
        switch (rawMaterial) {
            case IRON_ORE:
                return Material.IRON_INGOT;
            case GOLD_ORE:
                return Material.GOLD_INGOT;
            case NETHER_QUARTZ_ORE:
                return Material.QUARTZ;
            case COAL:
                // Return PLAYER_HEAD for coal so a custom texture can be applied.
                return Material.PLAYER_HEAD;
            case DIAMOND_ORE:
                // Custom output for diamond ore.
                return Material.DIAMOND;
            case COPPER_ORE:
                return Material.COPPER_INGOT;
            case REDSTONE_ORE:
                return Material.REDSTONE;
            case EMERALD_ORE:
                return Material.EMERALD;
            case LAPIS_ORE:
                return Material.LAPIS_LAZULI;
            default:
                if (rawMaterial.name().endsWith("_ORE")) {
                    String ingotName = rawMaterial.name().replace("_ORE", "_INGOT");
                    try {
                        return Material.valueOf(ingotName);
                    } catch (IllegalArgumentException e) {
                        return rawMaterial;
                    }
                }
                return null;
        }
    }

    /**
     * Returns the XP reward for smelting the given raw material.
     *
     * @param rawMaterial The raw material that was smelted.
     * @return The XP reward for smelting this material.
     */
    public static double getXpReward(Material rawMaterial) {
        switch (rawMaterial) {
            case IRON_ORE:
                return 10.0;
            case GOLD_ORE:
                return 15.0;
            case NETHER_QUARTZ_ORE:
                return 5.0;
            case COAL:
                return 4.0;
            case DIAMOND_ORE:
                return 20.0;
            case COPPER_ORE:
                return 7.0;
            case REDSTONE_ORE:
                return 8.0;
            case EMERALD_ORE:
                return 12.0;
            case LAPIS_ORE:
                return 6.0;
            default:
                if (rawMaterial.name().endsWith("_ORE")) {
                    return 8.0;
                }
                return 0;
        }
    }
}