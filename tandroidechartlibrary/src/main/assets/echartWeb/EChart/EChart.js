    /**
     * 使用百度前度Echart框架http://echarts.baidu.com/、https://gitee.com/free/ECharts
     * 参考iOS封装实现：https://github.com/Pluto-Y/iOS-Echarts
     * 等资料对Android版本的实现进行简单的封装
     * 时间不是很充裕，只能在零碎时间上来一点一点补充，因此肯定会出现许多不足之处
     * 希望在未来的某一天自己能封装的很完善
     *
     * Created by tikeyc on 2017/6/8.
     * GitHub：https://github.com/tikeyc
     */

    /////////////////////////

    var myChart;

    /////////////////////////

    $(function() {
        init();
    });

    function showDebugMessage(msg){
         Android.showDebugMessage(msg);
    }

    function init() {
        getMyChart();

        var echartJson = initChartOptions();

        loadEcharts(echartJson);
    }

    function getMyChart() {
        if(myChart != undefined) {
            return myChart;
        }
        showDebugMessage("initEchartView");
        var myChartDoc = document.getElementById('myChart');
        /*
         *设置lineChart的高度为Android中控件WebView的高度（达到不能滑动且显示完全的效果）
         *var height = document.documentElement.clientHeight;
         *var height = window.innerHeight
         *这2个获取高度建议选择第二个
         */
        var height = window.innerHeight;
        showDebugMessage("height" + height.toString());
        $(myChartDoc).css('height', height);
        //
        myChart = echarts.init(myChartDoc);

        return myChart;
    }

    function initChartOptions() {
        /*必须加JOSN.parse 转换数据类型
        *Android表示JAVA类TEChartWebView中的addJavascriptInterface(new TEChartWebView.WebAppEChartInterface(getContext()), "Android");中的Android
        *getChartOptions为WebAppEChartInterface接口中的方法
        */
        var echartJson = Android.getChartOptions();

        return echartJson;
    }

    /**
	 * 构建动态图表
	 */
    function loadEcharts(echartJson) {
        showDebugMessage("loadEcharts");
        //
        var option = JSON.parse(echartJson);
        option = preTask(option);
        getMyChart().setOption(option);
        //getMyChart().setOption(makeStaticOptions());//直接在js中获取静态数据
    }

    /*
     *刷新图表
     */
    function refreshEchartsWithOption(echartJson) {
        showDebugMessage("refreshEchartsWithOption");
        //
        var option = JSON.parse(echartJson);
        option = preTask(option);
        getMyChart().setOption(option, true);//刷新，带上第二个参数true
    }

    /*
     *添加图表事件响应监听
     */
    function addEchartActionHandler(eventName) {
        showDebugMessage("addEchartActionHandler:" + eventName);
        var ecConfig = echarts.config;
        getMyChart().on(eventName, addEchartViewAction);
    }
    function addEchartViewAction(param) {
        showDebugMessage("addEchartViewAction:" + param);
        alert(JSON.stringify(param));
        Android.addEchartActionHandlerResponseResult(JSON.stringify(param));
    }

    /*
     *移除图表事件响应监听
     */
    function removeEchartActionHandler(eventName) {
        showDebugMessage("removeEchartActionHandler:" + eventName);
        getMyChart().un(name, removeEchartViewAction);
    }
    function removeEchartViewAction(param) {
        showDebugMessage("removeEchartViewAction:" + param);
        alert(JSON.stringify(param));
        Android.removeEchartActionHandlerResponseResult(JSON.stringify(param));
    }

    function myChartShowLoading() {
        getMyChart().showLoading();
    }
    function myChartHideLoading() {
        getMyChart().hideLoading();
    }


    function preTask(obj) {
        var result;
        if(typeof(obj) == 'object') {
            if(obj instanceof Array) {
                result = new Array();
                for (var i = 0, len = obj.length; i < len ; i++) {
                     result.push(preTask(obj[i]));
                }
                return result;
            } else if(obj instanceof RegExp){
                return obj;
            } else {
                result = new Object();
                for (var prop in obj) {
                    result[prop] = preTask(obj[prop]);
                }
                return result;
            }
        } else if(typeof(obj) == 'string'){
            try {
                if(typeof(eval(obj)) == 'function'){
                    return eval(obj);
                } else if (typeof(eval(obj) == 'object') && (eval(obj) instanceof Array || eval(obj) instanceof CanvasGradient)) {
                    return eval(obj);
                }
            }catch(e) {
                return obj;
            }
            return obj;
        } else {
            return obj;
        }
    }



    /*示例
     *
     *地址：http://echarts.baidu.com/echarts2/doc/example/line8.html
     *静态JSON配置信息
     */
    function makeStaticOptions(){

        var option = {
            title : {
                text : '时间坐标折线图',
                subtext : 'dataZoom支持'
            },
            tooltip : {
                trigger: 'item',
                formatter : function (params) {
                    var date = new Date(params.value[0]);
                    data = date.getFullYear() + '-'
                           + (date.getMonth() + 1) + '-'
                           + date.getDate() + ' '
                           + date.getHours() + ':'
                           + date.getMinutes();
                    return data + '<br/>'
                           + params.value[1] + ', '
                           + params.value[2];
                }
            },
            toolbox: {
                show : true,
                feature : {
                    mark : {show: true},
                    dataView : {show: true, readOnly: false},
                    restore : {show: true},
                    saveAsImage : {show: true}
                }
            },
            dataZoom: {
                show: true,
                start : 70
            },
            legend : {
                data : ['series1']
            },
            grid: {
                y2: 80
            },
            xAxis : [
                {
                    type : 'time',
                    splitNumber:10
                }
            ],
            yAxis : [
                {
                    type : 'value'
                }
            ],
            series : [
                {
                    name: 'series1',
                    type: 'line',
                    showAllSymbol: true,
                    symbolSize: function (value){
                        return Math.round(value[2]/10) + 2;
                    },
                    data: (function () {
                        var d = [];
                        var len = 0;
                        var now = new Date();
                        var value;
                        while (len++ < 200) {
                            d.push([
                                new Date(2014, 9, 1, 0, len * 10000),
                                (Math.random()*30).toFixed(2) - 0,
                                (Math.random()*100).toFixed(2) - 0
                            ]);
                        }
                        return d;
                    })()
                }
            ]
        };

        return option;

    }