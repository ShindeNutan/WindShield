package com.windshield.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.card.MaterialCardView;
import com.windshield.Activity.Constant.Utils;
import com.windshield.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawer_layout;
    ImageView img_drawer_icon;
    LinearLayout lnr_nav, lnr_home, lnr_whatsapp, lnr_whatsappnumber, lnr_call, lnr_callnumber;
    MaterialCardView card_service1, card_service2, card_appoinment,card_service3;
    boolean doubleBackToExitPressedOnce = false;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        onClick();
    }

    public void init() {
        progressDialog = new ProgressDialog(this);
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        img_drawer_icon = (ImageView) findViewById(R.id.img_drawer_icon);
        lnr_nav = (LinearLayout) findViewById(R.id.lnr_nav);
        lnr_home = (LinearLayout) findViewById(R.id.lnr_home);
        lnr_whatsapp = (LinearLayout) findViewById(R.id.lnr_whatsapp);
        lnr_whatsappnumber = (LinearLayout) findViewById(R.id.lnr_whatsappnumber);
        lnr_call = (LinearLayout) findViewById(R.id.lnr_call);
        lnr_callnumber = (LinearLayout) findViewById(R.id.lnr_callnumber);
        card_appoinment = (MaterialCardView) findViewById(R.id.card_appoinment);
        card_service1 = (MaterialCardView) findViewById(R.id.card_service1);
        card_service2 = (MaterialCardView) findViewById(R.id.card_service2);
        card_service3 = (MaterialCardView) findViewById(R.id.card_service3);

    }

    public void onClick() {

        img_drawer_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer_layout.openDrawer(lnr_nav);
            }
        });

        lnr_whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lnr_whatsappnumber.getVisibility() == View.VISIBLE){
                    lnr_whatsappnumber.setVisibility(View.GONE);
                }else {
                    lnr_whatsappnumber.setVisibility(View.VISIBLE);
                }
            }
        });

        lnr_whatsappnumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkWhatsApp("9867386385");
//                Uri uri = Uri.parse("smsto:" + R.string.number);
//                Intent i = new Intent(Intent.ACTION_SENDTO, uri);
//                i.putExtra("sms_body", "abc");
//                i.setPackage("com.whatsapp");
//                startActivity(i);

//                Intent callIntent = new Intent(Intent.ACTION_CALL);
//                callIntent.setData(Uri.parse("tel:" + ));
//                if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    Activity#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for Activity#requestPermissions for more details.
//                    return;
//                }
//                startActivity(callIntent);

            }
        });

        lnr_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lnr_callnumber.getVisibility() == View.VISIBLE){
                    lnr_callnumber.setVisibility(View.GONE);
                }else{
                    lnr_callnumber.setVisibility(View.VISIBLE);
                }
            }
        });

        lnr_callnumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + "9867386385"));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    Activity#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for Activity#requestPermissions for more details.
                        return;
                    }
                }
                startActivity(callIntent);

            }
        });

        card_service1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openDialog("1");

            }
        });

        card_service2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog("2");
            }
        });

        card_service3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog("3");
            }
        });
        card_appoinment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,BookAppoinmentActivity.class));
            }
        });
    }

    public void openDialog(final String type){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setDimAmount(0.8f);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.dimAmount = 1f;
        dialog.setContentView(R.layout.dialog_layout);
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(lp);
        ImageView img_close = dialog.findViewById(R.id.img_close);
        Button btn_submit = dialog.findViewById(R.id.btn_submit);
        final EditText edt_email = dialog.findViewById(R.id.edt_email);
        final EditText edt_mobile = dialog.findViewById(R.id.edt_mobile);
        final EditText edt_name = dialog.findViewById(R.id.edt_name);

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
                }else {
                    sendRequest(edt_name.getText().toString(), edt_mobile.getText().toString(), edt_email.getText().toString(), type);
                }
//                sendMail("uzmaansari2222@gmail.com");
            }
        });

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void sendRequest(final String name , final String mob, final String email, final String type){
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.Request_Send, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("msg");
                    if(status.equals("ok")){
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                        sendMail(email);
                        progressDialog.dismiss();
                    }else{
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
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
                params.put("fullname",name);
                params.put("contact",mob);
                params.put("email",email);
                params.put("serviceType",type);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }

    public void sendMail(String emailid){
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("text/html");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{emailid});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Test Subject");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "test text body");
        startActivity(Intent.createChooser(emailIntent, "Send mail..."));


    }

    private void checkWhatsApp(String number) {

        String smsNumber = "91"+number;
        boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp");
        if (isWhatsappInstalled) {
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.setType("text/plain");
            sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(smsNumber) + "@s.whatsapp.net");
            startActivity(sendIntent);
        } else {
            Uri uri = Uri.parse("market://details?id=com.whatsapp");
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            Toast.makeText(this, "WhatsApp not Installed",
                    Toast.LENGTH_SHORT).show();
            startActivity(goToMarket);
        }
    }

    private boolean whatsappInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    void backPressed() {

        if (doubleBackToExitPressedOnce) {
            finishAffinity();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (drawer_layout.isDrawerOpen(lnr_nav)) {
            drawer_layout.closeDrawer(lnr_nav);
        } else {
            backPressed();
        }
    }

    public static boolean isValidEmail(CharSequence  email){
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
