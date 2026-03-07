package fr.haily.hTreecapitator.placeholders;

import fr.haily.hTreecapitator.config.Settings;
import fr.haily.hTreecapitator.service.ToggleService;
import fr.haily.hTreecapitator.utils.ChatUtils;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jspecify.annotations.NonNull;

public class PlaceholderRegistry extends PlaceholderExpansion {

    @Override
    public @NonNull String getIdentifier() {
        return "htreecapitator";
    }

    @Override
    public @NonNull String getAuthor() {
        return "Haily";
    }

    @Override
    public @NonNull String getVersion() {
        return "1.8.7";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if (params.contains("toggle_raw")) {
            if(!ToggleService.getToggle(player.getUniqueId())){
                return  "false";
            }
            return "true";
        }else if (params.contains("toggle")) {
            if(!ToggleService.getToggle(player.getUniqueId())){
                return ChatUtils.format(Settings.getPlaceholderToggleOff());
            }
            return ChatUtils.format(Settings.getPlaceholderToggleOn());
        }
        return "";
    }


}
