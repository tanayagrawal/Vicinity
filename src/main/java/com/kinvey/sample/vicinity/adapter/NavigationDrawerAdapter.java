package com.kinvey.sample.vicinity.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kinvey.sample.vicinity.R;
import com.kinvey.sample.vicinity.model.NavDrawerItem;
import com.kinvey.sample.vicinity.util.SetTypeface;

import java.util.Collections;
import java.util.List;

/**
 * Created by Tanay Agrawal on 10/12/2015.
 */
public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {

    List<NavDrawerItem> drawerItems = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;

    public NavigationDrawerAdapter(Context context, List<NavDrawerItem> drawerItems) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.drawerItems = drawerItems;
    }

    public void delete(int position) {
        drawerItems.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(NavigationDrawerAdapter.MyViewHolder holder, int position) {
        NavDrawerItem current = drawerItems.get(position);
        holder.title.setText(current.getTitle());
        holder.image.setImageResource(current.getImage());
    }

    @Override
    public int getItemCount() {
        return drawerItems.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.navigation_drawer_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.navDrawerItem_title);
            image = (ImageView) itemView.findViewById(R.id.navDrawerItem_image);
            SetTypeface typeface = new SetTypeface(context);
            Typeface tf = typeface.getFont("regular");
            title.setTypeface(tf);
        }
    }
}
