package ru.kvisaz.yandextranslate.speech;

import ru.kvisaz.yandextranslate.R;
import ru.yandex.speechkit.Error;
import ru.yandex.speechkit.Synthesis;
import ru.yandex.speechkit.Vocalizer;
import ru.yandex.speechkit.VocalizerListener;

public class MyVocalizerListener implements VocalizerListener {
    private final IVocalizerListenerOwner owner;

    public MyVocalizerListener(IVocalizerListenerOwner owner) {
        this.owner = owner;
    }

    @Override
    public void onSynthesisBegin(Vocalizer vocalizer) {
        owner.showLoadingIndicator(true);
        owner.showMessage(owner.getContext().getString(R.string.vocalizer_synt_begin));
    }

    @Override
    public void onSynthesisDone(Vocalizer vocalizer, Synthesis synthesis) {
        owner.showMessage(owner.getContext().getString(R.string.vocalizer_synt_done));
    }

    @Override
    public void onPlayingBegin(Vocalizer vocalizer) {
        owner.showLoadingIndicator(false);
        owner.showMessage(owner.getContext().getString(R.string.vocalizer_play_begin));
    }

    @Override
    public void onPlayingDone(Vocalizer vocalizer) {
        owner.showMessage(owner.getContext().getString(R.string.vocalizer_play_done));
    }

    @Override
    public void onVocalizerError(Vocalizer vocalizer, Error error) {
        owner.showMessage(owner.getContext().getString(R.string.vocalizer_error));
        owner.showMessage(error.getString());
        owner.resetVocalizer();
    }
}