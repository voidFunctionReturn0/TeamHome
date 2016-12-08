package com.example.myapplication.actionbar.calendar;


import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.dto.Schedule;
import com.example.myapplication.network.Network3;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailScheduleFragment extends Fragment {
    private Schedule schedule;

    private EditText txtScheduleTitle;
    private Button iconSchedule;
    private TextView txtFldStartDate;
    private TextView txtFldEndDate;
    private TextView txtStartDateLabel;
    private TextView txtEndDateLabel;
    private EditText txtScheduleLocation;
    private EditText txtScheduleContent;

    private Button btnDelete;
    private Button btnUpdate;


    public DetailScheduleFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;
        // Inflate the layout for this fragment
        try {
            view = inflater.inflate(R.layout.fragment_detail_schedule, container, false);
            txtScheduleTitle = (EditText) view.findViewById(R.id.txtScheduleTitle);
            iconSchedule = (Button) view.findViewById(R.id.iconSchedule);
            txtFldStartDate = (TextView) view.findViewById(R.id.txtFldStartDate);
            txtFldEndDate = (TextView) view.findViewById(R.id.txtFldEndDate);
            txtStartDateLabel = (TextView) view.findViewById(R.id.txtStartDateLabel);
            txtEndDateLabel = (TextView) view.findViewById(R.id.txtEndDateLabel);
            txtScheduleLocation = (EditText) view.findViewById(R.id.txtScheduleLocation);
            txtScheduleContent = (EditText) view.findViewById(R.id.txtScheduleContent);
            btnDelete = (Button) view.findViewById(R.id.btnDelete);
            btnUpdate = (Button) view.findViewById(R.id.btnUpdate);

            txtScheduleTitle.setEnabled(false);
            txtScheduleContent.setEnabled(false);
            txtScheduleLocation.setEnabled(false);

            // 상세볼 일정데이터 가져오기
            schedule = MainActivity.selectedSchedule;

            // 데이터 세팅
            txtScheduleTitle.setText(schedule.getSname());
            int iconId = getResources().getIdentifier(schedule.getSicon(), "drawable", getActivity().getPackageName());
            iconSchedule.setBackgroundResource(iconId);
            setStartDate(schedule.getSstartdate());
            setEndDate(schedule.getSenddate());
            txtScheduleLocation.setText(schedule.getSlocation());
            txtScheduleContent.setText(schedule.getSdescr());


            // 삭제 버튼이벤트 처리
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // 삭제여부 다시 묻는 다이얼로그
                    AlertDialog.Builder alt_bld = new AlertDialog.Builder(getActivity());
                    alt_bld.setMessage("일정을 삭제하시겠습니까?").setCancelable(
                            false).setPositiveButton("네",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Network3.removeSchedule(schedule.getSid());
                                    getActivity()
                                            .getSupportFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.fragmentContainer, new CalendarFragment())
                                            .commit();

                                    Toast.makeText (v.getContext(), "일정이 삭제되었습니다.", Toast.LENGTH_LONG).show();
                                }
                            }).setNegativeButton("아니요",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = alt_bld.create();
                    // Title for AlertDialog
                    alert.setTitle("일정 삭제");
                    // Icon for AlertDialog
                    alert.setIcon(android.R.drawable.ic_dialog_alert);
                    alert.show();
                }
            });


            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity()
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, new UpdateScheduleFragment())
                            .commit();

                }
            });

        } catch(InflateException | Resources.NotFoundException e) { e.printStackTrace(); }

        return view;
    }

    // "연-월-일" -> "월 일, 연"
    private String sqlDateStrToNewFormat(String sqlDateStr) {
        String[] dateArr = sqlDateStr.split("-");
        return dateArr[1] + " " + dateArr[2] + ", " + dateArr[0];
    }

    // set 시작날짜
    private void setStartDate(String date) {
        if(date.equals("Start Date")) {
            return;
        }
        Log.d("date1", date);
        String[] dateArr = date.split("-");
        Log.d("d", dateArr[1]+"");
        dateArr[1] = strToEngMonth(dateArr[1]);
        String newDateStr = " " + dateArr[1] + " " + dateArr[2] + ", " + dateArr[0];
        txtStartDateLabel.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        txtFldStartDate.setText(newDateStr);
    }

    // set 종료날짜
    private void setEndDate(String date) {
        String[] dateArrStr = date.split("-");
        Integer[] dateArr = new Integer[3];
        dateArr[0] = Integer.parseInt(dateArrStr[0]);
        dateArr[1] = Integer.parseInt(dateArrStr[1]);
        dateArr[2] = Integer.parseInt(dateArrStr[2]);
        String engMonth = strToEngMonth(String.valueOf(dateArr[1]));
        String newDateStr = " " + engMonth + " " + dateArr[2] + ", " + dateArr[0];
        txtEndDateLabel.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        txtFldEndDate.setText(newDateStr);
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

}
