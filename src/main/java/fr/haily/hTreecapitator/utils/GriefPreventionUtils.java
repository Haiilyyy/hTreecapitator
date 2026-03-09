package fr.haily.hTreecapitator.utils;

import fr.haily.hTreecapitator.HTreecapitator;
import me.ryanhamshire.GriefPrevention.ClaimPermission;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.Claim;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.function.Supplier;

public class GriefPreventionUtils {

    public static boolean canBreak(Player player, Location location) {
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(location, false, null);

        if (claim == null) {
            return true;
        }

        Supplier<String> result = claim.checkPermission(player, ClaimPermission.Build, null);

        return result == null;
    }
}