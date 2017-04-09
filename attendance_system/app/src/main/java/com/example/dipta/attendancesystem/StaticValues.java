package com.example.dipta.attendancesystem;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class StaticValues {

    public static ArrayList<String> userTypeList = new ArrayList<String>(Arrays.asList("Student", "Teacher"));
    public static ArrayList<String> deptList = new ArrayList<String>(Arrays.asList("CSE", "EEE", "CE", "ME"));
    public static ArrayList<String> sectionList = new ArrayList<String>(Arrays.asList("A", "B", "None"));
    public static String[] designationList = {"Dean", "Head", "Professor", "Associate Professor", "Assistant Professor", "Lecturer", "Lab Assistant"};

    //public static String server = "http://wepoka.org/mobicon_server/";
    public static String server = "http://192.168.43.194/attendance_system_server/";

    public static String userInfoUrl = StaticValues.server + "user_info.php";
    public static String registerUrl = StaticValues.server + "register.php";

    public static String myCoursesUrl = StaticValues.server + "my_courses.php";
    public static String percentageUrl = StaticValues.server + "percentage.php";

    public static String assignedCourseListUrl = StaticValues.server + "assigned_course_list.php";
    public static String courseRegUrl = StaticValues.server + "course_reg.php";

    public static String attendanceCourseListUrl = StaticValues.server + "attendance_course_list.php";
    public static String attendanceStudentListUrl = StaticValues.server + "attendance_student_list.php";
    public static String submitAttendanceUrl = StaticValues.server + "submit_attendance.php";

    public static String modifyAttendanceStudentListUrl = StaticValues.server + "modify_attendance_student_list.php";
    public static String modifyAttendanceDateListUrl = StaticValues.server + "modify_attendance_date_list.php";
    public static String submitModifyAttendanceUrl = StaticValues.server + "submit_modify_attendance.php";

    public static String addCourseUrl = StaticValues.server + "add_course.php";
    public static String addAdminUrl = StaticValues.server + "add_admin.php";

    public static String courseListUrl = StaticValues.server + "course_list.php";
    public static String assignCourseUrl = StaticValues.server + "assign_course.php";

    public static String changePassUrl = StaticValues.server + "change_pass.php";

    public static SharedPreferences prefLoggedIn;
    public static HashMap<String, String> userData;


    public static void showToast(Activity activity, String msg) {
        Toast toast = Toast.makeText(activity, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

    }

}
