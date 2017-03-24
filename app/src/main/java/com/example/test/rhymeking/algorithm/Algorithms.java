package com.example.test.rhymeking.algorithm;

import android.util.Log;

import com.example.test.rhymeking.item.Phrase;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 24254 on 07/03/2017.
 */

public class Algorithms {

    public static String displayingFormat(List<String> list) {

        Log.e("TAG", "size = " + list.size());

        String displayingFormat = "";
        for (String str : list)
            displayingFormat += str + " ";

        Log.e("TAG", displayingFormat);

        return displayingFormat;
    }

    public static Phrase asPhrase(String word, List<Phrase> selectedDictionariesPhrases) {

        for (Phrase phrase : selectedDictionariesPhrases)
            if (phrase.getCharacters().equals(word))
                return phrase;

        return null;
    }

    public static List<String> getRhymePinyins(List<List<String>> rhymeTable, String finals) throws JSONException {

        for (int i = 0; i < rhymeTable.size(); i++) {
            List<String> tempList = rhymeTable.get(i);

            if (tempList.contains(finals))
                return tempList;
        }
        return null;
    }

    public static List<String> toList(JSONArray array) throws JSONException {

        List<String> list = new ArrayList<String>();

        for (int i = 0; i < array.length(); i++) {
            list.add(array.getString(i));
        }

        return list;
    }

    public static List<Phrase> toPhraseList(JSONArray array) throws JSONException {

        List<Phrase> list = new ArrayList<Phrase>();

        for (int i = 0; i < array.length(); i++) {
            list.add(new Phrase(array.getString(i)));
        }

        Log.e("TAG", list.size() + "");
        return list;
    }
}
