package fr.haily.hTreecapitator.listener;

import fr.haily.hTreecapitator.utils.EnchantUtils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;

public class AnvilListener implements Listener {

    @EventHandler
    public void onAnvilPrepare(PrepareAnvilEvent event) {
        AnvilInventory inv = event.getInventory();

        ItemStack first = inv.getItem(0);
        ItemStack second = inv.getItem(1);

        if (first == null || second == null) {
            return;
        }

        boolean firstIsAxe = first.getType().name().endsWith("_AXE");
        boolean secondIsBook = second.getType() == Material.ENCHANTED_BOOK && EnchantUtils.hasEnchant(second);

        if (!firstIsAxe || !secondIsBook) {
            return;
        }

        if (EnchantUtils.hasEnchant(first)) {
            return;
        }

        ItemStack result = first.clone();
        if (!EnchantUtils.applyEnchant(result)) {
            return;
        }

        event.setResult(result);
        inv.setRepairCost(1);
    }
}
