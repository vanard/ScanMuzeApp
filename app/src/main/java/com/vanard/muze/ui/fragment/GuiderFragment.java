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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.vanard.muze.R;
import com.vanard.muze.model.DataGuider;
import com.vanard.muze.util.Preferences;

import java.util.ArrayList;
import java.util.List;

public class GuiderFragment extends Fragment {
    private static final String TAG = "GuiderFragment";

    private static final String ARG_COLUMN_COUNT = "column-count";

    private List<DataGuider> mValues = new ArrayList<>();
    private FirebaseFirestore db;
    private ProgressDialog dialog;
    private GuiderRecyclerViewAdapter guiderRecyclerViewAdapter;
    private String museumName;

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

        db = FirebaseFirestore.getInstance();
        dialog = new ProgressDialog(requireContext());
        dialog.setMessage("Fetching data...");
        dialog.setCancelable(false);

        museumName = Preferences.getMuseumName(requireContext());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = view.getContext();
        RecyclerView recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayout.VERTICAL));

        guiderRecyclerViewAdapter = new GuiderRecyclerViewAdapter(mValues, context);
        recyclerView.setAdapter(guiderRecyclerViewAdapter);

        getData();
//        createGuider();
    }

    private void getData() {
        db.collection("guiders").whereEqualTo("museum", museumName).get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            DataGuider dataGuider = document.toObject(DataGuider.class);
                            if (dataGuider != null)
                                guiderRecyclerViewAdapter.addItem(dataGuider);
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
    }

    private void createGuider() {
        DataGuider a = new DataGuider("Sanoto", museumName, "0813555124");
        DataGuider b = new DataGuider("Fani", museumName, "0817555973");
        DataGuider c = new DataGuider("Musi", "Dieng Plateu Museum (Museum Kaliasa Dieng)", "0838555326");
        DataGuider d = new DataGuider("Sanusi", "Art: 1 New Museum", "0812555769");
        DataGuider e = new DataGuider("Vanya", museumName, "08530555147");
        DataGuider f = new DataGuider("Dila", museumName, "08123045956");

        db.collection("guiders").document().set(f)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG, "onComplete: " + task);
            }
        });
    }
}
