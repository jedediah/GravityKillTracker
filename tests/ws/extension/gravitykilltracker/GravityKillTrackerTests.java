package ws.extension.gravitykilltracker;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerOnGroundEvent;
import org.bukkit.inventory.ItemStack;

import org.junit.Before;
import org.junit.Test;
import sun.awt.geom.AreaOp;

import static org.junit.Assert.*;

import java.util.ArrayList;

public class GravityKillTrackerTests {

    private MockGravityKillTrackerPlugin plugin;
    private MockPlayerBlockChecker blockChecker;
    private MockTickTimer timer;
    private MockPlayer attacker, victim;

    private GravityKillTracker tracker;
    private PlayerDeathEvent lastDeathEvent;

    @Before
    public void before() {
        plugin = new MockGravityKillTrackerPlugin();
        plugin.timer = timer = new MockTickTimer(plugin);

        blockChecker = new MockPlayerBlockChecker();
        tracker = new GravityKillTracker(plugin, timer, blockChecker);

        attacker = new MockPlayer("Attacker");
        victim = new MockPlayer("Victim");

        System.out.println("--------------------------------------");
    }

    private void onGround(MockPlayer player, boolean onGround) {
        if (player.onGround != onGround) {
            player.onGround = onGround;
            tracker.onPlayerOnGroundChanged(new PlayerOnGroundEvent(player, onGround));
        }
    }

    private void climbing(MockPlayer player, boolean climbing) {
        if (blockChecker.isPlayerClimbing(player) != climbing) {
            blockChecker.setClimbing(player, climbing);
            PlayerMoveEvent event = new PlayerMoveEvent(
                player,
                player.getLocation(),
                new Location(null, 0, 0, 0)
            );
            tracker.onPlayerMove(event);
        }
    }

    private void swimming(MockPlayer player, boolean swimming) {
        if (blockChecker.isPlayerSwimming(player, Material.WATER) != swimming) {
            blockChecker.setSwimming(player, swimming);
            PlayerMoveEvent event = new PlayerMoveEvent(
                player,
                player.getLocation(),
                new Location(null, 0, 0, 0)
            );
            tracker.onPlayerMove(event);
        }
    }

    private void inLava(MockPlayer player, boolean inLava) {
        if (blockChecker.isPlayerSwimming(player, Material.LAVA) != inLava) {
            blockChecker.setInLava(player, inLava);
            PlayerMoveEvent event = new PlayerMoveEvent(
                player,
                player.getLocation(),
                new Location(null, 0, 0, 0)
            );
            tracker.onPlayerMove(event);
        }
    }

    private void meleeAttack(MockPlayer attacker, MockPlayer victim) {
        EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(
            attacker,
            victim,
            EntityDamageEvent.DamageCause.ENTITY_ATTACK,
            1
        );

        victim.setLastDamageCause(event);
        tracker.onEntityDamageByEntity(event);
    }

    private void meleeAttack() {
        meleeAttack(attacker, victim);
    }

    private void projectileAttack() {
        EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(
            new MockProjectile(attacker),
            victim,
            EntityDamageEvent.DamageCause.PROJECTILE,
            1
        );

        victim.setLastDamageCause(event);
        tracker.onEntityDamageByEntity(event);
    }

    private String defaultDeathMessage(EntityDamageEvent.DamageCause cause) {
        switch (cause) {
            case VOID:   return " fell out of the world";
            case FALL:   return " hit the ground too hard";
            case LAVA:   return " tried to swim in lava";
            default:     return " died from " + cause;
        }
    }

    private void die(MockPlayer player, EntityDamageEvent.DamageCause cause) {
        EntityDamageEvent damageEvent = new EntityDamageEvent(player, cause, 1);
        victim.setLastDamageCause(damageEvent);

        lastDeathEvent = new PlayerDeathEvent(
            player,
            new ArrayList<ItemStack>(),
            0,
            player.getName() + defaultDeathMessage(cause)
        );
        tracker.onPlayerDeath(lastDeathEvent);
    }

    private void breakBlock(MockPlayer player, int x, int y, int z) {
        BlockBreakEvent event = new BlockBreakEvent(new MockBlock(x, y, z), player);
        tracker.onBlockBreak(event);
    }

    private void advance(long ticks) {
        plugin.advance(ticks);
    }

    private void assertDeathMessage(String expected) {
        assertEquals(expected, lastDeathEvent.getDeathMessage());
    }

    /**
     * Falling kills
     */

    @Test public void testKnockOffHighPlace() {
        meleeAttack();

        advance(GravityKillTracker.MAX_KNOCKBACK_TIME);
        onGround(victim, false);

        advance(99);
        onGround(victim, true);
        die(victim, EntityDamageEvent.DamageCause.FALL);

        assertDeathMessage("Victim was knocked off a high place by Attacker");
    }

    /**
     * Void kills
     */

    @Test public void testKnockOutOfWorld() {
        meleeAttack();

        advance(GravityKillTracker.MAX_KNOCKBACK_TIME);
        onGround(victim, false);

        advance(99);
        die(victim, EntityDamageEvent.DamageCause.VOID);

        assertDeathMessage("Victim was knocked out of the world by Attacker");
    }

    /**
     * Lava kills
     */

    @Test public void testKnockIntoLava() {
        meleeAttack();

        advance(GravityKillTracker.MAX_KNOCKBACK_TIME);
        onGround(victim, false);

        advance(99);
        inLava(victim, true);
        die(victim, EntityDamageEvent.DamageCause.LAVA);

        assertDeathMessage("Victim was knocked into lava by Attacker");
    }

    @Test public void testKnockIntoLavaFireTick() {
        meleeAttack();

        advance(GravityKillTracker.MAX_KNOCKBACK_TIME);
        onGround(victim, false);

        advance(99);
        inLava(victim, true);
        die(victim, EntityDamageEvent.DamageCause.FIRE_TICK);

        assertDeathMessage("Victim was knocked into lava by Attacker");
    }

    @Test public void testKnockIntoLavaOnGround() {
        meleeAttack();

        advance(GravityKillTracker.MAX_KNOCKBACK_TIME);
        onGround(victim, false);

        advance(99);
        inLava(victim, true);

        advance(1);
        onGround(victim, true);

        advance(99);
        die(victim, EntityDamageEvent.DamageCause.LAVA);

        assertDeathMessage("Victim was knocked into lava by Attacker");
    }

    /**
     * Attack sites
     */

    @Test public void testKnockOffLadder() {
        onGround(victim, false);
        climbing(victim, true);
        meleeAttack();

        advance(GravityKillTracker.MAX_KNOCKBACK_TIME);
        climbing(victim, false);

        advance(99);
        onGround(victim, true);
        die(victim, EntityDamageEvent.DamageCause.FALL);

        assertDeathMessage("Victim was knocked off a ladder by Attacker");
    }

    @Test public void testKnockOutOfWater() {
        onGround(victim, false);
        swimming(victim, true);
        meleeAttack();

        advance(GravityKillTracker.MAX_KNOCKBACK_TIME);
        swimming(victim, false);

        advance(99);
        onGround(victim, true);
        die(victim, EntityDamageEvent.DamageCause.FALL);

        assertDeathMessage("Victim was knocked out of the water by Attacker");
    }

    /**
     * Projectiles
     */

    @Test public void testShootOffHighPlace() {
        projectileAttack();

        advance(GravityKillTracker.MAX_KNOCKBACK_TIME);
        onGround(victim, false);

        advance(99);
        die(victim, EntityDamageEvent.DamageCause.FALL);

        assertDeathMessage("Victim was shot off a high place by Attacker");
    }

    /**
     * Spleefing
     */

    @Test public void testSpleef() {
        victim.location = new Location(null, 0.5, 64.0, 0.5);
        breakBlock(attacker, 0, 63, 0);

        advance(GravityKillTracker.MAX_KNOCKBACK_TIME);
        onGround(victim, false);

        advance(99);
        die(victim, EntityDamageEvent.DamageCause.VOID);

        assertDeathMessage("Victim was spleefed out of the world by Attacker");
    }

    @Test public void testSpleefOffsetBlock() {
        for (int z = 0; z < 2; z++) {
            for (int x = 0; x < 2; x++) {
                victim.location = new Location(null, -0.29 + 1.58*x, 64.0, -0.29 + 1.58*z);
                onGround(victim, true);
                breakBlock(attacker, 0, 63, 0);

                advance(GravityKillTracker.MAX_KNOCKBACK_TIME);
                onGround(victim, false);

                advance(99);
                die(victim, EntityDamageEvent.DamageCause.VOID);

                assertDeathMessage("Victim was spleefed out of the world by Attacker");

                advance(99);
            }
        }
    }

    @Test public void testSpleefSlab() {
        victim.location = new Location(null, 0.5, 63.5, 0.5);
        breakBlock(attacker, 0, 63, 0);

        advance(GravityKillTracker.MAX_KNOCKBACK_TIME);
        onGround(victim, false);

        advance(99);
        die(victim, EntityDamageEvent.DamageCause.VOID);

        assertDeathMessage("Victim was spleefed out of the world by Attacker");
    }

    /**
     * Compound cases
     */

    @Test public void testKnockOffLadderAndIntoVoid() {
        onGround(victim, false);
        climbing(victim, true);
        meleeAttack();

        advance(GravityKillTracker.MAX_KNOCKBACK_TIME);
        climbing(victim, false);

        advance(99);
        die(victim, EntityDamageEvent.DamageCause.VOID);

        assertDeathMessage("Victim was knocked off a ladder and into the void by Attacker");
    }

    @Test public void tesShootOffLadderAndIntoLava() {
        onGround(victim, false);
        climbing(victim, true);
        projectileAttack();

        advance(GravityKillTracker.MAX_KNOCKBACK_TIME);
        climbing(victim, false);

        advance(99);
        inLava(victim, true);
        die(victim, EntityDamageEvent.DamageCause.LAVA);

        assertDeathMessage("Victim was shot off a ladder and into lava by Attacker");
    }

    @Test public void testShootOutOfWaterAndIntoVoid() {
        onGround(victim, false);
        swimming(victim, true);
        projectileAttack();

        advance(GravityKillTracker.MAX_KNOCKBACK_TIME);
        swimming(victim, false);

        advance(99);
        inLava(victim, true);
        die(victim, EntityDamageEvent.DamageCause.VOID);

        assertDeathMessage("Victim was shot out of the water and into the void by Attacker");
    }

    @Test public void testKnockOutOfWaterAndIntoLava() {
        onGround(victim, false);
        swimming(victim, true);
        meleeAttack();

        advance(GravityKillTracker.MAX_KNOCKBACK_TIME);
        swimming(victim, false);

        advance(99);
        inLava(victim, true);
        die(victim, EntityDamageEvent.DamageCause.LAVA);

        assertDeathMessage("Victim was knocked out of the water and into lava by Attacker");
    }

    /**
     * Attack cancel timeouts
     */

    @Test public void testKnockbackExpired() {
        meleeAttack();

        advance(GravityKillTracker.MAX_KNOCKBACK_TIME + 1);
        onGround(victim, false);

        advance(99);
        die(victim, EntityDamageEvent.DamageCause.VOID);

        assertDeathMessage("Victim fell out of the world");
    }

    @Test public void testOnGroundTooLong() {
        meleeAttack();

        advance(1);
        onGround(victim, false);

        advance(99);
        onGround(victim, true);

        advance(GravityKillTracker.MAX_ON_GROUND_TIME + 1);
        onGround(victim, false);

        advance(99);
        die(victim, EntityDamageEvent.DamageCause.VOID);

        assertDeathMessage("Victim fell out of the world");
    }

    @Test public void testClimbingTooLong() {
        meleeAttack();

        advance(1);
        onGround(victim, false);

        advance(99);
        climbing(victim, true);

        advance(GravityKillTracker.MAX_CLIMBING_TIME+1);
        climbing(victim, false);

        advance(99);
        die(victim, EntityDamageEvent.DamageCause.VOID);

        assertDeathMessage("Victim fell out of the world");
    }

    @Test public void testSwimmingTooLong() {
        meleeAttack();

        advance(1);
        onGround(victim, false);

        advance(99);
        swimming(victim, true);

        advance(GravityKillTracker.MAX_SWIMMING_TIME+1);
        swimming(victim, false);

        advance(99);
        die(victim, EntityDamageEvent.DamageCause.VOID);

        assertDeathMessage("Victim fell out of the world");
    }

    @Test public void testSupportedBriefly() {
        meleeAttack();

        advance(1);
        onGround(victim, false);

        advance(99);
        onGround(victim, true);

        advance(GravityKillTracker.MAX_ON_GROUND_TIME);
        onGround(victim, false);

        advance(99);
        climbing(victim, true);

        advance(GravityKillTracker.MAX_CLIMBING_TIME);
        climbing(victim, false);

        advance(99);
        swimming(victim, true);

        advance(GravityKillTracker.MAX_SWIMMING_TIME);
        swimming(victim, false);

        advance(99);
        die(victim, EntityDamageEvent.DamageCause.VOID);

        assertDeathMessage("Victim was knocked out of the world by Attacker");
    }

    /**
     * Misc. cases
     */

    @Test public void testHitWhileFallingCredited() {
        onGround(victim, false);

        advance(99);
        meleeAttack();

        advance(99);
        die(victim, EntityDamageEvent.DamageCause.VOID);

        assertDeathMessage("Victim was knocked out of the world by Attacker");
    }

    @Test public void testNoKillStealing() {
        MockPlayer thief = new MockPlayer("thief");

        meleeAttack(attacker, victim);

        advance(1);

        meleeAttack(thief, victim);

        advance(1);
        onGround(victim, false);

        meleeAttack(thief, victim);

        advance(99);
        die(victim, EntityDamageEvent.DamageCause.VOID);

        assertDeathMessage("Victim was knocked out of the world by Attacker");
    }
}
