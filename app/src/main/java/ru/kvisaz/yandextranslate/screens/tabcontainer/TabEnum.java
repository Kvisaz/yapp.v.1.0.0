package ru.kvisaz.yandextranslate.screens.tabcontainer;

import ru.kvisaz.yandextranslate.R;

public enum TabEnum {
    TRANSLATOR(R.drawable.ic_action_translate, "TRANSLATOR_FRAGMENT"),
    HISTORY(R.drawable.ic_action_bookmark, "HISTORY_FRAGMENT"),
    SETTINGS(R.drawable.ic_action_settings, "SETTINGS_FRAGMENT");

    final public int iconResId;
    final public String fragmentTag;

    TabEnum(int iconResId, String fragmentTag) {
        this.iconResId = iconResId;
        this.fragmentTag = fragmentTag;
    }
}