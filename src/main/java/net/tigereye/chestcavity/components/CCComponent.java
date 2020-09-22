package net.tigereye.chestcavity.components;

//import dev.onyxstudios.cca.api.v3.util.PlayerComponent;
import net.tigereye.chestcavity.items.CC_Items;
import net.tigereye.chestcavity.listeners.*;

import dev.onyxstudios.cca.api.v3.entity.PlayerComponent;
import nerdhub.cardinal.components.api.util.sync.EntitySyncedComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import net.minecraft.item.ItemStack;

public class CCComponent implements InventoryComponent, EntitySyncedComponent, PlayerComponent<InventoryComponent> {
    private EnderChestInventory inventory = new EnderChestInventory();
    private PlayerEntity owner;
    private ChestCavityListener CCListener;

    public CCComponent (PlayerEntity owner){
        this.owner = owner;
        initChestCavityInventory();
        initCCListener();
    }

    @Override
    public void sync() {
        if (!this.getEntity().world.isClient) {
            //only sync with the holder, not everyone
            this.syncWith((ServerPlayerEntity) this.getEntity());
        }
    }

    @Override
    public boolean shouldCopyForRespawn(boolean lossless, boolean keepInventory) {
        return true;
    }

    @Override
    public void fromTag(CompoundTag tag) {
        inventory.readTags((ListTag) tag.get("chestcavity"));
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        tag.put("chestcavity", inventory.getTags());
        return tag;
    }

    @Override
    public LivingEntity getEntity() {
        return this.owner;
    }

    @Override
    public EnderChestInventory getInventory() {
        return this.inventory;
    }

    public ChestCavityListener getCCListener(){
        return this.CCListener;
    }
    public void setInventory(EnderChestInventory inventory){
        this.inventory = inventory;
        sync();
    }

    public void setEntity(PlayerEntity player){
        this.owner = player;
        sync();
    }

    public void initCCListener()
	{
		System.out.println("Initializing Chest Cavity Listener!");
		if(CCListener == null)
		{
			CCListener = new ChestCavityListener(owner);
            inventory.addListener(CCListener);
            CCListener.EvaluateChestCavity(inventory);
		}
			
    }
    
    public void initChestCavityInventory()
	{
		
        inventory = new EnderChestInventory();
        inventory.setStack(0, new ItemStack(CC_Items.MUSCLE, 64));
        inventory.setStack(1, new ItemStack(CC_Items.RIB, 4));
        inventory.setStack(2, new ItemStack(CC_Items.APPENDIX, 1));
        inventory.setStack(3, new ItemStack(CC_Items.LUNG, 1));
        inventory.setStack(4, new ItemStack(CC_Items.HEART, 1));
        inventory.setStack(5, new ItemStack(CC_Items.LUNG, 1));
        //inventory.setStack(6, new ItemStack(NOTHING));
        inventory.setStack(7, new ItemStack(CC_Items.RIB, 4));
        inventory.setStack(8, new ItemStack(CC_Items.MUSCLE, 64));
        inventory.setStack(9, new ItemStack(CC_Items.MUSCLE, 64));
        inventory.setStack(10, new ItemStack(CC_Items.RIB, 4));
        inventory.setStack(11, new ItemStack(CC_Items.SPLEEN, 1));
        inventory.setStack(12, new ItemStack(CC_Items.KIDNEY, 1));
        inventory.setStack(13, new ItemStack(CC_Items.SPINE, 1));
        inventory.setStack(14, new ItemStack(CC_Items.KIDNEY, 1));
        inventory.setStack(15, new ItemStack(CC_Items.LIVER, 1));
        inventory.setStack(16, new ItemStack(CC_Items.RIB, 4));
        inventory.setStack(17, new ItemStack(CC_Items.MUSCLE, 64));
        inventory.setStack(18, new ItemStack(CC_Items.MUSCLE, 64));
        inventory.setStack(19, new ItemStack(CC_Items.MUSCLE, 64));
        inventory.setStack(20, new ItemStack(CC_Items.INTESTINE));
        inventory.setStack(21, new ItemStack(CC_Items.INTESTINE));
        inventory.setStack(22, new ItemStack(CC_Items.STOMACH));
        inventory.setStack(23, new ItemStack(CC_Items.INTESTINE));
        inventory.setStack(24, new ItemStack(CC_Items.INTESTINE));
        inventory.setStack(25, new ItemStack(CC_Items.MUSCLE, 64));
        inventory.setStack(26, new ItemStack(CC_Items.MUSCLE, 64));
			
	}

    public void chestCavityPostMortem(){
        rejectForeignObjects();
        insertWelfareOrgans();
    }

    private void rejectForeignObjects(){
        //TODO: use human_organ tag
        for(int i = 0; i < inventory.size(); i++){
            if(inventory.getStack(i).getItem() != CC_Items.APPENDIX
            && inventory.getStack(i).getItem() != CC_Items.HEART
            && inventory.getStack(i).getItem() != CC_Items.INTESTINE
            && inventory.getStack(i).getItem() != CC_Items.KIDNEY
            && inventory.getStack(i).getItem() != CC_Items.LIVER
            && inventory.getStack(i).getItem() != CC_Items.LUNG
            && inventory.getStack(i).getItem() != CC_Items.MUSCLE
            && inventory.getStack(i).getItem() != CC_Items.RIB
            && inventory.getStack(i).getItem() != CC_Items.SPINE
            && inventory.getStack(i).getItem() != CC_Items.SPLEEN
            && inventory.getStack(i).getItem() != CC_Items.STOMACH
            )
            owner.dropStack(inventory.removeStack(i));
        }
    }

    private void insertWelfareOrgans(){
        //urgently essential organs are: heart, spine, lung
        if(inventory.count(CC_Items.HEART) == 0){
            forcefullyAddStack(new ItemStack(CC_Items.ROTTEN_HEART),4);
        }
        if(inventory.count(CC_Items.LUNG) == 0){
            forcefullyAddStack(new ItemStack(CC_Items.ROTTEN_LUNG),3);
        }
        if(inventory.count(CC_Items.SPINE) == 0){
            forcefullyAddStack(new ItemStack(CC_Items.ROTTEN_SPINE),13);
        }
    }

    private void forcefullyAddStack(ItemStack stack, int slot){
        if(!inventory.canInsert(stack)){
            owner.dropStack(inventory.removeStack(slot));
        }
        inventory.addStack(stack);
    }
}