package com.samimi.nusapay.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.samimi.nusapay.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 30/11/2016.
 */

public class FrMultipleNumber extends Fragment implements View.OnClickListener{

    View view;
    TextView txt7;
    LinearLayout linearLayout;
    Button btnAddNumber;

    int clickcount;
    List<String> arrayNomor = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fr_multiple_number, container, false);

        initUI();
        
        txt7 = (TextView) view.findViewById(R.id.textView7);
        txt7.setText("Boo");
        return view;


    }

    private void initUI() {
        linearLayout = (LinearLayout) view.findViewById(R.id.layoutAddNumber);
        btnAddNumber = (Button) view.findViewById(R.id.btnaddNumber);
        btnAddNumber.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnaddNumber:
                clickcount=clickcount+1;
                addEditText(clickcount);

                break;
        }
    }

    private void addEditText(int count) {

        //btnAddNumber.setClickable(false);
        btnAddNumber.setEnabled(false);
        //btnAddNumber.setBackgroundColor(Color.GRAY);
        //editText.setLayoutParams(new ViewGroup.LayoutParams(2));

        EditText editText = new EditText(getActivity());
        editText.setHint("Add Number "+ Integer.toString(count));
        editText.setId(100+count);
        editText.requestFocus();
        String nomor = editText.getText().toString();
        if (nomor.equals("")) {
            Toast.makeText(getActivity(), "Wait number", Toast.LENGTH_SHORT);
        } else {
            arrayNomor.add(nomor);
            Toast.makeText(getActivity(), arrayNomor.toString(),Toast.LENGTH_SHORT).show();
        }


        //editText.setText(Integer.toString(editText.getId()));
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 10) {
                    //btnAddNumber.setClickable(true);


                    btnAddNumber.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        linearLayout.addView(editText);
        checkString(linearLayout);


    }

    public void checkString(ViewGroup parent) {
        for(int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (child instanceof EditText) {
                //Support for RadioGroups
                EditText et = (EditText)child;

            }

        }
    }
}
