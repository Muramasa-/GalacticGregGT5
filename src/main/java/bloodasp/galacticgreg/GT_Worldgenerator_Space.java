package bloodasp.galacticgreg;

import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.eventhandler.EventBus;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.util.GT_Log;
import gregtech.api.world.GT_Worldgen;
import gregtech.common.blocks.GT_TileEntity_Ores;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

import com.google.common.eventbus.Subscribe;

import micdoodle8.mods.galacticraft.api.event.wgen.GCCoreEventPopulate;
import micdoodle8.mods.galacticraft.core.world.gen.ChunkProviderMoon;
import micdoodle8.mods.galacticraft.planets.asteroids.world.gen.ChunkProviderAsteroids;
import micdoodle8.mods.galacticraft.planets.mars.world.gen.ChunkProviderMars;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderEnd;
import net.minecraft.world.gen.ChunkProviderHell;
import net.minecraftforge.common.MinecraftForge;

public class GT_Worldgenerator_Space implements IWorldGenerator {
	public static boolean sAsteroids = true;
	private final EventBus eventBus = new EventBus();
	private World worldObj;
	private Random randomGenerator;

	private int chunkX;
	private int chunkZ;
	private int mAsteroidProbability = 50;
	private int mEndAsteroidProbability = 300;
	private int mSize = 100;
	private int endMinSize = 50;
	private int endMaxSize = 200;
	private int minSize = 100;
	private int maxSize = 400;
	private boolean endAsteroids=true;
	private boolean gcAsteroids=true;

	public GT_Worldgenerator_Space() {
		// boolean experimental = GregTech_API.sWorldgenFile.get("worldgen",
		// "ExperimentalGCWorldgen", false);
		// if (experimental) {
		// MinecraftForge.EVENT_BUS.register(this);
		// } else {
		if(!GalacticGreg.experimental){
		endAsteroids = GregTech_API.sWorldgenFile.get("galacticgreg", "GenerateAsteroidsInEnd", true);
		endMinSize = GregTech_API.sWorldgenFile.get("galacticgreg", "EndAsteroidMinSize", 50);
		endMaxSize = GregTech_API.sWorldgenFile.get("galacticgreg", "EndAsteroidMaxSize", 200);
		mEndAsteroidProbability = GregTech_API.sWorldgenFile.get("galacticgreg", "EndAsteroidProbability", 300);}
		gcAsteroids = GregTech_API.sWorldgenFile.get("galacticgreg", "GenerateAsteroidsInGC", true);
		minSize = GregTech_API.sWorldgenFile.get("galacticgreg", "AsteroidMinSize", 100);
		maxSize = GregTech_API.sWorldgenFile.get("galacticgreg", "AsteroidMaxSize", 400);
		mAsteroidProbability = GregTech_API.sWorldgenFile.get("galacticgreg", "AsteroidProbability", 50);
		GameRegistry.registerWorldGenerator(this, 1073741923);
		// }
	}

	// @SubscribeEvent
	// public void onPlanetDecorated(GCCoreEventPopulate.Post event) {
	// this.worldObj = event.worldObj;
	// this.randomGenerator = event.rand;
	// this.chunkX = event.chunkX / 16;
	// this.chunkZ = event.chunkZ / 16;
	//
	// Random fmlRandom = new Random(this.worldObj.getSeed());
	// long xSeed = fmlRandom.nextLong() >> 2 + 1L;
	// long zSeed = fmlRandom.nextLong() >> 2 + 1L;
	// long chunkSeed = (xSeed * chunkX + zSeed * chunkZ) ^
	// this.worldObj.getSeed();
	// fmlRandom.setSeed(chunkSeed);
	//
	// generate(fmlRandom, this.chunkX, this.chunkZ, this.worldObj, null, null);
	// }

	public void generate(Random aRandom, int aX, int aZ, World aWorld, IChunkProvider aChunkGenerator, IChunkProvider aChunkProvider) {
		// BlockFalling.fallInstantly = false;
		aRandom = new Random(aRandom.nextInt());
		aX *= 16;
		aZ *= 16;
		String tBiome = aWorld.getBiomeGenForCoords(aX + 8, aZ + 8).biomeName;
		int tDimensionType = 0;
		if (tBiome == null) {
			tBiome = BiomeGenBase.plains.biomeName;
		}
		if ((tBiome.equals("marsFlat")) || ((aChunkGenerator instanceof ChunkProviderMars))) {
			tDimensionType = -29;
		} else if ((tBiome.equals("moon")) || ((aChunkGenerator instanceof ChunkProviderMoon))) {
			tDimensionType = -28;
		} else if ((aWorld.provider.dimensionId == -30) || ((aChunkGenerator instanceof ChunkProviderAsteroids))) {
			tDimensionType = -30;
		} else if ((tBiome.equals(BiomeGenBase.sky.biomeName)) || (aWorld.provider.dimensionId == 1) || ((aChunkGenerator instanceof ChunkProviderEnd))) {
			tDimensionType = 1;
		} else {
			tDimensionType = 0;
		}
		// Generate Ore Mix
		if ((Math.abs(aX / 16) % 3 == 1) && (Math.abs(aZ / 16) % 3 == 1) && (tDimensionType == -29 || tDimensionType == -28)) {
			if ((GT_Worldgen_GT_Ore_Layer_Space.sWeight > 0) && (GT_Worldgen_GT_Ore_Layer_Space.sList.size() > 0)) {
				boolean temp = true;
				int tRandomWeight;
				for (int i = 0; (i < 256) && (temp); i++) {
					tRandomWeight = aRandom.nextInt(GT_Worldgen_GT_Ore_Layer_Space.sWeight);
					for (GT_Worldgen tWorldGen : GT_Worldgen_GT_Ore_Layer_Space.sList) {
						tRandomWeight -= ((GT_Worldgen_GT_Ore_Layer_Space) tWorldGen).mWeight;
						if (tRandomWeight <= 0) {
							try {
								if (tWorldGen.executeWorldgen(aWorld, aRandom, tBiome, tDimensionType, aX, aZ, aChunkGenerator, aChunkProvider)) {
									temp = false;
								}
									break;
							} catch (Throwable e) {
								e.printStackTrace(GT_Log.err);
							}
						}
					}
				}
			}
			// Generate Small Ores
			int i = 0;
			for (int tX = aX - 16; i < 3; tX += 16) {
				int j = 0;
				for (int tZ = aZ - 16; j < 3; tZ += 16) {
					tBiome = aWorld.getBiomeGenForCoords(tX + 8, tZ + 8).biomeName;
					if (tBiome == null) {
						tBiome = BiomeGenBase.plains.biomeName;
					}
					for (GT_Worldgen tWorldGen : GalacticGreg.sWorldgenList) {
						try {
							tWorldGen.executeWorldgen(aWorld, aRandom, tBiome, tDimensionType, tX, tZ, aChunkGenerator, aChunkProvider);
						} catch (Throwable e) {
							e.printStackTrace(GT_Log.err);
						}
					}
					j++;
				}
				i++;
			}
		}
		// Asteroid Gen
		if (((tDimensionType == 1)&& endAsteroids && !GalacticGreg.experimental &&((this.mEndAsteroidProbability <= 1) || (aRandom.nextInt(this.mEndAsteroidProbability) == 0))) || ((tDimensionType == -30) && gcAsteroids && ((this.mAsteroidProbability <= 1) || (aRandom.nextInt(this.mAsteroidProbability) == 0)))) {
			short primaryMeta = 0;
			short secondaryMeta = 0;
			short betweenMeta = 0;
			short sporadicMeta = 0;
			if ((GT_Worldgen_GT_Ore_Layer_Space.sWeight > 0) && (GT_Worldgen_GT_Ore_Layer_Space.sList.size() > 0)) {
				boolean temp = true;
				int tRandomWeight;
				for (int i = 0; (i < 256) && (temp); i++) {
					tRandomWeight = aRandom.nextInt(GT_Worldgen_GT_Ore_Layer_Space.sWeight);
					for (GT_Worldgen_GT_Ore_Layer_Space tWorldGen : GT_Worldgen_GT_Ore_Layer_Space.sList) {
						tRandomWeight -= ((GT_Worldgen_GT_Ore_Layer_Space) tWorldGen).mWeight;
						if (tRandomWeight <= 0) {
							try {
								if ((tWorldGen.mAsteroid && tDimensionType == -30) || (tWorldGen.mEndAsteroid && tDimensionType == 1)) {
									primaryMeta = tWorldGen.mPrimaryMeta;
									secondaryMeta = tWorldGen.mSecondaryMeta;
									betweenMeta = tWorldGen.mBetweenMeta;
									sporadicMeta = tWorldGen.mSporadicMeta;
									temp = false;
								}
									break;
							} catch (Throwable e) {
								e.printStackTrace(GT_Log.err);
							}
						}
					}
				}
			}
			int tX = aX + aRandom.nextInt(16);
			int tY = 50 + aRandom.nextInt(200 - 50);
			int tZ = aZ + aRandom.nextInt(16);
			if (tDimensionType == 1) {
				mSize = aRandom.nextInt((int) (endMaxSize - endMinSize));
			} else {
				mSize = aRandom.nextInt((int) (maxSize - minSize));
			}
			if ((aWorld.getBlock(tX, tY, tZ).isAir(aWorld, tX, tY, tZ))) {
				float var6 = aRandom.nextFloat() * 3.141593F;
				double var7 = tX + 8 + MathHelper.sin(var6) * this.mSize / 8.0F;
				double var9 = tX + 8 - MathHelper.sin(var6) * this.mSize / 8.0F;
				double var11 = tZ + 8 + MathHelper.cos(var6) * this.mSize / 8.0F;
				double var13 = tZ + 8 - MathHelper.cos(var6) * this.mSize / 8.0F;
				double var15 = tY + aRandom.nextInt(3) - 2;
				double var17 = tY + aRandom.nextInt(3) - 2;
				for (int var19 = 0; var19 <= this.mSize; var19++) {
					double var20 = var7 + (var9 - var7) * var19 / this.mSize;
					double var22 = var15 + (var17 - var15) * var19 / this.mSize;
					double var24 = var11 + (var13 - var11) * var19 / this.mSize;
					double var26 = aRandom.nextDouble() * this.mSize / 16.0D;
					double var28 = (MathHelper.sin(var19 * 3.141593F / this.mSize) + 1.0F) * var26 + 1.0D;
					double var30 = (MathHelper.sin(var19 * 3.141593F / this.mSize) + 1.0F) * var26 + 1.0D;
					int tMinX = MathHelper.floor_double(var20 - var28 / 2.0D);
					int tMinY = MathHelper.floor_double(var22 - var30 / 2.0D);
					int tMinZ = MathHelper.floor_double(var24 - var28 / 2.0D);
					int tMaxX = MathHelper.floor_double(var20 + var28 / 2.0D);
					int tMaxY = MathHelper.floor_double(var22 + var30 / 2.0D);
					int tMaxZ = MathHelper.floor_double(var24 + var28 / 2.0D);
					for (int eX = tMinX; eX <= tMaxX; eX++) {
						double var39 = (eX + 0.5D - var20) / (var28 / 2.0D);
						if (var39 * var39 < 1.0D) {
							for (int eY = tMinY; eY <= tMaxY; eY++) {
								double var42 = (eY + 0.5D - var22) / (var30 / 2.0D);
								if (var39 * var39 + var42 * var42 < 1.0D) {
									for (int eZ = tMinZ; eZ <= tMaxZ; eZ++) {
										double var45 = (eZ + 0.5D - var24) / (var28 / 2.0D);
										if ((var39 * var39 + var42 * var42 + var45 * var45 < 1.0D) && (aWorld.getBlock(tX, tY, tZ).isAir(aWorld, tX, tY, tZ))) {
											int ranOre = aRandom.nextInt(50);
											if (ranOre < 3) {
												GT_TileEntity_Ores_Space.setOreBlock(aWorld, eX, eY, eZ, primaryMeta + (tDimensionType == -30 ? 4000 : 2000), true);
											} else if (ranOre < 6) {
												GT_TileEntity_Ores_Space.setOreBlock(aWorld, eX, eY, eZ, secondaryMeta + (tDimensionType == -30 ? 4000 : 2000), true);
											} else if (ranOre < 8) {
												GT_TileEntity_Ores_Space.setOreBlock(aWorld, eX, eY, eZ, betweenMeta + (tDimensionType == -30 ? 4000 : 2000), true);
											} else if (ranOre < 10) {
												GT_TileEntity_Ores_Space.setOreBlock(aWorld, eX, eY, eZ, sporadicMeta + (tDimensionType == -30 ? 4000 : 2000), true);
											} else {
												if (tDimensionType == -30) {
													aWorld.setBlock(eX, eY, eZ, GregTech_API.sBlockGranites, 8, 3);
												} else {
													aWorld.setBlock(eX, eY, eZ, Blocks.end_stone, 0, 0);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}

		}

		// Save Chunk
		// BlockFalling.fallInstantly = true;

		Chunk tChunk = aWorld.getChunkFromBlockCoords(aX, aZ);
		if (tChunk != null) {
			tChunk.isModified = true;
		}
	}
}
