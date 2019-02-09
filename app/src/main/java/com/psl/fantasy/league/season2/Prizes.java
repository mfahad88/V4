package com.psl.fantasy.league.season2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
;
/**
 * Created by Yaqoob
 */


public class Prizes extends Fragment {

    ImageView iv_title;
    ImageView iv_prizes,iv_prizes2,iv_prizes3;
    LinearLayout rows_layout, image_layout;
    Button btn_back;
    ImageView iv_row_daily, iv_row_weekly, iv_row_grand;
    String title = "row";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = null;
        try {
            view = inflater.inflate(R.layout.fragment_prizes, container, false);

            rows_layout = (LinearLayout) view.findViewById(R.id.row_layout);
            image_layout = (LinearLayout) view.findViewById(R.id.prizes_main);

            iv_row_daily = (ImageView) view.findViewById(R.id.daily_prizes);
            iv_row_weekly = (ImageView) view.findViewById(R.id.weekly_prizes);
            iv_row_grand = (ImageView) view.findViewById(R.id.grand_prizes);

            iv_prizes = (ImageView) view.findViewById(R.id.prize_image);
            iv_prizes2 = (ImageView) view.findViewById(R.id.prize_image2);
            iv_prizes3 = (ImageView) view.findViewById(R.id.prize_image3);
            iv_title = (ImageView) view.findViewById(R.id.title);

            ((Button) view.findViewById(R.id.btn_back)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        rows_layout.setVisibility(View.VISIBLE);
                        image_layout.setVisibility(View.GONE);
                        iv_title.setBackgroundDrawable(getResources().getDrawable(R.drawable.title_prizes));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            iv_row_daily.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        rows_layout.setVisibility(View.GONE);
                        image_layout.setVisibility(View.VISIBLE);
                        iv_prizes2.setVisibility(View.VISIBLE);
                        iv_prizes3.setVisibility(View.VISIBLE);

                        iv_prizes.setBackgroundDrawable(getResources().getDrawable(R.drawable.samsung));
                        iv_prizes2.setBackgroundDrawable(getResources().getDrawable(R.drawable.huawei));
                        iv_prizes3.setBackgroundDrawable(getResources().getDrawable(R.drawable.led));
                        iv_title.setBackgroundDrawable(getResources().getDrawable(R.drawable.title_daily_prizes));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            iv_row_weekly.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        rows_layout.setVisibility(View.GONE);
                        image_layout.setVisibility(View.VISIBLE);
                        iv_prizes2.setVisibility(View.VISIBLE);
                        iv_prizes3.setVisibility(View.VISIBLE);


                        iv_prizes.setBackgroundDrawable(getResources().getDrawable(R.drawable.dubai));
                        iv_prizes2.setBackgroundDrawable(getResources().getDrawable(R.drawable.iphone));
                        iv_prizes3.setBackgroundDrawable(getResources().getDrawable(R.drawable.ps4));
                        iv_title.setBackgroundDrawable(getResources().getDrawable(R.drawable.title_weekly_prizes));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            iv_row_grand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        rows_layout.setVisibility(View.GONE);
                        image_layout.setVisibility(View.VISIBLE);
                        iv_prizes2.setVisibility(View.GONE);
                        iv_prizes3.setVisibility(View.GONE);

                        iv_prizes.setBackgroundDrawable(getResources().getDrawable(R.drawable.corolla));

                        iv_title.setBackgroundDrawable(getResources().getDrawable(R.drawable.title_grand_prizes));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

}