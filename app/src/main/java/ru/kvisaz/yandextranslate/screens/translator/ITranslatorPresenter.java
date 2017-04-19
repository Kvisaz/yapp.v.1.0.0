package ru.kvisaz.yandextranslate.screens.translator;

import android.text.Editable;

import ru.kvisaz.yandextranslate.data.models.Language;

public interface ITranslatorPresenter {

    void onStart();

    void onSourceSelect(Language sourceLanguage);

    void onDestinationSelect(Language destinationLanguage);

    void onSwitchLanguagesButtonClick();

    void onMakeFavoriteCheck(Boolean checked);

    void onSourceSoundButtonClick();

    void onDestinationSoundButtonClick();

    void onShareButtonClick();

    void onCopyButtonClick();

    void onInputChanged(Editable editable);
}