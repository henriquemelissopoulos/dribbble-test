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

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    ArrayList<Shot> shots = new ArrayList<>();
    ShotAdapter shotAdapter = new ShotAdapter(shots);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setSupportActionBar(binding.toolbar);

        binding.toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Service.getInstance().getPopularShots(1);
                binding.setLoading(true);
            }
        });

        Service.getInstance().getPopularShots(1);
        binding.setLoading(true);

        binding.rvShots.setLayoutManager(new LinearLayoutManager(this));
        binding.rvShots.setAdapter(shotAdapter);
        binding.rvShots.setHasFixedSize(true);
    }

    public void init() {
        shotAdapter.addDataSet(shots);
    }


    public void onEventMainThread(Bus<ArrayList<Shot>> bus) {

        if (bus.key == Config.BUS_GET_SHOTS) {

            if (bus.error) {
                Toast.makeText(this, "Ocorreu um erro durante a requisição." + bus.info, Toast.LENGTH_SHORT).show();
                return;
            }

            shots = bus.data;

            binding.setLoading(false);
            init();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
