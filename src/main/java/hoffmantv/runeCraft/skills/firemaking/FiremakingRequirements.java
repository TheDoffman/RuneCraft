package hoffmantv.runeCraft.skills.firemaking;

import org.bukkit.Material;

public class FiremakingRequirements {

    /**
     * Returns the required firemaking level to burn a specific log type.
     */
    public static int getRequiredLevel(Material log) {
        switch (log) {
            case OAK_LOG:
                return 1;
            case SPRUCE_LOG:
                return 5;
            case BIRCH_LOG:
                return 10;
            case JUNGLE_LOG:
                return 15;
            case ACACIA_LOG:
                return 20;
            case DARK_OAK_LOG:
                return 30;
            default:
                return 1;
        }
    }

    /**
     * Returns the XP reward for burning a specific log type.
     */
    public static double getXpReward(Material log) {
        switch (log) {
            case OAK_LOG:
                return 8;
            case SPRUCE_LOG:
                return 12;
            case BIRCH_LOG:
                return 10;
            case JUNGLE_LOG:
                return 15;
            case ACACIA_LOG:
                return 18;
            case DARK_OAK_LOG:
                return 22;
            default:
                return 5;
        }
    }
}