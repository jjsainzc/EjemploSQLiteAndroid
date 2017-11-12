package org.aplicaciones.alienware.ejemplosqliteandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.aplicaciones.alienware.ejemplosqliteandroid.utilidades.DatabaseCustomUtils;


public class Principal extends AppCompatActivity {

    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        StringBuilder sb = new StringBuilder();
        String databaseDir = "/data/data/" + getPackageName() + "/databases";
        String databaseName = "mi_database.db";

        if (!DatabaseCustomUtils.checkDatabase(databaseDir + "/" + databaseName)) {
            DatabaseCustomUtils.copyDatabaseFromAssets(getApplicationContext(), databaseName, databaseDir);
        }

        db = SQLiteDatabase.openDatabase(databaseDir + "/" + databaseName, null, SQLiteDatabase.OPEN_READWRITE);

        Log.i("COUNT", String.valueOf(DatabaseCustomUtils.countRows(db, "persona", null)) );

        db.execSQL("INSERT INTO persona (nombre,fecha,estatura) VALUES ('Jorge','1964-06-30',1.5)");

        Cursor cursor = db.rawQuery("SELECT * FROM persona", new String[]{});

        ContentValues registro = new ContentValues();

        if (cursor != null) {
            cursor.moveToFirst();
            do {
                DatabaseUtils.cursorRowToContentValues(cursor, registro);
                sb.append(registro.toString());
               /*
                for (String colName : cursor.getColumnNames()) {
                    int colIdx = cursor.getColumnIndex(colName);
                    String valor = cursor.getString(colIdx);
                    sb.append(colName).append("=").append(valor).append("\t");
                }
                */
                sb.append("\n");

            } while (cursor.moveToNext());
            cursor.close();
        }
        Log.i("DATOS", sb.toString());

        db.execSQL("DELETE FROM persona WHERE nombre='Jorge'");
    }
}
