package io.github.llamarama.team.plantek.mixin;

import io.github.llamarama.team.plantek.common.register.PBlocks;
import io.github.llamarama.team.plantek.common.register.PTags;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CropBlock.class)
public abstract class MixinCropBlock extends PlantBlock implements Fertilizable {

    protected MixinCropBlock(Settings settings) {
        super(settings);
    }

    @Inject(at = @At("HEAD"), method = "canPlantOnTop", cancellable = true)
    public void canPlantOnTop(BlockState floor, BlockView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        Biome.Category category = ((WorldView) world).getBiome(pos).getCategory();
        Biome.Category tagCategory = this.getBiomeCategory();
        if ((category == tagCategory && floor.isOf(Blocks.FARMLAND) && tagCategory != Biome.Category.NONE) || floor.isOf(PBlocks.FERTILE_SOIL)) {
            cir.setReturnValue(true);
        } else {
            cir.setReturnValue(false);
        }
    }

    private Biome.Category getBiomeCategory() {
        if (this.getDefaultState().isIn(PTags.Blocks.DESERT_CROP)) {
            return Biome.Category.DESERT;
        } else if (this.getDefaultState().isIn(PTags.Blocks.FOREST_CROP)) {
            return Biome.Category.FOREST;
        } else if (this.getDefaultState().isIn(PTags.Blocks.MOUNTAINS_CROP)) {
            return Biome.Category.EXTREME_HILLS;
        } else if (this.getDefaultState().isIn(PTags.Blocks.PLAINS_CROP)) {
            return Biome.Category.PLAINS;
        } else {
            return Biome.Category.NONE;
        }
    }

}
