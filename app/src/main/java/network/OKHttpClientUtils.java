package network;

import java.io.File;

import okhttp3.Cache;

/**
 * Created by javiosyc on 2017/2/16.
 */

public class OKHttpClientUtils {
    private static final int cacheSize = 10 * 1024 * 1024;
    private static final String cacheFile ="cache";
    private OKHttpClientUtils() {
        Cache cache = new Cache(new File(cacheFile), cacheSize);
        OkHttpClient
    }

    public static OKHttpClientUtils getOkHttpClient() {
        return OKHttpClientHolder.instance;
    }

    private static class OKHttpClientHolder {
        private static OKHttpClientUtils instance = new OKHttpClientUtils();
    }
}
