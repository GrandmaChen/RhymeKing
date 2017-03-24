package com.example.test.rhymeking;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.test.rhymeking.adapter.ResultTableAdapter;
import com.example.test.rhymeking.algorithm.Algorithms;
import com.example.test.rhymeking.item.DictionaryInfo;
import com.example.test.rhymeking.item.Phrase;
import com.example.test.rhymeking.item.Pinyin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Attributes from other activities
    public static String connectionIPAddress;
    public static boolean autoParse;
    public static boolean matchAll;
    public static boolean sameInitials;
    public static boolean sameFinals;
    public static boolean sameTones;
    public static List<DictionaryInfo> catalogue = null;
    public static List<Phrase> phrasePool = null;
    public static List<Pinyin> pinyins;

    //Attributes inside this activity
    public static List<String> rhymeWordsList = null;
    public static TextView message = null;
    private ResultTableAdapter resultTableAdapter;
    private List<String> writtenLyrics;
    private int editingIndex;
    private ListView resultTable;

    private Button prev;
    private Button next;
    private Button showAll;

    private EditText lyricsInput;

    private String[] settings;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialise pinyin data
        initialisePinyinData();

        // Initialise rhyme words list
        rhymeWordsList = new ArrayList<String>();

        // Initialise the phrase pool
        phrasePool = new ArrayList<Phrase>();

        // Initialise written lyrics
        writtenLyrics = new ArrayList<String>();
        writtenLyrics.add("");

        // Initialse message box
        message = (TextView) findViewById(R.id.message);
        message.setText("目前选中词库量:" + phrasePool.size());

        // Initialise the queue
        queue = Volley.newRequestQueue(this);

        // Initialise buttons
        prev = (Button) findViewById(R.id.prev);
        next = (Button) findViewById(R.id.next);
        showAll = (Button) findViewById(R.id.show_all);

        // Initialise the attributes
        editingIndex = 0;

        // Initialise the drawer
        settings = new String[]{"设置", "选择词典", "押韵音节"};
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Initialise the settings
        connectionIPAddress = getString(R.string.ip_address);
        autoParse = true;
        matchAll = false;
        sameInitials = false;
        sameFinals = true;
        sameTones = false;

        // Get the component and set auto rhyme
        lyricsInput = (EditText) findViewById(R.id.lyrics_input);
        lyricsInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            //Get rhyme words
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (DictionaryListsActivity.isNull() || RhymeActivity.isNull()) {
                    Toast.makeText(MainActivity.this, "词典或拼音设置尚未完成!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String editingLyrics = s.toString();


                if (autoParse)
                    try {
                        autoParse(editingLyrics);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                else {

                    // Get parsed lyrics
                    List<String> parsedLyrics = new ArrayList<String>();

                    // Parse it by space
                    for (String str : editingLyrics.split("\\s+"))
                        if (str.length() > 1)
                            parsedLyrics.add(str);

                    getRhymeWordsAndUpdateListView(parsedLyrics);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<Object>(this, R.layout.drawer_list_item, settings));

        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // Initialise the result table
        resultTable = (ListView) findViewById(R.id.resultTable);
    }

    public void getRhymeWordsAndUpdateListView(List<String> parsedLyrics) {

        if (phrasePool == null || phrasePool.size() == 0)
            return;

        // Get rhyme words
        for (String word : parsedLyrics) {

            Phrase wordAsPhrase = Algorithms.asPhrase(word, phrasePool);
            if (wordAsPhrase == null) continue;

            List<String> rhymeWordsInfo = new ArrayList<String>();

            // Add itself as the first element
            rhymeWordsInfo.add(word);

            // Compare every phrase in selected dictionaries, add it if matches

            for (Phrase phrase : phrasePool) {

                // Skip if meets same word
                if (phrase.getCharacters().equals(word)) continue;

                if (wordAsPhrase.rhymes(phrase, sameInitials, sameFinals, sameTones, matchAll))
                    rhymeWordsInfo.add(phrase.getCharacters());

            }

            if (rhymeWordsInfo.size() > 1)
                rhymeWordsList.add(Algorithms.displayingFormat(rhymeWordsInfo));

        }

        // Update adapter if this is not empty
        if (rhymeWordsList != null && rhymeWordsList.size() > 0) {
            resultTableAdapter = new ResultTableAdapter(MainActivity.this);
            resultTable.setAdapter(resultTableAdapter);
        }
    }

    // View all written lyrics
    public void viewAll(View v) {

        View view = View.inflate(this, R.layout.dialog_view, null);

        String writtenLyrics = "";
        for (String str : this.writtenLyrics)
            writtenLyrics += str + "\n";

        final TextView textView = (TextView) view.findViewById(R.id.written_lyrics);
        textView.setText(writtenLyrics);

        new AlertDialog.Builder(MainActivity.this).setView(view).show();
    }

    public void saveAndGetPrevSentence(View view) {

        // If this is the first sentence
        if (editingIndex == 0) {
            Toast.makeText(this, "已经是第一句了", Toast.LENGTH_SHORT).show();
            return;
        }

        // Else get restored sentence
        else {

            String lyrics = lyricsInput.getText().toString();

            if (!lyrics.equals(""))

                if (writtenLyrics.size() == editingIndex)
                    writtenLyrics.add(lyrics);
                else
                    writtenLyrics.set(editingIndex, lyrics);

            editingIndex--;
            lyricsInput.setText(writtenLyrics.get(editingIndex));
        }
    }

    public void saveAndGetNextSentence(View view) {

        String lyrics = lyricsInput.getText().toString();

        // If this is the last sentence
        if (editingIndex == writtenLyrics.size()) {

            if (lyrics.equals("")) {
                Toast.makeText(MainActivity.this, "已经是最后一句了", Toast.LENGTH_SHORT).show();
                return;
            }

            writtenLyrics.add(lyrics);
            lyricsInput.setText("");
            editingIndex++;
        }

        // Else get sentence restored
        else {
            writtenLyrics.set(editingIndex, lyrics);
            editingIndex++;

            if (editingIndex == writtenLyrics.size())
                writtenLyrics.add("");

            lyricsInput.setText(writtenLyrics.get(editingIndex));
        }

    }

    public void autoParse(String lyrics) throws JSONException {

        final List<String> split = new ArrayList<String>();

        // Get parsed lyrics from the servlet
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("requestType", "autoParse");
        jsonObject.put("lyrics", lyrics);

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.POST, MainActivity.connectionIPAddress, jsonObject, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray parsedLyrics = response.getJSONArray("parsedLyrics");

                            for (int i = 0; i < parsedLyrics.length(); i++)
                                split.add(parsedLyrics.getString(i));

                            getRhymeWordsAndUpdateListView(split);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }, null);

        queue.add(jsonRequest);
    }

    private void initialisePinyinData() {
        pinyins = new ArrayList<Pinyin>();

        pinyins.add(new Pinyin("a", 1));
        pinyins.add(new Pinyin("ia", 1));
        pinyins.add(new Pinyin("ua", 1));

        pinyins.add(new Pinyin("ai", 2));
        pinyins.add(new Pinyin("uai", 2));

        pinyins.add(new Pinyin("ao", 3));
        pinyins.add(new Pinyin("iao", 3));

        pinyins.add(new Pinyin("an", 4));
        pinyins.add(new Pinyin("uan", 4));

        pinyins.add(new Pinyin("o/uo", 5));

        pinyins.add(new Pinyin("ou", 6));
        pinyins.add(new Pinyin("iu", 6));

        pinyins.add(new Pinyin("er", 7));

        pinyins.add(new Pinyin("ie", 8));
        pinyins.add(new Pinyin("üe", 8));

        pinyins.add(new Pinyin("ü", 9));

        pinyins.add(new Pinyin("e", 10));

        pinyins.add(new Pinyin("ei", 11));
        pinyins.add(new Pinyin("ui", 11));

        pinyins.add(new Pinyin("un", 12));
        pinyins.add(new Pinyin("en", 12));

        pinyins.add(new Pinyin("ün", 13));
        pinyins.add(new Pinyin("in", 13));

        pinyins.add(new Pinyin("u", 14));

        pinyins.add(new Pinyin("ong", 15));
        pinyins.add(new Pinyin("iong", 15));

        pinyins.add(new Pinyin("-i(z/c/s)", 16));

        pinyins.add(new Pinyin("-i(zh/ch/sh/r)", 17));

        pinyins.add(new Pinyin("ian", 18));

        pinyins.add(new Pinyin("i", 19));

        pinyins.add(new Pinyin("üan", 20));

        pinyins.add(new Pinyin("eng", 21));

        pinyins.add(new Pinyin("ang", 22));
        pinyins.add(new Pinyin("iang", 22));
        pinyins.add(new Pinyin("uang", 22));

        pinyins.add(new Pinyin("ing", 23));
    }

    private class DrawerItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0:
                    startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                    break;
                case 1:
                    startActivity(new Intent(MainActivity.this, DictionaryListsActivity.class));
                    break;
                case 2:
                    startActivity(new Intent(MainActivity.this, RhymeActivity.class));
                    break;
            }

        }
    }

}
