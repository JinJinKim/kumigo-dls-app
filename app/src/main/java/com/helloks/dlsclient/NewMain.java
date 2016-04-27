package com.helloks.dlsclient;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

public class NewMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String PREF_USER_LEARNED_EULA = "eula_learned";
    private boolean mUserLearnedEULA;

    private boolean isFirstOpen = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState != null) {
            isFirstOpen = savedInstanceState.getBoolean("first");
        }

        if (isFirstOpen) {
            onNavigationItemSelected(navigationView.getMenu().getItem(0));
            navigationView.setCheckedItem(navigationView.getMenu().getItem(0).getItemId());
            isFirstOpen = false;
        }

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        mUserLearnedEULA = sp.getBoolean(PREF_USER_LEARNED_EULA, false);

        if (!mUserLearnedEULA) {
            showEULA();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putBoolean("first", isFirstOpen);
        super.onSaveInstanceState(outState, outPersistentState);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        Fragment fragment;

        int id = item.getItemId();

        if (id == R.id.nav_search) {
            fragment = getFragmentManager().findFragmentByTag(BookSearchFragment.TAG);

            if (fragment == null) {
                fragment = new BookSearchFragment();
            }

            getFragmentManager().beginTransaction().replace(R.id.container, fragment, BookSearchFragment.TAG).commit();
        } else if (id == R.id.nav_popular) {
            fragment = getFragmentManager().findFragmentByTag(PopBooksFragment.TAG);

            if (fragment == null) {
                fragment = new PopBooksFragment();
            }

            getFragmentManager().beginTransaction().replace(R.id.container, fragment, PopBooksFragment.TAG).commit();
        } else if (id == R.id.nav_arrival) {
            Toast.makeText(this, "준비 중", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void showEULA() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(NewMain.this);
        dialog.setPositiveButton("숙지했습니다", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mUserLearnedEULA = true;
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(NewMain.this);
                sp.edit().putBoolean(PREF_USER_LEARNED_EULA, true).apply();
            }
        });
        dialog.setCancelable(false);
        dialog.setTitle("처음 사용자 안내");
        dialog.setMessage("사용해 주셔서 감사합니다.\n" +
                "그 전에, 몇가지 알아야 하는 점이 있습니다.\n" +
                "1) 본 어플리케이션은 비영리로 제공되는 비공식 어플리케이션입니다. 학교와는 일절 관계 없습니다.\n" +
                "2) 사용하는 데이터의 출처는 도 교육청 DLS 이며, 사정에 따라 추후 서비스가 제한 될 수 있습니다.\n" +
                "3) 학교 도서관의 운영상의 사정으로 본 앱의 데이터는 실제 도서 상황과 일치하지 않을 수 있습니다. (분실, 도난, 파손 등)\n" +
                "위 사항에 동의하시면 아래 버튼을 눌러 사용을 시작하십시오.");
        dialog.show();
    }

}
