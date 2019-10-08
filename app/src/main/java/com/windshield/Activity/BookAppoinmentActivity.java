package com.windshield.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.windshield.Activity.Constant.Utils;
import com.windshield.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookAppoinmentActivity extends AppCompatActivity {

    final Calendar myCalendar = Calendar.getInstance();
    EditText edt_date,edt_time,edt_name,edt_mobile,edt_email;
    int year,month,day;
    SimpleDateFormat startTimeFormat;
    Button btn_submit;
//    Toolbar toolbar;
    ProgressDialog progressDialog ;
    ImageView img_back;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appoinment);
        init();
        onClick();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void init(){
        progressDialog = new ProgressDialog(this);
        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
//        toolbar = findViewById(R.id.toolbar);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });
        edt_date = findViewById(R.id.edt_date);
        edt_time = findViewById(R.id.edt_time);
        edt_name = findViewById(R.id.edt_name);
        edt_mobile = findViewById(R.id.edt_mobile);
        edt_email = findViewById(R.id.edt_email);
        btn_submit = findViewById(R.id.btn_submit);
    }

    public void onClick(){
        edt_date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
//                        myCalendar.set(Calendar.YEAR, year);
//                        myCalendar.set(Calendar.MONTH, monthOfYear);
//                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        year  = year;
                        month = monthOfYear;
                        day = dayOfMonth;

                        updateLabel();
                    }

                };

                // TODO Auto-generated method stub
                DatePickerDialog datePickerDialog = new DatePickerDialog(BookAppoinmentActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
                return false;
            }
        });

        edt_time.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
//                edt_date.setText("");
                final Calendar mcurrentTime = Calendar.getInstance();
                final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                final  int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(BookAppoinmentActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        if (selectedHour>=hour && selectedMinute>=minute){
                            edt_time.setText( selectedHour + ":" + selectedMinute);
                        }
//                        else{
//                            text_error.setText("Invalid time! Please Select greater than current time");
//                        }
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select From Time");
                mTimePicker.show();
                return false;
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edt_name.getText().toString().equals("")){
                    edt_name.setError("Enter Name");
                    edt_name.requestFocus();
                }else if(edt_mobile.getText().toString().equals("")){
                    edt_mobile.setError("Enter Mobile number");
                    edt_mobile.requestFocus();
                }else if(edt_mobile.getText().length()!=10){
                    edt_mobile.setError("Enter Mobile number");
                    edt_mobile.requestFocus();
                } else if(edt_email.getText().toString().equals("")){
                    edt_email.setError("Enter Email Id");
                    edt_email.requestFocus();
                }else if(!isValidEmail(edt_email.getText().toString())){
                    edt_email.setError("Enter Email Id");
                    edt_email.requestFocus();
                }else if(edt_date.getText().toString().equals("")){
                    edt_date.setError("Select Appointment Date");
                    edt_date.requestFocus();
                }else if(edt_time.getText().toString().equals("")){
                    edt_time.setError("Select Appointment Time");
                    edt_time.requestFocus();
                }else{
                    setAppointment();
                }
            }
        });

    }

    public SimpleDateFormat startTimeFormat(){
        return this.startTimeFormat = new SimpleDateFormat("hh:00 a");
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        edt_date.setText(sdf.format(myCalendar.getTime()));
    }

    public void setAppointment(){
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.Book_Appoinment, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("msg");
                    if(status.equals("ok")){
                        Toast.makeText(BookAppoinmentActivity.this, message, Toast.LENGTH_SHORT).show();
                        edt_name.setText("");
                        edt_mobile.setText("");
                        edt_email.setText("");
                        edt_date.setText("");
                        edt_time.setText("");
                        progressDialog.dismiss();
                    }else{
                        edt_name.setText("");
                        edt_mobile.setText("");
                        edt_email.setText("");
                        edt_date.setText("");
                        edt_time.setText("");
                        Toast.makeText(BookAppoinmentActivity.this, message, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("fullname",edt_name.getText().toString());
                params.put("contact",edt_mobile.getText().toString());
                params.put("email",edt_email.getText().toString());
                params.put("date",edt_date.getText().toString());
                params.put("time",edt_time.getText().toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }

    public static boolean isValidEmail(CharSequence  email){
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}

