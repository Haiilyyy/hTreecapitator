package fr.haily.hTreecapitator.service;

import fr.haily.hTreecapitator.HTreecapitator;
import fr.haily.hTreecapitator.utils.BlockUtils;
import fr.haily.hTreecapitator.utils.ProtectionUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class LeafDecayService {

    private static final List<Queue<Block>> blocksQueue = new ArrayList<>();

    private static final int[][] OFFSETS_SCAN = generateOffsets(5, 5);
    private static final int[][] OFFSETS_CHECK = generateOffsets(2, 2);

    private LeafDecayService() {
        throw new IllegalStateException("This class cannot be instantiated");
    }

    public static void start() {
        new BukkitRunnable() {
            @Override
            public void run() {
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
        Set<Block> leaves = new HashSet<>();
        for (Block log : logs) {
            collectLeaves(log, leaves);
        }

        List<Block> decayLeaves = new ArrayList<>();
        for (Block leaf : leaves) {
            if (shouldDecay(leaf) && ProtectionUtils.canBreak(player, leaf)) {
                decayLeaves.add(leaf);
            }
        }

        Collections.shuffle(decayLeaves);
        blocksQueue.add(new ArrayDeque<>(decayLeaves));
    }

    private static boolean shouldDecay(Block block) {
        if (!(block.getBlockData() instanceof Leaves leaves)) {
            return false;
        }

        if (leaves.isPersistent()) {
            return false;
        }

        for (int[] off : OFFSETS_CHECK) {
            Block relative = block.getRelative(off[0], off[1], off[2]);
            if (BlockUtils.isLog(relative)) {
                return false;
            }
        }

        return true;
    }

    private static void collectLeaves(Block origin, Set<Block> output) {
        for (int[] off : OFFSETS_SCAN) {
            Block relative = origin.getRelative(off[0], off[1], off[2]);
            if (relative.getBlockData() instanceof Leaves leaves) {
                if (!leaves.isPersistent()) {
                    output.add(relative);
                }
            }
        }
    }

    private static int[][] generateOffsets(int range, int yRange) {
        List<int[]> offsets = new ArrayList<>();
        for (int dx = -range; dx <= range; dx++) {
            for (int dy = -yRange; dy <= yRange; dy++) {
                for (int dz = -range; dz <= range; dz++) {
                    offsets.add(new int[]{dx, dy, dz});
                }
            }
        }
        return offsets.toArray(new int[0][]);
    }
}
