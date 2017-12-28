package com.pain.orderdrinks;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * 自訂類別
 * 項目選取
 */

public class OrderItem implements AdapterView.OnItemClickListener {

    private OrderPage op;
    private String[] sugar = {"正常", "少糖", "半糖", "微糖", "無糖"},
            ice = {"正常", "少冰", "微冰", "去冰"};

    private ArrayAdapter<String> sugarAdapter;
    private ArrayAdapter<String> iceAdapter;
    private Handler handle;
    private LayoutInflater inflater;

    public OrderItem(Context context){
        this.op = (OrderPage) context;
        sugarAdapter = new ArrayAdapter<>(op,android.R.layout.simple_spinner_dropdown_item, sugar);
        iceAdapter = new ArrayAdapter<>(op,android.R.layout.simple_spinner_dropdown_item, ice);
        handle = new Handler();
        inflater = LayoutInflater.from(op);
    }

    //項目點選時的事件處理
    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        view = inflater.inflate(R.layout.adjust_layout, null);
        final Spinner s = (Spinner)view.findViewById(R.id.sugar);
        final Spinner i = (Spinner)view.findViewById(R.id.ice);
        final EditText amount = (EditText)view.findViewById(R.id.amount);
        s.setAdapter(sugarAdapter);
        i.setAdapter(iceAdapter);
        op.hideSoftKeyboard();
        new AlertDialog.Builder(op)
                .setTitle(op.drink.get(position))
                .setView(view)
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        String sugarStr = s.getSelectedItem().toString(),
                                iceStr = i.getSelectedItem().toString();
                        int num = amount.getText().toString().equals("") ? 0 : Integer.parseInt(amount.getText().toString());
                        String str = op.drink.get(position) + "  ->  " + sugarStr + " / " + iceStr + "  ->  " + op.price.get(position);
                        if(num != 0){
                            op.detail.setText("【明細】");
                            setDetail(op, str, num);
                            op.totalMoney += op.price.get(position)*num;
                            op.total.setText("總計：NT$" + String.valueOf(op.totalMoney));
                            op.cash.setVisibility(View.VISIBLE);
                        }

                    }
                })
                .show();
    }

    //建立明細資料
    public void setDetail(Context context, String str, int num){
        TextView text = new TextView(context);
        text.setTextColor(Color.BLUE);
        text.setTextSize(20f);
        text.setText(str + "   x" + num);

        text.setClickable(true);
        text.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                TextView t = (TextView)arg0;
                String[] arr = t.getText().toString().split("  ->  ");
                String[] arr1 = arr[2].split("   x");
                op.totalMoney -= Integer.parseInt(arr1[0])*Integer.parseInt(arr1[1]);
                if(op.totalMoney == 0){
                    op.detail.setText("");
                    op.total.setText("");
                    op.cash.setText("");
                    op.cash.setVisibility(View.GONE);
                }else
                    op.total.setText("總計：NT$" + String.valueOf(op.totalMoney));

                op.details.removeView(t);
                op.hideSoftKeyboard();
            }
        });

        op.details.addView(text);
        op.details.setBackgroundColor(0xe0FFF0F5);
        handle.post(new Runnable() {
            @Override
            public void run() {
                op.scroll.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }
}
