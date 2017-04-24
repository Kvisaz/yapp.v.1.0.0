package ru.kvisaz.yandextranslate.screens.translator;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.kvisaz.yandextranslate.data.models.DictArticle;
import ru.kvisaz.yandextranslate.data.models.Language;

public interface ITranslatorView extends MvpView {

    @StateStrategyType(SingleStateStrategy.class)
    void showOfflineScreen(boolean visible);

    @StateStrategyType(SkipStrategy.class)
    void goToStartActivity();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setSourceLanguages(Language[] languages);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void selectSourceLanguage(Language language);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setDestinationLanguages(Language[] languages);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void selectDestinationLanguage(Language language);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showTranslatedText(String translated);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showOriginalText(String original);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showDictionaryArticle(DictArticle article);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void cancelFavorite(boolean wasChecked);

    @StateStrategyType(SkipStrategy.class)
    void vocalize(String text);
}