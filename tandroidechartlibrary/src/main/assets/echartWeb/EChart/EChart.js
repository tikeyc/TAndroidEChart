

$(function() {
    init();
});

function toast(msg){
     Android.showToast(msg);
}

function init() {
    initChartView();
}

function initChartView() {
//    toast("initLineChartView");
	/*必须加JOSN.parse 转换数据类型
	 *Android表示JAVA类TEChartWebView中的addJavascriptInterface(new TEChartWebView.WebAppEChartInterface(getContext()), "Android");中的Android
	 *getChartOptions为WebAppEChartInterface接口中的方法
	 */
    var option = JSON.parse(Android.getChartOptions());
    var lineChartDoc = document.getElementById('lineChart');
    /*
     *设置lineChart的高度为Android中控件WebView的高度（达到不能滑动且显示完全的效果）
     *var height = document.documentElement.clientHeight;
     *var height = window.innerHeight
     *这2个获取高度建议选择第二个
     */
    var height = window.innerHeight;
//    toast("height" + height.toString());
    $(lineChartDoc).css('height', height);
    //
    var lineChart = echarts.init(lineChartDoc);
    lineChart.setOption(option);
//    lineChart.setOption(makeStaticOptions());//直接在js中获取静态数据
}


//地址：http://echarts.baidu.com/echarts2/doc/example/line8.html
function makeStaticOptions(){

    option = {
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