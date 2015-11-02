package com.henriquemelissopoulos.dribbbletest.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.henriquemelissopoulos.dribbbletest.R;
import com.henriquemelissopoulos.dribbbletest.controller.Bus;
import com.henriquemelissopoulos.dribbbletest.controller.Config;
import com.henriquemelissopoulos.dribbbletest.databinding.ActivityMainBinding;
import com.henriquemelissopoulos.dribbbletest.model.Shot;
import com.henriquemelissopoulos.dribbbletest.network.Service;
import com.henriquemelissopoulos.dribbbletest.view.adapter.ShotAdapter;

import de.greenrobot.event.EventBus;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Realm realm;
    private RealmResults<Shot> shots;
    private ShotAdapter shotAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        realm = Realm.getDefaultInstance();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setSupportActionBar(binding.toolbar);

        shotAdapter = new ShotAdapter(this, shots);
        binding.rvShots.setLayoutManager(new LinearLayoutManager(this));
        binding.rvShots.setAdapter(shotAdapter);
        binding.rvShots.setHasFixedSize(true);


        binding.toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Service.getInstance().getPopularShots(1);
                binding.setLoading(true);
            }
        });

        findPopularShots();
        Service.getInstance().getPopularShots(1);

        //shots not found on DB, set loading to inform user of shots request
        if (shots == null || shots.isEmpty()) {
            binding.setLoading(true);
        } else {
            init();
        }
    }


    public void findPopularShots() {
        shots = realm.where(Shot.class).findAllSorted(Shot.FIELD_LIKES_COUNT, false);
    }


    public void init() {
        shotAdapter.addDataSet(shots);
    }


    public void onEventMainThread(Bus<Shot> bus) {

        shotAdapter.notifyDataSetChanged();

        if (bus.key == Config.BUS_GET_POPULAR_SHOTS) {

            if (bus.error) {
                Toast.makeText(this, R.string.general_error_message, Toast.LENGTH_SHORT).show();
                return;
            }

            findPopularShots();
            if (shots == null || shots.isEmpty()) {
                Toast.makeText(this, R.string.general_error_message, Toast.LENGTH_SHORT).show();
            }

            binding.setLoading(false);
            init();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null) realm.close();
        EventBus.getDefault().unregister(this);
    }
}
