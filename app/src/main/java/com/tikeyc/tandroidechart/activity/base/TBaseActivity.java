package com.tikeyc.tandroidechart.activity.base;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.tikeyc.tandroidechart.R;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by public1 on 2017/5/25.
 */

public class TBaseActivity extends Activity {

    public boolean isLandScape;

    @ViewInject(R.id.navigationbar)
    public LinearLayout navigationBar;
    @ViewInject(R.id.navigationBar_title_tv)
    public TextView navigationBar_title_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!isLandScape && this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

    }

    private void initNavigationBar() {
        x.view().inject(this);

    }
}
