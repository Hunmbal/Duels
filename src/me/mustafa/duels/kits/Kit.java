package me.mustafa.duels.kits;

import java.util.Collection;
import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class Kit {
	
	private String name;
	private ItemStack logo;
	private ItemStack[] armor;
	private ItemStack[] items;
	private Collection<PotionEffect> effects;
	
	private boolean certainArena;
	private List<String> arenas;
	
	private boolean isCombo;
	
	public void addArena(String name) {
		arenas.add(name);
	}
	
	

}
