package hoffmantv.runeCraft.skills.cooking;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CookingRequirements {

    /**
     * Returns FoodData for a given raw food item.
     * If the raw food's name starts with "RAW_", its cooked result is generated
     * by replacing "RAW_" with "COOKED_" for the Material and "Cooked " for the display name.
     *
     * Example:
     * - RAW_BEEF becomes "Cooked Beef" (Material.COOKED_BEEF).
     */
    public static FoodData getFoodData(ItemStack rawItem) {
        if (rawItem == null) {
            return null;
        }
        Material rawFood = rawItem.getType();
        String rawName = rawFood.toString();
        String displayName = getDisplayName(rawItem);

        FoodData fishData = getFishData(displayName);
        if (fishData != null) {
            return fishData;
        }

        switch (rawFood) {
            case COD:
                return new FoodData("Cooked Shrimp", 1, 30, Material.COOKED_COD);
            case SALMON:
                return new FoodData("Cooked Salmon", 25, 90, Material.COOKED_SALMON);
            case TROPICAL_FISH:
                return new FoodData("Cooked Lobster", 40, 120, Material.COOKED_COD);
            case PUFFERFISH:
                return new FoodData("Cooked Monkfish", 62, 150, Material.COOKED_COD);
            case RAW_BEEF:
                return new FoodData("Cooked Beef", 1, 30, Material.COOKED_BEEF);
            case RAW_CHICKEN:
                return new FoodData("Cooked Chicken", 1, 30, Material.COOKED_CHICKEN);
            case RAW_PORKCHOP:
                return new FoodData("Cooked Porkchop", 1, 30, Material.COOKED_PORKCHOP);
            case RAW_MUTTON:
                return new FoodData("Cooked Mutton", 1, 30, Material.COOKED_MUTTON);
            case RAW_RABBIT:
                return new FoodData("Cooked Rabbit", 1, 30, Material.COOKED_RABBIT);
            default:
                if (rawName.startsWith("RAW_")) {
                    String cookedName = "Cooked " + rawName.substring(4).replace('_', ' ');
                    Material cookedMaterial = convertRawToCooked(rawFood);
                    return new FoodData(cookedName, 1, 30, cookedMaterial);
                }
                return null;
        }
    }

    public static FoodData getFoodData(Material rawFood) {
        if (rawFood == null) {
            return null;
        }
        return getFoodData(new ItemStack(rawFood));
    }

    private static String getDisplayName(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) {
            return null;
        }
        return ChatColor.stripColor(meta.getDisplayName());
    }

    private static FoodData getFishData(String displayName) {
        if (displayName == null) {
            return null;
        }
        switch (displayName) {
            case "Raw Shrimp":
                return new FoodData("Cooked Shrimp", 1, 30, Material.COOKED_COD);
            case "Raw Trout":
                return new FoodData("Cooked Trout", 15, 70, Material.COOKED_SALMON);
            case "Raw Salmon":
                return new FoodData("Cooked Salmon", 25, 90, Material.COOKED_SALMON);
            case "Raw Lobster":
                return new FoodData("Cooked Lobster", 40, 120, Material.COOKED_COD);
            case "Raw Swordfish":
                return new FoodData("Cooked Swordfish", 45, 140, Material.COOKED_COD);
            case "Raw Monkfish":
                return new FoodData("Cooked Monkfish", 62, 150, Material.COOKED_COD);
            case "Raw Shark":
                return new FoodData("Cooked Shark", 80, 210, Material.COOKED_COD);
            default:
                return null;
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