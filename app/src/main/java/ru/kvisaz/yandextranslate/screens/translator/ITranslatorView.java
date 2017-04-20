package ru.kvisaz.yandextranslate.screens.translator;

import com.arellomobile.mvp.MvpView;

import ru.kvisaz.yandextranslate.data.models.DictArticle;
import ru.kvisaz.yandextranslate.data.models.Language;

public interface ITranslatorView extends MvpView {
    void setSourceLanguages(Language[] languages);

    void selectSourceLanguage(Language language);

    void setDestinationLanguages(Language[] languages);

    void selectDestinationLanguage(Language language);

    void showTranslatedText(String translated);

    void showOriginalText(String original);

    void showDictionaryArticle(DictArticle article);
}