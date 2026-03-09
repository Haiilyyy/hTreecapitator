package fr.haily.hTreecapitator.config;

import fr.haily.hTreecapitator.HTreecapitator;
import org.bukkit.Sound;

import java.util.List;

public class Settings {

    private Settings() {
        throw new IllegalStateException("This class cannot be instantiated");
    }

    //addons

    private static boolean placeholderApiEnabled;
    public static boolean isPlaceholderApiEnabled(){
        return placeholderApiEnabled;
    }
    public static void setPlaceholderApiEnabled(boolean placeholderAPI) {
        placeholderApiEnabled = placeholderAPI;
    }

    private static boolean jobsEnabled;
    public static boolean isJobsEnabled(){
        return jobsEnabled;
    }
    public static void setJobsEnabled(boolean jobs) {
        jobsEnabled = jobs;
    }

    private static boolean worldGuardEnabled;
    public static boolean isWorldGuardEnabled() {
        return worldGuardEnabled;
    }
    public static void setWorldGuardEnabled(boolean worldGuard) {
        worldGuardEnabled = worldGuard;
    }

    private static boolean griefPreventionEnabled;
    public static boolean isGriefPreventionEnabled() {
        return griefPreventionEnabled;
    }
    public static void setGriefPreventionEnabled(boolean GriefPrevention) {
        griefPreventionEnabled = GriefPrevention;
    }

    //block limits
    private static int maxBlocks;
    public static int getMaxBlocks(){
        return maxBlocks;
    }

    //settings
    private static boolean autoPickup;
    public static boolean getAutoPickup(){
        return autoPickup;
    }

    private static boolean shiftMining;
    public static boolean getShiftMining(){
        return shiftMining;
    }

    private static boolean axeOnly;
    public static boolean getAxeOnly(){
        return axeOnly;
    }

    private static boolean requireEnchantment;
    public static boolean getRequireEnchantment(){
        return requireEnchantment;
    }

    private static String placeholderToggleOn;
    public static String getPlaceholderToggleOn(){
        return placeholderToggleOn;
    }

    private static String placeholderToggleOff;
    public static String getPlaceholderToggleOff(){
        return placeholderToggleOff;
    }

    private static List<String> blockedWorlds;
    public static List<String> getBlockedWorlds() {
        return blockedWorlds;
    }
    public static boolean isWorldBlocked(String worldName) {
        return blockedWorlds.contains(worldName);
    }

    private static boolean instantBreakLogs;
    public static boolean getInstantBreakLogs() {
        return instantBreakLogs;
    }

    private static boolean instantBreakLeaves;
    public static boolean getInstantBreakLeaves() {
        return instantBreakLeaves;
    }

    private static boolean soundEnabled;
    public static boolean getSoundEnabled() {
        return soundEnabled;
    }

    private static String soundType;
    public static String getSoundType() {
        return soundType;
    }

    public static Sound getSound() {
        return switch (soundType.toUpperCase()) {
            case "BELL" -> Sound.BLOCK_NOTE_BLOCK_BELL;
            case "ORB" -> Sound.ENTITY_EXPERIENCE_ORB_PICKUP;
            default -> Sound.BLOCK_WOOD_BREAK;
        };
    }

    //permission
    private static boolean usePermissions;
    public static boolean getUsePermissions(){
        return usePermissions;
    }

    private static String permission;
    public static String getPermission(){
        return permission;
    }

    //messages
    private static String permissionMessage;
    public static String getPermissionMessage(){
        return permissionMessage;
    }

    private static String toggleMessageOn;
    public static String getToggleMessageOn(){
        return toggleMessageOn;
    }

    private static String toggleMessageOff;
    public static String getToggleMessageOff(){
        return toggleMessageOff;
    }

    private static String consoleMessage;
    public static String getConsoleMessage(){
        return consoleMessage;
    }

    private static String reloadStart;
    public static String getReloadStart(){
        return reloadStart;
    }

    private static String reloadEnd;
    public static String getReloadEnd(){
        return reloadEnd;
    }

    private static String enchantGiveMessage;
    public static String getEnchantGiveMessage(){
        return enchantGiveMessage;
    }

    private static String enchantGiveOtherMessage;
    public static String getEnchantGiveOtherMessage(){
        return enchantGiveOtherMessage;
    }

    private static List<String> helpMessage;
    public static List<String> getHelpMessage(){
        return helpMessage;
    }

    public static void loadConfig(boolean reload) {
        var javaPlugin = HTreecapitator.getInstance();

        if(!reload) javaPlugin.saveDefaultConfig();
        else javaPlugin.reloadConfig();

        var config = javaPlugin.getConfig();

        maxBlocks = config.getInt("max-blocks", 128);
        autoPickup = config.getBoolean("auto-pickup-drops", false);
        shiftMining = config.getBoolean("shift-mining", false);
        axeOnly = config.getBoolean("axe-only", true);
        requireEnchantment = config.getBoolean("require-enchantment", false);
        blockedWorlds = config.getStringList("blocked-worlds");
        instantBreakLogs = config.getBoolean("instant-break-logs", false);
        instantBreakLeaves = config.getBoolean("instant-break-leaves", false);
        soundEnabled = config.getBoolean("sound", false);
        soundType = config.getString("sound-type", "WOOD");

        placeholderToggleOn = config.getString("placeholder.toggle-on");
        placeholderToggleOff = config.getString("placeholder.toggle-off");

        usePermissions = config.getBoolean("use-permissions");
        permission = config.getString("permission");

        permissionMessage = config.getString("messages.permission");
        consoleMessage = config.getString("messages.console");
        helpMessage = config.getStringList("messages.help");
        reloadStart = config.getString("messages.reload.start");
        reloadEnd = config.getString("messages.reload.end");
        toggleMessageOn = config.getString("messages.toggle.enable");
        toggleMessageOff = config.getString("messages.toggle.disable");
        enchantGiveMessage = config.getString("messages.enchant.give");
        enchantGiveOtherMessage = config.getString("messages.enchant.give-other");
    }

}
