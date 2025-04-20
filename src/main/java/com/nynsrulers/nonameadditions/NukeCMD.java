package com.nynsrulers.nonameadditions;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;

public class NukeCMD implements CommandExecutor {
    private final NoNameAdditions plugin;
    public NukeCMD(NoNameAdditions plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CoreTools.getInstance().getPrefix() + ChatColor.RED + "This command can only be used by players!");
            return false;
        }
        if (!sender.hasPermission("nncore.cmd.nuke")) {
            sender.sendMessage(CoreTools.getInstance().getPrefix() + ChatColor.RED + "You don't have permission to do this!");
            return false;
        }
        Nuke nuke = null;
        Location nukeSpawn = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY() + 10, player.getLocation().getZ());

        if (args.length == 0) {
            nuke = new Nuke(12, 20, nukeSpawn, false);
        }
        if (args.length == 1) {
            if (Integer.parseInt(args[0]) <= 1) {
                sender.sendMessage(CoreTools.getInstance().getPrefix() + ChatColor.RED + "You must enter a number greater than 1!");
                return false;
            }
            nuke = new Nuke(12, Integer.parseInt(args[0]), nukeSpawn, false);
        }
        if (nuke == null) {
            sender.sendMessage(CoreTools.getInstance().getPrefix() + ChatColor.RED + "There was an issue with your command!");
            sender.sendMessage(ChatColor.DARK_AQUA + "Syntax: /nuke [amount]");
            return false;
        }
        plugin.detonateNuke(nuke);
        sender.sendMessage(CoreTools.getInstance().getPrefix() + ChatColor.GREEN + "Nuke detonated!");
        return true;
    }
}
