package com.henriquemelissopoulos.dribbbletest.view.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.henriquemelissopoulos.dribbbletest.R;
import com.henriquemelissopoulos.dribbbletest.controller.Bus;
import com.henriquemelissopoulos.dribbbletest.controller.Utils;
import com.henriquemelissopoulos.dribbbletest.databinding.ActivityShotDetailBinding;
import com.henriquemelissopoulos.dribbbletest.model.Shot;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by h on 01/11/15.
 */
public class ShotDetailActivity extends AppCompatActivity {

    ActivityShotDetailBinding binding;
    int flexibleSpaceHeight, statusBarHeight, baseColor, statusBarColor;

    TextView tvToolbarTitle;


    public static Intent startIntent(Context context) {
        return new Intent(context, ShotDetailActivity.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        //Draw over status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(getResources().getColor(R.color.transparent));
        }


        flexibleSpaceHeight = getResources().getDimensionPixelSize(R.dimen.shot_height);
        statusBarHeight = Utils.getStatusBarHeight(this);
        baseColor = getResources().getColor(R.color.colorPrimary);
        statusBarColor = getResources().getColor(R.color.black);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_shot_detail);

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvToolbarTitle = Utils.getToolbartvTitle(binding.toolbar);


        binding.obScrollView.setScrollViewCallbacks(new ObservableScrollViewCallbacks() {
            @Override
            public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
                updateScroll(scrollY);
            }

            @Override
            public void onDownMotionEvent() {
            }

            @Override
            public void onUpOrCancelMotionEvent(ScrollState scrollState) {
            }
        });


        init();
    }


    public void init() {

        ScrollUtils.addOnGlobalLayoutListener(binding.rlRoot, new Runnable() {
            @Override
            public void run() {
                updateScroll(binding.obScrollView.getCurrentScrollY());
            }
        });

        binding.tvDescription.setMovementMethod(LinkMovementMethod.getInstance());
    }


    private void updateScroll(final int scrollY) {

        float toolbarYPlusHeight = (binding.toolbar.getY() + binding.toolbar.getHeight());
        float alpha = Math.min(1, (float) scrollY / (flexibleSpaceHeight - binding.rlInfo.getHeight() - statusBarHeight));


        binding.ivShot.setTranslationY(-scrollY / 2);
        binding.rlInfo.setTranslationY(-scrollY);

        binding.overlay.setTranslationY(-scrollY);
        if ((binding.overlay.getY() + binding.overlay.getHeight()) < toolbarYPlusHeight) {
            binding.overlay.setY(toolbarYPlusHeight - binding.overlay.getHeight());
        }

        binding.overlay.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, baseColor));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ScrollUtils.getColorWithAlpha(alpha / 4, statusBarColor));
        }
        tvToolbarTitle.setAlpha((float) scrollY / (flexibleSpaceHeight / 3));
        binding.rlInfo.setAlpha(1 - ((float) scrollY / (flexibleSpaceHeight / 3)));
    }


    public void onEventMainThread(Bus<ArrayList<Shot>> bus) {

//        if (bus.key == Config.BUS_GET_SHOTS) {
//
//            if (bus.error) {
//                Toast.makeText(this, R.string.general_error_message + bus.info, Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            shots = bus.data;
//
//            binding.setLoading(false);
//            init();
//        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
