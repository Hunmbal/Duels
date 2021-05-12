package me.mustafa.duels.kits;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import lombok.Getter;
import me.mustafa.duels.Main;
import me.mustafa.duels.file.Config;


@Getter
public class KitManager {
	
	private Main plugin;
	private HashMap<String, Kit> kitList = new HashMap<String, Kit>();
	Config config;
	FileConfiguration fileConfig;
	ConfigurationSection kitSection;
	
	@SuppressWarnings("unchecked")
	public KitManager(Main m) {
		plugin = m;
		
		config = this.plugin.getFilemanager().getKits();
		fileConfig = config.getConfig();
   		kitSection = fileConfig.getConfigurationSection("kits");
		
   		
   		if (kitSection != null) {
   			kitSection.getKeys(false).forEach(name -> {
			
			ItemStack logo = (ItemStack) kitSection.get(name + ".logo");	
			ItemStack[] armor = ((List<ItemStack>) kitSection.get(name + ".armor")).toArray(new ItemStack[0]);
			ItemStack[] items = ((List<ItemStack>) kitSection.get(name + ".items")).toArray(new ItemStack[0]);
			Collection<PotionEffect> effects = (Collection<PotionEffect>) kitSection.get(name + ".effects");
			boolean isSpecific = kitSection.getBoolean(name + ".isSpecific");
			List<String> arenas = kitSection.getStringList(name + ".arenas");
			boolean isCombo = kitSection.getBoolean(name + ".isCombo");
			
			Kit kit = new Kit();
			kit.setName(name);
			kit.setLogo(logo);
			kit.setArmor(armor);
			kit.setItems(items);
			kit.setEffects(effects);
			kit.setArenas(arenas);
			kit.setCertainArena(isSpecific);
			kit.setCombo(isCombo);
			kitList.put(name, kit);

			});						
   		} 
   		
	}
	
	public void save() {
	      fileConfig.set("kits", null);
	      if (kitList.isEmpty()) {
	    	  return;
	      }
	      this.kitList.forEach((name, kit) -> {
	         fileConfig.set("kits." + name + ".logo", kit.getLogo());
	         fileConfig.set("kits." + name + ".armor", kit.getArmor());
	         fileConfig.set("kits." + name + ".items", kit.getItems());
	         fileConfig.set("kits." + name + ".effects", kit.getEffects());
	         fileConfig.set("kits." + name + ".arenas", kit.getArenas());
	         fileConfig.set("kits." + name + ".isSpecific", kit.isCertainArena());
	         fileConfig.set("kits." + name + ".isCombo", kit.isCombo());
	      });
	      config.save();
	      
	}
	
	
	public void equipKit(Player p, String name) {
		p.getInventory().setContents(kitList.get(name).getItems());
		p.getInventory().setArmorContents(kitList.get(name).getArmor());
		p.addPotionEffects(kitList.get(name).getEffects());
		p.updateInventory();

	}

}
