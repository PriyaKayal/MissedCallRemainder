package genius.com.callremainder;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    MediaPlayer mp=null ;
    private static final int PERMISSION_REQ = 111;
    private Button stopAlarmBtn;
    private DataStorage dataStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dataStorage = new DataStorage(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Function Not Set", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        TextView user = (TextView) navigationView.getHeaderView(0).findViewById(R.id.username);
        user.setText(dataStorage.getData("username"));

        stopAlarmBtn = (Button) findViewById(R.id.stop_alarm);
        assert stopAlarmBtn != null;
        stopAlarmBtn.setVisibility(View.INVISIBLE);

        mp = MediaPlayer.create(getBaseContext(),R.raw.alarm_tone);

        if(mp.isPlaying()){
            if(stopAlarmBtn.getVisibility() == View.INVISIBLE){
                stopAlarmBtn.setVisibility(View.VISIBLE);
            }
        }

        stopAlarmBtn.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                // TODO Auto-generated method stub
                mp.stop();
                finish();
                return false;
            }
        });

        playSound(this, getAlarmUri());
    }

    private void playSound(final Context context, Uri alert) {
        Thread background = new Thread(new Runnable() {
            public void run() {
                try {

                    mp.start();

                } catch (Throwable t) {
                }
            }
        });
        background.start();
    }

    private Uri getAlarmUri() {

        Uri alert = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alert == null) {
            alert = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if (alert == null) {
                alert = RingtoneManager
                        .getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }
        return alert;
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
        getMenuInflater().inflate(R.menu.home, menu);
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

        if (id == R.id.nav_logout) {
           dataStorage.clearAll();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void getPermisisns(){
        if(Build.VERSION.SDK_INT >= 23){
            String[] permisison = {Manifest.permission.READ_PHONE_STATE};
            if(!havePermission(this, permisison)){
                ActivityCompat.requestPermissions(this, permisison, PERMISSION_REQ);
            }
        }
    }

    private boolean havePermission(HomeActivity homeActivity, String[] permisison) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            for(String permissions : permisison){
                if(ActivityCompat.checkSelfPermission(homeActivity, permissions) != PackageManager.PERMISSION_GRANTED){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case PERMISSION_REQ:
                if(grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    getPermisisns();
                }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        getPermisisns();
    }

    @Override
    protected void onDestroy() {
        mp.stop();

        super.onDestroy();
    }
}
