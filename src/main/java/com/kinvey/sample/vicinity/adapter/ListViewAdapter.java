package com.kinvey.sample.vicinity.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.kinvey.sample.vicinity.R;
import com.kinvey.sample.vicinity.util.SetTypeface;

import java.util.ArrayList;


/**
 * Created by tanayagrawal on 16/11/15.
 */
public class ListViewAdapter extends BaseAdapter {


    ArrayList<String> placeNames;
    ArrayList<String> placeDistances;
    ArrayList<String> vicinity;
    Context context;
    ViewHolder holder;


    public ListViewAdapter(Context context, ArrayList<String> placeNames,
                           ArrayList<String> placeDistances, ArrayList<String> vicinity) {
        this.placeNames = placeNames;
        this.placeDistances = placeDistances;
        this.vicinity = vicinity;
        this.context = context;

    }

    @Override
    public int getCount() {
        return placeNames.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, null);

        }

        holder = new ViewHolder();

        holder.placeName = (TextView) convertView.findViewById(R.id.place_name);
        holder.placeDistance = (TextView) convertView.findViewById(R.id.place_distance);
        holder.placeVicinity = (TextView) convertView.findViewById(R.id.place_vicinity);
        setTypeface();
        holder.placeName.setText(placeNames.get(position));
        holder.placeDistance.setText("Distance: "+ placeDistances.get(position) + "  KM");
        holder.placeVicinity.setText("Vicinity: " + vicinity.get(position));
        convertView.setTag(holder);
        return convertView;
    }


    public void setTypeface() {
        SetTypeface typeface = new SetTypeface(context);
        Typeface tf = typeface.getFont("regular");
        holder.placeName.setTypeface(tf);

        Typeface tf1 = typeface.getFont("light");
        holder.placeDistance.setTypeface(tf1);
        holder.placeVicinity.setTypeface(tf1);
    }

    static class ViewHolder{
        TextView placeName;
        TextView placeDistance;
        TextView placeVicinity;

    }
}
