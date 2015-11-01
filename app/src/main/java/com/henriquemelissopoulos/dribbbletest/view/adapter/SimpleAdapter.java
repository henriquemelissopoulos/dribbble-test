package com.henriquemelissopoulos.dribbbletest.view.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by h on 22/09/15.
 */
public abstract class SimpleAdapter<T, K extends ViewDataBinding> extends BaseAdapter<T> {

    public SimpleAdapter(ArrayList<T> data) {
        super(data);
    }

    public abstract @LayoutRes int layoutToInflate();
    protected abstract void doOnBindViewHolder(SimpleViewHolder holder, K binding, int position, T item);


    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layoutToInflate(), parent, false);
        return new SimpleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, int position) {
        SimpleViewHolder holder = (SimpleViewHolder) h;
        doOnBindViewHolder(holder, holder.getBinding(), position, getItem(position));
        holder.getBinding().executePendingBindings();
    }


    public class SimpleViewHolder extends RecyclerView.ViewHolder {

        K binding;

        public SimpleViewHolder(View view) {
            super(view);
            binding = DataBindingUtil.bind(view);
        }

        public K getBinding() {
            return binding;
        }
    }
}
