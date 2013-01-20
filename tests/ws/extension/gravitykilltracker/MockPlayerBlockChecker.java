package ws.extension.gravitykilltracker;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class MockPlayerBlockChecker extends PlayerBlockChecker {

    private static class State {
        public boolean climbing;
        public boolean swimming;
        public boolean inLava;
    }

    private HashMap<Player, State> states = new HashMap<Player, State>();

    @Override
    public boolean isPlayerClimbing(Player player) {
        State state = states.get(player);
        return state != null && state.climbing;
    }

    @Override
    public boolean isPlayerSwimming(Player player, Material material) {
        State state = states.get(player);
        return state != null && (
            (material == Material.WATER && state.swimming) ||
            (material == Material.LAVA && state.inLava)
        );
    }

    private State getState(Player player) {
        State state = states.get(player);

        if (state == null) {
            state = new State();
            states.put(player, state);
        }

        return state;
    }

    public void setClimbing(Player player, boolean climbing) {
        getState(player).climbing = climbing;
    }

    public void setSwimming(Player player, boolean swimming) {
        getState(player).swimming = swimming;
    }

    public void setInLava(Player player, boolean inLava) {
        getState(player).inLava = inLava;
    }
}
