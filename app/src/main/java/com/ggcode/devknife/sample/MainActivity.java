package com.ggcode.devknife.sample;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import com.ggcode.devknife.DevKnife;

/**
 * @author: zbb 33775
 * @date: 2019/4/18 15:43
 * @desc:
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.ll_root).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DevKnife.showFloatIcon();
            }
        });
    }
}
