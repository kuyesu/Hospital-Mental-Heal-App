package com.example.samialavi.splash;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsersAdd extends Fragment {

    private EditText et_fname, et_lname, et_mobnum, et_cnic, et_addr, et_password;

    private Spinner spinner_gender, spinner_speciality;

    private Button reg_buttonreg;

    private TextView mDisplayDate;
    private Calendar mCurrentCalendar;

    int year, month, day;
    String reg_fname, reg_lname, reg_mobnum, reg_cnic, reg_addr, reg_password, reg_gender, reg_dob, reg_speciality;

    private boolean state;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private View v;

    public UsersAdd() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_users_add, container, false);

        init();
        setDatePicker();
        setSpinnerGender();
        setSpinnerSpeciality();

        reg_buttonreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerDoctor();
            }
        });

        return v;
    }

    private void init() {
        et_fname = (EditText) v.findViewById(R.id.add_doc_fname);
        et_lname = (EditText) v.findViewById(R.id.add_doc_lname);
        et_mobnum = (EditText) v.findViewById(R.id.add_doc_mobnum);
        et_cnic = (EditText) v.findViewById(R.id.add_doc_cnic);
        et_addr = (EditText) v.findViewById(R.id.add_doc_addr);
        et_password = (EditText) v.findViewById(R.id.add_doc_password);

        spinner_gender = (Spinner) v.findViewById(R.id.spinner_gender_doc);
        spinner_speciality = (Spinner) v.findViewById(R.id.spinner_speciality_doc);

        reg_buttonreg = (Button) v.findViewById(R.id.add_doc_buttonreg);

        mDisplayDate = (TextView) v.findViewById(R.id.date_doc);
    }

    private void setDatePicker() {
        mCurrentCalendar = Calendar.getInstance();

        year = mCurrentCalendar.get(Calendar.YEAR);
        month = mCurrentCalendar.get(Calendar.MONTH);
        day = mCurrentCalendar.get(Calendar.DAY_OF_MONTH);


        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        mDisplayDate.setText(year + "-" + month + "-" + dayOfMonth);
                        reg_dob = year + "-" + month + "-" + dayOfMonth;
                    }
                }, year, month, day);
                datePickerDialog.getDatePicker().setMaxDate(mCurrentCalendar.getTimeInMillis());
                mCurrentCalendar.add(Calendar.YEAR, -100);
                datePickerDialog.getDatePicker().setMinDate(mCurrentCalendar.getTimeInMillis());
                mCurrentCalendar.add(Calendar.YEAR, 100);
                datePickerDialog.show();
            }
        });

    }

    private void setSpinnerGender() {

        ArrayAdapter<CharSequence> adapter_gender = ArrayAdapter.createFromResource(getContext(), R.array.gender, android.R.layout.simple_spinner_item);
        adapter_gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_gender.setAdapter(adapter_gender);
        spinner_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                reg_gender = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setSpinnerSpeciality() {

        ArrayAdapter<CharSequence> adapter_gender = ArrayAdapter.createFromResource(getContext(), R.array.speciality, android.R.layout.simple_spinner_item);
        adapter_gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_speciality.setAdapter(adapter_gender);
        spinner_speciality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    state = false;
                } else if (position == 2) {
                    state = true;
                    reg_speciality = "cardiology";
                } else if (position == 3) {
                    state = true;
                    reg_speciality = "dermatology";
                } else if (position == 1) {
                    state = true;
                    reg_speciality = "allergyandimmunology";
                } else if (position == 4) {
                    state = true;
                    reg_speciality = "infectiousdisease";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void registerDoctor() {
        reg_cnic = et_cnic.getText().toString();
        if (reg_cnic.length() != 13)
            et_cnic.setError("CNIC should be of 13 digits");
        else
            et_cnic.setError(null);


        reg_fname = et_fname.getText().toString();
        reg_lname = et_lname.getText().toString();
        reg_mobnum = et_mobnum.getText().toString();
        if (reg_mobnum.length() != 11)
            et_mobnum.setError("Mobile Number should be of 11 digits");
        else
            et_mobnum.setError(null);

        reg_addr = et_addr.getText().toString();
        reg_password = et_password.getText().toString();
        if (reg_password.length() > 4 && reg_password.length() < 11)
            et_password.setError(null);
        else
            et_password.setError("Password should be of 5 to 10 characters");


        Map<String, Object> doctor = new HashMap<>();
        doctor.put("firstname", reg_fname);
        doctor.put("lastname", reg_lname);
        doctor.put("gender", reg_gender);
        doctor.put("speciality", reg_speciality);
        doctor.put("dob", reg_dob);
        doctor.put("contact", reg_mobnum);
        doctor.put("address", reg_addr);
        doctor.put("password", reg_password);
        doctor.put("cnic", reg_cnic);

        if (reg_fname.isEmpty() || reg_lname.isEmpty() || reg_dob.equals("Select Date Here") || reg_addr.isEmpty()){
            Toast.makeText(getContext(), "Please provide all necessary information", Toast.LENGTH_SHORT).show();
            et_fname.setError("Enter first name");
            et_lname.setError("Enter last name");
            mDisplayDate.setError("");
            et_addr.setError("Enter address");
        }
        else{
            if (et_cnic.getText().length() == 13 && state) {
                if (reg_password.length() > 4 && reg_password.length() < 11) {
                    if (reg_mobnum.length() == 11) {
                        db.collection("Users").document("Doctor").collection("Doctors").document(reg_cnic).set(doctor)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), "Registration Error", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    } else {
                        Toast.makeText(getContext(), "Mobile Number should be of 11 digits", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Password should be of 5 to 10 characters", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Please provide correct information", Toast.LENGTH_SHORT).show();
        }

        }


    }

}
