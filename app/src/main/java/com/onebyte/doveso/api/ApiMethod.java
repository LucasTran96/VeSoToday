package com.onebyte.doveso.api;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.InputFilter;
import android.util.Log;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import static android.content.Context.MODE_PRIVATE;
import static com.onebyte.doveso.contants.Global.DAC_BIET;
import static com.onebyte.doveso.contants.Global.DATE_SELECT_FORMAT_DM;
import static com.onebyte.doveso.contants.Global.DB;
import static com.onebyte.doveso.contants.Global.DB_MB;
import static com.onebyte.doveso.contants.Global.DEFAULT_DATE_SELECT_FORMAT;
import static com.onebyte.doveso.contants.Global.G1;
import static com.onebyte.doveso.contants.Global.G1_MB;
import static com.onebyte.doveso.contants.Global.G2;
import static com.onebyte.doveso.contants.Global.G2_MB;
import static com.onebyte.doveso.contants.Global.G3;
import static com.onebyte.doveso.contants.Global.G3_MB;
import static com.onebyte.doveso.contants.Global.G4;
import static com.onebyte.doveso.contants.Global.G4_MB;
import static com.onebyte.doveso.contants.Global.G5;
import static com.onebyte.doveso.contants.Global.G5_MB;
import static com.onebyte.doveso.contants.Global.G6;
import static com.onebyte.doveso.contants.Global.G6_MB;
import static com.onebyte.doveso.contants.Global.G7;
import static com.onebyte.doveso.contants.Global.G7_MB;
import static com.onebyte.doveso.contants.Global.G8;
import static com.onebyte.doveso.contants.Global.GIAI_1;
import static com.onebyte.doveso.contants.Global.GIAI_2;
import static com.onebyte.doveso.contants.Global.GIAI_3;
import static com.onebyte.doveso.contants.Global.GIAI_4;
import static com.onebyte.doveso.contants.Global.GIAI_5;
import static com.onebyte.doveso.contants.Global.GIAI_6;
import static com.onebyte.doveso.contants.Global.GIAI_7;
import static com.onebyte.doveso.contants.Global.KET_QUA_MIEN_BAC;
import static com.onebyte.doveso.contants.Global.KET_QUA_MIEN_NAM;
import static com.onebyte.doveso.contants.Global.KET_QUA_MIEN_TRUNG;
import static com.onebyte.doveso.contants.Global.KET_QUA_VIETLOTT_6_45;
import static com.onebyte.doveso.contants.Global.KHUYEN_KHICH_MB;
import static com.onebyte.doveso.contants.Global.KHUYEN_KHICH_MN;
import static com.onebyte.doveso.contants.Global.KK_MB;
import static com.onebyte.doveso.contants.Global.KK_MN;
import static com.onebyte.doveso.contants.Global.MIEN_BAC;
import static com.onebyte.doveso.contants.Global.MIEN_NAM;
import static com.onebyte.doveso.contants.Global.MIEN_TRUNG;
import static com.onebyte.doveso.contants.Global.PDB;
import static com.onebyte.doveso.contants.Global.PDB_MB;
import static com.onebyte.doveso.contants.Global.PHU_DAC_BIET;
import static com.onebyte.doveso.contants.Global.SETTINGS;
import static com.onebyte.doveso.contants.Global.VIETLOTT_6_45;
import static com.onebyte.doveso.contants.Global.VIETLOTT_6_55;

public class ApiMethod {

    public  static AlertDialog alertDialog;
    /**
     * The getTimeNow method is used to retrieve the Day.
     */
    public static String convertDate(Date dateString) {

        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat(DEFAULT_DATE_SELECT_FORMAT);
        if(dateString == null)
        {
            Date date = Calendar.getInstance().getTime();
            return formatter.format(date);
        }
        else {
            return formatter.format(dateString);
        }
    }

    /**
     * The getTimeNow method is used to retrieve the current time.
     */
    public static String getDateNow(String date_Formart) {
        Date date = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat(date_Formart);

        return format.format(date);
    }

    /**
     * isValidDate ????y l?? ph????ng th???c ki???m tra c?? ????ng ?????nh d??ng ng??y th??ng hay kh??ng
     * @param dateFormat dd 'thang' MMMM
     * @param date VD: 25 thang 5
     * @return
     */
    public static boolean isValidDate(String dateFormat, String date) {
        DateFormat sdf = new SimpleDateFormat(dateFormat, new Locale("vn", "VN"));
        sdf.setLenient(false);
        try {
            sdf.parse(date);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }


    /**
     * Method: ????y l?? ph????ng th???c convert date to millisecond.
     * @param dateConvert
     * @return
     */
    public static Long convertDateToMillisecond(String dateConvert)
    {
        try {
            DateFormat formatter = new SimpleDateFormat(DEFAULT_DATE_SELECT_FORMAT);
            Date date = (Date)formatter.parse(dateConvert);
            long output=date.getTime()/1000L;
            String str=Long.toString(output);
            return Long.parseLong(str) * 1000;
        } catch (ParseException e) {
            e.printStackTrace();
            try {
                DateFormat formatter = new SimpleDateFormat(DATE_SELECT_FORMAT_DM);
                Date date = (Date)formatter.parse(dateConvert);
                long output=date.getTime()/1000L;
                String str=Long.toString(output);
                return Long.parseLong(str) * 1000;
            }catch (Exception f)
            {
                f.getMessage();
                return 0L;
            }
        }
    }

    /**
     * Method: ????y l?? ph????ng th???c convert date to millisecond.
     * @param dateConvert
     * @return
     */
    public static Long convertDateToSecond(String dateConvert, String dateFortMat)
    {
        try {
            DateFormat formatter = new SimpleDateFormat(dateFortMat);
            Date date = (Date)formatter.parse(dateConvert);
            long output=date.getTime()/1000L;
            String str=Long.toString(output);
            return Long.parseLong(str) * 1000;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0L;
        }
    }

    public static String convertLongToDate(long dateLong)
    {

        Date date = new Date(dateLong);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATE_SELECT_FORMAT);
        return format.format(date);
    }

    /**
     * ????y l?? ph????ng th???c gi??p l???y ra gi?? tr??? millisecond ngay th???i ??i???m hi???n t???i
     * @return long minllisecond
     */
    public static Long getMillisecondNow()
    {
        return System.currentTimeMillis();
    }

    /**
     * Format date conversion method.
     * @param date is the type of date you put in.
     * @param DateFormat is the type of date you want to receive back.
     * @return The DateFormat is the converted date value.
     */
    @SuppressLint("SimpleDateFormat")
    public static String formatDateStart(String date, String DateFormat, boolean checkStart) throws ParseException {
        Date initDate;
        SimpleDateFormat formatter;
        if(checkStart){
            initDate = new SimpleDateFormat(DateFormat).parse(date);
           formatter = new SimpleDateFormat(DEFAULT_DATE_SELECT_FORMAT);
        }else {
            initDate = new SimpleDateFormat(DEFAULT_DATE_SELECT_FORMAT).parse(date);
            formatter = new SimpleDateFormat(DateFormat);
        }

        return formatter.format(initDate);
    }


    /**
     * Format date conversion method.
     * @param date is the type of date you put in.
     * @return The DateFormat is the converted date value.
     */
    @SuppressLint("SimpleDateFormat")
    public static String formatDateStartEnd(String date, String startFormat, String endFormat) throws ParseException {
        Date initDate;
        SimpleDateFormat formatter;
        initDate = new SimpleDateFormat(startFormat).parse(date);
        formatter = new SimpleDateFormat(endFormat);

        return formatter.format(initDate);
    }

    /**
     * Format date conversion method.
     * @param date is the type of date you put in.
     * @return endDateFormat
     */
    public static String formatDate(String date) throws ParseException {
        @SuppressLint("SimpleDateFormat") Date initDate = new SimpleDateFormat(DEFAULT_DATE_SELECT_FORMAT).parse(date);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat(DEFAULT_DATE_SELECT_FORMAT);
        return formatter.format(initDate);
    }

    public static String getColoredSpanned(String text, String color) {
        return "<font color=" + color + ">" + text + "</font>";
    }

    /**
     * Method: getMoneyFromLottery ????y l?? ph????ng th???c l???y ra kho???ng ti???n tr??ng th?????ng c???a t???ng gi???i th?????ng.
     * @param prize
     * @return
     */
    public static String getMoneyFromLotteryMN(String prize)
    {
        Log.d("gii", "prize =" + prize);
        switch (prize)
        {

            /**
             *    public static final String KK = "6.000.000";
             *     public static final String PDB = "50.000.000";
             *     public static final String G1 = "30.000.000";
             *     public static final String G2 = "15.000.000";
             *     public static final String G3 = "10.000.000";
             *     public static final String G4 = "3.000.000";
             *     public static final String G5 = "1.000.000";
             *     public static final String G6 = "400.000";
             *     public static final String G7 = "200.000";
             *     public static final String G8 = "100.000";
             *
             *     public static final String KHUYEN_KHICH = "KK";
             *     public static final String PHU_DAC_BIET = "PDB";
             *     public static final String GIAI_1 = "G1";
             *     public static final String GIAI_2 = "G2";
             *     public static final String GIAI_3 = "G3";
             *     public static final String GIAI_4 = "G4";
             *     public static final String GIAI_5 = "G5";
             *     public static final String GIAI_6 = "G6";
             *     public static final String GIAI_7 = "G7";
             *     public static final String GIAI_8 = "G8";
             */
            case KHUYEN_KHICH_MN:
            {
                return KK_MN;
            }
            case PHU_DAC_BIET:
            {
                return PDB;
            }
            case DAC_BIET:
            {
                return DB;
            }
            case GIAI_1:
            {
                return G1;
            }
            case GIAI_2:
            {
                return G2;
            }
            case GIAI_3:
            {
                return G3;
            }
            case GIAI_4:
            {
                return G4;
            }
            case GIAI_5:
            {
                return G5;
            }
            case GIAI_6:
            {
                return G6;
            }
            case GIAI_7:
            {
                return G7;
            }
            default:{
                return G8;
            }
        }

    }


    /**
     * Method: getMoneyFromLottery ????y l?? ph????ng th???c l???y ra kho???ng ti???n tr??ng th?????ng c???a t???ng gi???i th?????ng.
     * @param prize
     * @return
     */
    public static String getMoneyFromLotteryMB(String prize)
    {
        Log.d("gii", "prize =" + prize);
        switch (prize)
        {

            /**
             public static final String PDB_MB = "20.000.00??";
             public static final String DB_MB = "1.000.000.000??";
             public static final String G1_MB = "10.000.000??";
             public static final String G2_MB = "5.000.000??";
             public static final String G3_MB = "1.000.000??";
             public static final String G4_MB = "400.000??";
             public static final String G5_MB = "200.000??";
             public static final String G6_MB = "100.000??";
             public static final String G7_MB = "40.000??";
             *
             *     public static final String KHUYEN_KHICH = "KK";
             *     public static final String PHU_DAC_BIET = "PDB";
             *     public static final String GIAI_1 = "G1";
             *     public static final String GIAI_2 = "G2";
             *     public static final String GIAI_3 = "G3";
             *     public static final String GIAI_4 = "G4";
             *     public static final String GIAI_5 = "G5";
             *     public static final String GIAI_6 = "G6";
             *     public static final String GIAI_7 = "G7";
             *     public static final String GIAI_8 = "G8";
             */

            case KHUYEN_KHICH_MB:
            {
                return KK_MB;
            }
            case PHU_DAC_BIET:
            {
                return PDB_MB;
            }
            case DAC_BIET:
            {
                return DB_MB;
            }
            case GIAI_1:
            {
                return G1_MB;
            }
            case GIAI_2:
            {
                return G2_MB;
            }
            case GIAI_3:
            {
                return G3_MB;
            }
            case GIAI_4:
            {
                return G4_MB;
            }
            case GIAI_5:
            {
                return G5_MB;
            }
            case GIAI_6:
            {
                return G6_MB;
            }
            default:{
                return G7_MB;
            }
        }

    }

    /**
     * Method: setEditTextMaxLength ????y l?? ph????ng th???c set ????? d??i t???i ??a c???a k?? t??? nh???p v??o EditText
     * @param et
     * @param maxLength
     */
    public static void setEditTextMaxLength(EditText et, int maxLength) {
        et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
    }


    /**
     * setSharedPreferLong This is the method to set a Long value from the given name
     */
    public static void setSharedPreference(Context context, String name, String value)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(SETTINGS, MODE_PRIVATE).edit();
        editor.putString(name, value);
        editor.apply();
        editor.commit();
    }

    /**
     * getSharedPreferLong is the method to get the value stored in SharedPrefer by the given name with type Long.
     */
    public static String getSharedPreference(Context context,String name)
    {
        SharedPreferences preferences = context.getSharedPreferences(SETTINGS, MODE_PRIVATE);
        return preferences.getString(name, "");
    }

    /**
     * getDayOfMonth ????y l?? ph????ng th???c gi??p chuy???n ?????i t??? ng??y sang th???.
     * @param date "20-10-2021" ng??y mu???n chuy???n ?????i sang th???
     * @return int tr??? v??? th??? hi???n t???i m?? ng?????i d??ng l???a ch???n 1,2,3,4,5,6,7; 1: ch??? nh???t; 2: th??? 2
     */
    public static int getDayOfMonthNumber(String date)
    {
        int dayOfWeek = 0;
        try {
            Date initDate = new SimpleDateFormat(DEFAULT_DATE_SELECT_FORMAT).parse(date);
            Calendar c = Calendar.getInstance();
            c.setTime(initDate); // yourdate is an object of type Date
            dayOfWeek = c.get(Calendar.DAY_OF_WEEK); // this will for example return 3 for tuesday

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dayOfWeek;
    }

    /**
     * getDayOfMonth ????y l?? ph????ng th???c gi??p chuy???n ?????i t??? ng??y sang th???.
     * @param date "20-10-2021" ng??y mu???n chuy???n ?????i sang th???
     * @return int tr??? v??? th??? hi???n t???i m?? ng?????i d??ng l???a ch???n 1,2,3,4,5,6,7; 1: ch??? nh???t; 2: th??? 2
     */
    public static String getDayOfMonth(String date, boolean checkDay)
    {
        String day ="";
        int dayOfWeek = 0;
        try {
            Date initDate = new SimpleDateFormat(DEFAULT_DATE_SELECT_FORMAT).parse(date);
            Calendar c = Calendar.getInstance();
            c.setTime(initDate); // yourdate is an object of type Date
            dayOfWeek = c.get(Calendar.DAY_OF_WEEK); // this will for example return 3 for tuesday
            if(dayOfWeek == 1)
            {
                if(!checkDay)
                    day = "Chu Nhat"; // chuy???n ?????i th??nh th???
                else
                    day = "CN"; // chuy???n ?????i th??nh th???
            }
            else {

                if(!checkDay)
                    day = "Thu " + dayOfWeek; // chuy???n ?????i th??nh th???
                else
                    day = "T" + dayOfWeek;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return day;
    }

    public static String getDayNow(String date)
    {
        String day ="";
        int dayOfWeek = 0;
        try {
            Date initDate = new SimpleDateFormat(DEFAULT_DATE_SELECT_FORMAT).parse(date);
            Calendar c = Calendar.getInstance();
            c.setTime(initDate); // yourdate is an object of type Date
            dayOfWeek = c.get(Calendar.DAY_OF_WEEK); // this will for example return 3 for tuesday
            if(dayOfWeek == 1)
            {
                day = "Chu Nhat"; // chuy???n ?????i th??nh th???
            }
            else {
                day = "Thu " + dayOfWeek;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return day;
    }

    /**
     * method minus two days d??y l?? ph????ng th???c tr??? ng??y truy???n v??o v?? ng??y hi???n t???i s??? tr??? ra miliseccon
     * @param stringExpiry is the expiration date of the device
     *
     * @return sub_Date
     */
    @SuppressLint("SimpleDateFormat")
    public static long subDate(String stringExpiry) {

        Date d1 = null;
        try {
            d1 = new SimpleDateFormat(DEFAULT_DATE_SELECT_FORMAT).parse(stringExpiry);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        /* Today's date */
        Date today = new Date();
        return today.getTime() - d1.getTime();
    }

    /**
     * The getSubDate method is used to sub date
     */
    public static String getSubDate(int numberDate) {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -numberDate);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATE_SELECT_FORMAT);
        return format.format(cal.getTime());
    }

    /**
     * The isConnected method is used to check the internet.
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }


    /**
     * The alertDialog method is used to show sample AlertDialog has 3 parameter values ??????for other classes to reuse
     */
    public static void alertDialog(final Activity context, String title, String message) {


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //
                dialogInterface.dismiss();
                context.finish();
            }
        });
        // create alert dialog
        alertDialog = builder.create();
        alertDialog.show();

        // show it
        //alertDialog.show();  //--->leak window error
        //builder.show();
    }


    /**
     * checkDomain ????y l?? ph????ng th???c l???y ra t??n mi???n x??? s??? c???a t??? v?? s??? ng?????i d??ng ??ang d??n
     */
    public static String getDomain(int domainLottery) {

        if(domainLottery == KET_QUA_MIEN_BAC)
        {
            return MIEN_BAC;
        }
        else if(domainLottery == KET_QUA_MIEN_TRUNG)
        {
            return MIEN_TRUNG;
        }
        else if(domainLottery == KET_QUA_MIEN_NAM)
        {
            return MIEN_NAM;
        }
        else if(domainLottery == KET_QUA_VIETLOTT_6_45)
        {
            return VIETLOTT_6_45;
        }else { //KET_QUA_VIETLOTT_6_55
            return VIETLOTT_6_55;
        }
    }

    /**
     * getTextNormalizer l?? ph????ng th???c chuy???n k?? t??? c?? d???u th??nh kh??ng d???u.
     * @param text ??o???n text c?? d???u
     * @return ??o???n text kh??ng d???u
     */
    public static String getTextNormalizer(String text)
    {
        text = text.toLowerCase().replace("??","d");
        text = Normalizer.normalize(text, Normalizer.Form.NFD);
        text = text.replaceAll("[^\\p{ASCII}]", "");
        Log.d("TEN_DAI_FULL","textGetMaDai = " + text);
        return text;
    }

    /**
     * changePosition ????y l?? ph????ng th???c ?????o chu???i t??? ph??a sau ra tr?????c
     * @param text 123456
     * @return 654321
     */
    public static String changePosition(String text)
    {
        StringBuilder textChange = new StringBuilder();
        for(int i=(text.length()-1); i>=0; i--)
        {
            textChange.append(text.charAt(i));
        }
        return textChange.toString();
    }
}
