package com.example.liling.customercircleprogress;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener {
    private CustomerCircleProgress mCustomerCircleProgress;
    private EditText mProgressEditText;
    private EditText mDurationEditText;
    private Button mConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCustomerCircleProgress = (CustomerCircleProgress) findViewById(R.id.mCustomerCircleProgress);
        mProgressEditText = (EditText) findViewById(R.id.mProgressEditText);
        mDurationEditText = (EditText) findViewById(R.id.mDurationEditText);
        mConfirm = (Button) findViewById(R.id.mConfirm);

        mConfirm.setOnClickListener(this);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.mConfirm) {
            int progress =  Integer.valueOf(mProgressEditText.getText().toString());
            int duration = Integer.valueOf(mDurationEditText.getText().toString());
            mCustomerCircleProgress.setProgressWithAnimation(progress, duration);
        }
    }
}
