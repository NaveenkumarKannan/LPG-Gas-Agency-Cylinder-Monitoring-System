package com.naveen_project.hpgas;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by SANKAR on 3/30/2018.
 */

public class DataAdapter extends ArrayAdapter {

    List list = new ArrayList();
    public DataAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }


    public void add(Data object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row;
        row = convertView;
        DataHolder dataHolder;
        if(row == null){
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.row_layout,parent,false);
            dataHolder = new DataHolder();
            dataHolder.tvShowCylinderId = (TextView) row.findViewById(R.id.tvShowCylinderId);
            dataHolder.tvShowStatus = (TextView) row.findViewById(R.id.tvShowStatus);


            row.setTag(dataHolder);

        }
        else {
            dataHolder = (DataHolder) row.getTag();
        }
        Data data = (Data) this.getItem(position);
        dataHolder.tvShowCylinderId.setText(data.getGasId());
        dataHolder.tvShowStatus.setText(data.getStatus());


        return row;
    }
    static class  DataHolder
    {
        TextView tvShowCylinderId, tvShowStatus;
    }

}
