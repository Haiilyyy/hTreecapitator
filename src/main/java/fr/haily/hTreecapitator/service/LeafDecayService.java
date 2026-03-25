package fr.haily.hTreecapitator.service;

import fr.haily.hTreecapitator.HTreecapitator;
import fr.haily.hTreecapitator.config.Settings;
import fr.haily.hTreecapitator.utils.BlockUtils;
import fr.haily.hTreecapitator.utils.ProtectionUtils;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class LeafDecayService {

    private static final int SCAN_RANGE = 5;
    private static final int CHECK_RANGE = 2;

    private static final List<Queue<Block>> blocksQueue = new ArrayList<>();

    private static final int[][] OFFSETS_CHECK = generateOffsets(CHECK_RANGE);

    private LeafDecayService() {
        throw new IllegalStateException("This class cannot be instantiated");
    }

    public static void start() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (blocksQueue.isEmpty()) return;

                Iterator<Queue<Block>> iterator = blocksQueue.iterator();

                while (iterator.hasNext()) {
                    Queue<Block> queue = iterator.next();

                    for (int i = 0; i < 3; i++) {
                        Block block = queue.poll();

                        if (block == null) {
                            iterator.remove();
                            break;
                        }

                        if (block.getType() != Material.AIR) {
                            block.breakNaturally();
                        }
                    }
                }
            }
        }.runTaskTimer(HTreecapitator.getInstance(), 0L, 5L);
    }

    public static void scanLeaves(List<Block> logs, Player player) {
        if (logs.isEmpty()) return;

        Set<Block> leaves = collectLeavesBoundingBox(logs);

        List<Block> decayLeaves = new ArrayList<>(leaves.size());
        for (Block leaf : leaves) {
            if (shouldDecay(leaf) && ProtectionUtils.canBreak(player, leaf)) {
                decayLeaves.add(leaf);
            }
        }

        if (decayLeaves.isEmpty()) return;

        if (Settings.getInstantBreakLeaves()) {
            for (Block leaf : decayLeaves) {
                if (leaf.getType() != Material.AIR) {
                    leaf.breakNaturally();
                }
            }
        } else {
            Collections.shuffle(decayLeaves);
            blocksQueue.add(new ArrayDeque<>(decayLeaves));
        }
    }

    private static Set<Block> collectLeavesBoundingBox(List<Block> logs) {
        int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;
        int minZ = Integer.MAX_VALUE, maxZ = Integer.MIN_VALUE;

        World world = logs.get(0).getWorld();
        for (Block log : logs) {
            int x = log.getX(), y = log.getY(), z = log.getZ();
            if (x < minX) minX = x; if (x > maxX) maxX = x;
            if (y < minY) minY = y; if (y > maxY) maxY = y;
            if (z < minZ) minZ = z; if (z > maxZ) maxZ = z;
        }

        minX -= SCAN_RANGE; maxX += SCAN_RANGE;
        minY -= SCAN_RANGE; maxY += SCAN_RANGE;
        minZ -= SCAN_RANGE; maxZ += SCAN_RANGE;

        Set<Block> result = new HashSet<>();
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block block = world.getBlockAt(x, y, z);
                    if (block.getBlockData() instanceof Leaves leaves) {
                        if (!leaves.isPersistent()) result.add(block);
                    } else if (BlockUtils.isNetherLeaves(block.getType())) {
                        result.add(block);
                    }
                }
            }
        }
        return result;
    }

    private static boolean shouldDecay(Block block) {
        if (block.getBlockData() instanceof Leaves leaves) {
            if (leaves.isPersistent()) return false;
        } else if (!BlockUtils.isNetherLeaves(block.getType())) {
            return false;
        }

        for (int[] off : OFFSETS_CHECK) {
            if (BlockUtils.isLog(block.getRelative(off[0], off[1], off[2]))) {
                return false;
            }
        }

        return true;
    }

    private static int[][] generateOffsets(int range) {
        List<int[]> offsets = new ArrayList<>();
        for (int dx = -range; dx <= range; dx++) {
            for (int dy = -range; dy <= range; dy++) {
                for (int dz = -range; dz <= range; dz++) {
                    if (dx == 0 && dy == 0 && dz == 0) continue;
                    offsets.add(new int[]{dx, dy, dz});
                }
            }
        }
        return offsets.toArray(new int[0][]);
    }
}
