package net.wagnerjr.googlettshassio.jsonAdapters;


import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import java.time.Instant;

public class InstantLongAdapter {
    public static final InstantLongAdapter ADAPTER = new InstantLongAdapter();

    @ToJson
    public Long toJson(Instant instant) {
        return instant.getEpochSecond();
    }

    @FromJson
    public Instant fromJson(Long value) {
        return Instant.ofEpochSecond(value);
    }
}
