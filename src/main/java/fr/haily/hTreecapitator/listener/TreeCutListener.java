package fr.haily.hTreecapitator.listener;

import fr.haily.hTreecapitator.config.Settings;
import fr.haily.hTreecapitator.service.ToggleService;
import fr.haily.hTreecapitator.service.TreeCutService;
import fr.haily.hTreecapitator.utils.BlockUtils;
import fr.haily.hTreecapitator.utils.DurabilityUtils;
import fr.haily.hTreecapitator.utils.EnchantUtils;
import fr.haily.hTreecapitator.utils.PermissionsUtils;
import fr.haily.hTreecapitator.utils.ProtectionUtils;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;

public class TreeCutListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void treeCut(BlockBreakEvent event) {

        if (ProtectionUtils.isProcessing(event.getBlock())) {
            return;
        }

        if (Settings.isWorldBlocked(event.getBlock().getWorld().getName())) {
            return;
        }

        Player player = event.getPlayer();

        if (Settings.getUsePermissions()) {
            if (!PermissionsUtils.hasPermission(player, Settings.getPermission())) {
                return;
            }
        }

        if (Settings.getAxeOnly()) {
            if (!player.getInventory().getItemInMainHand().getType().name().endsWith("_AXE")) {
                return;
            }
        }

        if (Settings.getRequireEnchantment()) {
            if (!EnchantUtils.hasEnchant(player.getInventory().getItemInMainHand())) {
                return;
            }
        }

        if (!ToggleService.getToggle(player.getUniqueId())) {
            return;
        }

        if (!BlockUtils.isLog(event.getBlock())) {
            return;
        }

        if (!BlockUtils.isNaturalTree(event.getBlock())) {
            return;
        }

        if (Settings.getShiftMining() && !player.isSneaking()) {
            return;
        }

        List<Block> allLogs = BlockUtils.collectLogs(event.getBlock());

        int maxUses = DurabilityUtils.getRemainingUses(player.getInventory().getItemInMainHand());
        if (maxUses < allLogs.size()) {
            allLogs = allLogs.subList(0, maxUses);
        }

        allLogs.removeIf(log -> !ProtectionUtils.canBreak(player, log));

        if (allLogs.isEmpty()) {
            return;
        }

        TreeCutService.addTask(player, allLogs);
    }
}
