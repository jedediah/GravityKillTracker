package ws.extension.gravitykilltracker;

import org.bukkit.entity.*;

public class MockProjectile extends MockEntity implements Projectile {
    public LivingEntity shooter;

    public MockProjectile(LivingEntity shooter) {
        this.shooter = shooter;
    }

    public LivingEntity getShooter() {
        return shooter;
    }

    public void setShooter(LivingEntity shooter) {
        this.shooter = shooter;
    }

    public boolean doesBounce() {
        throw new NotMockedException();
    }

    public void setBounce(boolean doesBounce) {
        throw new NotMockedException();
    }
}
