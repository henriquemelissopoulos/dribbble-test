package com.henriquemelissopoulos.dribbbletest.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.henriquemelissopoulos.dribbbletest.R;
import com.henriquemelissopoulos.dribbbletest.controller.Bus;
import com.henriquemelissopoulos.dribbbletest.controller.Config;
import com.henriquemelissopoulos.dribbbletest.controller.GlideCircleTransform;
import com.henriquemelissopoulos.dribbbletest.controller.Utils;
import com.henriquemelissopoulos.dribbbletest.databinding.ActivityShotDetailBinding;
import com.henriquemelissopoulos.dribbbletest.model.Shot;
import com.henriquemelissopoulos.dribbbletest.network.Service;

import de.greenrobot.event.EventBus;
import io.realm.Realm;

/**
 * Created by h on 01/11/15.
 */
public class ShotDetailActivity extends AppCompatActivity {

    private ActivityShotDetailBinding binding;
    private Realm realm;
    private int flexibleSpaceHeight, statusBarHeight, baseColor, statusBarColor;
    private int shotID;
    private Shot shot;
    private TextView tvToolbarTitle;


    public static final String SHOT_ID = "shotID";
    public static Intent startIntent(Context context, int shotID) {
        Intent intent = new Intent(context, ShotDetailActivity.class);
        intent.putExtra(SHOT_ID, shotID);
        return intent;
    }

    public static final String SHOT_IMAGE = "shotImage";
    public static final String SHOT_INFO = "shotInfo";
    public static void startWithTransition(Activity activity, int shotID, View ivShot, View rlInfo) {
        Pair<View, String> shotImage = new Pair<>(ivShot, SHOT_IMAGE);
        Pair<View, String> shotInfo = new Pair<>(rlInfo, SHOT_INFO);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, shotImage, shotInfo);
        ActivityCompat.startActivity(activity, startIntent(activity, shotID), options.toBundle());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_shot_detail);

        ViewCompat.setTransitionName(binding.ivShot, SHOT_IMAGE);
        ViewCompat.setTransitionName(binding.rlInfo, SHOT_INFO);

        shotID = getIntent().getIntExtra(SHOT_ID, -1);
        if (shotID < 0) {
            invalidShot();
            return;
        }

        if(!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        realm = Realm.getDefaultInstance();

        //Draw over status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(getResources().getColor(R.color.transparent));
        }


        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvToolbarTitle = Utils.getToolbartvTitle(binding.toolbar); //to accesss toolbar title textview


        //Configure scroll animation
        flexibleSpaceHeight = getResources().getDimensionPixelSize(R.dimen.shot_height);
        statusBarHeight = Utils.getStatusBarHeight(this);
        baseColor = getResources().getColor(R.color.colorPrimary);
        statusBarColor = getResources().getColor(R.color.black);

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

        findShot();
        Service.getInstance().getShotDetail(shotID);

        //shot not found on DB, set loading to inform user of shot request
        if (shot == null) {
            binding.setLoading(true);
        } else {
            init();
        }
    }


    public void findShot() {
        shot = realm.where(Shot.class).equalTo(Shot.FIELD_SHOT_ID, shotID).findFirst();
    }


    public void invalidShot() {
        finish();
        Toast.makeText(this, R.string.general_error_message, Toast.LENGTH_SHORT).show();
    }


    public void init() {

        binding.setShot(shot);
        
        Glide.with(this)
                .load(shot.getImages().getHidpi())
                .thumbnail(Glide.with(this).load(shot.getImages().getNormal()).centerCrop().dontAnimate())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .dontAnimate()
                .centerCrop()
                .into(binding.ivShot);

        Glide.with(this)
                .load(shot.getUser().getAvatar())
                .centerCrop()
                .transform(new GlideCircleTransform(this))
                .into(binding.ivAvatar);

        ScrollUtils.addOnGlobalLayoutListener(binding.rlRoot, new Runnable() {
            @Override
            public void run() {
                updateScroll(binding.obScrollView.getCurrentScrollY());
            }
        });

        //HTML requirement
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


    public void onEventMainThread(Bus<Shot> bus) {
        if (bus.key == Config.BUS_GET_SHOT_DETAIL) {

            if (bus.error) {
                Toast.makeText(this, R.string.general_error_message, Toast.LENGTH_SHORT).show();
                binding.setLoading(false);
            }

            findShot();
            if (shot == null) {
                invalidShot();
                return;
            }

            binding.setLoading(false);
            init();
        }
    }


    public void share() {
        if (shot != null) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TITLE, shot.getTitle());
            String message = shot.getUser().getName() + " - " + shot.getTitle() + "\n\n" + shot.getUrl();
            intent.putExtra(Intent.EXTRA_TEXT, message);

            startActivity(Intent.createChooser(intent, getString(R.string.activity_shot_detail_share_dialog)));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_shot_detail, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.action_share:
                share();
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
