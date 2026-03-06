package fr.haily.hTreecapitator.listener;

import fr.haily.hTreecapitator.HTreecapitator;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class TreeCutListener implements Listener {

    @EventHandler
    public void treeCut(BlockBreakEvent event) {

        Player player = event.getPlayer();
        String permission = HTreecapitator.getInstance().getConfig().getString("options.permission");

        if(!permission.equals("")) {
            if(!player.hasPermission(permission)) {
                return;
            }
        }

        ItemStack item = player.getInventory().getItemInMainHand();

        if(HTreecapitator.getInstance().isLog(event.getBlock())) {

            if (!HTreecapitator.getInstance().isNaturalTree(event.getBlock())) {
                return;
            }

            if(HTreecapitator.getInstance().getConfig().isSet("options.shift_to_activate")) {
                if(HTreecapitator.getInstance().getConfig().getBoolean("options.shift_to_activate") && !player.isSneaking()) {
                    return;
                }
            }

            List<Block> logsLeft = new ArrayList<>();
            logsLeft.add(event.getBlock());


            new BukkitRunnable() {

                int logCounter = 0;

                @Override
                public void run() {

                    if(logsLeft.isEmpty() || logCounter > HTreecapitator.getInstance().getConfig().getInt("options.max_logs")) {
                        cancel();
                        return;
                    }

                    Block log = logsLeft.get(0);

                    if(HTreecapitator.getInstance().getConfig().getBoolean("options.auto_pickup_drops")) {
                        for(ItemStack item : log.getDrops(player.getInventory().getItemInMainHand())) {
                            player.getInventory().addItem(item);
                        }
                        log.setType(Material.AIR);
                    } else {
                        log.breakNaturally(player.getInventory().getItemInMainHand());
                    }

                    for(BlockFace face : HTreecapitator.getInstance().faces) {

                        BlockFace[] relatives = new BlockFace[] { BlockFace.SELF, BlockFace.UP, BlockFace.DOWN };

                        for(BlockFace relative : relatives) {
                            Block block = log.getRelative(face).getRelative(relative);
                            if(HTreecapitator.getInstance().isLog(block) && !logsLeft.contains(block)) {
                                logsLeft.add(block);
                            }
                        }

                    }

                    logsLeft.remove(0);
                    logCounter++;

                }

            }.runTaskTimer(HTreecapitator.getInstance(), 0L, 1L);
        }

    }

}
