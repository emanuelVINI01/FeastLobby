package com.emanuelvini.feastlobby.configuration;

import com.henryfabio.minecraft.configinjector.common.annotations.ConfigField;
import com.henryfabio.minecraft.configinjector.common.annotations.ConfigFile;
import com.henryfabio.minecraft.configinjector.common.annotations.ConfigSection;
import com.henryfabio.minecraft.configinjector.common.annotations.TranslateColors;
import com.henryfabio.minecraft.configinjector.common.injector.ConfigurationInjectable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.function.Function;

@Getter
@Accessors(fluent = true)
@ConfigFile("configuration.yml")
@ConfigSection("error fixer")
@NoArgsConstructor(access = AccessLevel.PRIVATE)

public class ErrorFixerValue implements ConfigurationInjectable {

    @Getter private static final ErrorFixerValue instance = new ErrorFixerValue();

    @ConfigField("delete table") private boolean deleteTable;

    @ConfigField("debug") private boolean debug;

    public static <T> T get(Function<ErrorFixerValue, T> function) {
        return function.apply(instance);
    }



}
