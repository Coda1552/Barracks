package codyhuh.barracks.common.world.feature;

import codyhuh.barracks.util.FastNoiseLite;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class SeaStackFeature extends Feature<NoneFeatureConfiguration> {

    public SeaStackFeature(Codec<NoneFeatureConfiguration> pCodec) {
        super(pCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> pContext) {
        WorldGenLevel worldGenLevel = pContext.level();
        BlockPos blockPos = pContext.origin();
        RandomSource random = pContext.random();

        FastNoiseLite noise = createNoise(worldGenLevel.getSeed() + random.nextLong(), 0.1F);

        //Minecraft.getInstance().getChatListener().handleSystemMessage(Component.literal("Where am I? " + blockPos), false);
        blockPos = blockPos.offset(0, worldGenLevel.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, blockPos.getX(), blockPos.getZ()) - 5, 0);

        createRock(worldGenLevel, blockPos, noise);
        return true;
    }

    public int[][] getConfigs() {
        return new int[][] {
                {4, 75},
                {5, 50}
        };
    }

    public void createRock(WorldGenLevel worldgenlevel, BlockPos origin, FastNoiseLite noise) {
        int radius;
        int height;

        for (int i = 0; i < getConfigs().length; i++) {
            radius = getConfigs()[i][0];
            height = getConfigs()[i][1] + worldgenlevel.getRandom().nextInt(20);
            boolean finalSection = i + 1 >= getConfigs().length;

            createRockSection(worldgenlevel, origin, radius, height, Blocks.AIR.defaultBlockState(), noise, finalSection);
        }

    }

    private static void createRockSection(WorldGenLevel worldgenlevel, BlockPos origin, int radius, int height, BlockState block, FastNoiseLite noise, boolean finalSection) {
        int heightLower = 0;

        if (finalSection) {
            heightLower = -height;
        }

        for (int x = -radius; x < radius; x++) {
            for (int y = heightLower; y < height; y++) {
                for (int z = -radius; z < radius; z++) {
                    BlockPos pos = origin.offset(x, y, z);

                    double distance = distance(x, y, z, radius, height, radius);
                    float f = noise.GetNoise(x, (float) y, z);

                    if (distance < 0.9) {
                        if (!finalSection) {
                            worldgenlevel.setBlock(pos, Blocks.GOLD_BLOCK.defaultBlockState(), 3);
                        }
                        else if (pos.getY() < worldgenlevel.getSeaLevel()) {
                            worldgenlevel.setBlock(pos, Blocks.STONE.defaultBlockState(), 3);
                        }
                        else if (f > -0.5 && f < 0.0) {
                            worldgenlevel.setBlock(pos, Blocks.STONE.defaultBlockState(), 3);
                        }
                        else if (f > 0.0 && f <= 1.0) {
                            worldgenlevel.setBlock(pos, Blocks.STONE.defaultBlockState(), 3);
                        }
                        else {
                            worldgenlevel.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
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