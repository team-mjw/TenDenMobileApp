package com.teamMJW.tenden;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ModeListAdapter extends ArrayAdapter<Mode> {

    private Context context;
    private int mResource;

    public ModeListAdapter(Context context, int resource, ArrayList<Mode> objects) {
        super(context, resource, objects);
        this.context = context;
        this.mResource = resource;
    }

    static class ViewHolder {
        public TextView modeName;
        public TextView brightness;
        public TextView temperature;
        public Button button;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        //get the mode's information
        String name = getItem(position).getName();
        int brightness = getItem(position).getBrightness();
        int temperature = getItem(position).getTemperature();

        //Create a Mode object with the 3 values
        Mode mode = new Mode(name, brightness, temperature);

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvName = (TextView) convertView.findViewById(R.id.device_settings_mode_name);
        TextView tvBrightness = (TextView) convertView.findViewById(R.id.brightness_value);
        TextView tvTemperature = (TextView) convertView.findViewById(R.id.temperature_value);

        tvName.setText(name);
        tvBrightness.setText(Integer.toString(brightness));
        tvTemperature.setText(Integer.toString(temperature));

        return  convertView;
        //        View rowView = convertView;
//        if(rowView == null){
//            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            rowView = inflater.inflate(groupId, parent, false);
//            ViewHolder viewHolder = new ViewHolder();
//            viewHolder.modeName = (TextView) rowView.findViewById(R.id.txt);
//            viewHolder.brightness = (TextView) rowView.findViewById(R.id.txt2);
//            viewHolder.temperature = (TextView) rowView.findViewById(R.id.txt3);
//            viewHolder.button = (Button) rowView.findViewById(R.id.bt);
//            rowView.setTag(viewHolder);
//        }
//
//        ViewHolder holder = (ViewHolder) rowView.getTag();
//        holder.modeName.setText(modeList[position]);
//        holder.brightness.setText(modeList[position]);
//        holder.button.setText(modeList[position]);
//        return rowView;
    }
}