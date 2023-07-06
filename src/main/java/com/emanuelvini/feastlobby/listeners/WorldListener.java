package com.emanuelvini.feastlobby.listeners;

import com.emanuelvini.feastlobby.configuration.FeatureValue;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WorldListener implements Listener {

    @EventHandler
    public void onWeather(WeatherChangeEvent e) {
        if (FeatureValue.get(FeatureValue::disableWorldWeather)) e.setCancelled(true);
        e.getWorld().setWeatherDuration(0);
    }

    @EventHandler
    public void onGrow(BlockGrowEvent e) {
        if (FeatureValue.get(FeatureValue::disableWorldGrow)) e.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (FeatureValue.get(FeatureValue::disableWorldPlaceAndBreak) && !e.getPlayer().hasPermission("feastlobby.admin.bypass")) e.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (FeatureValue.get(FeatureValue::disableWorldPlaceAndBreak) && !e.getPlayer().hasPermission("feastlobby.admin.bypass")) e.setCancelled(true);
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent e) {
        if (FeatureValue.get(FeatureValue::disableWorldEntitySpawn) && !(e.getEntityType() == EntityType.PLAYER)) e.setCancelled(true);
    }

    @EventHandler
    public void onPvP(EntityDamageByEntityEvent e) {
        if (FeatureValue.get(FeatureValue::disableWorldPvP)) e.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (FeatureValue.get(FeatureValue::disableWorldInteract)) e.setCancelled(true);
    }
}
