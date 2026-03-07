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

    /**
     * Check if a block is currently being tested by a simulated event.
     * Used by the listener to ignore simulated events.
     */
    public static boolean isProcessing(Block block) {
        return processing.contains(block);
    }

    /**
     * Check if a player can break a block, considering all protection plugins.
     * Uses specific APIs for WorldGuard and GriefPrevention,
     * and a simulated BlockBreakEvent for any other plugin (Towny, Lands, etc.)
     */
    public static boolean canBreak(Player player, Block block) {
        // Check WorldGuard (specific API, handles __global__ region)
        if (Settings.isWorldGuardEnabled()) {
            try {
                if (!WorldGuardUtils.canBreak(player, block.getLocation())) {
                    return false;
                }
            } catch (NoClassDefFoundError ignored) {}
        }

        // Check GriefPrevention (specific API)
        if (Settings.isGriefPreventionEnabled()) {
            try {
                if (!GriefPreventionUtils.canBreak(player, block.getLocation())) {
                    return false;
                }
            } catch (NoClassDefFoundError ignored) {}
        }

        // Generic check: simulate a BlockBreakEvent for other protection plugins
        // (Towny, Lands, Residence, etc.)
        processing.add(block);
        BlockBreakEvent testEvent = new BlockBreakEvent(block, player);
        getServer().getPluginManager().callEvent(testEvent);
        processing.remove(block);

        return !testEvent.isCancelled();
    }
}
