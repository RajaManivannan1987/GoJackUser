package com.calljack.im028.calljack.CommonActivityClasses;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.calljack.im028.calljack.ActivityClasses.AboutActivity;
import com.calljack.im028.calljack.ActivityClasses.ChooseTypeActivity;
import com.calljack.im028.calljack.ActivityClasses.DashboardActivity;
import com.calljack.im028.calljack.ActivityClasses.HistoryActivity;
import com.calljack.im028.calljack.ActivityClasses.PaymentMethodActivity;
import com.calljack.im028.calljack.ActivityClasses.PaytmLogin;
import com.calljack.im028.calljack.ActivityClasses.RateCardActivity;
import com.calljack.im028.calljack.ActivityClasses.ReferActivity;
import com.calljack.im028.calljack.ActivityClasses.ScheduleTripListActivity;
import com.calljack.im028.calljack.ActivityClasses.SettingsActivity;
import com.calljack.im028.calljack.AdapterClasses.CommonActionBarListAdapter;
import com.calljack.im028.calljack.ApplicationClass.MyApplication;
import com.calljack.im028.calljack.R;
import com.calljack.im028.calljack.Utility.ConstantClasses.ConstantFunctions;
import com.calljack.im028.calljack.Utility.ConstantClasses.ConstantValues;
import com.calljack.im028.calljack.Utility.InterNet.ConnectivityReceiver;
import com.calljack.im028.calljack.Utility.Session;

/**
 * Created by IM028 on 8/2/16.
 */
public class MenuCommonActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private static final String TAG = "MenuCommonActivity";
    private Toolbar toolbar;
    private FrameLayout frameLayout, menuActivityFrameLayout;
    private DrawerLayout drawerLayout;
    private ImageView menuImageView, menuHeaderImageView;
    public ImageView sosImg;
    private TextView menuHeaderTextView, titleTextView;
    private ListView menuListView;
    private LocationManager lm;
    boolean gps_enabled = false;
    boolean network_enabled = false;
    private final int MY_LOCATION = 1;
    private final int paymentTypeRequestCode = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_menu_activity_layout);
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        toolbar = (Toolbar) findViewById(R.id.commonMenuActivityToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        drawerLayout = (DrawerLayout) findViewById(R.id.commonMenuActivityDrawerLayout);
        menuImageView = (ImageView) findViewById(R.id.menu);
        sosImg = (ImageView) findViewById(R.id.sosMenu);
        menuListView = (ListView) findViewById(R.id.commonMenuActivityDrawerListView);
        titleTextView = (TextView) findViewById(R.id.commonMenuActivityTitleTextView);
        menuActivityFrameLayout = (FrameLayout) findViewById(R.id.menuActivityFrameLayout);
        sosImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstantFunctions.call(MenuCommonActivity.this, "+91 1111111111");
            }
        });
        menuImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstantFunctions.hideKeyboard(MenuCommonActivity.this, v);
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
        View headerView = getLayoutInflater().inflate(R.layout.commom_navigation_heading, null, false);
        menuHeaderImageView = (ImageView) headerView.findViewById(R.id.commonNavigationHeadingImageView);
        menuHeaderTextView = (TextView) headerView.findViewById(R.id.commonNavigationHeadingTextView);
        menuHeaderTextView.setText(new Session(this, "MenuCommonActivity").getName());
        menuListView.setAdapter(new CommonActionBarListAdapter(this));
        menuListView.addHeaderView(headerView);
        menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    TextView nameTextView = (TextView) view.findViewById(R.id.commonNavigationItemTextView);
//                    Toast.makeText(NavigationCommonActivity.this, nameTextView.getText().toString(), Toast.LENGTH_SHORT).show();
                    switch (nameTextView.getText().toString().toLowerCase().trim()) {
                        case "settings":
                            startActivity(new Intent(MenuCommonActivity.this, SettingsActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                            break;
                        case "history":
                            startActivity(new Intent(MenuCommonActivity.this, HistoryActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                            break;
                        case "scheduled trips":
                            startActivity(new Intent(MenuCommonActivity.this, ScheduleTripListActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                            break;
                        case "book your ride":
                            startActivity(new Intent(MenuCommonActivity.this, ChooseTypeActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                            break;
                        case "about":
                            startActivity(new Intent(MenuCommonActivity.this, AboutActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                            break;
                        case "payment method":
                            /*Intent intent = new Intent(MenuCommonActivity.this, PaymentMethodActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            intent.putExtra(ConstantValues.paymentType, new ConstantValues().getPayType());
                            startActivityForResult(intent, paymentTypeRequestCode);*/
//                            startActivity(new Intent(MenuCommonActivity.this, PaytmLogin.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                            break;
                        case "offers and free rides":
                            startActivity(new Intent(MenuCommonActivity.this, ReferActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                            break;
                        case "rate card":
                            startActivity(new Intent(MenuCommonActivity.this, RateCardActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                            break;
                        default:
                            break;
                    }
                    if (drawerLayout.isDrawerOpen(Gravity.LEFT))
                        drawerLayout.closeDrawer(Gravity.LEFT);
                }
            }
        });

    }

    public void setView(int viewLayout) {
        frameLayout = (FrameLayout) findViewById(R.id.commonMenuActivityFrameLayout);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = layoutInflater.inflate(viewLayout, null, false);
        frameLayout.addView(activityView);
    }

    public void setTitle(String title) {
        titleTextView.setText(title);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {
        String message = null;

        if (!isConnected) {
            message = "Sorry! Not connected to internet";

        } else {
            message = "Good! Connected to Internet";

        }
        ConstantFunctions.showSnakBar(message, menuActivityFrameLayout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
        enableLocation();
    }

    private void enableLocation() {
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }
        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("Enable Location Services");
            dialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    finish();
                }
            });
            dialog.show();
        } else {
            enableMyLocation();
        }
    }

    private boolean enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MenuCommonActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_LOCATION);
        } else {
            MyApplication.instanceLocation(MenuCommonActivity.this);
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableLocation();
                } else {
                    ConstantFunctions.toast(MenuCommonActivity.this, "My Location permission denied");
                    finish();
                }
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case paymentTypeRequestCode:
                if (resultCode == RESULT_OK) {
                    new ConstantValues().setPayType(data.getExtras().getString(ConstantValues.paymentType, "cash"));
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
