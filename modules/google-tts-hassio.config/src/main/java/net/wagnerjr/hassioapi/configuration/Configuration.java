package net.wagnerjr.hassioapi.configuration;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import okio.BufferedSource;

import java.io.IOException;

@AutoValue
public abstract class Configuration {
    protected static final Moshi MOSHI = new Moshi.Builder()
          .add(ConfigurationMoshiJsonAdapterFactory.FACTORY)
          .build();

    public static JsonAdapter<Configuration> jsonAdapter(Moshi moshi) {
        return new AutoValue_Configuration.MoshiJsonAdapter(moshi);
    }

    public static Configuration fromJsonString(String jsonString) throws IOException {
        return jsonAdapter(MOSHI).fromJson(jsonString);
    }

    public static Configuration fromJsonBufferedSource(BufferedSource jsonString) throws IOException {
        return jsonAdapter(MOSHI).fromJson(jsonString);
    }

    @Memoized
    public String toJsonString() {
        return jsonAdapter(MOSHI).toJson(this);
    }

    @Json(name = "database")
    public abstract DatabaseConfiguration getDatabaseConfiguration();

    @Json(name = "googleTextToSpeech")
    public abstract GoogleTextToSpeechConfiguration getGoogleTextToSpeechConfiguration();
}
