package data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = DBHelper.class.getSimpleName();
    String sql;
    /**
     * Имя файла базы данных
     */


    /**
     * Версия базы данных. При изменении схемы увеличить на единицу
     */
    private static final int DATABASE_VERSION = 1;


    public DBHelper(Context context) {
        super(context, "notes", null, DATABASE_VERSION);
        sql = "CREATE TABLE `notes` (\n" +
                "\t`number`\tTEXT,\n" +
                "\t`time`\tINTEGER,\n" +
                "\t`iswait`\tINTEGER\n" +
                ");";
    }

    public DBHelper(Context context, String note) {
        super(context, note, null, 2);
        sql = "CREATE TABLE " + note + " (\n" +
                "\t`message`\tTEXT\n" +
                ");";
    }

    /**
     * Вызывается при создании базы данных
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Строка для создания таблицы


        // Запускаем создание таблицы
        db.execSQL(sql);


    }

    public boolean isCreated(String note){
        String selectQuery = "select number from notes where number = \'" + note+"\'";
        Cursor cursor = null;
        SQLiteDatabase db = this.getWritableDatabase();

        cursor = db.rawQuery(selectQuery, null);

        if(cursor.getCount() == 0){
            cursor.close();
            return false;
        }else{
            cursor.close();
            return true;
        }




    }

    public void addMessages(String note, ArrayList<String> messages){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        for (String message : messages) {
            values.put("message", message);
        }

        db.insert(note, null, values);
        db.close();
    }

    public void addTable(String note, long time, int isWait){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("number", note);
        values.put("time", time);
        values.put("iswait", isWait);

        db.insert("notes", null, values);
        db.close();
    }

    public ArrayList<String> getNotWaits(){
        String sql = "select number from notes where iswait = 0";

        ArrayList<String> list = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()){
            list.add(cursor.getString(cursor.getColumnIndex("number")));
        }

        return list;
    }

    public ArrayList<String> getMessages(ArrayList<String> notes){
        SQLiteDatabase db;

        ArrayList<String> list = new ArrayList<>();

        for (String note : notes) {
            String sql = "select * from \'note121\' where type = \'table\'";
            db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(sql, null);

            cursor.moveToLast();
            list.add(cursor.getString(cursor.getColumnIndex("message")));
            cursor.close();
            db.close();
        }

        return list;
    }

    public void setisWaitNull(String note){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "select iswait from notes where number = \'" + note+"\'";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        int iswait = cursor.getInt(cursor.getColumnIndex("iswait"));
        if(iswait == 1){
            ContentValues values = new ContentValues();
            values.put("iswait", 0);
            db.update("notes", values, "number" +" = ?", new String[]{note});
        }
    }

    public int getCountOfMessage(String note){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "select message from " + note;

        Cursor cursor = db.rawQuery(sql, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public void addMeesage(String note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("message", "some message");

        db.insert(note, null, values);
        db.close();
    }

    /**
     * Вызывается при обновлении схемы базы данных
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
