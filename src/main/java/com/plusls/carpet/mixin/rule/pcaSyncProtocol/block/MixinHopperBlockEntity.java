package com.plusls.carpet.mixin.rule.pcaSyncProtocol.block;

import com.plusls.carpet.ModInfo;
import com.plusls.carpet.PcaSettings;
import com.plusls.carpet.network.PcaSyncProtocol;
import com.plusls.carpet.util.PcaBlockEntityDirtyHook;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.Hopper;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//#if MC >= 11700
import org.spongepowered.asm.mixin.injection.ModifyVariable;
//#else
//$$ import org.spongepowered.asm.mixin.injection.Inject;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//#endif

@Mixin(HopperBlockEntity.class)
public abstract class MixinHopperBlockEntity extends RandomizableContainerBlockEntity implements Hopper, PcaBlockEntityDirtyHook {

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
            method = "tryMoveItems",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/entity/HopperBlockEntity;setChanged(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V"
            ),
            argsOnly = true
    )
    private static HopperBlockEntity onInsertAndExtract(HopperBlockEntity blockEntity) {
    //#else
    //$$ @Inject(
    //$$         method = "tryMoveItems",
    //$$         at = @At(
    //$$                 value = "INVOKE",
    //$$                 target = "Lnet/minecraft/world/level/block/entity/HopperBlockEntity;setChanged()V"
    //$$         )
    //$$ )
    //$$ private void onInsertAndExtract(CallbackInfoReturnable<Boolean> cir) {
    //$$     HopperBlockEntity blockEntity = (HopperBlockEntity)(Object)this;
    //#endif
        if (PcaSettings.pcaSyncProtocol && PcaSyncProtocol.syncBlockEntityToClient(blockEntity)) {
            ModInfo.LOGGER.debug("update HopperBlockEntity: {}", blockEntity.getBlockPos());
        }
        //#if MC >= 11700
        return blockEntity;
        //#endif
    }

    @Override
    public void pca$onMarkDirty() {
        if (PcaSettings.pcaSyncProtocol && PcaSyncProtocol.syncBlockEntityToClient(this)) {
            ModInfo.LOGGER.debug("update HopperBlockEntity: {}", this.worldPosition);
        }
    }
}