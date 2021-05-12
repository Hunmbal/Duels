package me.mustafa.duels.arenas;

import java.util.HashMap;

import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import lombok.Getter;
import me.mustafa.duels.Main;
import me.mustafa.duels.file.Config;

@Getter
public class ArenaManager {

	
	private Main plugin;
	private HashMap<String, Arena> arenaList = new HashMap<String, Arena>();
	private Object[] arenas;
	
	Config config;
	FileConfiguration fileConfig;
	ConfigurationSection arenaSection;
	
	public ArenaManager(Main m) {
		plugin = m;
		config = this.plugin.getFilemanager().getArenas();
		fileConfig = config.getConfig();
   		arenaSection = fileConfig.getConfigurationSection("arenas");
		
   		
   		if (arenaSection != null) {
   			arenaSection.getKeys(false).forEach(name -> {
			
			Location a = (Location) arenaSection.get(name + ".a");	
			Location b = (Location) arenaSection.get(name + ".b");	
   			boolean isWhitelist = (boolean) arenaSection.get(name + ".isWhitelisted");
			
			Arena arena = new Arena();
			arena.setA(a);
			arena.setB(b);
			arena.setWhitelisted(isWhitelist);
			arenaList.put(name, arena);

			});						
   		}
   		arenas = arenaList.values().toArray();
	}
	
	
	public void save() {
	      fileConfig.set("arenas", null);
	      if (this.arenaList.isEmpty()) {
	    	  return;
	      }
	      
	      this.arenaList.forEach((name, arena) -> {
	         fileConfig.set("arenas." + name + ".a", arena.getA());
	         fileConfig.set("arenas." + name + ".b", arena.getB());
	         fileConfig.set("arenas." + name + ".isWhitelisted", arena.isWhitelisted());
	      });
	      config.save();
	      
	}
	
	Random generator = new Random();
	
	public Arena getRandomArena(String kit) {
		
		Arena randomArena;
		
	    if (this.plugin.getKitManager().getKitList().get(kit).isCertainArena()) {
	    	List<String> selectedList = this.plugin.getKitManager().getKitList().get(kit).getArenas();
	    	String arenaName = selectedList.get(generator.nextInt(selectedList.size()));
	    	randomArena = this.arenaList.get(arenaName);
	    } else {
	    	do {
	    		randomArena = (Arena) arenas[generator.nextInt(arenas.length)];
	    	} while (randomArena.isWhitelisted());
	    }
		
		return randomArena;
	}


	public void createArena(String name) {
		Arena arena = new Arena();
		arena.setName(name);
		this.arenaList.put(name, arena);
		
	}
	public void updateList() {
		arenas = arenaList.values().toArray();
	}
	
	
}
