package com.emanuelvini.feastlobby.listeners;


import com.emanuelvini.feastlobby.configuration.FeatureValue;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (FeatureValue.get(FeatureValue::setPlayerHpToMin)) {
            e.getPlayer().setMaxHealth(1);
            e.getPlayer().setHealth(0.5);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player && FeatureValue.get(FeatureValue::disablePlayerDamage)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodUpdate(FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player && FeatureValue.get(FeatureValue::disablePlayerFood)) {
            e.setCancelled(true);
        }
    }

}
