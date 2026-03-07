package fr.haily.hTreecapitator.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.util.concurrent.ThreadLocalRandom;

public class DurabilityUtils {

    private DurabilityUtils() {
        throw new IllegalStateException("This class cannot be instantiated");
    }

    public static boolean damageTool(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() == Material.AIR) {
            return false;
        }

        if (!(item.getItemMeta() instanceof Damageable damageable)) {
            return true;
        }

        int maxDurability = item.getType().getMaxDurability();

        if (maxDurability == 0) {
            return true;
        }

        int unbreakingLevel = item.getEnchantmentLevel(Enchantment.UNBREAKING);
        if (unbreakingLevel > 0) {
            if (ThreadLocalRandom.current().nextInt(unbreakingLevel + 1) > 0) {
                return true;
            }
        }

        damageable.setDamage(damageable.getDamage() + 1);

        if (damageable.getDamage() >= maxDurability) {
            player.getInventory().setItemInMainHand(null);
            return false;
        }

        item.setItemMeta(damageable);
        return true;
    }

    public static int getRemainingUses(ItemStack item) {
        if (item.getType() == Material.AIR) {
            return 0;
        }

        if (!(item.getItemMeta() instanceof Damageable damageable)) {
            return Integer.MAX_VALUE;
        }

        int maxDurability = item.getType().getMaxDurability();

        if (maxDurability == 0) {
            return Integer.MAX_VALUE;
        }

        return maxDurability - damageable.getDamage();
    }
}
