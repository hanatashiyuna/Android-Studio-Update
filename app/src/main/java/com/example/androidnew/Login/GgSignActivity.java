package com.example.androidnew.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidnew.R;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class GgSignActivity extends AppCompatActivity {

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gg_sign);

        ImageView avatar = findViewById(R.id.avatar);
        TextView name = findViewById(R.id.name);
        TextView email = findViewById(R.id.email);
        Button btnSignOut = findViewById(R.id.btn_sign_out);

        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            return;
        }
        String contact = bundle.getString("contact");
        if(contact.equals("gg")){
//            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
//            gsc = GoogleSignIn.getClient(this, gso);
//
//            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//            if (account != null){
//                String personName = account.getDisplayName();
//                String personEmail = account.getEmail();
//                name.setText(personName);
//                email.setText(personEmail);
//            }
//
//            btnSignOut.setOnClickListener(view -> {
//                gsc.signOut().addOnCompleteListener(task -> {
//                    finish();
//                    startActivity(new Intent(GgSignActivity.this, LoginActivity.class));
//                });
//            });
        }else{
            Toast.makeText(this, "fb", Toast.LENGTH_SHORT).show();
            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            GraphRequest request = GraphRequest.newMeRequest(
                    accessToken,
                    (object, response) -> {
                        try {
                            String url = object.getJSONObject("picture").getJSONObject("data").getString("url");
                            String fullName = object.getString("name");
                            name.setText(fullName);
                            Picasso.get().load(url).into(avatar);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,link,picture.type(large)");
            request.setParameters(parameters);
            request.executeAsync();

        }
        btnSignOut.setOnClickListener(view -> {
            Toast.makeText(this, "out", Toast.LENGTH_SHORT).show();
            LoginManager.getInstance().logOut();
            startActivity(new Intent(GgSignActivity.this, LoginActivity.class));
            finish();
        });
    }
}