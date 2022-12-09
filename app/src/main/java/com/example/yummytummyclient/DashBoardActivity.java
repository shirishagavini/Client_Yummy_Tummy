package com.example.yummytummyclient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;


public class DashBoardActivity extends AppCompatActivity
{

    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;

    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);


        toolbar = (Toolbar) findViewById(R.id.customToolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer);
//      Setting the up the drawer layout.

        drawerToggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
        if(drawerToggle == null){
            Log.e("error","hello");
        }

        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();
//      Tie DrawerLayout events to the ActionBarToggle
//      It is making the listner for drawer layout.
        mDrawer.addDrawerListener(drawerToggle);


        nvDrawer = (NavigationView) findViewById(R.id.side_navigation);
//      Setting up the custom base fragment
        Fragment fragment = new OrderPendingFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.framelayout,fragment);
        transaction.commit();

        setupDrawerContent(nvDrawer);
    }

    private void setupDrawerContent(NavigationView navigationView)
    {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener()
                {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                      setting up the menuItem
                        selectDrawerItem(menuItem);
                        return true;
                    }
            });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment = new OrderPendingFragment();

        switch(menuItem.getItemId()) {
            case R.id.ordersPending:
                fragment = new OrderPendingFragment();
                break;
            case R.id.ordersCompeleted:
                fragment = new OrderCompeletedFragment();
                break;
            case R.id.menu:
                fragment = new MyMenuFragment();
                break;
            case R.id.profile:
                fragment = new ProfileFragment();
                break;
            default:
                fragment = new OrderPendingFragment();
        }

//      Getting fragment and setting the main framelayout
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.framelayout,fragment);
        transaction.commit();

        menuItem.setChecked(true);
//      Setting the title on the drawer layout
        setTitle(menuItem.getTitle());
//       closing the drawer
        mDrawer.closeDrawers();

    }


    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

}