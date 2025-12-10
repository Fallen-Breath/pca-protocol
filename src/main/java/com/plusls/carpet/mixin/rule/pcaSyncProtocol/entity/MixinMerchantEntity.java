package com.plusls.carpet.mixin.rule.pcaSyncProtocol.entity;

import com.plusls.carpet.ModInfo;
import com.plusls.carpet.PcaSettings;
import com.plusls.carpet.network.PcaSyncProtocol;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Npc;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(AbstractVillager.class)
public abstract class MixinMerchantEntity extends AgeableMob implements Npc, Merchant, ContainerListener {
    @Final
    @Shadow
    private SimpleContainer inventory;

    protected MixinMerchantEntity(EntityType<? extends AgeableMob> entityType, Level world) {
        super(entityType, world);
    }


    @Inject(method = "<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;)V", at = @At(value = "RETURN"))
    private void addInventoryListener(EntityType<? extends AbstractVillager> entityType, Level world, CallbackInfo info) {
        if (world.isClientSide()) {
            return;
        }
        this.inventory.addListener(this);
    }

    @Override
    public void containerChanged(Container inventory) {
        if (PcaSettings.pcaSyncProtocol && PcaSyncProtocol.syncEntityToClient(this)) {
            ModInfo.LOGGER.debug("update villager inventory: onInventoryChanged.");
        }
    }
}