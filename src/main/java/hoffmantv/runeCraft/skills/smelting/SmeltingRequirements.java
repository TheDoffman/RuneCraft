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
                return 15;
            case GOLD_ORE:
                return 40;
            case NETHER_QUARTZ_ORE:
                return 20;
            case COAL:
                return 1;
            case DIAMOND_ORE:
                return 85;
            case COPPER_ORE:
                return 30;
            case REDSTONE_ORE:
                return 50;
            case EMERALD_ORE:
                return 1;
            case LAPIS_ORE:
                return 70;
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
                return null;
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
                return 12.5;
            case GOLD_ORE:
                return 22.5;
            case NETHER_QUARTZ_ORE:
                return 13.7;
            case COAL:
                return 6.2;
            case DIAMOND_ORE:
                return 50.0;
            case COPPER_ORE:
                return 17.5;
            case REDSTONE_ORE:
                return 30.0;
            case LAPIS_ORE:
                return 37.5;
            default:
                if (rawMaterial.name().endsWith("_ORE")) {
                    return 0;
                }
                return 0;
        }
    }
}