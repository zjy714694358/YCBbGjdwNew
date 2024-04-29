package com.yc.ycbbgjdwnew.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.yc.ycbbgjdwnew.R;
import com.yc.ycbbgjdwnew.config.Config;
import com.yc.ycbbgjdwnew.utils.StringUtils;

import java.util.Locale;

public class XiafaShiyanJiChuInfoEndActivity extends AppCompatActivity {

    private TextView tvSyyqlx;
    private TextView tvYqcj;
    private TextView tvYqxh;
    private TextView tvYqccbh;
    private TextView tvGfbbh;
    private TextView tvWendu;
    private TextView tvShidu;
    private TextView tvJingdu;
    private TextView tvWeidu;
    private TextView tvHaiba;
    private TextView tvBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏

        Resources resources = this.getResources();// 获得res资源对象
        Configuration config = resources.getConfiguration();// 获得设置对象
        DisplayMetrics dm = resources.getDisplayMetrics();// 获得屏幕参数：主要是分辨率，像素等。
        if("zh".equals(Config.zyType)){
            config.locale = Locale.SIMPLIFIED_CHINESE;
        }else{
            config.locale = Locale.US;
        }
        resources.updateConfiguration(config, dm);
        setContentView(R.layout.activity_xiafa_shiyan_ji_chu_info_end);
        initView();
    }
    public void initView(){
        tvSyyqlx = findViewById(R.id.tvBbXiafaEndSyyqlx);
        tvYqcj = findViewById(R.id.tvBbXiafaEndYqcj);
        tvYqxh = findViewById(R.id.tvBbXiafaEndYqxh);
        tvYqccbh = findViewById(R.id.tvBbXiafaEndYqccbh);
        tvGfbbh = findViewById(R.id.tvBbXiafaEndGfbbh);
        tvWendu = findViewById(R.id.tvBbXiafaEndWendu);
        tvShidu = findViewById(R.id.tvBbXiafaEndShidu);
        tvJingdu = findViewById(R.id.tvBbXiafaEndJingdu);
        tvWeidu = findViewById(R.id.tvBbXiafaEndWeidu);
        tvHaiba = findViewById(R.id.tvBbXiafaEndHaiba);
        tvBack = findViewById(R.id.tvBbXiafaEndFanhui);
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //startActivity(new Intent(XiafaShiyanJiChuInfoEndActivity.this,GetDataAndUpLoadActivity.class));
            }
        });
        Intent intent = getIntent();
        String syyqlx = intent.getStringExtra("yqlx");
        String yqcj = intent.getStringExtra("yqcj");
        String yqxh = intent.getStringExtra("yqxh");
        String yqccbh = intent.getStringExtra("yqccbh");
        String gfbbh = intent.getStringExtra("gfbbh");
        String wendu = intent.getStringExtra("wendu");
        String shidu = intent.getStringExtra("shidu");
        String jingdu = intent.getStringExtra("jingdu");
        String weidu = intent.getStringExtra("weidu");
        String haiba = intent.getStringExtra("haiba");

        if(StringUtils.noEmpty(syyqlx)){
            tvSyyqlx.setText(syyqlx);
        }
        if(StringUtils.noEmpty(yqcj)){
            tvYqcj.setText(yqcj);
        }
        if(StringUtils.noEmpty(yqxh)){
            tvYqxh.setText(yqxh);
        }
        if(StringUtils.noEmpty(yqccbh)){
            tvYqccbh.setText(yqccbh);
        }
        if(StringUtils.noEmpty(gfbbh)){
            tvGfbbh.setText(gfbbh);
        }
        if(StringUtils.noEmpty(wendu)){
            tvWendu.setText(wendu+"℃");
        }
        if(StringUtils.noEmpty(shidu)){
            tvShidu.setText(shidu+"%");
        }
        if(StringUtils.noEmpty(jingdu)){
            tvJingdu.setText(jingdu);
        }
        if(StringUtils.noEmpty(weidu)){
            tvWeidu.setText(weidu);
        }
        if(StringUtils.noEmpty(haiba)){
            tvHaiba.setText(haiba+"m");
        }
    }
}