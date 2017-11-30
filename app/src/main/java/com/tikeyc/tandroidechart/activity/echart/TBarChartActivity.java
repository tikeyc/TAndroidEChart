package com.tikeyc.tandroidechart.activity.echart;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import com.github.abel533.echarts.Legend;
import com.github.abel533.echarts.Toolbox;
import com.github.abel533.echarts.Tooltip;
import com.github.abel533.echarts.axis.Axis;
import com.github.abel533.echarts.axis.AxisLabel;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.Magic;
import com.github.abel533.echarts.code.SeriesType;
import com.github.abel533.echarts.code.Tool;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.feature.DataView;
import com.github.abel533.echarts.feature.Feature;
import com.github.abel533.echarts.feature.MagicType;
import com.github.abel533.echarts.feature.Restore;
import com.github.abel533.echarts.feature.SaveAsImage;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.Line;
import com.github.abel533.echarts.series.Series;
import com.tikeyc.tandroidechart.R;
import com.tikeyc.tandroidechart.activity.base.TBaseActivity;
import com.tikeyc.tandroidechartlibrary.TEChartConstant;
import com.tikeyc.tandroidechartlibrary.TEChartWebView;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TBarChartActivity extends TBaseActivity {

    @ViewInject(R.id.barChartWebView)
    private TEChartWebView barChartWebView;

    @Event(R.id.navigationBar_title_tv)
    private void titleClick(View view) {
        if (!view.isSelected()) {
            barChartWebView.refreshEchartsWithOption(getLineChartOptions());
        } else {
            barChartWebView.refreshEchartsWithOption(getLineAndBarChartOption());
        }
        view.setSelected(!view.isSelected());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.isLandScape = true;
        super.onCreate(savedInstanceState);
        //设置横屏
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        setContentView(R.layout.activity_tbar_chart);

        initData();

        initView();
    }



    private void initData() {

    }


    private void initView() {
        x.view().inject(this);

        //
        //navigationBar_title_tv.setText("BarChart");
        //开启调试模式默认false
        barChartWebView.setDebug(true);
        //设置数据源
        barChartWebView.setDataSource(new TEChartWebView.DataSource() {
            @Override
            public GsonOption markChartOptions() {
                return getLineAndBarChartOption();
            }
        });

        //添加事件监听
        TEChartConstant.PYEchartAction[] echartActions = {TEChartConstant.PYEchartAction.PYEchartActionLegendSelected, TEChartConstant.PYEchartAction.PYEchartActionClick};
        barChartWebView.addEchartActionHandler(echartActions, new TEChartWebView.OnAddEchartActionHandlerResponseResultListener() {
            @Override
            public void actionHandlerResponseResult(String result) {
                //查看事件信息 处理事件
                /*TEChartConstant.PYEchartAction.PYEchartActionLegendSelected
                 *
                 *{"selected":{"蒸发量":true,"降水量":true,"平均温度":true},"target":"蒸发量","type":"legendSelected","event":{"zrenderX":220.33299255371094,"zrenderY":8.666999816894531,"zrenderFixed":1},"__echartsId":1512031135165}
                 */


                /*TEChartConstant.PYEchartAction.PYEchartActionClick
                 *
                 *{"seriesIndex":1,"seriesName":"降水量","dataIndex":4,"data":28.7,"name":"5月","value":28.7,"type":"click","event":{"zrenderX":261,"zrenderY":209,"zrenderFixed":1}}
                 */
            }
        });

    }



    /**根据https://mvnrepository.com/artifact/com.github.abel533/ECharts
     * 结合http://echarts.baidu.com/examples.html官方实例
     * 配置json数据
     * @return
     */
    public GsonOption getLineAndBarChartOption() {
        //http://echarts.baidu.com/echarts2/doc/example/mix1.html
        GsonOption option = new GsonOption();
        //title
        String text = "text";
        String subText = "subText";
        option.title(text,subText);
        //tooltip
        Tooltip tooltip = new Tooltip();
        tooltip.trigger(Trigger.axis);
        option.tooltip(tooltip);
        //toolbox
        Toolbox toolbox = new Toolbox();
        toolbox.show(true);
        Map<String, Feature> feature = new HashMap<String, Feature>();
        feature.put("mark",new Feature().show(true));
        feature.put("dataView",new DataView().show(true).readOnly(false));
        feature.put("magicType",new MagicType(Magic.line, Magic.bar).show(true));
        feature.put("restore",new Restore().show(true));
        feature.put("saveAsImage",new SaveAsImage().show(false));
        toolbox.setFeature(feature);
        option.toolbox(toolbox);
        //calculable
        option.setCalculable(true);
        //legend
        String legend1 = "蒸发量";
        String legend2 = "降水量";
        String legend3 = "平均温度";
        Legend legend = new Legend();
        legend.data(legend1,legend2,legend3);
        option.legend(legend);
        //grid
//            Grid grid = new Grid();
//            grid.y2(80);
//            option.grid(grid);
        //xAxis
        List<Axis> xAxis = new ArrayList<Axis>();
        CategoryAxis categoryAxis = new CategoryAxis();
        {
            List xAxisValues = new ArrayList();
            for (int i = 1; i <= 12; i++) {
                xAxisValues.add(i + "月");
            }
            categoryAxis.setData(xAxisValues);
        }
        xAxis.add(categoryAxis);
        option.xAxis(xAxis);
        //yAxis
        List<Axis> yAxis = new ArrayList<Axis>();
        {
            ValueAxis valueAxis = new ValueAxis();
            valueAxis.name("水量");
            valueAxis.axisLabel(new AxisLabel().formatter("{value} ml"));
            yAxis.add(valueAxis);
        }
        {
            ValueAxis valueAxis = new ValueAxis();
            valueAxis.name("温度");
            valueAxis.axisLabel(new AxisLabel().formatter("{value} °C"));
            yAxis.add(valueAxis);
        }
        option.yAxis(yAxis);
        //series
        List<Series> series = new ArrayList<Series>();
        {
            Bar bar = new Bar();
            bar.name(legend1).type(SeriesType.bar).yAxisIndex(0);
            List data = new ArrayList();
            double arrays[] = {2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 3.3};
            for (double value : arrays){
                data.add(value);
            }
            bar.setData(data);
            series.add(bar);
        }
        {
            Bar bar = new Bar();
            bar.name(legend2).type(SeriesType.bar).yAxisIndex(0);
            List data = new ArrayList();
            double arrays[] = {2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3};
            for (double value : arrays){
                data.add(value);
            }
            bar.setData(data);
            series.add(bar);
        }
        {
            Line line = new Line();
            line.name(legend3).type(SeriesType.line).yAxisIndex(1);
            List data = new ArrayList();
            double arrays[] = {2.0, 2.2, 3.3, 4.5, 6.3, 10.2, 20.3, 23.4, 23.0, 16.5, 12.0, 6.2};
            for (double value : arrays){
                data.add(value);
            }
            line.setData(data);
            series.add(line);
        }
        option.series(series);
        //
        return option;
    }


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
