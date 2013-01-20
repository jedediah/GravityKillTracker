package ws.extension.gravitykilltracker;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class GravityKillTrackerPlugin extends JavaPlugin {
    private GravityKillTracker gravityKillTracker;
    private TickTimer tickTimer;

    @Override
    public void onEnable() {
        tickTimer = new TickTimer(this);
        gravityKillTracker = new GravityKillTracker(this, tickTimer, new PlayerBlockChecker());

        getServer().getPluginManager().registerEvents(tickTimer, this);
        getServer().getPluginManager().registerEvents(gravityKillTracker, this);

        tickTimer.start();
    }

    @Override
    public void onDisable() {
        tickTimer.stop();

        HandlerList.unregisterAll((JavaPlugin) this);
    }

    public void debug(String msg) {
        getLogger().info(msg);
    }

    public void scheduleSyncDelayedTask(Runnable runme, long delay) {
        getServer().getScheduler().scheduleSyncDelayedTask(this, runme, delay);
    }
}
