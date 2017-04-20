package ru.kvisaz.yandextranslate.data.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import ru.kvisaz.yandextranslate.Constants;
import ru.kvisaz.yandextranslate.data.rest.models.LanguagesResponse;

public class LanguagesInfo {
    private static final String DIRS_DELIMITER = Constants.DEFAULT_DIRECTION_DELIMITER;

    private final LanguagesResponse mLanguagesResponse;

    private final SortedMap<String, List<String>> sourceLanguages;

    public LanguagesInfo(LanguagesResponse languagesResponse) {
        mLanguagesResponse = languagesResponse;
        sourceLanguages = makeDirections(mLanguagesResponse.dirs);
    }

    public boolean hasSource(Language language){
        boolean has = false;
        if(sourceLanguages.containsKey(language.code)){
            has = true;
        }
        return has;
    }

    @NonNull
    public Language getDefaultSourceLanguage() {
        return new Language(Constants.DEFAULT_LANGUAGE_CODE, Constants.DEFAULT_LANGUAGE_DESCRIPTION);
    }

    @Nullable
    public Language getDefaultDestination(Language source){
        String sourceCode = source.code;
        String defaultCode = Constants.DEFAULT_LANGUAGE_CODE;
        if(sourceCode.equals(defaultCode)){
            defaultCode = Constants.DEFAULT_LANGUAGE_2_CODE;
        }

        List<String> destinations = sourceLanguages.get(sourceCode);
        if(destinations == null || destinations.size()==0){
            return null;
        }

        if(!destinations.contains(defaultCode)){
            defaultCode = destinations.get(0);
        }

        return getLanguage(defaultCode);
    }

    @NonNull
    public Language[] getSourceLanguages() {
        List<String> sourceCodes = new ArrayList<>(sourceLanguages.keySet());
        return getLanguagesFromCodes(sourceCodes);
    }

    @NonNull
    public Language[] getDestinations(String code) {
        Language[] destinations = new Language[0];
        if(sourceLanguages.containsKey(code)){
            destinations = getLanguagesFromCodes(sourceLanguages.get(code));
        }
        return destinations;
    }

    @Nullable
    public Language getLanguage(String code) {
        Language language = null;
        if (mLanguagesResponse.langs.containsKey(code)) {
            String description = mLanguagesResponse.langs.get(code);
            language = new Language(code, description);
        }

        return language;
    }

    private SortedMap<String, List<String>> makeDirections(String[] dirs) {
        SortedMap<String, List<String>> directions = new TreeMap<>(new DescriptionComparator());

        List<String> destList;
        for (String dir : dirs) {
            String[] dirArr = dir.split(DIRS_DELIMITER);
            String src = dirArr[0];
            String dest = dirArr[1];

            // добавляем возможные цели перевода для исходного языка с кодом src
            if (directions.containsKey(src)) {
                destList = directions.get(src);
            } else {
                destList = new ArrayList<>();
                directions.put(src, destList);
            }

            destList.add(dest);
        }

        return directions;
    }

    @NonNull
    private Language[] getLanguagesFromCodes(List<String> codes) {
        Language[] langDescriptions = new Language[codes.size()];
        int i = 0;
        for (String code : codes) {
            langDescriptions[i] = getLanguage(code);
            i++;
        }
        return langDescriptions;
    }

    @NonNull
    private String getDescription(String code) {
        Map<String, String> descriptions = mLanguagesResponse.langs;
        String description = "";
        if (descriptions != null && descriptions.containsKey(code)) {
            description = descriptions.get(code);
        }
        return description;
    }

    private class DescriptionComparator implements Comparator<String>{
        @Override
        public int compare(String code1, String code2) {
            return getDescription(code1).compareTo(getDescription(code2));
        }
    }
}