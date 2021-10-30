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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddMedicines extends Fragment {

    private TextView t1;
    private String medname,meddesc,price,quantity,data,quantity1;
    private Spinner spinner_medicine;

    private EditText e1;
    private Button b1;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference md=db.collection("Medicines");

    private View v;


    public AddMedicines() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((MainActivity) getActivity()).setActionBarTitle("Pharmacist Portal");
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_add_medicines, container, false);
        //patcnic = getArguments().getString("CNIC");

        init();
        setSpinner();

        return v;
    }

    private void init(){
        t1=(TextView)v.findViewById(R.id.medicine_name);
        e1=(EditText)v.findViewById(R.id.med_e1);
        b1=(Button)v.findViewById(R.id.med_btn);
        spinner_medicine=(Spinner)v.findViewById(R.id.spinner_medicine);
    }

    private void setSpinner(){
        ArrayAdapter<CharSequence> adapter_gender = ArrayAdapter.createFromResource(getContext(), R.array.meds, android.R.layout.simple_spinner_item);
        adapter_gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_medicine.setAdapter(adapter_gender);
        spinner_medicine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                medname = parent.getItemAtPosition(position).toString();
                setTextView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setTextView(){
        if (medname.equals("Lipitor")){
            meddesc="Cholestrol-lowering statin drug";
            price="$7.2 billion";
        }
        if (medname.equals("Nexium")){
            meddesc="Antacid drug";
            price="$6.3 billion";
        }
        if (medname.equals("Plavix")){
            meddesc="Blood thinner";
            price="$6.1 billion";
        }
        if (medname.equals("Avair Diskus")){
            meddesc="Asthma Inhaler";
            price="$4.7 billion";
        }
        if (medname.equals("Abilify")){
            meddesc="Antipsychotic drug";
            price="$4.6 billion";
        }
        md.document(medname).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                quantity = documentSnapshot.getString("quantity");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                quantity=" ";
            }
        });

        data = "Medicine: " + medname +
                "\nPrice per med: " + price +
                "\nDescription: " + meddesc +
                "\nQuantity: " + quantity +
                "\n";

        t1.setText(data.replace("\\n", "\n"));

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity1=e1.getText().toString();
                if (quantity1.isEmpty()){
                    Toast.makeText(getContext(), "Enter quantity to set", Toast.LENGTH_SHORT).show();
                }
                else{
                    Map<String,Object> med=new HashMap<>();
                    med.put("quantity",quantity1);
                    data = "Medicine: " + medname +
                            "\nPrice per med: " + price +
                            "\nDescription: " + meddesc +
                            "\nQuantity: " + quantity1 +
                            "\n";

                    md.document(medname).set(med);

                    t1.setText(data.replace("\\n", "\n"));
                }

            }
        });


    }
}
