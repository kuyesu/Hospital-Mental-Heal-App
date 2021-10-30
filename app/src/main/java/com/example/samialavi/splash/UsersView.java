package com.example.samialavi.splash;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsersView extends Fragment {

    private Button img_docs, img_pats;
    private TextView user_docs, users_dets;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference patsRef = db.collection("Users").document("Patient").collection("Patients");
    private CollectionReference docsRef = db.collection("Users").document("Doctor").collection("Doctors");
    private CollectionReference pharmsRef = db.collection("Users").document("Pharmacist").collection("Pharmacists");

    int count;

    private View v;

    public UsersView() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_users_view, container, false);

        init();

        img_pats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPatients();
            }
        });

        img_docs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDoctors();
            }
        });

        return v;
    }

    private void loadPatients() {
        user_docs.setText("Patients");
        count = 0;

        patsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    String data = "";
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        String fname = documentSnapshot.getString("firstname");
                        String lname = documentSnapshot.getString("lastname");
                        String gender = documentSnapshot.getString("gender");
                        String dob = documentSnapshot.getString("dob");
                        String mobnum = documentSnapshot.getString("contact");
                        String cnicnum = documentSnapshot.getId();

                        data += "CNIC: " + cnicnum +
                                "\nFirst Name: " + fname +
                                "\nLast Name: " + lname +
                                "\nGender: " + gender +
                                "\nMobile Number: " + mobnum +
                                "\nDate of Birth: " + dob +
                                "\n" + "\n";

                        count++;

                    }
                    data += "Total Patients: " + count;
                    users_dets.setText(data.replace("\\n", "\n"));
                } else {
                    Toast.makeText(getContext(), "Cannot load patients' data", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void loadDoctors() {
        user_docs.setText("Doctors");
        count = 0;

        docsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    String data = "";
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        String fname = documentSnapshot.getString("firstname");
                        String lname = documentSnapshot.getString("lastname");
                        String speciality = documentSnapshot.getString("speciality");
                        String gender = documentSnapshot.getString("gender");
                        String dob = documentSnapshot.getString("dob");
                        String mobnum = documentSnapshot.getString("contact");
                        String cnicnum = documentSnapshot.getId();

                        if (speciality.equals("cardiology")) {
                            speciality = "Cardiology";
                        } else if (speciality.equals("dermatology")) {
                            speciality = "Dermatology";
                        } else if (speciality.equals("allergyandimmunology")) {
                            speciality = "Allergy and Immunology";
                        } else if (speciality.equals("infectiousdisease")) {
                            speciality = "Infectious Disease";
                        }

                        data += "CNIC: " + cnicnum +
                                "\nFirst Name: " + fname +
                                "\nLast Name: " + lname +
                                "\nSpeciality: " + speciality +
                                "\nGender: " + gender +
                                "\nMobile Number: " + mobnum +
                                "\nDate of Birth: " + dob +
                                "\n" + "\n";

                        count++;

                    }
                    data += "Total Doctors: " + count;
                    users_dets.setText(data.replace("\\n", "\n"));
                } else {
                    Toast.makeText(getContext(), "Cannot load doctors' data", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    private void init() {
        img_docs = (Button) v.findViewById(R.id.btn_get_doc);
        img_pats = (Button) v.findViewById(R.id.btn_get_pats);

        user_docs = (TextView) v.findViewById(R.id.text_getdoc1);
        users_dets = (TextView) v.findViewById(R.id.text_getdoc2);
    }

}
