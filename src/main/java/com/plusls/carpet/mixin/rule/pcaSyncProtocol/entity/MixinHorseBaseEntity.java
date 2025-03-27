package com.plusls.carpet.mixin.rule.pcaSyncProtocol.entity;

import com.plusls.carpet.ModInfo;
import com.plusls.carpet.PcaSettings;
import com.plusls.carpet.network.PcaSyncProtocol;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.inventory.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * mc1.14 ~ mc1.21.4: subproject 1.20.2 (main project)
 * mc1.21.5+        : subproject 1.21.5        <--------
 */
@Mixin(AbstractHorseEntity.class)
public abstract class MixinHorseBaseEntity {
    @Inject(method = "onInventoryChanged", at = @At(value = "HEAD"))
    private void updateEntity(Inventory sender, CallbackInfo ci) {
        Entity self = (Entity)(Object)this;
        if (PcaSettings.pcaSyncProtocol && PcaSyncProtocol.syncEntityToClient(self)) {
            ModInfo.LOGGER.debug("update HorseBaseEntity inventory: onInventoryChanged.");
        }
    }
}
