package com.henriquemelissopoulos.dribbbletest.view.adapter;

import android.app.Activity;
import android.view.View;

import com.bumptech.glide.Glide;
import com.henriquemelissopoulos.dribbbletest.R;
import com.henriquemelissopoulos.dribbbletest.databinding.AdapterShotBinding;
import com.henriquemelissopoulos.dribbbletest.model.Shot;
import com.henriquemelissopoulos.dribbbletest.view.activity.ShotDetailActivity;

import io.realm.RealmResults;

/**
 * Created by h on 31/10/15.
 */
public class ShotAdapter extends SimpleRealmAdapter<Shot, AdapterShotBinding> {

    Activity activity;

    public ShotAdapter(Activity activity, RealmResults<Shot> data) {
        super(activity, data, false);
        this.activity = activity;
    }

    @Override
    public int layoutToInflate() {
        return R.layout.adapter_shot;
    }


    @Override
    protected void doOnBindViewHolder(SimpleRealmAdapter.ViewHolder holder, final AdapterShotBinding binding, int position, final Shot shot) {
        binding.setShot(shot);

        binding.rlRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShotDetailActivity.startWithTransition(activity, shot.getId(), binding.ivShot, binding.rlInfo);
            }
        });

        Glide.with(activity)
                .load(shot.getImages().getNormal())
                .thumbnail(Glide.with(activity).load(shot.getImages().getTeaser()).centerCrop())
                .dontAnimate()
                .centerCrop()
                .into(binding.ivShot);
    }
}
