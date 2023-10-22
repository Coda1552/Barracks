package codyhuh.barracks.common.world;

import codyhuh.barracks.FastNoiseLite;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public abstract class AbstractReefRockFeature extends Feature<NoneFeatureConfiguration> {

    public AbstractReefRockFeature(Codec<NoneFeatureConfiguration> pCodec) {
        super(pCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> pContext) {
        WorldGenLevel worldGenLevel = pContext.level();
        BlockPos blockPos = pContext.origin();
        RandomSource random = pContext.random();

        FastNoiseLite noise = createNoise(worldGenLevel.getSeed() + random.nextLong(), 0.11F);

        //Minecraft.getInstance().getChatListener().handleSystemMessage(Component.literal("Where am I? " + blockPos), false);

        BlockPos heightmapPos = worldGenLevel.getHeightmapPos(Heightmap.Types.OCEAN_FLOOR_WG, blockPos);
        blockPos = heightmapPos.offset(0, -4, 0); // Offset the origin once instead of multiple times

        if (worldGenLevel.getBlockState(heightmapPos.below()).is(BlockTags.SAND) && worldGenLevel.getBlockState(blockPos.offset(0, getConfigs()[0][1], 0)).is(Blocks.WATER) && random.nextFloat() > 0.75F) {
            createRock(worldGenLevel, blockPos, noise);
            return true;
        }
        else {
            return false;
        }
    }

    public abstract int[][] getConfigs();

    public void createRock(WorldGenLevel worldgenlevel, BlockPos origin, FastNoiseLite noise) {
        int radius;
        int height;

        BlockState block = Blocks.LIGHT_GRAY_WOOL.defaultBlockState();
        BlockState block2 = Blocks.GRAY_WOOL.defaultBlockState();
        BlockState block3 = Blocks.AIR.defaultBlockState();


        for (int i = 0; i < getConfigs().length; i++) {
            radius = getConfigs()[i][0];
            height = getConfigs()[i][1];
            boolean finalSection = i + 1 >= getConfigs().length;

            createRockSection(worldgenlevel, origin, radius, height, block, block2, block3, noise, finalSection);
        }

    }

    private static void createRockSection(WorldGenLevel worldgenlevel, BlockPos origin, int radius, int height, BlockState block, BlockState block2, BlockState block3, FastNoiseLite noise, boolean finalSection) {
        int heightLower = 0;

        if(finalSection) {
            heightLower = -height;
        }

        for (int x = -radius; x < radius; x++) {
            for (int y = heightLower; y < height; y++) {
                for (int z = -radius; z < radius; z++) {
                    BlockPos pos = origin.offset(x, y, z);

                    double distance = distance(x, y, z, radius, height, radius);
                    float f = noise.GetNoise(x, (float) y, z);

                    if (distance < 1) {
                        if (f < 0 && f > -0.3) {
                            worldgenlevel.setBlock(pos, Blocks.STONE.defaultBlockState(), 3);
                        } else if (f <= 0.4 && f > 0) {
                            worldgenlevel.setBlock(pos, Blocks.STONE.defaultBlockState(), 3);
                        } else if (f > 0.4 && f < 0.9 && (pos.getY() > origin.getY() + 15)) {
                            worldgenlevel.setBlock(pos, Blocks.STONE.defaultBlockState(), 3);
                        } else if (pos.getY() < 63) {
                            worldgenlevel.setBlock(pos, Blocks.WATER.defaultBlockState(), 3);
                        } else if (pos.getY() < 40) {
                            worldgenlevel.setBlock(pos, block3, 3);
                        }
                        else {
                            worldgenlevel.setBlock(pos, block3, 3);
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
        noise.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2S);
        noise.SetFrequency(frequency);
        return noise;
    }
}