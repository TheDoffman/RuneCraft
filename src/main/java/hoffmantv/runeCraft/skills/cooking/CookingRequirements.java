package hoffmantv.runeCraft.skills.cooking;

import org.bukkit.Material;

public class CookingRequirements {

    /**
     * Returns FoodData for a given raw food item.
     * If the raw food's name starts with "RAW_", its cooked result is generated
     * by replacing "RAW_" with "COOKED_" for the Material and "Cooked " for the display name.
     *
     * Example:
     * - RAW_BEEF becomes "Cooked Beef" (Material.COOKED_BEEF).
     */
    public static FoodData getFoodData(Material rawFood) {
        String rawName = rawFood.toString();
        // You can include specific cases here for custom mappings if needed.
        switch (rawFood) {
            case COD:
                // For fish, we may want to use custom mappings:
                return new FoodData("Cooked Cod", 1, 5, Material.COOKED_COD);
            case SALMON:
                return new FoodData("Cooked Salmon", 1, 7, Material.COOKED_SALMON);
            default:
                if (rawName.startsWith("RAW_")) {
                    String displayName = "Cooked " + rawName.substring(4).replace('_', ' ');
                    Material cookedMaterial = convertRawToCooked(rawFood);
                    // Default required level and XP reward; adjust as needed.
                    return new FoodData(displayName, 1, 5, cookedMaterial);
                }
                // Fallback: if not raw, simply return a generic cooked version.
                return new FoodData("Cooked " + rawName, 1, 5, rawFood);
        }
    }

    private static Material convertRawToCooked(Material raw) {
        String rawName = raw.toString();
        if (rawName.startsWith("RAW_")) {
            String cookedName = rawName.replace("RAW_", "COOKED_");
            try {
                return Material.valueOf(cookedName);
            } catch (IllegalArgumentException e) {
                // If no such material exists, return the raw material as fallback.
                return raw;
            }
        }
        return raw;
    }

    public static class FoodData {
        public final String displayName;  // Custom OSRS-style name (e.g., "Cooked Beef")
        public final int requiredLevel;   // Minimum cooking level required.
        public final double xpReward;     // XP awarded for cooking this food.
        public final Material cookedResult; // The resulting cooked item (e.g., Material.COOKED_BEEF).

        public FoodData(String displayName, int requiredLevel, double xpReward, Material cookedResult) {
            this.displayName = displayName;
            this.requiredLevel = requiredLevel;
            this.xpReward = xpReward;
            this.cookedResult = cookedResult;
        }
    }
}