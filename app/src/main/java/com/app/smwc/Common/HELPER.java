package com.app.smwc.Common;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Telephony;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.app.omcsalesapp.Views.DialogToast;
import com.app.smwc.Activity.MainActivity;
import com.app.smwc.R;
import com.app.ssn.Common.Pref;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HELPER {

    public static Dialog dialog;
    public static DecimalFormat format = new DecimalFormat("0.00");
    String[] mimeTypes =
            {"application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                    "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                    "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                    "text/plain",
                    "application/pdf",
                    "application/zip", "application/vnd.android.package-archive"};

    public static void print(String tag, String message) {
        Log.e(tag, message);
    }

    public static void apiLog(String apiName, String method, Map<String, String> data) {
        print("Api: ", apiName);
        print("Post: ", method);
        print("Param: ", data.toString());
    }

    /**
     * INTENT-EVENT
     */
    public static void SIMPLE_ROUTE(Activity act, Class routeName) {
        Intent i = new Intent(act, routeName);
        act.startActivity(i);
        act.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
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

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    public static String getDateFormat() {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        return sd.format(new Date());
    }

    public static String getSecondsFormat(String dateString) {
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sd = new SimpleDateFormat("dd-MM-yyyy");
            Date d = sd.parse(dateString);
            sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            assert d != null;
            return sd.format(d).replace("/", "-");
        } catch (ParseException ignored) {
        }
        return "";
    }

    public static String formatDate(String dateString) {
        try {
            HELPER.print("Date", dateString);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d = sd.parse(dateString);
            sd = new SimpleDateFormat("dd/MM/yyyy");
            assert d != null;
            return sd.format(d).replace("/", "-");
        } catch (ParseException ignored) {
        }
        return "";
    }

    public static String setDateFormat(String dateString) {
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d = sd.parse(dateString);
            sd = new SimpleDateFormat("dd-MM-yyyy");
            assert d != null;
            return sd.format(d).replace("/", "-");
        } catch (ParseException ignored) {
        }
        return "";
    }

    public static String getMonthYear(String dateString, String resultFormat) {
        try {
            HELPER.print("Date", dateString);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
            Date d = sd.parse(dateString);
            sd = new SimpleDateFormat(resultFormat);
            assert d != null;
            return sd.format(d).replace("/", "-");
        } catch (ParseException ignored) {
        }
        return "";
    }

    public static String apiPassDateFormat(String dateString) {
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sd = new SimpleDateFormat("dd-MM-yyyy");
            Date d = sd.parse(dateString);
            sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            assert d != null;
            return sd.format(d).replace("/", "-");
        } catch (ParseException ignored) {
        }
        return "";
    }

    public static String apiPassingDateWithoutTimeFormat(String dateString) {
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sd = new SimpleDateFormat("dd-MM-yyyy");
            Date d = sd.parse(dateString);
            sd = new SimpleDateFormat("yyyy-MM-dd");
            assert d != null;
            return sd.format(d).replace("/", "-");
        } catch (ParseException ignored) {
        }
        return "";
    }

    public static String getDateConditionFormat(String dateString) {
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d = sd.parse(dateString);
            sd = new SimpleDateFormat("dd/MM/yyyy");
            assert d != null;
            return sd.format(d).replace("/", "-");
        } catch (ParseException ignored) {
        }
        return "";
    }

    public static String apiPassMonthFormat(String dateString) {
        try {
            //January, 2023
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sd = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                sd = new SimpleDateFormat("MMMM, YYYY");
            }
            assert sd != null;
            Date d = sd.parse(dateString);
            sd = new SimpleDateFormat("yyyy-MM");
            assert d != null;
            return sd.format(d).replace("/", "-");
        } catch (ParseException ignored) {
        }
        return "";
    }


    public static String getFormatMonth(String month) {
        SimpleDateFormat format = new SimpleDateFormat("MMMM,yyyy,dd");
        Date newDate = null;
        try {
            newDate = format.parse(month);
            format = new SimpleDateFormat("yyyy-MM-dd");
            return format.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static ConstraintLayout.LayoutParams setMargin(int left, int right, int top, int bottom, MaterialButton imageView) {
        ConstraintLayout.LayoutParams parameter = (ConstraintLayout.LayoutParams) imageView.getLayoutParams();
        parameter.setMargins(left, top, right, bottom); // left, top, right, bottom
        return parameter;
    }

    public static void setTextColour(TextView textView, Context context) {
        textView.setTextColor(ContextCompat.getColor(context, R.color.white));
    }

    public static void setImageColour(ImageView imgView, Context context) {
        imgView.setColorFilter(ContextCompat.getColor(context,
                R.color.white));
    }

    public static void setLayoutBgColour(ConstraintLayout layout, Context context) {
        layout.setBackgroundColor(ContextCompat.getColor(context,
                R.color.black));
    }

    public static LinearLayout.LayoutParams setTextMargin(int left, int right, int top, int bottom, TextView textView) {
        LinearLayout.LayoutParams parameter = (LinearLayout.LayoutParams) textView.getLayoutParams();
        parameter.setMargins(left, top, right, bottom); // left, top, right, bottom
        return parameter;
    }

    public static LinearLayout.LayoutParams setTextInputLayoutMargin(int left, int right, int top, int bottom, TextInputLayout TextInputLayout) {
        LinearLayout.LayoutParams parameter = (LinearLayout.LayoutParams) TextInputLayout.getLayoutParams();
        parameter.setMargins(left, top, right, bottom); // left, top, right, bottom
        return parameter;
    }

    public static boolean compareDate(String fromDate, String toDate) {

        @SuppressLint("SimpleDateFormat") SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date currentDate = fmt.parse(toDate);
            Date examDate = fmt.parse(fromDate);
            Log.e("DATE-", currentDate + "------ " + examDate);
            assert currentDate != null;
            if (currentDate.compareTo(examDate) == 0) {
                return true;
            }
            if (currentDate.compareTo(examDate) > 0) {
                return true;
            }
            if (currentDate.compareTo(examDate) < 0) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void ROUTE(Activity act, Class routeName) {
        act.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        act.finish();
        Intent i = new Intent(act, routeName);
        act.startActivity(i);
        act.overridePendingTransition(R.anim.enter_from_right, R.anim.enter_from_left);
    }

    public static void ROUTE_ANIM(Activity act, Class routeName) {
        Intent i = new Intent(act, routeName);
        act.startActivity(i);
        act.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
    }


    public static void CHANGE_ACTIONBAR_COLOUR(Activity act) {
        Window window = act.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(act.getResources().getColor(R.color.colorPrimaryDark));
        //window.setStatusBarColor(isBlack ? act.getResources().getColor(R.color.colorBlack) : act.getResources().getColor(R.color.colorPrimary));
    }

    public static void ON_BACK_PRESS(Activity act) {
        act.finish();
    }


    public static void ERROR_HELPER(TextInputEditText editText, TextInputLayout nameEdtLayout) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nameEdtLayout.setError("");
                nameEdtLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public static int getStatusBarHeight(Activity act) {
        int result = 0;
        int resourceId = act.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = act.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
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

    public static void showLoadingTran(Activity act) {

        if (dialog != null && dialog.isShowing())
            return;
        dialog = new Dialog(act);
        dialog.getWindow().setBackgroundDrawableResource(
                R.color.colorProgressBackground);
        dialog.setContentView(R.layout.api_progressbar);
        dialog.setCancelable(false);
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Showing Alert Message
                try {
                    if (dialog != null && !dialog.isShowing())
                        dialog.show();
                } catch (WindowManager.BadTokenException e) {
                    e.printStackTrace();
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void dismissLoadingTran() {
        try {
            if (dialog != null && dialog.isShowing())
                dialog.dismiss();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public static void LOAD_HTML(TextView textView, String data) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(data, Html.FROM_HTML_MODE_COMPACT));
        } else {
            textView.setText(Html.fromHtml(data));
        }
    }

    /**
     * Determine if the device is a tablet (i.e. it has a large screen).
     *
     * @param context The calling context.
     */

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static void closeKeyboard(View view, Activity act) {
        if (view != null) {
            InputMethodManager manager = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public static String convertDate(String dateStr) {
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        Date result = null;
        String convertedDate = "";
        if (dateStr != null) {
            try {
                result = df.parse(dateStr);
                System.out.println("date:" + result);
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                System.out.println(sdf.format(result)); // prints date in the format sdf
                convertedDate = sdf.format(result);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return convertedDate;
    }

    public static String getConsecutiveOTP(String message) {
        String otp = "";
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(message);
        while (m.find()) {

            if (m.group().length() == 4) {
                otp = m.group();
            }
            break;
        }
        return otp;
    }


    public static void slideEnter(Activity act) {
        act.overridePendingTransition(
                R.anim.slide_from_right,
                R.anim.slide_to_left
        );
        /* overridePendingTransition(R.anim.right_enter, R.anim.left_out)*/
    }

//    public static void LogoutNewDesign(Activity act, String userId) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(act, R.style.RoundShapeTheme);
//        final View customLayout = act.getLayoutInflater().inflate(R.layout.logout_dialog, null);
//        TextView loggedUserId = customLayout.findViewById(R.id.userId);
//        AppCompatButton cancelBtn = customLayout.findViewById(R.id.btnDialogCancel);
//        AppCompatButton logoutBtn = customLayout.findViewById(R.id.btnDialogLogout);
//        loggedUserId.setText(userId);
//        builder.setView(customLayout);
//        AlertDialog dialog = builder.create();
//        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent_color);
//        dialog.setCancelable(false);
//        dialog.show();
//        cancelBtn.setOnClickListener(view -> dialog.dismiss());
//        logoutBtn.setOnClickListener(view -> {
//            new Pref(act).Logout();
//            Intent i = new Intent(act, MainActivity.class);
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            act.startActivity(i);
//            act.finish();
//            slideEnter(act);
//        });
//    }

    public static void logoutDialog(Activity act, Boolean isLogout, String email) {
        DialogToast dialogPermission = new DialogToast(act);
        if (!act.isFinishing() && dialog != null && dialogPermission.isShowing()) {
            dialog.dismiss();
        }
        if (act != null && !dialogPermission.isShowing())
            dialogPermission.show();

        dialogPermission.getHolder().getMessageLayout().setVisibility(View.VISIBLE);
        dialogPermission.getHolder().getBottomBtnLayout().setVisibility(View.VISIBLE);
        dialogPermission.getHolder().getBtnDialogGet().setVisibility(View.GONE);
        Objects.requireNonNull(dialogPermission.getHolder()).getBtnDialogCancel().setOnClickListener(view1 -> dialogPermission.dismiss());

        if (isLogout) {
            dialogPermission.getHolder().getMessageLayout().setVisibility(View.GONE);
            dialogPermission.getHolder().getUserLogoutLayout().setVisibility(View.VISIBLE);
            dialogPermission.getHolder().getBtnDialogCancel().setVisibility(View.GONE);
            dialogPermission.getHolder().getBtnDialogLogout().setVisibility(View.GONE);
            dialogPermission.getHolder().getOkBtn().setVisibility(View.VISIBLE);
            dialogPermission.getHolder().getCancelDialog().setVisibility(View.VISIBLE);
            dialogPermission.getHolder().getOkBtn().setText(R.string.logout);
            dialogPermission.getHolder().getUserId().setText(email);
            dialogPermission.getHolder().getOkBtn().setOnClickListener(view12 -> {
                new Pref(act).Logout();
                Intent i = new Intent(act, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                act.startActivity(i);
                act.finish();
                slideEnter(act);
            });
        } else {
            dialogPermission.getHolder().getBtnDialogLogout().setOnClickListener(view12 -> {
                new Pref(act).Logout();
                Intent i = new Intent(act, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                act.startActivity(i);
                act.finish();
                slideEnter(act);
            });
        }
        dialogPermission.getHolder().getCancelDialog().setOnClickListener(view12 -> {
            if (act != null && dialogPermission.isShowing())
                dialogPermission.dismiss();
        });
    }

    public static void commonDialog(Activity act, String msg) {

        DialogToast dialogPermission = new DialogToast(act);
        dialogPermission.show();
        dialogPermission.getHolder().getMessageLayout().setVisibility(View.VISIBLE);
        dialogPermission.getHolder().getTvMessage().setText(msg);
        dialogPermission.getHolder().getBottomBtnLayout().setVisibility(View.VISIBLE);
        dialogPermission.getHolder().getBtnDialogGet().setVisibility(View.GONE);
        dialogPermission.getHolder().getBtnDialogLogout().setText(R.string.delete);
        Objects.requireNonNull(dialogPermission.getHolder()).getBtnDialogCancel().setOnClickListener(view1 -> dialogPermission.dismiss());
        dialogPermission.getHolder().getBtnDialogLogout().setOnClickListener(view12 -> {
            dialogPermission.dismiss();
        });

    }

    public static void apiResponseDialog(Activity act, String msg) {
        DialogToast dialogPermission = new DialogToast(act);
        dialogPermission.show();

        dialogPermission.getHolder().getMessageLayout().setVisibility(View.VISIBLE);
        dialogPermission.getHolder().getTvMessage().setText(msg);
        dialogPermission.getHolder().getTvMessage().setGravity(Gravity.CENTER);
        dialogPermission.getHolder().getBottomBtnLayout().setVisibility(View.VISIBLE);
        dialogPermission.getHolder().getBtnDialogGet().setVisibility(View.GONE);
        dialogPermission.getHolder().getBtnDialogLogout().setText(R.string.delete);
        Objects.requireNonNull(dialogPermission.getHolder()).getBtnDialogCancel().setOnClickListener(view1 -> dialogPermission.dismiss());
        dialogPermission.getHolder().getBtnDialogLogout().setOnClickListener(view12 -> {
            dialogPermission.dismiss();
        });

    }

    public static void deleteEntryDialog(Activity act) {
        DialogToast dialogPermission = new DialogToast(act);
        dialogPermission.show();
        dialogPermission.getHolder().getMessageLayout().setVisibility(View.VISIBLE);
        dialogPermission.getHolder().getTvMessage().setText(R.string.deleteMessage);
        dialogPermission.getHolder().getBottomBtnLayout().setVisibility(View.VISIBLE);
        dialogPermission.getHolder().getBtnDialogGet().setVisibility(View.GONE);
        dialogPermission.getHolder().getBtnDialogLogout().setText(R.string.delete);
        Objects.requireNonNull(dialogPermission.getHolder()).getBtnDialogCancel().setOnClickListener(view1 -> dialogPermission.dismiss());
        dialogPermission.getHolder().getBtnDialogLogout().setOnClickListener(view12 -> {
            dialogPermission.dismiss();
        });
    }

    public static String[] getPdfMimType() {
        String[] exelMimeTypes = {"application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"};
        return exelMimeTypes;
    }

    public static void readPermission(Activity act) {
        // check condition
        if (ActivityCompat.checkSelfPermission(
                act,
                Manifest.permission
                        .READ_EXTERNAL_STORAGE)
                != PackageManager
                .PERMISSION_GRANTED) {
            // When permission is not granted
            // Result permission
            ActivityCompat.requestPermissions(
                    act,
                    new String[]{
                            Manifest.permission
                                    .READ_EXTERNAL_STORAGE},
                    1);
        }
    }

    public static void datelogic() {
        // 24 Hours And AM AND PM SHOW IN LOGIC
        Date dt = new Date();
        SimpleDateFormat dateFormat;
        dateFormat = new SimpleDateFormat("hh:mm:ss a");
        String date = dateFormat.format(dt);
//        // 48 Hours Show
//        Calendar calendar = Calendar.getInstance();
//        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
//        String strDate = mdformat.format(calendar.getTime());
    }

    public static void getMimType() {
        String[] mimeTypes =
                {"application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                        "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                        "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                        "text/plain",
                        "application/pdf",
                        "application/zip", "application/vnd.android.package-archive"};
        Intent intent;
        intent = new Intent(Intent.ACTION_GET_CONTENT); // or ACTION_OPEN_DOCUMENT
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, "application/vnd.ms-excel");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
    }

    public static void selectPdf(Activity act) {
        Intent pdfIntent = new Intent(Intent.ACTION_GET_CONTENT);
        String[] types = {"application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"};
        pdfIntent.setType("*/*");
        pdfIntent.putExtra(Intent.EXTRA_MIME_TYPES, types);
        //pdfIntent.putExtra(Intent.EXTRA_MIME_TYPES, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        pdfIntent.addCategory(Intent.CATEGORY_OPENABLE);
        act.startActivity(pdfIntent);
        // act.startActivityForResult(pdfIntent);
    }

    public static void getExelFile(Activity act) {

        File file = null;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        assert file != null;
        Uri apkURI = FileProvider.getUriForFile(act,
                act.getPackageName(), file);

        MimeTypeMap myMime = MimeTypeMap.getSingleton();
        String mimeType = myMime.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(apkURI.toString()));//It will return the mimetype
        intent.setDataAndType(apkURI, mimeType);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        act.startActivity(intent);
    }

    public static void slideExit(Activity act) {
        act.overridePendingTransition(
                R.anim.slide_from_left,
                R.anim.slide_to_right
        );
    }


    public static boolean emailValidator(String emailToText) {
        return !emailToText.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailToText).matches();
    }

    public static boolean phoneValidator(String emailToText) {
        return !emailToText.isEmpty() && Patterns.PHONE.matcher(emailToText).matches();
    }

    public static String getFacebookPageURL(Context context) {

        //method to get the right URL to use in the intent

        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) {
                return "fb://facewebmodal/f?href=" + "http://developers.facebook.com/android";
            } else { //older versions of fb app
                return "";
            }
        } catch (PackageManager.NameNotFoundException e) {
            return "http://developers.facebook.com/android"; //normal web url
        }
    }

    public static void shareOnMessage(Activity act, String shareContent) {

        try {
            String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(act);
            Intent textIntent = new Intent(Intent.ACTION_SEND);
            textIntent.setType("text/plain");
            textIntent.putExtra(Intent.EXTRA_TEXT, shareContent.trim());
            if (defaultSmsPackageName != null) {
                textIntent.setPackage(defaultSmsPackageName);
            }
            act.startActivity(textIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void shareOnWhatsapp(Activity act, String shareContent) {

        try {
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setPackage("com.whatsapp");
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareContent);
            sendIntent.setType("text/html");
            act.startActivity(sendIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void shareOnEmail(Activity act, String promoCode) {
        try {
            // Uri playStoreUrl = Uri.parse("market://details?id=" + act.getPackageName());
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "", null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Share your refer code");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "User my referral code " + promoCode + "\n" + "Download the app: " + "Config.playStoreUrl");
            act.startActivity(Intent.createChooser(emailIntent, null));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return true;

            return activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE;
        }
        return false;
    }

    @SuppressLint("MissingPermission")
    public static String getCountryCode(Activity act) {

        TelephonyManager tm = (TelephonyManager) act.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getNetworkCountryIso().toUpperCase();

    }

    static String getJsonFromAssets(Context context) {
        String jsonString;
        try {
            InputStream is = context.getAssets().open("CountryCodes.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            jsonString = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return jsonString;
    }
}

