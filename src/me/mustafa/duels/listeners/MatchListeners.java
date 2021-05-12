package me.mustafa.duels.listeners;

import java.util.ArrayList;
import java.util.HashMap;


import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import me.mustafa.duels.MStrings;
import me.mustafa.duels.Main;
import me.mustafa.duels.arenas.Arena;
import me.mustafa.duels.events.DuelEndEvent;
import me.mustafa.duels.events.DuelStartEvent;
import me.mustafa.duels.match.MatchCountdown;
import net.md_5.bungee.api.ChatColor;

public class MatchListeners implements Listener {

	
	private Main plugin = Main.getInstance();
	private MStrings msgs = MStrings.getInstance();
	
	private int comboTicks=this.plugin.getComboTicks();
	private List<Player> inVoid = new ArrayList<>();
	
	private HashMap<String, Location> pLoc = new HashMap<String, Location>();
	private HashMap<String, ItemStack[]> pArmor = new HashMap<String, ItemStack[]>();
	private HashMap<String, ItemStack[]> pItems = new HashMap<String, ItemStack[]>();
	
	private List<String> endCmds = this.plugin.getCommandList();	
	String tempCmd;
	String temp;
	
	@EventHandler
	public void onMatchStart(DuelStartEvent e) {
		Arena arena = this.plugin.getArenaManager().getRandomArena(e.getKit());
		Player p1 = e.getPlayer1();
		Player p2 = e.getPlayer2();
		String name = e.getKit();
		List<Player> pList = new ArrayList<>();
		pList.add(p1);
		pList.add(p2);
		
		if (this.plugin.getKitManager().getKitList().get(name).isCombo()) {
			p1.setMaximumNoDamageTicks(comboTicks);
			p2.setMaximumNoDamageTicks(comboTicks);
		}
		
		this.plugin.getMatchmanager().createMatch(p1, p2);
		this.plugin.getMatchmanager().getMatchPlayers().put(p1.getUniqueId(), pList);
		
		for (Player online: Bukkit.getOnlinePlayers()) {
			online.hidePlayer(p1);
			online.hidePlayer(p2);
			p1.hidePlayer(online);
			p2.hidePlayer(online);
		}
		p1.showPlayer(p2);
		p2.showPlayer(p1);
		
		setInDuel(p1);
		setInDuel(p2);
		
		p1.getInventory().clear();
		p2.getInventory().clear();
		
		p1.teleport(arena.getA());
		p2.teleport(arena.getB());
		
		this.plugin.getKitManager().equipKit(p1, name);
		this.plugin.getKitManager().equipKit(p2, name);
		
		this.plugin.getMatchmanager().getDuelkit().remove(p1);
		this.plugin.getMatchmanager().getPending().remove(p1);
		this.plugin.getMatchmanager().getSelectingkit().remove(p1);
		
		this.plugin.getMatchmanager().getWaiting().add(p1);
		this.plugin.getMatchmanager().getWaiting().add(p2);
		
		
		
		
		(new BukkitRunnable() {
			public void run() {
	      			p1.teleport(arena.getA());
	      			p2.teleport(arena.getB());
	        	  	plugin.getMatchmanager().startMatch(p1, p2);
		          }
		        }).runTaskLater(plugin, 5*20); 
		
		
		
		new MatchCountdown(5, plugin) {      
		    @Override                      
		    public void count(int current) {
		              p1.sendMessage(ChatColor.YELLOW+ "Starts in " + current + "...");        
		              p2.sendMessage(ChatColor.YELLOW+ "Starts in " + current + "...");     
		    }                              
		}.start();  
		
		
	}
	
    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
        	if (this.plugin.getMatchmanager().getWaiting().contains(e.getDamager())) {
        		e.setCancelled(true);
        	}
        }
    }
	
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		if (this.plugin.getMatchmanager().getWaiting().contains(e.getPlayer()) 
				|| this.plugin.getMatchmanager().getPlaying().contains(e.getPlayer())) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		if (this.plugin.getMatchmanager().getWaiting().contains(e.getPlayer()) 
				|| this.plugin.getMatchmanager().getPlaying().contains(e.getPlayer())) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onDeath(EntityDamageEvent e) {
		if (this.inVoid.contains((Player)e.getEntity())) {
			e.setCancelled(true);
			return;
		} else if (this.plugin.getMatchmanager().getPlaying().contains(e.getEntity())) {
			if ((((Damageable) e.getEntity()).getHealth() - e.getFinalDamage()) <= 0 || e.getEntity().getLocation().getY()<0) {
				Player p = (Player)e.getEntity();
				
				e.setCancelled(true);
				
				Player winner = null;
				UUID uuid = this.plugin.getMatchmanager().getMatchID().get(p);
				for (Player temp: this.plugin.getMatchmanager().getMatchPlayers().get(uuid)) {
						if (temp!=p) {
							winner=temp;
							break;
						}
				}
				if (e.getCause()==DamageCause.VOID) {
					p.setGameMode(GameMode.CREATIVE);
					p.teleport(winner);
					p.setGameMode(GameMode.SURVIVAL);
					
					this.inVoid.add(p);
					this.inVoid.add(winner);
				}
				
				
				DuelEndEvent startEvent = new DuelEndEvent(p, winner);
				plugin.getServer().getPluginManager().callEvent(startEvent);
				
			
			} 
		}
		
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		if (this.plugin.getMatchmanager().getPlaying().contains(e.getPlayer()) || this.plugin.getMatchmanager().getWaiting().contains(e.getPlayer())) {
			Player p = e.getPlayer();
			Player winner = null;
			UUID uuid = this.plugin.getMatchmanager().getMatchID().get(p);
			for (Player temp: this.plugin.getMatchmanager().getMatchPlayers().get(uuid)) {
					if (temp!=p) {
						winner=temp;
						break;
					}
			}
			
			DuelEndEvent startEvent = new DuelEndEvent(p, winner);
			plugin.getServer().getPluginManager().callEvent(startEvent);
		}
	}
	
	
	

	@EventHandler
	public void onMatchEnd(DuelEndEvent e) {
		Player loser = e.getLoser();
		Player winner = e.getWinner();
		boolean isPot=false;
		
		for (String cmd: endCmds) {
			tempCmd = StringUtils.replace(cmd, "%loser%", loser.getName());
			tempCmd = StringUtils.replace(cmd, "%winner%", winner.getName());
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), tempCmd);
		}
		

		
		
		if (winner.getInventory().contains(Material.POTION)) {isPot=true;} else {isPot=false;}
		
		temp = this.plugin.getMatchmanager().getMatchStats().getWinnerStats(winner, isPot);
		if (loser!=null) {
			loser.sendMessage(msgs.stats);
			loser.sendMessage(temp);} 
		if (winner!=null) {
			winner.sendMessage(msgs.stats);
			winner.sendMessage(temp);} 
		
		if (loser.getInventory().contains(Material.POTION)) {isPot=true;} else {isPot=false;}
		
		temp = this.plugin.getMatchmanager().getMatchStats().getLoserStats(loser, isPot);
		if (loser!=null) {loser.sendMessage(temp);} 
		if (winner!=null) {winner.sendMessage(temp);} 
		
		if (loser!=null) {
			loser.getInventory().clear();
			loser.hidePlayer(winner);
			loser.setHealth(loser.getMaxHealth());
			loser.sendMessage(ChatColor.RED + "You lost the duel");
		}
		
		if (winner!=null) {
			winner.getInventory().clear();
			winner.hidePlayer(loser);
			winner.setHealth(winner.getMaxHealth());
			winner.sendMessage(ChatColor.GREEN + "You won the duel");
		}
		
		(new BukkitRunnable() {


			public void run() {
	        	  
	      		for (Player online: Bukkit.getOnlinePlayers()) {
	      			if (loser!=null) {
	      				online.showPlayer(loser);loser.showPlayer(online);
	      			}
	      			if (winner!=null) {
	      				online.showPlayer(winner);winner.showPlayer(online);
	      			}
	    		}
	      		
	      		if (loser!=null) {resetPlayer(loser);}
	      		if (winner!=null) {resetPlayer(winner);}

	      		if (inVoid.contains(loser)) {
	      			inVoid.remove(loser);
	      			inVoid.remove(winner);
	      		}
	      		
	        	plugin.getMatchmanager().removeMatch(loser, winner);;
		            
		    }}).runTaskLater(this.plugin, 6*20); 
		

	}
	
	private void setInDuel(Player p) {
		pLoc.put(p.getName(), p.getLocation());
		pArmor.put(p.getName(), p.getInventory().getArmorContents());
		pItems.put(p.getName(), p.getInventory().getContents());
		p.updateInventory();
	}
	
	
	
	private void resetPlayer(Player p) {
		for (PotionEffect effects: p.getActivePotionEffects()) {
			p.removePotionEffect(effects.getType());
		}
		p.setMaximumNoDamageTicks(20);
		p.getInventory().setContents(this.pItems.get(p.getName()));
		p.getInventory().setArmorContents(this.pArmor.get(p.getName()));
		p.updateInventory();
		p.teleport(pLoc.get(p.getName()));
	}
	
	
}
