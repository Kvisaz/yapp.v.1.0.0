package ru.kvisaz.yandextranslate.screens.translator;

import com.arellomobile.mvp.MvpView;

import java.util.List;
import java.util.Map;

import ru.kvisaz.yandextranslate.data.models.Language;

public interface ITranslatorView extends MvpView {

    // todo make custom Language array adapters for spinners

    void setSourceLanguages(Language[] languages);
    void selectSourceLanguage(Language language);
    void setDestinationLanguages(Language[] languages);
    void selectDestinationLanguage(Language language);

    void showTranslatedText(String translated);
    void showOriginalText(String original);
}