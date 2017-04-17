package ru.kvisaz.yandextranslate.screens.translator;

import com.arellomobile.mvp.MvpView;

import java.util.List;

import ru.kvisaz.yandextranslate.data.models.Language;

public interface ITranslatorView extends MvpView {

    // todo make custom Language array adapters for spinners
    void setSourceLanguages(List<Language> languages);
    void setDestinationLanguages(List<Language> languages);

    void showTranslatedText(String translated);
    void showOriginalText(String original);
}