package com.plusls.carpet.mixin.rule.pcaSyncProtocol.entity;

import com.plusls.carpet.ModInfo;
import com.plusls.carpet.PcaSettings;
import com.plusls.carpet.network.PcaSyncProtocol;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.inventory.InventoryChangedListener;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * mc1.14 ~ mc1.21.4: subproject 1.20.2 (main project)
 * mc1.21.5+        : subproject 1.21.5        <--------
 */
@Mixin(AbstractHorseEntity.class)
public abstract class MixinHorseBaseEntity extends AnimalEntity implements InventoryChangedListener
{
    @Shadow protected SimpleInventory items;

    protected MixinHorseBaseEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "<init>(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/World;)V", at = @At(value = "RETURN"))
    private void addInventoryListener(EntityType<? extends MerchantEntity> entityType, World world, CallbackInfo info) {
        if (world.isClient()) {
            return;
        }
        this.items.addListener(inv -> {
            if (PcaSettings.pcaSyncProtocol && PcaSyncProtocol.syncEntityToClient(this)) {
                ModInfo.LOGGER.debug("update HorseBaseEntity inventory: onInventoryChanged.");
            }
        });
    }
}
