package fr.haily.hTreecapitator.utils;

import fr.haily.hTreecapitator.HTreecapitator;
import fr.haily.hTreecapitator.config.Settings;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Leaves;

import java.util.*;

public class BlockUtils {

    private static final BlockFace[] FACES = {
            BlockFace.UP, BlockFace.DOWN,
            BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST,
            BlockFace.SOUTH, BlockFace.SOUTH_WEST, BlockFace.WEST, BlockFace.NORTH_WEST
    };

    private static final BlockFace[] RELATIVES = { BlockFace.SELF, BlockFace.UP, BlockFace.DOWN };

    private BlockUtils() {
        throw new IllegalStateException("This class cannot be instantiated");
    }

    public static boolean isLog(Material material) {
        String name = material.name();
        if (HTreecapitator.getInstance().getConfig().getBoolean("mangrove-roots") && name.equals("MANGROVE_ROOTS")) {
            return true;
        }
        return (name.endsWith("LOG") || name.equals("CRIMSON_STEM") || name.equals("WARPED_STEM"))
                && !name.startsWith("STRIPPED");
    }

    public static boolean isLog(Block block) {
        return isLog(block.getType());
    }

    public static boolean isNaturalTree(Block block) {
        Set<Block> visited = new HashSet<>();
        Deque<Block> toCheck = new ArrayDeque<>();
        toCheck.add(block);
        visited.add(block);

        while (!toCheck.isEmpty()) {
            Block current = toCheck.poll();

            for (BlockFace face : FACES) {
                for (BlockFace rel : RELATIVES) {
                    Block neighbor = current.getRelative(face).getRelative(rel);

                    if (neighbor.getBlockData() instanceof Leaves leaves) {
                        if (!leaves.isPersistent()) {
                            return true;
                        }
                    }

                    if (isLog(neighbor) && visited.add(neighbor)) {
                        toCheck.add(neighbor);
                    }
                }
            }

            if (visited.size() > Settings.getMaxBlocks()) {
                return false;
            }
        }

        return false;
    }

    public static List<Block> collectLogs(Block start) {
        List<Block> logs = new ArrayList<>();
        Set<Block> visited = new HashSet<>();
        Deque<Block> toCheck = new ArrayDeque<>();
        toCheck.add(start);
        visited.add(start);

        while (!toCheck.isEmpty()) {
            Block current = toCheck.poll();
            logs.add(current);

            for (BlockFace face : FACES) {
                for (BlockFace rel : RELATIVES) {
                    Block neighbor = current.getRelative(face).getRelative(rel);
                    if (isLog(neighbor) && visited.add(neighbor)) {
                        toCheck.add(neighbor);
                    }
                }
            }

            if (logs.size() > Settings.getMaxBlocks()) {
                break;
            }
        }

        return logs;
    }
}
