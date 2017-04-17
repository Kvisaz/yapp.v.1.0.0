package ru.kvisaz.yandextranslate.screens.translator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.arellomobile.mvp.presenter.InjectPresenter;

import butterknife.BindView;
import ru.kvisaz.yandextranslate.R;
import ru.kvisaz.yandextranslate.common.CommonTabFragment;
import ru.kvisaz.yandextranslate.data.models.Language;

public class TranslatorFragment extends CommonTabFragment implements ITranslatorView {

    @InjectPresenter
    TranslatorPresenter presenter;

    @BindView(R.id.sourceLangSpinner)
    Spinner sourceLangSpinner;

    @BindView(R.id.destLangSpinner)
    Spinner destLangSpinner;

    private ArrayAdapter<Language> sourceLanguagesAdapter;
    private ArrayAdapter<Language> destLanguagesAdapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_translator;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.onStart();
    }

    @Override
    public void setSourceLanguages(Language[] languages) {
        sourceLanguagesAdapter = createLanguagesAdapter(languages);
        sourceLangSpinner.setAdapter(sourceLanguagesAdapter);
    }

    @Override
    public void setDestinationLanguages(Language[] languages) {
        destLanguagesAdapter = createLanguagesAdapter(languages);
        destLangSpinner.setAdapter(destLanguagesAdapter);
    }

    @Override
    public void selectSourceLanguage(Language language) {
        int spinnerPosition = sourceLanguagesAdapter.getPosition(language);
        sourceLangSpinner.setSelection(spinnerPosition);
    }

    @Override
    public void selectDestinationLanguage(Language language) {
        destLangSpinner.setSelection(sourceLanguagesAdapter.getPosition(language));
    }

    @Override
    public void showTranslatedText(String translated) {

    }

    @Override
    public void showOriginalText(String original) {

    }

    private ArrayAdapter<Language> createLanguagesAdapter(Language[] languages) {
        return new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, languages);
    }

}