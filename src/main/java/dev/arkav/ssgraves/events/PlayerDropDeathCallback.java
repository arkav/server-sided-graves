package dev.arkav.ssgraves.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;

public interface PlayerDropDeathCallback {
    Event<PlayerDropDeathCallback> EVENT = EventFactory.createArrayBacked(PlayerDropDeathCallback.class,
            (listeners) -> (player, world) -> {
                for (PlayerDropDeathCallback listener : listeners) {
                    ActionResult result = listener.drop(player, world);
                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            });
    ActionResult drop(PlayerEntity entity, World world);
}
