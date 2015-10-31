package com.henriquemelissopoulos.dribbbletest.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import java.util.ArrayList;

public abstract class BaseAdapter<T> extends RecyclerView.Adapter {

    LayoutInflater inflater;
    ArrayList<T> data;


    public BaseAdapter(ArrayList<T> data) {
        this.data = data;
    }


    @Override
    public int getItemCount() {
        if (data == null) {
            return 0;
        }
        return data.size();
    }


    public T getItem(int position) {
        if (data == null) {
            return null;
        }
        return data.get(position);
    }


    public void addDataSet(ArrayList<T> data) {
        this.data = data;
        notifyDataSetChanged();
    }


    public void add(int position, T item) {
        data.add(position, item);
        notifyItemInserted(position);
    }


    public void remove(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }


    public void remove(T item) {
        data.remove(item);
        notifyItemRemoved(getItemPosition(item));
    }


    public int getItemPosition(T itemToFind) {
        return data.indexOf(itemToFind);
    }


    public void moveEntity(int fromPosition, int toPosition) {
        move(data, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }


    private void move(ArrayList<T> data, int a, int b) {
        T temp = data.remove(a);
        data.add(b, temp);
    }

}
