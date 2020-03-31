package dev.arkav.ssgraves.mixins;

import dev.arkav.ssgraves.events.PlayerDropDeathCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {
    @Inject(method = "drop", at = @At("HEAD"), cancellable = true)
    public void drop(DamageSource source, CallbackInfo cb) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (self instanceof PlayerEntity) {
            ActionResult result = PlayerDropDeathCallback.EVENT.invoker().drop((PlayerEntity)self, self.world);
            if (result == ActionResult.FAIL) {
                cb.cancel();
            }
        }
    }
}
