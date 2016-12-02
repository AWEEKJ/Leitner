package com.uos.leitner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.uos.leitner.R.layout.activity_login;

/**
 * Created by JungJee on 2016. 10. 24..
 *
 * FIN. on 11.05.
 */

public class LoginActivity extends AppCompatActivity
implements View.OnClickListener{

    private static final String TAG = "Activity_login";

//    private TextView mStatusTextView;
//    private TextView mDetailTextView;
    private EditText mEmailField;
    private EditText mPasswordField;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //tmp
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_login);

        progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // [START_EXCLUDE]
//                updateUI(user);
                // [END_EXCLUDE]
            }
        };


//        Button login_btn = (Button) findViewById(R.id.login_button);
//        login_btn.setOnClickListener(this);
//
//        Button signup_email_btn = (Button) findViewById(R.id.sign_in_email_button);
//        signup_email_btn.setOnClickListener(this);

        findViewById(R.id.login_button).setOnClickListener(this);
//        findViewById(R.id.sign_in_email_button).setOnClickListener(this);
        findViewById(R.id.sign_in_google_button).setOnClickListener(this);

        mEmailField = (EditText) findViewById(R.id.login_email);
        mPasswordField = (EditText) findViewById(R.id.login_password);

    }

    @Override
    public void onClick(View view) {

        int i = view.getId();

//        if (i == R.id.sign_in_email_button) {
//            Intent intent = new Intent(this, SigninActivity.class);
//
//            startActivity(intent);
//        } else if(i == R.id.login_button){
        if(i == R.id.login_button){
            /**
             * codes that
             * directly move to mainActivity.
            Intent intent = new Intent(this, MainActivity.class);

            startActivity(intent);
            **/

            signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());

        } else if(i == R.id.sign_in_google_button){

            Intent intent = new Intent(this, GoogleSigninActivity.class);

//            finish();
            startActivity(intent);
        }
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

//        showProgressDialog();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
//                        if (!task.isSuccessful()) {
//                            Log.w(TAG, "signInWithEmail:failed", task.getException());
//                            Toast.makeText(activity_login.this, R.string.auth_failed,
//                                    Toast.LENGTH_SHORT).show();
//                        }

                        if(task.isSuccessful()) {

                            Toast.makeText(LoginActivity.this,"Log in Successed.", Toast.LENGTH_LONG).show();

                            finish();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                            startActivity(intent);
                        } else{
                            Toast.makeText(LoginActivity.this,"Log in Failed.", Toast.LENGTH_LONG).show();

                        }
                     }
                });
        // [END sign_in_with_email]
    }



    //added 11.05

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    // [END on_start_add_listener]

    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Doesn't match equired form.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Doesn't match equired form.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

}
