package com.henriquemelissopoulos.dribbbletest.network;

import android.util.Log;

import com.henriquemelissopoulos.dribbbletest.controller.Bus;
import com.henriquemelissopoulos.dribbbletest.controller.Config;
import com.henriquemelissopoulos.dribbbletest.model.Shot;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import io.realm.Realm;
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

                Realm realm = null;

                try {
                    Call<ArrayList<Shot>> shotsCall = new API().service().shots(Config.ACCESS_TOKEN, page, Config.SHOTS_PER_PAGE);
                    ArrayList<Shot> shots = shotsCall.execute().body();

                    realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(shots);
                    realm.commitTransaction();

                    EventBus.getDefault().post(new Bus<Shot>(Config.BUS_GET_POPULAR_SHOTS));

                } catch (Exception e) {
                    EventBus.getDefault().post(new Bus<Shot>(Config.BUS_GET_POPULAR_SHOTS).error(true).info(e.toString()));
                    Log.d("service", "Failure on getPopularShots");
                    e.printStackTrace();
                } finally {
                    if (realm != null) realm.close();
                }
            }
        }).start();
    }


    public void getShotDetail(final int shotID) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                Realm realm = null;

                try {
                    Call<Shot> shotDetailCall = new API().service().shotDetail(shotID, Config.ACCESS_TOKEN);
                    Shot shot = shotDetailCall.execute().body();

                    realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(shot);
                    realm.commitTransaction();

                    EventBus.getDefault().post(new Bus<Shot>(Config.BUS_GET_SHOT_DETAIL));

                } catch (Exception e) {
                    EventBus.getDefault().post(new Bus<Shot>(Config.BUS_GET_SHOT_DETAIL).error(true).info(e.toString()));
                    Log.d("service", "Failure on getShotDetail");
                    e.printStackTrace();
                } finally {
                    if (realm != null) realm.close();
                }
            }
        }).start();
    }
}
