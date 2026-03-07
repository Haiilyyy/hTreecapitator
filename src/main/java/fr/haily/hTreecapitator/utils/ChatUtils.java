package fr.haily.hTreecapitator.utils;

import org.bukkit.ChatColor;

public class ChatUtils {

    private ChatUtils() {
        throw new IllegalStateException("This class cannot be instantiated");
    }

    public static String format(String message){
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
