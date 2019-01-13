package net.wagnerjr.googlettshassio.jsonAdapters;

import com.google.protobuf.ByteString;
import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

public class ByteStringAdapter {
    public static final ByteStringAdapter ADAPTER = new ByteStringAdapter();

    @FromJson
    public ByteString fromJson(String s) {
        return ByteString.copyFromUtf8(s);
    }

    @ToJson
    public String toJson(ByteString value) {
        return value.toStringUtf8();
    }
}
