package com.helloks.dlsclient;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


public class MainActivity extends AppCompatActivity
        implements NavigationDrawerCallbacks {

    private static final String PREF_USER_LEARNED_EULA = "eula_learned";
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Toolbar mToolbar;
    private boolean mUserLearnedEULA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.fragment_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        mUserLearnedEULA = sp.getBoolean(PREF_USER_LEARNED_EULA, false);

        if (!mUserLearnedEULA) {
            showEULA();
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        //Toast.makeText(this, "Menu item selected -> " + position, Toast.LENGTH_SHORT).show();

        Fragment fragment;

        switch (position) {
            case 0:
                fragment = getFragmentManager().findFragmentByTag(BookSearchFragment.TAG);

                if (fragment == null) {
                    fragment = new BookSearchFragment();
                }

                getFragmentManager().beginTransaction().replace(R.id.container, fragment, BookSearchFragment.TAG).commit();

                break;
            case 1:
                fragment = getFragmentManager().findFragmentByTag(PopBooksFragment.TAG);

                if (fragment == null) {
                    fragment = new PopBooksFragment();
                }

                getFragmentManager().beginTransaction().replace(R.id.container, fragment, PopBooksFragment.TAG).commit();

                break;
            default:
                break;
        }

    }


    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else
            super.onBackPressed();
    }

    public void showEULA() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setPositiveButton("숙지했습니다", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mUserLearnedEULA = true;
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
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

/**
 @Override public boolean onCreateOptionsMenu(Menu menu) {
 if (!mNavigationDrawerFragment.isDrawerOpen()) {
 // Only show items in the action bar relevant to this screen
 // if the drawer is not showing. Otherwise, let the drawer
 // decide what to show in the action bar.

 //getMenuInflater().inflate(R.menu.main, menu);
 return true;
 }
 return super.onCreateOptionsMenu(menu);
 }


 @Override public boolean onOptionsItemSelected(MenuItem item) {
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
 **/

}
