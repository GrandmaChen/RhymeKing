package com.example.test.rhymeking.item;

import java.util.List;

public class Phrase {

    private String hexCode;
    private String characters;
    private int length;
    private String[] initials;
    private String[] finals;
    private int[] tones;

    public Phrase() {
    }

    public Phrase(String asString) {

        String[] info = asString.split("\\|");
        this.hexCode = info[0];
        this.characters = info[1];
        this.length = this.characters.length();
        this.initials = info[2].split("\\s");
        this.finals = info[3].split("\\s");

        this.tones = new int[info[4].length()];

        for (int i = 0; i < tones.length; i++)
            tones[i] = Integer.parseInt(info[4].substring(i, i + 1));

    }

    public Phrase(String hexCode, String characters, String[] initials, String[] finals, int[] tones) {

        this.hexCode = hexCode;
        this.characters = characters;
        this.length = characters.length();
        this.initials = initials;
        this.finals = finals;
        this.tones = tones;
    }

    public Phrase(String hexCode, String characters, List<String> initials, List<String> finals, List<String> tones) {

        this.hexCode = hexCode;
        this.characters = characters;

        int length = characters.length();
        this.length = length;
        this.initials = new String[length];
        this.finals = new String[length];
        this.tones = new int[length];
        for (int i = 0; i < length; i++) {
            this.initials[i] = initials.get(i);
            this.finals[i] = finals.get(i);
            this.tones[i] = Integer.parseInt(tones.get(i));
        }
    }


    public boolean equals(Phrase phrase) {

        if (!phrase.characters.equals(characters))
            return false;

        int length = characters.length();

        for (int i = 0; i < length; i++)
            if (!initials[i].equals(phrase.getInitials()[i]) || !finals[i].equals(phrase.getFinals()[i])
                    || tones[i] != phrase.getTones()[i])
                return false;

        return true;
    }

    public boolean rhymes(Phrase candidate, boolean sameInitial, boolean sameFinal, boolean sameTone,
                          boolean matchAll) {

        Phrase thisPhrase = copy();

        // Cut both phrase to same length
        if (thisPhrase.getLength() > candidate.getLength())
            thisPhrase = thisPhrase.subPhrase(thisPhrase.getLength() - candidate.getLength(), thisPhrase.getLength());

        if (candidate.getLength() > this.length)
            candidate = candidate.subPhrase(candidate.getLength() - this.length, candidate.getLength());

        // If not all rhyme, only last character needs to match the candidate
        int times = 1;
        if (matchAll)
            times = thisPhrase.getLength();

        // Match initials
        if (sameInitial)
            for (int i = 0; i < times; i++)
                if (!thisPhrase.getInitials()[i].equals(candidate.getInitials()[i]))
                    return false;

        // Match finals
        if (sameFinal)
            for (int i = 0; i < times; i++)
                if (!thisPhrase.getFinals()[i].equals(candidate.getFinals()[i]))
                    return false;

        // Match tones
        if (sameTone)
            for (int i = 0; i < times; i++)
                if (thisPhrase.getTones()[i] != candidate.getTones()[i])
                    return false;

        return true;
    }

    public Phrase subPhrase(int beginIndex, int endIndex) {

        int length = endIndex - beginIndex;

        String[] newInitials = new String[length];
        String[] newFinals = new String[length];
        int[] newTones = new int[length];

        for (int i = 0; i < length; i++) {
            newInitials[length - i - 1] = initials[this.length - 1 - (i + beginIndex)];
            newFinals[length - i - 1] = finals[this.length - 1 - (i + beginIndex)];
            newTones[length - i - 1] = tones[this.length - 1 - (i + beginIndex)];
        }

        return new Phrase(this.hexCode, characters.substring(beginIndex, endIndex), newInitials, newFinals, newTones);
    }

    public String getHexCode() {
        return hexCode;
    }

    public int getLength() {
        return length;
    }

    public String getCharacters() {
        return characters;
    }

    public String[] getInitials() {
        return initials;
    }

    public String[] getFinals() {
        return finals;
    }

    public int[] getTones() {
        return tones;
    }

    public Phrase copy() {
        return new Phrase(this.hexCode, this.characters, this.initials, this.finals, this.tones);
    }

    @Override
    public String toString() {

        String initials = "";
        String finals = "";
        String tones = "";

        for (int i = 0; i < characters.length(); i++) {
            if (i < characters.length() - 1) {
                initials += this.initials[i] + " ";
                finals += this.finals[i] + " ";
            } else {
                initials += this.initials[i];
                finals += this.finals[i];
            }
            tones += this.tones[i];
        }

        return hexCode + "|" + characters + "|" + initials + "|" + finals + "|" + tones;
    }

}
