package ru.kvisaz.yandextranslate.common;

import ru.kvisaz.yandextranslate.Constants;

public class RxRestService extends RxService {

    protected String buldLangParam(String srcCode, String destCode) {
        return srcCode + Constants.DEFAULT_DIRECTION_DELIMITER + destCode;
    }
}
