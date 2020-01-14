package com.app.jobfizzer.Utilities.helpers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.app.jobfizzer.Utilities.FCM.ServiceClass;
import com.app.jobfizzer.Utilities.helpers.image_compressor.Compressor;
import com.gigamole.navigationtabstrip.NavigationTabStrip;
import com.app.jobfizzer.R;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.content.Context.INPUT_METHOD_SERVICE;


/*
 * Created by user on 23-10-2017.
 */

public class Utils {

    public static Boolean isShowing = false;

    @NonNull
    public static String getRoundUpValue(String totalCost) {
        return new BigDecimal(totalCost).setScale(2, RoundingMode.HALF_UP).toString();
    }

    public static InputFilter whiteSpacefilter = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                if (Character.isWhitespace(source.charAt(i))) {
                    return "";
                }
            }
            return null;
        }

    };
    public static InputFilter emojiSpecialFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                int type = Character.getType(source.charAt(i));
                System.out.println("Type : " + type);
                if (type == Character.SURROGATE || type == Character.OTHER_SYMBOL) {
                    return "";
                }

                if (!Character.isLetterOrDigit(source.charAt(i)) && !Character.isWhitespace(source.charAt(i))) {
                    return "";
                }

            }
            return null;
        }
    };
    public static InputFilter specialFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                if (!Character.isLetterOrDigit(source.charAt(i)) && !Character.isWhitespace(source.charAt(i))) {
                    return "";
                }
            }
            return null;
        }
    };
    public static InputFilter emojiChatFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                int type = Character.getType(source.charAt(i));
                System.out.println("Type : " + type);
                if (type == Character.SURROGATE || type == Character.OTHER_SYMBOL) {
                    return "";
                }

            }
            return null;
        }
    };
    private static CustomDialog customDialog;
    private static String TAG = Utils.class.getSimpleName();

    public static File getUploadFileName(Context context) {
        File filepath = Environment.getExternalStorageDirectory();
        final File zoeFolder = new File(filepath.getAbsolutePath(),
                context.getResources().getString(R.string.app_name)).getAbsoluteFile();
        if (!zoeFolder.exists()) {
            zoeFolder.mkdir();
        }
        File newFolder = new File(zoeFolder,
                context.getResources().getString(R.string.app_name) + context.getResources().getString(R.string.image)).getAbsoluteFile();
        if (!newFolder.exists()) {
            newFolder.mkdir();
        }

        Random r = new Random();
        int Low = 1000;
        int High = 10000000;
        int randomImageNo = r.nextInt(High - Low) + Low;
        String camera_captureFile = String.valueOf("PROFILE_IMG_" + randomImageNo);
        return new File(newFolder, camera_captureFile + ".jpg");
    }

    //image compression
    public static File getCompressedFile(Context context, File file) {
        File compressedImage = null;
        try {
            compressedImage = new Compressor(context)
                    .setQuality(80)
                    .setCompressFormat(Bitmap.CompressFormat.PNG)
                    .compressToFile(file);
            return compressedImage;
        } catch (IOException e) {
            e.printStackTrace();
            return file;
        }
    }


    public static Drawable getMapRequest(Context context) {
        Drawable drawable = context.getResources().getDrawable(R.drawable.map_placeholder);
        return drawable;
    }

    public static Drawable getProfilePicture(Context context) {
        Drawable drawable = context.getResources().getDrawable(R.drawable.dp);
        drawable.setColorFilter(new PorterDuffColorFilter(getPrimaryCOlor(context),
                PorterDuff.Mode.SRC_IN));
        return drawable;
    }


    public static Drawable getTinttedIcon(Drawable drawable, Context context) {

        drawable.setColorFilter(new PorterDuffColorFilter(Utils.getPrimaryCOlor(context),
                PorterDuff.Mode.SRC_IN));
        return drawable;
    }

    public static void initChatService(Activity activity) {

        if (!isMyServiceRunning(ServiceClass.class, activity)) {
            activity.startService(new Intent(activity, ServiceClass.class));
        } else {
            ServiceClass.Emitters emitters = new ServiceClass.Emitters(activity);
            emitters.emitonline();
        }
    }

    public static void showSnackBar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
    }

    public static boolean isMyServiceRunning(Class<?> serviceClass, Activity activity) {
        boolean finalA = false;
        ActivityManager manager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        if (manager != null) {
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    finalA = true;
                    break;
                }
            }
        }

        return finalA;
    }

    public static String convertDate(String dateInMilliseconds) {
        return DateFormat.format("h:mm a", Long.parseLong(dateInMilliseconds)).toString();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void hideKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public static boolean checkGpsisEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static boolean isValidEmail(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static int getPrimaryCOlor(Context context) {
        final TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorPrimary, value, true);
        return value.data;
    }

    public static void setIconColour(Context context, Drawable drawable) {
        drawable.setColorFilter(new PorterDuffColorFilter(getPrimaryCOlor(context), PorterDuff.Mode.SRC_IN));
    }


    public static void setButtonColor(Context context, Button button) {
        Drawable drawable1 = context.getResources().getDrawable(R.drawable.black_button);
        drawable1.setColorFilter(new PorterDuffColorFilter(Utils.getPrimaryCOlor(context), PorterDuff.Mode.SRC_IN));
        button.setBackground(drawable1);
    }


    public static void setStripColor(Context context, NavigationTabStrip drawable) {
        drawable.setStripColor(getPrimaryCOlor(context));
    }

    public static void Setheme(Context activity, String themevalue) {
        switch (themevalue) {
            case "1":
                activity.setTheme(R.style.AppThemeMain);
                break;
            case "2":
                activity.setTheme(R.style.AppThemePink);
                break;
            case "3":
                activity.setTheme(R.style.AppThemeBlue);
                break;
            case "4":
                activity.setTheme(R.style.AppThemeYellow);
                break;
            case "5":
                activity.setTheme(R.style.AppThemeIndigo);
                break;
            case "6":
                activity.setTheme(R.style.AppThemeRed);
                break;

            default:
                activity.setTheme(R.style.AppThemeMain);
                break;
        }
    }

    public static List<Integer> getAllMaterialColors(Context activity) {
        XmlResourceParser xrp = activity.getResources().getXml(R.xml.theme_color);
        List<Integer> allColors = new ArrayList<>();
        int nextEvent;
        try {
            while ((nextEvent = xrp.next()) != XmlResourceParser.END_DOCUMENT) {
                String s = xrp.getName();
                if ("color".equals(s)) {
                    String color = xrp.nextText();
                    allColors.add(Color.parseColor(color));
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allColors;
    }

    public static void log(String TAG, String content) {
        Log.d(TAG, "" + content);
    }

    public static void toast(View view, String toastmsg) {

        Snackbar snackbar = Snackbar.make(view, toastmsg, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    public static void show(Context context) {
        if (!isShowing) {
            isShowing = true;
            try {
                customDialog = new CustomDialog(context);
                customDialog.setCancelable(false);
                customDialog.setCanceledOnTouchOutside(false);
                customDialog.setContentView(R.layout.custom_dialog);
                Window window = customDialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.MATCH_PARENT);
                //  window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 0);
                customDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    public static void dismiss() {
        if (isShowing) {
            isShowing = false;
            try {
                customDialog.dismiss();
            } catch (Exception e) {

            }
        }
    }

    public static String getRealPathFromURI(Context context, Uri contentURI) {
        String result;
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentURI, filePathColumn,
                null, null, null);

        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor
                    .getColumnIndex(filePathColumn[0]);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    @SuppressLint("NewApi")
    public static String getRealPathFromUriNew(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static Drawable getColoredDrawable(Drawable drawable, Context context) {
        drawable.setColorFilter(new PorterDuffColorFilter(getPrimaryCOlor(context), PorterDuff.Mode.SRC_IN));
        return drawable;
    }


    public static class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new AsteriskPasswordTransformationMethod.PasswordCharSequence(source);
        }

        private class PasswordCharSequence implements CharSequence {
            private CharSequence mSource;

            public PasswordCharSequence(CharSequence source) {
                mSource = source; // Store char sequence
            }

            public char charAt(int index) {
                return '*'; // This is the important part
            }

            public int length() {
                return mSource.length(); // Return default
            }

            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end); // Return default
            }
        }
    }


}
