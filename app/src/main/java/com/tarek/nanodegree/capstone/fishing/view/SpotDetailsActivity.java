package com.tarek.nanodegree.capstone.fishing.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tarek.nanodegree.capstone.fishing.R;
import com.tarek.nanodegree.capstone.fishing.model.pojo.Spot;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SpotDetailsActivity extends AppCompatActivity {

    @BindView(R.id.textView6)
    TextView waterDepth;
    @BindView(R.id.textView7)
    TextView species;
    @BindView(R.id.textView8)
    TextView date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_details);
        ButterKnife.bind(this);

        Spot spot = null;
        String spotString = "";
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            spotString = bundle.getString("spot");
        }

        if (!spotString.isEmpty()) {
            spot = new Gson().fromJson(spotString, Spot.class);
        }

        if (spot != null) {

            waterDepth.setText(spot.getDepth() + "");

            if (spot.getSpecies() != null) {
                String speicesStr = "";
                for (int i = 0; i < spot.getSpecies().size(); i++) {
                    speicesStr = speicesStr + spot.getSpecies().get(i) + ",";
                }
                species.setText(speicesStr);
            }

            date.setText(spot.getDateTime());

        }
    }
}
