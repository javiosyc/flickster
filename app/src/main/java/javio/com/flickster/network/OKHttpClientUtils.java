package javio.com.flickster.network;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by javiosyc on 2017/2/16.
 */

public class OKHttpClientUtils {
    private static final int cacheSize = 10 * 1024 * 1024;
    private static final String cacheFile = "cache";
    private OkHttpClient okHttpClient;

    private OKHttpClientUtils() {
        Cache cache = new Cache(new File(cacheFile), cacheSize);
        okHttpClient = new OkHttpClient.Builder().cache(cache).build();
    }

    public static OKHttpClientUtils getInstance() {
        return OKHttpClientHolder.instance;
    }

    public void asyncCall(String url, OKHttpClientCallBack callBack) {
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(callBack);
    }

    private static class OKHttpClientHolder {
        private static OKHttpClientUtils instance = new OKHttpClientUtils();
    }

    public abstract class OKHttpClientCallBack implements Callback {
        private String url;

        @Override
        public void onFailure(Call call, IOException e) {
            Log.d("ERROR", "http get failed.., url =" + url, e);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {

            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            final String responseData = response.body().string();

            final JSONObject jsonObject;

            try {
                jsonObject = new JSONObject(responseData);
            } catch (JSONException e) {
                Log.d("ERROR", "parse responseData failed!" + responseData, e);
                throw new IOException(e);
            }

            processingJsonData(jsonObject);
        }

        protected abstract void processingJsonData(JSONObject jsonObject);

    }
}
