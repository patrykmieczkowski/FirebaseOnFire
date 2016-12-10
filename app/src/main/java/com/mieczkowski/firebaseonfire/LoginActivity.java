package com.mieczkowski.firebaseonfire;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.mieczkowski.firebaseonfire.storage.Callback;
import com.mieczkowski.firebaseonfire.storage.DBConnector;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements Callback<String> {

    private static final String TAG = LoginActivity.class.getSimpleName();
    @BindView(R.id.name)
    EditText username;

    @BindView(R.id.pass)
    EditText pass;

    @BindView(R.id.error_message)
    TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DBConnector.INSTANCE.addUserIdListener(this);
    }

    @Override
    protected void onPause() {
        DBConnector.INSTANCE.removeUserIdListener(this);
        super.onPause();

    }

    @OnClick(R.id.sign_in)
    void signIn() {
        DBConnector.INSTANCE
                .signIn(username.getText().toString(), pass.getText().toString(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: ", e);
                        error.setText("Can't sign in");
                    }
                });
    }

    @OnClick(R.id.sign_up)
    void signUp() {
        DBConnector.INSTANCE
                .signUp(username.getText().toString(), pass.getText().toString(), new OnFailureListener() {

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: ", e);
                        error.setText("Can't sign up");
                    }
                });
    }

    @Override
    public void on(String uid) {
        if (uid != null) {
            startActivity(new Intent(LoginActivity.this, FeedActivity.class));
        }
    }
}
