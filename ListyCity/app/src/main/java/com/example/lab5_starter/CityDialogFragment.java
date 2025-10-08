package com.example.lab5_starter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

public class CityDialogFragment extends DialogFragment {
    interface CityDialogListener {
        void updateCity(City city, String title, String year);
        void addCity(City city);
        void deleteCity(City city);
    }
    private CityDialogListener listener;

    public static CityDialogFragment newInstance(City city){
        Bundle args = new Bundle();
        args.putSerializable("City", city);

        CityDialogFragment fragment = new CityDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof CityDialogListener){
            listener = (CityDialogListener) context;
        }
        else {
            throw new RuntimeException("Implement listener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_city_details, null);
        EditText editCityName = view.findViewById(R.id.edit_city_name);
        EditText editProvince = view.findViewById(R.id.edit_province);

        String tag = getTag();
        Bundle bundle = getArguments();
        City city;

        if (Objects.equals(tag, "City Details") && bundle != null) {
            city = (City) bundle.getSerializable("City");
            assert city != null;
            editCityName.setText(city.getName());
            editProvince.setText(city.getProvince());
        } else {
            city = null;
        }

        AlertDialog dialog = new AlertDialog.Builder(getContext())
            .setView(view)
            .setTitle("City Details")
            .setNegativeButton("Cancel", null)
            .setNeutralButton("Delete", (dialog1, which) ->
                listener.deleteCity(city)
            )
            .setPositiveButton("Continue", null)
            .create();
        dialog.setOnShowListener(d -> {
            Button ok = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            ok.setOnClickListener(v -> {
                String cityName = editCityName.getText().toString().strip();
                if (cityName.isEmpty()) {
                    Toast.makeText(getContext(), "City name is empty.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String province = editProvince.getText().toString().strip();
                if (province.isEmpty()) {
                    Toast.makeText(getContext(), "Province is empty.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Objects.equals(tag, "City Details")) {
                    listener.updateCity(city, cityName, province);
                } else {
                    listener.addCity(new City(cityName, province));
                }
                dialog.dismiss();
            });
        });
        return dialog;
    }
}
