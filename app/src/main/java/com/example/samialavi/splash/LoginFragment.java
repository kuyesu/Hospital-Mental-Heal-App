package com.example.samialavi.splash;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private Spinner spinner_user;

    private Button et_log_buttonlog;

    private EditText et_cnic, et_password;

    private String log_user, log_cnic, log_password, cloud_password, speciality, name;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private View v;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Setting Title
        ((MainActivity) getActivity()).setActionBarTitle("Login");
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_login, container, false);

        init();
        setSpinnerUser();
        setButtonLogin();

        return v;
    }

    private void setSpinnerUser() {

        ArrayAdapter<CharSequence> adapter_gender = ArrayAdapter.createFromResource(getContext(), R.array.user, android.R.layout.simple_spinner_item);
        adapter_gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_user.setAdapter(adapter_gender);
        spinner_user.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                log_user = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void init() {
        spinner_user = (Spinner) v.findViewById(R.id.spinner_user);
        et_log_buttonlog = (Button) v.findViewById(R.id.et_log_buttonlog);
        et_cnic = (EditText) v.findViewById(R.id.et_log_cnic);
        et_password = (EditText) v.findViewById(R.id.et_log_password);
    }

    private void setButtonLogin() {
        et_log_buttonlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private void loginUser() {
        if (et_cnic.getText().length() == 13) {
            log_cnic = et_cnic.getText().toString();
            et_cnic.setError(null);

            switch (log_user) {
                case "Administrator":
                    db.collection("Users").document("Admin").collection("Administrator").document(log_cnic).get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()) {
                                        cloud_password = documentSnapshot.getString("password");
                                        log_password = et_password.getText().toString();
                                        if (checkPassword()) {
                                            createAdminActivity();
                                        }
                                    } else {
                                        Toast.makeText(getContext(), "User does not exist", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Error reaching database", Toast.LENGTH_SHORT).show();
                                }
                            });
                    break;
                case "Doctor":
                    db.collection("Users").document("Doctor").collection("Doctors").document(log_cnic).get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()) {
                                        cloud_password = documentSnapshot.getString("password");
                                        log_password = et_password.getText().toString();
                                        if (checkPassword()) {
                                            speciality = documentSnapshot.getString("speciality");
                                            String fname = documentSnapshot.getString("firstname");
                                            String lname = documentSnapshot.getString("lastname");
                                            name = fname + " " + lname;
                                            createDoctorsPortalFragment();
                                        }
                                    } else {
                                        Toast.makeText(getContext(), "User does not exist", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Error reaching database", Toast.LENGTH_SHORT).show();
                                }
                            });
                    break;
                case "Patient":
                    db.collection("Users").document("Patient").collection("Patients").document(log_cnic).get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()) {
                                        cloud_password = documentSnapshot.getString("password");
                                        log_password = et_password.getText().toString();
                                        if (checkPassword()) {
                                            createPatientFragment();
                                        }
                                    } else {
                                        Toast.makeText(getContext(), "User does not exist", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Error reaching database", Toast.LENGTH_SHORT).show();
                                }
                            });
                    break;
                case "Pharmacist":
                    db.collection("Users").document("Pharmacist").collection("Pharmacists").document(log_cnic).get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()) {
                                        cloud_password = documentSnapshot.getString("password");
                                        log_password = et_password.getText().toString();
                                        if (checkPassword()) {
                                            createPharmacistFragment();
                                        }
                                    } else {
                                        Toast.makeText(getContext(), "User does not exist", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Error reaching database", Toast.LENGTH_SHORT).show();
                                }
                            });
                    break;
            }
        } else {
            et_cnic.setError("CNIC should be of 13 digits");
            Toast.makeText(getContext(), "Please provide correct CNIC", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean checkPassword() {
        if (cloud_password.equals(log_password)) {
            Toast.makeText(getContext(), "Login Successful", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(getContext(), "Incorrect Password", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void createAdminActivity() {
        Intent intent = new Intent(getActivity(), AdministratorActivity.class);
        startActivity(intent);
    }

    private void createPatientFragment() {
        Fragment fr = new PatientsFragment();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        Bundle args = new Bundle();
        args.putString("CNIC", log_cnic);
        fr.setArguments(args);
        ft.replace(R.id.fl_main, fr);
        ft.commit();
    }

    private void createDoctorsPortalFragment() {
        Fragment fr = new DoctorsPortalFragment();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        Bundle args = new Bundle();
        args.putString("CNIC", log_cnic);
        args.putString("Spec", speciality);
        args.putString("Name", name);
        fr.setArguments(args);
        ft.replace(R.id.fl_main, fr);
        ft.commit();
    }

    private void createPharmacistFragment() {
        Fragment fr = new AddMedicines();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_main, fr);
        ft.commit();
    }


}
