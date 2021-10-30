package com.example.samialavi.splash;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsersDelete extends Fragment {

    private EditText et_fname, et_lname, et_cnic, et_password;

    private Button reg_buttonreg;

    private String reg_fname, reg_lname, reg_cnic, reg_password, reg_user;
    private String doc_fname, doc_lname, admin_password;

    private Spinner spinner_user;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference adminRef = db.collection("Users").document("Admin").collection("Administrator").document("6110135271643");
    private CollectionReference userRef;

    private View v;

    public UsersDelete() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_users_delete, container, false);

        init();
        setSpinnerUser();

        reg_buttonreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeUser();
            }
        });

        return v;
    }

    private void init() {
        et_fname = (EditText) v.findViewById(R.id.rem_doc_fname);
        et_lname = (EditText) v.findViewById(R.id.rem_doc_lname);
        et_cnic = (EditText) v.findViewById(R.id.rem_doc_cnic);
        et_password = (EditText) v.findViewById(R.id.rem_doc_password);
        spinner_user = (Spinner) v.findViewById(R.id.spinner_user90);
        reg_buttonreg = (Button) v.findViewById(R.id.rem_doc_buttonreg);
    }

    private void setSpinnerUser() {

        ArrayAdapter<CharSequence> adapter_gender = ArrayAdapter.createFromResource(getContext(), R.array.remove, android.R.layout.simple_spinner_item);
        adapter_gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_user.setAdapter(adapter_gender);
        spinner_user.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                reg_user = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void removeUser() {
        reg_cnic = et_cnic.getText().toString();
        if (reg_cnic.length() != 13)
            et_cnic.setError("CNIC should be of 13 digits");
        else
            et_cnic.setError(null);

        reg_fname = et_fname.getText().toString();
        reg_lname = et_lname.getText().toString();
        reg_password = et_password.getText().toString();

        switch (reg_user) {
            case "Doctor":
                userRef = db.collection("Users").document("Doctor").collection("Doctors");
                break;
            case "Pharmacist":
                userRef = db.collection("Users").document("Pharmacist").collection("Pharmacists");
                break;
            case "Patient":
                userRef = db.collection("Users").document("Patient").collection("Patients");
                break;
        }

        if (reg_cnic.length() == 13) {
            userRef.document(reg_cnic).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                doc_fname = documentSnapshot.getString("firstname");
                                doc_lname = documentSnapshot.getString("lastname");
                            }
                        }
                    });

            adminRef.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                admin_password = documentSnapshot.getString("password");
                            } else {
                            }
                        }
                    });

            if (reg_fname.equals(doc_fname) && reg_lname.equals(doc_lname) && reg_password.equals(admin_password)) {
                userRef.document(reg_cnic).delete();
                switch (reg_user) {
                    case "Doctor":
                        Toast.makeText(getContext(), "Doctor Removed", Toast.LENGTH_SHORT).show();
                        break;
                    case "Pharmacist":
                        Toast.makeText(getContext(), "Pharmacist Removed", Toast.LENGTH_SHORT).show();
                        break;
                    case "Patient":
                        Toast.makeText(getContext(), "Patient Removed", Toast.LENGTH_SHORT).show();
                        break;
                }
            } else {
                Toast.makeText(getContext(), "Please provide correct details", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Please provide correct CNIC", Toast.LENGTH_SHORT).show();
        }
    }

}
