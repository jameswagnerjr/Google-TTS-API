package net.wagnerjr.googlettshassio.jsonAdapters;

import com.ryanharter.auto.value.moshi.MoshiAdapterFactory;
import com.squareup.moshi.JsonAdapter;

@MoshiAdapterFactory
public abstract class BaseObjectsMoshiJsonAdapterFactory implements JsonAdapter.Factory {
    public static final JsonAdapter.Factory FACTORY = create();

    // Static factory method to access the package
    // private generated implementation
    public static JsonAdapter.Factory create() {
        return new AutoValueMoshi_BaseObjectsMoshiJsonAdapterFactory();
    }

}