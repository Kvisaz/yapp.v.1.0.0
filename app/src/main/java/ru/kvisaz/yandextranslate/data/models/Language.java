package ru.kvisaz.yandextranslate.data.models;

public final class Language {
    public final String code;
    public final String description;

    public Language(String code, String description){
        this.code = code;
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }

    @Override
    public boolean equals(Object obj) {
        boolean isEqual = false;
        if(obj !=null && obj instanceof Language){
            isEqual = ((Language) obj).code.equals(code);
        }
        return isEqual;
    }
}