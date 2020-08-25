package org.techtown.ydgfinalproject4;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    Button prevmonth, nextmonth; //저번달 다음달
    Button prevweek, nextweek; //저번주 다음주

    TextView monthtextM, yeartextM, monthtextW, yeartextW; //월간 주간 달 년
    TextView week0,week1,week2,week3,week4,week5,week6,week7; //주차

    ArrayList<String> dayList, weekdayList, daylist1, dayList2, items; //요일배열0 1 2, items
    ArrayList<Integer> satList, sunList; //토,일요일 리스트

    GridView monthGrid, weekGrid;//월간 주간 캘린더 그리드뷰
    GridAdapter monthGridAdapter;    //월간 그리드뷰 어댑터
    weekGridAdapter weekGridAdapter;  //주간 그리드뷰 어댑터

    Button beginCF, beginCC; //확인
    Button finishCF, finishCC; //취소

    TextView beginday,finishday, scheduleview; //시작일 종료일, 스케쥴뷰

    Calendar Cal = Calendar.getInstance();//캘린더 변수 선언

    int sat, sun; //토 일
    int curYear, curMonth, curDay, curWeek, curDayofMonth, curHour, curMin; //현재
    int initYear, initMonth, initWeek, initDay, initDayofMonth, initHour, initMin; //초기화
    int dayNum; //요일에 정수 값 대응
    int scheduleday; //스케줄 있는 날

    int weekday; //주중 일

    LinearLayout monthview, weekview; //월간 주간 뷰
    LinearLayout begindaylayout, finishdaylayout;//시작날 종료날
    LinearLayout begindaybtnlayout, finishdaybtnlayout;//시작날버튼 종료날버튼

    View dialogView; //대화상자

    int beginHour, finishHour; //시작 종료 시
    int beginMin, finishMin; //시작 종료 분
    int beginYear, finishYear; //시작 종료 연
    int beginMonth, finishMonth;//시작 종료 달
    int beginDay, finishDay;//시작 종료 날

    EditText schedulename, scheduleloc, schedulememo; //스케쥴 이름 장소 메모

    DatePicker schedulebegin, schedulefinish; //스케줄 시작 종료
    TimePicker schedulebegintime, schedulefinishtime; //스케줄 시작시간 종료시간

    String textfile, schedulevalue;//텍스트 파일  스케쥴 내용

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        monthtextM = findViewById(R.id.MonthtextM);
        yeartextM = findViewById(R.id.YeartextM);
        monthtextW = findViewById(R.id.MonthtextW);
        yeartextW = findViewById(R.id.YeartextW);
        monthGrid = findViewById(R.id.MonthGrid);
        weekGrid = findViewById(R.id.WeekGrid);
        prevmonth = findViewById(R.id.Prevmonth);
        nextmonth = findViewById(R.id.Nextmonth);
        prevweek = findViewById(R.id.Prevweek);
        nextweek = findViewById(R.id.Nextweek);
        week0 = findViewById(R.id.WeekNum);
        week1 = findViewById(R.id.Sun);
        week2 = findViewById(R.id.Mon);
        week3 = findViewById(R.id.Tue);
        week4 = findViewById(R.id.Wed);
        week5 = findViewById(R.id.Thu);
        week6 = findViewById(R.id.Fri);
        week7 = findViewById(R.id.Sat);
        monthview = findViewById(R.id.monthview);
        weekview = findViewById(R.id.weekview);
        schedulename = findViewById(R.id.Schedulename);

        long today = System.currentTimeMillis(); //현재시간 매칭
        final Date date = new Date(today);

        final SimpleDateFormat currentYear = new SimpleDateFormat("YYYY", Locale.KOREA);
        final SimpleDateFormat currentMonth = new SimpleDateFormat("MM", Locale.KOREA);
        final SimpleDateFormat currentDay = new SimpleDateFormat("DD", Locale.KOREA);

        initYear = Integer.parseInt(currentYear.format(date));
        initMonth = Integer.parseInt(currentMonth.format(date));
        initDay = Integer.parseInt(currentDay.format(date));
        initWeek = Cal.get(Calendar.WEEK_OF_YEAR);
        initDayofMonth = Cal.get(Calendar.DAY_OF_MONTH);

        //현재값에 초기값 대입
        curWeek=initWeek;
        curYear=initYear;
        curMonth=initMonth;
        curDay=initDay;

        //월 년 표시
        monthtextM.setText(initMonth+"월");
        monthtextW.setText(initMonth+"월");
        yeartextM.setText(initYear+"년");
        yeartextW.setText(initYear+"년");

        Cal = Calendar.getInstance();
        Cal.set(initYear,initMonth-1, 1); //캘린더 변수 초기화
        dayNum = Cal.get(Calendar.DAY_OF_WEEK);  //이 달의 1일이 무슨 요일인지 확인함

        setmonthcal();//월간 달력 초기화

        dayList2 = new ArrayList<String>();
        dayList2.add(" SUN");
        dayList2.add(" M0N"); //대문자 O넣으면 사이즈 초과 숫자0으로 대체함
        dayList2.add(" TUE");
        dayList2.add(" WED");
        dayList2.add(" THU");
        dayList2.add("  FRI");//사이즈 맞추기 위해 공백 넣음
        dayList2.add(" SAT");

        setweekcal();//주별달력 초기화+

        items = new ArrayList<String>();
        //월 주간 어댑터 set
        monthGrid.setAdapter(monthGridAdapter);
        weekGrid.setAdapter(weekGridAdapter);

        //저번 달 버튼
        prevmonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(curMonth==1){
                    curMonth=12;
                    curYear=curYear-1;
                } else {
                    curMonth=curMonth-1;
                }

                Cal.set(curYear,curMonth-1,1);

                dayNum = Cal.get(Calendar.DAY_OF_WEEK);
                monthtextM.setText(curMonth+"월");
                yeartextM.setText(curYear+"년"); //출력

                setmonthcal(); //초기화

                monthGridAdapter.notifyDataSetChanged();
                monthGrid.setAdapter(monthGridAdapter);//어댑터 재설정
            }
        });

        //다음 달 버튼
        nextmonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(curMonth==12){
                    curMonth=1;
                    curYear=curYear+1;
                } else {
                    curMonth=curMonth+1;
                }

                Cal.set(curYear,curMonth-1,1);

                dayNum = Cal.get(Calendar.DAY_OF_WEEK);
                monthtextM.setText(curMonth+"월");
                yeartextM.setText(curYear+"년");

                setmonthcal();

                monthGridAdapter.notifyDataSetChanged();
                monthGrid.setAdapter(monthGridAdapter);
            }
        });

        //저번 주 버튼
        prevweek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                curWeek = curWeek-1;
                setweekcal();
                curMonth = Cal.get(Calendar.MONTH)+1;
                weekGridAdapter.notifyDataSetChanged();
                weekGrid.setAdapter(weekGridAdapter);
                if(curWeek==1){
                    curMonth=1;
                }
                monthtextW.setText(curMonth+"월");
                yeartextW.setText(curYear+"년");
            }
        });

        //다음 주 버튼
        nextweek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                curWeek = curWeek + 1;
                setweekcal();
                curMonth = Cal.get(Calendar.MONTH)+1;
                weekGridAdapter.notifyDataSetChanged();
                weekGrid.setAdapter(weekGridAdapter);
                if(curWeek==1){
                    curMonth=1;
                }
                monthtextW.setText(curMonth+"월");
                yeartextW.setText(curYear+"년");
            }
        });

        //옵션 메뉴 누르면 월간 띄우기
        monthGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                scheduleday = position - dayNum + 2;
                textfile = curYear + "_" + curMonth + "_" + scheduleday + ".txt";
                String filepath = "/data/data/org.techtown.ydgfinalproject4/files/" + textfile; //파일 저장 경로
                File files = new File(filepath);
                if (files.exists() == true) { //저장된 파일 있을 경우
                    Toast.makeText(getApplicationContext(), "Schedule Already Exist", Toast.LENGTH_SHORT).show();
                    dialogView = View.inflate(MainActivity.this, R.layout.showschedule, null);
                    scheduleview = dialogView.findViewById(R.id.Scheduleview);
                    readEvent();
                    scheduleview.setText(items.get(0)+"\n"+items.get(1)+"\n"+items.get(2)+"\n"+items.get(3)+"\n"+items.get(4)+"\n"+items.get(5)+"\n"+items.get(6));
                    AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                    dlg.setTitle("Today's Schedule");
                    dlg.setView(dialogView);
                    dlg.setPositiveButton("Cancel", null);
                    dlg.setNegativeButton("Remove", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deletefile(textfile);
                            Toast.makeText(getApplicationContext(), "Schedule Deleted", Toast.LENGTH_SHORT).show();
                            monthGrid.setAdapter(monthGridAdapter);
                        }
                    });
                    dlg.show();
                } else {
                    dialogView = View.inflate(MainActivity.this, R.layout.schedule, null);
                    schedulename = dialogView.findViewById(R.id.Schedulename);
                    schedulebegin = dialogView.findViewById(R.id.Schedulebegin);
                    schedulebegintime = dialogView.findViewById(R.id.Schedulebegintime);
                    initHour = Cal.get(Calendar.HOUR_OF_DAY);
                    beginYear = curYear;
                    finishYear = beginYear;
                    beginMonth = curMonth;
                    finishMonth = curMonth;
                    beginDay = scheduleday;
                    finishDay = beginDay;
                    curHour = initHour;
                    beginHour = initHour;
                    initMin = 00;
                    curMin = initMin;
                    beginMin = curMin;
                    finishMin = beginMin;
                    finishHour = beginHour + 1;
                    begindaylayout = dialogView.findViewById(R.id.Begindaylayout);
                    finishdaylayout = dialogView.findViewById(R.id.Finishdaylayout);
                    //다이얼로그에서 쓸 지역변수 선언 및 xml 링크
                    schedulebegin.init(curYear, curMonth - 1, scheduleday, new DatePicker.OnDateChangedListener() {
                        @Override
                        public void onDateChanged(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                            beginYear = year;
                            finishYear = beginYear;
                            beginMonth = monthOfYear +1;
                            finishMonth = beginMonth;
                            beginDay = dayOfMonth;
                            finishDay = beginDay;
                            beginday.setText("BeginDay : " + beginYear + "/ " + beginMonth + "/ " + beginDay + "   " + (curHour + 1) + ":" + beginMin );
                            finishday.setText("FinishDay : " + finishYear + "/" + finishMonth + "/ " + finishDay + "   " + (curHour + 2) + ":" + finishMin );
                        }
                    });
                    schedulebegintime.setHour(curHour + 1);
                    schedulebegintime.setMinute(0);
                    schedulebegintime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                        @Override
                        public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
                            beginHour = hour;
                            finishHour = beginHour + 1;
                            beginMin = minute;
                            finishMin = beginMin;
                            beginday.setText("BeginDay : " + beginYear + "/ " + beginMonth + "/ " + beginDay + "   " + beginHour + ":" + beginMin );
                            finishday.setText("FinishDay : " + finishYear + "/ " + finishMonth + "/ " + finishDay + "   " + finishHour + ":" + finishMin );
                        }
                    });
                    schedulefinish = dialogView.findViewById(R.id.ScheduleFinish);
                    schedulefinish.init(curYear, curMonth - 1, scheduleday, new DatePicker.OnDateChangedListener() {
                        @Override
                        public void onDateChanged(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                            finishYear = year;
                            finishMonth = monthOfYear +1;
                            finishDay = dayOfMonth;
                            finishHour = curHour + 2;
                            finishday.setText("FinishDay : " + finishYear + "/ " + finishMonth + "/ " + finishDay + "   " + finishHour + ":" + finishMin );
                        }
                    });
                    schedulefinishtime = dialogView.findViewById(R.id.ScheduleFinishtime);
                    schedulefinishtime.setHour(curHour + 2);
                    schedulefinishtime.setMinute(0);
                    schedulefinishtime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                        @Override
                        public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
                            finishHour = hour;
                            finishMin = minute;
                            finishday.setText("FinishDay : " + finishYear + "/ " + finishMonth + "/ " + finishDay + "   " + finishHour + ":" + finishMin );
                        }
                    });
                    scheduleloc = dialogView.findViewById(R.id.Scheduleloc);
                    schedulememo = dialogView.findViewById(R.id.Schedulememo);
                    beginday = dialogView.findViewById(R.id.Beginday);
                    beginday.setText("BeginDay : " + curYear + "/ " + curMonth + "/ " + scheduleday + "   " + beginHour + ":" + beginMin );
                    beginday.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            begindaylayout.setVisibility(View.VISIBLE);
                            begindaybtnlayout.setVisibility(View.VISIBLE);
                        }
                    });
                    finishday = dialogView.findViewById(R.id.Finishday);
                    finishday.setText("FinishDay : " + finishYear + "/ " + finishMonth + "/ " + finishDay + "   " + finishHour + ":" + finishMin );
                    finishday.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finishdaylayout.setVisibility(View.VISIBLE);
                            finishdaybtnlayout.setVisibility(View.VISIBLE);
                        }
                    });
                    begindaybtnlayout = dialogView.findViewById(R.id.Begindaybtnlayout);
                    finishdaybtnlayout = dialogView.findViewById(R.id.Finishdaybtnlayout);
                    beginCF = dialogView.findViewById(R.id.BeginCF);
                    beginCF.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            begindaylayout.setVisibility(View.GONE);
                            begindaybtnlayout.setVisibility(View.GONE);
                        }
                    });
                    beginCC = dialogView.findViewById(R.id.BeginCC);
                    beginCC.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            begindaylayout.setVisibility(View.GONE);
                            begindaybtnlayout.setVisibility(View.GONE);
                            beginday.setText("BeginDay : " + curYear + "/ " + curMonth + "/ " + scheduleday + "   " + curHour + ":" + curMonth );
                        }
                    });
                    finishCF = dialogView.findViewById(R.id.FinishCF);
                    finishCF.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finishdaylayout.setVisibility(View.GONE);
                            finishdaybtnlayout.setVisibility(View.GONE);
                        }
                    });
                    finishCC = dialogView.findViewById(R.id.FinishCC);
                    finishCC.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finishdaylayout.setVisibility(View.GONE);
                            finishdaybtnlayout.setVisibility(View.GONE);
                            finishday.setText("FinishDay : " + finishYear + "/ " + finishMonth + "/ " + finishDay + "   " + finishHour + ":" + curMonth );
                        }
                    });

                    AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                    dlg.setTitle("Today's Schedule");
                    dlg.setView(dialogView);
                    dlg.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            textfile = beginYear + "_" + beginMonth + "_" + beginDay + ".txt";
                            saveEvent();
                            Toast.makeText(getApplicationContext(), "Schedule Saved", Toast.LENGTH_SHORT).show();
                            monthGrid.setAdapter(monthGridAdapter);//월별달력 어댑터설정
                        }
                    });
                    dlg.setNegativeButton("Cancel", null);
                    dlg.show();
                }
            }
        });

        weekGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                final int time, day;
                String dayko = null;
                time = position / 8; //포지션으로 날짜 매칭
                day = position % 8;
                weekday = curDayofMonth + day - 1;
                if (weekday > Cal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                    weekday = weekday - Cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                    curMonth = curMonth + 1;
                }
                textfile = curYear + "_" + curMonth + "_" + weekday + ".txt";
                String filepath = "/data/data/org.techtown.ydgfinalproject4/files/" + textfile;
                File files = new File(filepath);
                if (files.exists() == true) {
                    Toast.makeText(getApplicationContext(), "Schedule Already Exist", Toast.LENGTH_SHORT).show();
                    readEvent();
                    String text = items.get(0);
                    String[] eventTitle = text.split(" : ");
                    String starttime = items.get(2);
                    String[] startTime0 = starttime.split(" : ");
                    String[] startTime1 = startTime0[1].split(":");
                    int startTime = Integer.parseInt(startTime1[0]);
                    String endtime = items.get(4);
                    String[] endTime0 = endtime.split(" : ");
                    String[] endTime1 = endTime0[1].split(":");
                    int endTime = Integer.parseInt(endTime1[0]);
                    //일정 제목과 시작시간, 종료시간을 검출
                    if (time >= startTime && time < endTime ) {
                        dialogView = View.inflate(MainActivity.this, R.layout.showschedule, null);
                        scheduleview = dialogView.findViewById(R.id.Scheduleview);
                        items = new ArrayList<String>();
                        readEvent();
                        scheduleview.setText(items.get(0) + "\n" + items.get(1) + "\n" + items.get(2) + "\n" + items.get(3) + "\n" + items.get(4) + "\n" + items.get(5) + "\n" + items.get(6));
                        AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                        dlg.setTitle("Today's Schedule");
                        dlg.setView(dialogView);
                        dlg.setPositiveButton("Cancel", null);
                        dlg.setNegativeButton("Remove", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deletefile(textfile);
                                Toast.makeText(getApplicationContext(), "Schedule Deleted", Toast.LENGTH_SHORT).show();
                                weekGrid.setAdapter(weekGridAdapter);
                                monthGrid.setAdapter(monthGridAdapter);
                            }
                        });
                        dlg.show();
                    } else {
                        if (position % 8 == 0) {
                            Toast.makeText(getApplicationContext(), "Cannot be Selected", Toast.LENGTH_SHORT).show();

                        } else {
                            if (day == 1) {
                                dayko = "SUN";
                            } else if (day == 2) {
                                dayko = "MON";
                            } else if (day == 3) {
                                dayko = "TUE";
                            } else if (day == 4) {
                                dayko = "WED";
                            } else if (day == 5) {
                                dayko = "THU";
                            } else if (day == 6) {
                                dayko = "FRI";
                            } else if (day == 7) {
                                dayko = "SAT";
                            }
                            textfile = curYear + "_" + curMonth + "_" + weekday + "_" + time + ".txt";
                            dialogView = View.inflate(MainActivity.this, R.layout.schedule, null);
                            schedulename = dialogView.findViewById(R.id.Schedulename);
                            schedulebegin = dialogView.findViewById(R.id.Schedulebegin);
                            schedulebegintime = dialogView.findViewById(R.id.Schedulebegintime);
                            initHour = Cal.get(Calendar.HOUR_OF_DAY);
                            beginYear = curYear;
                            finishYear = beginYear;
                            beginMonth = curMonth;
                            finishMonth = curMonth;
                            beginDay = weekday;
                            finishDay = beginDay;
                            curHour = initHour;
                            beginHour = time;
                            initMin = 00;
                            curMin = initMin;
                            beginMin = curMin;
                            finishMin = beginMin;
                            finishHour = beginHour + 1;
                            begindaylayout = dialogView.findViewById(R.id.Begindaylayout);
                            finishdaylayout = dialogView.findViewById(R.id.Finishdaylayout);

                            schedulebegin.init(curYear, curMonth - 1, weekday, new DatePicker.OnDateChangedListener() {
                                @Override
                                public void onDateChanged(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                    beginYear = year;
                                    finishYear = beginYear;
                                    beginMonth = monthOfYear+1; //1작게나옴 그래서 1더함
                                    finishMonth = beginMonth;
                                    beginDay = dayOfMonth;
                                    finishDay = beginDay;
                                    beginday.setText("BeginDay : " + beginYear + "/ " + beginMonth + "/ " + weekday + "   " + time + ": " + beginMin  );
                                    finishday.setText("FinishDay : " + finishYear + "/ " + finishMonth + "/ " + weekday + "   " + time + ": " + finishMin );
                                }
                            });
                            schedulebegintime.setHour(time);
                            schedulebegintime.setMinute(0);
                            schedulebegintime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                                @Override
                                public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
                                    beginHour = hour;
                                    finishHour = beginHour + 1;
                                    beginMin = minute;
                                    beginday.setText("BeginDay : " + beginYear + "/ " + beginMonth + "/ " + beginDay + "   " + beginHour + ": " + beginMin );
                                    finishday.setText("FinishDay : " + finishYear + "/ " + finishMonth + "/ " + finishDay + "   " + finishHour + ": 0");
                                }
                            });
                            schedulefinish = dialogView.findViewById(R.id.ScheduleFinish);
                            schedulefinish.init(curYear, curMonth - 1, weekday, new DatePicker.OnDateChangedListener() {
                                @Override
                                public void onDateChanged(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                    finishYear = year;
                                    finishHour = beginHour + 1;
                                    finishMonth = monthOfYear + 1;
                                    finishDay = dayOfMonth;
                                    finishday.setText("FinishDay : " + finishYear + "/ " + finishMonth + "/ " + finishDay + "   " + finishHour + ": " + finishMin );
                                }
                            });
                            schedulefinishtime = dialogView.findViewById(R.id.ScheduleFinishtime);
                            schedulefinishtime.setHour(time + 1);
                            schedulefinishtime.setMinute(0);
                            schedulefinishtime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                                @Override
                                public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
                                    finishHour = hour;
                                    finishMin = minute;
                                    finishday.setText("FinishDay : " + finishYear + "/ " + finishMonth + "/ " + finishDay + "   " + finishHour + ": " + finishMin );
                                }
                            });
                            scheduleloc = dialogView.findViewById(R.id.Scheduleloc);
                            schedulememo = dialogView.findViewById(R.id.Schedulememo);
                            beginday = dialogView.findViewById(R.id.Beginday);
                            beginday.setText("BeginDay : " + beginYear + "/ " + beginMonth + "/ " + weekday + "   " + beginHour + ": " + beginMin);
                            beginday.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    begindaylayout.setVisibility(View.VISIBLE);
                                    begindaybtnlayout.setVisibility(View.VISIBLE);
                                }
                            });
                            finishday = dialogView.findViewById(R.id.Finishday);
                            finishday.setText("FinishDay : " + finishYear + "/ " + finishMonth + "/ " + finishDay + "   " + finishHour + ": " + finishMin );
                            finishday.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    finishdaylayout.setVisibility(View.VISIBLE);
                                    finishdaybtnlayout.setVisibility(View.VISIBLE);
                                }
                            });
                            begindaybtnlayout = dialogView.findViewById(R.id.Begindaybtnlayout);
                            finishdaybtnlayout = dialogView.findViewById(R.id.Finishdaybtnlayout);
                            beginCF = dialogView.findViewById(R.id.BeginCF);
                            beginCF.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    begindaylayout.setVisibility(View.GONE);
                                    begindaybtnlayout.setVisibility(View.GONE);
                                }
                            });
                            beginCC = dialogView.findViewById(R.id.BeginCC);
                            beginCC.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    begindaylayout.setVisibility(View.GONE);
                                    begindaybtnlayout.setVisibility(View.GONE);
                                    beginday.setText("BeginDay : " + beginYear + "/ " + beginMonth + "/ " + weekday + "   " + beginHour + ": " + beginMin );
                                }
                            });
                            finishCF = dialogView.findViewById(R.id.FinishCF);
                            finishCF.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    finishdaylayout.setVisibility(View.GONE);
                                    finishdaybtnlayout.setVisibility(View.GONE);
                                }
                            });
                            finishCC = dialogView.findViewById(R.id.FinishCC);
                            finishCC.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    finishdaylayout.setVisibility(View.GONE);
                                    finishdaybtnlayout.setVisibility(View.GONE);
                                    finishday.setText("Finish : " + finishYear + "/ " + finishMonth + "/ " + weekday + "   " + finishHour + ": " + finishMin );
                                }
                            });

                            AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                            dlg.setTitle("Today's Schedule");
                            dlg.setView(dialogView);
                            dlg.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    textfile = beginYear + "_" + beginMonth + "_" + beginDay + ".txt";
                                    saveEvent();
                                    weekGrid.setAdapter(weekGridAdapter);
                                    monthGrid.setAdapter(monthGridAdapter);
                                }
                            });
                            dlg.setNegativeButton("Cancel", null);
                            dlg.show();
                        }
                    }
                    }else {
                     if (position % 8 == 0) {
                        Toast.makeText(getApplicationContext(), "Cannot be Selected", Toast.LENGTH_SHORT).show();

                       } else {
                        if (day == 1) {
                            dayko = "SUN";
                        } else if (day == 2) {
                            dayko = "MON";
                        } else if (day == 3) {
                            dayko = "TUE";
                        } else if (day == 4) {
                            dayko = "WED";
                        } else if (day == 5) {
                            dayko = "THU";
                        } else if (day == 6) {
                            dayko = "FRI";
                        } else if (day == 7) {
                            dayko = "SAT";
                        }
                        textfile = curYear + "_" + curMonth + "_" + weekday + "_" + time + ".txt";
                        dialogView = View.inflate(MainActivity.this, R.layout.schedule, null);
                        schedulename = dialogView.findViewById(R.id.Schedulename);
                        schedulebegin = dialogView.findViewById(R.id.Schedulebegin);
                        schedulebegintime = dialogView.findViewById(R.id.Schedulebegintime);
                        initHour = Cal.get(Calendar.HOUR_OF_DAY);
                        beginYear = curYear;
                        finishYear = beginYear;
                        beginMonth = curMonth;
                        finishMonth = curMonth;
                        beginDay = weekday;
                        finishDay = beginDay;
                        curHour = initHour;
                        beginHour = time;
                        initMin = 00;
                        curMin = initMin;
                        beginMin = curMin;
                        finishMin = beginMin;
                        finishHour = beginHour + 1;
                        begindaylayout = dialogView.findViewById(R.id.Begindaylayout);
                        finishdaylayout = dialogView.findViewById(R.id.Finishdaylayout);

                        schedulebegin.init(curYear, curMonth - 1, weekday, new DatePicker.OnDateChangedListener() {
                            @Override
                            public void onDateChanged(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                beginYear = year;
                                finishYear = beginYear;
                                beginMonth = monthOfYear + 1; //1작게나옴 그래서 1더함
                                finishMonth = beginMonth;
                                beginDay = dayOfMonth;
                                finishDay = beginDay;
                                beginday.setText("BeginDay : " + beginYear + "/ " + beginMonth + "/ " + weekday + "   " + time + ": " + beginMin);
                                finishday.setText("FinishDay : " + finishYear + "/ " + finishMonth + "/ " + weekday + "   " + time + ": " + finishMin);
                            }
                        });
                        schedulebegintime.setHour(time);
                        schedulebegintime.setMinute(0);
                        schedulebegintime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                            @Override
                            public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
                                beginHour = hour;
                                finishHour = beginHour + 1;
                                beginMin = minute;
                                beginday.setText("BeginDay : " + beginYear + "/ " + beginMonth + "/ " + beginDay + "   " + beginHour + ": " + beginMin);
                                finishday.setText("FinishDay : " + finishYear + "/ " + finishMonth + "/ " + finishDay + "   " + finishHour + ": 00");
                            }
                        });
                        schedulefinish = dialogView.findViewById(R.id.ScheduleFinish);
                        schedulefinish.init(curYear, curMonth - 1, weekday, new DatePicker.OnDateChangedListener() {
                            @Override
                            public void onDateChanged(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                finishYear = year;
                                finishHour = beginHour + 1;
                                finishMonth = monthOfYear + 1;
                                finishDay = dayOfMonth;
                                finishday.setText("FinishDay : " + finishYear + "/ " + finishMonth + "/ " + finishDay + "   " + finishHour + ": " + finishMin);
                            }
                        });
                        schedulefinishtime = dialogView.findViewById(R.id.ScheduleFinishtime);
                        schedulefinishtime.setHour(time + 1);
                        schedulefinishtime.setMinute(0);
                        schedulefinishtime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                            @Override
                            public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
                                finishHour = hour;
                                finishMin = minute;
                                finishday.setText("FinishDay : " + finishYear + "/ " + finishMonth + "/ " + finishDay + "   " + finishHour + ": " + finishMin);
                            }
                        });
                        scheduleloc = dialogView.findViewById(R.id.Scheduleloc);
                        schedulememo = dialogView.findViewById(R.id.Schedulememo);
                        beginday = dialogView.findViewById(R.id.Beginday);
                        beginday.setText("BeginDay : " + beginYear + "/ " + beginMonth + "/ " + weekday + "   " + beginHour + ": " + beginMin);
                        beginday.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                begindaylayout.setVisibility(View.VISIBLE);
                                begindaybtnlayout.setVisibility(View.VISIBLE);
                            }
                        });
                        finishday = dialogView.findViewById(R.id.Finishday);
                        finishday.setText("FinishDay : " + finishYear + "/ " + finishMonth + "/ " + finishDay + "   " + finishHour + ": " + finishMin);
                        finishday.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finishdaylayout.setVisibility(View.VISIBLE);
                                finishdaybtnlayout.setVisibility(View.VISIBLE);
                            }
                        });
                        begindaybtnlayout = dialogView.findViewById(R.id.Begindaybtnlayout);
                        finishdaybtnlayout = dialogView.findViewById(R.id.Finishdaybtnlayout);
                        beginCF = dialogView.findViewById(R.id.BeginCF);
                        beginCF.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                begindaylayout.setVisibility(View.GONE);
                                begindaybtnlayout.setVisibility(View.GONE);
                            }
                        });
                        beginCC = dialogView.findViewById(R.id.BeginCC);
                        beginCC.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                begindaylayout.setVisibility(View.GONE);
                                begindaybtnlayout.setVisibility(View.GONE);
                                beginday.setText("BeginDay : " + beginYear + "/ " + beginMonth + "/ " + weekday + "   " + beginHour + ": " + beginMin);
                            }
                        });
                        finishCF = dialogView.findViewById(R.id.FinishCF);
                        finishCF.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finishdaylayout.setVisibility(View.GONE);
                                finishdaybtnlayout.setVisibility(View.GONE);
                            }
                        });
                        finishCC = dialogView.findViewById(R.id.FinishCC);
                        finishCC.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finishdaylayout.setVisibility(View.GONE);
                                finishdaybtnlayout.setVisibility(View.GONE);
                                finishday.setText("Finish : " + finishYear + "/ " + finishMonth + "/ " + weekday + "   " + finishHour + ": " + finishMin);
                            }
                        });

                        AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                        dlg.setTitle("Today's Schedule");
                        dlg.setView(dialogView);
                        dlg.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                textfile = beginYear + "_" + beginMonth + "_" + beginDay + ".txt";
                                saveEvent();
                                weekGrid.setAdapter(weekGridAdapter);
                                monthGrid.setAdapter(monthGridAdapter);
                            }
                        });
                        dlg.setNegativeButton("Cancel", null);
                        dlg.show();
                    }
                }
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch(id){
            case R.id.todayitem:
                Cal.set(initYear,initMonth-1, 1);
                curMonth=initMonth;
                curYear=initYear;
                curWeek=initWeek;
                dayNum = Cal.get(Calendar.DAY_OF_WEEK);
                setmonthcal();
                monthGridAdapter.notifyDataSetChanged();
                monthGrid.setAdapter(monthGridAdapter);
                setweekcal();
                weekGridAdapter.notifyDataSetChanged();
                weekGrid.setAdapter(weekGridAdapter);
                monthtextM.setText(initMonth+"월");
                yeartextM.setText(initYear+"년");
                monthtextW.setText(initMonth+"월");
                yeartextW.setText(initYear+"년");
                break;

            //월간 보기
            case R.id.monthview:
                monthview.setVisibility(View.VISIBLE);
                weekview.setVisibility(View.INVISIBLE);
                break;

            //주간 보기
            case R.id.weekview:
                monthview.setVisibility(View.INVISIBLE);
                weekview.setVisibility(View.VISIBLE);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    void saveEvent(){
        FileOutputStream outFs = null;
        try {
            outFs = openFileOutput(textfile, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        schedulevalue = "Schedule : "+schedulename.getText().toString()+"\nBeginDay : "+beginYear+"/ "+beginMonth+"/ "+beginDay+"  "+"\nBeginTime : "+beginHour+": "+beginMin+" "+
                "\nFinishDay : "+finishYear+"/ "+finishMonth+"/ "+finishDay+"  "+"\nFinishTime : "+finishHour+": "+finishMin+" "+"\nLocation : "+scheduleloc.getText().toString()+"\nMemo : "+schedulememo.getText().toString();
        try {
            outFs.write(schedulevalue.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outFs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(getApplicationContext(),textfile+" Saved.", Toast.LENGTH_SHORT).show();
    }
    //스케줄 save

    void readEvent(){
        items = new ArrayList<String>();
        File file = new File("/data/data/org.techtown.ydgfinalproject4/files/", textfile);
        FileReader fr = null;
        BufferedReader bufrd = null;
        String eventStr = null;

        try {
            fr = new FileReader(file);
            bufrd = new BufferedReader(fr);

            while ((eventStr = bufrd.readLine()) != null) {
                items.add(eventStr);
            }

            bufrd.close();
            fr.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    //스케줄 read

    public static void deletefile(String filename){
        File file = new File("/data/data/org.techtown.ydgfinalproject4/files/"+filename);
        file.delete();
    }
    void setmonthcal(){
        dayList = new ArrayList<String>();
        if(dayNum==1){
            sun = 1;
            sat = 7;
        } else if (dayNum==2){
            sun = 7;
            sat = 6;
        } else if (dayNum==3){
            sun = 6;
            sat = 5;
        } else if (dayNum==4){
            sun = 5;
            sat = 4;
        } else if (dayNum==5){
            sun = 4;
            sat = 3;
        } else if (dayNum==6){
            sun = 3;
            sat = 2;
        } else if (dayNum==7){
            sun = 2;
            sat = 1;
        }

        for (int i = 1; i<dayNum; i++){
            dayList.add("");
        }

        setCalendarDate(Cal.get(Calendar.MONTH)+1);
        monthGridAdapter = new GridAdapter(getApplicationContext(), dayList);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    void setweekcal(){
        weekdayList = new ArrayList<String>();
        daylist1 = new ArrayList<String>();
        Cal.setWeekDate(curYear,curWeek,Calendar.SUNDAY);
        if(curWeek>Cal.get(Calendar.WEEK_OF_YEAR)){
            curWeek=Cal.get(Calendar.WEEK_OF_YEAR);
            curYear=curYear+1;
        } else if(curWeek==0){
            curWeek=Cal.get(Calendar.WEEK_OF_YEAR);
            curYear=curYear-1;
        }

        curDayofMonth = Cal.get(Calendar.DAY_OF_MONTH);
        daylist1.add(0,"CLK");
        for(int i=0; i<7;i++){
            daylist1.add(i+1,curDayofMonth+i+dayList2.get(i));
        }
        // 숫자와 영문 요일 출력
        for (int i=0; i<7;i++){
            if(curDayofMonth+i>Cal.getActualMaximum(Calendar.DAY_OF_MONTH)){
                int maxday = Cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                daylist1.set(i+1,curDayofMonth-maxday+i+dayList2.get(i));
            }
            //해당 월의 최대 일수를 초과하게 되면 1부터 하나씩 다시 시작
        }

        week0.setText(daylist1.get(0));
        week1.setText(daylist1.get(1));
        week2.setText(daylist1.get(2));
        week3.setText(daylist1.get(3));
        week4.setText(daylist1.get(4));
        week5.setText(daylist1.get(5));
        week6.setText(daylist1.get(6));
        week7.setText(daylist1.get(7));

        int weekdayno = 193;
        //weekdayno = 8일*24시간=192 이고, 192+1 해서 193

        for (int i = 1; i<weekdayno; i++){
            weekdayList.add("");
        }

        for (int i = 0; i<24; i++){
            if(i==0){
                weekdayList.set(8*i,12+"");
            } else {
                weekdayList.set(8*i,i+ "");
            }
        }
        //왼쪽은 시간대
        weekGridAdapter = new weekGridAdapter(getApplicationContext(), weekdayList);

    }


    private void setCalendarDate(int month){
        Cal.set(Calendar.MONTH, month-1);
        for (int i=0; i<Cal.getActualMaximum(Calendar.DAY_OF_MONTH); i++){
            dayList.add(""+(i+1));
        }
    }

    private class GridAdapter extends BaseAdapter {
        private final List<String> list;
        private final LayoutInflater inflater;

        public GridAdapter(Context context, List<String>list){
            this.list = list;
            this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount(){
            return list.size();
        }

        @Override
        public String getItem(int position){
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;

        }

        @Override
        public View getView(int position, android.view.View convertView, ViewGroup parent){

            ViewHolder holder = null;

            if(convertView==null){
                convertView = inflater.inflate(R.layout.month_calendar,parent,false);
                holder = new ViewHolder();

                holder.tvItemGridView = (TextView)convertView.findViewById(R.id.item);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }

            holder.tvItemGridView.setText(""+getItem(position));


            Cal = Calendar.getInstance();


            satList = new ArrayList<Integer>();
            satList.add(0,sat);
            satList.add(1,sat+7);
            satList.add(2,sat+14);
            satList.add(3,sat+21);
            satList.add(4,sat+28);
            //토요일들 구함



            for(int i=0;i<satList.size();i++){
                String ssat = String.valueOf(satList.get(i));
                if(ssat.equals(getItem(position)))
                    holder.tvItemGridView.setTextColor(getResources().getColor(R.color.colorBlue));
            }
            //토요일 파란색 표시


            sunList = new ArrayList<Integer>();
            sunList.add(0,sun);
            sunList.add(1,sun+7);
            sunList.add(2,sun+14);
            sunList.add(3,sun+21);
            sunList.add(4,sun+28);
            //일요일들 구함

            for(int i=0;i<sunList.size();i++){
                String ssun = String.valueOf(sunList.get(i));
                if(ssun.equals(getItem(position)))
                    holder.tvItemGridView.setTextColor(getResources().getColor(R.color.colorRed));
            }
            //일요일 빨간색 표시
            Integer today = Cal.get(Calendar.DAY_OF_MONTH);
            String stoday = String.valueOf(today);
            if(stoday.equals(getItem(position))&&initYear==curYear&&initMonth==curMonth){
                holder.tvItemGridView.setBackgroundColor(getResources().getColor(R.color.colorGreen));
            }
            //오늘 표시 색 녹색

            scheduleday = position - dayNum + 2;
            textfile = curYear + "_" + curMonth + "_" + scheduleday + ".txt";
            String filepath = "/data/data/org.techtown.ydgfinalproject4/files/"+textfile;
            File files = new File(filepath);

            if (files.exists() == true) {
                holder.tvItemGridView.setBackgroundColor(getResources().getColor(R.color.colorSky));
            }
            //스케쥴이 있는 날은 노란색


            return convertView;

        }


    }

    private class weekGridAdapter extends BaseAdapter {
        private final List<String> list;
        private final LayoutInflater inflater;

        public weekGridAdapter(Context context, List<String>list){
            this.list = list;
            this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount(){
            return list.size();
        }

        @Override
        public String getItem(int position){
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;

        }

        @Override
        public View getView(int position, android.view.View convertView, ViewGroup parent){

            ViewHolder holder = null;

            if(convertView==null){
                convertView = inflater.inflate(R.layout.week_calendar,parent,false);
                holder = new ViewHolder();

                holder.tvItemGridView = (TextView)convertView.findViewById(R.id.item);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.tvItemGridView.setText(""+getItem(position));
            Cal = Calendar.getInstance();


            final int time, day;
            String dayko = null;
            time = position / 8;
            day = position % 8;
            weekday = curDayofMonth + day - 1;
            if (weekday > Cal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                weekday = weekday - Cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                curMonth = curMonth + 1;
            }
            //주별달력에서 해당 월이 아닌 익월이 나올때, 다음달로 수정해주는 코드
            textfile = curYear + "_" + curMonth + "_" + weekday  + ".txt";


            String filepath = "/data/data/org.techtown.ydgfinalproject4/files/" + textfile;
            File files = new File(filepath);
            if (files.exists() == true) {
                readEvent();
                String text = items.get(0);
                String[] eventTitle = text.split(" : ");
                String starttime = items.get(2);
                String[] startTime0 = starttime.split(" : ");
                String[] startTime1 = startTime0[1].split(":");
                int startTime = Integer.parseInt(startTime1[0]);
                String endtime = items.get(4);
                String[] endTime0 = endtime.split(" : ");
                String[] endTime1 = endTime0[1].split(":");
                int endTime = Integer.parseInt(endTime1[0]);
                if(position>=(day+8*startTime)&&position<(day+8*endTime)) {
                    if(eventTitle.length==2) {
                        holder.tvItemGridView.setText(eventTitle[1]);
                    } else
                    {
                        holder.tvItemGridView.setText("NO TITLE");
                    }
                    holder.tvItemGridView.setTextSize(15);
                    holder.tvItemGridView.setTextColor(getResources().getColor(R.color.colorWhite));
                    holder.tvItemGridView.setBackgroundColor(getResources().getColor(R.color.colorSky));

                }
            }
            //weekview 초기화 후 스케줄 존재 시 노란색 표시
            return convertView;
        }
    }

    private class ViewHolder{
        TextView tvItemGridView;
    }
}

