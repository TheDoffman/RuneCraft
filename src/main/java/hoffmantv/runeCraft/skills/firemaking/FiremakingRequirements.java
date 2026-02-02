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

    /**
     * Returns the XP reward for burning a specific log type.
     */
    public static double getXpReward(Material log) {
        switch (log) {
            case OAK_LOG:
                return 40.0;
            case SPRUCE_LOG:
                return 60.0;
            case BIRCH_LOG:
                return 90.0;
            case JUNGLE_LOG:
                return 135.0;
            case ACACIA_LOG:
                return 202.5;
            case DARK_OAK_LOG:
                return 303.8;
            default:
                return 40.0;
        }
    }
}