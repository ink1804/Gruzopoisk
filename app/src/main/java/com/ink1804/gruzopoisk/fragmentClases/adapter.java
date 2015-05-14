package com.ink1804.gruzopoisk.fragmentClases;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ink1804.gruzopoisk.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Ink1804 on 30.03.15.
 */
public class adapter extends BaseAdapter {
    Context context;
    ArrayList<struct> dataList;
    JSONAnalysis JAnalysClass = new JSONAnalysis();

    public adapter(Context context, ArrayList<struct> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public adapter() {
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View root;
        listF listClass = new listF();
        int  a=this.dataList.size();
    int t1=((a-2)/30)*30 //a=31
            ,t2=t1+30;
        if(!listClass.dataInArray){
            JAnalysClass.All_Data_To_Arraylist(t1, t2, listClass.A, listClass.JsonBase);
            listClass.dataInArray=true;}

        root = LayoutInflater.from(context).inflate(R.layout.listfragment_row, null);
        try {
            if(position!=a-1) {

                TextView cityTv = (TextView) root.findViewById(R.id.cityTVf);
                TextView matherialTv = (TextView) root.findViewById(R.id.matherialTVf);
                TextView priceTv = (TextView) root.findViewById(R.id.priceTVf);
                TextView pre_price = (TextView)root.findViewById(R.id.pre_price);

                cityTv.setText(listClass.A[0][position] + " → " + listClass.A[1][position]);
                matherialTv.setText(listClass.A[2][position]);
                priceTv.setText(listClass.A[7][position] +",");// + listClass.A[4][position]);
                pre_price.setText(listClass.A[10][position]+"\n");
            }else{
                root = LayoutInflater.from(context).inflate(R.layout.getdown, null);
            }
        }catch (Exception e ){}
        return root;
    }
}

