package bloodasp.galacticgreg;


import gregtech.api.GregTech_API;
import gregtech.common.GT_Worldgen_Stone;
import gregtech.common.blocks.GT_TileEntity_Ores;

import java.lang.reflect.Method;
import java.util.Random;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import micdoodle8.mods.galacticraft.api.event.wgen.GCCoreEventPopulate;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderMoon;
import micdoodle8.mods.galacticraft.core.dimension.WorldProviderOrbit;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import micdoodle8.mods.galacticraft.planets.mars.dimension.WorldProviderMars;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.gen.feature.WorldGenerator;

public class GCGTOreGen {

	private World worldObj;
    private Random randomGenerator;

    private int chunkX;
    private int chunkZ;

    private WorldGenerator oreGen;
    
    
    @SubscribeEvent
    public void onPlanetDecorated(GCCoreEventPopulate.Post event)
    {
    	this.worldObj = event.worldObj;
    	this.randomGenerator = event.rand;
    	this.chunkX = event.chunkX;
    	this.chunkZ = event.chunkZ;
    	
    	int dimDetected = 0;
    	
    	WorldProvider prov = worldObj.provider;
    	if (!(prov instanceof IGalacticraftWorldProvider) || (prov instanceof WorldProviderOrbit))
    		return;
    	
    	Block stoneBlock = null;
    	int stoneMeta = 0;
    	
    	if (prov instanceof WorldProviderMoon)
    	{
    		stoneBlock = GCBlocks.blockMoon;
    		stoneMeta = 4;
    		dimDetected = 1;
    	}
    	else if (GalacticraftCore.isPlanetsLoaded && prov instanceof WorldProviderMars)
    	{
    		stoneBlock = MarsBlocks.marsBlock;
    		stoneMeta = 9;
    		dimDetected = 2;
    	}

    	if (stoneBlock == null) return;

    	Method cls;
    	try {
			cls = GT_TileEntity_Ores.class.getDeclaredMethod("setOreBlock", GT_TileEntity_Ores.class);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
    	
    	
    	new GT_Worldgen_Stone("overworld.stone.redgranite.huge", true, GregTech_API.sBlockGranites, 8, 0, 1, 400, 240, 0, 120, null, true);
    	/*for (OreGenData ore : OreGenOtherMods.data)
    	{
	        if (ore.dimRestrict == 0 || ore.dimRestrict == dimDetected)
	        {
	    		this.oreGen = new WorldGenMinableMeta(ore.oreBlock, ore.sizeCluster, ore.oreMeta, true, stoneBlock, stoneMeta);
		        this.genStandardOre1(ore.numClusters, this.oreGen, ore.minHeight, ore.maxHeight);
	        }
    	}*/   	
    }
    
	
}
