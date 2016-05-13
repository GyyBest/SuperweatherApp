package simpleweather.simpleweather.activity;

/**
 * Created by acer on 2016/1/21.
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import simpleweather.simpleweather.R;
import simpleweather.simpleweather.adapter.LifeAdapter;


public class Weather extends Activity{
    public static final String PREFS_NAME = "WeatherDataFile";
    SharedPreferences dataofday =null;
    private TextView cityname;
    private TextView date;
    private TextView week;
    private TextView temp;
    private TextView weather;//天气描述
    private TextView clothesv;//穿衣指数

    private ViewPager mPager;//页卡内容
    private List<View> listViews; // Tab页面列表
    private ImageView icon0;//今日天气标志

    private ListView lifeValue;//生活指数

    private Button setcity;//设置城市
    private Button refresh;//刷新页面

    //城市id编号="101200101"="http://m.weather.com.cn/data/"+CityID+".html"
    private String strUrl ;

    int cityid=0;//城市编号
    String today; //当前日期
    String dayofweek; //当前星期
    String city; //城市名称
    int ftime;//更新时间（整点）【更新时间确定temp1属于哪天】
    //天气图片id
    int images[]=new int[6];
    // 六天的气温（摄氏度）
    List<String> tempes;
    //六天的天气描述
    List<String> weathers;
    // 六天的风力大小及风向
    List<String> winds;
    //六天的风力
    List<String> fls;
    //生活指数
    List<String> lifeV;//生活指数
    //今日穿衣指数：
    //今日穿衣描述
    //48小时穿衣指数
    //48小时穿衣描述
    //今日紫外线强度
    //48小时紫外线强度
    //洗车指数
    //旅游指数
    //舒适指数
    //晨练指数
    //晾晒指数
//息斯敏过敏气象指数

    //图片id
    private int drawable[]={R.drawable.a0,R.drawable.a1,R.drawable.a2,R.drawable.a3,R.drawable.a4,R.drawable.a5,
            R.drawable.a6,R.drawable.a7,R.drawable.a8,R.drawable.a9,R.drawable.a10,R.drawable.a11,R.drawable.a12,R.drawable.a13,
            R.drawable.a14,R.drawable.a15,R.drawable.a16,R.drawable.a17,R.drawable.a19,R.drawable.a20,R.drawable.a21,R.drawable.a22,
            R.drawable.a23,R.drawable.a24,R.drawable.a25,R.drawable.a26,R.drawable.a27,R.drawable.a28,R.drawable.a29,R.drawable.a30,R.drawable.a31};
    //图片Id
    private boolean flag=true;//线程标志
    @Override
    protected void onCreate(Bundle savedInstanceState) {	//天气预报页面
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ExitSystem.getInstance().addActivity(this);
        cityname=(TextView)findViewById(R.id.cityname);
        date=(TextView)findViewById(R.id.tdate);
        week=(TextView)findViewById(R.id.tweek);
        temp=(TextView)findViewById(R.id.tempe);//温度
        weather=(TextView)findViewById(R.id.weather);

        clothesv=(TextView)findViewById(R.id.clothesvalue);

        icon0=(ImageView)findViewById(R.id.icon);
        setcity=(Button)findViewById(R.id.setcity);
        refresh=(Button)findViewById(R.id.refresh);
        setListener();//设置监听器
        lifeV=new ArrayList<String>();
        tempes=new  ArrayList<String>();
        weathers=new ArrayList<String>();
        winds=new ArrayList<String>();
        fls=new ArrayList<String>();

        dataofday = getSharedPreferences(PREFS_NAME, 0);
        dataofday.edit().putString("cityid", "101200101").commit();//写入城市ID
        strUrl ="http://m.weather.com.cn/data/"+dataofday.getString("cityid", "101200101")+".html";//获取天气数据

        if(dataofday==null&&!dataofday.contains("Get")){	 //未保存数据
            //获得返回的Json字符串
            String strResult = connServerForResult(strUrl);
            //解析Json字符串
            parseJson(strResult);
            Log.i("Process", "------------获取数据！");

        }else{
            Log.i("Process", "------------读取数据！");
            //清空原数据
            lifeV.clear();
            tempes.clear();
            weathers.clear();
            winds.clear();
            fls.clear();
            //获取数据
            ftime=dataofday.getInt("ftime", 0);
            today=dataofday.getString("today", "2012年12月12日");   city=dataofday.getString("city", "未知");
            dayofweek=dataofday.getString("dayofweek", "星期三");
            for(int i=1;i<=6;i++){
                //图片Id
                images[i-1]=dataofday.getInt("img"+(i*2-1), 0);
                //六天温度
                tempes.add(dataofday.getString("temp"+i, "0"));
                //六天天气描述
                weathers.add(dataofday.getString("weather"+i, "未知"));
                //六天风的描述
                winds.add(dataofday.getString("wind"+i, "未知"));
                //六天风力描述
                fls.add(dataofday.getString("fl"+i, "未知"));
            }
            //生活指数
            lifeV.add(dataofday.getString("index", "未知"));
            lifeV.add(dataofday.getString("index_d", "未知"));
            lifeV.add(dataofday.getString("index48", "未知"));
            lifeV.add(dataofday.getString("index48_d", "未知"));
            lifeV.add( dataofday.getString("index_uv", "未知"));
            lifeV.add(dataofday.getString("index48_uv", "未知"));
            lifeV.add("洗车指数："+dataofday.getString("index_xc", "未知"));
            lifeV.add("旅游指数："+dataofday.getString("index_tr", "未知"));
            lifeV.add("舒适指数："+ dataofday.getString("index_co", "未知"));
            lifeV.add("晨跑指数："+dataofday.getString("index_cl", "未知"));
            lifeV.add("晾晒指数："+dataofday.getString("index_ls", "未知"));
            lifeV.add("息斯敏过敏气象指数："+dataofday.getString("index_ag", "未知"));
        }
        lifeValue=(ListView)findViewById(R.id.lifevalue);
        lifeValue.setAdapter(new LifeAdapter(this,lifeV));
        //  InitViewPager();//初始化
        showdata();//显示数据
        Log.i("Infor","--------------Successful !");
    }

    @Override
    protected void onResume() {
        super.onResume();
        dataofday = getSharedPreferences(PREFS_NAME, 0);
        strUrl ="http://m.weather.com.cn/data/"+dataofday.getString("cityid", "101200101")+".html";//获取天气数据
        //获得返回的Json字符串
        String strResult = connServerForResult(strUrl);
        //解析Json字符串
        parseJson(strResult);
        Log.i("Process", "------------获取数据！");
        showdata();
    }

    private void InitViewPager() {
        mPager = (ViewPager) findViewById(R.id.forcast);
        listViews = new ArrayList<View>();
        LayoutInflater mInflater = getLayoutInflater();
        //添加六个页面
        listViews.add(mInflater.inflate(R.layout.forcast_item, null));
        listViews.add(mInflater.inflate(R.layout.forcast_item, null));
        listViews.add(mInflater.inflate(R.layout.forcast_item, null));
        listViews.add(mInflater.inflate(R.layout.forcast_item, null));
        listViews.add(mInflater.inflate(R.layout.forcast_item, null));
        listViews.add(mInflater.inflate(R.layout.forcast_item, null));
        mPager.setAdapter(new MyPagerAdapter(this,listViews,tempes,weathers,winds,fls,images,today,ftime));
        mPager.setCurrentItem(0);
        //  mPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    public void showdata(){
        lifeValue.invalidate();
        InitViewPager();//初始化天气数据
        icon0.setImageDrawable(getResources().getDrawable(drawable[images[0]]));
        //天气预报信息
        date.setText(today);week.setText(dayofweek);cityname.setText(city);
        temp.setText(tempes.get(0));weather.setText(weathers.get(0));
        clothesv.setText("今日穿衣指数："+lifeV.get(0)
                +"\n      "+lifeV.get(1)+"\n48小时穿衣指数："+lifeV.get(2)+"\n      "+lifeV.get(3));
    }

    public void setListener(){//设置监听器
        final ProgressDialog Dialog = new ProgressDialog(Weather.this);
        Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);  //圆形进度条
        Dialog.setTitle("天气预报");
        Dialog.setMessage("数据正在更新中……");
        Dialog.setIcon(R.drawable.ic_launcher);
        Dialog.setIndeterminate(false);
        Dialog.setCancelable(true);
        setcity.setOnClickListener(new OnClickListener(){//设置地点
            @Override
            public void onClick(View arg0) {
                startActivity(new Intent(Weather.this,CitySet.class));
            }
        });

        refresh.setOnClickListener(new OnClickListener(){//刷新数据
            @Override
            public void onClick(View arg0) {
                Dialog.show();//显示进度框
                new Thread() {
                    public void run() {
                        try {
                            while(flag){
                                //获得返回的Json字符串
                                String strResult = connServerForResult(strUrl);

                                if(!strUrl.equals("")) parseJson(strResult);   //解析Json字符串
                                //取消对话框显示
                                Dialog.dismiss();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                //刷新数据
                showdata();//显示对应数据
            }
        });


    }

    private String connServerForResult(String strUrl) {
        //获取HttpGet对象
        HttpGet httpRequest = new HttpGet(strUrl);
        String strResult = "";
        try {
            // HttpClient对象
            HttpClient httpClient = new DefaultHttpClient();
            // 获得HttpResponse对象
            HttpResponse httpResponse = httpClient.execute(httpRequest);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // 取得返回的数据
                strResult = EntityUtils.toString(httpResponse.getEntity());
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("Infor", strResult);
        return strResult;
    }

    // 普通Json数据解析
    private void parseJson(String strResult) {
        try {
            JSONObject jsonObj = new JSONObject(strResult).getJSONObject("weatherinfo");
            //清空原数据
            lifeV.clear();
            tempes.clear();
            weathers.clear();
            winds.clear();
            fls.clear();
            //打开SharedPreferences更新数据
            Editor write=dataofday.edit();

            //   cityid = jsonObj.getInt("cityid");//城市编号
            today= jsonObj.getString("date_y"); //当前日期
            dayofweek= jsonObj.getString("week"); //当前星期
            city = jsonObj.getString("city"); //城市名称
            ftime= jsonObj.getInt("fchh");//更新时间（整点）【更新时间确定temp1属于哪天】

            write.putBoolean("Get", true);//标记是否有记录
            write.putInt("cityid", cityid);//城市ID
            write.putInt("ftime", ftime);//更新时间
            write.putString("city", city);  write.putString("today", today);  write.putString("dayofweek", dayofweek);
            for(int i=1;i<=6;i++){
                //六天天气的图片Id
                images[i-1]= jsonObj.getInt("img"+(i*2-1));
                write.putInt("img"+(i*2-1),jsonObj.getInt("img"+(i*2-1)));
                // 六天的气温（摄氏度）
                tempes.add( jsonObj.getString("temp"+i));
                write.putString("temp"+i,jsonObj.getString("temp"+i));
                //六天的天气描述
                weathers.add(jsonObj.getString("weather"+i));
                write.putString("weather"+i,jsonObj.getString("weather"+i));
                // 六天的风力大小及风向
                winds.add( jsonObj.getString("wind"+i));
                write.putString("wind"+i,jsonObj.getString("wind"+i));
                //六天的风力
                fls.add( jsonObj.getString("fl"+i));
                write.putString("fl"+i,jsonObj.getString("fl"+i));
            }
            //生活指数
            lifeV.add(jsonObj.getString("index"));
            lifeV.add( jsonObj.getString("index_d"));
            lifeV.add( jsonObj.getString("index48"));
            lifeV.add( jsonObj.getString("index48_d"));
            lifeV.add( jsonObj.getString("index_uv"));
            lifeV.add( jsonObj.getString("index48_uv"));
            lifeV.add( jsonObj.getString("index_xc"));
            lifeV.add( jsonObj.getString("index_tr"));
            lifeV.add( jsonObj.getString("index_co"));
            lifeV.add( jsonObj.getString("index_cl"));
            lifeV.add(jsonObj.getString("index_ls"));
            lifeV.add( jsonObj.getString("index_ag"));
            //写入保存数据
            write.putString("index", lifeV.get(0));  write.putString("index_d",lifeV.get(1));  write.putString("index48",lifeV.get(2));
            write.putString("index48_d", lifeV.get(3));  write.putString("index_uv", lifeV.get(4));  write.putString("index48_uv", lifeV.get(5));
            write.putString("index_xc", lifeV.get(6));  write.putString("index_tr", lifeV.get(7));  write.putString("index_co", lifeV.get(8));
            write.putString("index_cl", lifeV.get(9));  write.putString("index_ls", lifeV.get(10));  write.putString("index_ag", lifeV.get(11));
            //提交数据
            write.commit();
        } catch (JSONException e) {
            Log.i("Erorr","Json parse error");
            e.printStackTrace();
        }
    }

}
