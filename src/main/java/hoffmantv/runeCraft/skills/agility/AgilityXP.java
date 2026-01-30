// src/main/java/hoffmantv/runeCraft/skills/agility/AgilityXP.java
package hoffmantv.runeCraft.skills.agility;

public final class AgilityXP {
    // OSRS-like curve
    public static double levelToXp(int level) {
        double points = 0;
        for (int lvl = 1; lvl < level; lvl++) {
            points += Math.floor(lvl + 300 * Math.pow(2, (double) lvl / 7.0));
        }
        return Math.floor(points / 4.0);
    }
}