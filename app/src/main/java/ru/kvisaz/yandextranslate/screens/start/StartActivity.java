package ru.kvisaz.yandextranslate.screens.start;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.hamza.slidingsquaresloaderview.SlidingSquareLoaderView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.kvisaz.yandextranslate.R;
import ru.kvisaz.yandextranslate.screens.tabcontainer.TabActivity;

public class StartActivity extends MvpAppCompatActivity implements IStartView {
    @InjectPresenter
    StartPresenter presenter;

    @BindView(R.id.messageTitleTextView)
    TextView messageTitleTextView;

    @BindView(R.id.messageContentTextView)
    TextView messageContentTextView;

    @BindView(R.id.okButton)
    Button okButton;

    @BindView(R.id.slidingSquareLoaderView)
    SlidingSquareLoaderView loaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        ButterKnife.bind(this);
        loaderView.start();

        presenter.onActivityCreate();
    }

    @Override
    public void showOfflineScreen() {
        showErrorScreen(getString(R.string.start_screen_internet_error_message_title),
                getString(R.string.start_screen_internet_error_message_content));
    }

    @Override
    public void showErrorScreen(String title, String message) {
        messageTitleTextView.setVisibility(View.VISIBLE);
        messageTitleTextView.setText(title);

        messageContentTextView.setVisibility(View.VISIBLE);
        messageContentTextView.setText(message);

        loaderView.stop();
        loaderView.setVisibility(View.GONE);

        okButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void goToTabActivityScreen() {
        loaderView.stop(); // если не остановить анимацию, работающий ViewAnimator вызовет утечку

        Intent intent = new Intent(this, TabActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.okButton)
    public void onOkButtonClick() {
        presenter.onStartButtonClick();
    }
}