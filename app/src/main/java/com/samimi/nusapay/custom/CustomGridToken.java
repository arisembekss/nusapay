package com.samimi.nusapay.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.samimi.nusapay.R;

/**
 * Created by aris on 25/12/16.
 */

public class CustomGridToken extends BaseAdapter {

    private Context context;
    private final String[] textToken;
    private final String[] kodeToken;

    public CustomGridToken(String[] textToken, String[] kodeToken, Context context) {
        this.textToken = textToken;
        this.kodeToken = kodeToken;
        this.context = context;
    }

    @Override
    public int getCount() {
        return textToken.length;
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
            //grid = new View(context);
            grid = inflater.inflate(R.layout.custom_item_token, null);
            TextView btnHarga = (TextView) grid.findViewById(R.id.btnItemToken);
            TextView txtKodetoken = (TextView) grid.findViewById(R.id.txtkodeToken);

            btnHarga.setText(textToken[i]);
            txtKodetoken.setText(kodeToken[i]);

        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}
