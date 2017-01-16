package jp.techacademy.naofumi.teramoto.aensorapp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;

import static android.os.Environment.getDataDirectory;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {

    SensorManager sm;
    Sensor s;
    SampleSensorEventListener sse;
    int flag = 0;
//    TextView textViewX = (TextView) findViewById(R.id.textViewX);
//    TextView textViewY = (TextView) findViewById(R.id.textViewY);
//    TextView textViewZ = (TextView) findViewById(R.id.textViewZ);
//    Button button1 = (Button) findViewById(R.id.button1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(this);

        TextView textViewX = (TextView) findViewById(R.id.textViewX);
        textViewX.setText("Ｘ方向：");

        TextView textViewY = (TextView) findViewById(R.id.textViewY);
        textViewY.setText("Ｙ方向：");

        TextView textViewZ = (TextView) findViewById(R.id.textViewZ);
        textViewZ.setText("Ｚ方向：");

        sse=new SampleSensorEventListener();
    }

    @Override
    public void onClick(View v) {
        Button button1 = (Button) findViewById(R.id.button1);
        Log.d("UI_PARTS", "ボタンをタップしました");
        if (flag == 0){
            button1.setText("停止");
            flag = 1;
        }
        else{
            button1.setText("記録");
            flag = 0;
        }
    }

    // センサー関係
    protected void onResume()
    {
        super.onResume();
        sm=(SensorManager)getSystemService(
                Context.SENSOR_SERVICE);
        s=sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(sse,s,SensorManager.SENSOR_DELAY_NORMAL);
    }
    protected void onPause()
    {
        super.onPause();
        sm.unregisterListener(sse);
    }
    class SampleSensorEventListener
            implements SensorEventListener
    {
        public void onSensorChanged(SensorEvent e)
        {
            DecimalFormat df1=new DecimalFormat("##0.00");

            if(e.sensor.getType()==Sensor.TYPE_ACCELEROMETER)
            {
                float tmp= e.values[0]+e.values[1]+e.values[2];

                TextView textViewX = (TextView) findViewById(R.id.textViewX);
                TextView textViewY = (TextView) findViewById(R.id.textViewY);
                TextView textViewZ = (TextView) findViewById(R.id.textViewZ);
                textViewX.setText("X方向 "+df1.format(e.values[0]));
                textViewY.setText("Y方向 "+df1.format(e.values[1]));
                textViewZ.setText("Z方向 "+df1.format(e.values[2]));

                String et = new String(df1.format(e.values[0])+","+df1.format(e.values[1])+","+df1.format(e.values[2]));

                if(flag==0) {
                    try {

                        String filePath = "test.txt";
//                        String filePath = Environment.getDataDirectory()+"/test.txt";
                        File file = new File(filePath);
//                        file.getParentFile().mkdir();

                        FileOutputStream fos =
                                openFileOutput(filePath, Context.MODE_PRIVATE);
                        BufferedWriter bw =
                                new BufferedWriter(
                                        new OutputStreamWriter(fos));
                        bw.write(et);
                        bw.flush();
                        fos.close();
                    } catch (FileNotFoundException ee) {

                    } catch (IOException ee) {

                    }
                }
                else {
//                    tv[2].setText(String.valueOf(flag));
                }
            }
        }
        public void onAccuracyChanged(Sensor s,int accuracy){}
    }
    // ファイルを保存
    public void saveFile(String file, String str) {
        FileOutputStream fileOutputstream = null;

        try {
            fileOutputstream = openFileOutput(file, Context.MODE_PRIVATE);
            fileOutputstream.write(str.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
