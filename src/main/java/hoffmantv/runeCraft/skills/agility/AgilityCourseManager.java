// src/main/java/hoffmantv/runeCraft/skills/agility/AgilityCourseManager.java
package hoffmantv.runeCraft.skills.agility;

import hoffmantv.runeCraft.RuneCraft;
import hoffmantv.runeCraft.config.YamlConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.*;

public final class AgilityCourseManager {
    private static YamlConfigManager configManager;

    public static void init(RuneCraft plugin) {
        configManager = new YamlConfigManager(plugin, "agility_courses.yml");
        configManager.init();
    }

    public static void save() {
        if (configManager != null) {
            configManager.save();
        }
    }

    public static void setCourse(String name, List<CourseNode> nodes) {
        List<String> ser = new ArrayList<>();
        for (CourseNode n : nodes) {
            if (n.loc.getWorld() == null) {
                continue;
            }
            ser.add(n.type.name() + ";" + n.levelReq + ";" + n.xp + ";"
                    + n.loc.getWorld().getName() + ";" + n.loc.getBlockX() + ";" + n.loc.getBlockY() + ";" + n.loc.getBlockZ());
        }
        configManager.getConfig().set("courses."+name, ser);
        save();
    }

    public static List<CourseNode> getCourse(String name) {
        List<String> ser = configManager.getConfig().getStringList("courses."+name);
        List<CourseNode> out = new ArrayList<>();
        for (String s : ser) {
            String[] a = s.split(";");
            if (a.length < 7) {
                continue;
            }
            try {
                AgilityObstacleType t = AgilityObstacleType.valueOf(a[0]);
                int lvl = Integer.parseInt(a[1]);
                double xp = Double.parseDouble(a[2]);
                if (Bukkit.getWorld(a[3]) == null) {
                    continue;
                }
                Location l = new Location(Bukkit.getWorld(a[3]), Integer.parseInt(a[4]), Integer.parseInt(a[5]), Integer.parseInt(a[6]));
                out.add(new CourseNode(t, lvl, xp, l));
            } catch (Exception ex) {
                Bukkit.getLogger().warning("Invalid agility course entry: " + s);
            }
        }
        return out;
    }

    public record CourseNode(AgilityObstacleType type, int levelReq, double xp, Location loc) {}
}