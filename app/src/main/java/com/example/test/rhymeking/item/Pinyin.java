package com.example.test.rhymeking.item;

/**
 * Created by 24254 on 03/03/2017.
 */

public class Pinyin {

    private String pinyin;
    private int label;

    public Pinyin(String pinyin, int label) {
        this.pinyin = pinyin;
        this.label = label;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public int getLabel() {
        return label;
    }


    public void setLabel(int label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return pinyin + " " + label;
    }
}
