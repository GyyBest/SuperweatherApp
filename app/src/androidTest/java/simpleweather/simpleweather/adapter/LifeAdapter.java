package simpleweather.simpleweather.adapter;

/**
 * Created by acer on 2016/1/21.
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import org.apache.http.HttpResponse;
//import org.apache.http.HttpStatus;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.util.EntityUtils;
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

public class LifeAdapter extends BaseAdapter{
    private LayoutInflater mInflater;
    private List<Map<String, Object>> mData;
    private List<String> mytitle=new ArrayList<String>();
    private int drawableId[]={R.drawable.clothes,R.drawable.word,R.drawable.clothes,R.drawable.word,R.drawable.sunshine,R.drawable.sunshine,
            R.drawable.car,R.drawable.trip,R.drawable.cool,R.drawable.run,R.drawable.light,R.drawable.health};
    public LifeAdapter(Context context,List<String> title) {
        mInflater = LayoutInflater.from(context);
        mytitle.addAll(title);//预留标题
        init();
    }
    //初始化
    private void init() {
        mData = new ArrayList<Map<String,Object>>();
        for (int i=6; i<mytitle.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("img", drawableId[i]);//图片
            map.put("title", mytitle.get(i));//文字
            mData.add(map);
        }
    }
    @Override
    public int getCount() {
        return mData.size();
    }
    @Override
    public Object getItem(int arg0) {
        return null;
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        //convertView为null的时候初始化convertView
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.weather_list_item, null);
            holder.img = (ImageView)convertView.findViewById(R.id.valueimage);
            holder.title = (TextView)convertView.findViewById(R.id.lifevalue);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.img.setBackgroundResource((Integer)mData.get(position).get("img"));
        holder.title.setText(mData.get(position).get("title").toString());
        return convertView;
    }
    public final class ViewHolder {
        public ImageView img;
        public TextView title;
    }
}

