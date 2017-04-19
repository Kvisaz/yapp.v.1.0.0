package ru.kvisaz.yandextranslate.data.rest.models;

import java.util.List;

public class DictTr {
    public String text;
    public String pos;
    public List<DictSyn> syn = null;
    public List<DictMean> mean = null;
    public List<DictEx> ex = null;
}
