package com.example.samialavi.splash;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorFragment extends Fragment {

    public Spinner spinner_speciality;

    private TextView textViewdoctors;

    private String spec = " ";

    private List<String> statename = null;
    private List<String> stategender = null;
    private List<String> statemob = null;

    private List<String> cardioname = new ArrayList<>();
    private List<String> cardiogender = new ArrayList<>();
    private List<String> dermname = new ArrayList<>();
    private List<String> dermgender = new ArrayList<>();
    private List<String> endoname = new ArrayList<>();
    private List<String> endogender = new ArrayList<>();
    private List<String> entname = new ArrayList<>();
    private List<String> entgender = new ArrayList<>();
    private List<String> cardiomob = new ArrayList<>();
    private List<String> dermmob = new ArrayList<>();
    private List<String> endomob = new ArrayList<>();
    private List<String> entmob = new ArrayList<>();

    private int pos;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference docsRef = db.collection("Users").document("Doctor").collection("Doctors");

    private View v;

    public DoctorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Setting Title
        ((MainActivity) getActivity()).setActionBarTitle("Doctors");
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_doctor, container, false);

        init();
        getDoctorsDetails();
        setSpinnerSpeciality();

        return v;
    }

    private void getDoctorsDetails() {
        docsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    String data = "";
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        String fname = documentSnapshot.getString("firstname");
                        String lname = documentSnapshot.getString("lastname");
                        String gender = documentSnapshot.getString("gender");
                        String speciality = documentSnapshot.getString("speciality");

                        if (speciality.equals("cardiology")) {
                            cardioname.add(fname + " " + lname);
                            cardiogender.add(gender);
                        } else if (speciality.equals("dermatology")) {
                            dermname.add(fname + " " + lname);
                            dermgender.add(gender);
                        } else if (speciality.equals("allergyandimmunology")) {
                            endoname.add(fname + " " + lname);
                            endogender.add(gender);
                        } else if (speciality.equals("infectiousdisease")) {
                            entname.add(fname + " " + lname);
                            entgender.add(gender);
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "Cannot load doctors' data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void init() {
        spinner_speciality = (Spinner) (v.findViewById(R.id.spinner_speciality));
        textViewdoctors = (TextView) v.findViewById(R.id.textviewdoctors);
    }

    private void setSpinnerSpeciality() {

        ArrayAdapter<CharSequence> adapter_speciality = ArrayAdapter.createFromResource(getContext(), R.array.speciality, android.R.layout.simple_spinner_item);
        adapter_speciality.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_speciality.setAdapter(adapter_speciality);
        spinner_speciality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
                if (position == 0) {
                    statename = new ArrayList<String>(Arrays.asList("Please select a speciality"));
                    textViewdoctors.setText(" ");
                } else if (position == 2) {
                    statename = cardioname;
                    stategender = cardiogender;
                    statemob = cardiomob;
                    spec = "Cardiology";
                } else if (position == 3) {
                    statename = dermname;
                    stategender = dermgender;
                    statemob = dermmob;
                    spec = "Dermatology";
                } else if (position == 1) {
                    statename = endoname;
                    stategender = endogender;
                    statemob = endomob;
                    spec = "Allergy and Immunology";
                } else if (position == 4) {
                    statename = entname;
                    stategender = entgender;
                    statemob = entmob;
                    spec = "Infectious Disease";
                }
                setDocImages();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setDocImages() {
        if (pos == 0) {
            Toast.makeText(getContext(), "Please select a speciality", Toast.LENGTH_SHORT).show();
        } else {
            String data = "";
            textViewdoctors.setText(data);
            for (int i = 0; i < statename.size(); i++) {
                data += "\nDoctor: " + statename.get(i) +
                        "\nGender: " + stategender.get(i) +
                        "\nSpeciality: " + spec +
                        "\n";
            }
            textViewdoctors.setText(data.replace("\\n", "\n"));

        }
    }

}
