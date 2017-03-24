package com.example.test.rhymeking.algorithm;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.test.rhymeking.MainActivity;
import com.example.test.rhymeking.item.DictionaryInfo;
import com.example.test.rhymeking.item.Phrase;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by 24254 on 10/03/2017.
 */

public class DBTools {


    public static List<Phrase> getPhrases(Context context, String hexCode) {

        ArrayList<Phrase> phrases = new ArrayList<>();
        DB database = null;

        try {
            database = DBFactory.open(context, "dictionaries");
            phrases = (ArrayList<Phrase>) database.getObject(hexCode, ArrayList.class);

            Log.e("DBTools", "database.exists(" + hexCode + ") = " + database.exists(hexCode));
            Log.e("DBTools", "size = " + phrases.size());

            database.close();
        } catch (SnappydbException e) {
            e.printStackTrace();
        }

        return phrases;
    }

    public static void addDictionary(final Context context, final String hexCode, final CheckBox checkBox,
                                     final DictionaryInfo dictionaryInfo, final Button button, int maxLength) {

        // Create a queue
        RequestQueue queue = Volley.newRequestQueue(context);

        try {
            // Input dictionary info
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("requestType", "downloadDictionary");
            jsonObject.put("hexCode", hexCode);
            jsonObject.put("maxLength", maxLength);

/*
            OkGo.post(MainActivity.connectionIPAddress).tag(context).upJson(jsonObject.toString())//
                    .execute(new StringCallback() {

                        @Override
                        public void onSuccess(String s, Call call, okhttp3.Response response) {
                            Log.e("TAG", s + " " + call.toString() + " " + response.toString());
                            // Restore the dictionary
                            try {

                                JSONObject object = new JSONObject(s);

                                JSONArray dictionaryContent = object.getJSONArray("dictionaryContent");

                                DB database = DBFactory.open(context, "dictionaries");
                                database.put(hexCode, Algorithms.toPhraseList(dictionaryContent));

                                // Update view of the button
                                button.setVisibility(View.GONE);
                                checkBox.setVisibility(View.VISIBLE);
                                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        dictionaryInfo.select();
                                    }
                                });

                                database.close();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                            Log.e("TAG", "progress: " + progress + " " + networkSpeed);
                        }
                    });
*/


            final JsonObjectRequest jsonRequest = new JsonObjectRequest
                    (Request.Method.POST, MainActivity.connectionIPAddress, jsonObject, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            // Restore the dictionary
                            try {
                                JSONArray dictionaryContent = response.getJSONArray("dictionaryContent");

                                DB database = DBFactory.open(context, "dictionaries");
                                database.put(hexCode, Algorithms.toPhraseList(dictionaryContent));

                                // Update view of the button
                                button.setVisibility(View.GONE);
                                checkBox.setVisibility(View.VISIBLE);
                                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        dictionaryInfo.select();
                                    }
                                });

                                database.close();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }, null);


            queue.add(jsonRequest);

        } catch (Exception e) {
        }
    }
}

