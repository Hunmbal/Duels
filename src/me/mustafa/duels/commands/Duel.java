package me.mustafa.duels.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.mustafa.duels.Main;

public class Duel extends Command {
	
	
	   private final Main plugin;
	   

	   public Duel(Main main) {
	      super("duel");
	      this.setUsage(ChatColor.RED + "Usage: /duel <player>");
	      this.plugin=main;
	   }

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		
		if (!(sender instanceof Player)) {return false;}
		if (!sender.hasPermission("duels.basic")) {return false;}
		
		if (args.length < 1) {
			sender.sendMessage(this.usageMessage);
			return false;
		} else if (this.plugin.getMatchmanager().getPending().containsKey(sender)) {
			sender.sendMessage(ChatColor.RED + "You are on command cooldown");
			return false;
			
		}
		
		Player p = (Player)sender;
		Player target = Bukkit.getPlayer(args[0]);
		
		if (target==null) {
			p.sendMessage(ChatColor.RED + "Player not online");
			return false;
		} else if (target==p) {
			p.sendMessage(ChatColor.RED + "You cant duel yourself...");
			return false;
		} else if (this.plugin.getMatchmanager().getPlaying().contains(target) || this.plugin.getMatchmanager().getWaiting().contains(target)) {
			p.sendMessage(ChatColor.RED + "Player is currently in a duel");
			return false;
		}
		
		
		p.openInventory(this.plugin.getInvManager().getDuelsInv());
		
		
		this.plugin.getMatchmanager().getSelectingkit().put(p, target);
		

		
		
		return false;
		
	}



}
