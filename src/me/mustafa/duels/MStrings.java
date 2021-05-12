package me.mustafa.duels;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import lombok.Getter;

@Getter
public class MStrings {

	private Main plugin = Main.getInstance();
	@Getter
	public static MStrings instance;
	
	public String stats;
	public String clickHere;
	public String req;

	public MStrings() {
		instance = this;
		FileConfiguration config = this.plugin.getConfig();
		ConfigurationSection MsgSection = config.getConfigurationSection("Messages");
		
		if (MsgSection == null) {
			init();
			return;
		}

		clickHere = colorize(MsgSection.getString("Request"));
		stats = colorize(MsgSection.getString("Match-End"));
		req = colorize(MsgSection.getString("Click-Here"));
	}
	
	private void init() {
		stats = colorize("&7&m-------&7 Match Stats &7&m-------");
		clickHere = colorize("&c&l(Click-Here-To-Accept)");
		req = colorize("&6&lDuel >> &6&l%player% &e&lhas sent a duel request");
		
	}










	private static String colorize(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
		
		
	}
	
	
	
}
