package ws.extension.gravitykilltracker;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.*;
import org.bukkit.map.MapView;
import org.bukkit.permissions.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.net.InetSocketAddress;
import java.util.*;

public class MockPlayer extends MockEntity implements Player {
    public String name;
    public boolean onGround;

    public MockPlayer(String name) {
        this.name = name;
        onGround = true;
    }

    @Override
    public String toString() {
        return getDisplayName();
    }

    public String getName() {
        return name;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public String getDisplayName() {
        return name;
    }

    public void setDisplayName(String name) {
        this.name = name;
    }

    public String getPlayerListName() {
        throw new NotMockedException();
    }

    public void setPlayerListName(String name) {
        throw new NotMockedException();
    }

    public void setCompassTarget(Location loc) {
        throw new NotMockedException();
    }

    public Location getCompassTarget() {
        throw new NotMockedException();
    }

    public InetSocketAddress getAddress() {
        throw new NotMockedException();
    }

    public boolean isConversing() {
        throw new NotMockedException();
    }

    public void acceptConversationInput(String input) {
        throw new NotMockedException();
    }

    public boolean beginConversation(Conversation conversation) {
        throw new NotMockedException();
    }

    public void abandonConversation(Conversation conversation) {
        throw new NotMockedException();
    }

    public void abandonConversation(Conversation conversation, ConversationAbandonedEvent details) {
        throw new NotMockedException();
    }

    public void sendRawMessage(String message) {
        throw new NotMockedException();
    }

    public void kickPlayer(String message) {
        throw new NotMockedException();
    }

    public void chat(String msg) {
        throw new NotMockedException();
    }

    public boolean performCommand(String command) {
        throw new NotMockedException();
    }

    public boolean isSneaking() {
        throw new NotMockedException();
    }

    public void setSneaking(boolean sneak) {
        throw new NotMockedException();
    }

    public boolean isSprinting() {
        throw new NotMockedException();
    }

    public void setSprinting(boolean sprinting) {
        throw new NotMockedException();
    }

    public void saveData() {
        throw new NotMockedException();
    }

    public void loadData() {
        throw new NotMockedException();
    }

    public void setSleepingIgnored(boolean isSleeping) {
        throw new NotMockedException();
    }

    public boolean isSleepingIgnored() {
        throw new NotMockedException();
    }

    public void playNote(Location loc, byte instrument, byte note) {
        throw new NotMockedException();
    }

    public void playNote(Location loc, Instrument instrument, Note note) {
        throw new NotMockedException();
    }

    public void playSound(Location location, Sound sound, float volume, float pitch) {
        throw new NotMockedException();
    }

    public void playEffect(Location loc, Effect effect, int data) {
        throw new NotMockedException();
    }

    public <T> void playEffect(Location loc, Effect effect, T data) {
        throw new NotMockedException();
    }

    public void sendBlockChange(Location loc, Material material, byte data) {
        throw new NotMockedException();
    }

    public boolean sendChunkChange(Location loc, int sx, int sy, int sz, byte[] data) {
        throw new NotMockedException();
    }

    public void sendBlockChange(Location loc, int material, byte data) {
        throw new NotMockedException();
    }

    public void sendMap(MapView map) {
        throw new NotMockedException();
    }

    public void updateInventory() {
        throw new NotMockedException();
    }

    public void awardAchievement(Achievement achievement) {
        throw new NotMockedException();
    }

    public void incrementStatistic(Statistic statistic) {
        throw new NotMockedException();
    }

    public void incrementStatistic(Statistic statistic, int amount) {
        throw new NotMockedException();
    }

    public void incrementStatistic(Statistic statistic, Material material) {
        throw new NotMockedException();
    }

    public void incrementStatistic(Statistic statistic, Material material, int amount) {
        throw new NotMockedException();
    }

    public void setPlayerTime(long time, boolean relative) {
        throw new NotMockedException();
    }

    public long getPlayerTime() {
        throw new NotMockedException();
    }

    public long getPlayerTimeOffset() {
        throw new NotMockedException();
    }

    public boolean isPlayerTimeRelative() {
        throw new NotMockedException();
    }

    public void resetPlayerTime() {
        throw new NotMockedException();
    }

    public void giveExp(int amount) {
        throw new NotMockedException();
    }

    public void giveExpLevels(int amount) {
        throw new NotMockedException();
    }

    public float getExp() {
        throw new NotMockedException();
    }

    public void setExp(float exp) {
        throw new NotMockedException();
    }

    public int getLevel() {
        throw new NotMockedException();
    }

    public void setLevel(int level) {
        throw new NotMockedException();
    }

    public int getTotalExperience() {
        throw new NotMockedException();
    }

    public void setTotalExperience(int exp) {
        throw new NotMockedException();
    }

    public float getExhaustion() {
        throw new NotMockedException();
    }

    public void setExhaustion(float value) {
        throw new NotMockedException();
    }

    public float getSaturation() {
        throw new NotMockedException();
    }

    public void setSaturation(float value) {
        throw new NotMockedException();
    }

    public int getFoodLevel() {
        throw new NotMockedException();
    }

    public void setFoodLevel(int value) {
        throw new NotMockedException();
    }

    public Location getBedSpawnLocation() {
        throw new NotMockedException();
    }

    public void setBedSpawnLocation(Location location) {
        throw new NotMockedException();
    }

    public void setBedSpawnLocation(Location location, boolean force) {
        throw new NotMockedException();
    }

    public boolean getAllowFlight() {
        throw new NotMockedException();
    }

    public void setAllowFlight(boolean flight) {
        throw new NotMockedException();
    }

    public void hidePlayer(Player player) {
        throw new NotMockedException();
    }

    public void showPlayer(Player player) {
        throw new NotMockedException();
    }

    public boolean canSee(Player player) {
        throw new NotMockedException();
    }

    public boolean isFlying() {
        throw new NotMockedException();
    }

    public void setFlying(boolean value) {
        throw new NotMockedException();
    }

    public void setFlySpeed(float value) throws IllegalArgumentException {
        throw new NotMockedException();
    }

    public void setWalkSpeed(float value) throws IllegalArgumentException {
        throw new NotMockedException();
    }

    public float getFlySpeed() {
        throw new NotMockedException();
    }

    public float getWalkSpeed() {
        throw new NotMockedException();
    }

    public void setTexturePack(String url) {
        throw new NotMockedException();
    }

    public void sendMessage(String message) {
        throw new NotMockedException();
    }

    public void sendMessage(String[] messages) {
        throw new NotMockedException();
    }

    public Map<String, Object> serialize() {
        throw new NotMockedException();
    }

    public boolean isOnline() {
        throw new NotMockedException();
    }

    public boolean isBanned() {
        throw new NotMockedException();
    }

    public void setBanned(boolean banned) {
        throw new NotMockedException();
    }

    public boolean isWhitelisted() {
        throw new NotMockedException();
    }

    public void setWhitelisted(boolean value) {
        throw new NotMockedException();
    }

    public Player getPlayer() {
        throw new NotMockedException();
    }

    public long getFirstPlayed() {
        throw new NotMockedException();
    }

    public long getLastPlayed() {
        throw new NotMockedException();
    }

    public boolean hasPlayedBefore() {
        throw new NotMockedException();
    }

    public PlayerInventory getInventory() {
        throw new NotMockedException();
    }

    public Inventory getEnderChest() {
        throw new NotMockedException();
    }

    public boolean setWindowProperty(InventoryView.Property prop, int value) {
        throw new NotMockedException();
    }

    public InventoryView getOpenInventory() {
        throw new NotMockedException();
    }

    public InventoryView openInventory(Inventory inventory) {
        throw new NotMockedException();
    }

    public InventoryView openWorkbench(Location location, boolean force) {
        throw new NotMockedException();
    }

    public InventoryView openEnchanting(Location location, boolean force) {
        throw new NotMockedException();
    }

    public void openInventory(InventoryView inventory) {
        throw new NotMockedException();
    }

    public void closeInventory() {
        throw new NotMockedException();
    }

    public ItemStack getItemInHand() {
        throw new NotMockedException();
    }

    public void setItemInHand(ItemStack item) {
        throw new NotMockedException();
    }

    public ItemStack getItemOnCursor() {
        throw new NotMockedException();
    }

    public void setItemOnCursor(ItemStack item) {
        throw new NotMockedException();
    }

    public boolean isSleeping() {
        throw new NotMockedException();
    }

    public int getSleepTicks() {
        throw new NotMockedException();
    }

    public GameMode getGameMode() {
        throw new NotMockedException();
    }

    public void setGameMode(GameMode mode) {
        throw new NotMockedException();
    }

    public boolean isBlocking() {
        throw new NotMockedException();
    }

    public int getExpToLevel() {
        throw new NotMockedException();
    }

    public double getEyeHeight() {
        throw new NotMockedException();
    }

    public double getEyeHeight(boolean ignoreSneaking) {
        throw new NotMockedException();
    }

    public Location getEyeLocation() {
        throw new NotMockedException();
    }

    public List<Block> getLineOfSight(HashSet<Byte> transparent, int maxDistance) {
        throw new NotMockedException();
    }

    public Block getTargetBlock(HashSet<Byte> transparent, int maxDistance) {
        throw new NotMockedException();
    }

    public List<Block> getLastTwoTargetBlocks(HashSet<Byte> transparent, int maxDistance) {
        throw new NotMockedException();
    }

    public Egg throwEgg() {
        throw new NotMockedException();
    }

    public Snowball throwSnowball() {
        throw new NotMockedException();
    }

    public Arrow shootArrow() {
        throw new NotMockedException();
    }

    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile) {
        throw new NotMockedException();
    }

    public int getRemainingAir() {
        throw new NotMockedException();
    }

    public void setRemainingAir(int ticks) {
        throw new NotMockedException();
    }

    public int getMaximumAir() {
        throw new NotMockedException();
    }

    public void setMaximumAir(int ticks) {
        throw new NotMockedException();
    }

    public int getMaximumNoDamageTicks() {
        throw new NotMockedException();
    }

    public void setMaximumNoDamageTicks(int ticks) {
        throw new NotMockedException();
    }

    public int getLastDamage() {
        throw new NotMockedException();
    }

    public void setLastDamage(int damage) {
        throw new NotMockedException();
    }

    public int getNoDamageTicks() {
        throw new NotMockedException();
    }

    public void setNoDamageTicks(int ticks) {
        throw new NotMockedException();
    }

    public Player getKiller() {
        throw new NotMockedException();
    }

    public boolean addPotionEffect(PotionEffect effect) {
        throw new NotMockedException();
    }

    public boolean addPotionEffect(PotionEffect effect, boolean force) {
        throw new NotMockedException();
    }

    public boolean addPotionEffects(Collection<PotionEffect> effects) {
        throw new NotMockedException();
    }

    public boolean hasPotionEffect(PotionEffectType type) {
        throw new NotMockedException();
    }

    public void removePotionEffect(PotionEffectType type) {
        throw new NotMockedException();
    }

    public Collection<PotionEffect> getActivePotionEffects() {
        throw new NotMockedException();
    }

    public boolean hasLineOfSight(Entity other) {
        throw new NotMockedException();
    }

    public boolean getRemoveWhenFarAway() {
        throw new NotMockedException();
    }

    public void setRemoveWhenFarAway(boolean remove) {
        throw new NotMockedException();
    }

    public EntityEquipment getEquipment() {
        throw new NotMockedException();
    }

    public void setCanPickupItems(boolean pickup) {
        throw new NotMockedException();
    }

    public boolean getCanPickupItems() {
        throw new NotMockedException();
    }

    public void damage(int amount) {
        throw new NotMockedException();
    }

    public void damage(int amount, Entity source) {
        throw new NotMockedException();
    }

    public int getHealth() {
        throw new NotMockedException();
    }

    public void setHealth(int health) {
        throw new NotMockedException();
    }

    public int getMaxHealth() {
        throw new NotMockedException();
    }

    public void setMaxHealth(int health) {
        throw new NotMockedException();
    }

    public void resetMaxHealth() {
        throw new NotMockedException();
    }
    public boolean isPermissionSet(String name) {
        throw new NotMockedException();
    }

    public boolean isPermissionSet(Permission perm) {
        throw new NotMockedException();
    }

    public boolean hasPermission(String name) {
        throw new NotMockedException();
    }

    public boolean hasPermission(Permission perm) {
        throw new NotMockedException();
    }

    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        throw new NotMockedException();
    }

    public PermissionAttachment addAttachment(Plugin plugin) {
        throw new NotMockedException();
    }

    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        throw new NotMockedException();
    }

    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        throw new NotMockedException();
    }

    public void removeAttachment(PermissionAttachment attachment) {
        throw new NotMockedException();
    }

    public void recalculatePermissions() {
        throw new NotMockedException();
    }

    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        throw new NotMockedException();
    }

    public void sendPluginMessage(Plugin source, String channel, byte[] message) {
        throw new NotMockedException();
    }

    public Set<String> getListeningPluginChannels() {
        throw new NotMockedException();
    }

    public boolean isOp() {
        throw new NotMockedException();
    }

    public void setOp(boolean value) {
        throw new NotMockedException();
    }
}
