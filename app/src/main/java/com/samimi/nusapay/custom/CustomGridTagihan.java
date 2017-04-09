package com.samimi.nusapay.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.samimi.nusapay.R;

/**
 * Created by aris on 14/12/16.
 */

public class CustomGridTagihan extends BaseAdapter {

    private Context context;
    private final String[] textTagihan;
    private final int[] imageId;

    public CustomGridTagihan(Context context, String[] textTagihan, int[] imageId) {
        this.context = context;
        this.textTagihan = textTagihan;
        this.imageId = imageId;
    }

    @Override
    public int getCount() {
        return textTagihan.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            grid = new View(context);
            grid = inflater.inflate(R.layout.custom_grid_tagihan, null);
            TextView textView = (TextView) grid.findViewById(R.id.gridview_text);
            ImageView imageView = (ImageView)grid.findViewById(R.id.gridview_image);
            textView.setText(textTagihan[position]);
            imageView.setImageResource(imageId[position]);
        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}
