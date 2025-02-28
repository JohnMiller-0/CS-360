package com.snhu.cs360_project_miller;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class FragmentChangePassword extends Fragment {
    private loginDBHelper loginDBHelper;
    private Button buttonSave;
    private EditText editTextNewPassword, editTextConfirmPassword;

    public FragmentChangePassword() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        loginDBHelper = loginDBHelper.getInstance(getContext());
        buttonSave = view.findViewById(R.id.buttonSave);
        editTextNewPassword = view.findViewById(R.id.editTextTextPassword);
        editTextConfirmPassword = view.findViewById(R.id.editTextTextPassword2);

        editTextNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                buttonSave.setEnabled(true);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        buttonSave.setOnClickListener(v -> savePassword());

        return view;
    }

    private void savePassword() {
        if (editTextNewPassword.getText().toString().equals(editTextConfirmPassword.getText().toString().trim())) {
            String newPassword = editTextNewPassword.getText().toString().trim();
            loginDBHelper.changePassword(newPassword);
            Toast.makeText(getContext(), "Password changed successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
        }

    }
}