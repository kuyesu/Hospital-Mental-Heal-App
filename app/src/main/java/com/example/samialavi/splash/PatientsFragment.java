package com.example.samialavi.splash;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


/**
 * A simple {@link Fragment} subclass.
 */
public class PatientsFragment extends Fragment {

    private TextView t;
    private String patcnic;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference patRef = db.collection("Appointments").document("Patient");

    private View v;

    public PatientsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((MainActivity) getActivity()).setActionBarTitle("Patient Portal");
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_patients, container, false);
        patcnic = getArguments().getString("CNIC");

        showDet();

        return v;
    }

    private void showDet() {
        patRef.collection("Patients").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    String data = "";
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        String patientcnic = documentSnapshot.getString("patientcnic");

                        if (patcnic.equals(patientcnic)) {
                            String docname = documentSnapshot.getString("doctorname");
                            String mobnum = documentSnapshot.getString("doctorcontact");
                            String status = documentSnapshot.getString("status");
                            String time = documentSnapshot.getString("time");

                            data = "\nDr. " + docname +
                                    "\nDoctor Contact: " + mobnum +
                                    "\nAppointment Time: " + time +
                                    "\nAppointment Status: " + status +
                                    "\n";

                            LinearLayout layout = (LinearLayout) v.findViewById(R.id.linearlayout2);

                            final TextView textView = new TextView(getContext());
                            textView.setText(data.replace("\\n", "\n"));
                            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.FILL_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );

                            if (status.equals("Accepted"))
                                textView.setBackgroundColor(Color.parseColor("#7FFF00"));
                            else if (status.equals("Declined"))
                                textView.setBackgroundColor(Color.parseColor("#FF0000"));
                            else if (status.equals("Deleted")) {
                                textView.setBackgroundColor(Color.parseColor("#000000"));
                                textView.setTextColor(Color.parseColor("#FFFFFF"));
                            }

                            layout.addView(textView, p);

                        } else {
                            continue;
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "Cannot load patients' data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
