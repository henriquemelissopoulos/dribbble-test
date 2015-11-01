package com.henriquemelissopoulos.dribbbletest.view.adapter;

import android.content.Context;
import android.view.View;

import com.bumptech.glide.Glide;
import com.henriquemelissopoulos.dribbbletest.R;
import com.henriquemelissopoulos.dribbbletest.databinding.AdapterShotBinding;
import com.henriquemelissopoulos.dribbbletest.model.Shot;
import com.henriquemelissopoulos.dribbbletest.view.activity.ShotDetailActivity;

import java.util.ArrayList;

/**
 * Created by h on 31/10/15.
 */
public class ShotAdapter extends SimpleAdapter<Shot, AdapterShotBinding> {

    Context context;

    public ShotAdapter(ArrayList<Shot> data, Context context) {
        super(data);
        this.context = context;
    }

    @Override
    public int layoutToInflate() {
        return R.layout.adapter_shot;
    }


    @Override
    protected void doOnBindViewHolder(SimpleAdapter.SimpleViewHolder holder, AdapterShotBinding binding, int position, Shot shot) {
        binding.setShot(shot);

        binding.rlRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(ShotDetailActivity.startIntent(context));
            }
        });

        Glide.with(context)
                .load(shot.getImages().getNormal())
                .thumbnail(Glide.with(context).load(shot.getImages().getTeaser()).centerCrop())
                .centerCrop()
                .into(binding.ivShot);
    }
}
