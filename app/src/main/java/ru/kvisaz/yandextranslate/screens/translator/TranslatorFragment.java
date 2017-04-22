package ru.kvisaz.yandextranslate.screens.translator;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;
import ru.kvisaz.yandextranslate.R;
import ru.kvisaz.yandextranslate.common.CommonTabFragment;
import ru.kvisaz.yandextranslate.data.models.DictArticle;
import ru.kvisaz.yandextranslate.data.models.Language;
import ru.kvisaz.yandextranslate.screens.translator.dict.DictArticleAdapter;

public class TranslatorFragment extends CommonTabFragment implements ITranslatorView {

    @InjectPresenter
    TranslatorPresenter presenter;

    @BindView(R.id.sourceLangSpinner)
    Spinner sourceLangSpinner;

    @BindView(R.id.destLangSpinner)
    Spinner destLangSpinner;

    @BindView(R.id.inputEditText)
    EditText inputEditText;

    @BindView(R.id.sourceTextView)
    TextView sourceTextView;

    @BindView(R.id.translatedTextView)
    TextView translatedTextView;

    @BindView(R.id.dictTypeTextView)
    TextView dictTypeTextView;

    @BindView(R.id.dictRecyclerView)
    RecyclerView dictRecyclerView;

    @BindView(R.id.translateBookmarkCheckbox)
    CheckBox translateBookmarkCheckbox;

    private ArrayAdapter<Language> sourcesSpinnerAdapter;
    private ArrayAdapter<Language> destLanguagesAdapter;
    private DictArticleAdapter dictArticleAdapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_translator;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDictArticleView();
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
        translatedTextView.setText(translated);
    }

    @Override
    public void showDictionaryArticle(DictArticle article) {
        dictTypeTextView.setText(article.type);
        dictArticleAdapter.setArticle(article);
    }

    @Override
    public void showOriginalText(String original) {
        sourceTextView.setText(original);
    }

    @OnClick(R.id.switchDirectionsButton)
    public void onSwitch() {
        presenter.onSwitchLanguagesButtonClick();
    }

    @OnClick(R.id.cancelButton)
    public void onCancel() {
        inputEditText.setText("");
    }

    @OnTextChanged(R.id.inputEditText)
    public void onInputChange(Editable editable) {
        presenter.onInputChanged(editable.toString());
    }

    @OnItemSelected(R.id.sourceLangSpinner)
    public void onSourceSelect(Spinner sourceLangSpinner, int position) {
        if (sourcesSpinnerAdapter == null) return;
        Language source = sourcesSpinnerAdapter.getItem(position);
        presenter.onSourceSelect(source);
    }

    @OnItemSelected(R.id.destLangSpinner)
    public void onDestinationsSelect(Spinner destLangSpinner, int position) {
        if (destLanguagesAdapter == null) return;
        Language dest = destLanguagesAdapter.getItem(position);
        presenter.onDestinationSelect(dest);
    }

    @NonNull
    private ArrayAdapter<Language> createLanguagesAdapter(Language[] languages) {
        return new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, languages);
    }

    private void initDictArticleView() {
        dictArticleAdapter = new DictArticleAdapter(new DictArticle());
        dictRecyclerView.setAdapter(dictArticleAdapter);
        dictRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

}