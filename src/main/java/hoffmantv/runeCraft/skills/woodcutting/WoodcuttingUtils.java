package hoffmantv.runeCraft.skills.woodcutting;

import org.bukkit.Material;

public class WoodcuttingUtils {

    // Returns the minimum woodcutting level required to chop a given log type.
    public static int getRequiredLevel(Material log) {
        switch(log) {
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

    // Returns the XP reward for chopping down a tree of a given log type.
    public static double getXpReward(Material log) {
        switch(log) {
            case OAK_LOG:
                return 25.0;
            case SPRUCE_LOG:
                return 37.5;
            case BIRCH_LOG:
                return 67.5;
            case JUNGLE_LOG:
                return 100.0;
            case ACACIA_LOG:
                return 175.0;
            case DARK_OAK_LOG:
                return 250.0;
            default:
                return 25.0;
        }
    }
}