package com.example.test.rhymeking;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;

import com.example.test.rhymeking.adapter.PinyinAdapter;
import com.example.test.rhymeking.item.Pinyin;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RhymeActivity extends AppCompatActivity {

    private PinyinAdapter pinyinAdapter;
    private GridView gv_gridview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rhyme);

        initialisePinyins();

        pinyinAdapter = new PinyinAdapter(this);
        gv_gridview.setAdapter(pinyinAdapter);

    }

    public static boolean isNull() {
        return MainActivity.pinyins == null;
    }

    public static JSONArray getRhymeTable() {

        Map<Integer, JSONArray> map = new HashMap<>();

        for (Pinyin pinyin : MainActivity.pinyins) {

            int label = pinyin.getLabel();

            // If there is no such a key, create it and add this element
            if (!map.containsKey(label)) {
                JSONArray newArray = new JSONArray();
                newArray.put(pinyin.getPinyin());
                map.put(label, newArray);

                // If there exists the key, add into it
            } else {
                JSONArray temp = map.get(label);
                temp.put(pinyin.getPinyin());
            }
        }

        // Convert it into JSONArray
        JSONArray jsonArray = new JSONArray();
        for (Map.Entry<Integer, JSONArray> entry : map.entrySet()) {
            jsonArray.put(entry.getValue());
        }

        return jsonArray;
    }


    private void initialisePinyins() {


        gv_gridview = (GridView) findViewById(R.id.rhyme_table);

        gv_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    final int position, long id) {
                View view = View.inflate(RhymeActivity.this, R.layout.pinyin_item, null);

                final EditText label = (EditText) view.findViewById(R.id.label);
                label.setInputType(EditorInfo.TYPE_CLASS_PHONE);

                // Change label of this pinyin
                new AlertDialog.Builder(RhymeActivity.this).setView(view).setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                int newLabel = Integer.parseInt(label.getText().toString());

                                MainActivity.pinyins.get(position).setLabel(newLabel);
                                pinyinAdapter.notifyDataSetChanged();
                            }
                        }).show();

            }
        });

    }

    public void save(View view) {
        finish();
    }
}
