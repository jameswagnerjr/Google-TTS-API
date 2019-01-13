package net.wagnerjr.hassioapi.configuration;

import com.google.auto.value.AutoValue;
import com.google.auto.value.extension.memoized.Memoized;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import okio.BufferedSource;

import java.io.IOException;

@AutoValue
public abstract class GoogleTextToSpeechConfiguration {

    protected static final Moshi MOSHI = new Moshi.Builder()
          .add(ConfigurationMoshiJsonAdapterFactory.FACTORY)
          .build();

    public static JsonAdapter<GoogleTextToSpeechConfiguration> jsonAdapter(Moshi moshi) {
        return new AutoValue_GoogleTextToSpeechConfiguration.MoshiJsonAdapter(moshi);
    }

    public static GoogleTextToSpeechConfiguration fromJsonString(String jsonString) throws IOException {
        return jsonAdapter(MOSHI).fromJson(jsonString);
    }

    public static GoogleTextToSpeechConfiguration fromJsonBufferedSource(BufferedSource jsonString) throws IOException {
        return jsonAdapter(MOSHI).fromJson(jsonString);
    }

    public static GoogleTextToSpeechConfiguration.Builder builder() {
        return new AutoValue_GoogleTextToSpeechConfiguration.Builder();
    }

    public static GoogleTextToSpeechConfiguration create(String newName, String newLanguageCode) {
        return builder()
              .setName(newName)
              .setLanguageCode(newLanguageCode)
              .build();
    }

    @Memoized
    public String toJsonString() {
        return jsonAdapter(MOSHI).toJson(this);
    }

    public abstract String getName();

    public abstract String getLanguageCode();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setName(String newName);

        public abstract Builder setLanguageCode(String newLanguageCode);

        public abstract GoogleTextToSpeechConfiguration build();
    }
}
