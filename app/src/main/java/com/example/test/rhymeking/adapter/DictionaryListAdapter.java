package com.example.test.rhymeking.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.test.rhymeking.algorithm.DBTools;
import com.example.test.rhymeking.item.DictionaryInfo;
import com.example.test.rhymeking.R;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import java.util.List;


/**
 * Created by 24254 on 06/03/2017.
 */

public class DictionaryListAdapter extends BaseAdapter {

    private Context context;
    private List<DictionaryInfo> catalogue;

    public DictionaryListAdapter(Context context, List<DictionaryInfo> catalogue) {
        this.context = context;
        this.catalogue = catalogue;
    }

    @Override
    public int getCount() {
        return catalogue.size();
    }

    @Override
    public Object getItem(int position) {
        return catalogue.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null)
            convertView = View.inflate(context, R.layout.dictonaries_list_item, null);

        final DictionaryInfo dictionary = catalogue.get(position);
        boolean downloaded = false;

        final TextView dictionaryInfo = (TextView) convertView.findViewById(R.id.dictionary_info);
        final Button btn_download = (Button) convertView.findViewById(R.id.download);
        final CheckBox cb_selected = (CheckBox) convertView.findViewById(R.id.selected);

        dictionaryInfo.setText(dictionary.getString());

        DB database = null;
        try {
            database = DBFactory.open(context, "dictionaries");
            String hexCode = catalogue.get(position).getHexCode();

            // If this dictionary has already been downloaded, update view
            if (database.exists(hexCode))
                downloaded = true;
            database.close();
        } catch (SnappydbException e) {
            e.printStackTrace();
        }

        // If this dictionary has been downloaded already, disable the btn_download and enable selection
        if (downloaded) {
            cb_selected.setVisibility(View.VISIBLE);
            btn_download.setVisibility(View.GONE);
            if (dictionary.isSelected())
                cb_selected.setChecked(true);
            else
                cb_selected.setChecked(false);

            cb_selected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    dictionary.select();
                }
            });

        } else {
            // If this dictionary has not been downloaded, enable the download button
            cb_selected.setVisibility(View.GONE);
            btn_download.setVisibility(View.VISIBLE);
            btn_download.setText("下载");
            btn_download.setTextColor(Color.BLACK);
            btn_download.setClickable(true);

            btn_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String hexCode = catalogue.get(position).getHexCode();
                    int maxLength = catalogue.get(position).getMaxLength();

                    DBTools.addDictionary(context, hexCode, cb_selected, dictionary, btn_download, maxLength);
                }
            });
        }

        return convertView;
    }
}


