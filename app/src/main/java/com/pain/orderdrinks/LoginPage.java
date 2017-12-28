package com.pain.orderdrinks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginPage extends Activity {

    private Button user, manager;
    private AlertDialog.Builder dialog;
    private String[] account = {"Hello", "123"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        user = (Button)findViewById(R.id.user);
        manager = (Button)findViewById(R.id.manager);
        dialog = new AlertDialog.Builder(this);

        user.setOnClickListener(listen);
        manager.setOnClickListener(listen);
    }

    View.OnClickListener listen = new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {
            //user模式 沒有新增及刪除功能
            if(arg0 == user){
                Intent intent = new Intent(LoginPage.this, OrderPage.class);
                intent.putExtra("result", false);
                startActivity(intent);
            }

            //manager模式
            if(arg0 == manager){
                View view = LayoutInflater.from(getApplication()).inflate(R.layout.login, null);
                final EditText n = (EditText)view.findViewById(R.id.edt1);
                final EditText m = (EditText)view.findViewById(R.id.edt2);
                n.setTextColor(Color.BLACK);
                m.setTextColor(Color.BLACK);
                dialog.setTitle("登入");
                dialog.setView(view);
                dialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        String name = n.getText().toString();
                        String pwd = m.getText().toString();
                        if(name.equals(account[0]) && pwd.equals(account[1])){
                            Intent intent = new Intent(LoginPage.this, OrderPage.class);
                            intent.putExtra("result", true);
                            startActivity(intent);
                        }else{
                            Toast.makeText(getApplication(), "帳號或密碼錯誤", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                dialog.show();
            }
        }
    };
}
