package ws.extension.gravitykilltracker;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BlockVector;

import java.util.*;

public class GravityKillTracker extends JavaPlugin implements Listener {

    public static final long
        MAX_KNOCKBACK_TIME = 20, // Max ticks between getting attacked and leaving the ground
        MAX_SPLEEF_TIME = 20,    // Max ticks between block breaking and leaving the ground
        MAX_SWIMMING_TIME = 20,  // Max ticks to be in water without ending the fall
        MAX_CLIMBING_TIME = 10;  // Max ticks to be on a ladder/vine without ending the fall

    // Width of player collision box
    public static final float
        PLAYER_HEIGHT = 1.8f,
        PLAYER_WIDTH = 0.6f,
        PLAYER_RADIUS = PLAYER_WIDTH/2.0f;

    /**
     * Represents a player potentially falling due to an attack. Whenever an attack is
     * detected, one of these is created and indexed by victim. It is discarded when the
     * player lands somewhere safe or dies. A player can only have one fall record at any time.
     * While a fall is in progress, the attacker and cause don't change, and no new falls
     * can begin.
     */
    static class Fall {
        static enum Cause { HIT, SHOOT, SPLEEF }
        static enum From {FLOOR, LADDER, WATER }

        Fall(Entity attacker, Cause cause, Player victim, From from, long time) {
            this.attacker = attacker;
            this.cause = cause;
            this.victim = victim;
            this.from = from;
            this.time = this.swimmingTime = this.climbingTime = time;
        }

        final public Player victim;         // Who is falling
        final public Entity attacker;       // Who is responsible
        final public Cause cause;           // What caused the fall
        final public From from;             // Where they are falling from
        final public long time;             // Time of the attack
        public boolean isFalling;           // If the victim has initially left the ground
        public boolean isSwimming;          // If the victim is currently in water
        public long swimmingTime;           // Time since the victim entered water
        public boolean isClimbing;          // If the victim is currently on a ladder/vine
        public long climbingTime;           // Time since the victim grabbed a ladder/vine
        public boolean isInLava;            // If the victim is currently in lava
    }

    private HashMap<Player, Fall> falls = new HashMap<Player, Fall>();

    /**
     * Represents a block that was recently broken by a player. Created for all blocks
     * broken, indexed by the block's location, and discarded after MAX_SPLEEF_TIME.
     */
    static class BrokenBlock {

        BrokenBlock(Block block, Player breaker, long time) {
            this.block = block;
            this.breaker = breaker;
            this.time = time;
        }

        final public Block block;
        final public Player breaker;
        final public long time;
    }

    private HashMap<BlockVector, BrokenBlock> brokenBlocks = new HashMap<BlockVector, BrokenBlock>();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll((JavaPlugin) this);
        falls.clear();
        brokenBlocks.clear();
    }

    private void info(String text) {
        getLogger().info(text);
    }

    private void info(Player player, String text) {
        info(player.getWorld().getFullTime() +
             " " + player.getDisplayName() +
             " " + text);
    }

    /**
     * Test if a player is climbing anything
     * @param player A player
     * @return true if they are hanging on a ladder or vine
     */
    private boolean isPlayerClimbing(Player player) {
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
    private boolean isPlayerInLiquid(Player player, Material material) {
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

    /**
     * Find the block most recently broken under a player's feet within the last MAX_SPLEEF_TIME,
     * or null if there is no such block.
     * @param player A player
     * @param time Time the player started to fall
     * @return Last block that was recently broken under the player, or null if no such blocks were found
     */
    private BrokenBlock findBlockBrokenUnderPlayer(Player player, long time) {
        Location location = player.getLocation();

        int y = (int) Math.floor(location.getY() - 0.1);

        int x1 = (int) Math.floor(location.getX() - PLAYER_RADIUS);
        int z1 = (int) Math.floor(location.getZ() - PLAYER_RADIUS);

        int x2 = (int) Math.floor(location.getX() + PLAYER_RADIUS);
        int z2 = (int) Math.floor(location.getZ() + PLAYER_RADIUS);

        BrokenBlock lastBrokenBlock = null;

        for (int x = x1; x <= x2; ++x) {
            for (int z = z1; z <= z2; ++z) {
                BlockVector bv = new BlockVector(x,y,z);

                if (brokenBlocks.containsKey(bv)) {
                    BrokenBlock brokenBlock = brokenBlocks.get(bv);
                    if (lastBrokenBlock == null || brokenBlock.time > lastBrokenBlock.time) {
                        lastBrokenBlock = brokenBlock;
                    }
                }
            }
        }

        if (lastBrokenBlock != null && time - lastBrokenBlock.time <= MAX_SPLEEF_TIME) {
            return lastBrokenBlock;
        } else {
            return null;
        }
    }

    /**
     * Called when a falling player lands on the ground. Waits one tick and then discards
     * the fall record. If the player died from the fall, the record will already be gone.
     * If the player is in lava, don't discard the fall, because we want to wait and see if
     * they die from the lava.
     * @param fall Fall being tracked
     * @param time Time they hit the ground
     */
    private void victimOnGround(final Fall fall, long time) {
        getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            public void run() {
                if (falls.containsKey(fall.victim) && !fall.isInLava) {
                    falls.remove(fall.victim);
                    info(fall.victim, "attack survived (on ground)");
                }
            }
        }, 1L);
    }

    /**
     * Called when a falling player leaves the ground. This is expected to happen within
     * MAX_KNOCKBACK_TIME of being attacked, otherwise the fall is discarded.
     * @param fall Fall being tracked
     * @param time Time they left the ground
     */
    private void victimOffGround(Fall fall, long time) {
        if (!fall.isFalling) {
            if (time - fall.time > MAX_KNOCKBACK_TIME) {
                falls.remove(fall.victim);
                info(fall.victim, "attack expired (off ground too late)");
            } else {
                fall.isFalling = true;
                info(fall.victim, "knockback confirmed (off ground)");
            }
        }
    }

    /**
     * Called when a falling player grabs a ladder or vine. Resets the climbing timer.
     * @param fall Fall being tracked
     * @param time Time they first touched the ladder/vine
     */
    private void victimOnLadder(Fall fall, long time) {
        fall.climbingTime = time;
    }

    /**
     * Called when a falling player lets go of a ladder or vine. If they hold a ladder
     * for longer than MAX_CLIMBING_TIME while falling, the fall is discarded.
     * @param fall Fall being tracked
     * @param time Time they let go of the ladder/vine
     */
    private void victimOffLadder(Fall fall, long time) {
        if (time - fall.climbingTime > MAX_CLIMBING_TIME) {
            falls.remove(fall.victim);
            info(fall.victim, "survived (climbing too long)");
        }
    }

    /**
     * Called when a falling player enters water. Resets the swimming timer.
     * @param fall Fall being tracked
     * @param time Time they first touched the water
     */
    private void victimInWater(Fall fall, long time) {
        fall.swimmingTime = time;
    }

    /**
     * Called when a falling player leaves water. If they were in the water for more than
     * MAX_SWIMMING_TIME, the fall is discarded.
     * @param fall Fall being tracked
     * @param time Time they stopped touching the water
     */
    private void victimOutOfWater(Fall fall, long time) {
        if (time - fall.swimmingTime > MAX_SWIMMING_TIME) {
            falls.remove(fall.victim);
            info(fall.victim, "survived (swimming too long)");
        }
    }

    /**
     * Called when a falling player enters lava. Currently not used.
     * @param fall Fall being tracked
     * @param time Time they first touched the lava
     */
    private void victimInLava(Fall fall, long time) {

    }

    /**
     * Called when a falling player leaves lava. If they are also on the ground,
     * discard the fall. victimOnGround didn't do that because the victim was
     * still in lava.
     * @param fall Fall being tracked
     * @param time Time they stopped touching the lava
     */
    private void victimOutOfLava(Fall fall, long time) {
        if (fall.victim.isOnGround()) {
            falls.remove(fall.victim);
            info(fall.victim, "is out of the lava and safe on the ground");
        }
    }

    /**
     * Called when a player who is NOT being tracked leaves the ground. If we can find
     * a block that was broken under them recently, we start tracking the fall as a spleefing.
     * @param player Player who left the ground
     * @param time Time they left the ground
     */
    private void nonVictimOffGround(Player player, long time) {
        info(player, "off ground");
        
        BrokenBlock brokenBlock = findBlockBrokenUnderPlayer(player, time);
        if (brokenBlock != null) {
            Fall fall = new Fall(
                brokenBlock.breaker,
                Fall.Cause.SPLEEF,
                player,
                Fall.From.FLOOR,
                brokenBlock.time
            );

            fall.isFalling = true; // Already off the ground
            fall.isClimbing = isPlayerClimbing(player);
            fall.isSwimming = isPlayerInLiquid(player, Material.WATER);
            fall.isInLava = isPlayerInLiquid(player, Material.LAVA);
            falls.put(player, fall);

            info(player, "spleef confirmed (off ground)");
        }
    }

    /**
     * Called when a player who is NOT being tracked is damaged by another entity (which
     * could be anything, not necessarily living). Ignore if the player is in lava, because
     * there is a good chance they are already doomed.
     * @param player Player who was attacked
     * @param damager Entity that attacked/damaged them
     * @param time Time of the attack
     */
    private void nonVictimAttacked(Player player, Entity damager, long time) {
        boolean isClimbing = isPlayerClimbing(player);
        boolean isSwimming = isPlayerInLiquid(player, Material.WATER);
        boolean isInLava = isPlayerInLiquid(player, Material.LAVA);

        if (!isInLava) {
            Fall.Cause cause;
            if (damager instanceof Projectile) {
                damager = ((Projectile) damager).getShooter();
                cause = Fall.Cause.SHOOT;
            } else {
                cause = Fall.Cause.HIT;
            }

            Fall.From from = null;
            if (player.isOnGround()) {
                from = Fall.From.FLOOR;
            } else if (isClimbing) {
                from = Fall.From.LADDER;
            } else if (isSwimming) {
                from = Fall.From.WATER;
            }

            Fall fall = new Fall(
                damager,
                cause,
                player,
                from,
                time
            );

            fall.isFalling = !player.isOnGround();
            fall.isClimbing = isClimbing;
            fall.isSwimming = isSwimming;
            fall.isInLava = isInLava;
            falls.put(player, fall);

            info(player, "attacked by " + fall.attacker +
                         " cause=" + cause +
                         " from=" + from);
        }
    }

    /**
     * Called when a falling player dies to decide if the death was caused by the fall.
     * @param fall The fall being tracked
     * @param damageCause Cause of death
     * @param time Time of death
     * @return true if death was caused by the fall
     */
    private boolean wasAttackFatal(Fall fall, EntityDamageEvent.DamageCause damageCause, long time) {
        switch (damageCause) {
            case VOID:
            case FALL:
            case LAVA:
            case SUICIDE:
                break;

            case FIRE_TICK:
                if (!fall.isInLava)
                    return false;
                break;

            default:
                return false;
        }

        if (fall.isSwimming && time - fall.swimmingTime > MAX_SWIMMING_TIME)
            return false;

        if (fall.isClimbing && time - fall.climbingTime > MAX_CLIMBING_TIME)
            return false;

        return true;
    }

    /**
     * Generates a death message based on the various circumstances of a falling kill.
     * @param fall The fall that resulted in a kill
     * @param damageCause The final cause of death
     * @return A text message describing the kill
     */
    private String makeDeathMessage(Fall fall, EntityDamageEvent.DamageCause damageCause) {
        String attackerText;
        if (fall.attacker instanceof Player) {
            attackerText = ((Player) fall.attacker).getDisplayName();
        } else {
            attackerText = fall.attacker.getType().getName();
        }

        String attackText;
        switch (fall.cause) {
            case HIT:
                attackText = " knocked ";
                break;

            case SHOOT:
                attackText = " shot ";
                break;

            case SPLEEF:
                attackText = " spleefed ";
                break;

            default:
                attackText = "";
                break;
        }

        String siteText;
        switch (fall.from) {
            case LADDER:
                siteText = " off a ladder";
                break;

            case WATER:
                siteText = " out of the water";
                break;

            default:
                siteText = "";
                break;
        }

        String damageText;
        if (fall.from == Fall.From.FLOOR) {
            switch (damageCause) {
                case VOID:
                    damageText = " out of the world";
                    break;

                case FALL:
                    damageText = " off a high place";
                    break;

                case LAVA:
                case FIRE_TICK:
                    damageText = " into lava";
                    break;

                case SUICIDE:
                    damageText = " to their death (suicide/battle log)";
                    break;

                default:
                    damageText = " to their death";
            }
        } else {
            switch (damageCause) {
                case VOID:
                    damageText = " and into the void";
                    break;

                case FALL:
                    damageText = "";
                    break;

                case LAVA:
                case FIRE_TICK:
                    damageText = " and into lava";
                    break;

                case SUICIDE:
                    damageText = " to their death (suicide/battle log)";
                    break;

                default:
                    damageText = " to their death";
            }
        }

        return attackerText +
               attackText +
               fall.victim.getDisplayName() +
               siteText +
               damageText;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerOnGroundChanged(PlayerOnGroundEvent event) {
        final Player player = event.getPlayer();
        long time = player.getWorld().getFullTime();

        if (falls.containsKey(player)) {
            Fall fall = falls.get(player);
            if (event.getOnGround()) {
                victimOnGround(fall, time);
            } else {
                victimOffGround(fall, time);
            }

        } else if (!event.getOnGround()) {
            nonVictimOffGround(player, time);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (falls.containsKey(player)) {
            Fall fall = falls.get(player);
            long time = player.getWorld().getFullTime();
            boolean isClimbing = isPlayerClimbing(player);
            boolean isSwimming = isPlayerInLiquid(player, Material.WATER);
            boolean isInLava = isPlayerInLiquid(player, Material.LAVA);

            if (isClimbing != fall.isClimbing) {
                if ((fall.isClimbing = isClimbing)) {
                    victimOnLadder(fall, time);
                } else {
                    victimOffLadder(fall, time);
                }
            }

            if (isSwimming != fall.isSwimming) {
                if ((fall.isSwimming = isSwimming)) {
                    victimInWater(fall, time);
                } else {
                    victimOutOfWater(fall, time);
                }
            }

            if (isInLava != fall.isInLava) {
                if ((fall.isInLava = isInLava)) {
                    victimInLava(fall, time);
                } else {
                    victimOutOfLava(fall, time);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            long time = player.getWorld().getFullTime();
            nonVictimAttacked(player, event.getDamager(), time);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        if (falls.containsKey(player)) {
            long time = player.getWorld().getFullTime();
            Fall fall = falls.remove(player);
            EntityDamageEvent.DamageCause damageCause = player.getLastDamageCause().getCause();

            if (wasAttackFatal(fall, damageCause, time)) {
                String deathMessage = makeDeathMessage(fall, damageCause);
                event.setDeathMessage(deathMessage);
                info(deathMessage);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Detect battle logging and credit the kill

        Player player = event.getPlayer();

        if (falls.containsKey(player)) {
            long time = player.getWorld().getFullTime();
            Fall fall = falls.remove(player);
            EntityDamageEvent.DamageCause damageCause = EntityDamageEvent.DamageCause.SUICIDE;

            if (wasAttackFatal(fall, damageCause, time)) {
                info(player, "battle log detected");
                
                Vector<ItemStack> inventory = new Vector<ItemStack>();
                Collections.addAll(inventory, player.getInventory().getContents());

                PlayerDeathEvent deathEvent = new PlayerDeathEvent(
                    player,
                    inventory,
                    0,
                    makeDeathMessage(fall, damageCause));

                // TODO: This event doesn't make it to onPlayerDeath.. why?
                getServer().getPluginManager().callEvent(deathEvent);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerGameModeChange(PlayerGameModeChangeEvent event) {
        if (event.getNewGameMode() == GameMode.CREATIVE &&
            falls.remove(event.getPlayer()) != null) {
            info(event.getPlayer(), "survived due to gamemode change");
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        final Player player = event.getPlayer();
        long time = player.getWorld().getFullTime();

        // Start tracking this broken block
        final BrokenBlock brokenBlock = new BrokenBlock(
            event.getBlock(),
            player,
            time
        );
        final BlockVector bv = new BlockVector(brokenBlock.block.getLocation().toVector());
        brokenBlocks.put(bv, brokenBlock);

        // Schedule the tracking to end after MAX_SPLEEF_TIME
        getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            public void run() {
                if (brokenBlocks.containsKey(bv) && brokenBlocks.get(bv) == brokenBlock) {
                    brokenBlocks.remove(bv);
                    info(player, "block break expired "+brokenBlock.block);
                }
            }
        }, MAX_SPLEEF_TIME);

        info(player, "broke block " + brokenBlock.block);
    }
}
