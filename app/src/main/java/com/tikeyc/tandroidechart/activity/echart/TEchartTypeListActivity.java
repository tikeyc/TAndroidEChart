package com.tikeyc.tandroidechart.activity.echart;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.tikeyc.tandroidechart.R;
import com.tikeyc.tandroidechart.activity.base.TBaseActivity;

import org.xutils.common.util.DensityUtil;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by public1 on 2017/6/7.
 */

public class TEchartTypeListActivity extends TBaseActivity {

    static final String LINECHART = "lineChart";
    static final String BARCHART = "barChart";

    private String[] chartTypes = {LINECHART,BARCHART};

    @ViewInject(R.id.listView)
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_techart_type_list);

        initView();
    }

    private void initView() {
        x.view().inject(this);

        navigationBar_title_tv.setText("Echart");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String chartType = (String) adapterView.getItemAtPosition(i);

                if (LINECHART.equals(chartType)) {
                    Intent intent = new Intent(TEchartTypeListActivity.this,TLineChartActivity.class);
                    startActivity(intent);

                } else if (BARCHART.equals(chartType)) {
                    Intent intent = new Intent(TEchartTypeListActivity.this,TBarChartActivity.class);
                    startActivity(intent);
                }
            }
        });

        TChartListAdapter listAdapter = new TChartListAdapter();
        listView.setAdapter(listAdapter);
    }


    private class TChartListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return chartTypes.length;
        }

        @Override
        public Object getItem(int i) {
            return chartTypes[i];
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            if (view == null) {
                TextView textView = new TextView(TEchartTypeListActivity.this);
                view = textView;
                textView.setMinWidth(DensityUtil.getScreenWidth());
                textView.setMinHeight(100);
                textView.setPadding(40,0,0,0);
                textView.setGravity(Gravity.CENTER_VERTICAL);

            }

            TextView textView = (TextView) view;
            textView.setText(chartTypes[i]);
            return view;
        }
    }

}
