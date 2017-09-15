package newsfeed.hindu.goku.com.thehindu;

/**
 * Created by goku on 01-02-2015.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by goku on 01-02-2015.
 */
public class SaveDataSource {


    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = {MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_TITLE, MySQLiteHelper.COLUMN_CONTENT, MySQLiteHelper.COLUMN_URL};


    public SaveDataSource(Context context) {

        dbHelper = new MySQLiteHelper(context);

    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }


    public Save createSave(String title, String content, String url) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_TITLE, title);
        values.put(MySQLiteHelper.COLUMN_CONTENT, content);
        values.put(MySQLiteHelper.COLUMN_URL, url);
        long insertId = database.insert(MySQLiteHelper.TABLE_SAVE, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_SAVE,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Save newComment = cursorToSave(cursor);
        cursor.close();
        return newComment;
    }

    public void deleteComment(long id) {
        long Id=id;
        System.out.println("Comment deleted with id: " + Id);
        database.delete(MySQLiteHelper.TABLE_SAVE, MySQLiteHelper.COLUMN_ID
                + " = " + Id, null);
    }

    public List<Save> getAllSave() {
        List<Save> saveData = new ArrayList<Save>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_SAVE,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Save save = cursorToSave(cursor);
            saveData.add(save);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return saveData;
    }


    private Save cursorToSave(Cursor cursor) {
        Save save = new Save();
        save.setId(cursor.getLong(0));
        save.setTitle(cursor.getString(1));
        save.setContnet(cursor.getString(2));
        save.setUrl(cursor.getString(3));
        return save;
    }
}

