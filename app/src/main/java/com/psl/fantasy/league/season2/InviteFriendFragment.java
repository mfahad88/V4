package com.psl.fantasy.league.season2;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.psl.classes.Config;
import com.psl.fantasy.league.season2.R;

import java.io.File;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class InviteFriendFragment extends Fragment {

    private String shareId;
    public InviteFriendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_invite_friend, container, false);
        Button share_img=view.findViewById(R.id.fab);
        final EditText edt_user=view.findViewById(R.id.edt_userId);
        SharedPreferences preferences= view.getContext().getSharedPreferences(Config.SHARED_PREF, MODE_PRIVATE);
        if(preferences.getString(Config.CELL_NO,"").length()>0){
            shareId=preferences.getString(Config.CELL_NO,"").substring(4,7).trim()+""+preferences.getString(Config.USER_ID,"");
        }
        edt_user.setText(shareId);
        share_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Let's play together, click https://play.google.com/store/apps/details?id=com.psl.fantasy.league&hl=en to download the app. Use My invite code" +
                        "("+edt_user.getText().toString()+") to get rupees 100 inventory as joining bonus for both! Let's test our cricketing skills");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);*/
                initShareIntent("text","Let's play together, click https://play.google.com/store/apps/details?id=com.psl.fantasy.league&hl=en to download the app. Use My invite code" +
                        "("+edt_user.getText().toString()+") to get rupees 100 inventory as joining bonus for both! Let's test our cricketing skills");
            }
        });
        return view;

    }

    private void initShareIntent(String type, String _text) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, _text);
        //shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(Environment.getExternalStorageDirectory().toString() + "/acl.png")));  //optional//use this when you want to send an image
        shareIntent.setType("text/plain");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "send"));
    }

}
