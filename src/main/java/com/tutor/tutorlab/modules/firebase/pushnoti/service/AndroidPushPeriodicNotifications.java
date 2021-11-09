package com.tutor.tutorlab.modules.firebase.pushnoti.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AndroidPushPeriodicNotifications {
    public static String PeriodicNotificationJson() throws JSONException {
        LocalDate localDate = LocalDate.now();

        String deviceToken[] = {"ct6ijGhHR9mjHzgbfZNOn4:APA91bGh0LsW-pKKLTPTYv4OgNR5f9oMQgATNxHpEVmIUIC9-BdKgBNtCkvP94lAreNbrQFFQgwYArEBzlQ5nLWSz9IekuyrFIA1kWUz4ExLeDRZpbMpL8kclrudX01BNWv-SK5eUmDs"};


        JSONObject body = new JSONObject();

        List<String> tokenlist = new ArrayList<String>();

        for(int i=0; i<deviceToken.length; i++){
            tokenlist.add(deviceToken[i]);
        }

        JSONArray array = new JSONArray();

        for(int i=0; i<tokenlist.size(); i++) {
            array.put(tokenlist.get(i));
        }

        body.put("registration_ids", array);

        JSONObject notification = new JSONObject();
        notification.put("title","안녕하세요!");
        notification.put("body","자기 전 투두 계획 세워보세요~ ");

        body.put("notification", notification);

        return body.toString();
    }
}