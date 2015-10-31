package com.henriquemelissopoulos.dribbbletest.network;

import android.util.Log;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.henriquemelissopoulos.dribbbletest.BuildConfig;
import com.henriquemelissopoulos.dribbbletest.controller.Config;
import com.henriquemelissopoulos.dribbbletest.model.Shot;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.realm.RealmObject;
import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by h on 30/10/15.
 */
public class API {

    private static Retrofit REST_ADAPTER;


    public API() {

        if (REST_ADAPTER == null) {

            Gson gson = new GsonBuilder()
                    .setExclusionStrategies(new ExclusionStrategy() {
                        @Override
                        public boolean shouldSkipField(FieldAttributes f) {
                            return f.getDeclaringClass().equals(RealmObject.class);
                        }

                        @Override
                        public boolean shouldSkipClass(Class<?> clazz) {
                            return false;
                        }
                    })
                    .create();

            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(0, TimeUnit.MILLISECONDS);
            client.interceptors().add(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    Request request = original.newBuilder()
                            .header("Accept", "application/json")
                            .header("Accept-Language", "pt-BR")
                            .method(original.method(), original.body())
                            .build();

                    Response response = chain.proceed(request);

                    if (BuildConfig.DEBUG) {
                        String bodyString = response.body().string();
                        Log.d("api", String.format("Sending request %s with headers %s ", original.url(), original.headers()));
                        Log.d("api", String.format("Got response HTTP %s %s \n\n with body %s \n\n with headers %s ", response.code(), response.message(), bodyString, response.headers()));
                        response = response.newBuilder().body(ResponseBody.create(response.body().contentType(), bodyString)).build();
                    }

                    return response;
                }
            });


            REST_ADAPTER = new Retrofit.Builder()
                    .baseUrl(Config.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
    }

    public EndpointInterface service() {
        return REST_ADAPTER.create(EndpointInterface.class);
    }


    public interface EndpointInterface {

//        http://api.dribbble.com/v1/shots/?access_token=40316f72c3dd5bab2ca8dd32a22f3463fac4bf09be9781901fe965c090109be7&page=2
//        http://api.dribbble.com/v1/shots/2324177/?access_token=40316f72c3dd5bab2ca8dd32a22f3463fac4bf09be9781901fe965c090109be7

        //List of shots
        @GET("shots/")
        Call<ArrayList<Shot>> shots(
                @Query("access_token") String token,
                @Query("page") int page);


        //Details of a shot
        @GET("shots/{id}/")
        Call<Shot> shotDetail(
                @Path("id") int shotID,
                @Query("access_token") String token);
    }

}
