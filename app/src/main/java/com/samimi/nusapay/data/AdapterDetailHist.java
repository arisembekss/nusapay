package com.samimi.nusapay.data;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.samimi.nusapay.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by aris on 31/12/16.
 */

public class AdapterDetailHist extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private Context context;
    private LayoutInflater inflater;
    List<DataPul> data = Collections.emptyList();
    DataPaket current;
    int currentPos = 0;

    public AdapterDetailHist(Context context, List<DataPul> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        /*if (this.data != null) {
            this.data.clear();
            //this.data.addAll(data);
            this.data = data;
        }
        else {
            this.data = data;
        }
        notifyDataSetChanged();*/
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_item_detailhist, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MyHolder myHolder = (MyHolder) holder;
        DataPul current = data.get(position);
        myHolder.dateDetail.setText(current.dateP);
        myHolder.nomorDetail.setText(current.nomorP);
        myHolder.hargaDetail.setText(current.hargaP);
        myHolder.kodeDetail.setText(current.kodeP);
        /*myHolder.keterangan.setText(current.keterangan);
        myHolder.kode.setText(current.kode);
        myHolder.harga.setText(current.harga);*/
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void swap(List<DataPul> list){
        if (data != null) {
            data.clear();
            data.addAll(list);
        }
        else {
            data = list;
        }
        notifyDataSetChanged();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        TextView dateDetail, nomorDetail, hargaDetail, kodeDetail;
        public MyHolder(View itemView) {
            super(itemView);

            dateDetail = (TextView) itemView.findViewById(R.id.dateDetail);
            nomorDetail = (TextView) itemView.findViewById(R.id.nomorDetail);
            hargaDetail = (TextView) itemView.findViewById(R.id.hargaDetail);
            kodeDetail = (TextView) itemView.findViewById(R.id.kodeDetail);
        }

    }
}
