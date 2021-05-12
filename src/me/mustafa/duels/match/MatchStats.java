package me.mustafa.duels.match;

import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;


public class MatchStats {


	String stats;
	String heart = StringEscapeUtils.unescapeJava("❤");
	
	public String getWinnerStats(Player winner, boolean isPot) {
		
		if (isPot) {
			ItemStack[] contents = winner.getInventory().getContents();
			int pots=0;
			for (ItemStack c: contents) {
				if (c !=null) {
					if (c.getType()==Material.POTION) {pots=pots+1;}
				}
			}
			
			stats = ChatColor.GREEN + "Winner: " + ChatColor.GRAY + winner.getName() 
			+ " [" +ChatColor.YELLOW+pots+ChatColor.GRAY + "]";
			
		} else {
			stats = ChatColor.GREEN + "Winner: " + ChatColor.GRAY + winner.getName()
			+ " [" +ChatColor.RED+ Math.round(winner.getHealth() / 2.0D * 2.0D) / 2.0D+ heart +ChatColor.GRAY+ "]";
		}
		
		
		
		
		

		
		return stats;
		
	}
	
	public String getLoserStats(Player loser, boolean isPot) {


		if (isPot) {
			ItemStack[] contents = loser.getInventory().getContents();
			int pots=0;
			for (ItemStack c: contents) {
				if (c !=null) {
					if (c.getType()==Material.POTION) {pots=pots+1;}
				}
			}
			
			stats = ChatColor.RED + "Loser: " + ChatColor.GRAY + loser.getName() 
			+ " [" +ChatColor.YELLOW+pots +ChatColor.GRAY+ "]";
			
		} else {
			stats = ChatColor.RED + "Loser: " + ChatColor.GRAY + loser.getName()
			+ " [" +ChatColor.RED+ 0.0 + heart + ChatColor.GRAY+ "]";
		}

		
		

		return stats;
	}
	
	
	
}
