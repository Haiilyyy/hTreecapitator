package fr.haily.hTreecapitator.utils;

import fr.haily.hTreecapitator.HTreecapitator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

public class UpdateChecker implements Listener {

    private static String latestVersion = null;
    private static boolean updateAvailable = false;

    public UpdateChecker() {}

    public static void check() {
        Bukkit.getScheduler().runTaskAsynchronously(HTreecapitator.getInstance(), () -> {
            try {
                String json = fetchLatestRelease();

                if (json == null) {
                    return;
                }

                latestVersion = parseTagName(json);

                if (latestVersion == null) {
                    return;
                }

                String currentVersion = HTreecapitator.getInstance().getDescription().getVersion();

                if (isNewerVersion(latestVersion, currentVersion)) {
                    updateAvailable = true;
                    HTreecapitator.getInstance().getLogger().info("New version available: " + latestVersion);
                }

            } catch (Exception ignored) {}
        });
    }

    private static String fetchLatestRelease() throws Exception {
        URL url = new URL("https://api.github.com/repos/Haiilyyy/hTreecapitator/releases/latest");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/vnd.github.v3+json");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        if (connection.getResponseCode() != 200) {
            connection.disconnect();
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            return reader.lines().collect(Collectors.joining());
        } finally {
            connection.disconnect();
        }
    }

    private static String parseTagName(String json) {
        int tagIndex = json.indexOf("\"tag_name\"");
        if (tagIndex == -1) {
            return null;
        }
        int start = json.indexOf("\"", tagIndex + 10) + 1;
        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }

    private static boolean isNewerVersion(String latest, String current) {
        // Remove 'v' prefix if present
        latest = latest.toLowerCase().replace("v", "");
        current = current.toLowerCase().replace("v", "");

        String[] latestParts = latest.split("\\.");
        String[] currentParts = current.split("\\.");

        int length = Math.max(latestParts.length, currentParts.length);

        for (int i = 0; i < length; i++) {
            int latestNum = i < latestParts.length ? Integer.parseInt(latestParts[i]) : 0;
            int currentNum = i < currentParts.length ? Integer.parseInt(currentParts[i]) : 0;

            if (latestNum > currentNum) {
                return true;
            }
            if (latestNum < currentNum) {
                return false;
            }
        }

        return false;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!updateAvailable) {
            return;
        }

        Player player = event.getPlayer();

        if (!player.isOp()) {
            return;
        }

        if(!player.hasPermission("*")){
            return;
        }

        Bukkit.getScheduler().runTaskLater(HTreecapitator.getInstance(), () -> {
            player.sendMessage(ChatUtils.format("&7A update for &ahTreecapitator &7(&fv"+latestVersion+"&7) is available at &ahttps://modrinth.com/plugin/htreecapitator"));
        }, 40L);
    }
}