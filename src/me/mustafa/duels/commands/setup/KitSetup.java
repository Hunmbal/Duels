package me.mustafa.duels.commands.setup;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.mustafa.duels.Main;
import me.mustafa.duels.kits.Kit;



public class KitSetup extends Command {
	
	
	   private final Main plugin;
	   Kit kit = new Kit();
	   String suc = colorize("&aSuccessful!");
	   public KitSetup(Main main) {
		  
	      super("duelskit");
	      this.setUsage(ChatColor.RED + "Usage: /duelskit");
	      this.plugin=main;
	   }

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		
		if (!(sender instanceof Player)) {return false;}
		if (!sender.hasPermission("duels.setup")) {return false;}
		
		Player player = (Player) sender;
		
		if (args.length < 2) {
			player.sendMessage("  ");
			player.sendMessage(colorize("&c&lDUELS COMMANDS:"));
			player.sendMessage(colorize("&7 /duelskit create <name> "));
			player.sendMessage(colorize("&7 /duelskit delete <name>"));
			player.sendMessage(colorize("&7 /duelskit specific <name> <true|false>"));
			player.sendMessage(" ");
			player.sendMessage(colorize("&7 /duelskit setCombo <kit-name> <true|false>"));
			player.sendMessage(colorize("&7 /duelskit addArena <kit-name> <arena-name>... whitelist kit to certain arenas"));
			player.sendMessage(colorize("&7 /duelskit setlogo <name> "));
			player.sendMessage(colorize("&7 /duelskit save <name>"));
			player.sendMessage("  ");

			return true;
		}
		
		
		
		switch (args[0].toLowerCase()) {
		
		case "create":
			kit.setName(args[1]);
			kit.setArmor(player.getInventory().getArmorContents());
			kit.setItems(player.getInventory().getContents());
			kit.setEffects(player.getActivePotionEffects());
			player.sendMessage(suc);
			break;
			
		case "delete":
			this.plugin.getKitManager().getKitList().remove(args[1]);
			player.sendMessage(suc);
			break;
			
		case "setlogo":
			if (player.getItemInHand() == null) {player.sendMessage(colorize("Hold the logo item in your hand")); return false;}
			ItemStack item = player.getItemInHand();
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatColor.RED + kit.getName());
			item.setItemMeta(meta);
			kit.setLogo(item);
			player.sendMessage(suc);
			break;
			
		case "save":
			if (kit.getLogo()==null || kit.getName()==null) {
				player.sendMessage(colorize("&cYou forgot to set kit logo"));
				return false;
			}
			this.plugin.getKitManager().getKitList().put(args[1], kit);
			this.plugin.getInvManager().updateinv(kit.getLogo());
			player.sendMessage(suc);			
			break;
			
		case "specific":
			if (this.plugin.getKitManager().getKitList().containsKey(args[1])) {
				kit = this.plugin.getKitManager().getKitList().get(args[1]); 
				if (args[2].equals("true")) {
					kit.setCertainArena(true);
				} else {
					kit.setCertainArena(false);
				}
				this.plugin.getKitManager().getKitList().put(args[1], kit);
				player.sendMessage(suc);
			}
			break;
			
		case "addarena":
			if(args.length<3) {return false;}
			if (this.plugin.getKitManager().getKitList().containsKey(args[1]) 
					&& this.plugin.getArenaManager().getArenaList().containsKey(args[2])) {
				kit = this.plugin.getKitManager().getKitList().get(args[1]);
				kit.addArena(args[2]);
				this.plugin.getKitManager().getKitList().put(args[1], kit);
				player.sendMessage(suc);
			} else {
				player.sendMessage(colorize("&cNo such kit or arena"));
			}
			break;
			
		case "setcombo":
			if(args.length<3) {return false;}
			if (this.plugin.getKitManager().getKitList().containsKey(args[1])) {
				kit = this.plugin.getKitManager().getKitList().get(args[1]); 
				if (args[2].equals("true")) {
					kit.setCombo(true);
				} else {
					kit.setCombo(false);
				}
				this.plugin.getKitManager().getKitList().put(args[1], kit);
				player.sendMessage(suc);
			}
			break;
		default:
			player.sendMessage(colorize("&cWrong usage .... use /duelskit help"));
			break;
			
			
		
		}
		
		
		return false;
	}
	
	public String colorize(String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}
	

}
