package com.example.test.rhymeking.algorithm;

import com.example.test.rhymeking.item.DictionaryInfo;

import java.util.Comparator;

/**
 * Created by 24254 on 22/03/2017.
 */

public class DictionaryInfoComparator implements Comparator<DictionaryInfo> {

    @Override
    public int compare(DictionaryInfo dictionaryInfo1, DictionaryInfo dictionaryInfo2) {
        return Integer.parseInt(dictionaryInfo2.getQuantity()) - Integer.parseInt(dictionaryInfo1.getQuantity());
    }
}

