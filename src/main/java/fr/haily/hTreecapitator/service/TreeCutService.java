package fr.haily.hTreecapitator.service;

import fr.haily.hTreecapitator.HTreecapitator;
import fr.haily.hTreecapitator.config.Settings;
import fr.haily.hTreecapitator.utils.ChatUtils;
import fr.haily.hTreecapitator.utils.DurabilityUtils;
import fr.haily.hTreecapitator.utils.JobsUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TreeCutService {

    private static final List<BreakTask> breakTasks = new ArrayList<>();

    private TreeCutService() {
        throw new IllegalStateException("This class cannot be instantiated");
    }

    public static void start() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (breakTasks.isEmpty()) return;

                Iterator<BreakTask> iterator = breakTasks.iterator();

                while (iterator.hasNext()) {
                    BreakTask task = iterator.next();

                    if (!task.player.isOnline()) {
                        iterator.remove();
                        continue;
                    }

                    if (task.isFinished()) {
                        LeafDecayService.scanLeaves(task.processedLogs, task.player);
                        iterator.remove();
                        continue;
                    }

                    ItemStack tool = task.player.getInventory().getItemInMainHand();
                    if (Settings.getInstantBreakLogs()) {
                        while (!task.isFinished()) {
                            Block block = task.nextBlock();
                            if (!breakBlock(task.player, block, tool)) {
                                break;
                            }
                            task.processedLogs.add(block);
                            task.notifyIfInventoryFull();
                        }
                        if (Settings.getSoundEnabled()) {
                            task.player.playSound(task.player.getLocation(), Settings.getSound(), 0.5f, 1.5f);
                        }
                        LeafDecayService.scanLeaves(task.processedLogs, task.player);
                        iterator.remove();
                    } else {
                        Block block = task.nextBlock();
                        if (!breakBlock(task.player, block, tool)) {
                            LeafDecayService.scanLeaves(task.processedLogs, task.player);
                            iterator.remove();
                            continue;
                        }
                        task.processedLogs.add(block);
                        task.notifyIfInventoryFull();
                        if (Settings.getSoundEnabled()) {
                            task.player.playSound(block.getLocation(), Settings.getSound(), 0.5f, 1.5f);
                        }
                    }
                }
            }
        }.runTaskTimer(HTreecapitator.getInstance(), 0L, 1L);
    }

    public static void addTask(Player player, List<Block> blocks) {
        breakTasks.add(new BreakTask(player, blocks));
    }

    private static boolean breakBlock(Player player, Block block, ItemStack tool) {
        if (block == null || block.getType() == Material.AIR) {
            return true;
        }

        if (Settings.isJobsEnabled()) {
            try {
                JobsUtils.giveJobsXp(player.getUniqueId(), block);
            } catch (NoClassDefFoundError ignored) {}
        }

        if (Settings.getAutoPickup()) {
            for (ItemStack item : block.getDrops(tool)) {
                player.getInventory().addItem(item).values()
                        .forEach(leftover -> block.getWorld().dropItemNaturally(block.getLocation(), leftover));
            }
            block.setType(Material.AIR);
        } else {
            block.breakNaturally(tool);
        }

        return DurabilityUtils.damageTool(player);
    }

    private static class BreakTask {
        private final Player player;
        private final List<Block> blocks;
        private final List<Block> processedLogs = new ArrayList<>();
        private int index = 0;
        private boolean inventoryFullNotified = false;

        BreakTask(Player player, List<Block> blocks) {
            this.player = player;
            this.blocks = blocks;
        }

        boolean isFinished() {
            return index >= blocks.size() || index >= Settings.getMaxBlocks();
        }

        Block nextBlock() {
            return blocks.get(index++);
        }

        void notifyIfInventoryFull() {
            if (!Settings.getAutoPickup() || inventoryFullNotified) return;
            if (player.getInventory().firstEmpty() == -1) {
                player.sendMessage(ChatUtils.format(Settings.getInventoryFullMessage()));
                inventoryFullNotified = true;
            }
        }
    }
}
