package com.aelithron.nonameadditions;

import com.nexomc.nexo.api.NexoItems;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Pig;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PigZapEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

import java.util.Objects;
import java.util.UUID;

public final class NoNameAdditions extends JavaPlugin implements Listener {
    public boolean JegPassive = true;
    public UUID JegUUID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    @Override
    public void onEnable() {
        // Config
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        // Events
        getServer().getPluginManager().registerEvents(this, this);
        // Commands
        getCommand("jeg").setExecutor(new JegCMD(this));
        // Some extra staging
        CoreTools.getInstance().setPlugin(this);
        // Update checker
        CoreTools.getInstance().checkForUpdates();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (!NexoItems.exists(e.getItemInHand())) {
            return;
        }
        boolean fake = true; // TODO: implement 1% chance for real
        if (Objects.equals(NexoItems.idFromItem(e.getItemInHand()), "small_fake_nuke")) {
            e.setCancelled(true);
            detonateNuke(new Nuke(1, 10, e.getBlock().getLocation(), fake));
            ItemStack item = e.getItemInHand();
            item.setAmount(item.getAmount() - 1);
            e.getPlayer().getInventory().setItemInMainHand(item);
            return;
        }
        if (Objects.equals(NexoItems.idFromItem(e.getItemInHand()), "medium_fake_nuke")) {
            e.setCancelled(true);
            detonateNuke(new Nuke(3, 20, e.getBlock().getLocation(), fake));
            ItemStack item = e.getItemInHand();
            item.setAmount(item.getAmount() - 1);
            e.getPlayer().getInventory().setItemInMainHand(item);
            return;
        }
        if (Objects.equals(NexoItems.idFromItem(e.getItemInHand()), "large_fake_nuke")) {
            e.setCancelled(true);
            detonateNuke(new Nuke(5, 30, e.getBlock().getLocation(), fake));
            ItemStack item = e.getItemInHand();
            item.setAmount(item.getAmount() - 1);
            e.getPlayer().getInventory().setItemInMainHand(item);
            return;
        }
    }

    public void detonateNuke(Nuke nuke) {
        assert nuke.getLocation().getWorld() != null;
        nuke.getLocation().getWorld().strikeLightning(nuke.getLocation());
        for (int i = 0; i < nuke.getAmount(); i++) {
            Location spreadLocation = nuke.getLocation().clone().add(
                Math.random() * 10 - 5,
                10,
                Math.random() * 10 - 5
            );
            TNTPrimed nukeTNT = spreadLocation.getWorld().spawn(spreadLocation, TNTPrimed.class);
            nukeTNT.setFuseTicks(40);
            nukeTNT.setYield(nuke.getStrength() * 4);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) { return; }
        if (e.getItem() == null || !NexoItems.exists(e.getItem())) { return; }
        if (Objects.equals(NexoItems.idFromItem(e.getItem()), "edible_magma_cream")) {
            e.setCancelled(true);
            ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
            item.setAmount(e.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
            e.getPlayer().getInventory().setItemInMainHand(item);

            e.getPlayer().setFoodLevel(e.getPlayer().getFoodLevel() + 3);
            e.getPlayer().setSaturation(e.getPlayer().getSaturation() + 0.3F);
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 200, 0, true, true));
            e.getPlayer().setFireTicks(195);
            // Evaporate water
            Location loc = e.getPlayer().getLocation();
            for (int x = -2; x <= 2; x++) {
                for (int y = -2; y <= 2; y++) {
                    for (int z = -2; z <= 2; z++) {
                        Block block = loc.getWorld().getBlockAt(loc.getBlockX() + x, loc.getBlockY() + y, loc.getBlockZ() + z);
                        if (block.getType() == Material.WATER) {
                            block.setType(Material.AIR);
                            block.getWorld().spawnParticle(Particle.SMOKE, block.getLocation(), 10);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityHit(EntityDamageByEntityEvent e) {
        if (e.getEntity().getUniqueId() != JegUUID) {
            return;
        }
        if (JegPassive) {
            return;
        }
        Pig jeg = (Pig) e.getEntity();
        if (e.getDamager() instanceof Damageable) {
            e.getDamager().getWorld().strikeLightning(e.getDamager().getLocation());
            ((Damageable) e.getDamager()).damage(80, jeg);
        }
    }

    @EventHandler
    public void onPigZap(PigZapEvent e) {
        if (e.getEntity().getUniqueId() == JegUUID) {
            e.setCancelled(true);
        }
    }
}
