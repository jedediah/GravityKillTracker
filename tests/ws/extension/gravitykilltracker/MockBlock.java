package ws.extension.gravitykilltracker;

import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.List;

public class MockBlock implements Block {

    public final Location location;

    public MockBlock(int x, int y, int z) {
        this(new Location(null, x, y, z));
    }

    public MockBlock(Location location) {
        this.location = location;
    }

    public int getX() {
        return Location.locToBlock(location.getX());
    }

    public int getY() {
        return Location.locToBlock(location.getY());
    }

    public int getZ() {
        return Location.locToBlock(location.getZ());
    }

    public Location getLocation() {
        return location;
    }

    public Location getLocation(Location loc) {
        loc.setWorld(location.getWorld());
        loc.setX(location.getX());
        loc.setY(location.getY());
        loc.setZ(location.getZ());
        loc.setPitch(location.getPitch());
        loc.setYaw(location.getYaw());
        return loc;
    }

    public byte getData() {
        return 0;
    }

    public Block getRelative(int modX, int modY, int modZ) {
        return null;
    }

    public Block getRelative(BlockFace face) {
        return null;
    }

    public Block getRelative(BlockFace face, int distance) {
        return null;
    }

    public Material getType() {
        return null;
    }

    public int getTypeId() {
        return 0;
    }

    public byte getLightLevel() {
        return 0;
    }

    public byte getLightFromSky() {
        return 0;
    }

    public byte getLightFromBlocks() {
        return 0;
    }

    public World getWorld() {
        return null;
    }

    public Chunk getChunk() {
        return null;
    }

    public void setData(byte data) {
    }

    public void setData(byte data, boolean applyPhysics) {
    }

    public void setType(Material type) {
    }

    public boolean setTypeId(int type) {
        return false;
    }

    public boolean setTypeId(int type, boolean applyPhysics) {
        return false;
    }

    public boolean setTypeIdAndData(int type, byte data, boolean applyPhysics) {
        return false;
    }

    public BlockFace getFace(Block block) {
        return null;
    }

    public BlockState getState() {
        return null;
    }

    public Biome getBiome() {
        return null;
    }

    public void setBiome(Biome bio) {
    }

    public boolean isBlockPowered() {
        return false;
    }

    public boolean isBlockIndirectlyPowered() {
        return false;
    }

    public boolean isBlockFacePowered(BlockFace face) {
        return false;
    }

    public boolean isBlockFaceIndirectlyPowered(BlockFace face) {
        return false;
    }

    public int getBlockPower(BlockFace face) {
        return 0;
    }

    public int getBlockPower() {
        return 0;
    }

    public boolean isEmpty() {
        return false;
    }

    public boolean isLiquid() {
        return false;
    }

    public double getTemperature() {
        return 0;
    }

    public double getHumidity() {
        return 0;
    }

    public PistonMoveReaction getPistonMoveReaction() {
        return null;
    }

    public boolean breakNaturally() {
        return false;
    }

    public boolean breakNaturally(ItemStack tool) {
        return false;
    }

    public Collection<ItemStack> getDrops() {
        return null;
    }

    public Collection<ItemStack> getDrops(ItemStack tool) {
        return null;
    }

    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
    }

    public List<MetadataValue> getMetadata(String metadataKey) {
        return null;
    }

    public boolean hasMetadata(String metadataKey) {
        return false;
    }

    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
    }
}
