package fr.haily.hTreecapitator.service;

import java.util.HashMap;
import java.util.UUID;

public class ToggleService {
    private static final HashMap<UUID, Boolean> toggleMap = new HashMap<>();

    private ToggleService() {
        throw new IllegalStateException("This class cannot be instantiated");
    }

    public static boolean getToggle(UUID playerUUID){
        toggleMap.putIfAbsent(playerUUID, true);
        return toggleMap.get(playerUUID);
    }

    public static boolean changeToggle(UUID playerUUID){
        boolean toggle = getToggle(playerUUID);

        toggleMap.put(playerUUID, !toggle);

        return !toggle;
    }
}
