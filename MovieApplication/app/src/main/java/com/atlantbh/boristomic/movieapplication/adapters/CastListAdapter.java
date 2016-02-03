package com.atlantbh.boristomic.movieapplication.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.atlantbh.boristomic.movieapplication.R;
import com.atlantbh.boristomic.movieapplication.holders.CastListHolder;
import com.atlantbh.boristomic.movieapplication.models.rest.Cast;

import java.util.List;

/**
 * Created by boristomic on 03/02/16.
 */
public class CastListAdapter extends RecyclerView.Adapter<CastListHolder> {

    private List<Cast> casts;
    private Context context;

    public CastListAdapter(List<Cast> casts, Context context) {
        this.casts = casts;
        this.context = context;
    }

    @Override
    public CastListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.actor_list_view, parent, false);
        return new CastListHolder(view, context);
    }

    @Override
    public void onBindViewHolder(CastListHolder holder, int position) {
        final Cast temp = casts.get(position);
        holder.bind(temp);
    }

    @Override
    public int getItemCount() {
        return casts.size();
    }
}
