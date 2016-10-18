package com.example.im028.gojackuser.CommonActivityClasses;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import com.example.im028.gojackuser.ActivityClasses.ChooseTypeActivity;
import com.example.im028.gojackuser.ActivityClasses.HistoryActivity;
import com.example.im028.gojackuser.AdapterClasses.CommonActionBarListAdapter;
import com.example.im028.gojackuser.R;
import com.example.im028.gojackuser.Utility.ConstantClasses.ConstantFunctions;
import com.example.im028.gojackuser.Utility.Session;

/**
 * Created by IM028 on 8/2/16.
 */
public class MenuCommonActivity extends AppCompatActivity {
    private static final String TAG = "MenuCommonActivity";
    private Toolbar toolbar;
    private FrameLayout frameLayout;
    private DrawerLayout drawerLayout;
    private ImageView menuImageView, menuHeaderImageView;
    private TextView menuHeaderTextView, titleTextView;
    private ListView menuListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_menu_activity_layout);


        toolbar = (Toolbar) findViewById(R.id.commonMenuActivityToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        drawerLayout = (DrawerLayout) findViewById(R.id.commonMenuActivityDrawerLayout);
        menuImageView = (ImageView) findViewById(R.id.menu);
        menuListView = (ListView) findViewById(R.id.commonMenuActivityDrawerListView);
        titleTextView = (TextView) findViewById(R.id.commonMenuActivityTitleTextView);
        menuImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        case "logout":
                            new Session(MenuCommonActivity.this, TAG).logout();
                            ConstantFunctions.logout(MenuCommonActivity.this);
                            break;
                        case "history":
                            startActivity(new Intent(MenuCommonActivity.this, HistoryActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                            break;
                        case "book your ride":
                            startActivity(new Intent(MenuCommonActivity.this, ChooseTypeActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
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
}
