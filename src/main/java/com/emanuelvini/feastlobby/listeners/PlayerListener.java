package com.emanuelvini.feastlobby.listeners;


import com.emanuelvini.feastcore.bukkit.api.common.ItemStackBuilder;
import com.emanuelvini.feastlobby.configuration.FeatureValue;
import com.emanuelvini.feastlobby.configuration.SelectorValue;
import com.emanuelvini.feastlobby.inventories.SelectorInventory;
import lombok.val;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {

    private ItemStack selectorItem;

    public PlayerListener() {
        val selectorItemBuilder = new ItemStackBuilder(Material.getMaterial(SelectorValue.get(SelectorValue::material)))
                .withName(SelectorValue.get(SelectorValue::itemName))
                .withLore(SelectorValue.get(SelectorValue::itemLore));
        if (SelectorValue.get(SelectorValue::enabledItemCustomSkull)) {
            selectorItem = selectorItemBuilder.toSkullBuilder().withTexture(SelectorValue.get(SelectorValue::itemSkullUrl)).buildSkull();
        } else {
            selectorItem = selectorItemBuilder.buildStack();
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (FeatureValue.get(FeatureValue::disablePlayerItemDrop)) e.setCancelled(true);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (FeatureValue.get(FeatureValue::setPlayerHpToMin)) {
            e.getPlayer().setMaxHealth(1);
            e.getPlayer().setHealth(0.5);
        }
        if (FeatureValue.get(FeatureValue::clearPlayerInventory)) {
            e.getPlayer().getInventory().clear();
            e.getPlayer().updateInventory();
        }
        e.getPlayer().getInventory().setItem(SelectorValue.get(SelectorValue::itemSlot), selectorItem);
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

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (FeatureValue.get(FeatureValue::disableWorldBlockInteract)) e.setCancelled(true);
        if (e.getItem().getItemMeta().getLore().equals(selectorItem.getItemMeta().getLore())) {
            SelectorInventory.open(e.getPlayer());
        }
    }

}
