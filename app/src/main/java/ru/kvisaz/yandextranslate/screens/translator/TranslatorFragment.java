package ru.kvisaz.yandextranslate.screens.translator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnItemSelected;
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

    private ArrayAdapter<Language> sourcesSpinnerAdapter;
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
        sourcesSpinnerAdapter = createLanguagesAdapter(languages);
        sourceLangSpinner.setAdapter(sourcesSpinnerAdapter);
    }

    @Override
    public void setDestinationLanguages(Language[] languages) {
        destLanguagesAdapter = createLanguagesAdapter(languages);
        destLangSpinner.setAdapter(destLanguagesAdapter);
    }

    @Override
    public void selectSourceLanguage(Language language) {
        int spinnerPosition = sourcesSpinnerAdapter.getPosition(language);
        sourceLangSpinner.setSelection(spinnerPosition);
    }

    @Override
    public void selectDestinationLanguage(Language language) {
        destLangSpinner.setSelection(destLanguagesAdapter.getPosition(language));
    }

    @Override
    public void showTranslatedText(String translated) {

    }

    @Override
    public void showOriginalText(String original) {

    }

    @OnItemSelected(R.id.sourceLangSpinner)
    public void onSourceSelect(Spinner sourceLangSpinner, int position){
        if(sourcesSpinnerAdapter==null) return;
        Language source =  sourcesSpinnerAdapter.getItem(position);
        presenter.onSourceSelect(source);
    }

    @OnItemSelected(R.id.destLangSpinner)
    public void onDestinationsSelect(Spinner destLangSpinner, int position){
        if(destLanguagesAdapter==null) return;
        Language dest = destLanguagesAdapter.getItem(position);
        presenter.onDestinationSelect(dest);
    }

    private ArrayAdapter<Language> createLanguagesAdapter(Language[] languages) {
        return new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, languages);
    }

}