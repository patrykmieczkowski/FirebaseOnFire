package com.mieczkowski.firebaseonfire;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.mieczkowski.firebaseonfire.storage.DBConnector;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedActivity extends AppCompatActivity {

    @BindView(R.id.button_save) Button saveButton;
    @BindView(R.id.edit_username) EditText editUsername;

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
}
