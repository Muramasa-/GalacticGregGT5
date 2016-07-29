package bloodasp.galacticgreg;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_Config;
import gregtech.api.world.GT_Worldgen;
import gregtech.common.blocks.GT_TileEntity_Ores;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

public class GT_Worldgen_GT_Ore_SmallPieces_Space extends GT_Worldgen {
	public final short mMinY;
	public final short mMaxY;
	public final short mAmount;
	public final short mMeta;
	public final boolean mMoon;
	public final boolean mMars;

	public GT_Worldgen_GT_Ore_SmallPieces_Space(String aName, boolean aDefault, int aMinY, int aMaxY, int aAmount, boolean aMoon, boolean aMars, boolean aEnd, Materials aPrimary) {
		super(aName, GalacticGreg.sWorldgenList, aDefault);
		this.mMinY = ((short) GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "MinHeight", aMinY));
		this.mMaxY = ((short) Math.max(this.mMinY + 1, GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "MaxHeight", aMaxY)));
		this.mAmount = ((short) Math.max(1, GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "Amount", aAmount)));
		this.mMeta = ((short) GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "Ore", aPrimary.mMetaItemSubID));
		this.mMoon = GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "Moon", aMoon);
		this.mMars = GregTech_API.sWorldgenFile.get("worldgen." + this.mWorldGenName, "Mars", aMars);
	}

	public boolean executeWorldgen(World aWorld, Random aRandom, String aBiome, int aDimensionType, int aChunkX, int aChunkZ, IChunkProvider aChunkGenerator, IChunkProvider aChunkProvider) {
		if (((aDimensionType == -29) && (!this.mMars)) || ((aDimensionType == -28) && (!this.mMoon)) || ((aDimensionType == 1) || (aDimensionType == 0) || (aDimensionType == -1))) {
			return false;
		}
		if (this.mMeta > 0) {
			int i = 0;
			for (int j = Math.max(1, this.mAmount / 2 + aRandom.nextInt(this.mAmount) / 2); i < j; i++) {
				GT_TileEntity_Ores_Space.setOreBlock(aWorld, aChunkX + aRandom.nextInt(16), this.mMinY + aRandom.nextInt(Math.max(1, this.mMaxY - this.mMinY)), aChunkZ + aRandom.nextInt(16), this.mMeta + 16000);
			}
		}
		return true;
	}
}
