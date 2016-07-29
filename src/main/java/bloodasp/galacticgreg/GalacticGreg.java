package bloodasp.galacticgreg;

import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.world.GT_Worldgen;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;

@Mod(modid = GalacticGreg.MODID, version = GalacticGreg.VERSION, dependencies = "required-after:GalacticraftCore; required-after:gregtech; required-after:GalacticraftMars;", acceptableRemoteVersions="*")
public class GalacticGreg {
	public static final String MODID = "galacticgreg";
	public static final String VERSION = "0.7";
	public static final List<GT_Worldgen> sWorldgenList = new ArrayList();
	public static boolean experimental = GregTech_API.VERSION>508;

	@Mod.EventHandler
	public void onPostLoad(FMLPostInitializationEvent aEvent) {
		new WorldGenGaGT().run();
	}
	
	@Mod.EventHandler
	public void onLoad(FMLInitializationEvent aEvent) {
		GT_Values.RA.addCentrifugeRecipe(new ItemStack(GCBlocks.blockMoon, 1, 5), null, null, 
				Materials.Helium_3.getGas(33), 
				new ItemStack(Blocks.sand,1), 
				GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Aluminium, 1), 
				GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcite, 1), 
				GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 1), 
				GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Magnesium, 1), 
				GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Rutile, 1), new int[]{5000,400,400,100,100,100}, 400, 8);
		
		GT_Values.RA.addPulveriserRecipe(new ItemStack(GCBlocks.blockMoon, 1, 4), new ItemStack[]{new ItemStack(GCBlocks.blockMoon, 1, 5)}, null, 400, 2);
		
		GT_Values.RA.addFluidExtractionRecipe(new ItemStack(MarsBlocks.marsBlock, 1, 9), new ItemStack(Blocks.stone, 1), Materials.Iron.getMolten(50), 10000, 250, 16);
	}
}
