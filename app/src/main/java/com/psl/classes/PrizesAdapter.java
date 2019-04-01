package com.psl.classes;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.psl.fantasy.league.season2.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PrizesAdapter extends ArrayAdapter {

    Context context;
    List<PrizesClass> list;

    public PrizesAdapter(Context context, List<PrizesClass> list){
        super(context, R.layout.custom_prizes_row,list);
        this.list = list ;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        try {
            if (view == null) {
                LayoutInflater inflator = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflator.inflate(R.layout.custom_prizes_row, parent,false);

            }

            TextView tvPosition = (TextView)view.findViewById(R.id.tv_position);
            TextView tvValue = (TextView)view.findViewById(R.id.tv_value);


            LinearLayout parentLayout = (LinearLayout)view.findViewById(R.id.row);
            if ((position % 2) == 0) {
                parentLayout.setBackground(context.getResources().getDrawable(R.drawable.fixture_row_without_arrow));
            } else {
                parentLayout.setBackground(context.getResources().getDrawable(R.drawable.fixture_row_without_arrow));
            }
            /*if(from_where_to_call.equalsIgnoreCase("history"))
            {
                parentLayout.setBackground(context.getResources().getDrawable(R.drawable.fixture_row_new));
                tvPosition.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);
                tvValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);
            }else {

            }*/

           // Log.e("league.season2",position+". "+list.get(position).getPosition());
            tvPosition.setText(list.get(position).getPosition());
            tvValue.setText(list.get(position).getValue());


        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

}
