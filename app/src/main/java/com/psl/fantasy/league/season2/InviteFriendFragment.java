package com.psl.fantasy.league.season2;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.psl.classes.Config;
import com.psl.fantasy.league.season2.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class InviteFriendFragment extends Fragment {


    public InviteFriendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_invite_friend, container, false);
        ImageView share_img=view.findViewById(R.id.fab);
        final EditText edt_user=view.findViewById(R.id.edt_userId);
        SharedPreferences preferences= view.getContext().getSharedPreferences(Config.SHARED_PREF, MODE_PRIVATE);
        edt_user.setText(preferences.getString(Config.USER_ID,""));
        share_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Invite your fnf by sharing this refer code ("+edt_user.getText().toString()+")");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

            }
        });
        return view;

    }

}
