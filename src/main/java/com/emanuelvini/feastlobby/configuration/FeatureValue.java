package com.emanuelvini.feastlobby.configuration;

import com.henryfabio.minecraft.configinjector.common.annotations.ConfigField;
import com.henryfabio.minecraft.configinjector.common.annotations.ConfigFile;
import com.henryfabio.minecraft.configinjector.common.annotations.ConfigSection;
import com.henryfabio.minecraft.configinjector.common.injector.ConfigurationInjectable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.function.Function;

@Getter
@Accessors(fluent = true)
@ConfigFile("configuration.yml")
@ConfigSection("features")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FeatureValue implements ConfigurationInjectable {

    @Getter
    private static final FeatureValue instance = new FeatureValue();

    //Configuração de proteção do Player

    @ConfigField("player.set_hp_to_min") private boolean setPlayerHpToMin;

    @ConfigField("player.disable_food") private boolean disablePlayerFood;

    @ConfigField("player.disable_damage") private boolean disablePlayerDamage;

    @ConfigField("player.disable_item drop") private boolean disablePlayerItemDrop;

    @ConfigField("player.clear_inventory") private boolean clearPlayerInventory;


    //Configuração de proteção do mundo

    @ConfigField("world.disable_weather") private boolean disableWorldWeather;

    @ConfigField("world.disable_grow") private boolean disableWorldGrow;

    @ConfigField("world.disable_block place and break") private boolean disableWorldPlaceAndBreak;

    @ConfigField("world.disable_entity_spawn") private boolean disableWorldEntitySpawn;

    @ConfigField("world.disable_pvp") private boolean disableWorldPvP;

    @ConfigField("world.disable_block_interact") private boolean disableWorldBlockInteract;

    @ConfigField("world.always_day") private boolean worldAlwaysDay;

    public static <T> T get(Function<FeatureValue, T> function) {
        return function.apply(instance);
    }

}
