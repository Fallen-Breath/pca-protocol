package com.plusls.carpet.mixin.rule.pcaSyncProtocol.block;

import com.plusls.carpet.ModInfo;
import com.plusls.carpet.PcaSettings;
import com.plusls.carpet.network.PcaSyncProtocol;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

//#if MC >= 12104
//$$ import net.minecraft.world.entity.animal.Bee;
//#endif

// used in mc >= 1.15
@Mixin(BeehiveBlockEntity.class)
public abstract class MixinBeehiveBlockEntity extends BlockEntity {

    public MixinBeehiveBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(
                type
                //#if MC >= 11700
                , pos, state
                //#endif
        );
    }

    @Inject(method = "tickOccupants", at = @At(value = "INVOKE", target = "Ljava/util/Iterator;remove()V", shift = At.Shift.AFTER))
    //#if MC >= 11700
    private static void postTickBees(Level world, BlockPos pos, BlockState state, List<?> bees, BlockPos flowerPos, CallbackInfo ci) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity == null) {
            return;
        }
    //#else
    //$$ private void postTickBees(CallbackInfo ci) {
    //$$     BlockEntity blockEntity = this;
    //#endif
        if (PcaSettings.pcaSyncProtocol && PcaSyncProtocol.syncBlockEntityToClient(blockEntity)) {
            ModInfo.LOGGER.debug("update BeehiveBlockEntity: {}", blockEntity.getBlockPos());
        }
    }

    @Inject(method = "releaseAllOccupants", at = @At(value = "RETURN"))
    public void postTryReleaseBee(CallbackInfoReturnable<List<Entity>> cir) {
        if (PcaSettings.pcaSyncProtocol && PcaSyncProtocol.syncBlockEntityToClient(this) && cir.getReturnValue() != null) {
            ModInfo.LOGGER.debug("update BeehiveBlockEntity: {}", this.worldPosition);
        }
    }

    @Inject(
            method = "load",
            at = @At(value = "RETURN")
    )
    public void postFromTag(CallbackInfo ci) {
        if (PcaSettings.pcaSyncProtocol && PcaSyncProtocol.syncBlockEntityToClient(this)) {
            ModInfo.LOGGER.debug("update BeehiveBlockEntity: {}", this.worldPosition);
        }
    }

    @ModifyVariable(
            //#if MC >= 12006
            //$$ method = "addOccupant",
            //#else
            method = "addOccupantWithPresetTicks(Lnet/minecraft/world/entity/Entity;ZI)V",
            //#endif
            at = @At(
                    value = "INVOKE",
                    //#if MC >= 12104
                    //$$ target = "Lnet/minecraft/world/entity/animal/Bee;discard()V",
                    //#elseif MC >= 11700
                    target = "Lnet/minecraft/world/entity/Entity;discard()V",
                    //#else
                    //$$ target = "Lnet/minecraft/world/entity/Entity;remove()V",
                    //#endif
                    ordinal = 0
            ),
            argsOnly = true)
    //#if MC >= 12104
    //$$ public Bee postEnterHive(Bee entity) {
    //#else
    public Entity postEnterHive(Entity entity) {
    //#endif
        if (PcaSettings.pcaSyncProtocol && PcaSyncProtocol.syncBlockEntityToClient(this)) {
            ModInfo.LOGGER.debug("update BeehiveBlockEntity: {}", this.worldPosition);
        }
        return entity;
    }
}