package simpleweather.simpleweather.com.liuproject.reminder;

/**
 * Created by acer on 2016/1/21.
 */

import java.io.InputStream;
import org.apache.http.util.EncodingUtils;
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
import android.support.annotation.Nullable;
import android.util.Log;


import simpleweather.simpleweather.database.CityDB;


public class MyService extends Service {
    private final String DB_NAME="CityID";//数据库名称
    private final String NAME="City";
    private final int VERSION=1;
    private SQLiteDatabase cdb;//数据库工具
    private CityDB cdbhelper;//数据库

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        cdbhelper = new CityDB(this, NAME, null,VERSION);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        write(getFromAssets("CityId.text"));
    }

    public void write(String str){
        SQLiteDatabase cbd = cdbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        char c;
        String name = "", id = "";
        int type = 1, n = 0;
        int i = 0;
        for( i=0 ;i < str.length(); i++){
            c = str.charAt(i);
            if(c != '=' && n == 0) id +=c;
            if(c == '\n' && n == 1 && c != '\r') name +=c;
            if(c == '\n'){
                if(id.length()<=5) type = 1;

                values.put("cityid", id);
                values.put("cityname", name);
                values.put("type", type);
                cdb.insert(DB_NAME, null, values);
                Log.i("id---type", id+"("+type+")");
                Log.i("name--", name);
                values.clear();
                n=0;name="";id="" ;type=0;//恢复数据

            }

        }
        cdb.close();	//关闭数据库
        SharedPreferences dataofday = getSharedPreferences("WeatherDataFile", 0);
        dataofday.edit().putBoolean("recod", true).commit();//写入标记数据库创建完成
        this.onDestroy();

    }

    public String getFromAssets(String fileName){ //从assets文件夹读取数据初始化城市id
        String result = "";
        try {
            InputStream in = getResources().getAssets().open(fileName);
            //获取文件的字节数
            int lenght = in.available();
            //创建byte数组
            byte[]  buffer = new byte[lenght];
            //将文件中的数据读到byte数组中
            in.read(buffer);
            result = EncodingUtils.getString(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}




