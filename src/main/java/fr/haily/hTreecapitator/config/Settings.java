package fr.haily.hTreecapitator.config;

import fr.haily.hTreecapitator.HTreecapitator;

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

        maxBlocks = config.getInt("max-blocks");
        autoPickup = config.getBoolean("auto-pickup-drops");
        shiftMining = config.getBoolean("shift-mining");
        axeOnly = config.getBoolean("axe-only");
        requireEnchantment = config.getBoolean("require-enchantment");

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
