package net.wagnerjr.hassioapi.configuration;

import net.wagnerjr.hassioapi.configuration.AutoValueMoshi_ConfigurationMoshiJsonAdapterFactory;

import com.ryanharter.auto.value.moshi.MoshiAdapterFactory;
import com.squareup.moshi.JsonAdapter;

@MoshiAdapterFactory
public abstract class ConfigurationMoshiJsonAdapterFactory implements JsonAdapter.Factory {
    public static final JsonAdapter.Factory FACTORY = create();

    // Static factory method to access the package
    // private generated implementation
    public static JsonAdapter.Factory create() {
        return new AutoValueMoshi_ConfigurationMoshiJsonAdapterFactory();
    }

}