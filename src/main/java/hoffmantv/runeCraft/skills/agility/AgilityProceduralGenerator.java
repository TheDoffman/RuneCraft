// src/main/java/hoffmantv/runeCraft/skills/agility/AgilityProceduralGenerator.java
package hoffmantv.runeCraft.skills.agility;

import hoffmantv.runeCraft.RuneCraft;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class AgilityProceduralGenerator {
    private static final Random R = new Random();

    public static List<AgilityCourseManager.CourseNode> generate(Player p, String name, int radius, int difficulty) {
        World w = p.getWorld();
        Location base = p.getLocation().getBlock().getLocation();

        List<AgilityCourseManager.CourseNode> nodes = new ArrayList<>();
        int steps = Math.max(5, Math.min(20, 6 + difficulty * 2));

        Location cursor = base.clone().add(0, 1, 0);
        for (int i = 0; i < steps; i++) {
            AgilityObstacleType type = pickType(difficulty);
            int lvlReq = Math.min(99, 1 + difficulty * 10);
            double xp = 10 + difficulty * 5;

            Location target = randomNearby(w, base, radius);
            // Snap Y to safe height
            target.setY(w.getHighestBlockYAt(target) + 1);

            placeObstacle(w, type, target);
            nodes.add(new AgilityCourseManager.CourseNode(type, lvlReq, xp, target));
            cursor = target.clone();
        }

        AgilityCourseManager.setCourse(name, nodes);
        p.sendMessage(ChatColor.AQUA + "Generated agility course '" + name + "' with " + nodes.size() + " obstacles.");
        return nodes;
    }

    private static AgilityObstacleType pickType(int difficulty) {
        AgilityObstacleType[] pool = AgilityObstacleType.values();
        return pool[R.nextInt(pool.length)];
    }

    private static Location randomNearby(World w, Location base, int radius) {
        int dx = R.nextInt(radius*2+1) - radius;
        int dz = R.nextInt(radius*2+1) - radius;
        return base.clone().add(dx, 0, dz);
    }

    private static void placeObstacle(World w, AgilityObstacleType type, Location loc) {
        Block b;
        switch (type) {
            case JUMP -> {
                // 3x3 pads of stone, central pad is the node
                for (int x=-1;x<=1;x++) for (int z=-1;z<=1;z++) {
                    b = w.getBlockAt(loc.clone().add(x, -1, z));
                    b.setType(Material.STONE);
                }
                w.getBlockAt(loc).setType(Material.LIGHT_WEIGHTED_PRESSURE_PLATE);
            }
            case LADDER -> {
                // 4-high ladder on stone pillar
                for (int y=0;y<4;y++) {
                    w.getBlockAt(loc.clone().add(0, y, 0)).setType(Material.STONE);
                }
                // ladder faces player spawn direction loosely by using nearest air side
                w.getBlockAt(loc.clone().add(0, 1, -1)).setType(Material.LADDER);
            }
            case BEAM -> {
                // 5-block fence beam
                for (int i=0;i<5;i++) {
                    w.getBlockAt(loc.clone().add(i-2, 0, 0)).setType(Material.OAK_FENCE);
                }
            }
            case SLIME -> {
                // bounce pad with ring
                w.getBlockAt(loc.clone().add(0,-1,0)).setType(Material.SLIME_BLOCK);
                for (int x=-1;x<=1;x++) for (int z=-1;z<=1;z++) {
                    if (x==0 && z==0) continue;
                    w.getBlockAt(loc.clone().add(x,-1,z)).setType(Material.SMOOTH_STONE);
                }
                w.playSound(loc, Sound.BLOCK_SLIME_BLOCK_PLACE, 1f, 1f);
            }
            case TRAPDOOR -> {
                w.getBlockAt(loc.clone().add(0,-1,0)).setType(Material.SPRUCE_TRAPDOOR);
                Block td = w.getBlockAt(loc.clone().add(0,-1,0));
                if (td.getBlockData() instanceof TrapDoor data) {
                    data.setOpen(false);
                    td.setBlockData(data);
                }
                w.getBlockAt(loc).setType(Material.STRING);
            }
            case VINES -> {
                // vines on a 3-high log
                for (int y=0;y<3;y++) w.getBlockAt(loc.clone().add(0,y,0)).setType(Material.OAK_LOG);
                w.getBlockAt(loc.clone().add(0,1,1)).setType(Material.VINE);
            }
        }
        w.spawnParticle(Particle.CLOUD, loc.clone().add(0.5,0.8,0.5), 10, 0.2,0.2,0.2,0.02);
    }
}