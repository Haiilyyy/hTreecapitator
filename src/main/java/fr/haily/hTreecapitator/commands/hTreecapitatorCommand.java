package fr.haily.hTreecapitator.commands;

import fr.haily.hTreecapitator.config.Settings;
import fr.haily.hTreecapitator.service.ToggleService;
import fr.haily.hTreecapitator.utils.ChatUtils;
import fr.haily.hTreecapitator.utils.EnchantUtils;
import fr.haily.hTreecapitator.utils.PermissionsUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class hTreecapitatorCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 0) {
            sendInfo(sender);
            return true;
        }

        String arg = args[0].toUpperCase();

        switch (arg) {
            case "RELOAD" -> reload(sender);
            case "TOGGLE" -> toggle(sender);
            case "ENCHANT" -> enchant(sender, args);
            default -> sendInfo(sender);
        }
        return true;
    }

    private static void sendInfo(CommandSender sender) {
        if (!sender.hasPermission("htreecapitator.help")) {
            sender.sendMessage(ChatUtils.format(Settings.getPermissionMessage()));
            return;
        }

        Settings.getHelpMessage()
                .stream()
                .map(ChatUtils::format)
                .forEach(sender::sendMessage);
    }

    private void reload(CommandSender sender) {
        if (hasNotPermission(sender, "reload")) return;

        sender.sendMessage(ChatUtils.format(Settings.getReloadStart()));
        Settings.loadConfig(true);
        sender.sendMessage(ChatUtils.format(Settings.getReloadEnd()));
    }

    private void toggle(CommandSender sender) {
        if (hasNotPermission(sender, "toggle")) {
            return;
        }

        if (!(sender instanceof Player p)) {
            sender.sendMessage(Settings.getConsoleMessage());
            return;
        }

        boolean status = ToggleService.changeToggle(p.getUniqueId());

        String message = status ? Settings.getToggleMessageOn() : Settings.getToggleMessageOff();

        p.sendMessage(ChatUtils.format(message));
    }

    private void enchant(CommandSender sender, String[] args) {
        if (hasNotPermission(sender, "enchant")) {
            return;
        }

        // Determine target player
        Player target;

        if (args.length >= 2) {
            // /htc enchant <player>
            target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(ChatUtils.format("&c&l» &7Player not found!"));
                return;
            }
        } else {
            // /htc enchant (self)
            if (!(sender instanceof Player p)) {
                sender.sendMessage(ChatUtils.format(Settings.getConsoleMessage()));
                return;
            }
            target = p;
        }

        // Give enchanted book
        ItemStack book = EnchantUtils.createEnchantedBook();
        target.getInventory().addItem(book);

        // Send messages
        if (target.equals(sender)) {
            target.sendMessage(ChatUtils.format(Settings.getEnchantGiveMessage()));
        } else {
            String message = Settings.getEnchantGiveOtherMessage()
                    .replace("{player}", target.getName());
            sender.sendMessage(ChatUtils.format(message));
            target.sendMessage(ChatUtils.format(Settings.getEnchantGiveMessage()));
        }
    }

    private boolean hasNotPermission(CommandSender sender, String permission) {
        String perm = "htreecapitator." + permission;

        if (!PermissionsUtils.hasPermission(sender, perm)) {
            sender.sendMessage(ChatUtils.format(Settings.getPermissionMessage()));
            return true;
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            return List.of("reload", "toggle", "enchant");
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("enchant")) {
            List<String> players = new ArrayList<>();
            for (Player p : Bukkit.getOnlinePlayers()) {
                players.add(p.getName());
            }
            return players;
        }

        return List.of();
    }
}
