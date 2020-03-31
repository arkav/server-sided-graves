package dev.arkav.ssgraves.mixins;

import com.mojang.authlib.GameProfile;
import dev.arkav.ssgraves.mixinapi.IMixinSkullBlockEntity;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SkullBlockEntity.class)
public class MixinSkullBlockEntity implements IMixinSkullBlockEntity {
    @Shadow
    private GameProfile owner;

    private ListTag inventory;

    @Inject(method = "toTag", at = @At("RETURN"), cancellable = true)
    public CompoundTag toTag(CompoundTag tag, CallbackInfoReturnable<CompoundTag> cbr) {
        if (this.inventory != null && !this.inventory.isEmpty()) {
            tag.put("gravedata", this.inventory);
        }
        cbr.setReturnValue(tag);
        return tag;
    }

    @Inject(method = "fromTag", at = @At("TAIL"))
    public void fromTag(CompoundTag tag, CallbackInfo cb) {
        this.inventory = (ListTag) tag.get("gravedata");
    }

    @Override
    public void setGraveOwner(PlayerEntity entity) {
        SkullBlockEntity self = (SkullBlockEntity) (Object) this;
        self.setOwnerAndType(entity.getGameProfile());
        this.inventory = new ListTag();
        entity.inventory.serialize(this.inventory);
        self.markDirty();
    }

    @Override
    public ListTag getInventory() {
        return this.inventory;
    }

    @Override
    public GameProfile getGraveOwner() {
        return this.owner;
    }
}
