package fr.haily.hTreecapitator.utils;

import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.actions.BlockActionInfo;
import com.gamingmesh.jobs.container.ActionType;
import com.gamingmesh.jobs.container.JobsPlayer;
import org.bukkit.block.Block;

import java.util.UUID;

public class JobsUtils {

    public static void giveJobsXp(UUID playerUuid, Block block) {
        JobsPlayer jobsPlayer = Jobs.getPlayerManager().getJobsPlayer(playerUuid);
        if (jobsPlayer != null) {
            Jobs.action(jobsPlayer, new BlockActionInfo(block, ActionType.BREAK), block);
        }
    }
}
