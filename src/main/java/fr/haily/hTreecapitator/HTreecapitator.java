package fr.haily.hTreecapitator;

import fr.haily.hTreecapitator.listener.JobsTreeCutListener;
import fr.haily.hTreecapitator.listener.TreeCutListener;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class HTreecapitator extends JavaPlugin {

    private static HTreecapitator instance;

    public final BlockFace[] faces = new BlockFace[] { BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH, BlockFace.SOUTH_WEST, BlockFace.WEST, BlockFace.NORTH_WEST };

    @Override
    public void onEnable() {
        saveDefaultConfig();

        instance = this;

        if(getServer().getPluginManager().getPlugin("Jobs") != null) {
            getServer().getPluginManager().registerEvents(new JobsTreeCutListener(), this);
        } else {
            getServer().getPluginManager().registerEvents(new TreeCutListener(), this);
        }

    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public static HTreecapitator getInstance() {
        return instance;
    }

    public boolean isLog(Material material) {
        String name = material.name();
        if(getConfig().getBoolean("options.mangrove_roots") && name.equals("MANGROVE_ROOTS")) {
            return true;
        }
        return (name.endsWith("LOG") || name.equals("CRIMSON_STEM") || name.equalsIgnoreCase("WARPED_STEM")) && !name.startsWith("STRIPPED");
    }

    public boolean isLog(Block block) {
        return isLog(block.getType());
    }

    public boolean isNaturalTree(Block block) {
        List<Block> checkedLogs = new ArrayList<>();
        List<Block> toCheck = new ArrayList<>();
        toCheck.add(block);

        while (!toCheck.isEmpty()) {
            Block current = toCheck.remove(0);
            checkedLogs.add(current);

            for (BlockFace face : faces) {
                BlockFace[] relatives = new BlockFace[] { BlockFace.SELF, BlockFace.UP, BlockFace.DOWN };
                for (BlockFace rel : relatives) {
                    Block neighbor = current.getRelative(face).getRelative(rel);

                    if (neighbor.getBlockData() instanceof Leaves leaves) {
                        if (!leaves.isPersistent()) {
                            return true;
                        }
                    }

                    if (HTreecapitator.getInstance().isLog(neighbor) && !checkedLogs.contains(neighbor)) {
                        toCheck.add(neighbor);
                    }
                }
            }

            if (checkedLogs.size() > HTreecapitator.getInstance().getConfig().getInt("options.max_logs")) {
                return false;
            }
        }

        return false;
    }

}
