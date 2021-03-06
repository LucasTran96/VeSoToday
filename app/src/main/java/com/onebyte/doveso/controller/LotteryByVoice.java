package com.onebyte.doveso.controller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.onebyte.doveso.R;
import com.onebyte.doveso.adapter.AdapterResultsLottery;
import com.onebyte.doveso.model.NaturalVoice;
import com.onebyte.doveso.temporaryfiledbmanager.TemporaryFileDBNaturalVoice;
import com.r0adkll.slidr.Slidr;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import static com.onebyte.doveso.api.ApiMethod.changePosition;
import static com.onebyte.doveso.api.ApiMethod.formatDateStart;
import static com.onebyte.doveso.api.ApiMethod.getDateNow;
import static com.onebyte.doveso.api.ApiMethod.getSubDate;
import static com.onebyte.doveso.api.ApiMethod.getTextNormalizer;
import static com.onebyte.doveso.contants.Global.DATE_SELECT_FORMAT_D;
import static com.onebyte.doveso.contants.Global.DATE_SELECT_FORMAT_DM;
import static com.onebyte.doveso.contants.Global.DATE_SELECT_FORMAT_M;
import static com.onebyte.doveso.contants.Global.DEFAULT_DATE_SELECT_FORMAT;
import static com.onebyte.doveso.contants.Global.DEFAULT_MM;
import static com.onebyte.doveso.contants.Global.DEFAULT_YEAR;
import static com.onebyte.doveso.contants.Global.KET_QUA_MIEN_BAC;
import static com.onebyte.doveso.contants.Global.MIEN_BAC;
import static com.onebyte.doveso.contants.Global.MIEN_NAM;
import static com.onebyte.doveso.contants.Global.MIEN_TRUNG;
import static com.onebyte.doveso.contants.Global.THANG;
import static com.onebyte.doveso.controller.LotteryByImage.checkValidDateLottery;
import static com.onebyte.doveso.controller.LotteryByImage.isNumeric;
import static com.onebyte.doveso.controller.ResultsLottery.checkActionResults;
import static com.onebyte.doveso.controller.ResultsLottery.checkDomain;
import static com.onebyte.doveso.controller.ResultsLottery.checkLotteryFromDateSelected;
import static com.onebyte.doveso.controller.ResultsLottery.detectingLotteryTickets;
import static com.onebyte.doveso.controller.ResultsLottery.getDomainLottery;
import static com.onebyte.doveso.controller.ResultsLottery.traditionalLotteryListResults;

public class LotteryByVoice extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private ImageView img_Voice_Lottery;
    private static final int RECOGNIZER_RESULT = 1111;
    private TextView txt_Results_Lottery_Voice;
    private TextView txt_Truy_Van_Do_Ve_So;
    private Button btn_Help_Desk;
    private LinearLayout lnl_Help_Desk, lnl_Results_Lottery;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private String dateNow = "";
    private boolean btn_Help_Desk_Is_Click; // ????y l?? gi?? tr??? ????? ki???m tra xem btn_Help_Desk ???? ???????c click hay ch??a click

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottery_by_voice);
        setID();
        Slidr.attach(this);

    }

    /**
     * setID Khai b??o ID cho c??c bi???n TextView, Button, ImageView...
     */
    private void setID() {

        toolbar = findViewById(R.id.toolbar_Lottery_By_Voice);
        toolbar.setTitle(getApplicationContext().getResources().getString(R.string.app_name_alias));
        img_Voice_Lottery = findViewById(R.id.img_Voice_Lottery);
        img_Voice_Lottery.setOnClickListener(this);
        txt_Results_Lottery_Voice = findViewById(R.id.txt_Results_Lottery_Voice);
        txt_Truy_Van_Do_Ve_So = findViewById(R.id.txt_Truy_Van_Do_Ve_So_Voice);
        lnl_Help_Desk = findViewById(R.id.lnl_Help_Desk);
        lnl_Help_Desk.setVisibility(View.GONE);
        lnl_Results_Lottery = findViewById(R.id.lnl_Results_Lottery);
        lnl_Results_Lottery.setVisibility(View.GONE);
        btn_Help_Desk = findViewById(R.id.btn_Help_Desk);
        btn_Help_Desk_Is_Click = false;
        btn_Help_Desk.setOnClickListener(this);
        // mRecyclerView hi???n th??? k???t qu??? x??? s??? khi ng?????i d??ng b???m d?? v?? s???.
        mRecyclerView = findViewById(R.id.rcv_Results_Lottery_Top);
        mRecyclerView.setHasFixedSize(true);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        AdRequest adRequest = new AdRequest.Builder().build();
        // AdSize adSize = new AdSize(320, 100);
        AdView mAdView = findViewById(R.id.adView_Voice);
        mAdView.loadAd(adRequest);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * onClick g??n s??? ki???n Onclick cho c??c bi???n TextView, Button, ImageView...
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id)
        {
            case R.id.img_Voice_Lottery:
            {
                txt_Truy_Van_Do_Ve_So.setText("");
                Intent intentSpeech = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intentSpeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intentSpeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "vi_VN");
                intentSpeech.putExtra(RecognizerIntent.EXTRA_PROMPT,"Xin m???i b???n n??i");
                try {

                    startActivityForResult(intentSpeech,RECOGNIZER_RESULT);
                }catch (Exception e)
                {
                    Toast.makeText(LotteryByVoice.this, "Kh??ng th??? b???t Google gi???ng n??i!", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.btn_Help_Desk:
            {
                if(!btn_Help_Desk_Is_Click)
                {
                    lnl_Help_Desk.setVisibility(View.VISIBLE);
                    btn_Help_Desk_Is_Click = true;
                }else {
                    lnl_Help_Desk.setVisibility(View.GONE);
                    btn_Help_Desk_Is_Click = false;
                }
                break;
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        switch (requestCode) {
            case RECOGNIZER_RESULT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txt_Results_Lottery_Voice.setText(matches.get(0));
                    handleVoiceToText(matches.get(0));
                }
                break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressLint("SetTextI18n")
    private void handleVoiceToText(String textFromVoice) {
        try {
            if(textFromVoice != null || textFromVoice.isEmpty())
            {
                lnl_Results_Lottery.setVisibility(View.VISIBLE);
                Log.d("textFromVoice", "textFromVoice = " + textFromVoice);
                textFromVoice = getTextNormalizer(textFromVoice);
                Log.d("textFromVoice", "getTextNormalizer = " + textFromVoice);

                // x??? l?? t??m ki???m trong ??o???n text m?? ng?????i d??ng n??i ????? l???y ra ????i c???n d?? c???a t??? v?? s???
                String provinceLottery = getProvinceFromText(textFromVoice);

                // x??? l?? t??m ki???m trong ??o???n text m?? ng?????i d??ng n??i ????? l???y ra ng??y c???a t??? v?? s???
                String dateLottery = getDateFromText(textFromVoice.toLowerCase().replace(" ",""));
                Log.d("textFromVoice", "dateLottery = dateLottery "+dateLottery);
                String checkMien;
                // x??? l?? t??m ki???m trong ??o???n text m?? ng?????i d??ng n??i ????? l???y ra m?? v?? s??? c???n d?? c???a t??? v?? s???
                String codeLottery = "";
                if(!provinceLottery.isEmpty())
                {
                    int getDomainVerify;
                    if(provinceLottery.equals("Mi???n b???c"))
                    {
                        getDomainVerify = KET_QUA_MIEN_BAC;
                    }
                    else {
                        getDomainVerify = checkDomain(provinceLottery, getApplicationContext());
                    }


                    String textGetCode;
                    if(!dateNow.isEmpty())
                        textGetCode = textFromVoice.replace(" ", "").replace(dateNow,"");
                    else
                        textGetCode = textFromVoice.replace(" ", "");

                    if(getDomainVerify == KET_QUA_MIEN_BAC)
                        codeLottery = getCodeFromText(textGetCode, 5);
                    else
                        codeLottery = getCodeFromText(textGetCode, 6);
                }


                if(!provinceLottery.isEmpty() && !dateLottery.isEmpty() && !codeLottery.isEmpty())
                {
                    Log.d("textFromVoice", "handelGetResultLottery provinceLottery = "+ provinceLottery + " codeLottery = " + codeLottery + " dateLottery = "+ dateLottery);
                    handelGetResultLottery(provinceLottery, dateLottery, codeLottery);

                }
                else {

                    txt_Truy_Van_Do_Ve_So.setTextColor(getResources().getColor(R.color.bgLogo));
                    if(codeLottery.isEmpty() && provinceLottery.isEmpty() && dateLottery.isEmpty())
                    {
                        txt_Truy_Van_Do_Ve_So.setText("Kh??ng c?? th??ng tin t??n ????i, Ng??y, M?? v?? s??? m?? b???n mu???n d?? trong c??u b???n v???a n??i. B???n vui l??ng th??? n??i l???i l???n n???a.");
                    }
                    else {
                        if(codeLottery.isEmpty())
                        {
                            Log.d("textFromVoice", "codeLottery = isEmpty");
                            txt_Truy_Van_Do_Ve_So.setText("M?? v?? s??? m?? b???n n??i ???? kh??ng h???p l???. B???n vui l??ng th??? n??i l???i l???n n???a.");
                        }
                        if(provinceLottery.isEmpty())
                        {
                            Log.d("textFromVoice", "provinceLottery = isEmpty");
                            txt_Truy_Van_Do_Ve_So.setText("T??n ????i b???n n??i ???? kh??ng h???p l???. B???n vui l??ng th??? n??i l???i l???n n???a.");
                        }
                        if(dateLottery.isEmpty())
                        {
                            Log.d("textFromVoice", "dateLottery = isEmpty");
                            txt_Truy_Van_Do_Ve_So.setText("Ng??y c???n d?? c???a v?? s??? m?? b???n n??i ???? kh??ng h???p l???. B???n vui l??ng th??? n??i l???i l???n n???a.");
                        }
                    }


                }
                Log.d("textFromVoice", "provinceLottery = "+ provinceLottery + " codeLottery = " + codeLottery + " dateLottery = "+ dateLottery);
            }
        }catch (Exception e)
        {
            e.getMessage();
        }
    }

    /**
     * handelGetResultLottery ????y l?? ph????ng th???c x??? l?? l???y ra k???t qu??? x??? s??? v?? d?? v?? s??? cho ng?????i d??ng t??? ng??y, ????i, m?? s??? ???? l???y ???????c t??? voice.
     * @param provinceLottery t??n ????i
     * @param dateLottery ng??y
     * @param codeLottery m?? v?? s???
     */
    private void handelGetResultLottery(String provinceLottery, String dateLottery, String codeLottery)
    {

        try {
            if(provinceLottery.equals(MIEN_BAC))
                provinceLottery = "H?? N???i";

            int getDomainVerify = checkDomain(provinceLottery, getApplicationContext());
            Log.d("textFromVoice", getDomainVerify + " checkValidDateLottery " + checkValidDateLottery(dateLottery, txt_Truy_Van_Do_Ve_So, getApplicationContext()));
            if(checkValidDateLottery(dateLottery, txt_Truy_Van_Do_Ve_So, getApplicationContext()))
            {
                traditionalLotteryListResults = new ArrayList<>();
                mAdapter = new AdapterResultsLottery(LotteryByVoice.this, traditionalLotteryListResults);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                String getDomain = (getDomainLottery(provinceLottery, getApplicationContext()).isEmpty())?"Kh??ng x??c ?????nh":getDomainLottery(provinceLottery, getApplicationContext());

                if(checkActionResults(dateLottery, getDomainVerify) && checkLotteryFromDateSelected(provinceLottery, dateLottery, getApplicationContext()))
                {
                    txt_Truy_Van_Do_Ve_So.setText(getString(R.string.truy_van_do_ve_so_hom_nay,provinceLottery, getDomain, String.valueOf(codeLottery.length()), dateLottery, codeLottery));
                    txt_Truy_Van_Do_Ve_So.setTextColor(getResources().getColor(R.color.black));

                    if(getDomainVerify == 0)
                        detectingLotteryTickets(provinceLottery, dateLottery, codeLottery, LotteryByVoice.this, mRecyclerView, true);
                    else
                        detectingLotteryTickets(provinceLottery, dateLottery, codeLottery, LotteryByVoice.this, mRecyclerView, false);
                }
                else {
                    Log.d("ve_so_chua_xo",  provinceLottery + getDomain + dateLottery); //K???t qu??? x??? s??? % - X??? s??? % m??? th?????ng ng??y % hi???n ch??a c?? tr??n h??? th???ng vui l??ng b???m ????y ch??? m??? th?????ng.
                    txt_Truy_Van_Do_Ve_So.setVisibility(View.VISIBLE);
                    txt_Truy_Van_Do_Ve_So.setText(getString(R.string.ve_so_chua_xo, provinceLottery, getDomain, dateLottery));
                    txt_Truy_Van_Do_Ve_So.setTextColor(getResources().getColor(R.color.bgLogo));
                }

            }
        }catch (Exception e)
        {
            e.getMessage();
            Log.d("textFromVoice", "e.getMessage() = " + e.getMessage());
        }

    }


    /**
     * getProvinceFromText ????y l?? ph????ng th???c t??m ki???m v?? l???y ra t??n ????i ho???c t??n mi???n trong ??o???n text m?? ng?????i d??ng n??i.
     * @param textFromVoice ??o???n text m?? ng?????i d??ng n??i
     * @return t??n ????i ho???c t??n mi???n
     */
    private String getProvinceFromText(String textFromVoice) {

        Log.d("textFromVoice", "textFromVoice = " + textFromVoice);
        TemporaryFileDBNaturalVoice temporaryFileDBNaturalVoice = new TemporaryFileDBNaturalVoice(getApplicationContext());
        List<NaturalVoice> naturalVoiceList = temporaryFileDBNaturalVoice.naturalVoiceDBRead(textFromVoice);
        List<String> provinceOrDomainLottery = new ArrayList<>();
        String province = "";
        if(naturalVoiceList.size()>0)
        {
                for (NaturalVoice naturalVoice: naturalVoiceList)
                {
                    // l???y ra mi???n ho???c t??n ????i trong naturalVoice ???? l???y ra ???????c.
                    provinceOrDomainLottery.add(naturalVoice.getCUM_TU_CHINH_XAC());
                }

                if(provinceOrDomainLottery.size()>0)
                {
                    for (String domain: provinceOrDomainLottery) {

                        if(domain.equals(MIEN_BAC))
                        {
                            Log.d("textFromVoice", "province = " + domain);
                            return domain;
                        }else {
                            if(!domain.equals(MIEN_NAM) && !domain.equals(MIEN_TRUNG))
                                province = domain;
                        }
                    }
                }
        }
        else {
            Log.d("textFromVoice", "naturalVoiceList.size() == 0");
            return "";
        }
        Log.d("textFromVoice", "province = " + province);
       return province;
    }

    /**
     * getCodeFromText ????y l?? ph????ng th???c l???y ra 6 k?? t??? s??? c???a t??? v?? s???.
     * @param textFromVoice ??o???n text m?? ng?????i d??ng n??i t??? google voice sang text
     * @param numberCodeDomain l?? t???ng s??? m?? v?? s??? c???n l???y ra v?? d??? mi???n b???c th?? l???y 5 s??? mi???n nam th?? l???y 6 s???
     * @return tr??? v??? 6 k?? t??? c???a m?? v?? s???
     */
    private String getCodeFromText(String textFromVoice, int numberCodeDomain) {

        textFromVoice = replaceDayToNumeric(textFromVoice);

        if(!textFromVoice.isEmpty())
        {
            if(!textFromVoice.contains(THANG) && !textFromVoice.contains("/"))
            {
                return getCodeLottery(textFromVoice, numberCodeDomain);
            }
            else if(textFromVoice.contains(THANG) || textFromVoice.contains("/"))
            {
                //String[] splitThang =  textFromVoice.split(THANG);
                String[] splitThang;
                textFromVoice = textFromVoice.replace(" ","");
                if(textFromVoice.contains(THANG))
                    splitThang  = textFromVoice.split(THANG);
                else
                    splitThang  = textFromVoice.split("/");

                if(splitThang.length == 2)
                {
                    String beforeThang = getCodeLottery(changePosition(splitThang[0]), numberCodeDomain);
                    String afterThang = getCodeLottery(splitThang[1], numberCodeDomain);

                    // ki???m tra c???m t??? ph??a tr?????c t??? "th??ng" c?? m?? v?? s??? hay kh??ng
                    if(!beforeThang.isEmpty())
                    {
                        beforeThang = changePosition(beforeThang);
                        Log.d("textFromVoice", "beforeThang = "+beforeThang);
                        return beforeThang;
                    }else {
                        Log.d("textFromVoice", "beforeThang is empty");
                    }
                    // ki???m tra c???m t??? ph??a sau t??? "th??ng" c?? m?? v?? s??? hay kh??ng
                    if(!afterThang.isEmpty())
                    {
                        Log.d("textFromVoice", "afterThang = "+afterThang);
                        return afterThang;
                    }
                    else
                        Log.d("textFromVoice", "afterThang is empty");
                }
            }
        }
        return "";
    }

    /**
     * getCodeLottery ????y l?? ph????ng th???c con c???a ph????ng th???c getCodeFromText()
     * N?? gi??p t??ch s??? v?? l???y ra 6 k?? t??? s??? c???a t??? v?? s??? m?? ng?????i d??ng n??i b???ng gi???ng n??i.
     */
    private String getCodeLottery(String textCode, int numberCodeDomain)
    {
        int demSo = 0;
        String maVeSo = "";
        for (int i = 0; i < textCode.length(); i++) {
            if (isNumeric(String.valueOf(textCode.charAt(i)))) {

                    if (demSo < 6) {
                        maVeSo += String.valueOf(textCode.charAt(i));
                        Log.d("CheckMaVeSo2", "K?? t??? s??? l??: " + textCode.charAt(i));
                        demSo += 1;
                    }
            } else { //|| (String.valueOf(text.charAt(i)).equals("O") && (demSo == 0))
                if (demSo < numberCodeDomain) {
                    demSo = 0;
                    maVeSo = "";
                    Log.d("CheckMaVeSo", "K?? t??? kh??c: \t\t" + textCode.charAt(i));
                }
            }
        }

        if(numberCodeDomain == 5)
        {
            if ((demSo == numberCodeDomain || demSo == numberCodeDomain +1) && maVeSo.length() > 0) {
                Log.d("CheckMaVeSo1", "CheckMaVeSo = " + demSo + " Text = " + textCode + " M?? v?? s??? l??: " + maVeSo);
                return maVeSo;
            }
        }
        else {
            if (demSo == numberCodeDomain && maVeSo.length() > 0) {
                Log.d("CheckMaVeSo1", "CheckMaVeSo = " + demSo + " Text = " + textCode + " M?? v?? s??? l??: " + maVeSo);
                return maVeSo;
            }
        }
            return "";
    }

    /**
     * replaceDayToNumeric ????y l?? ph????ng th???c Replace c??c t??? ng??? thu???c d???ng s??? sang con s??? ch??nh x??c
     * V?? d???: M???t => 1
     * @param textVoice ??o???n text l???y ra ???????c t??? google voice
     * @return ??o???n text ???? chuy???n ?????i s??? d???ng ch??? sang d???ng con s??? ch??nh x??c.
     */
    private String replaceDayToNumeric(String textVoice)
    {
        if(!textVoice.isEmpty())
        {
            return textVoice.replace("mot","1")
                    .replace("hai", "2")
                    .replace("ba", "2")
                    .replace("bon", "2")
                    .replace("nam", "2")
                    .replace("sau", "2")
                    .replace("bay", "2")
                    .replace("tam", "2")
                    .replace("chin", "2")
                    .replace("muoi", "10");
        }
        return textVoice;
    }

    private String getDateFromText(String textFromVoice) {

        if(textFromVoice.contains("homnay") || textFromVoice.contains("buanay"))// H??m nay || b???a nay
        {
            Log.d("textFromVoice", getSubDate(1));
            return getDateNow(DEFAULT_DATE_SELECT_FORMAT);
        }else if(textFromVoice.contains("homqua"))
        {
            Log.d("textFromVoice", getSubDate(1));
            return getSubDate(1);
        }else if(textFromVoice.contains("homkia"))
        {
            Log.d("textFromVoice", getSubDate(2));
            return getSubDate(2);
        }else {

            if(textFromVoice.contains(THANG) || textFromVoice.contains("/") || textFromVoice.contains("-"))
            {
                Log.d("textFromVoice", "textFromVoice.contains(THANG)");
                // ng??y 25 th??ng 5
                // 2 th??ng 5
                // 25/5
                // x??? s??? An Giang ng??y 25 th??ng 5 m?? s??? 488122
                // An Giang

                String[] splitDate;
                textFromVoice = textFromVoice.replace(" ","");
                String checkIsThang;
                if(textFromVoice.contains(THANG))
                {
                    splitDate  = textFromVoice.split(THANG);
                    checkIsThang = THANG;
                }
                else if(textFromVoice.contains("/"))
                {
                    splitDate  = textFromVoice.split("/");
                    checkIsThang = "/";
                }
                else
                {
                    splitDate  = textFromVoice.split("-");
                    checkIsThang = "-";
                }


                String aNgay = "";
                String aMonth = "";
                if(splitDate.length == 2)
                {
                    // L???y ra ng??y c???a t??? v?? s???
                    aNgay = getDateLottery(splitDate[0]);

                    aMonth = getMonthLottery(splitDate[1]);
                    Log.d("textFromVoice", "splitDate.length == 2");
                    if((!aNgay.isEmpty()) && (!aMonth.isEmpty()))
                    {

                        dateNow = aNgay+checkIsThang+aMonth;
                        String year;
                        if(aMonth.equals("12"))
                        {
                            if(getDateNow(DEFAULT_MM).equals("12"))
                            {
                                year = getDateNow(DEFAULT_YEAR);
                            }else {
                                year = (Integer.parseInt(getDateNow(DEFAULT_YEAR)) -1) +"";
                            }
                        }else {
                            year = getDateNow(DEFAULT_YEAR);
                        }
                        Log.d("textFromVoice", "Date == " +aNgay+"-" + aMonth + "-"+ year);
                        String dateLottery = aNgay+"-" + aMonth + "-" + year;
                        try {
                            if(aNgay.length() == 1 && aMonth.length() == 1)
                            {
                                dateLottery = formatDateStart(dateLottery, DATE_SELECT_FORMAT_DM, true);
                            }else if(aNgay.length() == 1 && aMonth.length() == 2)
                            {
                                dateLottery = formatDateStart(dateLottery, DATE_SELECT_FORMAT_D, true);
                            }
                            else if(aNgay.length() == 2 && aMonth.length() == 1)
                            {
                                dateLottery = formatDateStart(dateLottery, DATE_SELECT_FORMAT_M, true);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                            return "";
                        }

                        Log.d("textFromVoice", "dateLottery== "+ dateLottery);
                        return dateLottery;
                    }
                    return "";
                }
                else
                {
                    return "";
                }
            }else {
                Log.d("textFromVoice", "Ch??a th??? l???y ???????c ng??y t??? ??o???n text n??y");
            }
            return "";
        }
    }

    /**
     * getDateLottery L?? ph????ng th???c l???y ra ng??y t??? google gi???ng n??i m?? ng?????i d??ng n??i v??o
     * @param text X??? s??? An Giang ng??y
     * @return
     */
    private String getDateLottery(String text) {

        Log.d("textFromVoice", "getDateLottery text = "+ text);
        String aNgay = "";
        if(text.length()>=2)
        {

            String position1 = String.valueOf(text.charAt(text.length() - 2));
            String position2 = String.valueOf(text.charAt(text.length() - 1));

            String ngayTamp = position1 + position2;
            Log.d("textFromVoice", "ngayTamp = "+ ngayTamp);
            Log.d("textFromVoice", "ngayTamp = "+ position1 + "-" + position2);
            if(isNumeric(position1) && isNumeric(position2))
            {
                Log.d("textFromVoice", "ngayTamp = isNumeric(position1) && isNumeric(position2)");
                Log.d("textFromVoice", "position1.charAt(0) = "+ position1.charAt(0));
                if(Integer.parseInt(position1)<=3)
                {
                    Log.d("textFromVoice", "position1.charAt(0) <=3");
                    aNgay = ngayTamp;
                }
                else {
                    Log.d("textFromVoice", "position1.charAt(0) >3");
                    aNgay = position2;
                }
            }
            else if(isNumeric(position2) && (!isNumeric(position1)))
            {
                Log.d("textFromVoice", "isNumeric(position2) && (!isNumeric(position1))");
                aNgay = position2;
                Log.d("textFromVoice","aNgay = "+aNgay);
            }
            else {
                Log.d("textFromVoice", "l???i kh??ng l???y ???????c ng??y");
                // l???i kh??ng l???y ???????c ng??y
                // Kh??ng ph???i ?????nh d???ng ng??y
            }
        }
        else if(text.length()== 1)
        {
            if(isNumeric(text))
            {
                aNgay = text;
            }
        }
        else {
            // l???i kh??ng l???y ???????c ng??y
        }

        return aNgay;
    }

    /**
     * getDateLottery L?? ph????ng th???c l???y ra ng??y t??? google gi???ng n??i m?? ng?????i d??ng n??i v??o
     * @param text X??? s??? An Giang ng??y
     * @return
     */
    private String getMonthLottery(String text) {

        String aMonth = "";
        if(text.length()>=2)
        {

            /*
                ?????m th??? ph??a sau c?? bao nhi??u s???
                v?? d??? 8 s??? th?? 2 s??? ?????u l?? th??ng 6 s??? sau l?? m?? v?? s???
                n???u 7 s??? th?? s??? ?????u ti??n l?? th??ng 6 s??? sau l?? m?? v?? s???
             */
            int countNumber = 0;
            for (int i=0; i<text.length(); i++)
            {
                if(isNumeric(String.valueOf(text.charAt(i))))
                {
                    countNumber += 1;
                }
            }

            /*
                N???u ?????m t???ng number trong ??o???n text l???n h??n 2 th?? c?? kh??? n??ng l???y ???????c th??ng.
             */
            if(countNumber>=2)
            {
                Log.d("textFromVoice", "getMonthLottery text == "+text);
                Log.d("textFromVoice", "getMonthLottery countNumber == "+countNumber);
                String aMonthFull = text.charAt(0) +""+ text.charAt(1);
                if(countNumber == 7) // 11 m?? s??? 12345
                {
                    if(isNumeric(String.valueOf(text.charAt(0))) && isNumeric(String.valueOf(text.charAt(1))))
                    {
                        String position0= String.valueOf(text.charAt(0));
                        if(Integer.parseInt(position0) == 1)
                        {
                            Log.d("textFromVoice", "getMonthLottery text == "+text);
                            String position1= String.valueOf(text.charAt(1));
                            if(Integer.parseInt(position1) < 3)//Integer.parseInt(position1)
                            {
                                aMonth = aMonthFull;
                                Log.d("textFromVoice", "Integer.parseInt(position1) < 3 aMonth  == "+aMonth);
                            }
                            else {
                                aMonth = String.valueOf(text.charAt(0));
                                Log.d("textFromVoice", "Integer.parseInt(position1) >= 3 aMonth  == "+aMonth);
                            }
                        }
                        else if(Integer.parseInt(position0) == 0)
                        {
                            aMonth = aMonthFull;
                            Log.d("textFromVoice", "Integer.parseInt(position0) == 0 aMonth  == "+aMonth);
                        }
                        else {
                            aMonth = String.valueOf(text.charAt(0));
                            Log.d("textFromVoice", "aMonth  == "+aMonth);
                        }
                    }
                    else if(isNumeric(String.valueOf(text.charAt(0))) && (!isNumeric(String.valueOf(text.charAt(1)))))
                    {
                        aMonth = String.valueOf(text.charAt(0));
                        Log.d("textFromVoice", "isNumeric(String.valueOf(text.charAt(0))) && (!isNumeric(String.valueOf(text.charAt(1)) aMonth  == "+aMonth);
                    }
                }
                else // 11 m?? s??? 123456
                {
                    if(isNumeric(String.valueOf(text.charAt(0))) && isNumeric(String.valueOf(text.charAt(1))))
                    {
                        String position0= String.valueOf(text.charAt(0));
                        if( Integer.parseInt(position0) == 0 || Integer.parseInt(position0) == 1)
                        {
                            Log.d("textFromVoice", "getMonthLottery aMonthFull == "+aMonthFull);
                            aMonth = aMonthFull;
                        }
                        else {
                            aMonth = String.valueOf(text.charAt(0));
                        }
                    }
                    else if(isNumeric(String.valueOf(text.charAt(0))) && (!isNumeric(String.valueOf(text.charAt(1)))))
                    {
                        aMonth = String.valueOf(text.charAt(0));
                    }
                }
            }
            else // ????y l?? tr?????ng h???p c?? ho???c c?? 1 number
            {
                if(countNumber==1) // c?? 1 number trong ??o???n text th?? check xem c?? ph???i l?? th??ng hay kh??ng
                {
                    if(isNumeric(String.valueOf(text.charAt(0))))
                    {
                        aMonth = String.valueOf(text.charAt(0));
                    }
                }
            }
        }
        else if(text.length()== 1)
        {
            if(isNumeric(text))
            {
                aMonth = text;
            }
        }
        Log.d("textFromVoice", "aMonth  == "+aMonth);
        return aMonth;
    }

}
