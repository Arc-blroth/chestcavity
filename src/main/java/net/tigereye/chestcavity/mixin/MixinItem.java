package net.tigereye.chestcavity.mixin;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.tigereye.chestcavity.chestcavities.organs.GeneratedOrganManager;
import net.tigereye.chestcavity.chestcavities.organs.OrganData;
import net.tigereye.chestcavity.util.OrganUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Item.class)
public class MixinItem {

    @Inject(at = @At("HEAD"), method = "appendTooltip")
    public void chestCavityItemAppendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context, CallbackInfo info){
        //((Item)(Object)this)
        Identifier id = Registry.ITEM.getId(((Item)(Object)this));
        if(GeneratedOrganManager.GeneratedOrganData.containsKey(id)){
            OrganData data = GeneratedOrganManager.GeneratedOrganData.get(id);
            if(!data.pseudoOrgan){
                OrganUtil.displayOrganQuality(data.organScores,tooltip);
                OrganUtil.displayCompatibility(stack,world,tooltip,context);
            }
        }
    }

}