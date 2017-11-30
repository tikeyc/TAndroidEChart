package com.tikeyc.tandroidechartlibrary;

import java.io.Serializable;

/**
 * Created by tikeyc on 2017/11/30.
 * GitHub：https://github.com/tikeyc
 */

public class TEChartConstant {


    /**echart点击事件名称
     * http://echarts.baidu.com/api.html#events
     *
     * 以下已满足大部分需求
     * 可以根据具体需求进入上面官方API说明增加事件名称
     *
     */
    public enum PYEchartAction {
        PYEchartActionResize          ("resize"),//调整
        PYEchartActionClick           ("click"),
        PYEchartActionDbClick         ("dblclick"),
        PYEchartActionDataChanged     ("dataChanged"),
        PYEchartActionDataZoom        ("dataZoom"),
        PYEchartActionDataRange       ("dataRange"),
        PYEchartActionLegendSelected  ("legendSelected"),
        PYEchartActionMapSelected     ("mapSelected"),
        PYEchartActionPieSelected     ("pieSelected"),
        PYEchartActionMagicTypeChange ("magicTypeChanged"),
        PYEchartActionDataViewChanged ("dataViewChanged"),
        PYEchartActionTimelineChanged ("timelineChanged");

        public String actionValue;

        PYEchartAction(String actionValue) {
            this.actionValue = actionValue;
        }
    }


}
