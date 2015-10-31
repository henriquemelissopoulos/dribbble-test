package com.henriquemelissopoulos.dribbbletest.network;

import android.util.Log;

import com.henriquemelissopoulos.dribbbletest.controller.Config;
import com.henriquemelissopoulos.dribbbletest.model.Shot;

import java.util.ArrayList;

import retrofit.Call;

/**
 * Created by h on 30/10/15.
 */
public class Service {

    private static Service instance;

    public static Service getInstance() {
        if (instance == null) {
            instance = new Service();
        }
        return instance;
    }


    public void getPopularShots(final int page) {

        Log.d("service", "getPopularShots start thread");

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Log.d("service", "start getPopularShots");
                    Call<ArrayList<Shot>> shotsCall = new API().service().shots(Config.ACCESS_TOKEN, page);
                    ArrayList<Shot> shots = shotsCall.execute().body();
                    Log.d("service", "start end of call");

                    for (Shot shot : shots) {
                        Log.d("shot", "id" + shot.getId());
                        Log.d("shot", "getTitle" + shot.getTitle());
                        Log.d("shot", "getViewCount" + shot.getViewCount());
                        Log.d("shot", "getUser id" + shot.getUser().getId());
                        Log.d("shot", "getUser name" + shot.getUser().getName());
                        Log.d("shot", "getImagesgetHidpi" + shot.getImages().getHidpi());
                        Log.d("shot", "getImagesgetNormal" + shot.getImages().getNormal());
                        Log.d("shot", "getImagesgetTeaser" + shot.getImages().getTeaser());
                        Log.d("shot", "- + \n");
                    }

                } catch (Exception e) {
                    Log.d("service", "Failure on getPopularShots");
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
