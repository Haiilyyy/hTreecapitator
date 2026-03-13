package fr.haily.hTreecapitator.utils;

import fr.haily.hTreecapitator.HTreecapitator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.stream.Collectors;

public class UpdateChecker implements Listener {

    private static String latestVersion = null;
    private static boolean updateAvailable = false;
    private static long lastCheckTime = 0;
    private static final long CHECK_INTERVAL = 10800000;

    public UpdateChecker() {}

    public static void check() {
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastCheckTime < CHECK_INTERVAL) {
            return;
        }

        lastCheckTime = currentTime;

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
                } else {
                    updateAvailable = false;
                }

            } catch (Exception e) {
                HTreecapitator.getInstance().getLogger().warning("Update check failed: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    private static String fetchLatestRelease() throws Exception {
        try {
            URL url = new URI("https://api.github.com/repos/Haiilyyy/hTreecapitator/releases/latest").toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/vnd.github.v3+json");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                HTreecapitator.getInstance().getLogger().warning("Failed to fetch update: HTTP " + responseCode);
                connection.disconnect();
                return null;
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                return reader.lines().collect(Collectors.joining());
            } finally {
                connection.disconnect();
            }
        } catch (Exception e) {
            HTreecapitator.getInstance().getLogger().warning("Error fetching update: " + e.getMessage());
            throw e;
        }
    }

    private static String parseTagName(String json) {
        try {
            int tagIndex = json.indexOf("\"tag_name\"");
            if (tagIndex == -1) {
                return null;
            }

            int start = json.indexOf('"', tagIndex + 10) + 1;
            if (start <= 0) return null;

            int end = json.indexOf('"', start);
            if (end == -1) return null;

            return json.substring(start, end);
        } catch (Exception e) {
            HTreecapitator.getInstance().getLogger().warning("Error parsing version: " + e.getMessage());
            return null;
        }
    }

    private static boolean isNewerVersion(String latest, String current) {
        try {
            latest = latest.toLowerCase().replace("v", "").trim();
            current = current.toLowerCase().replace("v", "").trim();

            String[] latestParts = latest.split("[^0-9]+");
            String[] currentParts = current.split("[^0-9]+");

            int length = Math.max(latestParts.length, currentParts.length);

            for (int i = 0; i < length; i++) {
                int latestNum = i < latestParts.length ? parseIntSafe(latestParts[i]) : 0;
                int currentNum = i < currentParts.length ? parseIntSafe(currentParts[i]) : 0;

                if (latestNum > currentNum) {
                    return true;
                }
                if (latestNum < currentNum) {
                    return false;
                }
            }

            return false;
        } catch (Exception e) {
            HTreecapitator.getInstance().getLogger().warning("Error comparing versions: " + e.getMessage());
            return false;
        }
    }

    private static int parseIntSafe(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!updateAvailable) {
            return;
        }

        Player player = event.getPlayer();

        if (!player.hasPermission("htreecapitator.updatenotify")) {
            return;
        }

        Bukkit.getScheduler().runTaskLater(HTreecapitator.getInstance(), () -> {
            player.sendMessage(ChatUtils.format("&7A update for &ahTreecapitator &7(&fv" + latestVersion + "&7) is available at &ahttps://modrinth.com/plugin/htreecapitator"));
        }, 60L);
    }
}
