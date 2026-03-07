package fr.haily.hTreecapitator.utils;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class WorldGuardUtils {

    public static boolean canBreak(Player player, Location location) {
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);

        if (WorldGuard.getInstance().getPlatform().getSessionManager().hasBypass(localPlayer, localPlayer.getWorld())) {
            return true;
        }

        var wgLocation = BukkitAdapter.adapt(location);
        var container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();

        ApplicableRegionSet regions = query.getApplicableRegions(wgLocation);

        boolean hasRealRegion = false;
        for (ProtectedRegion region : regions) {
            if (!region.getId().equals("__global__")) {
                hasRealRegion = true;
                break;
            }
        }

        if (!hasRealRegion) {
            return true;
        }

        return query.testState(wgLocation, localPlayer, Flags.BLOCK_BREAK);
    }
}