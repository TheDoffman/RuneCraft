// src/main/java/hoffmantv/runeCraft/skills/agility/AgilityXP.java
package hoffmantv.runeCraft.skills.agility;

import hoffmantv.runeCraft.skills.OsrsXpTable;

public final class AgilityXP {
    // OSRS-like curve
    public static double levelToXp(int level) {
        return OsrsXpTable.xpForLevel(level);
    }
}