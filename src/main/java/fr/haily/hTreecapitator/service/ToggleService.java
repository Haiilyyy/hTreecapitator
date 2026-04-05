package fr.haily.hTreecapitator.service;

import fr.haily.hTreecapitator.HTreecapitator;
import org.bukkit.configuration.file.YamlConfiguration;

import static fr.haily.hTreecapitator.HTreecapitator.ERROR_TRACKER;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class ToggleService {

    private static final Set<UUID> disabledPlayers = new HashSet<>();

    private ToggleService() {
        throw new IllegalStateException("This class cannot be instantiated");
    }

    public static void load() {
        File file = getFile();
        if (!file.exists()) return;

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        disabledPlayers.clear();

        for (String entry : config.getStringList("disabled")) {
            try {
                disabledPlayers.add(UUID.fromString(entry));
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private static void save() {
        YamlConfiguration config = new YamlConfiguration();
        config.set("disabled", disabledPlayers.stream().map(UUID::toString).collect(Collectors.toList()));

        try {
            config.save(getFile());
        } catch (IOException e) {
            HTreecapitator.getInstance().getLogger().warning("Failed to save toggles: " + e.getMessage());
            ERROR_TRACKER.trackError(e);
        }
    }

    private static File getFile() {
        return new File(HTreecapitator.getInstance().getDataFolder(), "toggles.yml");
    }

    public static boolean getToggle(UUID playerUUID) {
        return !disabledPlayers.contains(playerUUID);
    }

    public static boolean changeToggle(UUID playerUUID) {
        boolean nowEnabled;
        if (disabledPlayers.contains(playerUUID)) {
            disabledPlayers.remove(playerUUID);
            nowEnabled = true;
        } else {
            disabledPlayers.add(playerUUID);
            nowEnabled = false;
        }
        save();
        return nowEnabled;
    }
}
