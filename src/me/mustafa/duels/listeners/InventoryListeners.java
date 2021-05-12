package me.mustafa.duels.listeners;


import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.mustafa.duels.MStrings;
import me.mustafa.duels.Main;
import me.mustafa.duels.kits.Kit;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class InventoryListeners implements Listener {
	
	private Main plugin = Main.getInstance();
	private MStrings msgs = MStrings.getInstance();
	
	TextComponent acceptMsg;
	
	public InventoryListeners() {
		acceptMsg = new TextComponent(msgs.clickHere);
	}
	
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		if (e.getInventory().getTitle().equalsIgnoreCase("Duels")) {
			if (e.getCurrentItem()==null) {
				return;
			}
			e.setCancelled(true);
			String selected = "";
			for (Kit kit: this.plugin.getKitManager().getKitList().values()) {
				if (e.getCurrentItem().getType()!=null || e.getCurrentItem().getType()!=Material.AIR) {
					if (e.getCurrentItem().getType() == kit.getLogo().getType()) {
						selected = kit.getName();
						break;
					}
				}
				
			}
			if (selected==null || !this.plugin.getKitManager().getKitList().containsKey(selected)) {
				return;
			}
			Player p = (Player)e.getWhoClicked();
			Player target = (Player)this.plugin.getMatchmanager().getSelectingkit().get(e.getWhoClicked());

			this.plugin.getMatchmanager().addPending(selected, p, target);
			
			acceptMsg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/accept " + p.getName()));
			target.sendMessage(colorize(StringUtils.replace(msgs.req, "%player%", p.getName())));
			target.spigot().sendMessage(acceptMsg);
			
			p.closeInventory();
			
		}
	}

	private String colorize(String string) {
		return org.bukkit.ChatColor.translateAlternateColorCodes('&', string);
	}

}
