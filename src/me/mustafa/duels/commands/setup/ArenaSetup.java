package me.mustafa.duels.commands.setup;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.mustafa.duels.Main;
import me.mustafa.duels.arenas.Arena;

public class ArenaSetup extends Command {
	
	
	   private final Main plugin = Main.getInstance();
	   String suc = colorize("&aSuccessful!");
	   
	   

	   public ArenaSetup() {
	      super("duelsarena");
	      this.setUsage(ChatColor.RED + "Usage: /duelsarena");
	   }

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		
		if (!(sender instanceof Player)) {return false;}
		if (!sender.hasPermission("duels.setup")) {return false;}
		
		Player player = (Player) sender;
		
		if (args.length < 2) {
			player.sendMessage("  ");
			player.sendMessage(colorize("&c&lDUELS COMMANDS:"));
			player.sendMessage(colorize("&7 /duelsarena create <arena-name> "));
			player.sendMessage(colorize("&7 /duelsarena pos1 <arena-name> "));
			player.sendMessage(colorize("&7 /duelsarena pos2 <arena-name>"));
			player.sendMessage(colorize("&7 /duelsarena save <arena-name>"));
			
			player.sendMessage(colorize("&7 /duelsarena whitelist <arena-name>"));
			player.sendMessage("  ");

			return true;
		}
		
		Arena arena = this.plugin.getArenaManager().getArenaList().get(args[1]);
		
		
		switch (args[0].toLowerCase()) {
		
		case "create":
			this.plugin.getArenaManager().createArena(args[1]);
			player.sendMessage(suc);	
			break;
			
		case "pos1":
			if (!args[1].equals(arena.getName())) {
				player.sendMessage(colorize("&cWrong arena name!"));
				return false;
			}
			arena.setA(player.getLocation());
			player.sendMessage(suc);
			break;
			
		case "pos2":
			if (!args[1].equals(arena.getName())) {
				player.sendMessage(colorize("&cWrong arena name!"));
				return false;
			}
			arena.setB(player.getLocation());
			player.sendMessage(suc);
			break;
			
		case "whitelist":
			if (!this.plugin.getArenaManager().getArenaList().containsKey(args[1])) {
				player.sendMessage(colorize("&cArena doesnt exist"));
				return false;
			}
			arena.setWhitelisted(!arena.isWhitelisted());
			this.plugin.getArenaManager().getArenaList().put(arena.getName(), arena);
			player.sendMessage(suc);
			break;
			
		case "save":
			if (!args[1].equals(arena.getName())) {
				player.sendMessage(colorize("&cWrong arena name!"));
				return false;
			}
			this.plugin.getArenaManager().getArenaList().put(arena.getName(), arena);
			this.plugin.getArenaManager().updateList();
			player.sendMessage(suc);	
			break;
			
		default:
			player.sendMessage(colorize("&cWrong usage ..... use /duelsarena help"));
			break;
		
		}
		return false;
	}
	
	public String colorize(String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}
	

}
