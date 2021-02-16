package anviinfotechs.hartikandharparivar.helper;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;


public class Functions {

    private static String MAIN_URL = " ******** ENTER YOU SERVER URL HERE ****** ";

    // Login URL
    public static String LOGIN_URL = MAIN_URL + "login.php";

    public static String REGISTER_URL = MAIN_URL + "register.php";

    public static String OTP_VERIFY_URL = MAIN_URL + "verification.php";

    public static String FETCH_PERVIOUS_PLANT_LOCATION_URL = MAIN_URL + "fetchPreviusPlantLocation.php";

    // Forgot Password
    public static String RESET_PASS_URL = MAIN_URL + "reset-password.php";

    public static String UPLOAD_PROFILE_URL = MAIN_URL + "upload_profile.php";

    public static String FETCH_PROFILE_URL = MAIN_URL + "fetch_profile.php";

    public static String CHECK_ACCOUNT_STATUS_URL = MAIN_URL + "Check_Account_Status.php";

    public static String FETCH_PLAN = MAIN_URL + "Fetch_Tree_Slot.php";

    public static String NOTIFICATION = MAIN_URL + "Check_Notification.php";

    public static String GET_TREE_DETAILS = MAIN_URL + "Get_AssignTreeDetails.php";

    public static String UPLOAD_TREE_PICTURE = MAIN_URL + "Insert_Plant_Picture_DB.php";

    public static String VIEW_AGREEMENT_URL = MAIN_URL + "ViewAgreement.php";

    public static String UPDATE_AGREEMENT_STATUS_URL = MAIN_URL + "Update_Agreement.php";

    /**
     *  Email Address Validation
     */
    public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public boolean logoutUser(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        db.resetTables();
        return true;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
}
