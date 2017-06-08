package com.tikeyc.tandroidechartlibrary;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.github.abel533.echarts.json.GsonOption;

/**
 * Created by public1 on 2017/6/8.
 */

public class TEChartWebView extends WebView {

    public TEChartWebView(Context context) {
        this(context,null);
    }

    public TEChartWebView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TEChartWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        //
        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportZoom(true);
        webSettings.setDisplayZoomControls(true);
        addJavascriptInterface(new TEChartWebView.WebAppEChartInterface(getContext()), "Android");
        loadUrl("file:///android_asset/echartWeb/EChart/EChart.html");
    }


    class WebAppEChartInterface {
        Context context;

        public WebAppEChartInterface(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public void showToast(String msg) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }

        /**
         * 获取
         *
         * @return
         */
        @JavascriptInterface
        public String getChartOptions() {
            if (dataSource != null) {
                GsonOption option = dataSource.markLineChartOptions();
                LogUtils.d(option.toString());
                return option.toString();
            }
            return null;
        }

    }

    ////////////////////////////数据源

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        //
        reload();
    }

    public DataSource getDataSource() {

        return dataSource;
    }

    public interface DataSource {
        GsonOption markLineChartOptions();
    }

}
