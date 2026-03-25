package fr.haily.hTreecapitator.utils;

import fr.haily.hTreecapitator.config.Settings;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.HashSet;
import java.util.Set;

import static org.bukkit.Bukkit.getServer;

public class ProtectionUtils {

    private static final Set<Block> processing = new HashSet<>();

    private ProtectionUtils() {
        throw new IllegalStateException("This class cannot be instantiated");
    }

    public static boolean isProcessing(Block block) {
        return processing.contains(block);
    }

    public static boolean canBreak(Player player, Block block) {
        if (Settings.isWorldGuardEnabled()) {
            try {
                if (!WorldGuardUtils.canBreak(player, block.getLocation())) {
                    return false;
                }
            } catch (NoClassDefFoundError ignored) {}
        }

        if (Settings.isGriefPreventionEnabled()) {
            try {
                if (!GriefPreventionUtils.canBreak(player, block.getLocation())) {
                    return false;
                }
            } catch (NoClassDefFoundError ignored) {}
        }

        if (Settings.isWorldGuardEnabled() || Settings.isGriefPreventionEnabled()) {
            return true;
        }

        processing.add(block);
        BlockBreakEvent testEvent = new BlockBreakEvent(block, player);
        getServer().getPluginManager().callEvent(testEvent);
        processing.remove(block);

        return !testEvent.isCancelled();
    }
}
