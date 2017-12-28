package com.pain.orderdrinks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * 客製化 Adapter
 */

public class MyAdapter extends BaseAdapter {

    private LayoutInflater infla;
    private ArrayList<String> drink;
    private ArrayList<Integer> price;

    public MyAdapter(Context context, ArrayList<String> drink, ArrayList<Integer> price){
        infla = LayoutInflater.from(context);
        this.drink = drink;
        this.price = price;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return drink.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub

        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        // TODO Auto-generated method stub
        ViewTag tag;
        if(arg1 == null){
            arg1 = infla.inflate(R.layout.custom_layout, null);
            tag = new ViewTag();
            tag.item = (TextView)arg1.findViewById(R.id.name);
            tag.cost = (TextView)arg1.findViewById(R.id.price);
            arg1.setTag(tag);
        }else{
            tag = (ViewTag)arg1.getTag();
        }

        tag.item.setText(drink.get(arg0));
        tag.cost.setText(String.valueOf(price.get(arg0)));

        return arg1;
    }

}

class ViewTag{
    TextView item, cost;
}
