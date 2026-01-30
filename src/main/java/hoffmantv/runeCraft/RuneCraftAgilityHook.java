// src/main/java/hoffmantv/runeCraft/RuneCraftAgilityHook.java
package hoffmantv.runeCraft;

import hoffmantv.runeCraft.commands.AgilityCommand;
import hoffmantv.runeCraft.skills.agility.AgilityBootstrap;

public final class RuneCraftAgilityHook {
    public static void wire(RuneCraft plugin) {
        AgilityBootstrap.init(plugin);
        AgilityCommand.register(plugin);
    }
}