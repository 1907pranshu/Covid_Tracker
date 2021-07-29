package com.pranshu.covidtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.pranshu.covidtracker.api.ApiUtilities;
import com.pranshu.covidtracker.api.CountryData;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView totalConfirm;
    private TextView todayConfirm;
    private TextView totalActive;
    private TextView totalRecovered;
    private TextView todayRecovered;
    private TextView totalDeath;
    private TextView todayDeath;
    private TextView totalTest;
    private List<CountryData> list;
    private TextView updated_date;
    private PieChart pieChart;
    private TextView tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.covid_tracker);

        list = new ArrayList<>();
        init();
        startAnimation();


        ApiUtilities.getApiInterface().getCountryData()
                .enqueue(new Callback<List<CountryData>>() {
                    @Override
                    public void onResponse(Call<List<CountryData>> call, Response<List<CountryData>> response) {
                        list.addAll(response.body());

                        for(int i=0;i<list.size();i++){
                            if(list.get(i).getCountry().equals("India")){
                                int conf = Integer.parseInt(list.get(i).getCases());
                                int actv = Integer.parseInt(list.get(i).getActive());
                                int rcvr = Integer.parseInt(list.get(i).getRecovered());
                                int deat = Integer.parseInt(list.get(i).getDeaths());

                                String confirm = NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getCases()));
                                String active = NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getActive()));
                                String recovered = NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getRecovered()));
                                String death = NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getDeaths()));
                                String tests = NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTests()));

//                                YoYo.with(Techniques.RollIn).duration(1000).repeat(1).playOn(tv1);




                                Log.d("covid--->>>>", (confirm));
                                Log.d("covid--->>>>Active", String.valueOf(active));
                                Log.d("covid--->>>>Recovered", String.valueOf(recovered));
                                Log.d("covid--->>>>Death", String.valueOf(death));

                                totalActive.setText(active);
                                totalConfirm.setText(confirm);
                                totalRecovered.setText(recovered);
                                totalDeath.setText(death);
                                totalTest.setText(tests);


                                todayConfirm.setText("(+"+list.get(i).getTodayCases()+")");
                                todayRecovered.setText("(+"+list.get(i).getTodayRecovered()+")");
                                todayDeath.setText("(+"+list.get(i).getTodayDeaths()+")");

                                setText(list.get(i).getUpdated());

                                pieChart.addPieSlice(new PieModel("Confirm", conf, getResources().getColor(R.color.yellow)));
                                pieChart.addPieSlice(new PieModel("Active", actv, getResources().getColor(R.color.blue_pie)));
                                pieChart.addPieSlice(new PieModel("Recovered", rcvr, getResources().getColor(R.color.green_pie)));
                                pieChart.addPieSlice(new PieModel("Death", deat, getResources().getColor(R.color.red_pie)));
                                pieChart.startAnimation();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CountryData>> call, Throwable t) {

                        Toast.makeText(MainActivity.this, "Error: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @SuppressLint("SetTextI18n")
    private void setText(String updated) {
        @SuppressLint("SimpleDateFormat")
        DateFormat format = new SimpleDateFormat("MMM dd, yyyy");

        long milliseconds = Long.parseLong(updated);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);

        updated_date.setText("Updated at "+format.format(calendar.getTime()));
    }

    private void init(){
        totalConfirm = findViewById(R.id.totalConfirm);
        todayConfirm = findViewById(R.id.todayConfirm);
        totalActive = findViewById(R.id.totalActive);
        totalRecovered = findViewById(R.id.totalRecovered);
        todayRecovered = findViewById(R.id.todayRecovered);
        totalDeath = findViewById(R.id.totalDeath);
        todayDeath = findViewById(R.id.todayDeath);
        totalTest = findViewById(R.id.totalTest);
        pieChart = findViewById(R.id.piechart);
        updated_date = findViewById(R.id.date_updated);
        tv1 = findViewById(R.id.tv1);



    }

    private void startAnimation(){
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim);
        tv1.startAnimation(animation);
    }
}