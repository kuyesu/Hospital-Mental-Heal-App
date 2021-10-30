package com.example.samialavi.splash;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorsPortalFragment extends Fragment {

    ArrayList<Button> acceptList = new ArrayList<>();
    ArrayList<Button> declineList = new ArrayList<>();
    ArrayList<Button> deleteList = new ArrayList<>();
    ArrayList<TextView> textViewList = new ArrayList<>();
    ArrayList<String> dataList = new ArrayList<>();
    ArrayList<String> timeList = new ArrayList<>();
    ArrayList<String> indexID = new ArrayList<>();
    ArrayList<String> patTimeList = new ArrayList<>();
    ArrayList<String> patIDList = new ArrayList<>();
    ArrayList<String> patCNICList = new ArrayList<>();
    ArrayList<String> appIDList = new ArrayList<>();
    int i = 0;
    int zzz = 0;
    int yyy = 0;
    int xxx = 0;
    private String doccnic, patcnic, cloud_spec, cloud_name, patID;
    private String time1, time2, t1;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    //private DocumentReference docRef=db.collection("Appointments").document("Doctors");
    private DocumentReference patRef = db.collection("Appointments").document("Patient");

    private View v;

    public DoctorsPortalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((MainActivity) getActivity()).setActionBarTitle("Doctor Portal");
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_doctors_portal, container, false);
        doccnic = getArguments().getString("CNIC");
        cloud_spec = getArguments().getString("Spec");
        cloud_name = getArguments().getString("Name");

        showDet();

        return v;
    }

    private void showDet() {
        patRef.collection("Patients").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    String data = "";
                    for (final QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                        String docname = documentSnapshot.getString("doctorname");
                        String speciality = documentSnapshot.getString("speciality");

                        if (docname.equals(cloud_name) && speciality.equals(cloud_spec)) {

                            String patmob = documentSnapshot.getString("patientcontact");
                            String patname = documentSnapshot.getString("patientname");
                            patcnic = documentSnapshot.getString("patientcnic");
                            time1 = documentSnapshot.getString("time");
                            String status = documentSnapshot.getString("status");
                            String appid = documentSnapshot.getId();

                            if (status.equals("Deleted"))
                                continue;

                            indexID.add(documentSnapshot.getId());

                            data = "\nPatient Name: " + patname +
                                    "\nPatient Contact: " + patmob +
                                    "\nAppoitnment Time: " + time1 +
                                    "\n";
                            dataList.add(data);
                            timeList.add(time1);
                            patCNICList.add(patcnic);
                            appIDList.add(appid);

                            LinearLayout layout = (LinearLayout) v.findViewById(R.id.linearlayout);

                            i++;
                            final TextView textView = new TextView(getContext());
                            textView.setId(1000 + i);
                            textView.setText(data.replace("\\n", "\n"));
                            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.FILL_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );

                            if (status.equals("Accepted"))
                                textView.setBackgroundColor(Color.parseColor("#7FFF00"));
                            else if (status.equals("Declined"))
                                textView.setBackgroundColor(Color.parseColor("#FF0000"));

                            layout.addView(textView, p);
                            textViewList.add(textView);

                            i++;
                            Button accept = new Button(getContext());
                            accept.setId(1000 + i);
                            accept.setText("Accept");
                            accept.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Map<String, Object> status = new HashMap<>();
                                    String data2;
                                    String index;
                                    String time;
                                    for (Button b : acceptList) {
                                        if (b.getId() == v.getId()) {
                                            int bid = b.getId();
                                            for (TextView t : textViewList) {
                                                if (t.getId() == (bid - 1)) {
                                                    if (xxx < 1) {
                                                        Toast.makeText(getContext(), "Press button one more time", Toast.LENGTH_SHORT).show();
                                                        xxx++;
                                                    } else {
                                                        data2 = dataList.get(textViewList.indexOf(t)) + "Appointment Status: Accepted\n";
                                                        t.setText(data2.replace("\\n", "\n"));
                                                        t.setBackgroundColor(Color.parseColor("#7FFF00"));
                                                        t.setTextColor(Color.parseColor("#000000"));
                                                    }
                                                    status.put("status", "Accepted");
                                                    time1 = timeList.get(textViewList.indexOf(t));
                                                    patcnic = patCNICList.get(textViewList.indexOf(t));

                                                    index = indexID.get(textViewList.indexOf(t));
                                                    //docRef.collection(doccnic).document(index).update(status);
                                                    patID = appIDList.get(textViewList.indexOf(t));
                                                    patRef.collection("Patients").document(patID).update(status);


                                                }
                                            }
                                        }
                                    }
                                }
                            });
                            layout.addView(accept, p);
                            acceptList.add(accept);

                            i++;
                            Button decline = new Button(getContext());
                            decline.setId(1000 + i);
                            decline.setText("Decline");
                            decline.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Map<String, Object> status = new HashMap<>();
                                    String data3;
                                    String index;
                                    String time;
                                    for (Button b : declineList) {
                                        if (b.getId() == v.getId()) {
                                            int bid = b.getId();
                                            for (TextView t : textViewList) {
                                                if (t.getId() == (bid - 2)) {
                                                    if (xxx < 1) {
                                                        Toast.makeText(getContext(), "Press button one more time", Toast.LENGTH_SHORT).show();
                                                        xxx++;
                                                    } else {
                                                        data3 = dataList.get(textViewList.indexOf(t)) + "Appointment Status: Declined\n";
                                                        t.setText(data3.replace("\\n", "\n"));
                                                        t.setBackgroundColor(Color.parseColor("#FF0000"));
                                                        t.setTextColor(Color.parseColor("#000000"));
                                                    }
                                                    time1 = timeList.get(textViewList.indexOf(t));
                                                    patcnic = patCNICList.get(textViewList.indexOf(t));

                                                    status.put("status", "Declined");

                                                    index = indexID.get(textViewList.indexOf(t));
                                                    //docRef.collection(doccnic).document(index).update(status);
                                                    patID = appIDList.get(textViewList.indexOf(t));
                                                    patRef.collection("Patients").document(patID).update(status);
                                                }
                                            }
                                        }
                                    }
                                }
                            });
                            layout.addView(decline, p);
                            declineList.add(decline);

                            i++;
                            Button delete = new Button(getContext());
                            delete.setId(1000 + i);
                            delete.setText("Delete");
                            delete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Map<String, Object> status = new HashMap<>();
                                    String data3;
                                    String index;
                                    String time;
                                    for (Button b : deleteList) {
                                        if (b.getId() == v.getId()) {
                                            int bid = b.getId();
                                            for (TextView t : textViewList) {
                                                if (t.getId() == (bid - 3)) {
                                                    if (xxx < 1) {
                                                        Toast.makeText(getContext(), "Press button one more time", Toast.LENGTH_SHORT).show();
                                                        xxx++;
                                                    } else {
                                                        data3 = dataList.get(textViewList.indexOf(t)) + "Appointment Status: Deleted\n";
                                                        t.setText(data3.replace("\\n", "\n"));
                                                        t.setBackgroundColor(Color.parseColor("#000000"));
                                                        t.setTextColor(Color.parseColor("#FFFFFF"));
                                                    }
                                                    time1 = timeList.get(textViewList.indexOf(t));
                                                    patcnic = patCNICList.get(textViewList.indexOf(t));

                                                    status.put("status", "Deleted");

                                                    index = indexID.get(textViewList.indexOf(t));
                                                    //docRef.collection(doccnic).document(index).update(status);

                                                    patID = appIDList.get(textViewList.indexOf(t));

                                                    patRef.collection("Patients").document(patID).update(status);
                                                }
                                            }
                                        }
                                    }
                                }
                            });
                            layout.addView(delete, p);
                            deleteList.add(delete);
                        } else {
                            continue;
                        }
                    }

                } else {
                    Toast.makeText(getContext(), "Cannot load doctors' data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


/*    private String getPatientID(String t1){
        System.out.println("AAAAA: Function Called");
        patRef.collection("Patients").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    patTimeList.clear();
                    patIDList.clear();
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                        time2=documentSnapshot.getString("time");
                        patTimeList.add(time2);
                        patIDList.add(documentSnapshot.getId());
                        System.out.println("AAAAA: "+time2);
                    }
                }
                else{
                    Toast.makeText(getContext(), "Cannot load patients' data", Toast.LENGTH_SHORT).show();
                }
            }
        });
        for (String t2:patTimeList){
            if (t2.equals(t1)){
                System.out.println("AAAAA: "+t1+"---"+t2+"---"+patTimeList.indexOf(t2));
                return patIDList.get(patTimeList.indexOf(t2));
            }
        }
        return " ";
    } */

}
