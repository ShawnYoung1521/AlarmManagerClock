/*
 *  autor：OrandNot
 *  email：orandnot@qq.com
 *  time: 2016 - 1 - 13
 *
 */
package com.example.aaaaaaaa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class WeekGridAdpter extends BaseAdapter {

    private Context context;
    private String[] weekStr = {"日","一","二","三","四","五","六"};
    private int selected = 0;
    private  AlarmsSetting alarmsSetting;
    private  int type;

    public WeekGridAdpter(Context context, AlarmsSetting alarmsSetting, int type) {
        this.context = context;
        this.alarmsSetting = alarmsSetting;
        this.type = type;
        if(type==AlarmsSetting.ALARM_SETTING_TYPE_IN){
            this.selected =alarmsSetting.getInDays();
        }else{
            this.selected =alarmsSetting.getOutDays();
        }
    }

    public void updateSelected(int selected){
        this.selected = selected;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return weekStr.length;
    }

    @Override
    public Object getItem(int item) {
        // TODO Auto-generated method stub
        return weekStr[item];
    }

    @Override
    public long getItemId(int id) {
        // TODO Auto-generated method stub
        return id;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_week_item, null);
            holder =new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder =new ViewHolder(convertView);
        }

        holder.text.setText(weekStr[position]);
        if((selected>>position)%2==1){
            holder.text.setSelected(true);
        }else{
            holder.text.setSelected(false);
        }
        holder.text.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                Log.e("selected",""+ selected);
                if(v.isSelected()){
                    selected = selected - (int)(1 << position);
                    if(selected <= 0) {//至少选一个
                        selected = selected + (int)(1 << position);
                        return ;
                    }
                    v.setSelected(false);
                }else{
                    selected = selected + (int)(1 << position);
                    v.setSelected(true);
                }
                if(type==AlarmsSetting.ALARM_SETTING_TYPE_IN){
                    alarmsSetting.setInDays(selected);
                }else if(type==AlarmsSetting.ALARM_SETTING_TYPE_OUT){
                    alarmsSetting.setOutDays(selected);
                }
                AlarmOpreation.cancelAlert(context,type);
                AlarmOpreation.enableAlert(context, type, alarmsSetting);
            }
        });
        return convertView;
    }

    class ViewHolder{
        TextView text;
        public ViewHolder(View view){
            text = (TextView) view.findViewById(R.id.week_item_text);
        }
    }



}

