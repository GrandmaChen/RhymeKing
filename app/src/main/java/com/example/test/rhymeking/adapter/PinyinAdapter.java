package com.example.test.rhymeking.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.test.rhymeking.MainActivity;

/**
 * Created by 24254 on 06/03/2017.
 */

public class PinyinAdapter extends BaseAdapter {

    private Context context;

    public PinyinAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return MainActivity.pinyins.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView textView = new TextView(context);

        textView.setPadding(5, 2, 5, 2);
        textView.setText(MainActivity.pinyins.get(position).toString());

        return textView;
    }

}
