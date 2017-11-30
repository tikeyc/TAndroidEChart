package com.tikeyc.tandroidechartlibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.github.abel533.echarts.json.GsonOption;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by public1 on 2017/6/8.
 */

public class TEChartWebView extends WebView {

    /** 默认false
     * 在EChart.html和EChart.js中因开发调试多处调用了function toast(msg) 可以设置为true开启调试模式
     */
    private boolean isDebug = false;


    /**
     * 存放在第一时间需要Android调用js的function
     * 因为在第一次见EChart.html及EChart.js还没有加载完成，而Java代码却是在第一时间调用了
     * 所以需要等到html的标签及js加载成功后再调用，在WebViewClient中的onPageFinished方法中
     */
    private List<String> shouldCallJsFunctionArray = new ArrayList<String>();

    public void setDebug(boolean isDebug) {
        this.isDebug = isDebug;
    }

    public boolean isDebug() {

        return isDebug;
    }

    public TEChartWebView(Context context) {
        this(context,null);
    }

    public TEChartWebView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TEChartWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initWebViewClient();

        init();
    }

    private void init() {
        //
        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportZoom(false);
        webSettings.setDisplayZoomControls(false);
        addJavascriptInterface(new TEChartWebView.WebAppEChartInterface(getContext()), "Android");
        loadUrl("file:///android_asset/echartWeb/EChart/EChart.html");
    }

    private void initWebViewClient () {
        setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                for (String callJs : shouldCallJsFunctionArray) {
                    loadUrl(callJs);
                }
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }
        });
    }

    /**刷新图表
     * java调用js的refreshEChartWithGsonOption方法刷新echart
     * 不能在第一时间就用此方法来显示图表，因为第一时间html的标签还未加载完成，不能获取到标签值
     * 需先设置数据源DataSource，后续视具体情况来手动刷新
     * @param option
     */
    public void refreshEchartsWithOption(GsonOption option) {
        if (dataSource == null) {
            assert false : "ataSource == null";
        }
        String optionString = option.toString();
        String call = "javascript:refreshEchartsWithOption('" + optionString + "')";
        loadUrl(call);
    }


    /**添加图表事件响应监听
     * @param echartActions 事件名称数组
     * @param onAddEchartActionHandlerResponseResultListener 事件点击后的echart返回的事件信息(echart返回的事件信息:http://echarts.baidu.com/api.html#events) 响应监听给开发者
     */
    public void addEchartActionHandler(TEChartConstant.PYEchartAction[] echartActions, OnAddEchartActionHandlerResponseResultListener onAddEchartActionHandlerResponseResultListener) {
        this.onAddEchartActionHandlerResponseResultListener = onAddEchartActionHandlerResponseResultListener;
        //
        for (TEChartConstant.PYEchartAction echartAction : echartActions) {
            String callString = echartAction.actionValue;
            String call = "javascript:addEchartActionHandler('" + callString + "')";
            //loadUrl(call);
            shouldCallJsFunctionArray.add(call);
        }
    }


    /**移除图表事件响应监听
     * @param echartAction 事件名称
     */
    public void removeEchartActionHandler(TEChartConstant.PYEchartAction echartAction) {
        String callString = echartAction.actionValue;
        String call = "javascript:removeEchartActionHandler('" + callString + "')";
        loadUrl(call);
    }


    /**
     *显示Echart自带的默认样式的Loading
     */
    public void myChartShowLoading() {
        String call = "javascript:myChartShowLoading()";
        //loadUrl(call);
        shouldCallJsFunctionArray.add(call);
    }
    /**
     *隐藏Echart自带的默认样式的Loading
     */
    public void myChartHideLoading() {
        String call = "javascript:myChartHideLoading()";
        loadUrl(call);
    }


    ///////////////////////WebAppEChartInterface////////////////////////////////

    /**
     * js 与 Android原生交互接口
     */
    class WebAppEChartInterface {
        Context context;

        public WebAppEChartInterface(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public void showDebugMessage(String message) {
            if (isDebug) LogUtils.e(message);
        }

        /**
         * 获取图表配置JSON数据
         *
         * @return
         */
        @JavascriptInterface
        public String getChartOptions() {
            if (dataSource != null) {
                GsonOption option = dataSource.markChartOptions();
                LogUtils.d(option.toString());
                return option.toString();
            }
            return null;
        }


        /**添加图表事件响应监听
         * @param params echart返回的事件信息 http://echarts.baidu.com/api.html#events
         */
        @JavascriptInterface
        public void addEchartActionHandlerResponseResult(String params) {
            LogUtils.e(params);
            if (onAddEchartActionHandlerResponseResultListener != null) {
                onAddEchartActionHandlerResponseResultListener.actionHandlerResponseResult(params);
            }
        }


        /**移除图表事件响应监听
         * @param params echart返回的事件信息 http://echarts.baidu.com/api.html#events
         */
        @JavascriptInterface
        public void removeEchartActionHandlerResponseResult(String params) {
            LogUtils.e(params);
        }

    }

    ///////////////////////WebAppEChartInterface////////////////////////////////

    ////////////////////////////数据源 获取图表的JSON配置//////////////////////////////

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
        GsonOption markChartOptions();
    }
    ////////////////////////////数据源 获取图表的JSON配置//////////////////////////////


    ////////////////////////////添加事件监听echart返回的 事件相关属性（是一个json），将该json返回给开发者使用
    ///////////////////////////echart返回的事件信息:http://echarts.baidu.com/api.html#events

    private OnAddEchartActionHandlerResponseResultListener onAddEchartActionHandlerResponseResultListener;

    public void setOnAddEchartActionHandlerResponseResultListener(OnAddEchartActionHandlerResponseResultListener onAddEchartActionHandlerResponseResultListener) {
        this.onAddEchartActionHandlerResponseResultListener = onAddEchartActionHandlerResponseResultListener;
    }

    public OnAddEchartActionHandlerResponseResultListener getOnAddEchartActionHandlerResponseResultListener() {

        return onAddEchartActionHandlerResponseResultListener;
    }

    public interface OnAddEchartActionHandlerResponseResultListener {
        void actionHandlerResponseResult(String result);
    }

}
