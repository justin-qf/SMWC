package com.app.smwc.Common;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.app.smwc.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeReUse {

    // Notification
    public static final String CHANNEL_ID = "my_channel_01";
    public static final String CHANNEL_NAME = "iWINGZY";
    public static final String CHANNEL_DESCRIPTION = "com.app.iwingzy";
    public static final int GET_FORM_HEADER = 0;
    public static final int GET_JSON_HEADER = 1;
    public static final int CAMERA_INTENT = 101;
    public static final int GALLERY_INTENT = 102;
    public static final int ASK_PERMISSION = 103;
    public static final int SELECT_VIDEO_GALLERY = 104;
    public static final int SELECT_VIDEO_CAMERA = 105;
    public static DecimalFormat df = new DecimalFormat("##.###");
    public static String daysCount = "";
    @SuppressLint("DefaultLocale")
    public static NumberFormat formatterLakhs = new DecimalFormat("00,00,000");
    public static NumberFormat formatterCrores = new DecimalFormat("0,00,00,000");

    public static DateFormat dateformate = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&*+=])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    //check is email is valid or not
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    //check is contact valid or not
    public static boolean isContactValid(String mobileNumber) {
        if (mobileNumber == null) {
            return false;
        } else if (mobileNumber.isEmpty()) {
            return false;
        } else if (mobileNumber.length() < 10) {
            return false;
        } else return mobileNumber.length() <= 10;
    }

    public static void hideKeyboard(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideKeyboardWithoutView(Activity act) {
        InputMethodManager imm = (InputMethodManager) act.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = act.getCurrentFocus();
        if (view == null) {
            view = new View(act);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void setTimePickerDialog(Activity act, TextInputEditText editText) {

        TimePickerDialog picker;
        final Calendar cldr = Calendar.getInstance();
        int hour = cldr.get(Calendar.HOUR_OF_DAY);
        int minutes = cldr.get(Calendar.MINUTE);
        // time picker dialog
        picker = new TimePickerDialog(act,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                        int hour = sHour;
                        String timeSet;
                        if (hour > 12) {
                            hour -= 12;
                            timeSet = "PM";
                        } else if (hour == 0) {
                            hour += 12;
                            timeSet = "AM";
                        } else if (hour == 12) {
                            timeSet = "PM";
                        } else {
                            timeSet = "AM";
                        }

                        String min;
                        if (sMinute < 10)
                            min = "0" + sMinute;
                        else
                            min = String.valueOf(sMinute);

                        String aTime = String.valueOf(hour) + ':' + min + " " + timeSet;
                        Log.e("SELECT_TIME", aTime);
                        editText.setText(aTime);

                    }
                }, hour, minutes, false);

        picker.show();
    }

    public static String getRealPathFromURI(Activity activity, Uri contentURI) {

        String thePath = null;
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = activity.getContentResolver().query(contentURI, filePathColumn, null, null, null);

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            thePath = cursor.getString(columnIndex);
        }
        cursor.close();

        return thePath;
    }

    public static void datePicker(Activity act, TextInputEditText editText) {

        DatePickerDialog picker;
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);

        picker = new DatePickerDialog(act,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = (month + 1);
                        String dateStr = "";
                        String monthStr = "";
                        if (dayOfMonth < 10) {
                            dateStr = "0" + dayOfMonth;
                        } else {
                            dateStr = String.valueOf(dayOfMonth);
                        }

                        if (month < 10) {
                            monthStr = "0" + month;
                        } else {
                            monthStr = String.valueOf(month);
                        }
                        editText.setText(dateStr + "-" + monthStr + "-" + year);
                        Log.e("SELECT_DATE", dateStr + "-" + monthStr + "-" + year);
                    }

                }, year, month, day);

        picker.getDatePicker().setCalendarViewShown(false);
        picker.getDatePicker().setSpinnersShown(true);

        picker.show();
    }

    public static void activityBackPress(Activity act) {
        act.finish();
        act.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void setWhiteNavigationBar(@NonNull Dialog dialog, Activity act) {
        Window window = dialog.getWindow();
        if (window != null) {
            DisplayMetrics metrics = new DisplayMetrics();
            window.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            GradientDrawable dimDrawable = new GradientDrawable();
            // ...customize your dim effect here
            GradientDrawable navigationBarDrawable = new GradientDrawable();
            navigationBarDrawable.setShape(GradientDrawable.RECTANGLE);
            navigationBarDrawable.setColor(act.getColor(R.color.grayTextColor));
            // navigationBarDrawable.setTint(act.getColor(R.color.WhiteColor));
            Drawable[] layers = {dimDrawable, navigationBarDrawable};
            LayerDrawable windowBackground = new LayerDrawable(layers);
            windowBackground.setLayerInsetTop(1, metrics.heightPixels);
            window.setBackgroundDrawable(windowBackground);
        }
    }

    public static void RemoveError(EditText editText, TextInputLayout textInputLayout) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                editText.setError(null);
                textInputLayout.setError(null);
                textInputLayout.setHelperText("");
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
        });
    }

    //get each word capitalize
    public static String capitalizeString(String str) {
        String[] words = str.split("\\s");
        StringBuilder capitalizeWord = new StringBuilder();
        for (String w : words) {
            String first = w.substring(0, 1);
            String afterfirst = w.substring(1);
            capitalizeWord.append(first.toUpperCase()).append(afterfirst).append(" ");
        }
        return capitalizeWord.toString().trim();
    }

    public static String getDateFromDateTime(boolean wantDate, String dateTimeStr) {
        //true  return date
        //false return time
        try {
            //DateFormat f = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
            DateFormat f = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date d = f.parse(dateTimeStr);
            DateFormat date = new SimpleDateFormat("dd-MM-yyyy");
            DateFormat time = new SimpleDateFormat("hh:mm:ss a");


            if (wantDate)
                return date.format(d);
            else
                return time.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Intent makeCall(Activity act, String contactNumber) {
        Intent returnDate = new Intent();
        returnDate.putExtra("mobileNumber", contactNumber);
        Intent intent = new Intent(Intent.ACTION_CALL);

        intent.setData(Uri.parse("tel:" + contactNumber));
        if (ActivityCompat.checkSelfPermission(act, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            returnDate.putExtra("returnCode", -1);
            return returnDate;
        } else {
            returnDate.putExtra("returnCode", 1);
        }
        act.startActivity(intent);
        return returnDate;
    }

    public static String getFilenameFromURL(String url) {
        if (url == null) {
            return "";
        }
        try {
            URL resource = new URL(url);
            String host = resource.getHost();
            if (host.length() > 0 && url.endsWith(host)) {
                // handle ...example.com
                return "";
            }
        } catch (MalformedURLException e) {
            return "";
        }

        int startIndex = url.lastIndexOf('/') + 1;
        int length = url.length();

        // find end index for ?
        int lastQMPos = url.lastIndexOf('?');
        if (lastQMPos == -1) {
            lastQMPos = length;
        }

        // find end index for #
        int lastHashPos = url.lastIndexOf('#');
        if (lastHashPos == -1) {
            lastHashPos = length;
        }

        // calculate the end index
        int endIndex = Math.min(lastQMPos, lastHashPos);
        return url.substring(startIndex, endIndex);
    }

}
