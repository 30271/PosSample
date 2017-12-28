package com.pain.orderdrinks;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

public class OrderPage extends Activity {

    //畫面元件變數
    private Button add, clear;
    //private ListView list;
    LinearLayout details;
    ScrollView scroll;
    TextView total, detail, money;
    EditText cash;

    //ArrayList 存放項目名稱及價錢
    ArrayList<String> drink;
    ArrayList<Integer> price;

    //客製化 Adapter
    static MyAdapter adapter;

    //項目總金額
    int totalMoney = 0;

    private FragmentManager manager;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_page);

        //取得畫面元件
        //list = (ListView)findViewById(R.id.list);
        details = (LinearLayout)findViewById(R.id.details);
        scroll = (ScrollView)findViewById(R.id.scroll);
        total = (TextView)findViewById(R.id.total);
        detail = (TextView)findViewById(R.id.detail);
        money = (TextView)findViewById(R.id.money);
        cash = (EditText)findViewById(R.id.cash);
        clear = (Button)findViewById(R.id.clear);
        add = (Button)findViewById(R.id.add);

        drink = new ArrayList<>();
        price = new ArrayList<>();
        //取得資料庫資料
        if(getData(this)){
            createData(this);
            getData(this);
        }

        //建立 Adapter 實體
        adapter = new MyAdapter(this, drink, price);
        //將布置的 Adapter 配接給 ListView 元件
        //list.setAdapter(adapter);
        //元件設定監聽
        //list.setOnItemClickListener(new OrderItem(this));

        manager = getFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        TypeFragment fragment = new TypeFragment();
        ft.add(R.id.contain, fragment);
        currentFragment = fragment;
        ft.commit();

        add.setOnClickListener(new InsertItem(this));
        clear.setOnClickListener(listen1);
        cash.addTextChangedListener(watcher);
        details.addOnLayoutChangeListener(changeListener);

        Intent intent = getIntent();
        boolean result = intent.getBooleanExtra("result", false);
        if(result){
            //list.setOnItemLongClickListener(new DeleteItem(this));
        }else{
            add.setVisibility(View.GONE);
        }
    }

    //明細版面變動時的事件處理
    View.OnLayoutChangeListener changeListener = new View.OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            int cashs = cash.getText().toString().equals("") ? 0 : Integer.parseInt(cash.getText().toString());
            int moneys = cashs - totalMoney;
            if (cashs == 0) {
                money.setText("");
            } else {
                if (moneys < 0)
                    money.setText("現金不足");
                else
                    money.setText("找零：" + String.valueOf(moneys));
            }
        }
    };

    //現金輸入時的事件處理
    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int cashs = cash.getText().toString().equals("") ? 0 : Integer.parseInt(cash.getText().toString());
            int moneys = cashs - totalMoney;
            if (cashs == 0) {
                money.setText("");
            } else {
                if (moneys < 0)
                    money.setText("現金不足");
                else
                    money.setText("找零：" + String.valueOf(moneys));
            }
        }
        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    //清除按鈕的事件處理
    View.OnClickListener listen1 = new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {

                details.removeAllViews();
                totalMoney = 0;
                total.setText("");
                detail.setText("");
                cash.setText("");
                cash.setVisibility(View.GONE);
                hideSoftKeyboard();

        }
    };

    //隱藏軟鍵盤並移除焦點
    public void hideSoftKeyboard(){
        View current = getCurrentFocus();
        if (current != null) {
            current.clearFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(current.getWindowToken(), 0);
        }
    }

    //取得資料庫資料放入 ArrayList
    public boolean getData(Context context){
        drink.clear();
        price.clear();
        boolean result = false;
        MyDatabase db = new MyDatabase(context);
        SQLiteDatabase sdb = db.getReadableDatabase();
        Cursor c = sdb.rawQuery("SELECT * FROM drink ORDER BY cost ASC", null);

        if(c.getCount() == 0) {
            result = true;
        }

        while(c.moveToNext()){
            drink.add(c.getString(0));
            price.add(c.getInt(1));
        }
        c.close();
        sdb.close();
        return result;
    }

    //初始項目資料
    public void createData(Context context){
        SQLiteDatabase sdb = new MyDatabase(context).getWritableDatabase();
        sdb.execSQL("INSERT INTO drink VALUES('復刻咖啡奶', 70, '厚奶茶')");
        sdb.execSQL("INSERT INTO drink VALUES('龍涎冷烏鮮奶', 65, '鮮奶茶')");
        sdb.execSQL("INSERT INTO drink VALUES('阿薩姆鮮奶茶', 65, '鮮奶茶')");
        sdb.execSQL("INSERT INTO drink VALUES('低脂巧克力奶', 60, '厚奶茶')");
        sdb.execSQL("INSERT INTO drink VALUES('茉香鮮奶綠', 55, '鮮奶茶')");
        sdb.execSQL("INSERT INTO drink VALUES('碳焙烏龍奶', 55, '厚奶茶')");
        sdb.execSQL("INSERT INTO drink VALUES('格雷奶茶', 55, '厚奶茶')");
        sdb.close();
    }

    public void getTypeList(Context context, String str){
        drink.clear();
        price.clear();
        SQLiteDatabase sdb = new MyDatabase(context).getReadableDatabase();
        Cursor c = sdb.rawQuery("SELECT * FROM drink WHERE type IS '" + str + "'", null);
        while(c.moveToNext()){
            drink.add(c.getString(0));
            price.add(c.getInt(1));
        }
        c.close();
        sdb.close();
    }

    public void type1(View view){
        getTypeList(getApplicationContext(), "厚奶茶");
        adapter.notifyDataSetChanged();
        FragmentTransaction ft = manager.beginTransaction();
        TypeFragment fragment = new TypeFragment();
        ft.remove(currentFragment);
        ft.add(R.id.contain, fragment);
        currentFragment = fragment;
        ft.commit();
    }

    public void type2(View view){
        getTypeList(getApplicationContext(), "鮮奶茶");
        adapter.notifyDataSetChanged();
        FragmentTransaction ft = manager.beginTransaction();
        TypeFragment fragment = new TypeFragment();
        ft.remove(currentFragment);
        ft.add(R.id.contain, fragment);
        currentFragment = fragment;
        ft.commit();
    }

    public void all(View view){
        getData(getApplicationContext());
        adapter.notifyDataSetChanged();
        FragmentTransaction ft = manager.beginTransaction();
        TypeFragment fragment = new TypeFragment();
        ft.remove(currentFragment);
        ft.add(R.id.contain, fragment);
        currentFragment = fragment;
        ft.commit();
    }

}
