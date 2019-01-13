package net.wagnerjr.googlettshassio.jsonAdapters;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

public class BooleanStringAdapter {
    public static final BooleanStringAdapter ADAPTER = new BooleanStringAdapter();

    @FromJson
    public Boolean fromJson(String s) {
        return Boolean.parseBoolean(s);
    }

    @ToJson
    public String toJson(Boolean value) {
        return value.toString();
    }
}
