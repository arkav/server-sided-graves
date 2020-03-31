package dev.arkav.ssgraves.mixinapi;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.ListTag;

public interface IMixinSkullBlockEntity {
    void setGraveOwner(PlayerEntity entity);
    ListTag getInventory();
    GameProfile getGraveOwner();
}
