package com.snhu.cs360_project_miller;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class FragmentAddWeight extends Fragment {
    private EditText editTextWeight;
    private Button buttonSave;
    private weightTrackerDBHelper dbHelper;
    private loginDBHelper loginDB;
    private String username;

    public FragmentAddWeight() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_add_weight, container, false);
        dbHelper = dbHelper.getInstance(getContext());
        loginDB = loginDBHelper.getInstance(getContext());
        username = loginDBHelper.getCurrentEmail();

        buttonSave = view.findViewById(R.id.buttonSave);
        editTextWeight = view.findViewById(R.id.editTextWeight);

        editTextWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                buttonSave.setEnabled(!s.toString().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        buttonSave.setOnClickListener(v -> saveWeight());
        return view;
    }

    private void saveWeight() {
        String weightString = editTextWeight.getText().toString();
        int weight = Integer.parseInt(weightString);
        dbHelper.addWeighIn(weight, username);

        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new FragmentViewWeights());
        transaction.addToBackStack(null);
        transaction.commit();
    }
}