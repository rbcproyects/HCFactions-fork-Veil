package net.bfcode.bfhcf.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.google.common.collect.ImmutableList;

import net.bfcode.bfbase.util.BukkitUtils;
import net.bfcode.bfhcf.HCFaction;
import net.bfcode.bfhcf.timer.type.PvpProtectionTimer;
import net.bfcode.bfhcf.utils.ConfigurationService;

import org.bukkit.command.TabCompleter;
import org.bukkit.command.CommandExecutor;

public class PvpTimerCommand implements CommandExecutor, TabCompleter
{
    private static ImmutableList<String> COMPLETIONS;
    private HCFaction plugin;
    
    public PvpTimerCommand(HCFaction plugin) {
        this.plugin = plugin;
    }
    
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command is only executable by players.");
            return true;
        }
        if (ConfigurationService.KIT_MAP) {
            return true;
        }
        Player player = (Player)sender;
        PvpProtectionTimer pvpTimer = this.plugin.getTimerManager().pvpProtectionTimer;
        if (args.length < 1) {
            this.printUsage(sender, label, pvpTimer);
            return true;
        }
        if (args[0].equalsIgnoreCase("enable") || args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("off")) {
            if (pvpTimer.getRemaining(player) > 0L) {
                sender.sendMessage(ChatColor.RED + "Your " + pvpTimer.getDisplayName() + ChatColor.RED + " timer is now off.");
                pvpTimer.clearCooldown(player);
                return true;
            }
            if (pvpTimer.getLegible().remove(player.getUniqueId())) {
                player.sendMessage(ChatColor.YELLOW + "You will no longer be legible for your " + pvpTimer.getDisplayName() + ChatColor.YELLOW + " when you leave spawn.");
                return true;
            }
            sender.sendMessage(ChatColor.RED + "Your " + pvpTimer.getDisplayName() + ChatColor.RED + " timer is currently not active.");
            return true;
        }
        else {
            if (!args[0].equalsIgnoreCase("remaining") && !args[0].equalsIgnoreCase("time") && !args[0].equalsIgnoreCase("left") && !args[0].equalsIgnoreCase("check")) {
                this.printUsage(sender, label, pvpTimer);
                return true;
            }
            long remaining = pvpTimer.getRemaining(player);
            if (remaining <= 0L) {
                sender.sendMessage(ChatColor.RED + "Your " + pvpTimer.getDisplayName() + ChatColor.RED + " timer is currently not active.");
                return true;
            }
            sender.sendMessage(ChatColor.YELLOW + "Your " + pvpTimer.getDisplayName() + ChatColor.YELLOW + " timer is active for another " + ChatColor.BOLD + HCFaction.getRemaining(remaining, true, false) + ChatColor.YELLOW + (pvpTimer.isPaused(player) ? " and is currently paused" : "") + '.');
            return true;
        }
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return (args.length == 1) ? BukkitUtils.getCompletions(args, (List)PvpTimerCommand.COMPLETIONS) : Collections.emptyList();
    }
    
    private void printUsage(CommandSender sender, String label, PvpProtectionTimer pvpTimer) {
        List<String> toSend = new ArrayList<String>();
        for (String string : HCFaction.getPlugin().getConfig().getStringList("pvp-timer")) {
            string = string.replace("%line%", BukkitUtils.STRAIGHT_LINE_DEFAULT + "");
            toSend.add(string);
        }
        for (String message : toSend) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }
    
    static {
        COMPLETIONS = ImmutableList.of("enable", "time");
    }
}
