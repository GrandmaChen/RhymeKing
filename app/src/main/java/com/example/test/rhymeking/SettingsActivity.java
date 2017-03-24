package com.example.test.rhymeking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    private EditText ipAddress;

    private CheckBox autoParse;
    private CheckBox matchAll;

    private CheckBox sameInitials;
    private CheckBox sameFinals;
    private CheckBox sameTones;

    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Edit ip address
        ipAddress = (EditText) findViewById(R.id.list_address);
        ipAddress.setText(MainActivity.connectionIPAddress);

        // Initial checkboxes
        autoParse = (CheckBox) findViewById(R.id.auto_parse);
        autoParse.setChecked(MainActivity.autoParse);

        matchAll = (CheckBox) findViewById(R.id.match_all);
        matchAll.setChecked(MainActivity.matchAll);

        autoParse.setChecked(MainActivity.autoParse);
        matchAll.setChecked(MainActivity.matchAll);

        autoParse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MainActivity.autoParse = isChecked;
            }
        });

        matchAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MainActivity.matchAll = isChecked;
            }
        });

        sameInitials = (CheckBox) findViewById(R.id.same_initials);
        sameFinals = (CheckBox) findViewById(R.id.same_finals);
        sameTones = (CheckBox) findViewById(R.id.same_tones);

        sameInitials.setChecked(MainActivity.sameInitials);
        sameFinals.setChecked(MainActivity.sameFinals);
        sameTones.setChecked(MainActivity.sameTones);

        sameInitials.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MainActivity.sameInitials = isChecked;
            }
        });

        sameFinals.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MainActivity.sameFinals = isChecked;
            }
        });

        sameTones.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MainActivity.sameTones = isChecked;
            }
        });

        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.connectionIPAddress = ipAddress.getText().toString();
                MainActivity.autoParse = autoParse.isChecked();

                finish();
            }
        });
    }


}
