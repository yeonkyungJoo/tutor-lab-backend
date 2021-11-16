package com.tutor.tutorlab.modules.firebase.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AndroidPushPeriodicNotifications {

    public static String PeriodicNotificationJson() {

        // TODO - application.yml
        String[] deviceTokens = {"ct6ijGhHR9mjHzgbfZNOn4:APA91bGh0LsW-pKKLTPTYv4OgNR5f9oMQgATNxHpEVmIUIC9-BdKgBNtCkvP94lAreNbrQFFQgwYArEBzlQ5nLWSz9IekuyrFIA1kWUz4ExLeDRZpbMpL8kclrudX01BNWv-SK5eUmDs"};

        JSONObject body = new JSONObject();

        JSONArray array = new JSONArray();
        for (String deviceToken : deviceTokens) {
            array.put(deviceToken);
        }
        body.put("registration_ids", array);

        JSONObject notification = new JSONObject();
        notification.put("title", "안녕하세요!");
        notification.put("body", "자기 전 투두 계획 세워보세요~ ");
        body.put("notification", notification);

        return body.toString();
    }
}