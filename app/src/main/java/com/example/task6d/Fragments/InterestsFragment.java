package com.example.task6d.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.task6d.Adapters.InterestButtonAdapter;
import com.example.task6d.Data.AppDatabase;
import com.example.task6d.Data.Entity.Interest;
import com.example.task6d.R;

import java.util.ArrayList;
import java.util.List;


public class InterestsFragment extends Fragment {

    RecyclerView interestRecyler;
    Button nextButton;
    NavController navController;

    public InterestsFragment() {
        // Required empty public constructor
    }
    public static InterestsFragment newInstance(String param1, String param2) {
        InterestsFragment fragment = new InterestsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_interests, container, false);

        nextButton = view.findViewById(R.id.next_button);
        interestRecyler = view.findViewById(R.id.interest_recycler);
        AppDatabase db = Room.databaseBuilder(getActivity().getApplicationContext(), AppDatabase.class, "app-db").allowMainThreadQueries().build();

        ArrayList<ArrayList<Interest>> interestsOrganised = new ArrayList<>();

        List<Interest> interestsList = db.interestDAO().getInterests();

        ArrayList<Interest> interests = new ArrayList<>(interestsList);

        ArrayList<Interest> formattedInterest = new ArrayList<>();
        for(int index = 0; index < interests.size(); index++) {
            formattedInterest.add(interests.get(index));
            if(index%2 == 0 && index != 0) {
                interestsOrganised.add(formattedInterest);
                formattedInterest = new ArrayList<>();
            }
        }

        InterestButtonAdapter interestButtonAdapter = new InterestButtonAdapter(requireContext(), interestsOrganised);

        interestRecyler.setAdapter(interestButtonAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.to_home_fragment);
            }
        });
    }
}