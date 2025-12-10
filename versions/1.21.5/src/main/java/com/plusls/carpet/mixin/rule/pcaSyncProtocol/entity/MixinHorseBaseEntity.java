package com.plusls.carpet.mixin.rule.pcaSyncProtocol.entity;

import com.plusls.carpet.ModInfo;
import com.plusls.carpet.PcaSettings;
import com.plusls.carpet.network.PcaSyncProtocol;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * mc1.14 ~ mc1.21.4: subproject 1.20.2 (main project)
 * mc1.21.5+        : subproject 1.21.5        <--------
 */
@Mixin(AbstractHorse.class)
public abstract class MixinHorseBaseEntity extends Animal
{
    @Shadow protected SimpleContainer inventory;

    protected MixinHorseBaseEntity(EntityType<? extends Animal> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;)V", at = @At(value = "RETURN"))
    private void addInventoryListener(EntityType<? extends AbstractHorse> entityType, Level world, CallbackInfo info) {
        if (world.isClientSide()) {
            return;
        }
        this.inventory.addListener(inv -> {
            if (PcaSettings.pcaSyncProtocol && PcaSyncProtocol.syncEntityToClient(this)) {
                ModInfo.LOGGER.debug("update HorseBaseEntity inventory: onInventoryChanged.");
            }
        });
    }
}
