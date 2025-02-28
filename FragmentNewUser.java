package com.snhu.cs360_project_miller;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;


public class FragmentNewUser extends Fragment {
    private EditText editTextEmail, editTextPassword, editTextCurrent, editTextGoal;
    private androidx.appcompat.widget.SwitchCompat switchSMS;
    private Button buttonCreate;
    private loginDBHelper loginDB;
    private weightTrackerDBHelper weightTrackerDB;
    private goalWeightDBHelper goalWeightDB;
    private SharedPreferences sharedPreferences;
    private final int SMS_PERMISSION_REQUEST_CODE = 1234;

    public FragmentNewUser() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_user, container, false);

        getContext();
        sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        editTextCurrent = view.findViewById(R.id.editTextCurrent);
        editTextGoal = view.findViewById(R.id.editTextGoal);
        switchSMS = view.findViewById(R.id.switchSMS);
        buttonCreate = view.findViewById(R.id.buttonCreate);

        loginDB = loginDBHelper.getInstance(getContext());
        weightTrackerDB = weightTrackerDBHelper.getInstance(getContext());
        goalWeightDB = goalWeightDBHelper.getInstance(getContext());

        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                buttonCreate.setEnabled(true);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        buttonCreate.setOnClickListener(v -> createUser());
        return view;
    }

    private void createUser() {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        String current = editTextCurrent.getText().toString();
        String goal = editTextGoal.getText().toString();

        switchSMS.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Toast.makeText(getContext(), "Switch is " + (isChecked ? "ON" : "OFF"), Toast.LENGTH_SHORT).show();
        });

        if (email.isEmpty() || password.isEmpty() || editTextCurrent.getText().toString().isEmpty()
                || editTextGoal.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean insertSuccess;
        insertSuccess = loginDB.insertUser(email, password);

        if (insertSuccess) {
            weightTrackerDB.addWeighIn(Integer.parseInt(current), email);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(email + "_sms", switchSMS.isChecked());
            editor.apply();

            if (Integer.parseInt(goal) >= Integer.parseInt(current)) {
                goalWeightDB.insertGoal(Integer.parseInt(goal), "lose", email);
            } else {
                goalWeightDB.insertGoal(Integer.parseInt(goal), "gain", email);
            }

            Toast.makeText(getContext(), "User created successfully", Toast.LENGTH_SHORT).show();
            if (switchSMS.isChecked()) {
                hasFilePermissions();
            }
            // Navigate to the login fragment
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new FragmentLogIn());
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            Toast.makeText(getContext(), "Failed to create user", Toast.LENGTH_SHORT).show();
        }

    }

    private void hasFilePermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                    Manifest.permission.SEND_SMS)) {
                // You can show a rationale here if needed
            } else {
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.SEND_SMS},
                        SMS_PERMISSION_REQUEST_CODE);
            }
        }
    }
}

