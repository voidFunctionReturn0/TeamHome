package com.example.myapplication.actionbar.calendar;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.dto.Schedule;

import java.util.Calendar;
import java.util.List;

/**
 * Created by ms on 2016-07-12.
 */
public class SchedulesListViewAdapter extends BaseAdapter {

    private List<Schedule> list;
    public void setList(List<Schedule> list) { this.list = list; }

    private Context context;
    public SchedulesListViewAdapter(Context context) { this.context = context; }


    @Override
    public int getCount() {
        if(list != null)    return list.size();
        else                return -1;
    }

    @Override
    public Object getItem(int position) {
        if(list != null)    return list.get(position);
        else                return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 아이템뷰를 새로 만드는 경우
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_view_schedule, parent, false);
        }

        // 뷰 인플레이션
        //TextView txtDay = (TextView) convertView.findViewById(R.id.txtDay);
        //TextView txtDayOfWeek = (TextView) convertView.findViewById(R.id.txtDayOfWeek);
        ImageView imgScheduleIcon = (ImageView) convertView.findViewById(R.id.imgScheduleIcon);
        TextView txtScheduleTitle = (TextView) convertView.findViewById(R.id.txtScheduleTitle);
        TextView txtScontent = (TextView) convertView.findViewById(R.id.txtScontent);
        TextView txtToday = (TextView) convertView.findViewById(R.id.txtToday);

        // 리스트 정보로 아이템뷰를 세팅하기 시작
        Schedule s = list.get(position);

        String[] selectedDate = s.getSenddate().split("-");
        //txtDay.setText(selectedDate[2]); // senddate가 선택된 일자로 변경되어 있음
        //txtDayOfWeek.setText(Network3.getDayOfWeek(s.getSenddate()));
        txtScontent.setText(s.getSdescr());
        txtScheduleTitle.setText(s.getSname());

        // 아이콘 세팅
        Log.d("icon", s.getSicon()+"");
        int iconId = 0;
        if(s.getSicon() != null) {
            iconId = context.getResources().getIdentifier(s.getSicon(), "drawable", context.getPackageName());
            Log.d("iconId", iconId+"");
            try {
                if (iconId != 0) {
                    imgScheduleIcon.setImageResource(iconId);
                }
            } catch(Resources.NotFoundException e) { e.printStackTrace(); }
        }


        // 오늘일정인지 확인하고 오늘이면 "오늘" 표시
        txtToday.setVisibility(View.INVISIBLE);
        Calendar cal = Calendar.getInstance();
        String todayStr = "" + cal.get(Calendar.YEAR) + cal.get(Calendar.MONTH) + cal.get(Calendar.DAY_OF_MONTH);
        if (todayStr.equals(s.getSenddate())) {
            txtToday.setVisibility(View.VISIBLE);
        } else {
            txtToday.setVisibility(View.INVISIBLE);
        }


        return convertView;
    }
}
