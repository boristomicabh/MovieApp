package com.atlantbh.boristomic.movieapplication.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.atlantbh.boristomic.movieapplication.R;
import com.atlantbh.boristomic.movieapplication.holders.HorizontalListHolder;
import com.atlantbh.boristomic.movieapplication.models.rest.Cast;

import java.util.List;

/**
 * Created by boristomic on 03/02/16.
 */
public class HorizontalListAdapter extends RecyclerView.Adapter<HorizontalListHolder> {

    private List<Cast> casts;
    private int type;
    private Context context;

    public HorizontalListAdapter(List<Cast> casts, int type, Context context) {
        this.casts = casts;
        this.type = type;
        this.context = context;
    }

    @Override
    public HorizontalListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.actor_list_view, parent, false);
        return new HorizontalListHolder(view, context, type);
    }

    @Override
    public void onBindViewHolder(HorizontalListHolder holder, int position) {
        final Cast temp = casts.get(position);
        holder.bindCast(temp, type);
    }

    @Override
    public int getItemCount() {
        return casts.size();
    }
}
