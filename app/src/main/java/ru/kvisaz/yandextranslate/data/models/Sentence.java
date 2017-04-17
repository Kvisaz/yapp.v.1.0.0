package ru.kvisaz.yandextranslate.data.models;

public final class Sentence {
    public final String text;
    public final Language language;

    public Sentence(String text, Language language){
        this.text = text;
        this.language = language;
    }
}