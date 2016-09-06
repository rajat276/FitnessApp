package com.example.rajatjain.fitnessapp;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.result.DailyTotalResult;

import java.text.DateFormat;
import java.util.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;


import xyz.hanks.library.SmallBang;
import xyz.hanks.library.SmallBangListener;

import static java.text.DateFormat.getDateInstance;
import static java.text.DateFormat.getTimeInstance;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    public GoogleApiClient mClient = null;
    static String TAG="MainActivity_tag_log";
    Calendar cal;
    Date now;
    long endTime,startTime;
    TextView calories,steps,addtotitle,addtoinfo;
    SmallBang smallBang;
    Button AddhnW;
    CardView cardHnW;
    DonutProgress donutProgress;
    private boolean isInFront;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calories=(TextView)findViewById(R.id.tvccal);
        steps=(TextView)findViewById(R.id.tvstep);
        addtoinfo=(TextView)findViewById(R.id.tvadd2);
        addtotitle=(TextView)findViewById(R.id.tvadd);
        AddhnW=(Button)findViewById(R.id.badd);
        AddhnW.setOnClickListener(this);
        cardHnW=(CardView)findViewById(R.id.card_view_hnw);
        donutProgress = (DonutProgress) findViewById(R.id.donut_progress);
        donutProgress.setProgress(40);
        isInFront=true;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        smallBang= SmallBang.attach2Window(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        if (fab!=null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    smallBang.bang(view, 80, new SmallBangListener() {
                        @Override
                        public void onAnimationStart() {

                        }

                        @Override
                        public void onAnimationEnd() {

                        }
                    });
                    //Toast.makeText(MainActivity.this, "Long press to open Favourite Events", Toast.LENGTH_SHORT).show();
                    /*Snackbar.make(view, "", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();*/
                }
            });
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent i=new Intent("com.example.rajatjain.fitnessapp.leaderboardactivity");
            startActivity(i);


        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }
    void showDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.alert_dialog, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText mheight=(EditText)alertLayout.findViewById(R.id.etheight);
        final EditText mWeight=(EditText)alertLayout.findViewById(R.id.etweight);

        alert.setTitle("Height and Weight");
        final SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        final String height= sharedPref.getString("height",null);
        final String weight = sharedPref.getString("weight",null);
        if (height != null && weight!= null )
        {
            mheight.setText(height);
            mWeight.setText(weight);
        }
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(true);
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getBaseContext(), "Cancel clicked", Toast.LENGTH_SHORT).show();
                onResume();
            }
        });

        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String height=mheight.getText().toString();
                String weight=mWeight.getText().toString();
                if(TextUtils.isEmpty(height)||TextUtils.isEmpty(weight)) {
                    if (TextUtils.isEmpty(height)) {
                        mheight.setError("Enter height");
                    }
                    if (TextUtils.isEmpty(weight)) {
                        mWeight.setError("Enter weight");
                    }
                }else {
                    Toast.makeText(getBaseContext(), weight + " " + height, Toast.LENGTH_LONG).show();
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("height", height);
                    editor.putString("weight", weight);
                    editor.commit();
                    onResume();

                }
            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();

    }


    @Override
    protected void onResume() {
        super.onResume();
        isInFront=true;
        Log.e(TAG,"onResume");
        if(isNotEmpty()){
            final SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
            final String height= sharedPref.getString("height",null);
            final String weight = sharedPref.getString("weight",null);
            addtotitle.setText("Height and Weight");
            String temp="   Height: "+height+" cm"+"\n"+"   Weight: "+weight+" kg";
            addtoinfo.setText(temp);
            AddhnW.setText("Change");
        }else {
            addtotitle.setText("To get Started");
            addtoinfo.setText("Add your Height and Weight");
            AddhnW.setText("Add");

        }
        buildFitnessClient();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isInFront=false;
    }

    private void buildFitnessClient() {
        Log.e(TAG,"In buildfitnessClient");
        if (mClient == null) {
            mClient = new GoogleApiClient.Builder(this)
                    .addApi(Fitness.HISTORY_API)
                    .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                    .addConnectionCallbacks(
                            new GoogleApiClient.ConnectionCallbacks() {
                                @Override
                                public void onConnected(Bundle bundle) {
                                    Log.e(TAG, "Connected!!!");
                                    if(isNotEmpty()){
                                        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                                        saveUserHeight(Integer.parseInt(sharedPref.getString("height","0")));
                                        float floatweight = 0;
                                        try
                                        {
                                            floatweight=Float.parseFloat(sharedPref.getString("height","0").trim());
                                        }
                                        catch(NumberFormatException e)
                                        {
                                            Log.e(TAG,e.toString());
                                        }
                                        saveUserWeight(floatweight);
                                    }

                                    findFitnessDataSources();
                                }

                                @Override
                                public void onConnectionSuspended(int i) {
                                    // If your connection to the sensor gets lost at some point,
                                    // you'll be able to determine the reason and react to it here.
                                    if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
                                        Log.e(TAG, "Connection lost.  Cause: Network Lost.");
                                    } else if (i
                                            == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
                                        Log.e(TAG,
                                                "Connection lost.  Reason: Service Disconnected");
                                    }
                                }
                            }
                    )
                    .enableAutoManage(this, 0, new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult result) {
                            Log.e(TAG, "Google Play services connection failed. Cause: " +
                                    result.toString());
                        }
                    })
                    .build();
        }
    }

    private boolean isNotEmpty() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String height = sharedPref.getString("height", null);
        String weight = sharedPref.getString("weight", null);
        if (height != null && weight!= null )
        {
            return true;
        }
        return false;
    }

    private void findFitnessDataSources() {
        cal = Calendar.getInstance();
        now = new Date();
        cal.setTime(now);
        endTime = cal.getTimeInMillis();
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        startTime = cal.getTimeInMillis();

        java.text.DateFormat dateFormat = getDateInstance();
        Log.e(TAG, "Range Start: " + dateFormat.format(startTime));
        Log.e(TAG, "Range End: " + dateFormat.format(endTime));

     /*   DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_CALORIES_EXPENDED, DataType.AGGREGATE_CALORIES_EXPENDED)
                .bucketByActivityType(1, TimeUnit.SECONDS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();


        Fitness.HistoryApi.readData(mClient,readRequest).setResultCallback(new ResultCallback<DataReadResult>() {
            @Override
            public void onResult(@NonNull DataReadResult dataReadResult) {
               dumpDataSet(dataReadResult.getDataSet(DataType.TYPE_CALORIES_EXPENDED));

            }
        });*/
       /* DataReadRequest readRequest = new DataReadRequest.Builder()
                // The data request can specify multiple data types to return, effectively
                // combining multiple data queries into one call.
                // In this example, it's very unlikely that the request is for several hundred
                // datapoints each consisting of a few steps and a timestamp.  The more likely
                // scenario is wanting to see how many steps were walked per day, for 7 days.
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                // Analogous to a "Group By" in SQL, defines how data should be aggregated.
                // bucketByTime allows for a time span, whereas bucketBySession would allow
                // bucketing by "sessions", which would need to be defined in code.
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();
        Fitness.HistoryApi.readData(mClient,readRequest).setResultCallback(new ResultCallback<DataReadResult>() {
            @Override
            public void onResult(@NonNull DataReadResult dataReadResult) {
                dumpDataSet(dataReadResult.getDataSet(DataType.TYPE_STEP_COUNT_DELTA));

            }
        });*/
        Fitness.HistoryApi.readDailyTotal(mClient,DataType.TYPE_CALORIES_EXPENDED).setResultCallback(new ResultCallback<DailyTotalResult>() {
            @Override
            public void onResult(@NonNull DailyTotalResult dailyTotalResult) {
                dumpDataSet(dailyTotalResult.getTotal());
            }
        });
        Fitness.HistoryApi.readDailyTotal(mClient,DataType.TYPE_STEP_COUNT_DELTA).setResultCallback(new ResultCallback<DailyTotalResult>() {
            @Override
            public void onResult(@NonNull DailyTotalResult dailyTotalResult) {
                dumpDataSet(dailyTotalResult.getTotal());
            }
        });
    }
    private void dumpDataSet(DataSet dataSet) {
        Log.e(TAG, "Data returned for Data type: " + dataSet.getDataType().getName());
        DateFormat dateFormat = getTimeInstance();

        for (DataPoint dp : dataSet.getDataPoints()) {
            Log.e(TAG, "Data point:");
            Log.e(TAG, "\tType: " + dp.getDataType().getName());
            Log.e(TAG, "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            Log.e(TAG, "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
            for(Field field : dp.getDataType().getFields()) {
                Log.e(TAG, "\tField: " + field.getName() +
                        " Value: " + dp.getValue(field));
                if(field.getName().equals("calories")){
                    float temp= Float.valueOf(dp.getValue(field).toString());
                    int inttemp= 0;
                    inttemp=(int) temp;

                    calories.setText(String.valueOf(inttemp));
                }else {
                    steps.setText(dp.getValue(field).toString());
                }
            }
        }
    }

    public void saveUserHeight(int heightCentimiters) {
        // to post data
        float height = ((float) heightCentimiters) / 100.0f;
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        long startTime = cal.getTimeInMillis();

        DataSet heightDataSet = createDataForRequest(
                DataType.TYPE_HEIGHT,    // for height, it would be DataType.TYPE_HEIGHT
                DataSource.TYPE_RAW,
                height,                  // weight in kgs
                startTime,              // start time
                endTime,                // end time
                TimeUnit.MILLISECONDS                // Time Unit, for example, TimeUnit.MILLISECONDS
        );


                Fitness.HistoryApi.insertData(mClient, heightDataSet).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        Log.e(TAG,"height insert status:"+status);
                    }
                });
                     //   .await(1, TimeUnit.MINUTES);
    }

    public void saveUserWeight(float weight) {
        // to post data
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        long startTime = cal.getTimeInMillis();

        DataSet weightDataSet = createDataForRequest(
                DataType.TYPE_WEIGHT,    // for height, it would be DataType.TYPE_HEIGHT
                DataSource.TYPE_RAW,
                weight,                  // weight in kgs
                startTime,              // start time
                endTime,                // end time
                TimeUnit.MILLISECONDS                // Time Unit, for example, TimeUnit.MILLISECONDS
        );


                Fitness.HistoryApi.insertData(mClient, weightDataSet).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                Log.e(TAG,"weight insert status:"+status);
            }
        });
                        //.await(1, TimeUnit.MINUTES);
    }

    public DataSet createDataForRequest(DataType dataType,
                                        int dataSourceType,
                                        Object values,
                                        long startTime,
                                        long endTime,
                                        TimeUnit timeUnit) {
        DataSource dataSource = new DataSource.Builder()
                .setAppPackageName(this)
                .setDataType(dataType)
                .setType(dataSourceType)
                .build();

        DataSet dataSet = DataSet.create(dataSource);
        DataPoint dataPoint = dataSet.createDataPoint().setTimeInterval(startTime, endTime, timeUnit);

        if (values instanceof Integer) {
            dataPoint = dataPoint.setIntValues((Integer) values);
        } else {
            dataPoint = dataPoint.setFloatValues((Float) values);
        }

        dataSet.add(dataPoint);

        return dataSet;
    }


    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.badd){
            showDialog();
        }

    }
}
