package simpleweather.simpleweather.database;

/**
 * Created by acer on 2016/1/21.
 */

import java.io.InputStream;
//import org.apache.http.util.EncodingUtils;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.os.IBinder;
import android.util.Log;


public class CityDB extends SQLiteOpenHelper {
    private final String DB_NAME="CityID";//数据库名称

    public CityDB(Context context, String name, CursorFactory factory,
                  int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createDB= "create table "+DB_NAME+"(cityid varchar(14) primary key , cityname varchar(20),type int)";
        db.execSQL(createDB);//创建数据库
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTableSQL = "DROP TABLE IF EXISTS "  + DB_NAME + " ";
        db.execSQL(dropTableSQL);
        dropTableSQL = "DROP TABLE IF EXISTS " + DB_NAME   + " ";
        db.execSQL(dropTableSQL);
        onCreate(db);
    }

    public void execSQL(String sql, Object[] args) {//执行操作
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sql, args);
    }

    public Cursor query(String sql, String[] args) {//返回操作结果指针
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, args);
        return cursor;
    }

}
