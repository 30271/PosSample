package com.pain.orderdrinks;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.AdapterView;

/**
 * 自訂類別
 * 刪除項目
 */

public class DeleteItem implements AdapterView.OnItemLongClickListener {

    private OrderPage op;

    public DeleteItem(OrderPage op){
        this.op = op;
    }

    //長按項目時的事件處理
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        new AlertDialog.Builder(op)
                .setTitle("刪除")
                .setView(op.adapter.getView(position, null, null))
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delData(op, op.drink.get(position));
                        op.getData(op);
                        op.adapter.notifyDataSetChanged();
                    }
                })
                .show();
        return true;
    }

    //刪除項目
    public void delData(Context context, String d){
        SQLiteDatabase sdb = new MyDatabase(context).getWritableDatabase();
        sdb.execSQL("DELETE FROM drink WHERE name='" + d + "'");
        sdb.close();
    }
}
