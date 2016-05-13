package simpleweather.simpleweather.activity;

/**
 * Created by acer on 2016/1/21.
 */
import java.util.ArrayList;
import java.util.List;

//import com.liuproject.reminder.Weather.LifeAdapter;

import android.app.Activity;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;
import android.widget.Toast;

import simpleweather.simpleweather.R;
import simpleweather.simpleweather.adapter.ContactsInfoAdapter;
import simpleweather.simpleweather.database.CityDB;


public class CitySet extends Activity {
    private final String DB_NAME = "CityID";
    private final String NAME="City";
    private final int VERSION=1;
    private SQLiteDatabase cdb;//数据库工具
    private CityDB cdbhelper;//数据库
    private Cursor cursor=null;

    private EditText getname;//用户输入
    private Button back;
    private TextView infor;
    private ExpandableListView  citylist;

    //省市自治区名称，id
    private List<String> cityid=new ArrayList<String>();
    private List<String> groupn=new ArrayList<String>();
    private List<String> childnt;
    private List<List<String>> childn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cityset);
        cdbhelper=  new CityDB(this, NAME, null,  VERSION);
        getname=(EditText)findViewById(R.id.get_cityname);
        back=(Button)findViewById(R.id.get_setcity);
        infor=(TextView)findViewById(R.id.infor);
        citylist=(ExpandableListView )findViewById(R.id.citylist);
        LoadData();
        back.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View arg0) {
                if(getname.getText().toString().equals(""))Toast.makeText(CitySet.this,"请输入城市名称！",Toast.LENGTH_SHORT).show();
                else{
                    cdb = cdbhelper.getReadableDatabase();
                    cursor =cdb.query(DB_NAME,new String[]{"cityid"},"cityname=?" ,new String[]{ ""+getname.getText().toString()},null, null, "cityid");
                    if(cursor.getCount()<=0)Toast.makeText(CitySet.this,"抱歉，无法获取该地区天气数据！",Toast.LENGTH_SHORT).show();
                    else{
                        cursor.moveToFirst();
                        SharedPreferences dataofday = getSharedPreferences("WeatherDataFile", 0);
                        dataofday.edit().putString("cityid", cursor.getString(0)).commit();//写入标记数据库创建完成
                        CitySet.this.finish();
                    }
                    cdb.close();
                }
            }
        });
        citylist.setOnChildClickListener(new OnChildClickListener(){
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                //返回被选中的城市id
                int sum=childPosition;
                for(int i=0;i<groupPosition;i++){
                    sum+=childn.get(i).size();
                }
                Log.i("click-","-----点击"+childn.get(groupPosition).get(childPosition)+" id-- "+cityid.get(sum));
                SharedPreferences dataofday = getSharedPreferences("WeatherDataFile", 0);
                dataofday.edit().putString("cityid", cityid.get(sum)).commit();//写入标记数据库创建完成
                CitySet.this.finish();
                return false;
            }

        })	;
    }
    //citylist;
    void LoadData(){
        int gnum=1;//区分组别
        childn=new ArrayList<List<String>>();
        childnt=new ArrayList<String>();
        cdb = cdbhelper.getReadableDatabase();
        cursor =cdb.query(DB_NAME,new String[]{"cityid","cityname","type"},null,null,null, null, "cityid");
        cursor.moveToFirst();
        if(cursor.getCount()<=0){
            Toast.makeText(this, "数据出错！", Toast.LENGTH_SHORT).show();
        }else
            while(!cursor.isAfterLast()){
                //省市特别行政区
                if(cursor.getString(2).equals("1"))groupn.add(cursor.getString(1));
                //城市地区
                if(cursor.getString(2).equals("0")) {
                    cityid.add(cursor.getString(0));//保存ID
                    if(groupn.size()>gnum){
                        childn.add(childnt);//添加到子目录中
                        //childnt.clear();
                        gnum++;
                        childnt=new ArrayList<String>();
                        childnt.add(cursor.getString(1));
                    }
                    else childnt.add(cursor.getString(1));

                }
                cursor.moveToNext();
            }
        cdb.close();
        childn.add(childnt);//添加到子目录中
        citylist.setAdapter(new ContactsInfoAdapter(this,groupn,childn));
    }
}



