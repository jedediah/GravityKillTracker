package ws.extension.gravitykilltracker;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class TickTimer implements Listener {

    private long ticks = 0;

    public void setTicks(long ticks) {
        this.ticks = ticks;
    }

    public long getTicks() {
        return ticks;
    }

    private JavaPlugin plugin;
    private boolean running;
    private int taskId;

    public TickTimer(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean isRunning() {
        return running;
    }

    public void start() {
        if (running)
            throw new RuntimeException("Timer already running");

        this.taskId = this.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            public void run() {
                TickTimer.this.ticks++;
            }
        }, 1L, 1L);
        running = true;
    }

    public void stop() {
        if (!running)
            throw new RuntimeException("Timer not running");

        this.plugin.getServer().getScheduler().cancelTask(this.taskId);
    }
}
