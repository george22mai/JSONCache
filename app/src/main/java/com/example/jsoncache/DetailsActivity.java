package com.example.jsoncache;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jsoncache.Utilities.JSONUtils;
import com.example.jsoncache.Utilities.StackUser;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    int position;

    @BindView(R.id.im_profile_pic)
    ImageView imProfilePic;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_location) TextView tvLocation;
    @BindView(R.id.tv_gold_medals) TextView tvGoldMedals;
    @BindView(R.id.tv_silver_medals) TextView tvSilverMedals;
    @BindView(R.id.tv_bronze_medals) TextView tvBronzeMedals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        position = getIntent().getIntExtra("position", 0);
        StackUser user = JSONUtils.getUserFromInternalCache(getApplicationContext(), position);

        Picasso.with(getApplicationContext()).load(user.getProfilePicUrl()).error(R.drawable.ic_android).into(imProfilePic);
        tvName.setText(user.getName());
        tvLocation.setText(user.getLocation());
        tvGoldMedals.setText("Gold medals: " + user.getGold());
        tvSilverMedals.setText("Silver medals: " + user.getSilver());
        tvBronzeMedals.setText("Bronze medals: " + user.getBronze());
    }
}
