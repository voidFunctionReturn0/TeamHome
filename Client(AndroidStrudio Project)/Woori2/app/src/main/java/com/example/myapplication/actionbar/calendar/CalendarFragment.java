package com.example.myapplication.actionbar.calendar;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.dto.Schedule;
import com.example.myapplication.network.Network3;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment {
    // private Calendar currentCalender = Calendar.getInstance(Locale.getDefault());"
    private CalendarFragment me;

    private int showYear;
    private int showMonth;
    private int showDay;

    private Button btnAddSchedule;
    //private Button btnGoToday;
    private Button btnListUp;
    private Button btnPrevMonth, btnNextMonth;
    private boolean shouldShow = false;
    private CompactCalendarView compactCalendarView;
    private TextView txtYear, txtMonth;
    private ListView schedulesListView;

    private List<Schedule> schedulesList;
    private List<Schedule> showScheduleList;
    private SchedulesListViewAdapter schedulesListadapter;

    private LinearLayout goTodayLayout;
    private TextView txtEngMonth;
    private TextView txtDay;
    private TextView txtMonthOfList;
    private TextView txtDayOfList;
    private TextView txtDayOfWeekOfList;

    public CalendarFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity.presentFragment = "CalendarFragment";
        getActivity().invalidateOptionsMenu();
        MainActivity.toggle.setDrawerIndicatorEnabled(true);

        me = this;

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        btnAddSchedule = (Button) view.findViewById(R.id.btnAddSchedule);
        //btnGoToday = (Button) view.findViewById(R.id.btnGoToday);
        btnListUp = (Button) view.findViewById(R.id.btnListUp);
        compactCalendarView = (CompactCalendarView) view.findViewById(R.id.compactCalendarView);
        txtYear = (TextView) view.findViewById(R.id.txtYear);
        txtMonth = (TextView) view.findViewById(R.id.txtMonth);
        schedulesListView = (ListView) view.findViewById(R.id.schedulesListView);
        btnPrevMonth = (Button) view.findViewById(R.id.btnPrevMonth);
        btnNextMonth = (Button) view.findViewById(R.id.btnNextMonth);
        goTodayLayout = (LinearLayout) view.findViewById(R.id.goTodayLayout);
        txtEngMonth = (TextView) view.findViewById(R.id.txtEngMonth);
        txtDay = (TextView) view.findViewById(R.id.txtDay);
        txtMonthOfList = (TextView) view.findViewById(R.id.txtMonthOfList);
        txtDayOfList = (TextView) view.findViewById(R.id.txtDayOfList);
        txtDayOfWeekOfList = (TextView) view.findViewById(R.id.txtDayOfWeekOfList);

        showScheduleList = new ArrayList<>();
        schedulesListadapter = new SchedulesListViewAdapter(this.getActivity());
        schedulesListadapter.setList(showScheduleList);
        schedulesListView.setAdapter(schedulesListadapter);



        compactCalendarView.setCalendarBackgroundColor(Color.parseColor("#ffffff"));
        // 오늘날짜 지정
        goToday();


        // 리스트뷰 이벤트처리: 상세보기
        schedulesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 선택된 아이템만 배경색 변경(흐린 빨강)
                /*view.setBackgroundColor(Color.parseColor("#ecd5d5"));
                for(int i=0; i<schedulesListadapter.getCount(); i++) {
                    if(i == position) continue;
                    View otherView = schedulesListView.getChildAt(i);
                    if(otherView != null) {
                        otherView.setBackgroundColor(Color.parseColor("#ffffff"));
                    }
                }*/

                int sid = showScheduleList.get(position).getSid();
                Schedule schedule = null;
                for(Schedule s : schedulesList) {
                    if(s.getSid() == sid) {
                        schedule = s;
                    }
                }

                if(schedule != null) {
                    MainActivity.selectedSchedule = schedule;

                    getActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, new DetailScheduleFragment())
                            .commit();

                } else {
                    Log.i("일정상세보기", "상세보기 오류");
                }
            }
        });


        // 일정 리스트 슬라이드 Up & Down 이벤트 처리
        btnListUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shouldShow) {
                    compactCalendarView.showCalendar();
                    btnListUp.setBackgroundResource(R.drawable.up_arrow);
                } else {
                    compactCalendarView.hideCalendar();
                    btnListUp.setBackgroundResource(R.drawable.down_arrow);
                }
                shouldShow = !shouldShow;
            }
        });


        // 오늘날짜 색 변경 (black)
        compactCalendarView.setCurrentDayBackgroundColor(Color.parseColor("#003366"));


        // 선택한 날짜 색 변경 (dark red)
        compactCalendarView.setCurrentSelectedDayBackgroundColor(Color.parseColor("#9DA1C7"));


        // 일요일을 첫 번째 칸에 오도록 하기
        // compactCalendarView.setShouldShowMondayAsFirstDay(false);


        // 이전 달 버튼이벤트 처리
        btnPrevMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 연, 월 텍스트뷰를 수정
                if(showMonth == 1) {
                    txtYear.setText(--showYear+"");
                    showMonth = 12;
                    txtMonth.setText(showMonth+"");
                    showEvents();
                } else {
                    txtMonth.setText(--showMonth+"");
                    txtEngMonth.setText(strToEngMonth(showMonth+""));
                }
                txtEngMonth.setText(strToEngMonth(showMonth+""));
                txtDay.setText("1");
                compactCalendarView.showPreviousMonth();
            }
        });


        // 다음 달 버튼이벤트 처리
        btnNextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 연, 월 텍스트뷰를 수정
                if(showMonth == 12) {
                    txtYear.setText(++showYear+"");
                    showMonth = 1;
                    txtMonth.setText(showMonth+"");
                    showEvents();
                } else {
                    txtMonth.setText(++showMonth+"");
                }
                txtEngMonth.setText(strToEngMonth(showMonth+""));
                txtDay.setText("1");
                compactCalendarView.showNextMonth();
            }
        });


        // 오늘날짜로 가는 버튼이벤트 처리
        goTodayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToday();
            }
        });

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            // 날짜 클릭 이벤트처리
            @Override
            public void onDayClick(Date dateClicked) {
                showScheduleOnList(dateClicked);

                // 일정추가에 시작날짜로 지정될 날짜
                MainActivity.startDate = (dateClicked.getYear()+1900) + "-" + (dateClicked.getMonth()+1) + "-" + dateClicked.getDate();
                txtDay.setText(dateClicked.getDate()+"");

                // 일정리스트 위에 표시될 날짜
                txtMonthOfList.setText((dateClicked.getMonth()+1)+"");
                txtDayOfList.setText(dateClicked.getDate()+"");
                try {
                    String dateStr = (dateClicked.getYear()+1900) + "-" + (dateClicked.getMonth()+1) + "-" + dateClicked.getDate();
                    txtDayOfWeekOfList.setText(getDateDay(dateStr));
                } catch(Exception e) { e.printStackTrace(); }
            }


            // 달력 스와이프 이벤트처리
            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                // 연도가 바뀌면 일정리스트 갱신
                if(Integer.parseInt(txtYear.getText().toString()) != firstDayOfNewMonth.getYear()+1900) {
                    txtYear.setText(firstDayOfNewMonth.getYear()+1900+"");
                    showEvents();
                }
                txtMonth.setText(firstDayOfNewMonth.getMonth()+1+"");
                txtEngMonth.setText(strToEngMonth(firstDayOfNewMonth.getMonth()+1+""));
                txtDay.setText("1");
            }
        });


        // 일정추가하기 화면으로 가기 버튼이벤트 처리
        btnAddSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, new AddScheduleFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        showEvents();

        return view;
    }


    // 오늘날짜로 가기
    private void goToday() {
        Calendar calendar = Calendar.getInstance();
        showYear = calendar.get(Calendar.YEAR);
        showMonth = calendar.get(Calendar.MONTH) + 1; // Calendar에서 얻은 Month는 0~11
        showDay = calendar.get(Calendar.DAY_OF_MONTH);
        compactCalendarView.setCurrentDate(new Date(showYear-1900, showMonth-1, showDay));
        MainActivity.startDate = showYear + "-" + showMonth + "-" + showDay;


        // 연, 월, 일 텍스트뷰를 오늘에 맞게 수정
        // 오늘로 가는 연도가 현재달력연도와 다르면 이벤트리스트도 새로 가져옴
        if(showYear != Integer.parseInt(txtYear.getText().toString())) {
            txtYear.setText(showYear + "");
            showEvents();
        }
        txtMonth.setText(showMonth + "");
        txtEngMonth.setText(strToEngMonth(showMonth+""));
        txtDay.setText(showDay+"");

        // 일정리스트 위에 표시될 날짜
        txtMonthOfList.setText(showMonth + "");
        txtDayOfList.setText(showDay+"");
        try {
            String dateStr = showYear + "-" + showMonth + "-" + showDay;
            txtDayOfWeekOfList.setText(getDateDay(dateStr));
        } catch(Exception e) { e.printStackTrace(); }


        // 일정리스트에 오늘 날짜 일정리스트 보여주기
        Date today = calendar.getTime();
        showScheduleOnList(today);
    }


    // 연별 일정 달력에 보여주기
    private void showEvents() {
        List<Event> eventList = getEvents();
        compactCalendarView.removeAllEvents();
        compactCalendarView.addEvents(eventList);
    }


    private List<Event> getEvents() {
        List<Event> eventList = null;

        // DB에서 팀별-연별 일정 리스트 받아오기
        Calendar calendar = Calendar.getInstance();
        calendar.set(showYear-1900, showMonth-1, 1);
        schedulesList = Network3.getScheduleListByTidAndDate(MainActivity.tid, calendar);
        Log.d("list3", schedulesList.size()+"");

        // List<Schedule> -> List<Event> 변환
        // 일별일정 저장은 라이브러리의 Event객체를 사용함 (첫번째=달력에보일 색, 두번째=달력에 표시될 날짜, 세번째=일정내용문자열-이걸 리스트 인덱스로 사용)
        if(schedulesList != null) {
            eventList = new ArrayList<>();
            for(int idx=0; idx<schedulesList.size(); idx++) {
                Log.d("idx", idx+"");
                // 일정 색 설정
                int iconColor = getSiconColor(schedulesList.get(idx).getSicon());

                // 시작날짜, 종료날짜 구하기
                Date startDate = Network3.strToUtilDate(schedulesList.get(idx).getSstartdate());
                Log.d("startDate", startDate+"");
                Log.d("startDay", startDate.getDate()+"");
                Date endDate = Network3.strToUtilDate(schedulesList.get(idx).getSenddate());
                Log.d("endDate", endDate+"");

                int z = 0;
                // 시작날짜~종료날짜만큼 Event객체 만들어서 리스트에 추가
                while(startDate.compareTo(endDate) <= 0) {
                    Log.d("z", (z++)+"");
                    startDate.setHours(0);
                    startDate.setMinutes(0);
                    startDate.setSeconds(0);
                    long timeInMillis = startDate.getTime();
                    eventList.add(new Event(iconColor, timeInMillis, idx + ""));

                    Log.d("startDate1", startDate+"");
                    // 날짜 + 1 (while조건을 위함)
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(startDate);
                    cal.add(Calendar.DATE, 1);
                    startDate = cal.getTime();
                    Log.d("startDate2", startDate+"");
                }
            }
        }

        return eventList;
    }

    // 일정 아이콘별 달력에 보일 색깔 설정
    private int getSiconColor(String sicon) {
        int color = 0;

        if(sicon.equals("sicon_alcohol")) {
            color = Color.parseColor("#000000");
        }
        else if(sicon.equals("sicon_exercise")) {
            color = Color.parseColor("#ff0000");
        }
        else if(sicon.equals("sicon_experiment")) {
            color = Color.parseColor("#00ff00");
        }
        else if(sicon.equals("sicon_football")) {
            color = Color.parseColor("#0000ff");
        }
        else if(sicon.equals("sicon_game")) {
            color = Color.parseColor("#ffff00");
        }
        else if(sicon.equals("sicon_meal")) {
            color = Color.parseColor("#00ffff");
        }
        else if(sicon.equals("sicon_meeting")) {
            color = Color.parseColor("#ff00ff");
        }
        else if(sicon.equals("sicon_music")) {
            color = Color.parseColor("#0f0f0f");
        }
        else if(sicon.equals("sicon_party")) {
            color = Color.parseColor("#f0f0f0");
        }
        else if(sicon.equals("sicon_presentation")) {
            color = Color.parseColor("#0f00f0");
        }
        else if(sicon.equals("sicon_study")) {
            color = Color.parseColor("#f0ff0f");
        }
        else if(sicon.equals("sicon_trophy")) {
            color = Color.parseColor("#0ffff0");
        }
        else if(sicon.equals("btn_addschedule_cetegory")) {
            color = Color.parseColor("#000000");
        }

        return color;
    }


    // 해당 날짜 일정리스트 보여주기
    private void showScheduleOnList(Date dateSelected) {
        List<Event> schedulesFromMap = compactCalendarView.getEvents(dateSelected);
        Log.d("schedulesFromMapSize", schedulesFromMap.size()+"");
        if(schedulesFromMap != null){
            showScheduleList.clear();

            int p = 0;
            for(Event scheduleEvent : schedulesFromMap){
                Log.d("p", (p++)+"");

                int idx = Integer.parseInt((String)scheduleEvent.getData());
                Schedule s2 = schedulesList.get(idx);
/*                String strDate= Network3.utilDateToStr(dateSelected);
                s2.setSenddate(strDate);*/
                showScheduleList.add(s2);
            }
            schedulesListadapter.notifyDataSetChanged();
        }
    }

    // 숫자 Month를 영어로 변경
    private String strToEngMonth(String month) {
        Log.d("month", month+"");
        if(month.equals("1")) { return "Jan"; }
        if(month.equals("2")) { return "Feb"; }
        if(month.equals("3")) { return "Mar"; }
        if(month.equals("4")) { return "Apr"; }
        if(month.equals("5")) { return "May"; }
        if(month.equals("6")) { return "Jun"; }
        if(month.equals("7")) { return "July"; }
        if(month.equals("8")) { return "Aug"; }
        if(month.equals("9")) { return "Sep"; }
        if(month.equals("10")) { return "Oct"; }
        if(month.equals("11")) { return "Nov"; }
        if(month.equals("12")) { return "Dec"; }
        return "...";
    }

    // 날짜로 요일 구하기
    public String getDateDay(String date) throws Exception {

        String day = "" ;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd") ;
        Date nDate = dateFormat.parse(date) ;

        Calendar cal = Calendar.getInstance() ;
        cal.setTime(nDate);

        int dayNum = cal.get(Calendar.DAY_OF_WEEK) ;

        switch(dayNum){
            case 1:
                day = "일";
                break ;
            case 2:
                day = "월";
                break ;
            case 3:
                day = "화";
                break ;
            case 4:
                day = "수";
                break ;
            case 5:
                day = "목";
                break ;
            case 6:
                day = "금";
                break ;
            case 7:
                day = "토";
                break ;
        }
        return day ;
    }
}
