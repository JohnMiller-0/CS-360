package com.snhu.cs360_project_miller;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;


public class FragmentSetting extends Fragment {
    private static SharedPreferences sharedPreferences;
    private androidx.appcompat.widget.SwitchCompat switchSMS;
    private int SMS_PERMISSION_REQUEST_CODE = 1234;


    public FragmentSetting() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        getContext();
        sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        loginDBHelper loginDB = loginDBHelper.getInstance(getContext());
        String username = loginDBHelper.getCurrentEmail();


        Button buttonChange = view.findViewById(R.id.buttonChange);
        switchSMS = view.findViewById(R.id.switchSMS);
        Button buttonReturn = view.findViewById(R.id.buttonReturn);
        Button buttonLogout = view.findViewById(R.id.buttonLogout);

        boolean isSMSEnabled = sharedPreferences.getBoolean(username + "_sms", false);
        switchSMS.setChecked(isSMSEnabled);

        buttonChange.setOnClickListener(v -> changePassword());
        buttonReturn.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
        buttonLogout.setOnClickListener(v -> logout());
        switchSMS.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Toast.makeText(getContext(), "Switch is " + (isChecked ? "ON" : "OFF"), Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(username + "_sms", switchSMS.isChecked());
            editor.apply();
            if (switchSMS.isChecked()) {
                hasFilePermissions();
            }
        });
        return view;
    }

    private void changePassword() {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new FragmentChangePassword());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void logout() {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new FragmentLogIn());
        transaction.addToBackStack(null);
        transaction.commit();
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

