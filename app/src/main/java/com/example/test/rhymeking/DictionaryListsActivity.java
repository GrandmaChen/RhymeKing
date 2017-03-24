package com.example.test.rhymeking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.test.rhymeking.adapter.DictionaryListAdapter;
import com.example.test.rhymeking.algorithm.DictionaryInfoComparator;
import com.example.test.rhymeking.item.DictionaryInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DictionaryListsActivity extends AppCompatActivity {

    private ListView lv_dictionaryList;
    private DictionaryListAdapter dictionaryListAdapter;

    private EditText search;

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_lists);

        // Initialise the queue
        queue = Volley.newRequestQueue(this);

        lv_dictionaryList = (ListView) findViewById(R.id.dictionary_lists);

        // If the list is empty, initialise the dictionaries
        if (MainActivity.catalogue == null)
            try {
                initialiseDictionaryLists();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Else load it
        else {
            dictionaryListAdapter = new DictionaryListAdapter(this, MainActivity.catalogue);
            lv_dictionaryList.setAdapter(dictionaryListAdapter);
        }

        // Search key word and present the list
        search = (EditText) findViewById(R.id.search);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                List<DictionaryInfo> result = new ArrayList<DictionaryInfo>();

                String searchName = s.toString();

                for (DictionaryInfo dictionaryInfo : MainActivity.catalogue)
                    if (dictionaryInfo.getName().contains(searchName))
                        result.add(dictionaryInfo);

                dictionaryListAdapter = new DictionaryListAdapter(DictionaryListsActivity.this, result);
                lv_dictionaryList.setAdapter(dictionaryListAdapter);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    public void initialiseDictionaryLists() throws JSONException {

        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("requestType", "dictionaryList");

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Method.POST, MainActivity.connectionIPAddress, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject dictionaryList = response.getJSONObject("dictionaryList");

                            JSONArray ja_hexCode = dictionaryList.getJSONArray("hexCode");
                            JSONArray ja_name = dictionaryList.getJSONArray("name");
                            JSONArray ja_quantity = dictionaryList.getJSONArray("quantity");
                            JSONArray ja_maxLength = dictionaryList.getJSONArray("maxLength");

                            MainActivity.catalogue = new ArrayList<DictionaryInfo>();

                            for (int i = 0; i < ja_name.length(); i++)
                                MainActivity.catalogue.add(new DictionaryInfo(DictionaryListsActivity.this,
                                        ja_name.get(i).toString(), ja_hexCode.get(i).toString(),
                                        ja_quantity.get(i).toString(),
                                        Integer.parseInt(ja_maxLength.get(i).toString()), false));

                            Collections.sort(MainActivity.catalogue, new DictionaryInfoComparator());

                            dictionaryListAdapter = new DictionaryListAdapter(DictionaryListsActivity.this, MainActivity.catalogue);
                            lv_dictionaryList.setAdapter(dictionaryListAdapter);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }, null);


        queue.add(jsonRequest);
    }

    // Get all selected dictionaries and their max length
    public static JSONObject getSelectedDictionary() {

        JSONObject jsonObject;

        Map<String, String> selectedDictionary = new HashMap<String, String>();

        for (DictionaryInfo dictionaryInfo : MainActivity.catalogue)
            if (dictionaryInfo.isSelected())
                selectedDictionary.put(dictionaryInfo.getHexCode(), dictionaryInfo.getMaxLength() + "");

        jsonObject = new JSONObject(selectedDictionary);
        return jsonObject;
    }

    public void save(View view) {
        finish();
    }

    public static boolean isNull() {
        return MainActivity.catalogue == null;
    }
}
