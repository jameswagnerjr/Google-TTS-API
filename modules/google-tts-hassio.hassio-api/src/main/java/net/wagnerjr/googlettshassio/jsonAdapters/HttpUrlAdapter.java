package net.wagnerjr.googlettshassio.jsonAdapters;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;
import okhttp3.HttpUrl;

public class HttpUrlAdapter {
    public static final HttpUrlAdapter ADAPTER = new HttpUrlAdapter();

    @FromJson
    public HttpUrl fromJson(String url) {
        return HttpUrl.parse(url);
    }

    @ToJson
    public String toJson(HttpUrl url) {
        return url.toString();
    }
}
