package com.snhu.cs360_project_miller;

import static java.lang.String.valueOf;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.zip.Inflater;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentItemCard#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentItemCard extends Fragment {

    private static final String ARG_ID = "_id";
    private static final String ARG_DATE = "date";
    private static final String ARG_WEIGHT = "weight";
    private Button buttonEdit;
    private Button buttonDelete;

    private long entryID;
    private String entryDate;
    private int entryWeight;

    public FragmentItemCard() {
        // Required empty public constructor
    }


    public static FragmentItemCard newInstance(long ARG_ID, String ARG_DATE, int ARG_WEIGHT) {
        FragmentItemCard fragment = new FragmentItemCard();
        Bundle args = new Bundle();
        args.putLong("ARG_ID", ARG_ID);
        args.putString("ARG_DATE", ARG_DATE);
        args.putFloat("ARG_WEIGHT", ARG_WEIGHT);

        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_card, container, false);
        buttonEdit = view.findViewById(R.id.buttonEdit);
        buttonDelete = view.findViewById(R.id.buttonDelete);

        if (getArguments() != null) {
            entryID = getArguments().getLong(ARG_ID);
            entryDate = getArguments().getString(ARG_DATE);
            entryWeight = getArguments().getInt(ARG_WEIGHT);
        }

        TextView textDate = view.findViewById(R.id.textDateValue);
        TextView textWeight = view.findViewById(R.id.textWeightValue);

        textDate.setText(entryDate);
        textWeight.setText(valueOf(entryWeight));

        return view;
    }

}