package ru.kvisaz.yandextranslate.screens.translator;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;
import ru.kvisaz.yandextranslate.ApiKeys;
import ru.kvisaz.yandextranslate.Constants;
import ru.kvisaz.yandextranslate.R;
import ru.kvisaz.yandextranslate.common.CommonTabFragment;
import ru.kvisaz.yandextranslate.common.utils.KeyboardUtils;
import ru.kvisaz.yandextranslate.data.models.DictArticle;
import ru.kvisaz.yandextranslate.data.models.Language;
import ru.kvisaz.yandextranslate.data.models.Translate;
import ru.kvisaz.yandextranslate.screens.start.StartActivity;
import ru.kvisaz.yandextranslate.screens.translator.dict.DictArticleAdapter;
import ru.kvisaz.yandextranslate.speech.IVocalizerListenerOwner;
import ru.kvisaz.yandextranslate.speech.MyVocalizerListener;
import ru.yandex.speechkit.Recognizer;
import ru.yandex.speechkit.SpeechKit;
import ru.yandex.speechkit.Vocalizer;
import ru.yandex.speechkit.gui.RecognizerActivity;

public class TranslatorFragment extends CommonTabFragment implements ITranslatorView, IVocalizerListenerOwner {

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

    @BindView(R.id.offlineBgView)
    View offlineBgView;

    @BindView(R.id.offlineMessageTextView)
    TextView offlineMessageTextView;

    @BindView(R.id.offlineButton)
    Button offlineButton;

    @BindView(R.id.sourceVoiceRecognizerButton)
    ImageButton sourceVoiceRecognizerButton;

    @BindView(R.id.sourceVocalizeButton)
    ImageButton sourceVocalizeButton;

    @BindView(R.id.translateVocalizeButton)
    ImageButton translateVocalizeButton;

    @BindView(R.id.sourceVoiceRecognizeProgressBar)
    ProgressBar sourceVoiceRecognizeProgressBar;

    @BindView(R.id.sourceVocalizeProgressBar)
    ProgressBar sourceVocalizeProgressBar;

    @BindView(R.id.translateVocalizeProgressBar)
    ProgressBar translateVocalizeProgressBar;

    private ArrayAdapter<Language> sourcesSpinnerAdapter;
    private ArrayAdapter<Language> destLanguagesAdapter;
    private DictArticleAdapter dictArticleAdapter;

    private Vocalizer vocalizer;
    private MyVocalizerListener vocalizerListener;
    private int vocalizerTag; // для пометки, на какой кнопке показывать индикатор ожидания и других полезных вещей

    private final int VOICE_RECOGNIZER_REQUEST_CODE = 1231;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_translator;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDictArticleView();
        vocalizerListener = new MyVocalizerListener(this);
        SpeechKit.getInstance().configure(getContext(), ApiKeys.API_VOICE_KEY_VALUE);
        presenter.onViewCreated();
    }

    @Override
    public void showOfflineScreen(boolean visible) {
        int visibility = visible ? View.VISIBLE : View.GONE;
        offlineBgView.setVisibility(visibility);
        offlineMessageTextView.setVisibility(visibility);
        offlineButton.setVisibility(visibility);
    }

    @Override
    protected void onVisible() {
        KeyboardUtils.hideKeyboard(this); // просто убираем клавиатуру при переходах между фрагментами
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

    @Override
    public void cancelFavorite(boolean wasChecked) {
        translateBookmarkCheckbox.setChecked(!wasChecked);
    }

    @Override
    public void goToStartActivity() {
        Intent intent = new Intent(getActivity(), StartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void showMessage(String message) {
        Log.d(Constants.LOG_TAG, message);
    }

    @Override
    public void showTranslate(Translate translate) {
        KeyboardUtils.hideKeyboard(this);
        showOriginalText(translate.getSource());
        showTranslatedText(translate.getText());
        showDictionaryArticle(translate.getDictArticle());
    }

    @Override
    public void resetVocalizer() {
        sourceVocalizeButton.setVisibility(View.VISIBLE);
        translateVocalizeButton.setVisibility(View.VISIBLE);

        sourceVocalizeProgressBar.setVisibility(View.GONE);
        translateVocalizeProgressBar.setVisibility(View.GONE);

        if (vocalizer != null) {
            vocalizer.cancel();
        }
        vocalizer = null;
    }

    @Override
    public void showLoadingIndicator(boolean visible) {
        int visibility = visible ? View.VISIBLE : View.GONE;
        int buttonVisibilty = !visible ? View.VISIBLE : View.INVISIBLE;
        switch (vocalizerTag) {
            case TranslatorPresenter.SOURCE_VOCALIZER:
                sourceVocalizeProgressBar.setVisibility(visibility);
                sourceVocalizeButton.setVisibility(buttonVisibilty);
                break;
            case TranslatorPresenter.TRANSLATE_VOCALIZER:
                translateVocalizeProgressBar.setVisibility(visibility);
                translateVocalizeButton.setVisibility(buttonVisibilty);
                break;
        }
    }

    @Override
    public void vocalize(String text, int vocalizerTag) {
        this.vocalizerTag = vocalizerTag;
        resetVocalizer();
        vocalizer = Vocalizer.createVocalizer(Vocalizer.Language.RUSSIAN, text, true, Vocalizer.Voice.ALYSS);
        vocalizer.setListener(vocalizerListener);
        vocalizer.start();
    }

    @Override
    public void voiceRecognize() {
        // To start recognition create an Intent with required extras.
        Intent intent = new Intent(getActivity(), RecognizerActivity.class);
        // Specify the model for better results.
        intent.putExtra(RecognizerActivity.EXTRA_MODEL, Recognizer.Model.QUERIES);
        // Specify the language.
        intent.putExtra(RecognizerActivity.EXTRA_LANGUAGE, Recognizer.Language.RUSSIAN);
        // To get recognition results use startActivityForResult(),
        // also don't forget to override onActivityResult().
        startActivityForResult(intent, VOICE_RECOGNIZER_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VOICE_RECOGNIZER_REQUEST_CODE) {
            if (resultCode == RecognizerActivity.RESULT_OK && data != null) {
                final String result = data.getStringExtra(RecognizerActivity.EXTRA_RESULT);
                inputEditText.setText(result);
            } else if (resultCode == RecognizerActivity.RESULT_ERROR) {
                String errorMessage = ((ru.yandex.speechkit.Error) data.getSerializableExtra(RecognizerActivity.EXTRA_ERROR)).getString();
                showMessage(errorMessage);
            }
        }
    }

    @OnClick(R.id.offlineButton)
    public void onOfflineClick(View view) {
        presenter.onOfflineButtonClick();
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

    @OnCheckedChanged(R.id.translateBookmarkCheckbox)
    public void onCheckedChange(boolean checked) {
        presenter.onMakeFavoriteCheck(checked);
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

    @OnClick(R.id.sourceVoiceRecognizerButton)
    public void onRecognizerButtonClick(){
        presenter.onSourceVoiceInputButtonClick();
    }

    @OnClick(R.id.translateVocalizeButton)
    public void onTranslateVoicePlay() {
        presenter.onTranslateVocalizeButtonClick();
    }

    @OnClick(R.id.sourceVocalizeButton)
    public void onInputVocalize() {
        presenter.onSourceVocalizeButtonClick();
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