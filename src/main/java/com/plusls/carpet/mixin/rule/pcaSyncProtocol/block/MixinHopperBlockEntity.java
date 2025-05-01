package com.plusls.carpet.mixin.rule.pcaSyncProtocol.block;

import com.plusls.carpet.ModInfo;
import com.plusls.carpet.PcaSettings;
import com.plusls.carpet.network.PcaSyncProtocol;
import com.plusls.carpet.util.PcaBlockEntityDirtyHook;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.Hopper;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//#if MC >= 11700
import org.spongepowered.asm.mixin.injection.ModifyVariable;
//#else
//$$ import org.spongepowered.asm.mixin.injection.Inject;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//#endif

@Mixin(HopperBlockEntity.class)
public abstract class MixinHopperBlockEntity extends LootableContainerBlockEntity implements Hopper, PcaBlockEntityDirtyHook {

    protected MixinHopperBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(
                blockEntityType
                //#if MC >= 11700
                , blockPos, blockState
                //#endif
        );
    }

    //#if MC >= 11700
    @ModifyVariable(
            method = "insertAndExtract",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/entity/HopperBlockEntity;markDirty(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V"
            ),
            argsOnly = true
    )
    private static HopperBlockEntity onInsertAndExtract(HopperBlockEntity blockEntity) {
    //#else
    //$$ @Inject(
    //$$         method = "insertAndExtract",
    //$$         at = @At(
    //$$                 value = "INVOKE",
    //$$                 target = "Lnet/minecraft/block/entity/HopperBlockEntity;markDirty()V"
    //$$         )
    //$$ )
    //$$ private void onInsertAndExtract(CallbackInfoReturnable<Boolean> cir) {
    //$$     HopperBlockEntity blockEntity = (HopperBlockEntity)(Object)this;
    //#endif
        if (PcaSettings.pcaSyncProtocol && PcaSyncProtocol.syncBlockEntityToClient(blockEntity)) {
            ModInfo.LOGGER.debug("update HopperBlockEntity: {}", blockEntity.getPos());
        }
        //#if MC >= 11700
        return blockEntity;
        //#endif
    }

    @Override
    public void pca$onMarkDirty() {
        if (PcaSettings.pcaSyncProtocol && PcaSyncProtocol.syncBlockEntityToClient(this)) {
            ModInfo.LOGGER.debug("update HopperBlockEntity: {}", this.pos);
        }
    }
}