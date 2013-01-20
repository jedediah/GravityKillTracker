package ws.extension.gravitykilltracker;

import org.bukkit.plugin.java.JavaPlugin;

public class MockTickTimer extends TickTimer {
    public MockTickTimer(JavaPlugin plugin) {
        super(plugin);
    }

    public void advance(long ticks) {
        setTicks(getTicks() + ticks);
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }
}
