package bloodasp.galacticgreg;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_Config;
import gregtech.api.world.GT_Worldgen;
import gregtech.common.GT_Worldgen_GT_Ore_Layer;
import gregtech.common.blocks.GT_TileEntity_Ores;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

public class GT_Worldgen_GT_Ore_Layer_Space extends GT_Worldgen {
	public final boolean mMoon;
	public final boolean mMars;
	public final boolean mAsteroid;
	public final boolean mEndAsteroid;
	public static ArrayList<GT_Worldgen_GT_Ore_Layer_Space> sList = new ArrayList();
	public static int sWeight = 0;
	public final short mMinY;
	public final short mMaxY;
	public final short mWeight;
	public final short mDensity;
	public final short mSize;
	public final short mPrimaryMeta;
	public final short mSecondaryMeta;
	public final short mBetweenMeta;
	public final short mSporadicMeta;

	public GT_Worldgen_GT_Ore_Layer_Space(String aName, boolean aDefault, int aMinY, int aMaxY, int aWeight, int aDensity, int aSize, boolean aMoon,
			boolean aMars, boolean aEnd, boolean aAsteroids, Materials aPrimary, Materials aSecondary, Materials aBetween, Materials aSporadic) {
		super(aName, sList, aDefault);
		this.mMinY = ((short) GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "MinHeight", aMinY));
		this.mMaxY = ((short) Math.max(this.mMinY + 5, GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "MaxHeight", aMaxY)));
		this.mWeight = ((short) GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "RandomWeight", aWeight));
		this.mDensity = ((short) GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "Density", aDensity));
		this.mSize = ((short) Math.max(1, GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "Size", aSize)));
		this.mPrimaryMeta = ((short) GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "OrePrimaryLayer", aPrimary.mMetaItemSubID));
		this.mSecondaryMeta = ((short) GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "OreSecondaryLayer", aSecondary.mMetaItemSubID));
		this.mBetweenMeta = ((short) GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "OreSporadiclyInbetween", aBetween.mMetaItemSubID));
		this.mSporadicMeta = ((short) GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "OreSporaticlyAround", aSporadic.mMetaItemSubID));
		this.mMoon = GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "Moon", aMoon);
		this.mMars = GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "Mars", aMars);
		this.mAsteroid=GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "Asteroid", aAsteroids);
		if(GalacticGreg.experimental){this.mEndAsteroid=false;}else{
		this.mEndAsteroid=GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "EndAsteroid", aEnd);
		}
		if (this.mEnabled) {
			sWeight += this.mWeight;
		}
	}

	@Override
	public boolean executeWorldgen(World aWorld, Random aRandom, String aBiome, int aDimensionType, int aChunkX, int aChunkZ, IChunkProvider aChunkGenerator,
			IChunkProvider aChunkProvider) {
		if (((aDimensionType == -29) && (!this.mMars)) || ((aDimensionType == -28) && (!this.mMoon))|| ((aDimensionType == 1) || (aDimensionType == 0) || (aDimensionType == -1))) {
			return false;
		}
		int tMinY = this.mMinY + aRandom.nextInt(this.mMaxY - this.mMinY - 5);

		int cX = aChunkX - aRandom.nextInt(this.mSize);
		int eX = aChunkX + 16 + aRandom.nextInt(this.mSize);
		for (int tX = cX; tX <= eX; tX++) {
			int cZ = aChunkZ - aRandom.nextInt(this.mSize);
			int eZ = aChunkZ + 16 + aRandom.nextInt(this.mSize);
			for (int tZ = cZ; tZ <= eZ; tZ++) {
				if (this.mSecondaryMeta > 0) {
					for (int i = tMinY - 1; i < tMinY + 2; i++) {
						if ((aRandom.nextInt(Math.max(1, Math.max(Math.abs(cZ - tZ), Math.abs(eZ - tZ)) / this.mDensity)) == 0)
								|| (aRandom.nextInt(Math.max(1, Math.max(Math.abs(cX - tX), Math.abs(eX - tX)) / this.mDensity)) == 0)) {
							GT_TileEntity_Ores_Space.setOreBlock(aWorld, tX, i, tZ, this.mSecondaryMeta);
						}
					}
				}
				if ((this.mBetweenMeta > 0)
						&& ((aRandom.nextInt(Math.max(1, Math.max(Math.abs(cZ - tZ), Math.abs(eZ - tZ)) / this.mDensity)) == 0) || (aRandom.nextInt(Math.max(1,
								Math.max(Math.abs(cX - tX), Math.abs(eX - tX)) / this.mDensity)) == 0))) {
					GT_TileEntity_Ores_Space.setOreBlock(aWorld, tX, tMinY + 2 + aRandom.nextInt(2), tZ, this.mBetweenMeta);
				}
				if (this.mPrimaryMeta > 0) {
					for (int i = tMinY + 3; i < tMinY + 6; i++) {
						if ((aRandom.nextInt(Math.max(1, Math.max(Math.abs(cZ - tZ), Math.abs(eZ - tZ)) / this.mDensity)) == 0)
								|| (aRandom.nextInt(Math.max(1, Math.max(Math.abs(cX - tX), Math.abs(eX - tX)) / this.mDensity)) == 0)) {

							GT_TileEntity_Ores_Space.setOreBlock(aWorld, tX, i, tZ, this.mPrimaryMeta);
						}
					}
				}
				if ((this.mSporadicMeta > 0)
						&& ((aRandom.nextInt(Math.max(1, Math.max(Math.abs(cZ - tZ), Math.abs(eZ - tZ)) / this.mDensity)) == 0) || (aRandom.nextInt(Math.max(1,
								Math.max(Math.abs(cX - tX), Math.abs(eX - tX)) / this.mDensity)) == 0))) {
					GT_TileEntity_Ores_Space.setOreBlock(aWorld, tX, tMinY - 1 + aRandom.nextInt(7), tZ, this.mSporadicMeta);
				}
			}
		}
		return true;
	}
}
