package ws.extension.gravitykilltracker;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.UUID;

public class MockEntity implements Entity {

    public Location location;
    public EntityDamageEvent lastDamageEvent;

    public MockEntity() {
        location = new Location(null, 0, 0, 0);
    }

    @Override
    public String toString() {
        return getType().getName();
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

    public void setVelocity(Vector velocity) {
        throw new NotMockedException();
    }

    public Vector getVelocity() {
        throw new NotMockedException();
    }

    public boolean isOnGround() {
        throw new NotMockedException();
    }

    public World getWorld() {
        throw new NotMockedException();
    }

    public boolean teleport(Location location) {
        throw new NotMockedException();
    }

    public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause cause) {
        throw new NotMockedException();
    }

    public boolean teleport(Entity destination) {
        throw new NotMockedException();
    }

    public boolean teleport(Entity destination, PlayerTeleportEvent.TeleportCause cause) {
        throw new NotMockedException();
    }

    public List<Entity> getNearbyEntities(double x, double y, double z) {
        throw new NotMockedException();
    }

    public int getEntityId() {
        throw new NotMockedException();
    }

    public int getFireTicks() {
        throw new NotMockedException();
    }

    public int getMaxFireTicks() {
        throw new NotMockedException();
    }

    public void setFireTicks(int ticks) {
        throw new NotMockedException();
    }

    public void remove() {
        throw new NotMockedException();
    }

    public boolean isDead() {
        throw new NotMockedException();
    }

    public boolean isValid() {
        throw new NotMockedException();
    }

    public Server getServer() {
        throw new NotMockedException();
    }

    public Entity getPassenger() {
        throw new NotMockedException();
    }

    public boolean setPassenger(Entity passenger) {
        throw new NotMockedException();
    }

    public boolean isEmpty() {
        throw new NotMockedException();
    }

    public boolean eject() {
        throw new NotMockedException();
    }

    public float getFallDistance() {
        throw new NotMockedException();
    }

    public void setFallDistance(float distance) {
        throw new NotMockedException();
    }

    public void setLastDamageCause(EntityDamageEvent event) {
        lastDamageEvent = event;
    }

    public EntityDamageEvent getLastDamageCause() {
        return lastDamageEvent;
    }

    public UUID getUniqueId() {
        throw new NotMockedException();
    }

    public int getTicksLived() {
        throw new NotMockedException();
    }

    public void setTicksLived(int value) {
        throw new NotMockedException();
    }

    public void playEffect(EntityEffect type) {
        throw new NotMockedException();
    }

    public EntityType getType() {
        throw new NotMockedException();
    }

    public boolean isInsideVehicle() {
        throw new NotMockedException();
    }

    public boolean leaveVehicle() {
        throw new NotMockedException();
    }

    public Entity getVehicle() {
        throw new NotMockedException();
    }

    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        throw new NotMockedException();
    }

    public List<MetadataValue> getMetadata(String metadataKey) {
        throw new NotMockedException();
    }

    public boolean hasMetadata(String metadataKey) {
        throw new NotMockedException();
    }

    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
        throw new NotMockedException();
    }
}
