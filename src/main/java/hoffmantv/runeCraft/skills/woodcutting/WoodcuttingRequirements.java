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
                return 10;
            case IRON_AXE:
                return 15;
            case GOLDEN_AXE:
                return 20;
            case DIAMOND_AXE:
                return 30;
            case NETHERITE_AXE:
                return 40;
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
                return 10;
            case BIRCH_LOG:
                return 15;
            case JUNGLE_LOG:
                return 20;
            case ACACIA_LOG:
                return 30;
            case DARK_OAK_LOG:
                return 40;
            default:
                return 1;
        }
    }
}