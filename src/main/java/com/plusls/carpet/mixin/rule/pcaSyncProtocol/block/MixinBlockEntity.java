package com.plusls.carpet.mixin.rule.pcaSyncProtocol.block;

import com.plusls.carpet.util.PcaBlockEntityDirtyHook;
import net.minecraft.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntity.class)
public abstract class MixinBlockEntity implements PcaBlockEntityDirtyHook
{
    @Inject(method = "markDirty()V", at = @At("RETURN"))
    private void onMarkDirty(CallbackInfo ci) {
        this.pca$onMarkDirty();
    }

    /**
     * 用于回调<br/>
     * Use for callbacks
     */
    @Override
    public void pca$onMarkDirty() {
    }
}
