package com.example.task6d.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.task6d.Data.AppDatabase;
import com.example.task6d.Data.Entity.User;
import com.example.task6d.R;
import com.google.android.material.textfield.TextInputEditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    TextView needAccountButton;
    NavController navController;
    TextInputEditText usernameInput;
    TextInputEditText passwordInput;
    Button loginButton;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        usernameInput = view.findViewById(R.id.username_input);
        passwordInput = view.findViewById(R.id.password_input);
        loginButton = view.findViewById(R.id.login_button);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        needAccountButton = view.findViewById(R.id.need_account_button);


        needAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navController.navigate(R.id.to_sign_up_fragment);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(usernameInput.getText().isEmpty() || passwordInput.getText().isEmpty()) {
                    Toast.makeText(requireContext(), "Missing username or password", Toast.LENGTH_SHORT).show();
                }
                AppDatabase db = Room.databaseBuilder(getActivity().getApplicationContext(), AppDatabase.class, "app-db").allowMainThreadQueries().build();

                User retrivedUser = db.userDAO().getUserByName(usernameInput.getText().toString());

                if(retrivedUser == null) {
                    Toast.makeText(requireContext(), "Invalid username", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!retrivedUser.getPassword().equals(passwordInput.getText().toString())) {
                    Toast.makeText(requireContext(), "Invalid password", Toast.LENGTH_SHORT).show();
                    return;
                }

                db.userDAO().deactivateAllUsers();
                retrivedUser.setActive(true);

                db.userDAO().updateUser(retrivedUser);

                navController.navigate(R.id.to_home_fragment);
            }
        });
    }
}