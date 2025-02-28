package com.snhu.cs360_project_miller;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.zip.Inflater;

public class FragmentLogIn extends Fragment {
    private EditText eUsername;
    private EditText ePassword;
    private loginDBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log_in, container, false);
        
        eUsername = view.findViewById(R.id.EmailAddress);
        ePassword = view.findViewById(R.id.Password);
        dbHelper = dbHelper.getInstance(getContext());
        
        Button buttonLogin = view.findViewById(R.id.buttonLogin);
        Button buttonCreate = view.findViewById(R.id.buttonCreate);
        
        buttonLogin.setOnClickListener(v -> loginUser());
        buttonCreate.setOnClickListener(v -> openNewUserFrag());

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String username = eUsername.getText().toString().trim();
                String password = ePassword.getText().toString().trim();

                // Enable button only if both fields are non-empty
                buttonLogin.setEnabled(!username.isEmpty() && !password.isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        // Attach the TextWatcher to both input fields
        eUsername.addTextChangedListener(textWatcher);
        ePassword.addTextChangedListener(textWatcher);
        return view;
    }

   

    private void loginUser() {
        String username = eUsername.getText().toString().trim();
        String password = ePassword.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "Please enter both username and password", Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (dbHelper.isUserValid(username, password)) {
                Toast.makeText(getContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                openViewWeights();
            } else {
                Toast.makeText(getContext(), "Login Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openViewWeights() {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new FragmentViewWeights());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void openNewUserFrag() {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new FragmentNewUser());
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
    