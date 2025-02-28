package com.snhu.cs360_project_miller;



import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class FragmentViewWeights extends Fragment {
    private weightTrackerDBHelper wDatabaseHelper;
    private RecyclerView recyclerView;
    private loginDBHelper loginDB;
    private GoalWeightEntry goalEntry;
    private goalWeightDBHelper gDatabaseHelper;

    public FragmentViewWeights() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_weights, container, false);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        wDatabaseHelper = weightTrackerDBHelper.getInstance(getContext());
        loginDB = loginDBHelper.getInstance(getContext());
        String username = loginDBHelper.getCurrentEmail();
        gDatabaseHelper = goalWeightDBHelper.getInstance(getContext());

        goalEntry = gDatabaseHelper.getGoalWeight(username);
        int GOAL_WEIGHT = goalEntry.getGoalWeight();

        recyclerView = view.findViewById(R.id.recyclerView);

        FloatingActionButton fabAdd = view.findViewById(R.id.fabAdd);
        Button buttonSettings = view.findViewById(R.id.buttonSettings);

        fabAdd.setOnClickListener(v -> {
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new FragmentAddWeight());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        buttonSettings.setOnClickListener(v -> {
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new FragmentSetting());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        // Set Layout Manager
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load weight entries
        loadWeightEntries();

        // Check goal weight and send SMS if necessary
        boolean smsEnabled = sharedPreferences.getBoolean(username + "_sms", false);
        if (smsEnabled){
            checkGoalAndSendSMS(GOAL_WEIGHT);
        }

        return view;
    }

    private void loadWeightEntries() {
        String username = loginDBHelper.getCurrentEmail();
        List<WeightEntry> weightEntries = wDatabaseHelper.getWeightEntries(username);

        // Debug logging to check if database has data
        Log.d("FragmentViewWeights", "Entries loaded: " + weightEntries.size());
        if (weightEntries.isEmpty()) {
            Log.d("FragmentViewWeights", "No weight entries found in database.");
        }

        WeightEntryAdapter adapter = new WeightEntryAdapter(getContext(), weightEntries);
        recyclerView.setAdapter(adapter);
    }

    private void checkGoalAndSendSMS(int GOAL_WEIGHT) {
        String username = loginDBHelper.getCurrentEmail();
        List<WeightEntry> weightEntries = wDatabaseHelper.getWeightEntries(username);

        if (!weightEntries.isEmpty()) {
            int latestWeight = weightEntries.get(weightEntries.size() - 1).getWeight();
            Log.d("FragmentViewWeights", "Latest weight: " + latestWeight + ", Goal weight: " + GOAL_WEIGHT);

            // Check if SMS has already been sent
            if (goalReachedAlreadyNotified()) {
                Log.d("FragmentViewWeights", "Goal already reached previously. No SMS sent.");
                return; // Exit the function early to prevent duplicate messages
            }

            if (latestWeight <= GOAL_WEIGHT) {
                Log.d("FragmentViewWeights", "Goal reached! Sending SMS.");
                sendSMSMessage("Congratulations! You've reached your goal weight of " + GOAL_WEIGHT + " lbs.");
                Toast.makeText(getContext(), "Congratulations! You've reached your goal weight of " + GOAL_WEIGHT + " lbs.", Toast.LENGTH_LONG).show();

                // Mark SMS as sent
                setGoalReachedNotified(true);
            } else {
                Log.d("FragmentViewWeights", "Goal not yet reached.");
            }
        } else {
            Log.d("FragmentViewWeights", "No weight entries found.");
        }
    }

    // Check if SMS was already sent
    private boolean goalReachedAlreadyNotified() {
        return requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                .getBoolean("goal_reached_sent", false);
    }

    // Mark that SMS was sent
    private void setGoalReachedNotified(boolean value) {
        requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                .edit()
                .putBoolean("goal_reached_sent", value)
                .apply();
    }


    private void sendSMSMessage(String message) {
        try {
            String phoneNumber = "+15555215554"; // Default emulator number

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Log.d("FragmentViewWeights", "SMS sent to: " + phoneNumber);
        } catch (Exception e) {
            Log.e("FragmentViewWeights", "SMS sending failed", e);
        }
    }
}