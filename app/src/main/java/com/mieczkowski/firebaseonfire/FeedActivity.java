package com.mieczkowski.firebaseonfire;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.mieczkowski.firebaseonfire.storage.Callback;
import com.mieczkowski.firebaseonfire.storage.DBConnector;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedActivity extends AppCompatActivity {

    private static final String TAG = FeedActivity.class.getSimpleName();
    @BindView(R.id.button_save) Button saveButton;
    @BindView(R.id.edit_username) EditText editUsername;

    DBConnector.Cancellable nameCancelable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button_save)
    public void hitButtonSaveAction() {
        String uid = DBConnector.INSTANCE.getUid();
        DBConnector.INSTANCE.setUserName(uid, editUsername.getText().toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        String uid = DBConnector.INSTANCE.getUid();
        nameCancelable = DBConnector.INSTANCE.observeName(uid, new Callback<String>() {
            @Override
            public void on(String s) {
                Log.d(TAG, "moj tekst: " + s);
                editUsername.setText(s);
            }
        });
    }

    @Override
    protected void onPause() {
        nameCancelable.cancel();
        super.onPause();
    }
}
