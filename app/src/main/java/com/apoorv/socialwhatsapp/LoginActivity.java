package com.apoorv.socialwhatsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginActivity extends AppCompatActivity implements  View.OnClickListener{

    private EditText edtLoginemail,edtLoginpassword;
    private Button btnLoginActivity,btnSignupActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Log In");

        edtLoginemail = findViewById(R.id.edtloginuser);
        edtLoginpassword = findViewById(R.id.edtloginpass);
        edtLoginpassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER &&
                event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    onClick(btnLoginActivity);
                }
                return false;
            }
        });
        btnLoginActivity  = findViewById(R.id.btnloglogin);
        btnSignupActivity = findViewById(R.id.btnlogsignup);

        btnLoginActivity.setOnClickListener(this);
        btnSignupActivity.setOnClickListener(this);
        if(ParseUser.getCurrentUser()!=null)
        {
            ParseUser.getCurrentUser().logOut();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnloglogin:
                if (edtLoginemail.getText().toString().equals("") ||
                        edtLoginpassword.getText().toString().equals(""))
                {
                    FancyToast.makeText(LoginActivity.this,
                            "Email, Password is required",
                            Toast.LENGTH_SHORT,FancyToast.INFO,true).show();
                }
                else
                {
                    ParseUser.logInInBackground(edtLoginemail.getText().toString(),
                            edtLoginpassword.getText().toString(), new LogInCallback() {
                                @Override
                                public void done(ParseUser user, ParseException e) {
                                    if(user!=null && e == null)
                                    {
                                        FancyToast.makeText(LoginActivity.this,
                                                user.getUsername() + " is logged in",
                                                Toast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                                        transitiontowhatsappactivity();
                                    }
                                }
                            });
                }
                break;
            case R.id.btnlogsignup:
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
    public void loginLayoutTapped(View view)
    {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void transitiontowhatsappactivity()
    {
        Intent intent = new Intent(LoginActivity.this,WhatsappUserActivity.class);
        startActivity(intent);
        finish();
    }
}
