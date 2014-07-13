package com.cvirn.mobileapp2.survey.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cvirn.mobileapp2.survey.R;
import com.cvirn.mobileapp2.survey.model.POI;

import java.util.List;

/**
 * Created by janko on 5/6/14.
 */
public class POIAdapter extends ArrayAdapter<POI> {

    private static final String TAG ="PoiAdapter" ;
    Context context;
    List<POI> poi_list;

    public POIAdapter(Context context, List<POI> points) {
        super(context, android.R.id.content, points);
        this.context = context;
        this.poi_list =points;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = vi.inflate(R.layout.list_extended_poi, null);


        POI m=poi_list.get(position);

        TextView tv = (TextView) view.findViewById(R.id.textPoiName);
        tv.setText(m.getName());

        TextView tv_details = (TextView) view.findViewById(R.id.textPoiDetails);
        tv_details.setText(m.getType()+" | "+m.getAddress()+" - "+m.getCity());








        return view;
    }
}
