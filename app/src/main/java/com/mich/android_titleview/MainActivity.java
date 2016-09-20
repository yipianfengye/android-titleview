package com.mich.android_titleview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mich.titleview.TitleView;

public class MainActivity extends AppCompatActivity {

    private TitleView titleView = null;
    private Button btnAdd = null;
    private Button btnDel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    /**
     * 初始化组件
     */
    public void initView() {
        titleView = (TitleView) findViewById(R.id.title_view);
        btnAdd = (Button) findViewById(R.id.btn_add);
        btnDel = (Button) findViewById(R.id.btn_del);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleView.setTitleContent(titleView.getTitleContent() + "人鱼线,狗公腰,刚过队长太帅了");
            }
        });

        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titleView.getTitleContent().length() > 15) {
                    titleView.setTitleContent(titleView.getTitleContent().substring(0, titleView.getTitleContent().length() - 15));
                }
            }
        });
    }
}
