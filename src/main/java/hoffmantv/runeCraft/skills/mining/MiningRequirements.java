package hoffmantv.runeCraft.skills.mining;

import org.bukkit.Material;

public class MiningRequirements {

    // Returns the required mining level for the given ore.
    public static int getRequiredLevel(Material material) {
        switch(material) {
            case COAL_ORE:
                return 1;
            case IRON_ORE:
                return 10;
            case GOLD_ORE:
                return 20;
            case REDSTONE_ORE:
                return 15;
            case LAPIS_ORE:
                return 20;
            case DIAMOND_ORE:
                return 30;
            case EMERALD_ORE:
                return 40;
            case NETHER_QUARTZ_ORE:
                return 50;
            case COPPER_ORE:
                return 60;
            default:
                return 1;
        }
    }

    // Returns the XP reward for mining the given ore.
    public static double getXpReward(Material material) {
        switch(material) {
            case COAL_ORE:
                return 5;
            case IRON_ORE:
                return 15;
            case GOLD_ORE:
                return 25;
            case REDSTONE_ORE:
                return 20;
            case LAPIS_ORE:
                return 25;
            case DIAMOND_ORE:
                return 50;
            case EMERALD_ORE:
                return 60;
            case NETHER_QUARTZ_ORE:
                return 7;
            case COPPER_ORE:
                return 13;
            default:
                return 1;
        }
    }
    /**
     * Returns an OreDrop for the given mined ore material.
     * If the ore is one of the specified types, a custom display name is used.
     *
     * @param ore The raw ore material that was mined.
     * @return The OreDrop representing the custom drop.
     */
    public static OreDrop getOreDrop(Material ore) {
        switch (ore) {
            case COAL_ORE:
                // When coal ore is mined, rename it to "Bronze Ore"
                return new OreDrop(Material.COAL, "Bronze Ore", 5.0);
            case COPPER_ORE:
                // When copper ore is mined, rename it to "Steel Ore"
                return new OreDrop(Material.COPPER_ORE, "Steel Ore", 7.0);
            case REDSTONE_ORE:
                // When redstone ore is mined, rename it to "Mithril Ore"
                return new OreDrop(Material.REDSTONE_ORE, "Mithril Ore", 10.0);
            case DIAMOND_ORE:
                // When diamond ore is mined, rename it to "Rune Ore"
                return new OreDrop(Material.DIAMOND, "Rune Ore", 15.0);
            default:
                // For any other ore, return the material with its normal name.
                return new OreDrop(ore, ore.name(), 3.0);
        }
    }

    /**
     * Represents the custom drop for a mined ore.
     */
    public static class OreDrop {
        public final Material material;
        public final String displayName;
        public final double xpReward;

        /**
         * Constructs an OreDrop.
         *
         * @param material    The base material for the drop.
         * @param displayName The custom display name (e.g., "Bronze Ore").
         * @param xpReward    The XP reward for mining this ore.
         */
        public OreDrop(Material material, String displayName, double xpReward) {
            this.material = material;
            this.displayName = displayName;
            this.xpReward = xpReward;
        }
    }
}