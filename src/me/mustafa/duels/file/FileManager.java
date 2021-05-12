package me.mustafa.duels.file;

import lombok.Getter;
import me.mustafa.duels.Main;

@Getter
public class FileManager {
	
	private Main plugin;
	private Config arenas;
	private Config kits;

	
	
	public FileManager(Main m) {
		plugin = m;
		arenas = new Config("arenas" , plugin);
		kits = new Config("kits", plugin);

	
	}


	public void save() {
		this.plugin.getKitManager().save();
		this.plugin.getArenaManager().save();
	}

	
	
	

}
