package hoffmantv.runeCraft.skills.woodcutting;

import org.bukkit.Material;

public class WoodcuttingRequirements {

    /**
     * Returns the required woodcutting level for the specified axe type.
     */
    public static int getRequiredAxeLevel(Material axe) {
        switch (axe) {
            case WOODEN_AXE:
                return 1;
            case STONE_AXE:
                return 1;
            case IRON_AXE:
                return 6;
            case GOLDEN_AXE:
                return 21;
            case DIAMOND_AXE:
                return 31;
            case NETHERITE_AXE:
                return 61;
            default:
                return 1;
        }
    }

    /**
     * Returns the required woodcutting level for the specified tree (log) type.
     */
    public static int getRequiredTreeLevel(Material log) {
        switch (log) {
            case OAK_LOG:
                return 1;
            case SPRUCE_LOG:
                return 15;
            case BIRCH_LOG:
                return 30;
            case JUNGLE_LOG:
                return 45;
            case ACACIA_LOG:
                return 60;
            case DARK_OAK_LOG:
                return 75;
            default:
                return 1;
        }
    }
}