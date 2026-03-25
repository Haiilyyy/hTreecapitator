package fr.haily.hTreecapitator.utils;

import fr.haily.hTreecapitator.config.Settings;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Leaves;

import java.util.*;

public class BlockUtils {

    private static final int[][] NEIGHBOR_OFFSETS = {
        {0,1,0},{0,2,0},{0,-1,0},{0,-2,0},
        {0,0,-1},{0,1,-1},{0,-1,-1},
        {1,0,-1},{1,1,-1},{1,-1,-1},
        {1,0,0},{1,1,0},{1,-1,0},
        {1,0,1},{1,1,1},{1,-1,1},
        {0,0,1},{0,1,1},{0,-1,1},
        {-1,0,1},{-1,1,1},{-1,-1,1},
        {-1,0,0},{-1,1,0},{-1,-1,0},
        {-1,0,-1},{-1,1,-1},{-1,-1,-1}
    };

    private BlockUtils() {
        throw new IllegalStateException("This class cannot be instantiated");
    }

    public static boolean isLog(Material material) {
        String name = material.name();
        if (Settings.isMangroveRoots() && name.equals("MANGROVE_ROOTS")) {
            return true;
        }
        return (name.endsWith("LOG") || name.equals("CRIMSON_STEM") || name.equals("WARPED_STEM"))
                && !name.startsWith("STRIPPED");
    }

    public static boolean isLog(Block block) {
        return isLog(block.getType());
    }

    public static boolean isNetherLeaves(Material material) {
        return material == Material.NETHER_WART_BLOCK
                || material == Material.WARPED_WART_BLOCK
                || material == Material.SHROOMLIGHT;
    }

    public static List<Block> collectLogsIfNatural(Block start) {
        List<Block> logs = new ArrayList<>();
        Set<Block> visited = new HashSet<>();
        Deque<Block> toCheck = new ArrayDeque<>();
        boolean hasLeaves = false;

        toCheck.add(start);
        visited.add(start);

        while (!toCheck.isEmpty()) {
            Block current = toCheck.poll();
            logs.add(current);

            for (int[] off : NEIGHBOR_OFFSETS) {
                Block neighbor = current.getRelative(off[0], off[1], off[2]);
                Material type = neighbor.getType();

                if (!hasLeaves) {
                    if (neighbor.getBlockData() instanceof Leaves leaves) {
                        if (!leaves.isPersistent()) hasLeaves = true;
                    } else if (isNetherLeaves(type)) {
                        hasLeaves = true;
                    }
                }

                if (isLog(type) && visited.add(neighbor)) {
                    toCheck.add(neighbor);
                }
            }

            if (logs.size() >= Settings.getMaxBlocks()) {
                break;
            }
        }

        return hasLeaves ? logs : null;
    }

    public static int[][] getNeighborOffsets() {
        return NEIGHBOR_OFFSETS;
    }
}
