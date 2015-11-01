package com.henriquemelissopoulos.dribbbletest.network;

import android.util.Log;

import com.henriquemelissopoulos.dribbbletest.controller.Bus;
import com.henriquemelissopoulos.dribbbletest.controller.Config;
import com.henriquemelissopoulos.dribbbletest.model.Shot;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
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
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Call<ArrayList<Shot>> shotsCall = new API().service().shots(Config.ACCESS_TOKEN, page);
                    ArrayList<Shot> shots = shotsCall.execute().body();

//                    for (Shot shot : shots) {
//                        Log.d("shot", "id" + shot.getId());
//                        Log.d("shot", "getTitle" + shot.getTitle());
//                        Log.d("shot", "getViewCount" + shot.getViewCount());
//                        Log.d("shot", "getUser id" + shot.getUser().getId());
//                        Log.d("shot", "getUser name" + shot.getUser().getName());
//                        Log.d("shot", "getImagesgetHidpi" + shot.getImages().getHidpi());
//                        Log.d("shot", "getImagesgetNormal" + shot.getImages().getNormal());
//                        Log.d("shot", "getImagesgetTeaser" + shot.getImages().getTeaser());
//                        Log.d("shot", "- + \n");
//                    }

                    EventBus.getDefault().post(new Bus<ArrayList<Shot>>(Config.BUS_GET_POPULAR_SHOTS).data(shots));

                } catch (Exception e) {
                    EventBus.getDefault().post(new Bus<ArrayList<Shot>>(Config.BUS_GET_POPULAR_SHOTS).error(true).info(e.toString()));
                    Log.d("service", "Failure on getPopularShots");
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public void getShotDetail(final int shotID) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Call<Shot> shotDetailCall = new API().service().shotDetail(shotID, Config.ACCESS_TOKEN);
                    Shot shot = shotDetailCall.execute().body();

                    Log.d("shot", "id" + shot.getId());
                    Log.d("shot", "getTitle" + shot.getTitle());
                    Log.d("shot", "getViewCount" + shot.getViewCount());
                    Log.d("shot", "getUser id" + shot.getUser().getId());
                    Log.d("shot", "getUser name" + shot.getUser().getName());
                    Log.d("shot", "getImagesgetHidpi" + shot.getImages().getHidpi());
                    Log.d("shot", "getImagesgetNormal" + shot.getImages().getNormal());
                    Log.d("shot", "getImagesgetTeaser" + shot.getImages().getTeaser());

                    EventBus.getDefault().post(new Bus<Shot>(Config.BUS_GET_SHOT_DETAIL).data(shot));

                } catch (Exception e) {
                    EventBus.getDefault().post(new Bus<Shot>(Config.BUS_GET_SHOT_DETAIL).error(true).info(e.toString()));
                    Log.d("service", "Failure on getShotDetail");
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
