package com.example.test.rhymeking.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.test.rhymeking.MainActivity;
import com.example.test.rhymeking.R;
import com.ms.square.android.expandabletextview.ExpandableTextView;

public class ResultTableAdapter extends BaseAdapter {

    private Context context;
    private SparseBooleanArray sparseBooleanArray;

    public ResultTableAdapter(Context context) {
        this.context = context;
        this.sparseBooleanArray = new SparseBooleanArray();
    }

    @Override
    public int getCount() {
        return MainActivity.rhymeWordsList.size();
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

        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.expandableTextView = (ExpandableTextView) convertView.findViewById(R.id.expand_text_view);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.expandableTextView.setText(MainActivity.rhymeWordsList.get(position), sparseBooleanArray, position);

        return convertView;
    }

    private static class ViewHolder {
        ExpandableTextView expandableTextView;
    }
}