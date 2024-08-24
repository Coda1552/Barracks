package codyhuh.barracks.common.world.feature;

import codyhuh.barracks.util.FastNoiseLite;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class DragonCaveFeature extends Feature<NoneFeatureConfiguration> {

    public DragonCaveFeature(Codec<NoneFeatureConfiguration> pCodec) {
        super(pCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> pContext) {
        WorldGenLevel worldGenLevel = pContext.level();
        BlockPos blockPos = pContext.origin();
        RandomSource random = pContext.random();

        FastNoiseLite noise = createNoise(worldGenLevel.getSeed() + random.nextLong(), 0.4F);

        //Minecraft.getInstance().getChatListener().handleSystemMessage(Component.literal("Where am I? " + blockPos), false);

        blockPos = blockPos.offset(0, -16, 0);

        if (worldGenLevel.getBlockState(blockPos.offset(0, -1, 0)).is(Blocks.WATER) && random.nextFloat() > 0.25F) {
            createRock(worldGenLevel, blockPos, noise);
            return true;
        }
        else {
            return false;
        }
    }

    public void createRock(WorldGenLevel worldgenlevel, BlockPos origin, FastNoiseLite noise) {
        int radius;
        int height;

        int[][] configs = new int[][]{
                {20, 15},
        };

        for (int i = 0; i < configs.length; i++) {
            radius = configs[i][0];
            height = configs[i][1];

            createRockSection(worldgenlevel, origin, radius, height, Blocks.AIR.defaultBlockState(), noise);
        }
    }

    private static void createRockSection(WorldGenLevel worldgenlevel, BlockPos origin, int radius, int height, BlockState hollowBlock, FastNoiseLite noise) {
        for (int x = -radius; x < radius; x++) {
            for (int y = -height; y < height; y++) {
                for (int z = -radius; z < radius; z++) {
                    BlockPos pos = origin.offset(x, y, z);

                    double distance = distance(x, y, z, radius, height, radius);
                    float f = noise.GetNoise(x, (float) y, z);

                    if (distance < 1.25) {
                        if (distance < 0.75) {
                            if (y > -6) {
                                worldgenlevel.setBlock(pos, hollowBlock, 3);
                            }
                            else {
                                if (f < 0.0F && f > -0.4F) {
                                    worldgenlevel.setBlock(pos, Blocks.BLACKSTONE.defaultBlockState(), 3);
                                } else {
                                    worldgenlevel.setBlock(pos, Blocks.SMOOTH_BASALT.defaultBlockState(), 3);
                                }
                            }
                        }
                        else if (distance <= 0.91) {
                            if (f < 0.0F && f > -0.4F) {
                                worldgenlevel.setBlock(pos, Blocks.BLACKSTONE.defaultBlockState(), 3);
                            } else {
                                worldgenlevel.setBlock(pos, Blocks.SMOOTH_BASALT.defaultBlockState(), 3);
                            }
                        }
                        else {
                            worldgenlevel.setBlock(pos, Blocks.SMOOTH_BASALT.defaultBlockState(), 3);
                        }
                    }
                }
            }
        }
    }

    public static double distance(double x, double y, double z, double xRadius, double yRadius, double zRadius) {
        return Mth.square(x / (xRadius)) + Mth.square(y / (yRadius)) + Mth.square(z / (zRadius));
    }

    private static FastNoiseLite createNoise(long seed, float frequency) {
        FastNoiseLite noise = new FastNoiseLite((int) seed);
        noise.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        noise.SetFrequency(frequency);
        return noise;
    }
}