package com.example.myapplication.actionbar.calendar;


import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.dto.Schedule;
import com.example.myapplication.network.Network3;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateScheduleFragment extends Fragment {
    private LinearLayout datePickerLayout;
    private LinearLayout iconSelectLayout;
    private Button iconSchedule;
    private Button btnFinishUpdateSchedule;
    private Button btnStartDate;
    private Button btnEndDate;
    private TextView txtFldStartDate;
    private TextView txtFldEndDate;
    private TextView txtStartDateLabel;
    private TextView txtEndDateLabel;
    private EditText txtScheduleTitle;
    private EditText txtScheduleLocation;
    private EditText txtScheduleContent;
    private DatePicker datePicker;
    private LinearLayout selectStartDate;
    private LinearLayout selectEndDate;

    // 아이콘
    private ImageButton siconAlcohol;
    private ImageButton siconExercise;
    private ImageButton siconExperiment;
    private ImageButton siconFootball;
    private ImageButton siconGame;
    private ImageButton siconMeal;
    private ImageButton siconMeeting;
    private ImageButton siconMusic;
    private ImageButton siconParty;
    private ImageButton siconPresentation;
    private ImageButton siconStudy;
    private ImageButton siconTrophy;
    private ImageButton siconDefault;

    private String siconName;

    private Schedule schedule;


    public UpdateScheduleFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        siconName = "btn_addschedule_cetegory";

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_schedule, container, false);

        // 필드 Views 인플레이션
        datePickerLayout = (LinearLayout) view.findViewById(R.id.datePickerLayout);
        txtFldStartDate = (TextView) view.findViewById(R.id.txtFldStartDate);
        txtFldEndDate = (TextView) view.findViewById(R.id.txtFldEndDate);
        txtStartDateLabel = (TextView) view.findViewById(R.id.txtStartDateLabel);
        txtEndDateLabel = (TextView) view.findViewById(R.id.txtEndDateLabel);
        //btnStartDate = (Button) view.findViewById(R.id.btnStartDate);
        //btnEndDate = (Button) view.findViewById(R.id.btnEndDate);
        btnFinishUpdateSchedule = (Button) view.findViewById(R.id.btnFinishUpdateSchedule);

        txtScheduleTitle = (EditText) view.findViewById(R.id.txtScheduleTitle);
        txtScheduleLocation = (EditText) view.findViewById(R.id.txtScheduleLocation);
        txtScheduleContent = (EditText) view.findViewById(R.id.txtScheduleContent);
        iconSelectLayout = (LinearLayout) view.findViewById(R.id.iconSelectLayout);
        iconSchedule = (Button) view.findViewById(R.id.iconSchedule);
        datePicker = (DatePicker) view.findViewById(R.id.datePicker);

        siconAlcohol = (ImageButton) view.findViewById(R.id.siconAlcohol);
        siconExercise = (ImageButton) view.findViewById(R.id.siconExercise);
        siconExperiment = (ImageButton) view.findViewById(R.id.siconExperiment);
        siconFootball = (ImageButton) view.findViewById(R.id.siconFootball);
        siconGame = (ImageButton) view.findViewById(R.id.siconGame);
        siconMeal = (ImageButton) view.findViewById(R.id.siconMeal);
        siconMeeting = (ImageButton) view.findViewById(R.id.siconMeeting);
        siconMusic = (ImageButton) view.findViewById(R.id.siconMusic);
        siconParty = (ImageButton) view.findViewById(R.id.siconParty);
        siconPresentation = (ImageButton) view.findViewById(R.id.siconPresentation);
        siconStudy = (ImageButton) view.findViewById(R.id.siconStudy);
        siconTrophy = (ImageButton) view.findViewById(R.id.siconTrophy);
        siconDefault = (ImageButton) view.findViewById(R.id.siconDefault);
        selectStartDate = (LinearLayout) view.findViewById(R.id.selectStartDate);
        selectEndDate = (LinearLayout) view.findViewById(R.id.selectEndDate);

        ImageButton[] sicons = new ImageButton[13];
        sicons[0] = siconAlcohol;
        sicons[1] = siconExercise;
        sicons[2] = siconExperiment;
        sicons[3] = siconFootball;
        sicons[4] = siconGame;
        sicons[5] = siconMeal;
        sicons[6] = siconMeeting;
        sicons[7] = siconMusic;
        sicons[8] = siconParty;
        sicons[9] = siconPresentation;
        sicons[10] = siconStudy;
        sicons[11] = siconTrophy;
        sicons[12] = siconDefault;

        // 아이콘선택 버튼이벤트처리
        for(ImageButton sicon : sicons) {
            sicon.setOnClickListener(onClickListenerImgBtnSicon);
        }

        // 일정수정완료 버튼이벤트처리
        btnFinishUpdateSchedule.setOnClickListener(onClickListenerBtnFinish);

        // 날짜선택레이아웃 나오는 이벤트
        selectStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickAndApply(v);
            }
        });
        selectEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickAndApply(v);
            }
        });

        // 일정아이콘 선택하기 화면 나오는 버튼이벤트처리
        iconSchedule.setOnClickListener(onClickBtnIconSchedule);

        // 공백클릭하면 datePickerLayout사라지기
        datePickerLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setVisibility(View.INVISIBLE);
                return true;
            }
        });

        // 공백클릭하면 iconSelectLayout사라지기
        iconSelectLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setVisibility(View.INVISIBLE);
                return true;
            }
        });

        // 일정데이터 가져오기
        schedule = MainActivity.selectedSchedule;

        // 데이터 세팅
        txtScheduleTitle.setText(schedule.getSname());
        int iconId = getResources().getIdentifier(schedule.getSicon(), "drawable", getActivity().getPackageName());
        iconSchedule.setBackgroundResource(iconId);
        siconName = schedule.getSicon();
        setStartDate(schedule.getSstartdate());
        setEndDate(schedule.getSenddate());
        txtScheduleLocation.setText(schedule.getSlocation());
        txtScheduleContent.setText(schedule.getSdescr());

        return view;
    }

    // 일정수정완료 버튼이벤트
    private View.OnClickListener onClickListenerBtnFinish = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // 일정추가 유효성검사
            // 일정제목이 비어있는 경우
            if(txtScheduleTitle.getText().toString().getBytes().length <= 0) {
                // 에러메시지
                Toast.makeText (v.getContext(), "일정제목을 입력해주세요.", Toast.LENGTH_LONG).show();
                return;
            }

            // 시작날짜, 종료날짜가 비어있는 경우
            if(txtFldStartDate.getText().equals("Start Date") || txtFldEndDate.getText().equals("End Date")) {
                // 에러메시지
                Toast.makeText (v.getContext(), "날짜를 모두 입력해주세요.", Toast.LENGTH_LONG).show();
                return;
            }

            Integer[] startDateArr = newFormatToIntegerArr(txtFldStartDate.getText().toString());
            Integer[] endDateArr = newFormatToIntegerArr(txtFldEndDate.getText().toString());

            // 일정을 DTO에 세팅
            Schedule s = new Schedule();
            s.setSid(schedule.getSid());
            s.setSname(txtScheduleTitle.getText().toString());
            s.setSstartdate(startDateArr[0]+"-"+startDateArr[1]+"-"+startDateArr[2]);
            s.setSenddate(endDateArr[0]+"-"+endDateArr[1]+"-"+endDateArr[2]);
            s.setSlocation(txtScheduleLocation.getText().toString());
            s.setSicon(siconName);
            s.setSdescr(txtScheduleContent.getText().toString());
            s.setTid(MainActivity.tid);

            // 일정을 DB에 저장
            Network3.updateSchedule(s);

            // 상세보기에서 보여줄 일정 세팅
            MainActivity.selectedSchedule = s;

            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new CalendarFragment())
                    .commit();

            Toast.makeText (v.getContext(), "일정이 수정되었습니다.", Toast.LENGTH_LONG).show();
        }
    };


    // 날짜선택 Layout보여지고 선택된 날짜를 EditText에 적용하기
    private void datePickAndApply(View v) {
        datePickerLayout.setVisibility(View.VISIBLE);

        if(v.getId() == R.id.selectStartDate) {
            // 시작날짜선택완료 버튼이벤트처리
            Button btnFinishDatePick = (Button) datePickerLayout.findViewById(R.id.btnFinishDatePick);
            btnFinishDatePick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String dateStr = datePicker.getYear() + "-" + (datePicker.getMonth()+1) + "-" + datePicker.getDayOfMonth();
                    Log.d("month1", datePicker.getMonth()+1+"");
                    setStartDate(dateStr);
                    datePickerLayout.setVisibility(View.INVISIBLE);
                }
            });

        } else if(v.getId() == R.id.selectEndDate) {

            // 종료날짜선택완료 버튼이벤트처리
            Button btnFinishDatePick = (Button) datePickerLayout.findViewById(R.id.btnFinishDatePick);
            btnFinishDatePick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    // 시작날짜가 먼저 선택되었는지 검사
                    if(txtFldStartDate.getText().toString().equals("Start Date")) {
                        Toast.makeText(v.getContext(), "시작날짜를 먼저 선택해주세요.", Toast.LENGTH_LONG).show();

                    } else {
                        // 종료날짜가 시작날짜보다 작은 경우 에러메시지 띄우기
                        // 선택된 종료날짜 얻기
                        Integer[] startDateArr = newFormatToIntegerArr(txtFldStartDate.getText().toString());
                        Integer[] endDateArr = new Integer[3];
                        endDateArr[0] = datePicker.getYear();
                        endDateArr[1] = datePicker.getMonth()+1;
                        endDateArr[2] = datePicker.getDayOfMonth();

                        Log.d("startDate", ""+startDateArr[0] +", " + startDateArr[1] + ", " + startDateArr[2]);
                        Log.d("endDate", ""+endDateArr[0] + ", " + endDateArr[1] + ", " + endDateArr[2]);


                        Log.d("dateCon1", String.valueOf(endDateArr[0].compareTo(startDateArr[0])));
                        Log.d("dateCon1", String.valueOf(endDateArr[0].equals(startDateArr[0])));
                        Log.d("dateCon1", String.valueOf(endDateArr[1].compareTo(startDateArr[1])));
                        Log.d("dateCon1", String.valueOf(endDateArr[1].equals(startDateArr[1])));
                        Log.d("dateCon1", String.valueOf(endDateArr[2].compareTo(startDateArr[2])));
                        if (endDateArr[0].compareTo(startDateArr[0]) < 0) {
                            Log.d("endDateError", "1");
                            // 에러메시지
                            Toast.makeText (v.getContext(), "종료날짜(연도)가 시작날짜보다 빠릅니다", Toast.LENGTH_LONG).show();
                            return;
                        } else if(endDateArr[0].equals(startDateArr[0])) {
                            if(endDateArr[1].compareTo(startDateArr[1]) < 0) {
                                Log.d("endDateError", "2");
                                // 에러메시지
                                Toast.makeText (v.getContext(), "종료날짜(월)가 시작날짜보다 빠릅니다", Toast.LENGTH_LONG).show();
                                return;
                            } else if(endDateArr[1].equals(startDateArr[1])) {
                                if(endDateArr[2].compareTo(startDateArr[2]) < 0) {
                                    Log.d("endDateError", "3");
                                    // 에러메시지
                                    Toast.makeText (v.getContext(), "종료날짜(일)가 시작날짜보다 빠릅니다", Toast.LENGTH_LONG).show();
                                    return;
                                }
                            }
                        }

                        // 올바른 종료일자인 경우
                        setEndDate(endDateArr);
                        datePickerLayout.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }
    }

    // 일정아이콘 선택하기 화면 버튼이벤트
    private View.OnClickListener onClickBtnIconSchedule = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            iconSelectLayout.setVisibility(View.VISIBLE);
        }
    };


    // 선택된 아이콘에 따라 일정아이콘 변경
    private View.OnClickListener onClickListenerImgBtnSicon = new View.OnClickListener() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onClick(View v) {
            if(v.getId() == siconAlcohol.getId()) {
                iconSchedule.setBackground(getActivity().getResources().getDrawable(R.drawable.sicon_alcohol));
                siconName = "sicon_alcohol";
                iconSelectLayout.setVisibility(View.INVISIBLE);
            } else if(v.getId() == siconExercise.getId()) {
                iconSchedule.setBackground(getActivity().getResources().getDrawable(R.drawable.sicon_exercise));
                siconName = "sicon_exercise";
                iconSelectLayout.setVisibility(View.INVISIBLE);
            } else if(v.getId() == siconExperiment.getId()) {
                iconSchedule.setBackground(getActivity().getResources().getDrawable(R.drawable.sicon_experiment));
                siconName = "sicon_experiment";
                iconSelectLayout.setVisibility(View.INVISIBLE);
            } else if(v.getId() == siconFootball.getId()) {
                iconSchedule.setBackground(getActivity().getResources().getDrawable(R.drawable.sicon_football));
                siconName = "sicon_football";
                iconSelectLayout.setVisibility(View.INVISIBLE);
            } else if(v.getId() == siconGame.getId()) {
                iconSchedule.setBackground(getActivity().getResources().getDrawable(R.drawable.sicon_game));
                siconName = "sicon_game";
                iconSelectLayout.setVisibility(View.INVISIBLE);
            } else if(v.getId() == siconMeal.getId()) {
                iconSchedule.setBackground(getActivity().getResources().getDrawable(R.drawable.sicon_meal));
                siconName = "sicon_meal";
                iconSelectLayout.setVisibility(View.INVISIBLE);
            } else if(v.getId() == siconMeeting.getId()) {
                iconSchedule.setBackground(getActivity().getResources().getDrawable(R.drawable.sicon_meeting));
                siconName = "sicon_meeting";
                iconSelectLayout.setVisibility(View.INVISIBLE);
            } else if(v.getId() == siconMusic.getId()) {
                iconSchedule.setBackground(getActivity().getResources().getDrawable(R.drawable.sicon_music));
                siconName = "sicon_music";
                iconSelectLayout.setVisibility(View.INVISIBLE);
            } else if(v.getId() == siconParty.getId()) {
                iconSchedule.setBackground(getActivity().getResources().getDrawable(R.drawable.sicon_party));
                siconName = "sicon_party";
                iconSelectLayout.setVisibility(View.INVISIBLE);
            } else if(v.getId() == siconPresentation.getId()) {
                iconSchedule.setBackground(getActivity().getResources().getDrawable(R.drawable.sicon_presentation));
                siconName = "sicon_presentation";
                iconSelectLayout.setVisibility(View.INVISIBLE);
            } else if(v.getId() == siconStudy.getId()) {
                iconSchedule.setBackground(getActivity().getResources().getDrawable(R.drawable.sicon_study));
                siconName = "sicon_study";
                iconSelectLayout.setVisibility(View.INVISIBLE);
            } else if(v.getId() == siconTrophy.getId()) {
                iconSchedule.setBackground(getActivity().getResources().getDrawable(R.drawable.sicon_trophy));
                siconName = "sicon_trophy";
                iconSelectLayout.setVisibility(View.INVISIBLE);
            } else if(v.getId() == siconDefault.getId()) {
                iconSchedule.setBackground(getActivity().getResources().getDrawable(R.drawable.btn_addschedule_cetegory));
                siconName = "btn_addschedule_cetegory";
                iconSelectLayout.setVisibility(View.INVISIBLE);
            }
        }
    };

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

        txtEndDateLabel.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        txtFldEndDate.setText("End Date");
    }

    // set 종료날짜
    private void setEndDate(Integer[] dateArr) {
        String engMonth = strToEngMonth(String.valueOf(dateArr[1]));
        String newDateStr = " " + engMonth + " " + dateArr[2] + ", " + dateArr[0];
        txtEndDateLabel.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        txtFldEndDate.setText(newDateStr);
    }
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

    // "월 일, 연" -> Integer[]로
    private Integer[] newFormatToIntegerArr(String newFormat) {
        Integer[] dateArr = new Integer[3];
        java.util.Date date = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(" MMM dd, yyyy", Locale.US);
            date = dateFormat.parse(newFormat);
            dateArr[0] = date.getYear()+1900;
            dateArr[1] = date.getMonth()+1;
            dateArr[2] = date.getDate();

            Log.d("simpleDateFormatConvert", dateArr[0] + ", " + dateArr[1] + ", " + dateArr[2]);
        } catch(Exception e) { e.printStackTrace(); }

        return dateArr;
    }

}
