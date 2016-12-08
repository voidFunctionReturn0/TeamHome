package com.example.myapplication;

import android.R;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Switch;

import com.example.myapplication.actionbar.ChattingFragment;
import com.example.myapplication.actionbar.calendar.CalendarFragment;
import com.example.myapplication.actionbar.navi.MyInfoFragment;
import com.example.myapplication.actionbar.navi.teamInfo.TeamInfoFragment;
import com.example.myapplication.actionbar.search.SearchingFragment;
import com.example.myapplication.dto.Member;
import com.example.myapplication.dto.News;
import com.example.myapplication.dto.Schedule;
import com.example.myapplication.home.HomeFragment;
import com.example.myapplication.login.LoginFragment;
import com.example.myapplication.network.Network3;
import com.example.myapplication.network.Network4;
import com.example.myapplication.team.TeamHomeFragment;
import com.example.myapplication.team.news.AddNewsFragment;

import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static Activity mainActivity;
    public static Activity getInstance() { return mainActivity; }
    public static Member member;
    public static int tid;
    public static ActionBarDrawerToggle toggle;
    private MenuItem action_search;
    private MenuItem action_chatting;
    private MenuItem action_teamhome;
    private MenuItem action_calendar;
    private MenuItem action_commit;
    private MenuItem nav_team_Info;

    // 일정추가에 시작날짜로 지정될 날짜
    public static String startDate = "Start Date";


    public static String presentFragment;

    public static Schedule selectedSchedule;

    public static ImageView navProfile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startActivity(new Intent(this, SplashActivity.class));

        mainActivity = this;

        //화면 회전방지
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        member = new Member();
        member.setMid("1");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        navProfile = (ImageView) findViewById(R.id.navProfile);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED );
        toggle.syncState();




        int[][] state = new int[][] {
                new int[] {-android.R.attr.state_enabled}, // disabled
                new int[] {android.R.attr.state_enabled}, // enabled
                new int[] {-android.R.attr.state_checked}, // unchecked
                new int[] { android.R.attr.state_pressed}  // pressed

        };

        int[] color = new int[] {
                Color.WHITE,
                Color.WHITE,
                Color.WHITE,
                Color.WHITE
        };

        ColorStateList csl = new ColorStateList(state, color);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        navigationView.setItemTextColor(csl);
        //navigationView.getMenu().getItem(0).setVisible(false);

        if(navigationView.getMenu().equals("JoinFragment")){
            Log.i("mylog","여기타냐?"+"");
        }



        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, new LoginFragment())
                .commit();

       /* MenuItem menuItem = navigationView.getMenu().findItem(R.id.menu_team_Info);
        SpannableString spanString = new SpannableString(menuItem.getTitle().toString());
        spanString.setSpan(new ForegroundColorSpan(Color.GREEN), 0, spanString.length(), 0); // fix the color to white
        menuItem.setTitle(spanString);*/

        Switch widgetSwitch = new Switch(this);

       // menuItem.setActionView(widgetSwitch);


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(presentFragment == "AddNewsFragment"){
            presentFragment = "TeamHomeFragment";
            this.invalidateOptionsMenu();
            MainActivity.toggle.setDrawerIndicatorEnabled(true);
        }else if(presentFragment == "TeamHomeFragment"){

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        action_search = menu.findItem(R.id.action_search);
        action_chatting = menu.findItem(R.id.action_chatting);
        action_teamhome = menu.findItem(R.id.action_teamhome);
        action_calendar = menu.findItem(R.id.action_calendar);
        action_commit = menu.findItem(R.id.action_commit);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        action_search.setVisible(false);
        action_chatting.setVisible(false);
        action_teamhome.setVisible(false);
        action_calendar.setVisible(false);
        action_commit.setVisible(false);
        getSupportActionBar().setTitle("로그인");

        if(presentFragment!=null) {
            if (presentFragment.equals("HomeFragment")) {
                action_search.setVisible(true);
                action_chatting.setVisible(false);
                action_teamhome.setVisible(false);
                action_calendar.setVisible(false);
                action_commit.setVisible(false);
                getSupportActionBar().setTitle("HomeMain");
                navigationView.getMenu().getItem(0).setVisible(false);
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED );
            }else if(presentFragment.equals("TeamHomeFragment")){
                action_search.setVisible(false);
                action_chatting.setVisible(false);
                action_teamhome.setVisible(true);
                action_calendar.setVisible(true);
                action_commit.setVisible(false);
                getSupportActionBar().setTitle("팀메인");
                navigationView.getMenu().getItem(0).setVisible(true);
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED );
            }else if(presentFragment.equals("JoinFragment")){
                action_search.setVisible(false);
                action_chatting.setVisible(false);
                action_teamhome.setVisible(false);
                action_calendar.setVisible(false);
                action_commit.setVisible(false);
                getSupportActionBar().setTitle("회원가입");
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED );
            }else if(presentFragment.equals("LoginFragment")){
                action_search.setVisible(false);
                action_chatting.setVisible(false);
                action_teamhome.setVisible(false);
                action_calendar.setVisible(false);
                action_commit.setVisible(false);
                getSupportActionBar().setTitle("로그인");
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED );

            }else if(presentFragment.equals("LoginFragment")) {
                action_search.setVisible(false);
                action_chatting.setVisible(false);
                action_teamhome.setVisible(false);
                action_calendar.setVisible(false);
                action_commit.setVisible(false);
                getSupportActionBar().setTitle("로그인");
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED );
            }else if(presentFragment.equals("TeamInfoFragment")) {
                action_search.setVisible(false);
                action_chatting.setVisible(false);
                action_teamhome.setVisible(true);
                action_calendar.setVisible(true);
                action_commit.setVisible(false);
                getSupportActionBar().setTitle("팀정보");
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED );
            }else if(presentFragment.equals("MyInfoFragment")) {
                action_search.setVisible(false);
                action_chatting.setVisible(false);
                action_teamhome.setVisible(false);
                action_calendar.setVisible(false);
                action_commit.setVisible(false);
                getSupportActionBar().setTitle("회원정보수정");
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED );
            }else if(presentFragment.equals("TeamHomeFragment")) {
                action_search.setVisible(false);
                action_chatting.setVisible(false);
                action_teamhome.setVisible(true);
                action_calendar.setVisible(true);
                action_commit.setVisible(false);
                getSupportActionBar().setTitle("");
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED );
            }else if(presentFragment.equals("CalendarFragment")) {
                action_search.setVisible(false);
                action_chatting.setVisible(false);
                action_teamhome.setVisible(true);
                action_calendar.setVisible(false);
                action_commit.setVisible(false);
                getSupportActionBar().setTitle("일정");
                navigationView.getMenu().getItem(0).setVisible(true);
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED );
            }else if(presentFragment.equals("AddNewsFragment")){
                action_search.setVisible(false);
                action_chatting.setVisible(false);
                action_teamhome.setVisible(false);
                action_calendar.setVisible(false);
                action_commit.setVisible(true);
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED );
                getSupportActionBar().setTitle("AddNews");
            }else if(presentFragment.equals("SearchingFragment")){
                action_search.setVisible(true);
                action_chatting.setVisible(false);
                action_teamhome.setVisible(false);
                action_calendar.setVisible(false);
                action_commit.setVisible(false);
                navigationView.getMenu().getItem(0).setVisible(false);
                getSupportActionBar().setTitle("검색");
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_teamhome) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new TeamHomeFragment())
                    .commit();
            MainActivity.presentFragment = "TeamHomeFragment";
            invalidateOptionsMenu();
            return true;
        }
        else if(id == R.id.action_calendar){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new CalendarFragment())
                    .commit();
            MainActivity.presentFragment = "CalendarFragment";
            invalidateOptionsMenu();
            Log.i("myinfo",MainActivity.tid +"");
            return true;

        }
        else if(id == R.id.action_chatting){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new ChattingFragment())
                    .commit();
            getSupportActionBar().setTitle("채팅");
            return true;

        }
       /* else if(id == R.id.action_inbox){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new InboxFragment())
                    .commit();
            getSupportActionBar().setTitle("알람");
            return true;
        }*/
        else if(id == R.id.action_search) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new SearchingFragment())
                    .addToBackStack(null)
                    .commit();
            MainActivity.presentFragment = "SearchingFragment";
            invalidateOptionsMenu();
            getSupportActionBar().setTitle("검색");
            return true;
        }
        else if(id == R.id.action_commit){
            if(presentFragment.equals("AddNewsFragment")) {
                //공백제거 후 내용이 없으면서 파일이나 사진이 첨부되지 않았다면 내용을 입력하라는 다이얼로그 띄우기
                //파일이나 사진이 첨부되면 addAttachImageView의 Tag를 photo 혹은 file로 변경하도록 처리해 놨음
                if(AddNewsFragment.addNewsMultiTextView.getText().toString().trim().length() < 1
                        && AddNewsFragment.txtAttachPhotoName.getTag(AddNewsFragment.txtAttachPhotoName.getId()).equals("none")
                        && AddNewsFragment.txtAttachFileName.getTag(AddNewsFragment.txtAttachFileName.getId()).equals("none")
                        ){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    // Setting Dialog Title
                    builder.setTitle("News Dialog");
                    // Setting Dialog Message
                    builder.setMessage("내용을 입력해 주세요 ");
                    // Setting Icon to Dialog
                    //alertDialog.setIcon(R.drawable.tick);
                    // Setting OK Button
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();

                } else {
                    //서버로 입력받은 값 전달
                    long now = System.currentTimeMillis();
                    Date date = new Date(now);


                    News news = new News();
                    news.setMid(MainActivity.member.getMid());
                    news.setMprofile(MainActivity.member.getMprofile());
                    news.setNcontent(AddNewsFragment.addNewsMultiTextView.getText().toString());
                    news.setTid(MainActivity.tid);
                    news.setNdate(date);
                    news.setNphotoname(AddNewsFragment.photoName);
                    news.setNdataname(AddNewsFragment.fileName);
                    news.setNisnotice(0);

                    if(AddNewsFragment.bitmap != null) {
                        AddNewsFragment.bitmap = AddNewsFragment.getResizedBitmap(AddNewsFragment.bitmap, 900);
                    }
                    Network3.addNews(news,AddNewsFragment.bitmap,AddNewsFragment.file, this);


                }
            }
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_team_Info) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new TeamInfoFragment())
                    .commit();
            MainActivity.presentFragment = "TeamInfoFragment";
            invalidateOptionsMenu();

        } else if (id == R.id.nav_logout) {

            AlertDialog.Builder ad = new AlertDialog.Builder(this);
            ad.setTitle("로그아웃 하시겠습니까?").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    member = null;
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, new LoginFragment())
                            .commit();
                    MainActivity.presentFragment = "LoginFragment";
                    invalidateOptionsMenu();
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            }).create().show();



/*            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new LogoutFragment())
                    .commit();*/

        } else if (id == R.id.nav_member_Info) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new MyInfoFragment())
                    .addToBackStack(null)
                    .commit();
            MainActivity.presentFragment = "MyInfoFragment";
            invalidateOptionsMenu();
        } else if (id == R.id.nav_home) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new HomeFragment())
                    .commit();
            MainActivity.presentFragment = "HomeFragment";
            invalidateOptionsMenu();
        }else if(id == R.id.nav_out){
            AlertDialog.Builder ad = new AlertDialog.Builder(this);
            ad.setTitle("회원 탈퇴 하시겠습니까?").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Network4.out(member);
                    member = null;
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, new LoginFragment())
                            .commit();
                    MainActivity.presentFragment = "LoginFragment";
                    invalidateOptionsMenu();
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            }).create().show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
        //김민수 강영준
    }
}
