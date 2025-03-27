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
            default:
                return 1;
        }
    }
}