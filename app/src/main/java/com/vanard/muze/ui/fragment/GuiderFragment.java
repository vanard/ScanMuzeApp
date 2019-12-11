package com.vanard.muze.ui.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;
import com.vanard.muze.R;
import com.vanard.muze.model.DataGuider;

import java.util.ArrayList;
import java.util.List;

public class GuiderFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";

    private List<DataGuider> mValues = new ArrayList<>();
    private FirebaseFirestore db;
    private ProgressDialog dialog;
    private GuiderRecyclerViewAdapter guiderRecyclerViewAdapter;

    public GuiderFragment() {
    }


    public static GuiderFragment newInstance(int columnCount) {
        GuiderFragment fragment = new GuiderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guider_list, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
