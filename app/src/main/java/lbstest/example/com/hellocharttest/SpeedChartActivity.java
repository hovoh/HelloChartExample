package lbstest.example.com.hellocharttest;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.formatter.SimpleAxisValueFormatter;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

public class SpeedChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed_chart);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
        }
    }


    public static class PlaceholderFragment extends Fragment{
        private LineChartView chart;
        private LineChartData data;

        public PlaceholderFragment(){

        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tempo_chart,container,false);
            chart = rootView.findViewById(R.id.chart);
            generateSpeedData();

            return rootView;
        }

        private void generateSpeedData(){
            float speedRange = 55;
            float minHeight = 200;
            float maxHeight = 300;

            float scale = speedRange / maxHeight;
            float sub = (minHeight * scale) /2;

            int numValues = 52;

            Line line;
            List<PointValue> values;
            List<Line> lines = new ArrayList<>();

            values = new ArrayList<PointValue>();

            //获得每个点的高度
            for (int i=0;i<numValues;++i){
                float rawHeight = (float)(Math.random()*100+200);
                float normalizedHeight = rawHeight * scale -sub;
                values.add(new PointValue(i,normalizedHeight));
            }

            line = new Line(values);
            line.setColor(Color.GRAY);
            line.setHasPoints(false);   //不标点
            line.setFilled(true);
            line.setStrokeWidth(1);
            lines.add(line);

            values = new ArrayList<PointValue>();
            for (int i=0;i<numValues;++i){
                values.add(new PointValue(i,(float)Math.random()*30+20));
            }
            line = new Line(values);
            line.setColor(ChartUtils.COLOR_GREEN);
            line.setHasPoints(false);
            line.setStrokeWidth(3);
            lines.add(line);

            data = new LineChartData(lines);

            Axis distanceAxis = new Axis();
            distanceAxis.setName("Distance");
            distanceAxis.setTextColor(ChartUtils.COLOR_ORANGE);
            distanceAxis.setMaxLabelChars(4);
            distanceAxis.setFormatter(new SimpleAxisValueFormatter().setAppendedText("km".toCharArray()));
            distanceAxis.setHasLines(true);
            distanceAxis.setInside(true);
            data.setAxisXBottom(distanceAxis);

            data.setAxisYRight(new Axis().setName("Height [m]").setMaxLabelChars(3).setTextColor(ChartUtils.COLOR_BLUE)
                    .setFormatter(new HeightValueFormatter(scale, sub, 0)).setInside(true));

            chart.setLineChartData(data);

            Viewport v = chart.getMaximumViewport();
            v.set(v.left,speedRange,v.right,0);
            chart.setMaximumViewport(v);
            chart.setCurrentViewport(v);
        }

        private static class HeightValueFormatter extends SimpleAxisValueFormatter{
            private float scale;
            private float sub;
            private int decimalDigits;

            public HeightValueFormatter(float scale, float sub, int decimalDigits) {
                this.scale = scale;
                this.sub = sub;
                this.decimalDigits = decimalDigits;
            }

            @Override
            public int formatValueForAutoGeneratedAxis(char[] formattedValue, float value, int autoDecimalDigits) {
                float scaledValue = (value + sub) /scale ;
                return super.formatValueForAutoGeneratedAxis(formattedValue, value, autoDecimalDigits);
            }
        }

    }
}
