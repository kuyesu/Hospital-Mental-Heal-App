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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;


/**
 * A simple {@link Fragment} subclass.
 */
public class AppointmentFragment extends Fragment {

    public Spinner spinner_speciality;
    public Spinner spinner_doctor;
    public Spinner spinner_schedule;

    public TextView mDisplayStartDate;
    public TextView mDisplayEndDate;
    public Calendar mCurrentCalendar1, mCurrentCalendar2;
    public TextView confirmation;
    public TextView CNICnumber;
    public TextView pwd;
    public TextView text_schedule;

    public EditText cnic;
    public EditText password;

    public Button view_schedule;
    public Button next;
    public Button confirm;
    public int y, m, d;
    String startdate, enddate;
    Date date1, date2;
    private Vector<String> schedule = new Vector<String>();
    private List<String> cardioname = new ArrayList<>();
    private List<String> cardiocnic = new ArrayList<>();
    private List<String> dermname = new ArrayList<>();
    private List<String> dermcnic = new ArrayList<>();
    private List<String> endoname = new ArrayList<>();
    private List<String> endocnic = new ArrayList<>();
    private List<String> entname = new ArrayList<>();
    private List<String> entcnic = new ArrayList<>();
    private List<String> cardiomob = new ArrayList<>();
    private List<String> dermmob = new ArrayList<>();
    private List<String> endomob = new ArrayList<>();
    private List<String> entmob = new ArrayList<>();
    private List<String> state = null;
    private String timings[];
    private String docname, doccnic, docmob, patmob;
    private String text_cnic, cloud_password;
    private String text_password;
    private String time, name_pat;
    private String speciality;
    private int year1, month1, day1, year2, month2, day2;
    private int difference;
    private int pos, index;
    private int xx = 0, yy = 0;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference docsRef = db.collection("Users").document("Doctor").collection("Doctors");

    private View v;

    public AppointmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Setting Title
        ((MainActivity) getActivity()).setActionBarTitle("Appointment");
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_appointment, container, false);

        timings = new String[]{"11:30:00", "12:00:00", "12:30:00", "13:00:00"};

        init();
        setDateStart();
        setDateEnd();
        getDoctorsDetails();
        setSpinnerSpeciality();
        setButtonAppointment();

        return v;
    }

    private void init() {
        spinner_speciality = (Spinner) (v.findViewById(R.id.spinner_speciality));
        spinner_doctor = (Spinner) (v.findViewById(R.id.spinner_doctor));
        spinner_schedule = (Spinner) v.findViewById(R.id.spinner_schedule);

        mDisplayStartDate = (TextView) v.findViewById(R.id.date_start);
        mDisplayEndDate = (TextView) v.findViewById(R.id.date_end);
        text_schedule = (TextView) v.findViewById(R.id.text_schedule);
        confirmation = (TextView) v.findViewById(R.id.text_confirmation);
        CNICnumber = (TextView) v.findViewById(R.id.text_cnic);
        pwd = (TextView) v.findViewById(R.id.text_password);

        view_schedule = (Button) v.findViewById(R.id.view_schedule);
        next = (Button) v.findViewById(R.id.btn_next);
        confirm = (Button) v.findViewById(R.id.btn_confirm);

        cnic = (EditText) v.findViewById(R.id.edittext_cnic);
        password = (EditText) v.findViewById(R.id.edittext_password);

        mCurrentCalendar1 = Calendar.getInstance();

        mCurrentCalendar2 = Calendar.getInstance();

    }

    private void makeInvisible() {
        text_schedule.setVisibility(View.INVISIBLE);
        spinner_schedule.setVisibility(View.INVISIBLE);
        next.setVisibility(View.INVISIBLE);
        text_schedule.setVisibility(View.INVISIBLE);
        spinner_schedule.setVisibility(View.INVISIBLE);
        confirmation.setVisibility(View.INVISIBLE);
        CNICnumber.setVisibility(View.INVISIBLE);
        cnic.setVisibility(View.INVISIBLE);
        pwd.setVisibility(View.INVISIBLE);
        password.setVisibility(View.INVISIBLE);
        confirm.setVisibility(View.INVISIBLE);
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
                        String mobnum = documentSnapshot.getString("contact");
                        String gender = documentSnapshot.getString("gender");
                        String speciality = documentSnapshot.getString("speciality");
                        String cnicnum = documentSnapshot.getId();

                        if (speciality.equals("cardiology")) {
                            cardioname.add(fname + " " + lname);
                            cardiocnic.add(cnicnum);
                            cardiomob.add(mobnum);
                        }
                        if (speciality.equals("dermatology")) {
                            dermname.add(fname + " " + lname);
                            dermcnic.add(cnicnum);
                            dermmob.add(mobnum);
                        }
                        if (speciality.equals("allergyandimmunology")) {
                            endoname.add(fname + " " + lname);
                            endocnic.add(cnicnum);
                            endomob.add(mobnum);
                        }
                        if (speciality.equals("infectiousdisease")) {
                            entname.add(fname + " " + lname);
                            entcnic.add(cnicnum);
                            entmob.add(mobnum);
                        }
                    }

                } else {
                    Toast.makeText(getContext(), "Cannot load doctors' data", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                    state = new ArrayList<String>(Arrays.asList(" "));
                } else if (position == 2) {
                    state = cardioname;
                    speciality = "cardiology";
                } else if (position == 3) {
                    state = dermname;
                    speciality = "dermatology";
                } else if (position == 1) {
                    state = endoname;
                    speciality = "allergyandimmunology";
                } else if (position == 4) {
                    state = entname;
                    speciality = "infectiousdisease";
                }
                setSpinnerDoctor();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setSpinnerDoctor() {

        ArrayAdapter<String> adapter_doctor = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, state);
        spinner_doctor.setAdapter(adapter_doctor);
        spinner_doctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                docname = parent.getItemAtPosition(position).toString();
                index = state.indexOf(docname);
                if (pos == 2) {
                    doccnic = cardiocnic.get(index);
                    docmob = cardiomob.get(index);
                }
                if (pos == 3) {
                    doccnic = dermcnic.get(index);
                    docmob = dermmob.get(index);
                }
                if (pos == 1) {
                    doccnic = endocnic.get(index);
                    docmob = endomob.get(index);
                }
                if (pos == 4) {
                    doccnic = entcnic.get(index);
                    docmob = entmob.get(index);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setButtonAppointment() {

        view_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pos != 0) {
                    if (startdate != null && enddate != null) {
                        difference = Integer.parseInt(differenceDates(startdate, enddate));
                        if (difference >= 0) {
                            text_schedule.setVisibility(View.VISIBLE);
                            spinner_schedule.setVisibility(View.VISIBLE);
                            setSpinnerSchedule();
                            setButtonNext();
                        } else {
                            Toast.makeText(getContext(), "No schedules found", Toast.LENGTH_SHORT).show();
                            makeInvisible();
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "Please fill all details", Toast.LENGTH_SHORT).show();
                    next.setVisibility(View.INVISIBLE);
                    text_schedule.setVisibility(View.INVISIBLE);
                    spinner_schedule.setVisibility(View.INVISIBLE);
                }

            }
        });
    }

    private void setSpinnerSchedule() {
        schedule.clear();

        for (int i = 0; i < (difference + 1); i++) {
            for (int j = 0; j < timings.length; j++) {
                String addi = y + "-" + m + "-" + (d + i) + " " + timings[j];
                schedule.add(addi);
            }
        }

        ArrayAdapter<String> adapter_schedule = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, schedule);
        spinner_schedule.setAdapter(adapter_schedule);
        spinner_schedule.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                time = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setButtonNext() {
        next.setVisibility(View.VISIBLE);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonConfirm();
            }
        });
    }

    private void setButtonConfirm() {
        confirm.setVisibility(View.VISIBLE);
        confirmation.setVisibility(View.VISIBLE);
        CNICnumber.setVisibility(View.VISIBLE);
        pwd.setVisibility(View.VISIBLE);
        cnic.setVisibility(View.VISIBLE);
        password.setVisibility(View.VISIBLE);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validatePatient();
            }
        });
    }

    private void validatePatient() {
        text_cnic = cnic.getText().toString();
        db.collection("Users").document("Patient").collection("Patients").document(text_cnic).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            name_pat = documentSnapshot.getString("firstname") + " " +
                                    documentSnapshot.getString("lastname");
                            patmob = documentSnapshot.getString("contact");
                            cloud_password = documentSnapshot.getString("password");
                            text_password = password.getText().toString();
                            if (checkPassword()) {
                                Map<String, Object> appointment = new HashMap<>();
                                appointment.put("doctorname", docname);
                                appointment.put("patientname", name_pat);
                                appointment.put("patientcnic", text_cnic);
                                appointment.put("patientcontact", patmob);
                                appointment.put("speciality", speciality);
                                appointment.put("time", time);
                                appointment.put("doctorcontact", docmob);
                                appointment.put("status", " ");
                                db.collection("Appointments").document("Patient").collection("Patients").document().set(appointment);
                                //appointment.put("doctorcnic",doccnic);
                                //db.collection("Appointments").document("Doctors").collection(doccnic).document().set(appointment);
                                Toast.makeText(getContext(), "Appointment Successful", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Please register first", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean checkPassword() {
        if (cloud_password.equals(text_password)) {
            return true;
        } else {
            Toast.makeText(getContext(), "Incorrect Password", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void setDateStart() {

        year1 = mCurrentCalendar1.get(Calendar.YEAR);
        month1 = mCurrentCalendar1.get(Calendar.MONTH);
        day1 = mCurrentCalendar1.get(Calendar.DAY_OF_MONTH);

        mDisplayStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        startdate = year + "-" + (month + 1) + "-" + dayOfMonth;
                        mDisplayStartDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                        y = year;
                        m = month + 1;
                        d = dayOfMonth;
                    }
                }, year1, month1, day1);
                datePickerDialog.getDatePicker().setMinDate(mCurrentCalendar1.getTimeInMillis());
                mCurrentCalendar1.add(Calendar.MONTH, +1);
                mCurrentCalendar1.add(Calendar.DAY_OF_MONTH, -day1);
                datePickerDialog.getDatePicker().setMaxDate(mCurrentCalendar1.getTimeInMillis());
                mCurrentCalendar1.add(Calendar.MONTH, -1);
                mCurrentCalendar1.add(Calendar.DAY_OF_MONTH, day1);
                datePickerDialog.show();
            }
        });
    }

    private void setDateEnd() {

        year2 = mCurrentCalendar2.get(Calendar.YEAR);
        month2 = mCurrentCalendar2.get(Calendar.MONTH);
        day2 = mCurrentCalendar2.get(Calendar.DAY_OF_MONTH);

        mDisplayEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        if (y == year && m == month + 1) {
                            enddate = year + "-" + (month + 1) + "-" + dayOfMonth;
                            mDisplayEndDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                        } else {
                            Toast.makeText(getContext(), "Select start date first", Toast.LENGTH_SHORT).show();
                            next.setVisibility(View.INVISIBLE);
                            text_schedule.setVisibility(View.INVISIBLE);
                            spinner_schedule.setVisibility(View.INVISIBLE);
                        }
                    }
                }, year2, month2, day2);
                datePickerDialog.getDatePicker().setMinDate(mCurrentCalendar2.getTimeInMillis());
                mCurrentCalendar2.add(Calendar.MONTH, +1);
                mCurrentCalendar2.add(Calendar.DAY_OF_MONTH, -day1);
                datePickerDialog.getDatePicker().setMaxDate(mCurrentCalendar2.getTimeInMillis());
                mCurrentCalendar2.add(Calendar.MONTH, -1);
                mCurrentCalendar2.add(Calendar.DAY_OF_MONTH, day1);
                datePickerDialog.show();
            }
        });
    }

    private String differenceDates(String startdate, String enddate) {
        SimpleDateFormat dates = new SimpleDateFormat("yyyy-mm-dd");
        try {
            date1 = dates.parse(startdate);
            date2 = dates.parse(enddate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long difference = date2.getTime() - date1.getTime();
        difference /= 24 * 60 * 60 * 1000;

        return Long.toString(difference);
    }


}

