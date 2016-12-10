package com.mieczkowski.firebaseonfire;

/**
 * Created by Patryk Mieczkowski on 10.12.2016
 */

public class Question {

    public final String id;
    public final String owner;
    public final String text;
    public final long timestamp;

    public Question(String id, String owner, String text, long timestamp) {
        this.id = id;
        this.owner = owner;
        this.text = text;
        this.timestamp = timestamp;
    }
}
