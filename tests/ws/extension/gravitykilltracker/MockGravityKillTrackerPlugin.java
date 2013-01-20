package ws.extension.gravitykilltracker;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class MockGravityKillTrackerPlugin extends GravityKillTrackerPlugin {
    private static class ScheduledTask implements Comparable<ScheduledTask> {
        public final long time;
        public final Runnable task;

        public ScheduledTask(long time, Runnable task) {
            this.time = time;
            this.task = task;
        }

        public int compareTo(ScheduledTask task) {
            return time < task.time ? -1 : time > task.time ? 1 : 0;
        }
    }

    public TickTimer timer;
    public final PriorityQueue<ScheduledTask> scheduledTasks = new PriorityQueue<ScheduledTask>();

    @Override
    public void scheduleSyncDelayedTask(Runnable task, long delay) {
        scheduledTasks.add(new ScheduledTask(timer.getTicks() + delay, task));
    }

    @Override
    public void debug(String msg) {
        System.out.println(msg);
    }

    public void advance(long ticks) {
        long endTime = timer.getTicks() + ticks;

        while (!scheduledTasks.isEmpty() && scheduledTasks.peek().time <= endTime) {
            ScheduledTask next = scheduledTasks.poll();
            timer.setTicks(next.time);
            next.task.run();
        }

        timer.setTicks(endTime);
    }
}
