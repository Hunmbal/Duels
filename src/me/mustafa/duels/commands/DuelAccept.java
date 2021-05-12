package me.mustafa.duels.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.mustafa.duels.Main;
import me.mustafa.duels.events.DuelStartEvent;

public class DuelAccept extends Command {
	
	
	   private final Main plugin = Main.getInstance();
	   

	   public DuelAccept() {
	      super("accept");
	      this.setUsage(ChatColor.RED + "Usage: /accept <player>");
	   }

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		
		if (!(sender instanceof Player)) {return false;}
		if (args.length<1) {return false;}
		
		Player p = (Player)sender;
		Player from = Bukkit.getPlayer(args[0]);
		
		if (from==null) {
			p.sendMessage(ChatColor.RED + "Player not online");
			return false;
		}
		if (!this.plugin.getMatchmanager().getPending().containsKey(from)) {
			p.sendMessage(ChatColor.RED + "No requests pending");
			return false;
		}
		if (this.plugin.getMatchmanager().getPlaying().contains(p) || this.plugin.getMatchmanager().getWaiting().contains(p)) {
			p.sendMessage(ChatColor.RED + "You cannot do this in your current state!");
			return false;
		}
		
		DuelStartEvent startEvent = new DuelStartEvent(from, p, this.plugin.getMatchmanager().getDuelkit().get(from));
		plugin.getServer().getPluginManager().callEvent(startEvent);
		
		
		return true;
	}

}
