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
import com.example.jsoncache.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StackUserAdapter extends ArrayAdapter<StackUser> {

    private List<StackUser> mListUsers;

    public StackUserAdapter(@NonNull Context context, int resource, @NonNull List<StackUser> objects) {
        super(context, resource, objects);
        mListUsers = objects;
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
                Intent intent = new Intent(getContext(), DetailsActivity.class);
                intent.putExtra("position", position);
                getContext().startActivity(intent);
            }
        });

        return view;
    }
}
