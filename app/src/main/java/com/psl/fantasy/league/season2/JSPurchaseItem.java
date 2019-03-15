package com.psl.fantasy.league.season2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.psl.classes.Config;
import com.psl.classes.InventoryClass;
import com.psl.classes.JSUtils;
import com.psl.classes.LeaderboarPositionVO;
import com.psl.classes.Partner;
import com.psl.classes.XMLParser;
import com.psl.transport.Connection;
import com.psl.fantasy.league.season2.R;;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class JSPurchaseItem extends Fragment {

    String strItemName, strOTP, strMobNmbr, strAuthHeader, strAmount, strCharges, userID;
    TextView txtItemName, txtItemPrice, txtTxn,txtMobileNumber;
    Button btnNext, btnPay;
    int itemResId;
    ImageView imgItem;
    SharedPreferences sharedPreferences;
    ArrayList<InventoryClass> dealItems;
    Spinner spinnerWallet;
    EditText edtMobileNumber,edtEmail;
    int wallet_index=0;
    Date date;
    SimpleDateFormat sdf;
    List<String> list_url=null;
    Connection conn;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_purchase_item, container, false);//setContentView(R.layout.activity_purchase_item);
        sdf=new SimpleDateFormat("ddMMyyHHmmssSSS");
        list_url=new ArrayList<>();
        sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF, MODE_PRIVATE);
        strMobNmbr = sharedPreferences.getString(Config.JS_Mobile_Number, "");
        userID = sharedPreferences.getString(Config.USER_ID, "");

        Config.PopulateHeader(getActivity(), view.findViewById(R.id.helmet_layout));
        conn=new Connection(view.getContext());
        strAmount = getArguments().getString(Config.JS_Item_Purchase_Price);
        strItemName = getArguments().getString(Config.JS_Item_Purchase_Name);
        itemResId = getArguments().getInt(Config.JS_Item_Purchase_Res, 0);

        try {
            dealItems = (ArrayList<InventoryClass>) getArguments().getSerializable("DealItems");
        } catch (Exception e) {

        }

        txtItemName = (TextView) view.findViewById(R.id.txtItemName);
        txtItemPrice = (TextView) view.findViewById(R.id.txtItemPrice);
        txtTxn = (TextView) view.findViewById(R.id.textView7);
        btnNext = (Button) view.findViewById(R.id.btnNext);
        btnPay = (Button) view.findViewById(R.id.btnPay);
        btnPay.setVisibility(View.GONE);
        imgItem = (ImageView) view.findViewById(R.id.imgItem);
        edtEmail = view.findViewById(R.id.edtEmail);
        edtMobileNumber=view.findViewById(R.id.edtMobileNumber);
        spinnerWallet=view.findViewById(R.id.wallet_type);


        edtEmail.setText(sharedPreferences.getString(Config.EMAIL,""));
        //  txtItemName.setText(strItemName);
        edtMobileNumber.setText(strMobNmbr);
        txtItemPrice.setText("PKR " + strAmount);
        imgItem.setImageResource(itemResId);

        new MyAsyncIntegration().execute();
        spinnerWallet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                wallet_index=position;
                if(wallet_index==0){
                    edtEmail.setVisibility(View.GONE);
                    edtMobileNumber.setVisibility(View.GONE);
                }else{
                    edtEmail.setVisibility(View.VISIBLE);
                    edtMobileNumber.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(wallet_index==0) {
                    new MyAyncTaskPaymentINQ().execute();

                }else{
                    new MyAyncTaskEPPayBill().execute();

                }
            }
        });
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(wallet_index==0) {
                    new MyAyncTaskPayBill().execute();
                }
            }
        });
        return view;
    }



    void poulateSpinner(Context ctx, List list){


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, list);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerWallet.setAdapter(dataAdapter);
    }


    class MyAsyncIntegration extends AsyncTask<Void,Void,Void>{
        ProgressDialog progressDialog;
        List<String> listSpinner;
        String mResult;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            Connection conn = new Connection(getActivity());
            mResult = conn.getIntegrationPartner();
            listSpinner=new ArrayList<>();
            if (mResult != null && !mResult.equals("")) {
                XMLParser xmp = new XMLParser();
                xmp.parse(mResult);
                listSpinner = xmp.getMyPartners();
                list_url = xmp.getMyPartnersUrl();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            if (mResult.startsWith("-1")) {
                showAlert2(mResult.replace("-1", ""), "Failure");

            } else {
                poulateSpinner(getActivity(),listSpinner);

            }
        }
    }
    class MyAyncTaskPaymentINQ extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;
        String res = "";
        String encrptedOTP;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Connection conn = new Connection(getActivity());
            res = conn.JSAuth();

            if (!res.startsWith("-1") && !res.trim().isEmpty()) {
                {
                    strAuthHeader = res;
                    res = conn.JSPurchaseItemINQ(strAuthHeader, strMobNmbr, strAmount);
                }

            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            if (res.startsWith("-1")) {
                showAlert2(res.replace("-1", ""), "Failure");

            } else {

                strCharges = res;
                Intent intent = new Intent(getActivity(), OTPActivity.class);
                intent.putExtra(Config.JS_Mobile_Number, strMobNmbr);
                intent.putExtra(Config.JS_Auth_Header, strAuthHeader);
                intent.putExtra(Config.JS_Action, 1);
                startActivityForResult(intent, OTPActivity.REQUEST_OTP);


            }


        }
    }

    class MyAyncTaskPayBill extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;
        String res = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Connection conn = new Connection(getActivity());
            res = conn.JSPurchaseItemPayment(strAuthHeader, strMobNmbr, strAmount, strOTP, strCharges, strItemName);
            if(!res.startsWith("-1")){
                StringBuilder sb=new StringBuilder();
                for(int i=0;i<JSUtils.shopingCartList.size();i++){
                    InventoryClass iv=JSUtils.shopingCartList.get(i);
                    sb.append("Id= "+iv.getId()+",");
                    sb.append("Name= "+iv.getName()+",");
                    sb.append("Price= "+iv.getPrice()+",");
                    sb.append("Item Count= "+iv.getItemCount()+",");
                    sb.append("Description= "+iv.getDescription()+",");
                    sb.append("Deal of day= "+iv.getDealOfDay());
                }
                conn.insertUserLog(sharedPreferences.getString(Config.JS_Mobile_Number, ""), "Purchase",
                        sb.toString(), "  ", " ");
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            if (res.startsWith("-1")) {
                showAlert2(res.replace("-1", ""), "Failure");

            } else {
                if (dealItems == null) {
                    for (int i = 0; i < JSUtils.shopingCartList.size(); i++) {

                        InventoryClass ivClass = JSUtils.shopingCartList.get(i);
                        String name = ivClass.getName();
                        int itemCount = 0;
                        String item = "";
                        switch (name.toLowerCase()) {
                            case InventoryClass.GoldenGloves:
                                item = Config.USER_GOLDEN_GLOVES;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.USER_GOLDEN_GLOVES, "0"));
                                break;

                            case InventoryClass.OrangeCap:
                                item = Config.USER_ORANGE_CAP;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.USER_ORANGE_CAP, "0"));
                                break;

                            case InventoryClass.PurpleCap:
                                item = Config.USER_PURPLE_CAP;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.USER_PURPLE_CAP, "0"));
                                break;

                            case InventoryClass.SafetyCap:
                                item = Config.USER_PLAYER_SAFETY;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.USER_PLAYER_SAFETY, "0"));
                                break;
                            case InventoryClass.TeamSafety:
                                item = Config.USER_TEAM_SAFETY;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.USER_TEAM_SAFETY, "0"));
                                break;

                            case InventoryClass.Swaps:
                                item = Config.SWAP_COUNT;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.SWAP_COUNT, "0"));
                                break;
                            case InventoryClass.Swaps10:
                                item = Config.SWAP_COUNT;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.SWAP_COUNT, "0"));
                                break;
                            case InventoryClass.Iconic:
                                item = Config.USER_ICONIC_PLAYER;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.USER_ICONIC_PLAYER, "0"));
                                break;

                            case InventoryClass.IconPlayer:
                                item = Config.USER_ICONIC_PLAYER;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.USER_ICONIC_PLAYER, "0"));
                                break;
                            case InventoryClass.PlayerSafety:
                                item = Config.USER_PLAYER_SAFETY;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.USER_PLAYER_SAFETY, "0"));
                                break;
                        }

                        int itemPurchse = 0;
                        try {
                            itemPurchse = ivClass.getItemCount();
                        } catch (Exception e) {

                        }
                        sharedPreferences.edit().putString(item, String.valueOf(itemCount + itemPurchse)).commit();

                    }
                } else {
                    for (int i = 0; i < dealItems.size(); i++) {
                        String item = "";
                        InventoryClass ivClass = dealItems.get(i);
                        String name = ivClass.getName();
                        int itemCount = 0;
                        switch (name.toLowerCase()) {
                            case "golden golves":
                                item = Config.USER_GOLDEN_GLOVES;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.USER_GOLDEN_GLOVES, "0"));
                                break;


                            case InventoryClass.OrangeCap:
                                item = Config.USER_ORANGE_CAP;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.USER_ORANGE_CAP, "0"));
                                break;

                            case InventoryClass.PurpleCap:
                                item = Config.USER_PURPLE_CAP;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.USER_PURPLE_CAP, "0"));
                                break;

                            case InventoryClass.SafetyCap:
                                item = Config.USER_PLAYER_SAFETY;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.USER_PLAYER_SAFETY, "0"));
                                break;
                            case InventoryClass.TeamSafety:
                                item = Config.USER_TEAM_SAFETY;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.USER_TEAM_SAFETY, "0"));
                                break;

                            case InventoryClass.Swaps:
                                item = Config.SWAP_COUNT;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.SWAP_COUNT, "0"));
                                break;
                            case InventoryClass.Swaps10:
                                item = Config.SWAP_COUNT;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.SWAP_COUNT, "0"));
                                break;
                            case InventoryClass.Iconic:
                                item = Config.USER_ICONIC_PLAYER;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.USER_ICONIC_PLAYER, "0"));
                                break;

                            case InventoryClass.IconPlayer:
                                item = Config.USER_ICONIC_PLAYER;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.USER_ICONIC_PLAYER, "0"));
                                break;
                            case InventoryClass.PlayerSafety:
                                item = Config.USER_PLAYER_SAFETY;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.USER_PLAYER_SAFETY, "0"));
                                break;
                        }

                        int itemPurchse = 0;
                        try {
                            itemPurchse = ivClass.getItemCount();
                        } catch (Exception e) {
                        }
                        sharedPreferences.edit().putString(item, String.valueOf(itemCount + itemPurchse)).commit();

                    }
                }

                JSUtils.shopingCartList.clear();
                showAlert2("You have successfully purchased item(s)" + strItemName + ".\n" + Config.JS_TransactionID + " : " + res, "Success");

                for (int i = 0; i < JSUtils.inventoryClassList.size(); i++) {
                    JSUtils.inventoryClassList.get(i).clearItemCount();
                }


            }
        }
    }

    class MyAyncTaskEPPayBill extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;
        String res = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Connection conn = new Connection(getActivity());
            date=new Date();
            res = conn.JSAuth();

            if (!res.startsWith("-1") && !res.trim().isEmpty()) {
                {
                    strAuthHeader = res;
                    //res = conn.JSPurchaseItemINQ(strAuthHeader, strMobNmbr, strAmount);
                    res = conn.EPPurchaseItemINQ(list_url.get(1),strAuthHeader,"6743" + sdf.format(date),strAmount ,edtMobileNumber.getText().toString(), edtEmail.getText().toString());
                    if(!res.startsWith("-1")){
                        StringBuilder sb=new StringBuilder();
                        for(int i=0;i<JSUtils.shopingCartList.size();i++){
                            InventoryClass iv=JSUtils.shopingCartList.get(i);
                            sb.append("Id= "+iv.getId()+",");
                            sb.append("Name= "+iv.getName()+",");
                            sb.append("Price= "+iv.getPrice()+",");
                            sb.append("Item Count= "+iv.getItemCount()+",");
                            sb.append("Description= "+iv.getDescription()+",");
                            sb.append("Deal of day= "+iv.getDealOfDay());
                        }
                        conn.insertUserLog(sharedPreferences.getString(Config.JS_Mobile_Number, ""), "Shop",
                                sb.toString(), "  ", " ");
                    }
                }
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            if (res.startsWith("-1")) {
                showAlert2(res.replace("-1", ""), "Failure");

            }
            else {
                if (dealItems == null) {
                    for (int i = 0; i < JSUtils.shopingCartList.size(); i++) {

                        InventoryClass ivClass = JSUtils.shopingCartList.get(i);
                        String name = ivClass.getName();
                        int itemCount = 0;
                        String item = "";
                        switch (name.toLowerCase()) {
                            case InventoryClass.GoldenGloves:
                                item = Config.USER_GOLDEN_GLOVES;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.USER_GOLDEN_GLOVES, "0"));
                                break;

                            case InventoryClass.OrangeCap:
                                item = Config.USER_ORANGE_CAP;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.USER_ORANGE_CAP, "0"));
                                break;

                            case InventoryClass.PurpleCap:
                                item = Config.USER_PURPLE_CAP;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.USER_PURPLE_CAP, "0"));
                                break;

                            case InventoryClass.SafetyCap:
                                item = Config.USER_PLAYER_SAFETY;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.USER_PLAYER_SAFETY, "0"));
                                break;
                            case InventoryClass.TeamSafety:
                                item = Config.USER_TEAM_SAFETY;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.USER_TEAM_SAFETY, "0"));
                                break;

                            case InventoryClass.Swaps:
                                item = Config.SWAP_COUNT;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.SWAP_COUNT, "0"));
                                break;
                            case InventoryClass.Swaps10:
                                item = Config.SWAP_COUNT;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.SWAP_COUNT, "0"));
                                break;
                            case InventoryClass.Iconic:
                                item = Config.USER_ICONIC_PLAYER;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.USER_ICONIC_PLAYER, "0"));
                                break;

                            case InventoryClass.IconPlayer:
                                item = Config.USER_ICONIC_PLAYER;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.USER_ICONIC_PLAYER, "0"));
                                break;
                            case InventoryClass.PlayerSafety:
                                item = Config.USER_PLAYER_SAFETY;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.USER_PLAYER_SAFETY, "0"));
                                break;
                        }

                        int itemPurchse = 0;
                        try {
                            itemPurchse = ivClass.getItemCount();
                        } catch (Exception e) {

                        }
                        sharedPreferences.edit().putString(item, String.valueOf(itemCount + itemPurchse)).commit();

                    }
                } else {
                    for (int i = 0; i < dealItems.size(); i++) {
                        String item = "";
                        InventoryClass ivClass = dealItems.get(i);
                        String name = ivClass.getName();
                        int itemCount = 0;
                        switch (name.toLowerCase()) {
                            case "golden golves":
                                item = Config.USER_GOLDEN_GLOVES;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.USER_GOLDEN_GLOVES, "0"));
                                break;


                            case InventoryClass.OrangeCap:
                                item = Config.USER_ORANGE_CAP;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.USER_ORANGE_CAP, "0"));
                                break;

                            case InventoryClass.PurpleCap:
                                item = Config.USER_PURPLE_CAP;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.USER_PURPLE_CAP, "0"));
                                break;

                            case InventoryClass.SafetyCap:
                                item = Config.USER_PLAYER_SAFETY;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.USER_PLAYER_SAFETY, "0"));
                                break;
                            case InventoryClass.TeamSafety:
                                item = Config.USER_TEAM_SAFETY;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.USER_TEAM_SAFETY, "0"));
                                break;

                            case InventoryClass.Swaps:
                                item = Config.SWAP_COUNT;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.SWAP_COUNT, "0"));
                                break;
                            case InventoryClass.Swaps10:
                                item = Config.SWAP_COUNT;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.SWAP_COUNT, "0"));
                                break;
                            case InventoryClass.Iconic:
                                item = Config.USER_ICONIC_PLAYER;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.USER_ICONIC_PLAYER, "0"));
                                break;

                            case InventoryClass.IconPlayer:
                                item = Config.USER_ICONIC_PLAYER;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.USER_ICONIC_PLAYER, "0"));
                                break;
                            case InventoryClass.PlayerSafety:
                                item = Config.USER_PLAYER_SAFETY;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.USER_PLAYER_SAFETY, "0"));
                                break;
                        }

                        int itemPurchse = 0;
                        try {
                            itemPurchse = ivClass.getItemCount();
                        } catch (Exception e) {
                        }
                        sharedPreferences.edit().putString(item, String.valueOf(itemCount + itemPurchse)).commit();

                    }
                }

                JSUtils.shopingCartList.clear();
                //showAlert2("You have successfully purchased item(s)" + strItemName + ".\n" + Config.JS_TransactionID + " : " + res, "Success");
                showAlert2(res, "Success");

                for (int i = 0; i < JSUtils.inventoryClassList.size(); i++) {
                    JSUtils.inventoryClassList.get(i).clearItemCount();
                }
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == OTPActivity.REQUEST_OTP) {

            if (resultCode == -1) {

                String message = data.getStringExtra("message");
                showAlert2(message, "Failure");
            } else if (resultCode == -2) {

                btnNext.performClick();
            } else if (resultCode == 100) {
                strOTP = data.getStringExtra(Config.JS_Encrypted_OTP);
                txtTxn.setText("Transaction Charges : " + strCharges + " Rs.");
                txtTxn.setVisibility(View.INVISIBLE);
                btnPay.setVisibility(View.VISIBLE);
                btnNext.setVisibility(View.GONE);

            }
        }
    }


    void showAlert(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    void showAlert2(String message, final String title) {
        Config.getAlert(getActivity(), message, title);

        if (title.toLowerCase().equals("success")) {

            final RadioButton radiogroup1 = getActivity().findViewById(R.id.iv_qr);
            radiogroup1.setChecked(true);
                   /* Fragment fragment=new MyInventory();

                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.commit();*/
        }


    }
}

