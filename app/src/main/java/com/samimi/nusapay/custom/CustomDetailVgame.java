package com.samimi.nusapay.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.samimi.nusapay.R;

/**
 * Created by aris on 28/12/16.
 */

public class CustomDetailVgame extends BaseAdapter {

    private Context context;
    private final String[] kodevGame;
    private final String[] hargavGame;

    public CustomDetailVgame(Context context, String[] kodevGame, String[] hargavGame) {
        this.context = context;
        this.kodevGame = kodevGame;
        this.hargavGame = hargavGame;
    }

    @Override
    public int getCount() {
        return kodevGame.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            grid = new View(context);
            grid = inflater.inflate(R.layout.custom_item_detail_vgame, null);
            TextView txtkodevgame = (TextView) grid.findViewById(R.id.itemdetailVgame);
            TextView txthargavgame = (TextView) grid.findViewById(R.id.itemhargaVgame);

            txtkodevgame.setText(kodevGame[i]);
            txthargavgame.setText(hargavGame[i]);

        } else {
            grid = (View) convertView;
        }
        return grid;
    }
}
