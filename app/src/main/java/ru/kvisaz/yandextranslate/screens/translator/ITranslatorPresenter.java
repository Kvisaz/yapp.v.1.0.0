package ru.kvisaz.yandextranslate.screens.translator;

import ru.kvisaz.yandextranslate.data.models.Language;

public interface ITranslatorPresenter {

    void onViewCreated();

    void onSourceSelect(Language sourceLanguage);

    void onDestinationSelect(Language destinationLanguage);

    void onSwitchLanguagesButtonClick();

    void onMakeFavoriteCheck(Boolean checked);

    void onShareButtonClick();

    void onCopyButtonClick();

    void onInputChanged(String input);

    void onOfflineButtonClick();

    void onSourceVoiceInputButtonClick();

    void onSourceVocalizeButtonClick();

    void onTranslateVocalizeButtonClick();

}