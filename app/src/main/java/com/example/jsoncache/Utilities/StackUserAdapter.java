package com.example.jsoncache.Utilities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jsoncache.DetailsActivity;
import com.example.jsoncache.MainActivity;
import com.example.jsoncache.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StackUserAdapter extends ArrayAdapter<StackUser> {

    private List<StackUser> mListUsers;
    private View mDetailsLayout;

    public StackUserAdapter(@NonNull Context context, int resource, @NonNull List<StackUser> objects, @Nullable View detailsLayout) {
        super(context, resource, objects);
        mListUsers = objects;
        mDetailsLayout = detailsLayout;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.stack_user_view, null);
        }

        ImageView mProfilePic = view.findViewById(R.id.user_profile_picture);
        TextView mNameTextView = view.findViewById(R.id.user_display_name);

        final StackUser user = mListUsers.get(position);
        Picasso.with(getContext()).load(user.getProfilePicUrl()).error(R.drawable.ic_android).into(mProfilePic);
        mNameTextView.setText(user.getName());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.isTablet){
                    StackUser user = mListUsers.get(position);
                    ImageView imProfilePic = mDetailsLayout.findViewById(R.id.im_profile_pic);
                    TextView tvName = mDetailsLayout.findViewById(R.id.tv_name);
                    TextView tvLocation = mDetailsLayout.findViewById(R.id.tv_location);
                    TextView tvGoldMedals = mDetailsLayout.findViewById(R.id.tv_gold_medals);
                    TextView tvSilverMedals = mDetailsLayout.findViewById(R.id.tv_silver_medals);
                    TextView tvBronzeMedals = mDetailsLayout.findViewById(R.id.tv_bronze_medals);

                    Picasso.with(getContext()).load(user.getProfilePicUrl()).error(R.drawable.ic_android).into(imProfilePic);
                    tvName.setText(user.getName());
                    tvLocation.setText(user.getLocation());
                    tvGoldMedals.setText("Gold medals: " + user.getGold());
                    tvSilverMedals.setText("Silver medals: " + user.getSilver());
                    tvBronzeMedals.setText("Bronze medals: " + user.getBronze());
                }
                else {
                    Intent intent = new Intent(getContext(), DetailsActivity.class);
                    intent.putExtra("position", position);
                    getContext().startActivity(intent);
                }
            }
        });

        return view;
    }
}
