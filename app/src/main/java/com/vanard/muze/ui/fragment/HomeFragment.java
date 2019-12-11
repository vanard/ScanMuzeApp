package com.vanard.muze.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.vanard.muze.R;
import com.vanard.muze.model.museum.MuseumCheckIn;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";

    private static final String ARG_COLUMN_COUNT = "column-count";

    private List<MuseumCheckIn> mValues = new ArrayList<>();
    private FirebaseFirestore db;
    private ProgressDialog dialog;
    private HomeRecyclerViewAdapter homeRecyclerViewAdapter;

    public HomeFragment() {
    }

    public static HomeFragment newInstance(int columnCount) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_list, container, false);

        db = FirebaseFirestore.getInstance();
        dialog = new ProgressDialog(requireContext());
        dialog.setMessage("Fetching data...");
        dialog.setCancelable(false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = view.findViewById(R.id.list);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayout.VERTICAL));

            homeRecyclerViewAdapter = new HomeRecyclerViewAdapter(mValues);
            recyclerView.setAdapter(homeRecyclerViewAdapter);
//        }

        getData();
    }

    private void getData() {
        Query a = db.collection("museum").orderBy("countCheckIn", Query.Direction.DESCENDING).limit(5);
        a.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    dialog.dismiss();
                    return;
                }

                for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                    if (doc != null && doc.exists()) {
                        MuseumCheckIn museumCheckIn = doc.toObject(MuseumCheckIn.class);

                        homeRecyclerViewAdapter.addItem(museumCheckIn);
                    }
                }

            }
        });
    }
}
