package com.psl.fantasy.league.season2;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.psl.classes.PrizesAdapter;
import com.psl.classes.PrizesBean;
import com.psl.classes.PrizesClass;
import com.psl.classes.XMLParser;
import com.psl.transport.Connection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yaqoob
 */


public class Prizes extends Fragment {


    Button btn_back;
    String title = "row";
    private ListView lv;
    View view = null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        try {
            view = inflater.inflate(R.layout.fragment_prizes, container, false);
            //lv=view.findViewById(R.id.lv_prizes);
            new GetPrizesAsync().execute();
        } catch (Exception e) {
            Log.e("league.season2",e.getMessage());
            e.printStackTrace();
        }

        return view;
    }

    private class GetPrizesAsync extends AsyncTask<String, String, String> {
        String objResult;
        ProgressDialog pDialog;
        String mResult;
        List<PrizesClass> list;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog=new ProgressDialog(getActivity());
            pDialog.setTitle("Loading");
            pDialog.setMessage("Please Wait...");
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                //if (fixruresList.size() == 0) {

                Connection connection = new Connection(getActivity());
                mResult = connection.getPrizes();
                list = new ArrayList<>();
                if (mResult != null && !mResult.equals("")) {
                    XMLParser xmp = new XMLParser();
                    xmp.parse(mResult);
                    list = xmp.getPrizesData();
                }

                //}

            } catch (Exception e) {
                Log.e("league.season2",e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            try {
                pDialog.dismiss();
                ((TextView)getActivity().findViewById(R.id.txt_header)).setText(list.get(0).getHeading());
                PrizesAdapter adapter=new PrizesAdapter(getActivity(),list);
                ((ListView) getActivity().findViewById(R.id.lv_prizes)).setVisibility(View.VISIBLE);
                ((ListView) getActivity().findViewById(R.id.lv_prizes)).setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}