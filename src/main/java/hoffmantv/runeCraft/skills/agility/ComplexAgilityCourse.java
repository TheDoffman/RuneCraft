package hoffmantv.runeCraft.skills.agility;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class ComplexAgilityCourse {

    private final String courseName;

    public ComplexAgilityCourse(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseName() {
        return courseName;
    }

    /**
     * Generates a complex agility course starting from the given origin.
     * The course is built using stone, oak logs, and ladders.
     *
     * @param origin The starting location (bottom-left corner of the starting platform).
     */
    public void generateCourse(Location origin) {
        World world = origin.getWorld();
        if (world == null) return;

        // PART 1: Starting Platform (5x5 stone)
        generatePlatform(world, origin, 5, Material.STONE);

        // PART 2: Jump Gap and Landing Platform.
        // Leave a gap of 3 blocks in the X direction; landing platform is 5x5 stone.
        Location landingOrigin = origin.clone().add(5 + 3, 0, 0);
        generatePlatform(world, landingOrigin, 5, Material.STONE);

        // PART 3: Climbing Wall.
        // Build a wall (3 blocks wide x 6 blocks high) using oak logs, attached to the eastern edge of the landing platform.
        // The wall's bottom-left is at landingOrigin + (5, 0, 0).
        Location wallOrigin = landingOrigin.clone().add(5, 0, 0);
        int wallWidth = 3;
        int wallHeight = 6;
        generateWall(world, wallOrigin, wallWidth, wallHeight, Material.OAK_LOG);

        // Build a column of ladders on the eastern face of the wall for climbing.
        // Place ladders one block to the east of the wall.
        Location ladderOrigin = wallOrigin.clone().add(wallWidth, 0, 0);
        generateVerticalColumn(world, ladderOrigin, wallHeight, Material.LADDER, org.bukkit.block.data.BlockData.class);
        // (For simplicity, we assume the default ladder facing is acceptable.)

        // PART 4: Bridge.
        // Build a narrow (1 block wide) bridge from the top of the wall across a 7-block gap.
        // The bridge starts at the top-right corner of the wall.
        Location bridgeOrigin = wallOrigin.clone().add(wallWidth, wallHeight - 1, 0);
        generateBridge(world, bridgeOrigin, 7, Material.OAK_LOG);
    }

    /**
     * Generates a square platform.
     *
     * @param world    The world.
     * @param origin   The bottom-left corner of the platform.
     * @param size     The side length.
     * @param material The material to fill with.
     */
    private void generatePlatform(World world, Location origin, int size, Material material) {
        int startX = origin.getBlockX();
        int startY = origin.getBlockY();
        int startZ = origin.getBlockZ();
        for (int x = startX; x < startX + size; x++) {
            for (int z = startZ; z < startZ + size; z++) {
                Block block = world.getBlockAt(x, startY, z);
                block.setType(material);
            }
        }
    }

    /**
     * Generates a wall of given width and height.
     *
     * @param world    The world.
     * @param origin   The bottom-left corner of the wall.
     * @param width    The wall width.
     * @param height   The wall height.
     * @param material The material to use.
     */
    private void generateWall(World world, Location origin, int width, int height, Material material) {
        int startX = origin.getBlockX();
        int startY = origin.getBlockY();
        int startZ = origin.getBlockZ();
        for (int x = startX; x < startX + width; x++) {
            for (int y = startY; y < startY + height; y++) {
                // For the wall, assume a 1-block-thick wall (in Z dimension).
                Block block = world.getBlockAt(x, y, startZ);
                block.setType(material);
            }
        }
    }

    /**
     * Generates a vertical column (for ladders).
     *
     * @param world    The world.
     * @param origin   The bottom block location of the column.
     * @param height   The height of the column.
     * @param material The material to use.
     * @param dataType Unused placeholder for potential BlockData customization.
     */
    private void generateVerticalColumn(World world, Location origin, int height, Material material, Class<?> dataType) {
        int x = origin.getBlockX();
        int startY = origin.getBlockY();
        int z = origin.getBlockZ();
        for (int y = startY; y < startY + height; y++) {
            Block block = world.getBlockAt(x, y, z);
            block.setType(material);
        }
    }

    /**
     * Generates a horizontal bridge.
     *
     * @param world    The world.
     * @param origin   The starting location of the bridge.
     * @param length   The length of the bridge.
     * @param material The material to use.
     */
    private void generateBridge(World world, Location origin, int length, Material material) {
        int startX = origin.getBlockX();
        int startY = origin.getBlockY();
        int startZ = origin.getBlockZ();
        // Create a 1-block-wide bridge along the X-axis.
        for (int x = startX; x < startX + length; x++) {
            Block block = world.getBlockAt(x, startY, startZ);
            block.setType(material);
        }
    }
}