package ru.kvisaz.yandextranslate.speech;

import android.content.Context;

public interface IVocalizerListenerOwner {
    Context getContext();
    void showLoadingIndicator(boolean visible);
    void showMessage(String message);
    void resetVocalizer();
}
