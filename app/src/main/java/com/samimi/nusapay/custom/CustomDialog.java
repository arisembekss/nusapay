package com.samimi.nusapay.custom;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.samimi.nusapay.AddSaldoActivity;
import com.samimi.nusapay.R;

/**
 * Created by lenovo on 17/03/2017.
 */

public class CustomDialog {

    public void makeDialog(final Context context, String title, String message, final String tag) {
        final Dialog dialogStatus = new Dialog(context);
        dialogStatus.setTitle(title);
        dialogStatus.setContentView(R.layout.custom_dialog_keterangan);
        TextView tv = (TextView) dialogStatus.findViewById(R.id.msgDialogKet);
        tv.setText(message);
        Button btnadd = (Button) dialogStatus.findViewById(R.id.addBtn);
        if (tag.matches("saldo")) {
            btnadd.setText("Tambah Saldo");
        } else {
            btnadd.setText("Close");
        }
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //context.startActivity(new Intent(context, AddSaldoActivity.class));
                if (tag.matches("saldo")) {
                    context.startActivity(new Intent(context, AddSaldoActivity.class));
                    dialogStatus.dismiss();
                } else {
                    dialogStatus.dismiss();
                }
            }
        });
        dialogStatus.show();
    }

    /*protected CustomDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }*/
}
