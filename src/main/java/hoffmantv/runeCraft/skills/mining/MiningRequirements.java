package hoffmantv.runeCraft.skills.mining;

import org.bukkit.Material;

public class MiningRequirements {

    // Returns the required mining level for the given ore.
    public static int getRequiredLevel(Material material) {
        switch(material) {
            case COAL_ORE:
                return 1; // Bronze ore tier
            case IRON_ORE:
                return 15;
            case GOLD_ORE:
                return 40;
            case REDSTONE_ORE:
                return 55;
            case LAPIS_ORE:
                return 70;
            case DIAMOND_ORE:
                return 85;
            case EMERALD_ORE:
                return 40;
            case NETHER_QUARTZ_ORE:
                return 20;
            case COPPER_ORE:
                return 30;
            default:
                return 1;
        }
    }

    // Returns the XP reward for mining the given ore.
    public static double getXpReward(Material material) {
        switch(material) {
            case COAL_ORE:
                return 17.5;
            case IRON_ORE:
                return 35;
            case GOLD_ORE:
                return 65;
            case REDSTONE_ORE:
                return 80;
            case LAPIS_ORE:
                return 95;
            case DIAMOND_ORE:
                return 125;
            case EMERALD_ORE:
                return 65;
            case NETHER_QUARTZ_ORE:
                return 40;
            case COPPER_ORE:
                return 50;
            default:
                return 5;
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
                return new OreDrop(Material.COAL, "Bronze Ore", getXpReward(ore));
            case COPPER_ORE:
                return new OreDrop(Material.COPPER_ORE, "Steel Ore", getXpReward(ore));
            case REDSTONE_ORE:
                return new OreDrop(Material.REDSTONE_ORE, "Mithril Ore", getXpReward(ore));
            case DIAMOND_ORE:
                return new OreDrop(Material.DIAMOND, "Runite Ore", getXpReward(ore));
            case IRON_ORE:
                return new OreDrop(Material.IRON_ORE, "Iron Ore", getXpReward(ore));
            case GOLD_ORE:
                return new OreDrop(Material.GOLD_ORE, "Gold Ore", getXpReward(ore));
            case LAPIS_ORE:
                return new OreDrop(Material.LAPIS_ORE, "Adamantite Ore", getXpReward(ore));
            case EMERALD_ORE:
                return new OreDrop(Material.EMERALD_ORE, "Gem Ore", getXpReward(ore));
            case NETHER_QUARTZ_ORE:
                return new OreDrop(Material.NETHER_QUARTZ_ORE, "Silver Ore", getXpReward(ore));
            default:
                // For any other ore, return the material with its normal name.
                return new OreDrop(ore, ore.name().replace('_', ' '), getXpReward(ore));
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