package hoffmantv.runeCraft.skills.woodcutting;

import org.bukkit.Material;

public class WoodcuttingUtils {

    // Returns the minimum woodcutting level required to chop a given log type.
    public static int getRequiredLevel(Material log) {
        switch(log) {
            case OAK_LOG:
                return 1;
            case SPRUCE_LOG:
                return 10;
            case BIRCH_LOG:
                return 5;
            case JUNGLE_LOG:
                return 15;
            case ACACIA_LOG:
                return 20;
            case DARK_OAK_LOG:
                return 25;
            default:
                return 1;
        }
    }

    // Returns the XP reward for chopping down a tree of a given log type.
    public static double getXpReward(Material log) {
        switch(log) {
            case OAK_LOG:
                return 10;
            case SPRUCE_LOG:
                return 15;
            case BIRCH_LOG:
                return 12;
            case JUNGLE_LOG:
                return 20;
            case ACACIA_LOG:
                return 25;
            case DARK_OAK_LOG:
                return 30;
            default:
                return 10;
        }
    }
}