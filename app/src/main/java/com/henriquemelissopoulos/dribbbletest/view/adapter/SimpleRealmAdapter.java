package com.henriquemelissopoulos.dribbbletest.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by h on 22/09/15.
 */
public abstract class SimpleRealmAdapter<T extends RealmObject, K extends ViewDataBinding> extends BaseRealmAdapter<T> {


    public SimpleRealmAdapter(Context context, RealmResults<T> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
    }

    public abstract int layoutToInflate();
    protected abstract void doOnBindViewHolder(ViewHolder holder, K binding, int position, T item);


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layoutToInflate(), parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, int position) {
        ViewHolder holder = (ViewHolder) h;
        doOnBindViewHolder(holder, holder.getBinding(), position, getItem(position));
        holder.getBinding().executePendingBindings();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        K binding;

        public ViewHolder(View view) {
            super(view);
            binding = DataBindingUtil.bind(view);
        }

        public K getBinding() {
            return binding;
        }
    }
}
