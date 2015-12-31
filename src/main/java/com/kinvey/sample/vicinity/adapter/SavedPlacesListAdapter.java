package com.kinvey.sample.vicinity.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kinvey.sample.vicinity.R;
import com.kinvey.sample.vicinity.object.Place;
import com.kinvey.sample.vicinity.util.SetTypeface;

import java.util.ArrayList;

/**
 * Created by tanayagrawal on 30/12/15.
 */
public class SavedPlacesListAdapter extends BaseAdapter {

    Context context;
    ArrayList<Place> savedPlaces;
    ViewHolder holder;
    private SparseBooleanArray mSelectedItemsIds;

    public SavedPlacesListAdapter(Context context, ArrayList<Place> savedPlaces) {

        mSelectedItemsIds = new SparseBooleanArray();
        this.savedPlaces = savedPlaces;
        this.context = context;

    }


    @Override
    public int getCount() {
        return savedPlaces.size();
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

        if (mSelectedItemsIds.get(position)) {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary_semi_transparent));
        }

        holder.placeName.setText(savedPlaces.get(position).getName());
        holder.placeDistance.setVisibility(View.GONE);
        holder.placeVicinity.setText("Vicinity: " + savedPlaces.get(position).getVicinity());

        convertView.setTag(holder);

        return convertView;
    }

    public void setTypeface() {
        SetTypeface typeface = new SetTypeface(context);
        Typeface tf = typeface.getFont("regular");
        holder.placeName.setTypeface(tf);

        Typeface tf1 = typeface.getFont("light");
        holder.placeVicinity.setTypeface(tf1);
    }

    static class ViewHolder {
        TextView placeName;
        TextView placeDistance;
        TextView placeVicinity;

    }

    public void remove(Place object) {
        savedPlaces.remove(object);
        notifyDataSetChanged();
    }

    // get List after update or delete
    public ArrayList<Place> getMyList() {
        return savedPlaces;
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    // Remove selection after unchecked
    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    // Item checked on selection
    public void selectView(int position, boolean value) {

        mSelectedItemsIds.put(position, value);

        notifyDataSetChanged();
    }

    // Get number of selected item
    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }
}
