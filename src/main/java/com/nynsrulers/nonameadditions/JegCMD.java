package com.nynsrulers.nonameadditions;

import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;

import java.util.UUID;

public class JegCMD implements CommandExecutor {
    private NoNameAdditions plugin;
    public JegCMD(NoNameAdditions plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command label, String command, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CoreTools.getInstance().getPrefix() + ChatColor.RED + "Only players can use this command!");
            return false;
        }
        if (!sender.hasPermission("nna.jeg")) {
            sender.sendMessage(CoreTools.getInstance().getPrefix() + ChatColor.RED + "You don't have permission to do this!");
            return false;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            sendHelpMessage(sender);
            return false;
        }

        if (args[0].equalsIgnoreCase("summon")) {
            Entity lookedUp = plugin.getServer().getEntity(plugin.JegUUID);
            if (lookedUp != null) {
                sender.sendMessage(CoreTools.getInstance().getPrefix() + ChatColor.RED + "Jeg is already summoned! Despawn first!");
                sendHelpMessage(sender);
                return false;
            }
            Pig jeg = player.getWorld().spawn(player.getLocation(), Pig.class);
            jeg.setCustomName(ChatColor.translateAlternateColorCodes('&', "&8[&3OVERLORD&8] &dJeg"));
            jeg.setCustomNameVisible(true);
            jeg.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(2048.0);
            jeg.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(1000);
            jeg.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).setBaseValue(500);
            jeg.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(2.5);
            jeg.setHealth(2048.0);
            plugin.JegUUID = jeg.getUniqueId();
            plugin.JegPassive = true;
            sender.sendMessage(CoreTools.getInstance().getPrefix() + ChatColor.GREEN + "Spawned Jeg! (Mode: Passive)");
            return true;
        }
        if (args[0].equalsIgnoreCase("vanish")) {
            Entity lookedUp = plugin.getServer().getEntity(plugin.JegUUID);
            if (lookedUp == null) {
                sender.sendMessage(CoreTools.getInstance().getPrefix() + ChatColor.RED + "Jeg isn't summoned!");
                sendHelpMessage(sender);
                return false;
            }
            Pig jeg = (Pig) lookedUp;
            jeg.remove();
            plugin.JegUUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
            sender.sendMessage(CoreTools.getInstance().getPrefix() + ChatColor.GREEN + "Jeg has been successfully vanished! (Despawned)");
            return true;
        }
        if (args[0].equalsIgnoreCase("passive")) {
            if (args.length < 2) {
                sendHelpMessage(sender);
                return false;
            }
            Entity lookedUp = plugin.getServer().getEntity(plugin.JegUUID);
            if (lookedUp == null) {
                sender.sendMessage(CoreTools.getInstance().getPrefix() + ChatColor.RED + "Jeg isn't summoned!");
                sendHelpMessage(sender);
                return false;
            }
            if (args[1].equalsIgnoreCase("true")) {
                plugin.JegPassive = true;
                sender.sendMessage(CoreTools.getInstance().getPrefix() + ChatColor.GREEN + "Jeg has been set to Passive mode.");
                return true;
            } else if (args[1].equalsIgnoreCase("false")) {
                plugin.JegPassive = false;
                sender.sendMessage(CoreTools.getInstance().getPrefix() + ChatColor.GREEN + "Jeg has been set to Neutral mode.");
                return true;
            } else {
                sendHelpMessage(sender);
                return false;
            }
        }
        sendHelpMessage(sender);
        return false;
    }

    void sendHelpMessage(CommandSender sender) {
        sender.sendMessage(CoreTools.getInstance().getPrefix() + ChatColor.DARK_AQUA + "Jeg Command Help");
        sender.sendMessage(ChatColor.AQUA + "/jeg summon: Summons Jeg to your location.");
        sender.sendMessage(ChatColor.AQUA + "/jeg vanish: Makes Jeg vanish to the hidden realm (Despawning).");
        sender.sendMessage(ChatColor.AQUA + "/jeg passive (true|false): Makes Jeg either passive or neutral. In neutral, Jeg can kill players.");
    }
}
