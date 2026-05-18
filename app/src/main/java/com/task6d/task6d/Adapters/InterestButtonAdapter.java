package com.task6d.task6d.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.task6d.task6d.Data.AppDatabase;
import com.task6d.task6d.Data.Entity.Interest;
import com.task6d.task6d.Data.Entity.UserInterest;
import com.task6d.task6d.R;

import java.util.ArrayList;

public class InterestButtonAdapter extends RecyclerView.Adapter<InterestButtonAdapter.InterestButtonHolder> {
    Context context;
    ArrayList<ArrayList<Interest>> interests;

    public InterestButtonAdapter(Context context, ArrayList<ArrayList<Interest>> interests) {
        this.context = context;
        this.interests = interests;
    }

    @NonNull
    @Override
    public InterestButtonHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_interest_buttons, parent, false);
        return new InterestButtonAdapter.InterestButtonHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InterestButtonHolder holder, int position) {

        holder.interestButtonOne.setText(interests.get(position).get(0).getName());
        holder.interestButtonTwo.setText(interests.get(position).get(1).getName());

        AppDatabase db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "app-db").allowMainThreadQueries().build();

        int activeUserId = db.userDAO().getActiveUser().id;
        int interestIdOne = interests.get(position).get(0).id;
        int interestIdTwo = interests.get(position).get(1).id;


        holder.interestButtonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInterest userInterest = new UserInterest();
                userInterest.interestId = interestIdOne;
                userInterest.userId = activeUserId;
                if(db.userInterestDAO().getUserInterest(activeUserId, interestIdOne) == null) {
                    db.userInterestDAO().createUserInterest(userInterest);
                    v.setBackgroundColor(ContextCompat.getColor(context, R.color.md_theme_primaryFixedDim));
                }
                else {
                    db.userInterestDAO().deleteUserInterest(userInterest);
                    v.setBackgroundColor(ContextCompat.getColor(context, R.color.md_theme_primary));
                }
            }
        });
        holder.interestButtonTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInterest userInterest = new UserInterest();
                userInterest.interestId = interestIdTwo;
                userInterest.userId = activeUserId;
                if(db.userInterestDAO().getUserInterest(activeUserId, interestIdTwo) == null) {
                    db.userInterestDAO().createUserInterest(userInterest);
                    v.setBackgroundColor(ContextCompat.getColor(context, R.color.md_theme_primaryFixedDim));
                }
                else {
                    db.userInterestDAO().deleteUserInterest(userInterest);
                    v.setBackgroundColor(ContextCompat.getColor(context, R.color.md_theme_primary));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return interests.size();
    }

    public static class InterestButtonHolder extends RecyclerView.ViewHolder {

        Button interestButtonOne;
        Button interestButtonTwo;

        public InterestButtonHolder(@NonNull View itemView) {
            super(itemView);

            interestButtonOne = itemView.findViewById(R.id.interest_button_one);
            interestButtonTwo = itemView.findViewById(R.id.interest_button_two);

        }
    }
}
