package fr.haily.hTreecapitator.utils;

import fr.haily.hTreecapitator.HTreecapitator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class EnchantUtils {

    private static final NamespacedKey ENCHANT_KEY = new NamespacedKey(HTreecapitator.getInstance(), "treecapitator");

    private EnchantUtils() {
        throw new IllegalStateException("This class cannot be instantiated");
    }

    public static boolean hasEnchant(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }

        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        if (pdc.has(ENCHANT_KEY, PersistentDataType.BYTE)) {
            return true;
        }

        if (meta instanceof EnchantmentStorageMeta storageMeta) {
            return storageMeta.getPersistentDataContainer().has(ENCHANT_KEY, PersistentDataType.BYTE);
        }

        return false;
    }

    public static boolean applyEnchant(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }

        if (meta.getPersistentDataContainer().has(ENCHANT_KEY, PersistentDataType.BYTE)) {
            return false;
        }

        meta.getPersistentDataContainer().set(ENCHANT_KEY, PersistentDataType.BYTE, (byte) 1);
        meta.setEnchantmentGlintOverride(true);

        List<String> lore = meta.hasLore() ? new ArrayList<>(meta.getLore()) : new ArrayList<>();
        lore.add(ChatColor.GRAY + "Treecapitator I");
        meta.setLore(lore);

        item.setItemMeta(meta);
        return true;
    }

    public static ItemStack createEnchantedBook() {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) book.getItemMeta();

        if (meta == null) {
            return book;
        }

        meta.getPersistentDataContainer().set(ENCHANT_KEY, PersistentDataType.BYTE, (byte) 1);
        meta.setEnchantmentGlintOverride(true);

        meta.setDisplayName(ChatColor.AQUA + "Enchanted Book");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Treecapitator I");
        meta.setLore(lore);

        book.setItemMeta(meta);
        return book;
    }

    public static NamespacedKey getKey() {
        return ENCHANT_KEY;
    }
}