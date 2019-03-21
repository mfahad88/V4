package com.psl.fantasy.league.season2;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.psl.classes.Config;
import com.psl.classes.DatabaseHandler;
import com.psl.fantasy.league.season2.ContactUs;
import com.psl.fantasy.league.season2.R;
import com.psl.transport.Connection;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class GoldFinanceFragment extends Fragment implements View.OnClickListener {

    SharedPreferences sharedPreferences;
    EditText name, mobile, email;
    AutoCompleteTextView city;
    Spinner spinner;
    DatabaseHandler dbhandler;
    String comment;
    boolean isCity=false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gold_finance, container, false);
        dbhandler=new DatabaseHandler(view.getContext());
        sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF, MODE_PRIVATE);


        name = (EditText) view.findViewById(R.id.txt_name);
        mobile = (EditText) view.findViewById(R.id.txt_mobile);
        email = (EditText) view.findViewById(R.id.txt_email);
        spinner = (Spinner) view.findViewById(R.id.spinner);
        city = view.findViewById(R.id.autoCompleteTextViewCity);
        name.setText(sharedPreferences.getString(Config.FIRST_NAME, "") + " " + sharedPreferences.getString(Config.LAST_NAME, ""));
        mobile.setText(sharedPreferences.getString(Config.CELL_NO, ""));
        email.setText(sharedPreferences.getString(Config.EMAIL,""));
        poulateSpinner(getActivity());
        popultateCity(view.getContext());

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                comment=parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ((Button) view.findViewById(R.id.iv_save)).setOnClickListener(this);

        return view;
    }

    void popultateCity(Context context){
    List<String> list=dbhandler.getCity();
    ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(context,android.R.layout.simple_list_item_1,list);
    city.setAdapter(dataAdapter);
    }

    void poulateSpinner(Context ctx){

        List<String> list=new ArrayList<>();
        list.add("I am interested, please call me");
        list.add("Need more details");
        list.add("I want to avail gold finance product");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, list);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.iv_save){
            if (name.getText().toString().equals("")) {
                Config.getAlert(getActivity(), "Please enter your name");
            } else if (mobile.getText().toString().equals("")) {
                Config.getAlert(getActivity(), "Please enter your mobile number");
            } /*else if (comment.getText().toString().equals("")) {
                    Config.getAlert(getActivity(), "Please enter your question");
                }*/
            else if((TextUtils.isEmpty(city.getText().toString().trim())) || (city.getText().toString().length()<5)){
                Config.getAlert(getActivity(),"Please enter your city");
            }
            else {
                for(int i=0;i<city.getAdapter().getCount();i++){
                    isCity=city.getAdapter().getItem(i).toString().contains(city.getText().toString());
                    if(isCity)
                    break;
                }

                if(isCity){
                    new SendEmail().execute(name.getText().toString(), email.getText().toString(), mobile.getText().toString(), comment+"; city="+city.getText().toString());
                }else{
                    Config.getAlert(getActivity(),"Please enter correct city");
                }
            }
        }
    }

    private class SendEmail extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;
        String mResult;
        String user_id = sharedPreferences.getString(Config.USER_ID, "");

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = ProgressDialog.show(getActivity(), "Submitting", "Please wait...");
        }

        @Override
        protected String doInBackground(String... params) {

            try {

                Connection connection = new Connection("send_email_GF", getActivity());
//                mResult=connection.sendEmail(user_id,params[0],params[1],params[2],params[3]);
                connection.addProperties("user_id", user_id);
                connection.addProperties("name", params[0]);
                connection.addProperties("email", params[1]);
                connection.addProperties("mobile", params[2]);
                connection.addProperties("question", params[3]);
                connection.addProperties("w_username", Config.w1);
                connection.addProperties("w_password", Config.w2);
                connection.ConnectForSingleNode();

                SoapObject object = connection.Result();
                mResult = object.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }



        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (pDialog != null)
                    pDialog.dismiss();

                if (mResult.contains("success")) {
                    Config.getAlert(getActivity(), "Your comments has been submitted", "Success");
                    RadioButton rb = (RadioButton)getActivity().findViewById(R.id.iv_team);
                    rb.setChecked(true);
                } else {
                    Config.getAlert(getActivity(), "Failed to submit, try again");
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
