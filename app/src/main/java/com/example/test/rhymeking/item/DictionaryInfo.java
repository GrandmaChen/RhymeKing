package com.example.test.rhymeking.item;

import android.content.Context;
import android.util.Log;

import com.example.test.rhymeking.MainActivity;
import com.example.test.rhymeking.algorithm.DBTools;


/**
 * Created by 24254 on 28/02/2017.
 */

public class DictionaryInfo {

    private Context context;
    private String name;
    private String hexCode;
    private String quantity;
    private int maxLength;
    private boolean selected;

    public DictionaryInfo(Context context, String name, String hexCode, String quantity, int maxLength, boolean selected) {
        this.context = context;
        this.name = name;
        this.hexCode = hexCode;
        this.quantity = quantity;
        this.maxLength = maxLength;
        this.selected = selected;
    }

    // Switch and return the value of selected
    public boolean select() {

        this.selected = !selected;

        // Add all phrases into phrase pool
        if (selected) {
            for (Phrase phrase : DBTools.getPhrases(this.context, this.hexCode))
                MainActivity.phrasePool.add(phrase);
        }

        // Delete all phrases from phrase pool
        else
            for (int i = 0; i < MainActivity.phrasePool.size(); i++)
                if (MainActivity.phrasePool.get(i).getHexCode().equals(hexCode)) {
                    MainActivity.phrasePool.remove(i);
                    i--;
                }

        MainActivity.message.setText("目前选中词库量:" + MainActivity.phrasePool.size());

        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public String getName() {
        return name;
    }

    public String getHexCode() {
        return hexCode;
    }

    public String getQuantity() {
        return quantity;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public String getString() {
        return "词库名: " + name + "\n词汇量: " + quantity + (selected ? "√" : "");
    }
}
