package com.example.dipta.attendancesystem.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.dipta.attendancesystem.R;
import com.example.dipta.attendancesystem.StaticValues;
import com.example.dipta.attendancesystem.fragments.AddAdminFragment;
import com.example.dipta.attendancesystem.fragments.AddCourseFragment;
import com.example.dipta.attendancesystem.fragments.AssignCourseFragment;
import com.example.dipta.attendancesystem.fragments.ChangePassFragment;
import com.example.dipta.attendancesystem.fragments.CourseRegFragment;
import com.example.dipta.attendancesystem.fragments.DefaultFragment;
import com.example.dipta.attendancesystem.fragments.ModifyAttendanceFragment;
import com.example.dipta.attendancesystem.fragments.MyCoursesFragment;
import com.example.dipta.attendancesystem.fragments.StudentListFragment;
import com.example.dipta.attendancesystem.fragments.TakeAttendanceFragment;
import com.example.dipta.attendancesystem.fragments.TeacherListFragment;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        DefaultFragment.OnFragmentInteractionListener,
        StudentListFragment.OnFragmentInteractionListener,
        TeacherListFragment.OnFragmentInteractionListener,
        CourseRegFragment.OnFragmentInteractionListener,
        MyCoursesFragment.OnFragmentInteractionListener,
        TakeAttendanceFragment.OnFragmentInteractionListener,
        ModifyAttendanceFragment.OnFragmentInteractionListener,
        AddCourseFragment.OnFragmentInteractionListener,
        AddAdminFragment.OnFragmentInteractionListener,
        AssignCourseFragment.OnFragmentInteractionListener,
        ChangePassFragment.OnFragmentInteractionListener {

    Fragment fragment = null;
    NavigationView navigationView;

    TextView navHeaderTxtName, navHeaderTxtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        navHeaderTxtName = (TextView) headerView.findViewById(R.id.navHeaderTxtName);
        navHeaderTxtEmail = (TextView) headerView.findViewById(R.id.navHeaderTxtEmail);

        navHeaderTxtName.setText(StaticValues.userData.get("full_name"));
        navHeaderTxtEmail.setText(StaticValues.userData.get("email"));

        if (StaticValues.userData.get("is_admin").equals("0")) {
            navigationView.getMenu().findItem(R.id.nav_admin_panel).setVisible(false);
        }

        if (StaticValues.userData.get("user_type").equals("1")) //student
        {
            navigationView.getMenu().findItem(R.id.nav_take_attendance).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_modify_attendance).setVisible(false);
        } else //teacher
        {
            navigationView.getMenu().findItem(R.id.nav_course_reg).setVisible(false);
        }

        if (savedInstanceState == null) {
            navigationView.getMenu().performIdentifierAction(R.id.nav_default, 0);
            navigationView.getMenu().findItem(R.id.nav_default).setChecked(true);
        }

    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if (fragment instanceof DefaultFragment) {
                new AlertDialog.Builder(this)
                        .setMessage("Are you sure you want to exit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                HomeActivity.this.finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            } else {
                navigationView.getMenu().performIdentifierAction(R.id.nav_default, 0);
                navigationView.getMenu().findItem(R.id.nav_default).setChecked(true);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_default) { //default fragment
            fragment = DefaultFragment.newInstance();

        } else if (id == R.id.nav_my_courses) { //default fragment
            fragment = MyCoursesFragment.newInstance();

        } else if (id == R.id.nav_course_reg) {
            fragment = CourseRegFragment.newInstance();

        } else if (id == R.id.nav_take_attendance) {
            fragment = TakeAttendanceFragment.newInstance();

        } else if (id == R.id.nav_modify_attendance) {
            fragment = ModifyAttendanceFragment.newInstance();

        } else if (id == R.id.nav_teachers) {
            fragment = TeacherListFragment.newInstance();

        } else if (id == R.id.nav_students) {
            fragment = StudentListFragment.newInstance();

        } else if (id == R.id.nav_add_course) {
            fragment = AddCourseFragment.newInstance();

        } else if (id == R.id.nav_assign_course) {
            fragment = AssignCourseFragment.newInstance();

        } else if (id == R.id.nav_add_admin) {
            fragment = AddAdminFragment.newInstance();

        } else if (id == R.id.nav_change_pass) {
            fragment = ChangePassFragment.newInstance();

        } else if (id == R.id.nav_logout) {

            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to logout?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            StaticValues.userData = null;

                            SharedPreferences.Editor editor = StaticValues.prefLoggedIn.edit();
                            editor.putInt("userID", -1);
                            editor.commit();

                            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(i);
                            finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }

        if (id != R.id.nav_logout) setTitle(item.getTitle());

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.homeContent, fragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
