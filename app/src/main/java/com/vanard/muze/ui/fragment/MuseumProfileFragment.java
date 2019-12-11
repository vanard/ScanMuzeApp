package com.vanard.muze.ui.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.vanard.muze.R;
import com.vanard.muze.model.museum.DataItem;
import com.vanard.muze.model.museum.DataMuseum;
import com.vanard.muze.network.RetrofitClient;
import com.vanard.muze.network.RetrofitService;
import com.vanard.muze.util.Preferences;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MuseumProfileFragment extends Fragment {
    private static final String TAG = "MuseumProfileFragment";

    private RetrofitService retrofitClient;
    private String museumId, museumName;
    private TextView museumView, descriptionView;
    private ProgressDialog dialog;

    public MuseumProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_museum_profile, container, false);

        bindView(view);

        dialog = new ProgressDialog(requireContext());
        dialog.setMessage("Fetching data...");
        dialog.setCancelable(true);

        museumId = Preferences.getMuseumId(requireContext());
        museumName = Preferences.getMuseumName(requireContext());

        return view;
    }

    private void bindView(View view) {
        museumView = view.findViewById(R.id.museum_name_text);
        descriptionView = view.findViewById(R.id.description_text);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRetrofit();

        requestData();
    }

    private void requestData() {
        dialog.show();
        Call<DataMuseum> museumCall = retrofitClient.getSearchMuseumByName(museumName);
        museumCall.enqueue(new Callback<DataMuseum>() {
            @Override
            public void onResponse(Call<DataMuseum> call, Response<DataMuseum>
                    response) {
                List<DataItem> dataItems = null;
                if (response.body() != null) {
                    dataItems = response.body().getData();

                    for (DataItem item : dataItems) {
                        if (item.getMuseumId().equals(museumId)) {
                            setInitData(item);
                            break;
                        }
                    }
                } else
                    Log.d(TAG, "onResponse: null");

                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<DataMuseum> call, Throwable t) {
                Log.e("Retrofit Get", t.toString());
                dialog.dismiss();
            }
        });
    }

    private void setInitData(DataItem dataItem) {
        dialog.dismiss();
        String description;

        if (dataItem.getTahunBerdiri().equals("0")) {
            description = dataItem.getAlamatJalan() + "\n Desa: " +
                    dataItem.getDesaKelurahan() + "\n Kecamatan: " +
                    dataItem.getKecamatan() + "\n Kabupaten: " +
                    dataItem.getKabupatenKota() + "\n Provinsi: " +
                    dataItem.getPropinsi();
        }
        else {
            description = dataItem.getAlamatJalan() + "\n Desa: " +
                    dataItem.getDesaKelurahan() + "\n Kecamatan: " +
                    dataItem.getKecamatan() + "\n Kabupaten: " +
                    dataItem.getKabupatenKota() + "\n Provinsi: " +
                    dataItem.getPropinsi() + "\n Berdiri Tahun: " +
                    dataItem.getTahunBerdiri();
        }

        museumView.setText(dataItem.getNama());
        descriptionView.setText(description);


    }

    private void initRetrofit() {
        RetrofitClient.clearClient();
        retrofitClient = RetrofitClient.getRetrofitInstance(RetrofitClient.BASE_URL).create(RetrofitService.class);
    }
}
