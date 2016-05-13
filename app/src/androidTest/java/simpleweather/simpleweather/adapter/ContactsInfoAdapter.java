package simpleweather.simpleweather.adapter;

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


public class ContactsInfoAdapter extends BaseExpandableListAdapter {

    List<String> group;           //组列表
    List<List<String>> child;     //子列表
    Context parent;//

    public ContactsInfoAdapter(Context p, List<String> g, List<List<String>> c){
        group=g;
        child=c;
        parent=p;
    }
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return child.get(groupPosition).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return child.get(groupPosition).size();
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        String string = child.get(groupPosition).get(childPosition);
        return getGenericView(string);
    }
    @Override
    public Object getGroup(int groupPosition) {
        return group.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public int getGroupCount() {
        return group.size();
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String string = group.get(groupPosition);
        return getGenericView(string);
    }

    //创建组/子视图
    public TextView getGenericView(String s) {
        // Layout parameters for the ExpandableListView
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT, 40);
        TextView text = new TextView(parent);
        text.setLayoutParams(lp);
        text.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        text.setPadding(50, 0, 0, 0);
        text.setTextColor(Color.BLACK);
        text.setText(s);
        return text;
    }


    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return true;
    }

}


