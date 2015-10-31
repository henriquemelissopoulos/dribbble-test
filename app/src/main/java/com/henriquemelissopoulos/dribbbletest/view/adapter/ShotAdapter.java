package com.henriquemelissopoulos.dribbbletest.view.adapter;

import com.henriquemelissopoulos.dribbbletest.R;
import com.henriquemelissopoulos.dribbbletest.databinding.AdapterShotBinding;
import com.henriquemelissopoulos.dribbbletest.model.Shot;

import java.util.ArrayList;

/**
 * Created by h on 31/10/15.
 */
public class ShotAdapter extends SimpleAdapter<Shot, AdapterShotBinding> {


    public ShotAdapter(ArrayList<Shot> data) {
        super(data);
    }

    @Override
    public int layoutToInflate() {
        return R.layout.adapter_shot;
    }


    @Override
    protected void doOnBindViewHolder(SimpleAdapter.SimpleViewHolder holder, AdapterShotBinding binding, int position, Shot item) {
        binding.setShot(item);
    }
}
