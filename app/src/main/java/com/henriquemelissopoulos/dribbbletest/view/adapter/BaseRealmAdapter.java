package com.henriquemelissopoulos.dribbbletest.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmObject;
import io.realm.RealmResults;

public abstract class BaseRealmAdapter<T extends RealmObject> extends RecyclerView.Adapter {

    LayoutInflater inflater;
    RealmResults<T> realmResults;
    Context context;
    final RealmChangeListener listener;


    public BaseRealmAdapter(Context context, RealmResults<T> realmResults, boolean automaticUpdate) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }
        this.context = context;
        this.realmResults = realmResults;
        this.inflater = LayoutInflater.from(context);
        this.listener = (!automaticUpdate) ? null : new RealmChangeListener() {
            @Override
            public void onChange() {
                notifyDataSetChanged();
            }
        };
    }


    @Override
    public int getItemCount() {
        if (realmResults == null) {
            return 0;
        }
        return realmResults.size();
    }


    public T getItem(int position) {
        if (realmResults == null) {
            return null;
        }
        return realmResults.get(position);
    }


    public void updateRealmResults(RealmResults<T> queryResults) {
        if (listener != null) {
            // Making sure that Adapter is refreshed correctly if new RealmResults come from another Realm
            if (this.realmResults != null) {
                Realm.getDefaultInstance().removeChangeListener(listener);
            }
            if (queryResults != null) {
                Realm.getDefaultInstance().addChangeListener(listener);
            }
        }

        this.realmResults = queryResults;
        notifyDataSetChanged();
    }


    public void addDataSet(RealmResults<T> realmResults) {
        this.realmResults = realmResults;
        notifyDataSetChanged();
    }


    @Deprecated
    public void add(int position, T item) {
        realmResults.add(position, item);
        notifyItemInserted(position);
    }


    public int getItemPosition(T itemToFind) {
        for (int i = 0; i < realmResults.size(); ++i) {
            T item = realmResults.get(i);
            if (itemToFind.equals(item)) {
                return i;
            }
        }

        return -1;
    }


    public void moveEntity(int fromPosition, int toPosition) {
        move(realmResults, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }


    private void move(List<T> data, int a, int b) {
        T temp = data.remove(a);
        data.add(b, temp);
    }

}
