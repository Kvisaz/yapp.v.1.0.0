package ru.kvisaz.yandextranslate.data.models;

public class SentencePair {
    public final Sentence source;
    public final Sentence destination;

    public SentencePair(Sentence source, Sentence destination){
        this.source = source;
        this.destination = destination;
    }
}
