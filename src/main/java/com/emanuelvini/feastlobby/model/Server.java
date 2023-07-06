package com.emanuelvini.feastlobby.model;

import lombok.Builder;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Builder
@Getter
@Setter
public class Server {

    private String id;

    private String name;

    private String bungee;

    private String address;

    private ItemStack item;

    private boolean maintenance;


}
