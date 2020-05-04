package com.example.application;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.RangeColumn;
import com.anychart.data.Mapping;
import com.anychart.data.Set;

import java.util.ArrayList;
import java.util.List;

public class Main_3_2_1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3_2_1);

        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));

        Cartesian cartesian = AnyChart.cartesian();

        cartesian.title("日統計圖表");

        List<DataEntry> data = new ArrayList<>();
        data.add(new CustomDataEntry("前日餘絀", 5.8, 7.9, 6.1, 8.9));
        data.add(new CustomDataEntry("薪資收入", 4.6, 6.1, 5.5, 8.2));
        data.add(new CustomDataEntry("獎金收入", 5.9, 8.1, 5.9, 8.1));
        data.add(new CustomDataEntry("投資收入", 7.8, 10.7, 7.1, 9.8));
        data.add(new CustomDataEntry("兼職收入", 10.5, 13.7, 8.3, 10.7));
        data.add(new CustomDataEntry("零用金", 13.8, 17, 10.7, 14.5));
        data.add(new CustomDataEntry("飲食支出", 16.5, 18.5, 12.3, 16.7));
        data.add(new CustomDataEntry("交通支出", 17.8, 19, 14, 16.3));
        data.add(new CustomDataEntry("娛樂支出", 15.4, 17.8, 13.7, 15.3));
        data.add(new CustomDataEntry("醫療支出", 12.7, 15.3, 12.3, 14.4));
        data.add(new CustomDataEntry("生活支出", 9.8, 13, 12.9, 10.7));
        data.add(new CustomDataEntry("教育支出", 9, 10.1, 8.2, 11.1));

        Set set = Set.instantiate();
        set.data(data);
        Mapping londonData = set.mapAs("{ x: 'x', high: 'londonHigh', low: 'londonLow' }");
        Mapping edinburgData = set.mapAs("{ x: 'x', high: 'edinburgHigh', low: 'edinburgLow' }");

        RangeColumn columnLondon = cartesian.rangeColumn(londonData);
        columnLondon.name("London收入");

        RangeColumn columnEdinburg = cartesian.rangeColumn(edinburgData);
        columnEdinburg.name("Edinburgh支出");

        cartesian.xAxis(true);
        cartesian.yAxis(true);

        cartesian.yScale()
                .minimum(4d)
                .maximum(20d);

        cartesian.legend(true);

        cartesian.yGrid(true)
                .yMinorGrid(true);

        cartesian.tooltip().titleFormat("{%SeriesName} ({%x})");

        anyChartView.setChart(cartesian);
    }

    private class CustomDataEntry extends DataEntry {
        public CustomDataEntry(String x, Number edinburgHigh, Number edinburgLow, Number londonHigh, Number londonLow) {
            setValue("x", x);
            setValue("edinburgHigh", edinburgHigh);
            setValue("edinburgLow", edinburgLow);
            setValue("londonHigh", londonHigh);
            setValue("londonLow", londonLow);
        }
    }
}
