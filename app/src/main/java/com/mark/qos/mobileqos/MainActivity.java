package com.mark.qos.mobileqos;

import android.app.PendingIntent;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mark.qos.mobileqos.fragments.FragmentMap;
import com.mark.qos.mobileqos.fragments.FragmentPhoneInfo;
import com.mark.qos.mobileqos.fragments.FragmentResults;
import com.mark.qos.mobileqos.fragments.FragmentTest;
import com.mark.qos.mobileqos.storage.DatabaseManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentMap fragmentmap;
    FragmentPhoneInfo fragmentPhoneInfo;
    FragmentResults fragmentResults;
    FragmentTest fragmentTest;
    final String LOG_TAG = "myLogs";
    Intent intent;
    LocationService locationService;
    private  boolean bound = false;
    private PendingIntent pi;
    final int LOCATION = 1;
    public final static String PARAM_PINTENT = "pendingIntent";
    public final static String PARAM_RESULT = "result";


    Location location;



    DatabaseManager databaseManager;
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
             //           .setAction("Action", null).show();
                FragmentTransaction ftrans =  getSupportFragmentManager().beginTransaction();
                ftrans.replace(R.id.content_main, fragmentTest);
                ftrans.commit();
                fab.setVisibility(View.GONE);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        fragmentmap = new FragmentMap();
        fragmentPhoneInfo = new FragmentPhoneInfo();
        fragmentResults =  new FragmentResults();
        fragmentTest =  new FragmentTest();

       databaseManager = new DatabaseManager(getApplication());

        //startService(new Intent(this, LocationService.class));
       // intent = new Intent(this, LocationService.class);
        Intent data = new Intent();
        pi = createPendingResult(LOCATION, data, 0);
        intent = new Intent(this, LocationService.class).putExtra(PARAM_PINTENT, pi);
        startService(intent);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(LOG_TAG, "requestCode = " + requestCode + ", resultCode = "
                + resultCode);

        try {
            location = data.getParcelableExtra(MainActivity.PARAM_RESULT);
            Log.d(LOG_TAG, "latitude = " + location.getLatitude());
            Log.d(LOG_TAG, "speed = " + location.getSpeed());
        } catch (Exception e) {
            Log.d(LOG_TAG, "не получилось" );
        }

      //  Log.d(LOG_TAG, "latitude = " + location.getLatitude());

       /* // Ловим сообщения о старте задач
        if (resultCode == STATUS_START) {
            switch (requestCode) {
                case TASK1_CODE:
                    tvTask1.setText("Task1 start");
                    break;
                case TASK2_CODE:
                    tvTask2.setText("Task2 start");
                    break;
                case TASK3_CODE:
                    tvTask3.setText("Task3 start");
                    break;
            }*/
    //   }
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
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        fab.setVisibility(View.VISIBLE);
     //   FragmentTransaction  ftrans = getFragmentManager().beginTransaction();
      FragmentTransaction ftrans =  getSupportFragmentManager().beginTransaction();

        if (id == R.id.phone_info) {
            Log.d(LOG_TAG, "phone_info");
            ftrans.replace(R.id.content_main, fragmentPhoneInfo);

        } else if (id == R.id.results) {
            Log.d(LOG_TAG, "results");
            ftrans.replace(R.id.content_main, fragmentResults);

        } else if (id == R.id.map) {
            Log.d(LOG_TAG, "map");
            ftrans.replace(R.id.content_main, fragmentmap);

        } else if (id == R.id.setting) {
            Log.d(LOG_TAG, "setting");
            Intent intent = new Intent(this, SettingsActivity.class);
           startActivity(intent);

        } else if (id == R.id.nav_share) {
            Log.d(LOG_TAG, "nav_share");

        } else if (id == R.id.nav_send) {
            Log.d(LOG_TAG, "nav_send");
        }
        ftrans.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");
        stopService(intent);

    }

    public void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "onStart");
      //  bindService(intent, connection, BIND_AUTO_CREATE); //Context.BIND_AUTO_CREATE

    }

    public void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "onStop");
       // stopService(intent);
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public Location getLocation() {
        return location;
    }

  /*
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder binder) {
            Log.d(LOG_TAG, "MainActivity onServiceConnected" );

            //  MyService.MyServiceBinder myServiceBinder =
            //          (MyService.MyServiceBinder) binder;
            //  myService = myServiceBinder.getMyService();
            locationService = ((LocationService.LocationServiceBinder) binder).getLocationService();

            bound=true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(LOG_TAG, "onServiceDisconnected");
            bound=false;
        }
    };*/
}
