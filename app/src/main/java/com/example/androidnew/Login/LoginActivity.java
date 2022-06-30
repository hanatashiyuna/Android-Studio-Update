package com.example.androidnew.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.androidnew.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    CallbackManager callbackManager;
    ImageView btnGG, btnFb, btnTwitter;
    AccessToken accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText usn = findViewById(R.id.usn);
        EditText password = findViewById(R.id.password);
        Button login = findViewById(R.id.button);
        btnGG = findViewById(R.id.btn_gg_sign_in);
        btnFb = findViewById(R.id.btn_fb_sign_in);
        btnTwitter = findViewById(R.id.btn_twitter_sign_in);
        callbackManager = CallbackManager.Factory.create();

        accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        login.setOnClickListener(view -> {
            if(usn.getText().equals("yuna") && password.getText().equals("yuna")){
                Bundle bundle = new Bundle();
                bundle.putString("contact", "none");
                Intent intent = new Intent(LoginActivity.this, GgSignActivity.class);
                startActivity(intent);
            }
        });

        btnGG.setOnClickListener(view -> {
            ggSignIn();
        });

        btnFb.setOnClickListener(view -> {
            LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile"));
        });

//        if(isLoggedIn){
//            Intent intent = new Intent(LoginActivity.this,GgSignActivity.class);
//            startActivity(intent);
//            finish();
//            //LoginManager.getInstance().logOut();
//        }

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Bundle bundle = new Bundle();
                        bundle.putString("contact", "fb");
                        startActivity(new Intent(LoginActivity.this, GgSignActivity.class));
                        finish();
                        //LoginManager.getInstance().logOut();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(LoginActivity.this,"Facebook login cancel",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(LoginActivity.this,"Facebook login error",Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void ggSignIn(){
        Intent intent = gsc.getSignInIntent();
        startActivityForResult(intent, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                task.getResult(ApiException.class);
                navigateToSecondActivity();
            } catch (ApiException e) {
                Toast.makeText(LoginActivity.this, "Something went wrong?", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void navigateToSecondActivity(){
        Bundle bundle = new Bundle();
        bundle.putString("contact", "gg");
        Intent intent = new Intent(LoginActivity.this, GgSignActivity.class);
        startActivity(intent);
    }
}