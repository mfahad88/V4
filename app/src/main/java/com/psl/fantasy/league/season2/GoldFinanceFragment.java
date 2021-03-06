package com.psl.fantasy.league.season2;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.psl.classes.Config;
import com.psl.fantasy.league.season2.ContactUs;
import com.psl.fantasy.league.season2.R;
import com.psl.transport.Connection;

import org.ksoap2.serialization.SoapObject;

import static android.content.Context.MODE_PRIVATE;


public class GoldFinanceFragment extends Fragment {

    SharedPreferences sharedPreferences;
    EditText name, mobile, email, comment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gold_finance, container, false);
        sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF, MODE_PRIVATE);
        name = (EditText) view.findViewById(R.id.txt_name);
        mobile = (EditText) view.findViewById(R.id.txt_mobile);
        email = (EditText) view.findViewById(R.id.txt_email);
        comment = (EditText) view.findViewById(R.id.txt_comment);

        name.setText(sharedPreferences.getString(Config.FIRST_NAME, "") + " " + sharedPreferences.getString(Config.LAST_NAME, ""));
        mobile.setText(sharedPreferences.getString(Config.CELL_NO, ""));
        email.setText(sharedPreferences.getString(Config.EMAIL,""));

        ((Button) view.findViewById(R.id.iv_save)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (name.getText().toString().equals("")) {
                    Config.getAlert(getActivity(), "Please enter your name");
                } else if (mobile.getText().toString().equals("")) {
                    Config.getAlert(getActivity(), "Please enter your mobile number");
                } /*else if (comment.getText().toString().equals("")) {
                    Config.getAlert(getActivity(), "Please enter your question");
                }*/ else {
                    new SendEmail().execute(name.getText().toString(), email.getText().toString(), mobile.getText().toString(), comment.getText().toString());
                }
            }
        });

        return view;
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
                    comment.setText("");
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
