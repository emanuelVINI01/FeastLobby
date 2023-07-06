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

    @ConfigField("set hp to min") private boolean setHpToMin;

    public static <T> T get(Function<FeatureValue, T> function) {
        return function.apply(instance);
    }

}
