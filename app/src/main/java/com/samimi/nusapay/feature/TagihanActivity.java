package com.samimi.nusapay.feature;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;

import com.samimi.nusapay.R;

public class TagihanActivity extends AppCompatActivity {

    GridView gridView;

    public static String[] gridViewStrings = {
            "PLN",
            "PDAM",
            "Telkom",
            "Orange TV",
            "Indovision",
            "Aora TV",
            "FIF",
            "Astra Credit\nCompany",
            "WOM Finane"

    };
    public static int[] gridViewImages = {
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher
    };

    AlertDialog dialogTagihan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tagihan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*gridView = (GridView) findViewById(R.id.grid);
        gridView.setAdapter(new CustomGridTagihan(this, gridViewStrings, gridViewImages));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv=(TextView)view.findViewById(R.id.gridview_text);
                final String data = tv.getText().toString();
                Toast.makeText(TagihanActivity.this, data, Toast.LENGTH_SHORT).show();
                dialogTagihan = new AlertDialog.Builder(TagihanActivity.this).create();

                LayoutInflater layoutInflater = LayoutInflater.from(TagihanActivity.this);
                View promptView = layoutInflater.inflate(R.layout.custom_dialog, null);
                TextView title = (TextView) promptView.findViewById(R.id.titleDialogTagihan);
                title.setText("Tagihan "+data);
                TextView msg = (TextView) promptView.findViewById(R.id.msgDialogTagihan);
                msg.setText("Bayar langsung atau cek terlebih dahulu tagihan "+data+" anda");
                Button cek = (Button) promptView.findViewById(R.id.cekBtn);
                Button bayar = (Button) promptView.findViewById(R.id.bayarBtn);

                cek.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intentCek = new Intent(TagihanActivity.this, CekTagihan.class);
                        intentCek.putExtra("jenis", data);
                        startActivity(intentCek);
                        dialogTagihan.dismiss();
                    }
                });

                bayar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialogTagihan.dismiss();
                    }
                });
                dialogTagihan.setView(promptView);
                dialogTagihan.show();
            }
        });*/

    }

    @Override
    protected void onPause() {
        super.onPause();
        //dialogTagihan.dismiss();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
