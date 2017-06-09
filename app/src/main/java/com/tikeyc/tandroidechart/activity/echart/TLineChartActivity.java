package com.tikeyc.tandroidechart.activity.echart;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;

import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.Magic;
import com.github.abel533.echarts.code.Tool;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.feature.MagicType;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Line;
import com.tikeyc.tandroidechart.R;
import com.tikeyc.tandroidechart.activity.base.TBaseActivity;
import com.tikeyc.tandroidechartlibrary.TEChartWebView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class TLineChartActivity extends TBaseActivity {

    @ViewInject(R.id.lineChartWebView)
    private TEChartWebView lineChartWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.isLandScape = true;
        super.onCreate(savedInstanceState);
        //设置横屏
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        setContentView(R.layout.activity_tline_chart);

        initData();
        initView();
    }



    private void initData() {

    }


    private void initView() {
        x.view().inject(this);

        //
        navigationBar_title_tv.setText("LineChart");
        //设置数据源
        lineChartWebView.setDataSource(new TEChartWebView.DataSource() {
            @Override
            public GsonOption markLineChartOptions() {
                return getLineChartOptions();
            }
        });
    }

    /**根据https://mvnrepository.com/artifact/com.github.abel533/ECharts
     * 结合http://echarts.baidu.com/examples.html官方实例
     * 配置json数据
     * @return
     */
    public GsonOption getLineChartOptions() {

        //地址:http://echarts.baidu.com/echarts2/doc/example/line5.html
        GsonOption option = new GsonOption();
        option.legend("高度(km)与气温(°C)变化关系");

        option.toolbox().show(true).feature(Tool.mark, Tool.dataView, new MagicType(Magic.line, Magic.bar), Tool.restore, Tool.saveAsImage);

        option.calculable(true);
        option.tooltip().trigger(Trigger.axis).formatter("Temperature : <br/>{b}km : {c}°C");

        ValueAxis valueAxis = new ValueAxis();
        valueAxis.axisLabel().formatter("{value} °C");
        option.xAxis(valueAxis);

        CategoryAxis categoryAxis = new CategoryAxis();
        categoryAxis.axisLine().onZero(false);
        categoryAxis.axisLabel().formatter("{value} km");
        categoryAxis.boundaryGap(false);
        categoryAxis.data(0, 10, 20, 30, 40, 50, 60, 70, 80);
        option.yAxis(categoryAxis);

        Line line = new Line();
        line.smooth(true).name("高度(km)与气温(°C)变化关系").data(15, -50, -56.5, -46.5, -22.1, -2.5, -27.7, -55.7, -76.5).itemStyle().normal().lineStyle().shadowColor("rgba(0,0,0,0.4)");
        option.series(line);
        return option;
    }



}
