package ws.extension.gravitykilltracker;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class PlayerBlockChecker {
    // Width of player collision box
    public static final float
        PLAYER_HEIGHT = 1.8f,
        PLAYER_WIDTH = 0.6f,
        PLAYER_RADIUS = PLAYER_WIDTH/2.0f;

    /**
     * Test if a player is climbing anything
     * @param player A player
     * @return true if they are hanging on a ladder or vine
     */
    public boolean isPlayerClimbing(Player player) {
        int blockId = player.getWorld().getBlockTypeIdAt(player.getLocation());
        return blockId == Material.LADDER.getId() || blockId == Material.VINE.getId();
    }

    /**
     * Test if a player is in a particular kind of liquid. Yes, this is the same test
     * that Minecraft does. It *tries* to account for the height of the liquid, but
     * actually treats all levels the same due to a bug.
     * @param player A player
     * @param material Kind of liquid, should be WATER or LAVA
     * @return true if the player is touching that kind of liquid
     */
    public boolean isPlayerSwimming(Player player, Material material) {
        Location location = player.getLocation();
        World world = player.getWorld();
        Material alternate = null;
        switch (material) {
            case WATER: alternate = Material.STATIONARY_WATER; break;
            case LAVA: alternate = Material.STATIONARY_LAVA; break;
        }

        int x1 = (int) Math.floor(location.getX() - PLAYER_RADIUS);
        int y1 = (int) Math.floor(location.getY());
        int z1 = (int) Math.floor(location.getZ() - PLAYER_RADIUS);

        int x2 = (int) Math.floor(location.getX() + PLAYER_RADIUS);
        int y2 = (int) Math.floor(location.getY() + PLAYER_HEIGHT);
        int z2 = (int) Math.floor(location.getZ() + PLAYER_RADIUS);

        for (int x = x1; x <= x2; ++x) {
            for (int y = y1; y <= y2; ++y) {
                for (int z = z1; z <= z2; ++z) {
                    Block block = world.getBlockAt(x,y,z);
                    if (block != null) {
                        Material type = block.getType();
                        if (type == material || type == alternate) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }
}
