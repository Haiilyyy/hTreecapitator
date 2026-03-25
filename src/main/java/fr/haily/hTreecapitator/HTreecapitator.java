package fr.haily.hTreecapitator;

import fr.haily.hTreecapitator.commands.hTreecapitatorCommand;
import fr.haily.hTreecapitator.config.Settings;
import fr.haily.hTreecapitator.listener.AnvilListener;
import fr.haily.hTreecapitator.listener.TreeCutListener;
import fr.haily.hTreecapitator.placeholders.PlaceholderRegistry;
import fr.haily.hTreecapitator.service.LeafDecayService;
import fr.haily.hTreecapitator.service.ToggleService;
import fr.haily.hTreecapitator.service.TreeCutService;
import fr.haily.hTreecapitator.utils.UpdateChecker;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class HTreecapitator extends JavaPlugin {

    private static HTreecapitator instance;

    public static HTreecapitator getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        Settings.loadConfig(false);
        ToggleService.load();

        TreeCutService.start();
        LeafDecayService.start();

        infoMessage();
        checkPlugins();

        registerListeners();
        registerCommand();
        registerPapi();

        loadBStats();
        UpdateChecker.check();
    }

    private void infoMessage() {
        var logger = getLogger();
        logger.info("");
        logger.info("hTreecapitator by Haily!");
        logger.info("");
    }

    private void checkPlugins() {
        Settings.setPlaceholderApiEnabled(checkPlugin("PlaceholderAPI"));
        Settings.setJobsEnabled(checkPlugin("Jobs"));
        Settings.setWorldGuardEnabled(checkPlugin("WorldGuard"));
        Settings.setGriefPreventionEnabled(checkPlugin("GriefPrevention"));
    }

    private boolean checkPlugin(String pluginName) {
        var plugin = getServer().getPluginManager().getPlugin(pluginName);

        if (plugin != null && plugin.isEnabled()) {
            getLogger().info("Hooked to: " + pluginName);
            return true;
        }
        return false;
    }

    private void registerListeners() {
        var pm = getServer().getPluginManager();
        pm.registerEvents(new TreeCutListener(), this);
        pm.registerEvents(new AnvilListener(), this);
        pm.registerEvents(new UpdateChecker(), this);
    }

    private void registerCommand() {
        Objects.requireNonNull(getCommand("htreecapitator")).setExecutor(new hTreecapitatorCommand());
    }

    private void registerPapi() {
        if (Settings.isPlaceholderApiEnabled()) {
            new PlaceholderRegistry().register();
        }
    }

    private void loadBStats() {
        new Metrics(this, ***REMOVED***);
    }
}
