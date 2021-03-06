package com.onebyte.doveso.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.google.gson.Gson;
import com.onebyte.doveso.R;
import com.onebyte.doveso.model.LotteryResults;
import com.onebyte.doveso.model.LotterySchedule;
import com.onebyte.doveso.model.TraditionalLottery;
import com.onebyte.doveso.temporaryfiledbmanager.TemporaryFileDBLotterySchedule;
import com.onebyte.doveso.temporaryfiledbmanager.TemporaryFileDBTraditionalLottery;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.List;
import static com.onebyte.doveso.api.ApiMethod.alertDialog;
import static com.onebyte.doveso.api.ApiMethod.convertDateToMillisecond;
import static com.onebyte.doveso.api.ApiMethod.convertLongToDate;
import static com.onebyte.doveso.api.ApiMethod.formatDate;
import static com.onebyte.doveso.api.ApiMethod.formatDateStart;
import static com.onebyte.doveso.api.ApiMethod.getDateNow;
import static com.onebyte.doveso.api.ApiMethod.getDayNow;
import static com.onebyte.doveso.api.ApiMethod.getMillisecondNow;
import static com.onebyte.doveso.api.ApiMethod.getSubDate;
import static com.onebyte.doveso.api.ApiMethod.isConnected;
import static com.onebyte.doveso.contants.Global.DEFAULT_DATE;
import static com.onebyte.doveso.contants.Global.DEFAULT_DATE_SELECT_FORMAT;
import static com.onebyte.doveso.contants.Global.DEFAULT_HH;
import static com.onebyte.doveso.contants.Global.DEFAULT_HHmm;
import static com.onebyte.doveso.contants.Global.DEFAULT_HOUR_MINUTES;
import static com.onebyte.doveso.contants.Global.DEFAULT_MINUTES;
import static com.onebyte.doveso.contants.Global.DEFAULT_mm;
import static com.onebyte.doveso.contants.Global.KET_QUA_MIEN_BAC;
import static com.onebyte.doveso.contants.Global.KET_QUA_MIEN_NAM;
import static com.onebyte.doveso.contants.Global.KET_QUA_MIEN_TRUNG;
import static com.onebyte.doveso.contants.Global.MIEN_NAM;
import static com.onebyte.doveso.contants.Global.ONEDAY;

public class LaunchScreen extends AppCompatActivity {

    private TraditionalLottery traditionalLottery;
    private String giaiDB, giaiNhat, giaiNhi, giaiBa, giaiTu, giaiNam, giaiSau, giaiBay, giaiTam;
    private int demSizeDai, demTongDaiDaTai;
    public static boolean checkDownloaded;
    private int countDateSaved = 0;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // check internet offline
        if(isConnected(getApplicationContext()))
        {
            // check ???? l??u k???t qu??? x??? s??? t???i ng??y h??m nay hay ch??a.
            //boolean checkSaved = checkLotterySaved(false);

            String checkSavedHaveInternet = checkLotterySavedWhenInternet(); // l???y ra c??c mi???n c??n thi???u k???t qu??? x??? s???: Saved, Failed, MNMBMT, MN, MT, MB.
            Log.d("TemporaryFileDBManager", "checkSaved = " + checkSavedHaveInternet);

            Log.d("TemporaryFileDBManager", "DEFAULT_HH = " + (Integer.parseInt(getDateNow(DEFAULT_HH)) >= 16) + " DEFAULT_mm = "+ (Integer.parseInt(getDateNow(DEFAULT_mm)) >= 45));

            boolean checkDownload30Date = false;
            if(!checkSavedHaveInternet.equals("Saved"))
            {
                Log.d("TemporaryFileDBManager", "countDateSaved = " + countDateSaved);
                TemporaryFileDBLotterySchedule temporaryFileDBManagerForLichXoSo = new TemporaryFileDBLotterySchedule(getApplicationContext());
                List<LotterySchedule> lotteryScheduleList;
                String THU = getDayNow(getDateNow(DEFAULT_DATE_SELECT_FORMAT));
                if(!checkSavedHaveInternet.equals("Failed"))
                {
                    if(checkSavedHaveInternet.equals("MN")) // Mi???n Nam
                        lotteryScheduleList = temporaryFileDBManagerForLichXoSo.LotteryListDBReadWithTime(
                                THU, "2");
                    else if(checkSavedHaveInternet.equals("MT")) // Mi???n Trung
                        lotteryScheduleList = temporaryFileDBManagerForLichXoSo.LotteryListDBReadWithTime(
                                THU, "1");
                    else if(checkSavedHaveInternet.equals("MB")) // Mi???n B???c
                        lotteryScheduleList = temporaryFileDBManagerForLichXoSo.LotteryListDBReadWithTime(
                                THU, "0");
                    else if(checkSavedHaveInternet.equals("MNMT")) // Mi???n Nam, Mi???n Trung
                        lotteryScheduleList = temporaryFileDBManagerForLichXoSo.LotteryListDBReadWithTime(
                                THU, "2,1");
                    else if(checkSavedHaveInternet.equals("MNMB")) // Mi???n Nam, Mi???n B???c
                        lotteryScheduleList = temporaryFileDBManagerForLichXoSo.LotteryListDBReadWithTime(
                                THU, "2,0");
                    else if(checkSavedHaveInternet.equals("MTMB")) //Mi???n Trung, Mi???n B???c
                        lotteryScheduleList = temporaryFileDBManagerForLichXoSo.LotteryListDBReadWithTime(
                                THU, "1,0");
                    else if(checkSavedHaveInternet.equals("MNN")) // Mi???n Nam tr?????c 4h30
                    {
                        lotteryScheduleList = temporaryFileDBManagerForLichXoSo.LotteryListDBReadWithTime(
                                null, "2");
                        checkDownload30Date = true;
                    }
                    else if(checkSavedHaveInternet.equals("MTT")) // Mi???n Trung tr?????c 4h30
                    {
                        lotteryScheduleList = temporaryFileDBManagerForLichXoSo.LotteryListDBReadWithTime(
                                null, "1");
                        checkDownload30Date = true;
                    }

                    else if(checkSavedHaveInternet.equals("MBB")) // Mi???n B???c tr?????c 4h30
                    {
                        lotteryScheduleList = temporaryFileDBManagerForLichXoSo.LotteryListDBReadWithTime(
                                null, "0");
                        checkDownload30Date = true;
                    }
                    else // Mi???n Nam, Mi???n Trung, Mi???n B???c
                    {
                        checkDownload30Date = true;
                        lotteryScheduleList = temporaryFileDBManagerForLichXoSo.LotteryListDBReadWithTime(
                                null, "2,1,0");
                    }

                }else
                {
                    checkDownload30Date = true;
                    // ch??a ?????y ????? 30 ng??y n??n c???n t???i l???i t??? ?????u cho 3 mi???n.
                    lotteryScheduleList = temporaryFileDBManagerForLichXoSo.LotteryScheduleDBReadWithTime(true, null);
                }

                Log.d("TemporaryFileDBManager", "Check 4h30 SIZE = "+lotteryScheduleList.size());
                if(lotteryScheduleList.size() != 0)
                {
                    demSizeDai = lotteryScheduleList.size();
                    demTongDaiDaTai = 0;
                    checkDownloaded = false;
                    dialog = ProgressDialog.show(LaunchScreen.this, "",
                            getResources().getString(R.string.tai_du_lieu), true);


                    for (LotterySchedule lotterySchedule : lotteryScheduleList)
                    {

                        Log.d("TemporaryFileDBManager", "lotterySchedule.getLINK_RSS() = "+lotterySchedule.getLINK_RSS());
                        if(lotterySchedule.getLINK_RSS() != null)
                        {
                            if(!lotterySchedule.getLINK_RSS().isEmpty())
                            {
                                Log.d("TemporaryFileDBManager", "\r\r\r\r\r\r\r\r\rT??n ????i = "+ lotterySchedule.getDAI_XO_SO());
                                ///RSSGetResultsLottery(lotterySchedule.getLINK_RSS(), lotterySchedule.getMIEN(), lotterySchedule.getDAI_XO_SO());
                                // domainLottery, URL, domain;
                                // new RSSGetResultsLottery(lotterySchedule.getDAI_XO_SO(), lotterySchedule.getLINK_RSS(), lotterySchedule.getMIEN()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


                                if(checkDownload30Date)
                                    new RSSGetResultsLottery(lotterySchedule.getDAI_XO_SO(), lotterySchedule.getLINK_RSS(), lotterySchedule.getMIEN(), null).execute();
                                else {
                                    new RSSGetResultsLottery(lotterySchedule.getDAI_XO_SO(), lotterySchedule.getLINK_RSS(), lotterySchedule.getMIEN(), getDateNow(DEFAULT_DATE_SELECT_FORMAT)).execute();
                                }
                            }
                        }
                    }
                }
            }
            else {
                //Toast.makeText(this, "D??? li???u ???? ???????c t???i t??? tr?????c.", Toast.LENGTH_SHORT).show();
                Log.d("TemporaryFileDBManager", "D??? li???u ???? ???????c t???i t??? tr?????c.");
                startActivityDashboard(50);
            }

            // startActivityDashboard();
        }
        else {
            Log.d("TemporaryFileDBManager", "no internet!");
            // no internet
            // check ???? l??u k???t qu??? x??? s??? t???i ng??y h??m nay hay ch??a.
            checkLotterySaved(true);
            //Toast.makeText(this, "Hi???n t???i thi???t b??? c???a b???n ??ang b??? m???t k???t n???i internet. B???n vui l??ng ki???m tra v?? th??? l???i sau!", Toast.LENGTH_LONG).show();
            alertDialog(LaunchScreen.this,getResources().getString(R.string.ket_noi_mang_loi),getResources().getString(R.string.ket_noi_mang_loi_chi_tiet));
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if ( alertDialog!=null && alertDialog.isShowing() ){
            alertDialog.dismiss();
        }
        if ( dialog!=null && dialog.isShowing() ){
            dialog.dismiss();
        }

    }

    /**
     * checkLotterySaved L?? ph????ng th???c ki???m tra khi kh??ng c?? internet
     * n???u kh??ng c?? k???t qu??? c???a ????? 30 ng??y th?? kh??ng cho ph??p ng?????i d??ng d?? s??? v?? s??? sai k???t qu???.
     */
    private boolean checkLotterySaved(boolean noInternet) {
        List<Long> listNgayXoSo;
        TemporaryFileDBTraditionalLottery traditionalLotteryDB = new TemporaryFileDBTraditionalLottery(getApplicationContext());
        listNgayXoSo = traditionalLotteryDB.getListNgayXoSo();

        if(listNgayXoSo != null && listNgayXoSo.size() > 0)
        {
            // ki???m tra v?? ?????m c??c ng??y l???n h??n ng??y h???t h???n l??nh th?????ng v?? s??? t??nh ?????n h??m nay
            String ngayHetHang = getSubDate(30);
            long ngayHetHangLong = convertDateToMillisecond(ngayHetHang);
            int tongNgay = 0;
            for (long ngayLayRa : listNgayXoSo)
            {
                if(ngayLayRa >= ngayHetHangLong)
                {
                    tongNgay += 1;
                    Log.d("NgayHetHang", "ngayHetHang = " + ngayHetHang + "  ngayHetHangLong = " + ngayHetHangLong + " Ngay Lay Ra = "+ convertLongToDate(ngayLayRa)  + " ngayLayRa = "+ ngayLayRa);
                }
            }

            if((Integer.parseInt(getDateNow(DEFAULT_HHmm)) <= DEFAULT_HOUR_MINUTES) && tongNgay == 30)
            {
                countDateSaved = 30;
                //Toast.makeText(this, "???? l??u ????? k???t qu??? trong 30 ng??y.", Toast.LENGTH_SHORT).show();
                if(noInternet)
                {
                    Log.d("TemporaryFileDBManager", "no internet! startActivityDashboard(50)");
                    // Start home activity
                    startActivityDashboard(50);
                }
                return true;
            }else if(Integer.parseInt(getDateNow(DEFAULT_HHmm)) >= DEFAULT_HOUR_MINUTES)
            {
                if(tongNgay == 30 || tongNgay == 31)
                {
                    countDateSaved = 30;
                }
                return false;
            }
            else {
                if(tongNgay == 30 || tongNgay == 31)
                {
                    countDateSaved = 30;
                    return false;
                }
                return false;
            }
        }else {
            return false;
        }
    }


    /**
     * checkLotterySavedWhenInternet L?? ph????ng th???c ki???m tra h??m nay ???? t???i gi??? x??? s??? ch??a
     * n???u t???i gi??? x??? s??? th?? mi???n n??o ???? c?? k???t qu??? v?? t??nh to??n ????? ch??? t???i k???t qu??? ???? x??? c???a h??m nay ????? gi??p ng???n qu?? tr??nh t???i
     */
    private String checkLotterySavedWhenInternet() {

        List<Long> listNgayXoSoMienNam, listNgayXoSoMienTrung, listNgayXoSoMienBac;
        TemporaryFileDBTraditionalLottery traditionalLotteryDB = new TemporaryFileDBTraditionalLottery(getApplicationContext());
        listNgayXoSoMienNam = traditionalLotteryDB.getListNgayXoSo3Mien(KET_QUA_MIEN_NAM);
        listNgayXoSoMienTrung = traditionalLotteryDB.getListNgayXoSo3Mien(KET_QUA_MIEN_TRUNG);
        listNgayXoSoMienBac = traditionalLotteryDB.getListNgayXoSo3Mien(KET_QUA_MIEN_BAC);

        if(listNgayXoSoMienNam.size() > 0
                && listNgayXoSoMienTrung.size() > 0
                && listNgayXoSoMienBac.size() > 0)
        {

            int tongNgayMN = getSumDate(listNgayXoSoMienNam);
            int tongNgayMT = getSumDate(listNgayXoSoMienTrung);;
            int tongNgayMB = getSumDate(listNgayXoSoMienBac);;

            if((Integer.parseInt(getDateNow(DEFAULT_HHmm)) <= DEFAULT_HOUR_MINUTES))
            {
                if(tongNgayMN == 30 && tongNgayMT == 30 && tongNgayMB == 30)
                {
                    countDateSaved = 30;
                    return "Saved";
                }else
                {
                    String failed ="";
                    if(tongNgayMN <30)
                        failed += "MNN"; // Mi???n Nam ch??a ?????y ????? k???t qu??? x??? s???
                    if(tongNgayMT <30)
                        failed += "MTT"; // Mi???n Trung ch??a ?????y ????? k???t qu??? x??? s???
                    if(tongNgayMB <30)
                        failed += "MBB"; // Mi???n B???c ch??a ?????y ????? k???t qu??? x??? s???

                    return failed;
                }

            } else
            {
                if(tongNgayMN >= 30 && tongNgayMT >= 30 && tongNgayMB >= 30)
                {
                    if(Integer.parseInt(getDateNow(DEFAULT_HH)) >= 18)
                    {
                        if(Integer.parseInt(getDateNow(DEFAULT_HH)) >= 19)
                        {
                            String failed ="";
                            if(tongNgayMN != 31)
                                failed += "MN";
                            if(tongNgayMT != 31)
                                failed += "MT";
                            if(tongNgayMB != 31)
                                failed += "MB";

                            if(failed.isEmpty()) // c??c k???t qu??? ???? ???????c t???i ?????y ?????
                                return "Saved";

                            return failed; // c?? mi???n n??o ???? ch??a t???i ????? k???t qu???
                        }else {
                            String failed ="";
                            if(tongNgayMN != 31)
                                failed += "MN";
                            if(tongNgayMT != 31)
                                failed += "MT";

                            if(failed.isEmpty()) // c??c k???t qu??? ???? ???????c t???i ?????y ?????
                                return "Saved";

                            return failed; // c?? mi???n n??o ???? ch??a t???i ????? k???t qu???
                        }
                    }else {
                        if(tongNgayMN != 31)
                            return "MN";
                        else
                            return "Saved"; // kh??ng c???n t???i th??m ????i n??o h???t
                    }
                }else {
                    return "Failed"; // n???u Failed th?? t???i l???i t???t c??? 3 mi???n x??? s???
                }
            }
        }else {
            return "Failed";
        }
    }

    private int getSumDate(List<Long> listNgayXoSo)
    {
        // ki???m tra v?? ?????m c??c ng??y l???n h??n ng??y h???t h???n l??nh th?????ng v?? s??? t??nh ?????n h??m nay
        String ngayHetHang = getSubDate(30);
        long ngayHetHangLong = convertDateToMillisecond(ngayHetHang);

        int tongNgay = 0;
        for (long ngayLayRa : listNgayXoSo)
        {
            if(ngayLayRa >= ngayHetHangLong)
            {
                tongNgay += 1;
                Log.d("NgayHetHang", "ngayHetHang = " + convertLongToDate(ngayHetHangLong) + "  ngayHetHangLong = " + ngayHetHangLong + " Ngay Lay Ra = "+ convertLongToDate(ngayLayRa)  + " ngayLayRa = "+ ngayLayRa);
            }
        }
        return tongNgay;
    }

    /**
     * startActivityDashboard l?? ph????ng th???c n??n l???i 0,3s ????? m??? Intent Dashboard
     */
    private void startActivityDashboard(int milliSecond)
    {
        // Start home activity
        try {
            Thread.sleep(milliSecond);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        startActivity(new Intent(LaunchScreen.this, Dashboard.class));
        // close splash activity
        finish();
    }

    /**
     * Method: getTraditionalFromRSS l?? ph????ng th???c l???y k???t qu??? x??? s??? t??? RSS
     * @param domainLottery T??n ????i x??? s??? c???n l???y RSS
     * @param pubDate Ng??y x??? s???
     * @param ResultsLottery k???t qu??? x??? s???
     * @param domain mi???n c???a k???t qu??? x??? s???
     * @return
     */
    private void getTraditionalFromRSS (String domainLottery, String pubDate, String ResultsLottery, String domain)
    {
        traditionalLottery.setTEN_DAI(domainLottery);
        traditionalLottery.setMIEN_XO_SO(domain);
        traditionalLottery.setNGAY_XO_SO(pubDate);
        setResutlLottery(ResultsLottery);
    }

    /**
     * Method: setResutlLottery l?? ph????ng th???c g??n gi?? tr??? k???t qu??? cho c??c gi???i khi ???? c?? d??? li???u k???t qu???.
     * @param resultsLottery
     */
    private void setResutlLottery( String resultsLottery) {

        if(resultsLottery.contains("<![CDATA["))
        {
            if(!resultsLottery.startsWith("<![CDATA[ GDB: -1"))
            {
                String [] listResults = resultsLottery.split(",");
                if(listResults.length == 8)
                {
                    Log.d("www", resultsLottery);
                /*
                <![CDATA[ GDB: 49255,
                 G1: 06649,
                  G2: 23570 - 97897,
                   G3: 17815 - 78585 - 28443 - 63237 - 25403 - 81764,
                    G4: 0137 - 4313 - 4219 - 2492,
                     G5: 5514 - 5159 - 5418 - 2343 - 9268 - 8470,
                      G6: 089 - 068 - 072,
                       G7: 64 - 50 - 81 - 58 ]]>
                 */

                    giaiDB = listResults[0].replace("<![CDATA[ GDB:","").trim(); //  <![CDATA[ GDB: 49255,
                    giaiNhat = listResults[1].replace("G1:","").trim(); // G1: 06649,
                    giaiNhi = listResults[2].replace("G2:","").trim(); // G2: 23570 - 97897,
                    giaiBa = listResults[3].replace("G3:","").trim(); //  G3: 17815 - 78585 - 28443 - 63237 - 25403 - 81764,
                    giaiTu = listResults[4].replace("G4:","").trim(); // G4: 0137 - 4313 - 4219 - 2492,
                    giaiNam = listResults[5].replace("G5:","").trim(); // G5: 5514 - 5159 - 5418 - 2343 - 9268 - 8470,
                    giaiSau = listResults[6].replace("G6:","").trim(); //  G6: 089 - 068 - 072,
                    giaiBay = listResults[7].replace("G7:","").replace("]]>","").trim(); // G7: 64 - 50 - 81 - 58 ]]>
                    giaiTam = "100";
                    setTraditionalLottery();
                }
                else {
                    Log.d("TAG"," Kh??ng ????? k???t qu??? x??? s??? c???a ????i n??y ho???c k???t qu??? x??? s??? b??? l???i! " + resultsLottery);
                }
            }
            else {
                Log.d("TAG"," startsWith(<![CDATA[ GDB: -1) ");
            }
        }
        else {
            String [] listResults = resultsLottery.split(":");
            Log.d("wwwe", listResults.length + "");
            if(listResults.length == 10)
            {
                Log.d("www", resultsLottery);
                /*
                ??B
                 9456921
                  671792
                   219803
                    79153 - 566604
                     14454 - 19922 - 88612 - 76890 - 67529 - 01661 - 993925
                      27626
                       2790 - 5561 - 26957
                        8978
                         42
                */

                giaiDB = listResults[1].substring(0,listResults[1].length()-1).trim(); // 945692 1
                giaiNhat = listResults[2].substring(0,listResults[2].length()-1).trim(); // 67179 2
                giaiNhi = listResults[3].substring(0,listResults[3].length()-1).trim(); // 21980 3
                giaiBa = listResults[4].substring(0,listResults[4].length()-1).trim(); // 79153 - 56660 4
                giaiTu = listResults[5].substring(0,listResults[5].length()-1).trim(); // 14454 - 19922 - 88612 - 76890 - 67529 - 01661 - 99392 5
                giaiNam = listResults[6].substring(0,listResults[6].length()-1).trim(); // 2762 6
                giaiSau = listResults[7].substring(0,listResults[7].length()-1).trim(); // 2790 - 5561 - 2695 7
                giaiBay = listResults[8].substring(0,listResults[8].length()-1).trim(); // 897 8
                giaiTam = listResults[9].trim(); // 42
                Log.d("giaiDB", giaiBay );
                setTraditionalLottery();

            }else {
                Log.d("TAG"," Kh??ng ????? k???t qu??? x??? s??? c???a ????i n??y ho???c k???t qu??? x??? s??? b??? l???i! " + resultsLottery);
            }
        }
    }

    /**
     * Method: setTraditionalLottery l?? ph????ng th???c g??n c??c k???t qu??? v??o ?????i t?????ng TraditionalLottery
     */
    private void setTraditionalLottery()
    {
        traditionalLottery.setKET_QUA_DB(giaiDB);
        traditionalLottery.setKET_QUA_G1(giaiNhat);
        traditionalLottery.setKET_QUA_G2(giaiNhi);
        traditionalLottery.setKET_QUA_G3(giaiBa);
        traditionalLottery.setKET_QUA_G4(giaiTu);
        traditionalLottery.setKET_QUA_G5(giaiNam);
        traditionalLottery.setKET_QUA_G6(giaiSau);
        traditionalLottery.setKET_QUA_G7(giaiBay);
        traditionalLottery.setKET_QUA_G8(giaiTam);
    }


    /**
     * RSSGetResultsLottery ????y l?? ph????ng th???c gi??p t???i v??? k???t qu??? x??? s??? c???a t???t c??c c??c ????i trong 30 ng??y v???a qua
     */
    public class RSSGetResultsLottery extends AsyncTask<String, Void, String> {
        String  domainLottery, // t??n ????i x??? s???
                URL, // ???????ng d???n rss c???a k???t qu??? ????i x??? s???
                domain, // t??n mi???n c???a k???t qu??? x??? s???
                oneDay; // L???y k???t qu??? x??? s??? c???a m???t ng??y duy nh???t

        public RSSGetResultsLottery(  String domainLottery, String URL, String domain , String oneDay) {
            this.domainLottery = domainLottery;
            this.URL = URL;
            this.domain = domain;
            this.oneDay = oneDay;
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            String result = "";
            try {
                URL url = new URL(URL);
                Log.i("cccd",url.toString() );

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
                int code = urlConnection.getResponseCode();

                if(code == 200){
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));
                    String line = "";

                    while ((line = bufferedReader.readLine()) != null)
                        result += line;

                    bufferedReader.close();
                }else {
                    // code = 503 Xin loi! Hien tai server bi qua tai. ban vui long tro lai sau
                    //Thong thuong loi nay chi la tam thoi. Ban co the tro lai sau it phut
                    result = "";
                    Log.d("aaaaca","code = " + code + " domainLinkLottery = " + URL);
                }
                Log.d("xxx", result);
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                urlConnection.disconnect();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            JSONObject jsonObj = null;
            try {

                jsonObj = XML.toJSONObject(result);

                JSONObject jsonObjRSS = jsonObj.getJSONObject("rss");
                JSONObject jsonObjChannel = jsonObjRSS.getJSONObject("channel");
                Log.d("xxxaaaa", jsonObjChannel.toString());
                JSONArray GPSJson = jsonObjChannel.getJSONArray("item"); // 375 item t???ng t???t c??? k???t qu??? c???ac??c ????i

                demTongDaiDaTai += 1; // ?????m s??? ????i ???? t???i ho??n t???t
                //if(demTongDaiDaTai == (demSizeDai - 2)) // demSizeDai - 2 l?? 2 gi???i vietlott
                if(demTongDaiDaTai == demSizeDai) // demSizeDai - 2 l?? 2 gi???i vietlott
                {
                    //Toast.makeText(getApplicationContext(), "???? t???i d??? li???u ho??n t???t", Toast.LENGTH_SHORT).show();
                    Log.d("xxxaaaa", "???? t???i d??? li???u ho??n t???t"+ giaiSau);
                    checkDownloaded = true;
                    dialog.dismiss();
                    startActivityDashboard(10);
                }
                Log.d("xxxaaaa", "demTongDaiDaTai = "+ demTongDaiDaTai + " demSizeDai = " + (demSizeDai-2));

                if (GPSJson.length() != 0)
                {

                    TemporaryFileDBTraditionalLottery dbTraditionalLottery = new TemporaryFileDBTraditionalLottery(getApplicationContext());
                    for (int i = 0; i < GPSJson.length(); i++) {

                        Gson gson = new Gson();
                        String a = GPSJson.get(i).toString();
                        Log.d("aaaaa", a);
                        LotteryResults lotteryResults = gson.fromJson(String.valueOf(GPSJson.get(i)), LotteryResults.class);

                        String pubDate = "";
                        String resultsLottery = "";
                        traditionalLottery = new TraditionalLottery();
                        // listCall.add(callHistory);
                        if(URL.contains("https://xskt.com.vn"))
                        {
                            int positionNgay = lotteryResults.getLink().indexOf("ngay-");

                            //https://xskt.com.vn/xsbth/ngay-25-2-2021
                            // Toast.makeText(LaunchScreen.this, " Description " + lotteryResults.getDescription(), Toast.LENGTH_LONG).show();
                            try {

                                pubDate = formatDate(lotteryResults.getLink().substring(positionNgay + 5).trim());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Log.d("checkDateMN","T??n ????i = " + domainLottery + " ng??y = " + pubDate);
                            Log.d("checkDateMN","T??n ????i = " + domainLottery + " Description " + lotteryResults.getDescription());
                            traditionalLottery.setNGAY_XO_SO(pubDate);

                            // Log.d("xxxaaaa","T??n ????i = " +  getDateNow() + " ng??y = " + pubDate);
                            /*
                            ??B: 9456921: 671792: 219803: 79153 - 566604: 14454 - 19922 - 88612 - 76890 - 67529 - 01661 - 993925: 27626: 2790 - 5561 - 26957: 8978: 42
                             */
                          /*  getTraditionalFromRSS (domainLottery, pubDate, lotteryResults.getDescription(), domain);
                            // Xu???t ra k???t qu??? x??? s??? ???? l???y ???????c
                            TraditionalLottery.TraditionalLotteryToString(traditionalLottery);*/

                        }else {
                            traditionalLottery.setNGAY_XO_SO(lotteryResults.getPubDate());
                            try {
                                pubDate = formatDateStart(lotteryResults.getPubDate(), DEFAULT_DATE, true); // ch??ng t??i d??ng formatDateStart() ????? chuy???n ?????i t??? dd/mm/yyyy sang dd-mm-yyyy.
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Log.d("checkDateMB","T??n ????i = " + domainLottery + " pubDate " + pubDate);
                             /*
                                <![CDATA[ GDB: 49255, G1: 06649, G2: 23570 - 97897, G3: 17815 - 78585 - 28443 - 63237 - 25403 - 81764, G4: 0137 - 4313 - 4219 - 2492, G5: 5514 - 5159 - 5418 - 2343 - 9268 - 8470, G6: 089 - 068 - 072, G7: 64 - 50 - 81 - 58 ]]>
                               */
                        }

                        if(getMillisecondNow() - convertDateToMillisecond(pubDate) < (ONEDAY * 32))
                        {
                            if(oneDay == null)
                            {
                                getTraditionalFromRSS (domainLottery, pubDate, lotteryResults.getDescription(), domain);
                                if(traditionalLottery != null )
                                {
                                    if(traditionalLottery.getKET_QUA_DB() != null && !traditionalLottery.getKET_QUA_DB().isEmpty())
                                    {
                                        Log.d("yyyy", " getKET_QUA_DB = "+ traditionalLottery.getKET_QUA_DB());
                                        // Xu???t ra k???t qu??? x??? s??? ???? l???y ???????c
                                        TraditionalLottery.TraditionalLotteryToString(traditionalLottery);
                                        // Th??m k???t qu??? x??? s??? v??o databases SQLite
                                        dbTraditionalLottery.appendTraditionalLottery(traditionalLottery);
                                    }

                                }
                            }
                            else { // ch??? l??u k???t qu??? x??? s??? c???a h??m nay
                                if(pubDate.equals(oneDay))
                                {
                                    getTraditionalFromRSS (domainLottery, pubDate, lotteryResults.getDescription(), domain);

                                    if(traditionalLottery != null )
                                    {
                                        if(traditionalLottery.getKET_QUA_DB() != null && !traditionalLottery.getKET_QUA_DB().isEmpty())
                                        {
                                            Log.d("yyyy", " getKET_QUA_DB1 = "+ traditionalLottery.getKET_QUA_DB());
                                            // Xu???t ra k???t qu??? x??? s??? ???? l???y ???????c
                                            TraditionalLottery.TraditionalLotteryToString(traditionalLottery);
                                            // Th??m k???t qu??? x??? s??? v??o databases SQLite
                                            dbTraditionalLottery.appendTraditionalLottery(traditionalLottery);
                                        }

                                    }
                                }
                            }
                        }else {
                            Log.d("xxxx", "ng??y x??? s??? n??y ???? h???t h???n d?? v?? s???: "+ pubDate + " = " + ((getMillisecondNow() - convertDateToMillisecond(pubDate))/ONEDAY));
                        }
                        Log.d("yyyy", " PubDate = "+ pubDate + " ===== " + getMillisecondNow() + " - " + convertDateToMillisecond(pubDate) + " = " + ((getMillisecondNow() - convertDateToMillisecond(pubDate))/ONEDAY));
                    }

                    /*List<TraditionalLottery> traditionalLotteryList = dbTraditionalLottery.TraditionalLotteryDBReadWithTime(5, "");
                    Log.d("zsize", "Size = " + traditionalLotteryList.size() + "");*/
                    // Log.d("zsize", "Size = " + 1 + "");
                }
            } catch (JSONException e) {
                Log.e("aaaaa", e.getMessage());
                e.printStackTrace();
            }

            Log.d("bbbbb", result);

            Log.d("cccc", jsonObj.toString());

            super.onPostExecute(result);
        }
    }
}
