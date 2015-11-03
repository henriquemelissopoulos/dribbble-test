package com.henriquemelissopoulos.dribbbletest.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.henriquemelissopoulos.dribbbletest.R;
import com.henriquemelissopoulos.dribbbletest.controller.Bus;
import com.henriquemelissopoulos.dribbbletest.controller.Config;
import com.henriquemelissopoulos.dribbbletest.controller.Utils;
import com.henriquemelissopoulos.dribbbletest.databinding.ActivityMainBinding;
import com.henriquemelissopoulos.dribbbletest.model.Shot;
import com.henriquemelissopoulos.dribbbletest.network.Service;
import com.henriquemelissopoulos.dribbbletest.view.adapter.RecyclerViewThresholdListener;
import com.henriquemelissopoulos.dribbbletest.view.adapter.ShotAdapter;

import de.greenrobot.event.EventBus;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Realm realm;
    private RealmResults<Shot> shots;
    private ShotAdapter shotAdapter;
    private RecyclerViewThresholdListener thresholdListener;


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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.rvShots.setLayoutManager(linearLayoutManager);
        binding.rvShots.setAdapter(shotAdapter);
        thresholdListener = new RecyclerViewThresholdListener(linearLayoutManager) {

            @Override public void onVisibleThreshold() {
                Service.getInstance().getPopularShots(Utils.pageToRequest(shots));
            }
        };
        binding.rvShots.addOnScrollListener(thresholdListener);

                binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        realm.beginTransaction();
                        realm.clear(Shot.class);
                        realm.commitTransaction();
                        thresholdListener.reset();
                        shotAdapter.notifyDataSetChanged();
                        Service.getInstance().getPopularShots(1);
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
        shots = realm.where(Shot.class).findAll();
    }


    public void init() {
        shotAdapter.addDataSet(shots);
    }


    public void onEventMainThread(Bus<Shot> bus) {

        shotAdapter.notifyDataSetChanged();

        if (bus.key == Config.BUS_GET_POPULAR_SHOTS) {

            if (binding.swipeRefresh.isRefreshing()) binding.swipeRefresh.setRefreshing(false);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_github:
                Uri uri = Uri.parse(getResources().getString(R.string.github_url));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null) realm.close();
        EventBus.getDefault().unregister(this);
    }
}
