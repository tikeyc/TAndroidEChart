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

    //在EChart.html中加载标签中默认调用了function toast(msg)，可以设置为false,显示自定义的加载提示
    private boolean isShowLoadingToast = true;

    public void setShowLoadingToast(boolean showLoadingToast) {
        isShowLoadingToast = showLoadingToast;
    }

    public boolean isShowLoadingToast() {

        return isShowLoadingToast;
    }

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
            if (isShowLoadingToast) Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
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

    /**java调用js的refreshEChartWithGsonOption方法刷新echart
     * 不能在第一时间就用此方法来显示图表，因为第一时间html的标签还未加载完成，不能获取到标签值
     * 需先设置数据源DataSource，后续视具体情况来手动刷新
     * @param option
     */
    public void refreshEChartWithGsonOption(GsonOption option) {
        String optionString = option.toString();
        String call = "javascript:refreshEChartWithOption('" + optionString + "')";
        loadUrl(call);
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
