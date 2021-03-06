package com.example.myapplication.login;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.join.JoinFragment;
import com.example.myapplication.network.MemberNetwork;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private Button btnLogin;
    private Button btnJoin;
    private EditText txtPwd;
    private EditText txtId;
    int loginFragmentId;
    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MainActivity.toggle.setDrawerIndicatorEnabled(false);


        loginFragmentId = this.getId();
        final InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        btnLogin = (Button)view.findViewById(R.id.btnLogin);
        btnJoin = (Button)view.findViewById(R.id.btnJoin);
        txtId =(EditText)view.findViewById(R.id.txtId);
        txtPwd =(EditText)view.findViewById(R.id.txtPwd);

        /*txtId.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    txtPwd.requestFocus();
                }
                return true;
            }
        });

        txtPwd.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                }
                return true;
            }
        });*/


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 로그인 성공시
                Fragment fragment =  getActivity().getSupportFragmentManager().findFragmentById(loginFragmentId);
                Context context = getActivity();
                //FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                MemberNetwork.login(txtId.getText().toString(), txtPwd.getText().toString(), fragment);
               /* if(MainActivity.member == null){
                    Log.i("mylog","fail");
                }else{
                    Log.i("mylog","success");
                }*/





            }
        });

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, new JoinFragment())
                        .commit();
                MainActivity.presentFragment = "JoinFragment";
                getActivity().invalidateOptionsMenu();
            }
        });

        return view;
    }
}
