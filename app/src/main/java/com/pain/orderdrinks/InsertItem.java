package com.pain.orderdrinks;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * 實作自訂類別事件用於新增項目
 */

public class InsertItem implements View.OnClickListener {

    private OrderPage op;
    private LinearLayout view;
    private EditText edt1, edt2;

    public InsertItem(OrderPage op){
        this.op = op;

    }

    //新增按鈕的事件處理
    @Override
    public void onClick(View v) {
        view = new LinearLayout(op);
        edt1 = new EditText(op);
        edt2 = new EditText(op);
        view.setOrientation(LinearLayout.VERTICAL);
        edt1.setHint("輸入飲品名稱");
        edt2.setHint("輸入飲品價格");
        edt2.setInputType(InputType.TYPE_CLASS_NUMBER);
        edt1.setSingleLine();
        edt2.setSingleLine();
        edt1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String d = edt1.getText().toString();
                int count = checkData(op, d);
                if(count != 0)
                    Toast.makeText(op, "飲料名稱重複囉!", Toast.LENGTH_SHORT).show();
            }
        });
        view.addView(edt1);
        view.addView(edt2);
        new AlertDialog.Builder(op)
                .setTitle("新增")
                .setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                String str;
                int num, c;
                if(edt1.getText().toString().equals("") || edt2.getText().toString().equals("")){
                    Toast.makeText(op, "資料不完整，無法新增", Toast.LENGTH_SHORT).show();
                }
                else{
                    str = edt1.getText().toString();
                    num = Integer.parseInt(edt2.getText().toString());
                    c = checkData(op, str);
                    if(c != 0) {
                        Toast.makeText(op, "飲料名稱重複囉!", Toast.LENGTH_SHORT).show();
                    }else{
                        addData(op, str, num);
                        op.getData(op);
                        op.adapter.notifyDataSetChanged();
                    }

                }
            }
            })
                .show();
    }
    //檢查項目是否存在
    public int checkData(Context context, String d){
        SQLiteDatabase sdb = new MyDatabase(context).getReadableDatabase();
        Cursor c = sdb.rawQuery("SELECT name FROM drink WHERE name='" + d + "'", null);
        int count = c.getCount();
        c.close();
        sdb.close();
        return count;
    }
    //新增項目
    public void addData(Context context, String d, int p){
        SQLiteDatabase sdb = new MyDatabase(context).getWritableDatabase();
        sdb.execSQL("INSERT INTO drink VALUES('" + d + "', " + p + ")");
        sdb.close();
    }
}
