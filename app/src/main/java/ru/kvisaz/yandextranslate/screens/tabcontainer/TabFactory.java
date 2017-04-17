package ru.kvisaz.yandextranslate.screens.tabcontainer;

import android.support.v4.app.Fragment;

import ru.kvisaz.yandextranslate.screens.history.HistoryFraqment;
import ru.kvisaz.yandextranslate.screens.settings.SettingsFragment;
import ru.kvisaz.yandextranslate.screens.translator.TranslatorFragment;

public class TabFactory {
    public static Fragment createTabFragment(int position) {
        switch (position) {
            case 0:
                return new TranslatorFragment();
            case 1:
                return new HistoryFraqment();
            case 2:
                return new SettingsFragment();
        }
        return new TranslatorFragment();
    }
}
