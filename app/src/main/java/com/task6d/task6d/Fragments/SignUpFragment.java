package com.task6d.task6d.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.room.Room;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.task6d.task6d.Data.AppDatabase;
import com.task6d.task6d.Data.Entity.User;
import com.task6d.task6d.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Pattern;

public class SignUpFragment extends Fragment {

    TextInputEditText usernameInput;
    TextInputEditText emailInput;
    TextInputEditText confirmEmailInput;
    TextInputEditText passwordInput;
    TextInputEditText confirmPasswordInput;
    TextInputEditText phoneNumberInput;
    Button createAccountButton;
    NavController navController;

    public SignUpFragment() {
        // Required empty public constructor
    }

    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
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
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        usernameInput = view.findViewById(R.id.username_input);
        emailInput = view.findViewById(R.id.email_input);
        confirmEmailInput = view.findViewById(R.id.confirm_email_input);
        passwordInput = view.findViewById(R.id.password_input);
        confirmPasswordInput = view.findViewById(R.id.confirm_password_input);
        phoneNumberInput = view.findViewById(R.id.phone_number_input);
        createAccountButton = view.findViewById(R.id.create_account_button);


        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(usernameInput.getText().isEmpty() || emailInput.getText().isEmpty() || confirmEmailInput.getText().isEmpty() || passwordInput.getText().isEmpty() || confirmPasswordInput.getText().isEmpty() || phoneNumberInput.getText().isEmpty()) {
                    Toast.makeText(requireContext(), "Please fill in missing fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!emailInput.getText().toString().equals(confirmEmailInput.getText().toString())) {
                    Toast.makeText(requireContext(), "Email addresses don't match", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(emailInput.getText().toString()).matches()) {
                    Toast.makeText(requireContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!passwordInput.getText().toString().equals(confirmPasswordInput.getText().toString())) {
                    Toast.makeText(requireContext(), "Password don't match", Toast.LENGTH_SHORT).show();
                    return;
                }

                String phoneNumberPattern = "^[+]{1}(?:[0-9\\-\\(\\)\\/" + "\\.]\\s?){6,15}[0-9]{1}$";

                if(!Pattern.matches(phoneNumberPattern, phoneNumberInput.getText().toString())) {
                    Toast.makeText(requireContext(), "Invalid phone number", Toast.LENGTH_SHORT).show();
                    return;
                }


                AppDatabase db = Room.databaseBuilder(getActivity().getApplicationContext(), AppDatabase.class, "app-db").allowMainThreadQueries().build();

                User newUser = new User();
                db.userDAO().deactivateAllUsers();
                newUser.setUserName(usernameInput.getText().toString());
                newUser.setEmail(emailInput.getText().toString());
                // TODO ADD HASHING TO PASSWORDS
                newUser.setPassword(passwordInput.getText().toString());
                newUser.setEmail(emailInput.getText().toString());
                newUser.setPhoneNumber(phoneNumberInput.getText().toString());
                newUser.setActive(true);

                db.userDAO().createUser(newUser);
                navController.navigate(R.id.to_interest_fragment);
                //navController.navigate();

            }
        });


    }
}