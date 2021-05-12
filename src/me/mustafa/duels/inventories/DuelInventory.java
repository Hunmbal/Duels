package me.mustafa.duels.inventories;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import me.mustafa.duels.Main;

public class DuelInventory {

	
	private final Main plugin;
	@Getter
	Inventory duelsInv;
	
	
	public DuelInventory(Main main) {
		this.plugin=main;
		createinv();
				
	}


	private void createinv() {
		int size;
		
		if (this.plugin.getKitManager().getKitList().isEmpty()) {
			size = 9;
		} else {
			size = this.plugin.getKitManager().getKitList().size();
		}
		
		
		size = size + (size%9);
		if (size < 9) {size = 9;}
		
		this.duelsInv  = Bukkit.createInventory(null, size, "Duels");

		if (this.plugin.getKitManager().getKitList().size() != 0) {

			this.plugin.getKitManager().getKitList().forEach((name, Kit) -> {
				duelsInv.addItem(Kit.getLogo());
		      });	
		}		
			
		
	}
	
	public void updateinv(ItemStack logo) {
		this.duelsInv.setItem(this.duelsInv.firstEmpty(), logo);
	}
	
	
	
	
}
