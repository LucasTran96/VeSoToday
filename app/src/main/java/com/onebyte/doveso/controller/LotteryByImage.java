package com.onebyte.doveso.controller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;
import com.onebyte.doveso.R;
import com.onebyte.doveso.adapter.AdapterResultsLottery;
import com.onebyte.doveso.temporaryfiledbmanager.TemporaryFileDBLotterySchedule;
import com.r0adkll.slidr.Slidr;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;
import static com.onebyte.doveso.api.ApiMethod.convertDateToMillisecond;
import static com.onebyte.doveso.api.ApiMethod.formatDate;
import static com.onebyte.doveso.api.ApiMethod.formatDateStart;
import static com.onebyte.doveso.api.ApiMethod.getDateNow;
import static com.onebyte.doveso.api.ApiMethod.getDayOfMonth;
import static com.onebyte.doveso.api.ApiMethod.getSubDate;
import static com.onebyte.doveso.api.ApiMethod.getTextNormalizer;
import static com.onebyte.doveso.contants.Global.DEFAULT_DATE_M;
import static com.onebyte.doveso.contants.Global.DEFAULT_DATE_SELECT_FORMAT;
import static com.onebyte.doveso.contants.Global.DEFAULT_YEAR;
import static com.onebyte.doveso.contants.Global.MAXDATE;
import static com.onebyte.doveso.contants.Global.MAXMONTH;
import static com.onebyte.doveso.contants.Global.MA_DAI;
import static com.onebyte.doveso.contants.Global.MIEN_BAC;
import static com.onebyte.doveso.contants.Global.ONEDAY;
import static com.onebyte.doveso.contants.Global.SELECT_PROVINCE;
import static com.onebyte.doveso.contants.Global.TAI_CHINH;
import static com.onebyte.doveso.contants.Global.TEN_DAI;
import static com.onebyte.doveso.contants.Global.TEN_DAI_FULL;
import static com.onebyte.doveso.controller.ResultsLottery.checkActionResults;
import static com.onebyte.doveso.controller.ResultsLottery.checkDateDaiXoSo;
import static com.onebyte.doveso.controller.ResultsLottery.checkDomain;
import static com.onebyte.doveso.controller.ResultsLottery.checkLotteryFromDateSelected;
import static com.onebyte.doveso.controller.ResultsLottery.checkValidInputLottery;
import static com.onebyte.doveso.controller.ResultsLottery.detectingLotteryTickets;
import static com.onebyte.doveso.controller.ResultsLottery.getDomainLottery;
import static com.onebyte.doveso.controller.ResultsLottery.traditionalLotteryListResults;//
import static com.onebyte.doveso.controller.ResultsLottery.prizeResultsListMB_Province;
import static com.onebyte.doveso.controller.TakeAPicture.EXTRA_DATA;
import static com.onebyte.doveso.controller.TakeAPicture.SAVED;
import static com.onebyte.doveso.controller.TakeAPicture.currentPhotoPath;

public class LotteryByImage extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView img_Image_Lottery;
    private TextView txt_Truy_Van_Do_Ve_So;
    private static int REQUEST_CODE_TAKE_IMAGE = 200;
    private static String defaultYear;
    private LinearLayout lnl_Lottery, lnl_Results_Lottery, lnl_Guide_Image_Lottery;
    private TextView txt_DatePicker, txt_Province_Lottery;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private Spinner spn_Name_Lottery;
    private EditText edt_Code_Lottery;
    private Button btn_Results, btn_Help_Desk;
    public static Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
    public String lastSelectedDay;
    private String domainLottery;
    int checkClickSpinner = 0;
    private boolean btn_Help_Desk_Is_Click; // ????y l?? gi?? tr??? ????? ki???m tra xem btn_Help_Desk ???? ???????c click hay ch??a click
    private final int RQ_CAMERA_PERMISSION = 200;
    // checkValidLottery ????y l?? gi?? tr??? ????? ki???m tra xem c?? ????? ??i???u ki???n ????? d?? v?? s??? hay ch??a
    // n???u checkValidLottery = 3 th?? xem nh?? ????? ??i???u ki???n d?? v?? s???.
    private boolean checkValidLottery = false;
    private boolean provinceNow;
    private List<String> dateSelect;
    private  boolean checkSaveImage; // ????y l?? gi?? tr??? bi???n l??u tr??? tr???ng th??i h??nh ???nh v???a ch???p c?? ???????c l??u th??nh c??ng hay kh??ng.

    // x??? l?? check l???y ng??y, ????i, m?? v?? s???
    String dateHere = "";
    boolean checkNotDate = false;
    boolean checkCodeLottery = false;
    boolean checkDaiXoSo = false;
    boolean checkAllowPermission = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottery_by_camera);
        setID();
        setEvent();
        Slidr.attach(this);
        //getIntentFromTakeAPicture();
    }

    /**
     * getIntentFromTakeAPicture ????y l?? ph????ng th???c nh???n d??? li???u Intent t??? class TakeAPicture
     * N?? s??? nh???n ???????c gi?? tr??? Saved ho???c Failed;
     * Sau ???? ti???p t???c x??? l?? gi???i m?? t??? h??nh ???nh sang ki???u text ????? d?? v?? s???.
     */
   /* private void getIntentFromTakeAPicture()
    {
        String getResultIntent = getIntent().getStringExtra(EXTRA_DATA);
        assert getResultIntent != null;
        if(getResultIntent.equals(SAVED))
            handleReadImageToText(Activity.RESULT_OK, null, true);
        else
            handleReadImageToText(Activity.RESULT_OK, null, false);
    }*/

    private void setID() {

        toolbar = findViewById(R.id.toolbar_Lottery_By_Voice);
        toolbar.setTitle(getApplicationContext().getResources().getString(R.string.app_name_alias));

        // khai b??o id cho c??c ?? hi???n th??? d??? li???u nh???p v??o khi d?? v?? s???
        lnl_Lottery = findViewById(R.id.lnl_Lottery);
        lnl_Results_Lottery = findViewById(R.id.lnl_Results_Lottery);
        //lnl_Ket_Qua_Test.setVisibility(View.GONE);
        lnl_Guide_Image_Lottery = findViewById(R.id.lnl_Guide_Image_Lottery);
        txt_DatePicker = findViewById(R.id.txt_DatePicker);
        txt_Province_Lottery = findViewById(R.id.txt_Province_Lottery);

        prizeResultsListMB_Province = new ArrayList<>();

        // mRecyclerView hi???n th??? k???t qu??? x??? s??? khi ng?????i d??ng b???m d?? v?? s???.
        mRecyclerView = findViewById(R.id.rcv_Results_Lottery_Top);
        mRecyclerView.setHasFixedSize(true);

        //spn_Name_Lottery ????y l?? Spinner ????? ng?????i d??ng ch???n ????i c???n d??. N?? ???????c ?????ng b??? v???i ng??y m?? ng?????i d??ng ch???n d?? v?? s???.
        spn_Name_Lottery = findViewById(R.id.spn_Name_Lottery);
        edt_Code_Lottery = findViewById(R.id.edt_Code_Lottery);
        btn_Results = findViewById(R.id.btn_Results);
        btn_Help_Desk = findViewById(R.id.btn_Help_Desk);
        btn_Help_Desk.setVisibility(View.VISIBLE);
        lnl_Guide_Image_Lottery.setVisibility(View.VISIBLE); // khi n??o ng?????i d??ng b???m v??o btn_Help_Desk th?? s??? hi???n ra h?????ng d???n n??y.

        lnl_Lottery.setVisibility(View.GONE);
        lnl_Results_Lottery.setVisibility(View.GONE);
        img_Image_Lottery = findViewById(R.id.img_Image_Lottery);
        txt_Truy_Van_Do_Ve_So = findViewById(R.id.txt_Truy_Van_Do_Ve_So_Image);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        defaultYear = getDateNow(DEFAULT_YEAR);

        lastSelectedDay = checkDateDaiXoSo();
        setSpinnerWithDay(checkDateDaiXoSo());
        txt_Province_Lottery.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.custom_edt_date));

        AdRequest adRequest = new AdRequest.Builder().build();
        // AdSize adSize = new AdSize(320, 100);
        AdView mAdView = findViewById(R.id.adView_Image);
        mAdView.loadAd(adRequest);

        checkPermission();
    }

    /**
     * checkPermission l?? ph????ng th???c ki???m tra ng?????i d??ng ???? ch???p nh???n quy???n ch???p ???nh hay ch??a
     */
    private void checkPermission()
    {
        //Check realtime permission if run higher API 23
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this,new String[]
                        {
                                Manifest.permission.CAMERA},RQ_CAMERA_PERMISSION);
                checkAllowPermission = false;
            }else {
                checkAllowPermission = true;
            }
        }
        else {
            checkAllowPermission = true;
        }
    }

    private void setEvent() {

        edt_Code_Lottery.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // do something, e.g. set your TextView here via .setText()
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        img_Image_Lottery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkAllowPermission)
                {
                    Log.d("RQ_CAMERA_PERMISSION", "???? ch???p nh???n quy???n d?? v?? s???.");
                    txt_DatePicker.setText("");
                    edt_Code_Lottery.setText("");
                    edt_Code_Lottery.setVisibility(View.VISIBLE);
                    txt_Province_Lottery.setText("");
                    lnl_Guide_Image_Lottery.setVisibility(View.GONE);
                    Intent featureIntent = new Intent(LotteryByImage.this, TakeAPicture.class);
                    startActivityForResult(featureIntent, REQUEST_CODE_TAKE_IMAGE);
                    //startActivity(featureIntent);
                    //finish();
                }else {
                    Log.d("RQ_CAMERA_PERMISSION", "B???n vui l??ng ch???p nh???n quy???n ????? c?? th??? ch???p ???nh t??? v?? s???!");
                    Toast.makeText(LotteryByImage.this, "B???n vui l??ng ch???p nh???n quy???n ????? c?? th??? ch???p ???nh t??? v?? s???!", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(LotteryByImage.this,new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },RQ_CAMERA_PERMISSION);
                }

            }
        });

        btn_Help_Desk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!btn_Help_Desk_Is_Click)
                {
                    lnl_Guide_Image_Lottery.setVisibility(View.VISIBLE);
                    btn_Help_Desk_Is_Click = true;
                }else {
                    lnl_Guide_Image_Lottery.setVisibility(View.GONE);
                    btn_Help_Desk_Is_Click = false;
                }
            }
        });

        txt_DatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(txt_DatePicker);
            }
        });

        btn_Results.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnl_Guide_Image_Lottery.setVisibility(View.GONE);
                lnl_Results_Lottery.setVisibility(View.VISIBLE);
                handleDoVeSo(edt_Code_Lottery.getText().toString(), txt_DatePicker.getText().toString(), txt_Province_Lottery.getText().toString());
            }
        });
    }

    /**
     * handleDoVeSo Khi ???? c?? ?????y ????? th??ng tin c???a t??? v?? s??? th?? s??? b???t ?????u x??? l?? d?? v?? s??? v?? l???y k???t qu??? hi???n th??? cho ng?????i d??ng.
     * @param edt_Code_Lottery
     * @param txt_DatePicker
     * @param txt_Province_Lottery
     */
    private void handleDoVeSo(String edt_Code_Lottery, String txt_DatePicker, String txt_Province_Lottery) {

        traditionalLotteryListResults = new ArrayList<>();
        mAdapter = new AdapterResultsLottery(LotteryByImage.this, traditionalLotteryListResults);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        provinceNow = false;

        // l???y m?? s???; l???y ????i x??? s???; l???y ng??y x??? s???.
        String dateLottery = null;
        try  {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.getMessage();
        }

        try {

            dateLottery = formatDate(txt_DatePicker);
            Log.d("getSubdate", "dateLottery = "+ dateLottery);
            if(!txt_Province_Lottery.isEmpty())
            {
                domainLottery = txt_Province_Lottery;

                if(domainLottery.equals(MIEN_BAC))
                    domainLottery = "H?? N???i"; // khi m??y ???nh qu??t t??? v?? s??? th?? ch??? l???y ra ???????c Mi???n B???c ch??? kh??ng l???y ???????c t??n ????i n??n d??ng ?????i di???n ????i H?? N???i ????? th??? hi???n mi???n b???c.
            }
            lnl_Lottery.setVisibility(View.VISIBLE);
            boolean checkValidInput = checkValidInputLottery(edt_Code_Lottery, domainLottery, getApplicationContext(), txt_Truy_Van_Do_Ve_So);
            Log.d("CheckHienThi", "checkValidInput = " + checkValidInput + " domainLottery = "+ domainLottery);

            if(checkValidDateLottery(dateLottery, txt_Truy_Van_Do_Ve_So, getApplicationContext()))
            {
                // x??? l?? tr??? v??? k??? qu??? khi th??ng tin v?? s??? ???? ???????c x??c th???c.
                checkValidInputIsTrue(checkValidInput, dateLottery, edt_Code_Lottery);
            }


        }catch (Exception e)
        {
            e.getMessage();
        }
    }

    @SuppressLint("SetTextI18n")
    public static boolean checkValidDateLottery(String dateLottery, TextView txt_Truy_Van_Do_Ve_So, Context context)
    {
         // check n???u qu?? ng??y th?? kh??ng d?? v?? s???
        boolean checkQuaHanDoVeSo = checkValidDateLottery(dateLottery, true);
        Log.d("CheckHienThi", "checkQuaHanDoVeSo = "+ checkQuaHanDoVeSo);
        if(!checkQuaHanDoVeSo) // ki???m tra ???? qu?? h???n d?? v?? s??? hay ch??a
        {
            txt_Truy_Van_Do_Ve_So.setVisibility(View.VISIBLE);
            txt_Truy_Van_Do_Ve_So.setText("V?? s??? c???a b???n ???? h???t h???n l??nh th?????ng. V?? ???? qu?? 30 ng??y k??? t??? ng??y x??? s???");
            Log.d("CheckHienThi", "V?? s??? c???a b???n ???? h???t h???n l??nh th?????ng. V?? ???? qu?? 30 ng??y k??? t??? ng??y x??? s???");
            txt_Truy_Van_Do_Ve_So.setTextColor(context.getResources().getColor(R.color.red));
            return false;
        }
        else {
            boolean checkVeSoChuaXo = checkValidDateLottery(dateLottery, false);
            /**
             * check n???u ch??a t???i ng??y x??? s??? th?? kh??ng cho h??nh ?????ng d?? v?? s???
             */
            if(!checkVeSoChuaXo) // ki???m tra ???? qu?? h???n d?? v?? s??? hay ch??a
            {
                txt_Truy_Van_Do_Ve_So.setVisibility(View.VISIBLE);
                txt_Truy_Van_Do_Ve_So.setText("V?? s??? c???a b???n ch??a t???i ng??y x??? s???!");
                txt_Truy_Van_Do_Ve_So.setTextColor(context.getResources().getColor(R.color.red));
                return false;
            }
        }
        return true;
    }
    /**
     * checkValidInputIsTrue Sau khi ???? check valid th??ng tin c???a t??? v?? s??? th?? x??? l?? d?? v?? s??? v?? hi???n th??? k???t qu???.
     * @param checkValidInput th??ng tin c???a t??? v?? s??? ???? ?????y ????? hay ch??a
     * @param dateLottery ng??y c???a t??? v?? s??? c???n d??
     */
    private void checkValidInputIsTrue(boolean checkValidInput, String dateLottery, String edt_Code_Lottery) {

        if(checkValidInput)
        {
            String getDomain = (getDomainLottery(domainLottery, getApplicationContext()).isEmpty())?"Kh??ng x??c ?????nh":getDomainLottery(domainLottery, getApplicationContext());

            int getDomainVerify = checkDomain(domainLottery, getApplicationContext());

            if(checkActionResults(dateLottery, getDomainVerify) && checkLotteryFromDateSelected(domainLottery, dateLottery, getApplicationContext()))
            {
                Log.d("CheckHienThi", "checkActionResults = " + checkActionResults(dateLottery, getDomainVerify));
                // provinceNow: x??? l?? l??u t??n t???nh m?? ng?????i d??ng v???a t??m ki???m k???t qu??? x??? s???.
                provinceNow = true;

                Log.d("ve_so_chua_xo",  checkActionResults(dateLottery, getDomainVerify) + "1111");
                Log.d("ve_so_chua_xo",  domainLottery + getDomain + dateLottery);
                txt_Truy_Van_Do_Ve_So.setVisibility(View.VISIBLE);
                //<string name="truy_van_do_ve_so_hom_nay">B???n ???? truy v???n d?? k???t qu??? v?? s??? %s - X??? s??? %s\nLo???i v?? %s ch??? s??? m???nh gi?? 10,000 ??, m??? th?????ng ng??y %s.\nD??y s??? c???a b???n l??: %s</string>
                txt_Truy_Van_Do_Ve_So.setText(getString(R.string.truy_van_do_ve_so_hom_nay,domainLottery, getDomain, String.valueOf(edt_Code_Lottery.length()), dateLottery, edt_Code_Lottery));
                txt_Truy_Van_Do_Ve_So.setTextColor(getResources().getColor(R.color.black));
                Log.d("checkDomain", " codeLottery = "+ edt_Code_Lottery + " dateLottery = "+ dateLottery +" domainLottery = " + domainLottery);

                    /*
                        Ki???m tra xem c?? ph???i mi???n b???c kh??ng n???u l?? ????i mi???n b???c th?? kh??ng c???n d?? th??m ng??y.
                     */
                if(getDomainVerify == 0)
                    detectingLotteryTickets(domainLottery, dateLottery, edt_Code_Lottery, LotteryByImage.this, mRecyclerView, true);
                else
                    detectingLotteryTickets(domainLottery, dateLottery, edt_Code_Lottery, LotteryByImage.this, mRecyclerView, false);
                //img_Closes_Code.setVisibility(View.VISIBLE);
            }else {
                Log.d("ve_so_chua_xo",  domainLottery + getDomain + dateLottery); //K???t qu??? x??? s??? % - X??? s??? % m??? th?????ng ng??y % hi???n ch??a c?? tr??n h??? th???ng vui l??ng b???m ????y ch??? m??? th?????ng.
                txt_Truy_Van_Do_Ve_So.setVisibility(View.VISIBLE);
                txt_Truy_Van_Do_Ve_So.setText(getString(R.string.ve_so_chua_xo, domainLottery, getDomain, dateLottery));
                txt_Truy_Van_Do_Ve_So.setTextColor(getResources().getColor(R.color.bgLogo));
                //05-05-2021
                Log.d("checkDomain", " codeLottery = "+ edt_Code_Lottery + " dateLottery = "+ dateLottery +" domainLottery = " + domainLottery);
            }
        }
//        else {
//           //lnl_Guide_Image_Lottery.setVisibility(View.VISIBLE);
//        }
    }

    /**
        checkValidDateLottery ????y l?? ph????ng th???c ki???m tra ng??y c???a t??? v?? s??? ???? qu?? h???n d?? v?? s??? hay ch??a.
        @param  checkQuaHan l?? gi?? tr??? ki???m tra ng??y c?? qu?? h???n n???u = true ho???c ki???m tra ng??y ch??a c?? k???t qu??? d??
     */
    public static boolean checkValidDateLottery(String dateLottery, boolean checkQuaHan) {

        if(checkQuaHan)
        {
            String getSubdate = getSubDate(30);
            Log.d("getSubdate", "getSubdate = "+ getSubdate);
            long date30 = convertDateToMillisecond(getSubdate);
            long dateLotteryLong =   convertDateToMillisecond(dateLottery);
            Log.d("getSubdate", "dateLotteryLong = "+ dateLotteryLong + "  date30= "+ date30);
            if(dateLotteryLong >= date30)
                return true;
            else
                return false;
        }
        else {
            long dateNow = convertDateToMillisecond(getDateNow(DEFAULT_DATE_SELECT_FORMAT));
            long dateLotteryLong =   convertDateToMillisecond(dateLottery);
            Log.d("getSubdate", "dateLotteryLong = "+ dateLotteryLong + "  dateNow= "+ dateNow);
            if(dateLotteryLong <= dateNow)
                return true;
            else
                return false;
        }
    }

    /**
     * showDateDialog ????y l?? ph????ng th???c hi???n th??? Dialog c???a datetime picker. v?? ki???m tra xem ng?????i d??ng c?? ch???n ng??y kh??c v???i ng??y hi???n t???i hay kh??ng?
     * @param txt_datePicker_Here truy???n v??o ng??y th??ng m?? ng?????i d??ng ch???n ????? d?? k???t qu??? x??? s???.
     */
    private void showDateDialog(final TextView txt_datePicker_Here) {
        final Calendar calendar = Calendar.getInstance();
        final String dateNow = txt_datePicker_Here.getText().toString();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
            {
                try {
                    lastSelectedDay = formatDate((dayOfMonth + "-" + (month +1) + "-" + year)); // do DatePickerDialog l???y ra th??ng t??? 0 -> 11 n??n s??? c???ng th??m 1 cho th??ng.
                    //Toast.makeText(LotteryByImage.this, lastSelectedDay, Toast.LENGTH_SHORT).show();
                    txt_datePicker_Here.setText(lastSelectedDay);
                    //txt_DatePicker.setText(lastSelectedDay);
                    // x??? l?? khi ng?????i d??ng ch???n ng??y th?? ?????i l???i
                    setSpinnerWithDay(lastSelectedDay);

                    txt_Province_Lottery.setText("");
                    txt_Province_Lottery.setVisibility(View.GONE);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };
        //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-10000);
        DatePickerDialog dialog = new DatePickerDialog(LotteryByImage.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - (ONEDAY*30));
        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dialog.show();


    }

    /**
     * setSpinnerWithDay th??m c??c t???nh v??o Spinner v?? c???p nh???t l???i danh s??ch c??c t???nh khi c?? thay ?????i day.
     * @param day
     */
    private void setSpinnerWithDay(String day)
    {
        getProvinceLottery(day);
        Log.d("checkGetCodeqa", "dateSelect = "+ dateSelect);
        if(dateSelect!= null && dateSelect.size()>0)
        {
            setSpinner(dateSelect);
        }
        else {
            //Toast.makeText(LotteryByImage.this, "L???i hi???n th??? t??n c??c t???nh!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * setSpinner l?? ph????ng th???c ki???m tra s??? thay ?????i c???a Spinner khi ng?????i d??ng b???m l???a ch???n item con trong Spinner
     * @param dateSelect
     */
    private void setSpinner(List<String> dateSelect) {


        // (@resource) android.R.layout.simple_spinner_item:
        //   The resource ID for a layout file containing a TextView to use when instantiating views.
        //    (Layout for one ROW of Spinner)
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                dateSelect);

        // Layout for All ROWs of Spinner.  (Optional for ArrayAdapter).
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        this.spn_Name_Lottery.setAdapter(adapter);

        // When user select a List-Item.
        this.spn_Name_Lottery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onItemSelectedHandler(parent, view, position, id);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Toast.makeText(getApplicationContext(), "onNothingSelected" ,Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * onItemSelectedHandler ????y l?? s??? ki???n thay ?????i t??n c???a item trong Spinner khi ng?????i d??ng ch???n l???a ch???n kh??c.
     * @param adapterView
     * @param view
     * @param position
     * @param id
     */
    private void onItemSelectedHandler(AdapterView<?> adapterView, View view, int position, long id) {
        Adapter adapter = adapterView.getAdapter();
        domainLottery = adapter.getItem(position).toString();
        if(checkClickSpinner == 0)
        {
            checkClickSpinner = 1;
        }else
        {
            txt_Province_Lottery.setText("");
            txt_Province_Lottery.setVisibility(View.GONE);
            //Toast.makeText(getApplicationContext(), "Selected Employee: " + domainLottery ,Toast.LENGTH_SHORT).show();
        }


        //Toast.makeText(getApplicationContext(), "Selected Employee: " + domainLottery ,Toast.LENGTH_SHORT).show();
    }

    /**
     * getProvinceLottery ????y l?? ph????ng th???c l???y ra danh s??ch c??c ????i x??? s??? theo th??? vd Thu 2: An Giang; B???c Li??u; V??nh Long.
     * @param date
     */
    private void getProvinceLottery(String date){

        dateSelect = new ArrayList<>();

        String aDay = getDayOfMonth(date, false);
        TemporaryFileDBLotterySchedule fileDBLotterySchedule = new TemporaryFileDBLotterySchedule(getApplicationContext());
        dateSelect = fileDBLotterySchedule.LotteryScheduleDBReadProvince(aDay, null);
        dateSelect.add(0, SELECT_PROVINCE);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    // Khi k???t qu??? ???????c tr??? v??? t??? Activity kh??c, h??m onActivityResult s??? ???????c g???i.
    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            // Ki???m tra requestCode c?? tr??ng v???i REQUEST_CODE v???a d??ng
            if(requestCode == REQUEST_CODE_TAKE_IMAGE) {
                if(resultCode == Activity.RESULT_OK)
                {
                    handleReadImageToText(resultCode, data, false);
                }
                else {
                    Toast.makeText(this, "Kh??ng l???y ???????c th??ng tin t??? t??? v?? s??? ???? ch???p!", Toast.LENGTH_SHORT).show();
                }
            }

        }catch (Exception e)
        {
            e.getMessage();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

            if (requestCode == RQ_CAMERA_PERMISSION) {
                if (grantResults.length != 1) {
                    Toast.makeText(getApplicationContext(), "B???n Ch??a ch???p nh???n quy???n ????? ch???p h??nh ???nh t??? v?? s???!", Toast.LENGTH_SHORT).show();
                    Log.d("RQ_CAMERA_PERMISSION", "Ch??a ch???p nh???n quy???n d?? v?? s???!");
                    checkAllowPermission = false;
                }
                else {
                    Log.d("RQ_CAMERA_PERMISSION", "???? ch???p nh???n quy???n d?? v?? s???.");
                    checkAllowPermission = true;
                }
            } else {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
    }

    /**
     * handleReadImageToText ki???m tra v?? check tr?????ng h???p c?? ?????y ????? ??i???u ki???n d?? v?? s??? ch??a
     * N???u ????? ??i???u ki???n d?? v?? s??? th?? s??? ti???n h??nh d?? v?? ????a ra k???t qu???
     * @param resultCode
     * @param data
     * @param isSaveImage
     */
    private void handleReadImageToText(int resultCode, Intent data, boolean isSaveImage)
    {
        // RESULT_OK ch??? ra r???ng k???t qu??? n??y ???? th??nh c??ng
        if(resultCode == Activity.RESULT_OK) {
            Log.d("eeee", " Activity.RESULT_OK");
            if(currentPhotoPath != null && (!currentPhotoPath.isEmpty()))
            {
                Log.d("eeee", currentPhotoPath);
                // Nh???n d??? li???u t??? Intent tr??? v???
                String result;
                if(data != null)
                {
                    result = data.getStringExtra(EXTRA_DATA);
                    assert result != null;
                }
                else {
                    result = "None";
                }

                if(result.equals(SAVED) || isSaveImage)
                {
                    Log.d("eeee",currentPhotoPath);
                    Bitmap myBitmap = BitmapFactory.decodeFile(currentPhotoPath);

                    if(myBitmap != null)
                    {
                        Matrix rotationMatrix = new Matrix();

                        Log.d("eeee","myBitmap.getWidth()  = " + myBitmap.getWidth()  + " myBitmap.getHeight() = " + myBitmap.getHeight());
                        if(myBitmap.getWidth() > myBitmap.getHeight())
                            rotationMatrix.postRotate(0);
                        else
                            rotationMatrix.postRotate(270);

                        Bitmap rotatedBitmap = Bitmap.createBitmap(myBitmap,0,0,myBitmap.getWidth(),myBitmap.getHeight(),rotationMatrix,true);
                        readTextFromImages(rotatedBitmap,txt_Truy_Van_Do_Ve_So);
                    }
                }else
                {
                    //Toast.makeText(this, "Result: " + result, Toast.LENGTH_LONG).show();
                    txt_Truy_Van_Do_Ve_So.setText(getResources().getString(R.string.khong_the_luu_hinh_anh));
                }
                // S??? d???ng k???t qu??? result b???ng c??ch hi???n Toast
                //Toast.makeText(this, "Result: " + result, Toast.LENGTH_LONG).show();
            }
        } else {
            // DetailActivity kh??ng th??nh c??ng, kh??ng c?? data tr??? v???.
            //Toast.makeText(this, "Result: " + getResources().getString(R.string.chua_chup_hinh_anh), Toast.LENGTH_LONG).show();
            txt_Truy_Van_Do_Ve_So.setText(getResources().getString(R.string.chua_chup_hinh_anh));
        }
    }

    private void readTextFromImages(Bitmap bitmap, final TextView textView){
        try {
            FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
            FirebaseVisionTextDetector firebaseVisionTextDetector = FirebaseVision.getInstance().getVisionTextDetector();
            firebaseVisionTextDetector.detectInImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                @Override
                public void onSuccess(FirebaseVisionText firebaseVisionText) {
                    textView.setText("");
                    displayTextFromImage(firebaseVisionText, textView);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("BUG", "L???i Detect image to text!" + e.getMessage());
                }
            });
        }catch (Exception e)
        {
            e.getMessage();
        }

    }

    /**
     * displayTextFromImage Khi ???? l???y ???????c text t??? FirebaseVisionText th?? b???t ?????u x??? l?? tri???m tra v?? hi???n th???
     * m?? v?? s???, ????i, ng??y x??? s??? v?? ?????y ????? d??? li???u th?? s??? d?? v?? s??? t??? ?????ng.
     * @param firebaseVisionText
     * @param textView
     */
    private void displayTextFromImage(FirebaseVisionText firebaseVisionText, TextView textView) {
        List<FirebaseVisionText.Block> firebaseVisionTextList = firebaseVisionText.getBlocks();

        if(firebaseVisionTextList.size() == 0)
        {
            Log.d("BUG", "Kh??ng t??m th???y ch??? trong h??nh ???nh!");
            //lnl_Lottery.setVisibility(View.VISIBLE);
            lnl_Lottery.setVisibility(View.GONE);
        }
        else {
            StringBuilder lotteryResultFromImage = new StringBuilder();
            for (FirebaseVisionText.Block block: firebaseVisionText.getBlocks())
            {
                String text = block.getText();
                Log.d("BUG", "text = " + text);
               // textView.append(text +"\n"); // nh??? s???a l???i ch??? n??y
                lotteryResultFromImage.append(text).append("\n");
            }
            //textView.setVisibility(View.GONE);
            lnl_Lottery.setVisibility(View.VISIBLE);
            // handleTextToLottery Khi ???? l???y ???????c d??? li???u text t??? h??nh ???nh th?? x??? l?? ki???m tra xem c?? ?????y ????? ng??y, m?? v?? s???, ????i x??? s??? hay ch??a
            handleTextToLottery(lotteryResultFromImage.toString());


        }
    }

    /**
     * handleTextToLottery Khi ???? l???y ???????c d??? li???u text t??? h??nh ???nh th?? x??? l?? ki???m tra xem c?? ?????y ????? ng??y, m?? v?? s???, ????i x??? s??? hay ch??a
     * n???u ????? ??i???u ki???n th?? ti???n h??nh d?? v?? s??? lu??n.
     * @param lotteryResultFromImage D??? li???u text l???y ???????c t??? h??nh ???nh
     */
    private void handleTextToLottery(String lotteryResultFromImage) {

        Log.d("lotteryResultFromImage", lotteryResultFromImage.toString());
        String [] splitResult = lotteryResultFromImage.toString().split("\n");
        Log.d("lotteryResultFromImage", splitResult.length+ " SIZE");
        checkCodeLottery = false;
        checkNotDate = false;
        checkDaiXoSo = false;
        dateHere = "";
        checkClickSpinner = 0;
        for(String text : splitResult)
        {
            Log.d("lotteryResultFromImages", "\t\t\t\t\t K???t qu??? = "+ text);
            /* X??? l?? l???y ra ng??y x??? s??? t??? h??nh ???nh ch???p t??? v?? s??? c???a ng?????i d??ng*/
            getDateLottery(text);
            // ki???m tra l???y ra m?? s??? v?? s??? t??? h??nh ???nh ng?????i d??ng ch???p
            getCodeLottery(text, checkNotDate);
            // ki???m tra l???y ra ????i x??? s??? ho???c mi???n x??? s???
            getProvince(text);
        }

        Log.d("TEN_DAz","checkDaiXoSo = " + checkDaiXoSo);
        if(checkDaiXoSo)
        {
            txt_Province_Lottery.setVisibility(View.VISIBLE);
           // checkClickSpinner = 1;
        }else
        {
            txt_Province_Lottery.setVisibility(View.GONE);
            checkClickSpinner = 1;
            //domainLottery = "Ch???n T???nh";
        }
        // ????y l?? ph????ng th???c ki???m tra xem ???? ????? ??i???u ki???n d?? v?? s??? ch??a
        // n???u ????? ??i???u ki???n th?? s??? ti???n h??nh d?? v?? s???
        // n???u ch??a ????? ??i???u ki???n th?? s??? ????? ng?????i d??ng ch???nh s???a v?? nh???p th??m cho ch??nh x??c.
        Log.d("checkGetCodeq", "getDateLottery = " + txt_DatePicker.getText().toString()
                + "\rgetCodeLottery = " + edt_Code_Lottery.getText().toString()
                + "\rgetDateLottery = " + txt_Province_Lottery.getText().toString());
        checkAndShowResult(checkCodeLottery, dateHere, checkDaiXoSo);
        //checkClickSpinner = 1;
    }

    /**
     * checkAndShowResult ????y l?? ph????ng th???c ki???m tra xem ???? ????? ??i???u ki???n d?? v?? s??? ch??a
     * n???u ????? ??i???u ki???n th?? s??? ti???n h??nh d?? v?? s???
     * n???u ch??a ????? ??i???u ki???n th?? s??? ????? ng?????i d??ng ch???nh s???a v?? nh???p th??m cho ch??nh x??c.
     * @param checkCodeLottery M?? c???a t??? v?? s??? ???? l???y ???????c hay kh??ng
     * @param dateHere Ng??y c???a t??? v?? s??? c?? l???y ???????c hay kh??ng
     * @param checkDaiXoSo ????i c???a t??? v?? s??? c?? l???y ???????c hay kh??ng
     */
    private void checkAndShowResult(boolean checkCodeLottery, String dateHere, boolean checkDaiXoSo) {
        if(checkCodeLottery && (!dateHere.isEmpty()) && checkDaiXoSo)
        {
            checkValidLottery = true;
            // ????? ??i???u ki???n ti???n h??nh d?? v?? s???
            Log.d("checkGetCode", "????? ??i???u ki???n ti???n h??nh d?? v?? s???");
            lnl_Results_Lottery.setVisibility(View.VISIBLE);
            txt_Truy_Van_Do_Ve_So.setVisibility(View.VISIBLE);
            txt_Province_Lottery.setVisibility(View.VISIBLE);
            Log.d("checkGetCodeq", "getDateLottery = " + txt_DatePicker.getText().toString()
                    + "getCodeLottery = " + edt_Code_Lottery.getText().toString()
                    + "getDateLottery = " + txt_Province_Lottery.getText().toString());

            handleDoVeSo(edt_Code_Lottery.getText().toString(), txt_DatePicker.getText().toString(), txt_Province_Lottery.getText().toString());
            setSpinnerWithDay(txt_DatePicker.getText().toString());
            //checkClickSpinner = 1;
        }else {
            //ch??a ?????y ????? ??i???u ki???n ????? d?? v?? s??? n??n ng?????i d??ng c???n nh???p th??m th??ng tin ????? b???t ?????u d?? v?? s???.
            Log.d("checkGetCode", "Kh??ng l???y ???????c m?? v?? s??? t??? h??nh ???nh t??? v?? s???");
            lnl_Results_Lottery.setVisibility(View.GONE);
            checkClickSpinner = 1;
        }
    }

    /**
     * getDateLottery ????y l?? ph????ng th???c t??m ki???m v?? l???y ra ng??y x??? s??? t??? ??o???n text ???????c ?????c ra t??? h??nh ???nh t??? v?? s???.
     * @param text ??o???n text ???????c ?????c ra t??? h??nh ???nh t??? v?? s???.
     */
    private void getDateLottery(String text) {
        /* X??? l?? l???y ra ng??y x??? s??? t??? h??nh ???nh ch???p t??? v?? s??? c???a ng?????i d??ng*/
        if(text.contains(defaultYear))
        {
            Log.d("checkGetCodeq", "\t\t\t\t\t Ch???a Ng??y = "+ text);
            int getYear = text.lastIndexOf(defaultYear); // n??m hi???n t???i m?? ng?????i d??ng ??ang s???ng
            String getBeforeYear = text.substring(0,(getYear+4));

            String dateTamp;
            if(getBeforeYear.contains("-") && (!getDateHCM(getBeforeYear).isEmpty()))
            {
                dateTamp = getDateHCM(getBeforeYear);
            }else {
                dateTamp = text.substring(0,(getYear));
            }
            Log.d("checkGetCodeq", "\t\t\t\t\t C???t ng??y ph??a tr?????c = "+ getBeforeYear);

            String getBeforeDayMonth = dateTamp
                    .replace("-","")
                    .replace("/","")
                    .replace(" ","")
                    .replace(":","")
                    .replace("*","");
            int viTriNumberBefore = 0;
            boolean checkDayMonth = true;
            String dayAndMonth ="";

            for (int i = (getBeforeDayMonth.length()-1); i>=0; i--)
            {
                        /*
                            N???u ???? c?? gi?? tr??? n??m m?? kh??ng ????ng format th?? ta ti???p t???c x??t xem c?? ph???i l???y ???????c ng??y th??ng n??m ??? d??ng n??y kh??ng.
                            Ki???m tra t??? sau v??? tr?????c ????? l???y ng??y v?? th??ng
                            -24-05
                            -2405
                            -24-5
                            -245
                            24 05
                            24 5
                            24:05
                            24:5
                            24*5
                            24*05
                            xo L07-03-2021
                         */
                if(getBeforeDayMonth.length() > 2)
                {
                    if(isNumeric(String.valueOf(getBeforeDayMonth.charAt(i))))
                    {
                        viTriNumberBefore += 1;
                        if(dayAndMonth.length()<=3)
                        {
                            dayAndMonth += getBeforeDayMonth.charAt(i);
                        }else {
                            // qu?? gi???i h???n k?? t??? ng??y
                        }
                        // ????y l?? k?? t??? s???
                        Log.d("KYTUSO", "2405K?? t??? s??? l??: "+ getBeforeDayMonth.charAt(i));
                    }
                    else {
                        if(viTriNumberBefore < 3)
                        {
                            viTriNumberBefore = 0;
                            checkDayMonth = false;
                        }
                        Log.d("KYTUSO", "K?? t??? kh??c: \t\t"+ getBeforeDayMonth.charAt(i));
                    }
                }
                else {
                    checkDayMonth = false;
                    // kh??ng ph???i date time
                    Log.d("KYTUSO", "kh??ng ph???i date time");
                }
            }

            if(checkDayMonth)
            {
                Log.d("checkDateT", "dayAndMonth = " + dayAndMonth);
                if(dayAndMonth.length() == 3)
                {
                    // 073 370 0 1 2
                    boolean checkDate = (Integer.parseInt(dayAndMonth.charAt(2) + ""+ dayAndMonth.charAt(1)) <= MAXDATE);
                    boolean checkMonth = false;
                    if((Integer.parseInt(dayAndMonth.charAt(0) + "") <= MAXMONTH) && (dayAndMonth.charAt(0) != 0))
                        checkMonth = true;

                    Log.d("checkDateT", "????y l?? ng??y " + dayAndMonth + " size ="+ dayAndMonth.length() + " Text = "+ getBeforeDayMonth);
                    Log.d("checkDateT", "????y l?? th??ng " + dayAndMonth.charAt(2) + ""+ dayAndMonth.charAt(1) +"-" + dayAndMonth.charAt(0) +"-"+ defaultYear);
                    Log.d("checkDateT", "????y l?? boolean " + checkDate + "   "+ checkMonth + " dayAndMonth.charAt(0) = "+ dayAndMonth.charAt(0));
                    if(checkDate && checkMonth)
                    {
                        if(dateHere.isEmpty())
                        {
                            dateHere =  dayAndMonth.charAt(2) + ""+ dayAndMonth.charAt(1) +"-" + dayAndMonth.charAt(0) +"-"+ defaultYear;
                            Log.d("checkDateT", "????y l?? ng??y khi dayAndMonth.length() == 3 : " + dateHere  + " Text = "+ getBeforeDayMonth);
                            try {
                                txt_DatePicker.setText(formatDateStart(dateHere, DEFAULT_DATE_M, true));
                            } catch (ParseException e) {
                                Log.d("checkDateT", "L???i convert ng??y: "+ e.getMessage());
                                e.printStackTrace();
                            }
                        }else {
                            Log.d("checkDateT", "???? c?? ng??y ??? c??c d??? li???u ph??a tr??n!");
                        }
                        Log.d("checkDateT", "????y l?? ng??y " + dayAndMonth.charAt(2) + ""+ dayAndMonth.charAt(1) +"-" + dayAndMonth.charAt(0) +"-"+ defaultYear);
                    }else {
                        checkNotDate = true;
                    }
                }
                else if( dayAndMonth.length() == 4)
                {
                    // 0703 3070 0 1 2 4
                    String date = dayAndMonth.charAt(3) +""+ dayAndMonth.charAt(2);
                    boolean checkDate = (Integer.parseInt(date) <= MAXDATE);
                    String month = dayAndMonth.charAt(1) + "" + dayAndMonth.charAt(0);
                    boolean checkMonth = (Integer.parseInt(month) <= MAXMONTH);
                    boolean checkMonth00 = (month.equals("00") || date.equals("00"));
                    Log.d("checkDateT", "????y l?? th??ng " +  dayAndMonth.charAt(3) + dayAndMonth.charAt(2) + "-"  + dayAndMonth.charAt(1) + dayAndMonth.charAt(0) +"-"+ defaultYear);
                    Log.d("checkDateT", "????y l?? boolean " + checkDate + "   "+ checkMonth);
                    if(checkDate && checkMonth && (!checkMonth00))
                    {
                        Log.d("checkDateT", "????y l?? ng??y " +  dayAndMonth.charAt(3) + dayAndMonth.charAt(2) + "-"  + dayAndMonth.charAt(1) + dayAndMonth.charAt(0) +"-"+ defaultYear);
                        if(dateHere.isEmpty())
                        {
                            dateHere = dayAndMonth.charAt(3) +""+ dayAndMonth.charAt(2) + "-"  + dayAndMonth.charAt(1) +""+ dayAndMonth.charAt(0) +"-"+ defaultYear;
                            txt_DatePicker.setText(dateHere);
                        }
                    }else {
                        checkNotDate = true;
                    }
                }
                else if(dayAndMonth.length() > 4)
                {
                    checkNotDate = true;
                    Log.d("checkDateT", "c?? nhi???u s??? h??n d??? li???u ng??y " + dayAndMonth + " size ="+ dayAndMonth.length() + " Text = "+ getBeforeDayMonth);
                }else {
                    checkNotDate = true;
                    Log.d("checkDateT", "B?? h??n 3 k?? t??? s???" + " Text = "+ getBeforeDayMonth);
                }
            }
        }
        else {
            checkNotDate = true;
        }
    }

    /**
     * getCodeLottery ????y l?? ph????ng th???c t??m ki???m v?? l???y ra m?? v?? s??? t??? ??o???n text ???????c ?????c ra t??? h??nh ???nh t??? v?? s???.
     * @param text ??o???n text ???????c ?????c ra t??? h??nh ???nh t??? v?? s???.
     * @param checkNotDate
     */
    private void getCodeLottery(String text, boolean checkNotDate) {

        // ki???m tra l???y ra m?? s??? v?? s??? t??? h??nh ???nh ng?????i d??ng ch???p
        String textCode = "";
        if(checkNotDate)
        {
            Log.d("lotteryResultFromImage", "checkNotDate = " + countNumeric(text));
            //lotteryResultFromImage
            if(!countNumeric(text).isEmpty())
            {
                textCode = countNumeric(text);
                Log.d("lotteryResultFromImage", "countNumeric = " + countNumeric(text));
            }
        }
        else
        {
            //7B2003 ngay 21-5-2021 782290 trong tr?????ng h???p ng??y th??ng ?????ng tr?????c m?? v?? s??? th?? t??i c???t b??? ph??a tr?????c
            // v?? check text ph??a sau xem c?? ph???i m?? v?? s??? hay kh??ng
            String dateBehind []= text.split(defaultYear);
            if(dateBehind.length >1)
            {
                if(dateBehind[1].length()>=6)
                    textCode = dateBehind[1];
                Log.d("lotteryResultFromImage", "dateBehind[1] = " + dateBehind[1]);
            }
        }
        // x??? l?? l???y ra m?? v?? s???
        handleGetCodeLottery(textCode);
    }

    /**
     * handleGetCodeLottery x??? l?? l???y ra m?? v?? s???
     * @param textCode ????y l?? ??o???n text ??ang c?? kh??? n??ng ch???a m?? v?? s??? trong ????
     */
    private void handleGetCodeLottery(String textCode) {
        if(!textCode.isEmpty())
        {
            int demSo = 0;
            String maVeSo = "";
            String textNumber = textCode.trim().replace(" ","").replace("'","");
            if((!textNumber.contains("/")) && (!textNumber.contains(".00")))
            {
                for (int i = 0; i < textNumber.length(); i++)
                {

                    boolean equals = String.valueOf(textNumber.charAt(i)).equals(".");
                    if(isNumeric(String.valueOf(textNumber.charAt(i)))
                            || (String.valueOf(textNumber.charAt(i)).equals("O") && (demSo == 0))
                            || equals
                            || (String.valueOf(textNumber.charAt(i)).equals("I")))
                    {

                        if((!equals))
                        {
                            if(demSo < 6)
                            {
                                maVeSo += String.valueOf(textNumber.charAt(i))
                                        .replace("O","0")
                                        .replace(".","")
                                        .replace("I","1");
                                Log.d("CheckMaVeSo2", "K?? t??? s??? l??: "+ textNumber.charAt(i));
                                demSo +=1;
                            }
                        }
                    }
                    else { //|| (String.valueOf(text.charAt(i)).equals("O") && (demSo == 0))
                        if(demSo < 6)
                        {
                            demSo = 0;
                            maVeSo = "";
                            Log.d("CheckMaVeSo", "K?? t??? kh??c: \t\t"+ textNumber.charAt(i));
                        }
                    }
                }
            }
            if(demSo == 6 && maVeSo.length() > 0)
            {
                if(!checkCodeLottery)
                {
                    Log.d("checkGetCodeq", "CheckMaVeSo = "+ demSo + " Text = "+ textNumber + " M?? v?? s??? l??: "+ maVeSo);
                    edt_Code_Lottery.setText(maVeSo);
                    checkCodeLottery = true;
                }
            }
            else {
                Log.d("CheckMaVeSo1", "CheckMaVeSo = "+ demSo + " Text = "+ textNumber + " M?? v?? s??? l??: "+ maVeSo);
            }
        }
    }



    /**
     * getProvince ????y l?? ph????ng th???c t??m ki???m v?? l???y ra ????i x??? s??? t??? ??o???n text ???????c ?????c ra t??? h??nh ???nh t??? v?? s???.
     * @param text ??o???n text ???????c ?????c ra t??? h??nh ???nh t??? v?? s???.
     */
    private void getProvince(String text) {

        // ki???m tra l???y ra ????i x??? s??? ho???c mi???n x??? s???
        // getTextNormalizer l?? ph????ng th???c chuy???n k?? t??? c?? d???u th??nh kh??ng d???u.

        String textGetMaDai =  getTextNormalizer(text);
        textGetMaDai = textGetMaDai.toLowerCase().replace(" ","");
        Log.d("TEN_DAz","textGetMaDai = " + textGetMaDai);
        if((!textGetMaDai.contains(TAI_CHINH)))
        {
            for (int i =0; i< MA_DAI.length; i++)
            {
                if(!textGetMaDai.contains(TAI_CHINH))
                {
                    if(textGetMaDai.contains(MA_DAI[i]) || textGetMaDai.contains(TEN_DAI[i]))
                    {
                        Log.d("checkGetCodeq", " TEN_DAI_FULL = " + TEN_DAI_FULL[i] + " MA_DAI = " + MA_DAI[i] + " \nTEN_DAI = " + TEN_DAI[i] + " Text = " + textGetMaDai);
                        txt_Province_Lottery.setText(TEN_DAI_FULL[i]);
                        checkDaiXoSo = true;
                    }
                }
            }
        }
    }

    /**
     * getDateHCM ????y l?? ph????ng th???c ki???m tra ph???i ng??y c?? ph???i c???a v?? s??? HCM kh??ng v?? c???t g???n ra ng??y th??ng c???a v?? s???
     * @param getBeforeYear
     * @return
     */
    private String getDateHCM(String getBeforeYear) {
        String [] getHCM = getBeforeYear.split("-");
        Log.d("getHCM", getHCM[0]);
        if(getHCM.length == 3)
        {
            if(getHCM[0].length()>2)
            {
                int size = getHCM[0].length();
                String dateHCM = getHCM[0].charAt(size-2) + "" +getHCM[0].charAt(size-1);
                String monthHCM = getHCM[1];
                String yearHCM = getHCM[2];
                Log.d("getHCM", dateHCM + monthHCM + yearHCM);
                Log.d("getHCM", " yearHCM = "+ yearHCM);
                return dateHCM + monthHCM;
            }else {
                return "";
            }
        }else {
            return "";
        }
    }

    /**
     * isNumeric ki???m tra xem strNum c?? ph???i l?? numeric kh??ng.
     * @param strNum
     * @return
     */
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }

    /**
     * countNumeric l?? ph????ng th???c ????? ki???m tra m?? s??? c?? ch???a n??m hi???n t???i kh??ng
     * v?? n?? c?? ph???i l?? ng??y th??ng ch??? kh??ng ph???i l?? m?? s??? c???a t??? v?? s??? hay kh??ng.
     * @param text
     * @return
     */
    private String countNumeric(String text)
    {
        try {
            if(text.contains(defaultYear)) {
                int lastIndext = text.lastIndexOf(defaultYear);
                String textRemove = text.substring(lastIndext + 4);
                int tamp = 0;
                for (int i = 0; i < textRemove.length(); i++) {
                    if (isNumeric(String.valueOf(textRemove.charAt(i)))) {
                        tamp += 1;
                    }
                }

                Log.d("lotteryResultFromImage", "tamp = " + tamp + " textRemove = "+ textRemove);
                if (tamp < 6) {
                    return "";
                }
                else {
                    return textRemove;
                }
            }
            else {
                return text;
            }

        }catch (Exception e)
        {
            e.getMessage();
            return text;
        }

    }

}
