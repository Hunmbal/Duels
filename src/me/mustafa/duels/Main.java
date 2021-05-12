package me.mustafa.duels;

import java.util.Arrays;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import me.mustafa.duels.arenas.ArenaManager;
import me.mustafa.duels.commands.Duel;
import me.mustafa.duels.commands.DuelAccept;
import me.mustafa.duels.commands.setup.ArenaSetup;
import me.mustafa.duels.commands.setup.KitSetup;
import me.mustafa.duels.file.FileManager;
import me.mustafa.duels.inventories.DuelInventory;
import me.mustafa.duels.kits.KitManager;
import me.mustafa.duels.listeners.InventoryListeners;
import me.mustafa.duels.listeners.MatchListeners;
import me.mustafa.duels.match.MatchManager;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.MinecraftServer;

@Getter
public class Main extends JavaPlugin {
	
	
	
	public void onEnable() {
		instance = this;
		this.saveDefaultConfig();

		System.out.print(ChatColor.RED.toString() + ChatColor.BOLD + "______ ENABLING DUELS ______ " + ChatColor.YELLOW + "Join our discord for support");
		registerManagers();
		registerListeners();
		registerCommands();
		
		
	}
	

	public void onDisable() {
		this.filemanager.save();	
	}
	

	private static Main instance;
	private FileManager filemanager;
	private KitManager kitManager;
	private ArenaManager arenaManager;
	private MatchManager matchmanager;
	private DuelInventory invManager;
	private MStrings mStrings;
	
	private void registerManagers() {
		this.mStrings = new MStrings();
		this.filemanager = new FileManager(this);
		this.kitManager = new KitManager(this);
		this.arenaManager = new ArenaManager(this);
		this.matchmanager = new MatchManager();
		this.invManager = new DuelInventory(this);
	}
	
	private void registerListeners() {
		 Arrays.<Listener>asList(new Listener[] {
		    	(Listener)new InventoryListeners(),
		    	(Listener)new MatchListeners()
		 }).forEach(listener -> getServer().getPluginManager().registerEvents(listener, (Plugin)this));
	}

	private void registerCommands() {
		 Arrays.<Command>asList(new Command[] { 
				(Command)new Duel(this),
				(Command)new DuelAccept(),
				(Command)new KitSetup(this),
				(Command)new ArenaSetup(),
		  }).forEach(command -> registerCommand(command, getName()));
	}
	  
	public void registerCommand(Command cmd, String fallbackPrefix) {
		 (MinecraftServer.getServer()).server.getCommandMap().register(cmd.getName(), fallbackPrefix, cmd);
	}

	public static Main getInstance() {	
		return instance;
	}
	
	public List<String> getCommandList() {
		return this.getConfig().getStringList("Duel-End-Commands");
	}
	public int getComboTicks() {
		return this.getConfig().getInt("Combo-Ticks");
	}
	

	
}
