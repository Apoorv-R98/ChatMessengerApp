package com.apoorv.socialwhatsapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText edtEmail,edtUsername,edtPassword;
    private Button btnSignUp,btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Sign Up");

        edtEmail = findViewById(R.id.edtsignupemail);
        edtUsername = findViewById(R.id.edtsignupuser);
        edtPassword = findViewById(R.id.edtsignuppass);

        edtPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                {
                    if(keyCode == KeyEvent.KEYCODE_ENTER
                    && event.getAction() == KeyEvent.ACTION_DOWN)
                    {
                        onClick(btnSignUp);
                    }
                }
                return false;
            }
        });

        btnSignUp = findViewById(R.id.btnsignup);
        btnLogin = findViewById(R.id.btnlogin);
        btnLogin.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);

        if(ParseUser.getCurrentUser()!=null)
        {
            //ParseUser.getCurrentUser().logOut();
            transitiontowhatsappactivity();
        }
    }


    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.btnsignup:
                if(edtEmail.getText().toString().equals("") ||
                        edtUsername.getText().toString().equals("") ||
                        edtPassword.getText().toString().equals(""))
                {
                    FancyToast.makeText(MainActivity.this,"Email, Username, Password is required", Toast.LENGTH_SHORT,FancyToast.INFO,true).show();
                }
                else {
                    final ParseUser appUser = new ParseUser();
                    appUser.setEmail(edtEmail.getText().toString());
                    appUser.setUsername(edtUsername.getText().toString());
                    appUser.setPassword(edtPassword.getText().toString());

                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Sign Up " + edtUsername.getText().toString());
                    progressDialog.show();
                    appUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null)
                            {
                                FancyToast.makeText(MainActivity.this,
                                        appUser.getUsername() + " is signed up",
                                        Toast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                                transitiontowhatsappactivity();

                            }else {
                                FancyToast.makeText(MainActivity.this,
                                        "There was an error " + e.getMessage(),
                                        Toast.LENGTH_SHORT, FancyToast.ERROR, true).show();

                            }
                            progressDialog.dismiss();
                        }
                    });
                }
                break;
            case R.id.btnlogin:
                    Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                    break;
        }
    }


    public void rootLayoutTapped(View view)
    {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void transitiontowhatsappactivity()
    {
        Intent intent = new Intent(MainActivity.this,WhatsappUserActivity.class);
        startActivity(intent);
        finish();
    }
}
