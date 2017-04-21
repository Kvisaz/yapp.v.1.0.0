package ru.kvisaz.yandextranslate.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ru.kvisaz.yandextranslate.data.database.models.HistoryEntity;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class HistoryDatabaseHelper extends SQLiteOpenHelper {

    public HistoryDatabaseHelper(Context context) {
        super(context, DbConstants.HISTORY_DATABASE_NAME, null, DbConstants.HISTORY_DATABASE_VERSION);
    }

    static {
        cupboard().register(HistoryEntity.class);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        cupboard().withDatabase(db).createTables();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        cupboard().withDatabase(db).upgradeTables();
    }
}
