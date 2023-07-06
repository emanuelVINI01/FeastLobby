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

    @ConfigField("player.set hp to min") private boolean setPlayerHpToMin;

    @ConfigField("player.disable food") private boolean disablePlayerFood;

    @ConfigField("player.disable damage") private boolean disablePlayerDamage;

    @ConfigField("player.disable item drop") private boolean disablePlayerItemDrop;

    @ConfigField("player.clear inventory") private boolean clearPlayerInventory;


    //Configuração de proteção do mundo

    @ConfigField("world.disable weather") private boolean disableWorldWeather;

    @ConfigField("world.disable grow") private boolean disableWorldGrow;

    @ConfigField("world.disable block place and break") private boolean disableWorldPlaceAndBreak;

    @ConfigField("world.disable entity spawn") private boolean disableWorldEntitySpawn;

    @ConfigField("world.disable pvp") private boolean disableWorldPvP;

    @ConfigField("world.disable block interact") private boolean disableWorldBlockInteract;

    public static <T> T get(Function<FeatureValue, T> function) {
        return function.apply(instance);
    }

}
