package com.plusls.carpet.mixin.rule.pcaSyncProtocol.block;

import com.plusls.carpet.ModInfo;
import com.plusls.carpet.PcaSettings;
import com.plusls.carpet.network.PcaSyncProtocol;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Objects;

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

    @Inject(method = "tickBees", at = @At(value = "INVOKE", target = "Ljava/util/Iterator;remove()V", shift = At.Shift.AFTER))
    //#if MC >= 11700
    private static void postTickBees(World world, BlockPos pos, BlockState state, List<?> bees, BlockPos flowerPos, CallbackInfo ci) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity == null) {
            return;
        }
    //#else
    //$$ private void postTickBees(CallbackInfo ci) {
    //$$     BlockEntity blockEntity = this;
    //#endif
        if (PcaSettings.pcaSyncProtocol && PcaSyncProtocol.syncBlockEntityToClient(blockEntity)) {
            ModInfo.LOGGER.debug("update BeehiveBlockEntity: {}", blockEntity.getPos());
        }
    }

    @Inject(method = "tryReleaseBee", at = @At(value = "RETURN"))
    public void postTryReleaseBee(CallbackInfoReturnable<List<Entity>> cir) {
        if (PcaSettings.pcaSyncProtocol && PcaSyncProtocol.syncBlockEntityToClient(this) && cir.getReturnValue() != null) {
            ModInfo.LOGGER.debug("update BeehiveBlockEntity: {}", this.pos);
        }
    }

    @Inject(
            //#if MC >= 11700
            method = "readNbt",
            //#else
            //$$ method = "fromTag",
            //#endif
            at = @At(value = "RETURN")
    )
    public void postFromTag(CallbackInfo ci) {
        if (PcaSettings.pcaSyncProtocol && PcaSyncProtocol.syncBlockEntityToClient(this)) {
            ModInfo.LOGGER.debug("update BeehiveBlockEntity: {}", this.pos);
        }
    }

    @ModifyVariable(
            //#if MC >= 12006
            //$$ method = "tryEnterHive",
            //#else
            method = "tryEnterHive(Lnet/minecraft/entity/Entity;ZI)V",
            //#endif
            at = @At(
                    value = "INVOKE",
                    //#if MC >= 11700
                    target = "Lnet/minecraft/entity/Entity;discard()V",
                    //#else
                    //$$ target = "Lnet/minecraft/entity/Entity;remove()V",
                    //#endif
                    ordinal = 0
            ),
            argsOnly = true)
    public Entity postEnterHive(Entity entity) {
        if (PcaSettings.pcaSyncProtocol && PcaSyncProtocol.syncBlockEntityToClient(this)) {
            ModInfo.LOGGER.debug("update BeehiveBlockEntity: {}", this.pos);
        }
        return entity;
    }
}