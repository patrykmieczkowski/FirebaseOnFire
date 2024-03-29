package com.mieczkowski.firebaseonfire.storage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mieczkowski.firebaseonfire.Question;
import com.mieczkowski.firebaseonfire.WearAdvisor;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public enum DBConnector {

    INSTANCE(WearAdvisor.instance());

    private FirebaseDatabase database;
    private FirebaseAuth firebaseAuth;

    private Map<Callback<String>, FirebaseAuth.AuthStateListener> authListeners = new HashMap<>();

    DBConnector(Context context) {
        FirebaseApp firebaseApp = FirebaseApp.initializeApp(context);

        database = FirebaseDatabase.getInstance(firebaseApp);
        firebaseAuth = FirebaseAuth.getInstance(firebaseApp);

        database
                .setPersistenceEnabled(true);
    }

    public String getUid() {
        return firebaseAuth.getCurrentUser().getUid();
    }

    public void signUp(String name,
                       String pass,
                       OnFailureListener onFailureListener) {
        firebaseAuth
                .createUserWithEmailAndPassword(name, pass)
                .addOnFailureListener(onFailureListener);
    }

    public void signIn(String name, String pass, OnFailureListener onFailureListener) {
        AuthCredential credentials = EmailAuthProvider.getCredential(name, pass);
        firebaseAuth
                .signInWithCredential(credentials)
                .addOnFailureListener(onFailureListener);
    }

    /**
     * null delivered means user not logged in.
     *
     * @param uidCallback
     */
    public void addUserIdListener(final Callback<String> uidCallback) {

        FirebaseAuth.AuthStateListener fireListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    uidCallback.on(user.getUid());
                } else {
                    uidCallback.on(null);
                }
            }
        };
        firebaseAuth.addAuthStateListener(fireListener);
    }

    public void removeUserIdListener(final Callback<String> uidCallback) {
        FirebaseAuth.AuthStateListener fireListener = authListeners.remove(uidCallback);
        firebaseAuth.removeAuthStateListener(fireListener);
    }

    public void setUserName(String uid, String name) {
        database
                .getReference()
                .child("users")
                .child(uid)
                .child("name")
                .setValue(name)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: ", e);
                    }
                });
    }

    public Cancellable observeName(String uid, final Callback<String> callback) {
        DatabaseReference reference = database.getReference()
                .child("users")
                .child(uid)
                .child("name");

        return observeReference(reference, new Callback<DataSnapshot>() {
            @Override
            public void on(DataSnapshot dataSnapshot) {
                callback.on(dataSnapshot.getValue(String.class));
            }
        });
    }

    public void askQuestion(Question question) {

        Map<String, Object> update = new HashMap<>();
        update.put("ask/" + question.id, question);
        update.put("users/" + question.owner + "/lastAsk", question.timestamp);

        database.getReference()
                .updateChildren(update);
    }

    public interface Cancellable {
        void cancel();
    }

    public Cancellable observeReference(final DatabaseReference reference,
                                        final Callback<DataSnapshot> callback) {

        final ValueEventListener listener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                callback.on(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        return new Cancellable() {
            @Override
            public void cancel() {
                reference.removeEventListener(listener);
            }
        };
    }

}
