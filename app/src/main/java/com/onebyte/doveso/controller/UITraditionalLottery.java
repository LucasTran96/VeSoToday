package com.onebyte.doveso.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.onebyte.doveso.R;
import com.onebyte.doveso.adapter.AdapterResultsLottery;
import com.onebyte.doveso.model.TraditionalLottery;
import com.onebyte.doveso.model.WinningLottery;
import com.onebyte.doveso.temporaryfiledbmanager.TemporaryFileDBTraditionalLottery;
import com.r0adkll.slidr.Slidr;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import static com.onebyte.doveso.api.ApiMethod.getColoredSpanned;
import static com.onebyte.doveso.api.ApiMethod.setEditTextMaxLength;
import static com.onebyte.doveso.contants.Global.DAC_BIET;
import static com.onebyte.doveso.contants.Global.GIAI_1;
import static com.onebyte.doveso.contants.Global.GIAI_2;
import static com.onebyte.doveso.contants.Global.GIAI_3;
import static com.onebyte.doveso.contants.Global.GIAI_4;
import static com.onebyte.doveso.contants.Global.GIAI_5;
import static com.onebyte.doveso.contants.Global.GIAI_6;
import static com.onebyte.doveso.contants.Global.GIAI_7;
import static com.onebyte.doveso.contants.Global.GIAI_8;
import static com.onebyte.doveso.contants.Global.KET_QUA_MIENNAM_MIENTRUNG;
import static com.onebyte.doveso.contants.Global.KET_QUA_MIEN_BAC;
import static com.onebyte.doveso.contants.Global.KET_QUA_MIEN_TRUNG;
import static com.onebyte.doveso.contants.Global.KET_QUA_VIETLOTT_6_45;
import static com.onebyte.doveso.contants.Global.KET_QUA_VIETLOTT_6_55;
import static com.onebyte.doveso.contants.Global.KHUYEN_KHICH_MB;
import static com.onebyte.doveso.contants.Global.KHUYEN_KHICH_MN;
import static com.onebyte.doveso.contants.Global.PHU_DAC_BIET;

public class UITraditionalLottery extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText edt_Code; // EditText ????? m???i ng?????i nh???p m?? s??? c???n d?? c???a t??? v?? s???.
    private ImageView img_Closes_Code; // ImageView ????? x??a m?? s??? ng?????i d??ng nh???p khi mu???n d?? m?? v?? s??? m???i kh??c.
    private LinearLayout lnl_Guide; // LinearLayout ????? hi???n th??? h?????ng d???n cho ng?????i d??ng c??ch d?? v?? s??? nhanh. Khi b???t ?????u d?? th?? s??? ???n lnl_Guide n??y.
    private LinearLayout lnl_Results_Lottery; // lnl_Results_Lottery ????? ???n ho???c hi???n k???t qu??? x??? s??? khi ng?????i d??ng b???m v??o d?? v?? s???.
    private Button btn_Results; // button k???t qu??? x??? s???
    private TextView  txt_Truy_Van_Do_Ve_So;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private List<WinningLottery>traditionalLotteryListResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traditional_lottery);
        setID();
        setEvent();
        // The gallery returns to the main screen
        Slidr.attach(this);
    }

    private void setID() {

        edt_Code = findViewById(R.id.edt_Code);
        img_Closes_Code = findViewById(R.id.img_Closes_Code);
        img_Closes_Code.setVisibility(View.GONE);
        //????y l?? ph????ng th???c ki???m tra v?? set l???a ch???n m???c ?????nh cho lo???i v?? s??? c???n d??.
        setEditTextMaxLength(edt_Code, 6);

        lnl_Guide = findViewById(R.id.lnl_Guide);
        lnl_Results_Lottery = findViewById(R.id.lnl_Results_Lottery);
        lnl_Results_Lottery.setVisibility(View.GONE);
        btn_Results = findViewById(R.id.btn_Results);

        txt_Truy_Van_Do_Ve_So = findViewById(R.id.txt_Truy_Van_Do_Ve_So);
//        img_gif_Ket_Qua_Xo_So = findViewById(R.id.img_gif_Ket_Qua_Xo_So);
        toolbar = findViewById(R.id.toolbar_Traditional_Lottery);
        toolbar.setTitle(getApplicationContext().getResources().getString(R.string.app_name_alias));
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        traditionalLotteryListResults = new ArrayList<>();
        mRecyclerView = findViewById(R.id.rcv_Results_Lottery);
        mRecyclerView.setHasFixedSize(true);

        AdRequest adRequest = new AdRequest.Builder().build();
        // AdSize adSize = new AdSize(320, 100);
        AdView mAdView = findViewById(R.id.adView_Traditional);
        mAdView.loadAd(adRequest);

    }

    private void setEvent() {

        edt_Code.setOnEditorActionListener(new TextView.OnEditorActionListener() {

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

        btn_Results.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnl_Guide.setVisibility(View.GONE);
                lnl_Results_Lottery.setVisibility(View.VISIBLE);
                traditionalLotteryListResults = new ArrayList<>();

                // Check if no view has focus:
               /* InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);*/
                try  {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    e.getMessage();
                }

                int valueLottery = checkValidLottery();
                //Toast.makeText(UITraditionalLottery.this, edt_Code.getText().toString().trim(), Toast.LENGTH_SHORT).show();
                //txt_Results.setText("");
                if(valueLottery != 15)
                {
                    img_Closes_Code.setVisibility(View.VISIBLE);
                    switch (valueLottery)
                    {
                        case KET_QUA_MIENNAM_MIENTRUNG:
                        {
                            /*
                             B???n ???? truy v???n d?? k???t qu??? v?? s??? nhanh trong 30 ng??y g???n nh???t c???a %s.
                             Lo???i v?? %s ch??? s??? m???nh gi?? 10,000??. \nD??y s??? c???a b???n l??: %s
                             */
                            txt_Truy_Van_Do_Ve_So.setText(getString(R.string.truy_van_do_ve_so,"30", "Mi???n Nam, Mi???n Trung", "6 ch??? s???", edt_Code.getText()));
                            // Ng?????i d??ng mu???n d?? v?? s??? Mi???n Nam, Mi???n Trung
                            getResultsLottery(edt_Code.getText().toString().trim(), KET_QUA_MIENNAM_MIENTRUNG);
                            break;
                        }
                        case KET_QUA_MIEN_BAC:
                        {
                            txt_Truy_Van_Do_Ve_So.setText(getString(R.string.truy_van_do_ve_so,"30", "Mi???n B???c", "5 ch??? s???", edt_Code.getText()));
                            getResultsLottery(edt_Code.getText().toString().trim(), KET_QUA_MIEN_BAC);
                            // Ng?????i d??ng mu???n d?? v?? s??? Mi???n B???c
                            break;
                        }
                        case KET_QUA_VIETLOTT_6_45:
                        {
                            txt_Truy_Van_Do_Ve_So.setText(getString(R.string.truy_van_do_ve_so,"60", "Vietlott","6 c???p s???", edt_Code.getText()));
                            getResultsLottery(edt_Code.getText().toString().trim(), KET_QUA_VIETLOTT_6_45);
                            // Ng?????i d??ng mu???n d?? v?? s??? Vietlott 6/45
                            break;
                        }
                        case KET_QUA_VIETLOTT_6_55:
                        {

                            txt_Truy_Van_Do_Ve_So.setText(getString(R.string.truy_van_do_ve_so,"60", "Vietlott", "7 c???p s???", edt_Code.getText()));
                            // Ng?????i d??ng mu???n d?? v?? s??? Vietlott 6/55
                            break;
                        }
                    }
                    // cho ra k???t qu??? ??? ????y
                }
            }
        });

        img_Closes_Code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_Code.setText("");
                img_Closes_Code.setVisibility(View.GONE);
            }
        });
    }

    /**
     * Method: getResultsLottery ????y l?? ph????ng th???c d?? v?? s??? Mi???n Nam, Mi???n Trung, Mi???n B???c.
     * @param codeLottery m?? v?? s??? m?? ng?????i d??ng c???n d??.
     */
    @SuppressLint("SetTextI18n")
    private void getResultsLottery(String codeLottery, int domain) {


        TemporaryFileDBTraditionalLottery dbTraditionalLottery = new TemporaryFileDBTraditionalLottery(getApplicationContext());
        List<TraditionalLottery> traditionalLotteryList;
        List<TraditionalLottery> traditionalLotteryListKK;
        if(domain == KET_QUA_MIENNAM_MIENTRUNG)
        {
            traditionalLotteryList = dbTraditionalLottery.CheckResultsLotteryDBReadWithTime(codeLottery, KET_QUA_MIEN_TRUNG);
            traditionalLotteryListKK = dbTraditionalLottery.CheckResultsLotteryDBReadWithTime(codeLottery, KET_QUA_MIENNAM_MIENTRUNG);

            // ????y l?? ph????ng th???c qu??t ki???m tra xem m?? v?? s??? n??y c?? tr??ng th?????ng gi???i n??o trong 30 ng??y qua hay kh??ng
            checkKetQuaMaVeSo(codeLottery, traditionalLotteryList);

            // ????y l?? ph????ng th???c qu??t ki???m tra xem m?? v?? s??? n??y c?? tr??ng gi???i khuy???n kh??ch n??o trong 30 ng??y qua hay kh??ng
            checkGiaiKhuyeKhich(traditionalLotteryListKK);
        }
        else if(domain == KET_QUA_MIEN_BAC)
        {
            traditionalLotteryList = dbTraditionalLottery.CheckResultsLotteryDBReadWithTime(codeLottery, KET_QUA_MIEN_BAC);
            Log.d("zsize", "Size = " + traditionalLotteryList.size());
            // ????y l?? ph????ng th???c qu??t ki???m tra xem m?? v?? s??? n??y c?? tr??ng th?????ng gi???i n??o c???a Mi???n B???c trong 30 ng??y qua hay kh??ng
            checkKetQuaMaVeSo(codeLottery, traditionalLotteryList);
        }
        else {
            traditionalLotteryList = dbTraditionalLottery.CheckResultsLotteryDBReadWithTime(codeLottery, KET_QUA_MIENNAM_MIENTRUNG);
            Log.d("zsize", "Size = " + traditionalLotteryList.size());
        }

        if(traditionalLotteryList!= null && traditionalLotteryList.size() > 0)
        {
            for (TraditionalLottery traditionalLottery : traditionalLotteryList)
            {
                Log.d("zsize", "T??n ????i = " + traditionalLottery.getTEN_DAI() + " Ng??y = " + traditionalLottery.getNGAY_XO_SO());
            }

        }

        if(traditionalLotteryListResults != null && traditionalLotteryListResults.size()>0)
        {
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);

            // s???p x???p l???i gi???i v???i th??? t??? t??? ?????c bi???t t???i gi???i 8
            Collections.sort(traditionalLotteryListResults, new Comparator<WinningLottery>()
            {
                @Override
                public int compare(WinningLottery lhs, WinningLottery rhs) {

                    return Integer.compare(lhs.getPriority(), rhs.getPriority());
                }
            });

            // adapter
            mAdapter = new AdapterResultsLottery(this, traditionalLotteryListResults);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }else {
            traditionalLotteryListResults.add(new WinningLottery(-1,"imageLottery","","","","","",0));
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);


            // adapter
            mAdapter = new AdapterResultsLottery(this, traditionalLotteryListResults);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

        }
    }

    /**
     * Method: checkKetQuaMaVeSo ????y l?? ph????ng th???c qu??t ki???m tra xem m?? v?? s??? n??y c?? tr??ng th?????ng gi???i n??o trong 30 ng??y qua hay kh??ng
     * @param codeLottery m?? v?? s???
     * @param traditionalLotteryList danh s??ch c??c k???t qu??? x??? s??? trong 30 ng??y g???n nh???t.
     */
    private void checkKetQuaMaVeSo(String codeLottery, List<TraditionalLottery> traditionalLotteryList) {
        if(traditionalLotteryList!= null && traditionalLotteryList.size() > 0)
        {
            for (TraditionalLottery traditionalLottery : traditionalLotteryList)
            {
                List<String> listTongKetQua;
                if(codeLottery.length() == 5)
                    listTongKetQua = KiemTraGiaiTrungThuongMB(codeLottery, traditionalLottery);
                else // codeLottery.length() == 6
                    listTongKetQua = KiemTraGiaiTrungThuongMN(codeLottery, traditionalLottery);

                if(listTongKetQua.size() > 0)
                {
                    //// c??ch t?? m??u text
                    //        String first = getColoredSpanned("First Color", "#cc0029");
                    //        String second = getColoredSpanned("Second Color","#ffcc00");
                    //        txt_Results.setText(Html.fromHtml(first + " " + second));
                    for (String ketQua : listTongKetQua) {
                        String [] splitKetQua = ketQua.split("-");
                        //String Giai = getColoredSpanned(splitKetQua[0], "#cc0029");
                        String maVeSo = getColoredSpanned(splitKetQua[1], "#cc0029");
                       /* txt_Results.append(Html.fromHtml("Ch??c m???ng b???n !..." +
                                "\nV?? s??? c???a b???n ???? tr??ng th?????ng gi???i " + Giai + " v???i c???p s??? "+ maVeSo + //Khuy???n Kh??ch
                                "\nT???ng gi?? tr??? gi???i th?????ng l??: xxxxx1??"
                                + "\n????i: "+ traditionalLottery.getTEN_DAI() + " Ng??y: "+ traditionalLottery.getNGAY_XO_SO() + " M?? V?? s???: "+ codeLottery));*/
                        Log.d("gii", "gi???i = " +splitKetQua[0]);
                       traditionalLotteryListResults.add(new WinningLottery(traditionalLottery.getROW_ID(),
                               splitKetQua[0],
                               maVeSo,
                               traditionalLottery.getTEN_DAI(),
                               traditionalLottery.getMIEN_XO_SO(),
                               traditionalLottery.getNGAY_XO_SO(),
                               codeLottery,
                               setPriority(splitKetQua[0])));

                       //WinningLottery(int rowID, String prize, String codePrizeLottery, String domainLottery, String dateLottery, String codeLottery)
                    }
                }
            }
        }
    }

    /**
     * Method: setPriority l?? ph????ng th???c ki???m tra ????? ??u ti??n c???a gi???i th?????ng ???? tr??ng.
     */
    public static int setPriority(String prize)
    {
        int priority;
        switch (prize)
        {
            case DAC_BIET:
            {
                priority = 0;
                break;
            }
            case PHU_DAC_BIET:
            {
                priority = 1;
                break;
            }
            case GIAI_1:
            {
                priority = 2;
                break;
            }
            case GIAI_2:
            {
                priority = 3;
                break;
            }
            case GIAI_3:
            {
                priority = 4;
                break;
            }
            case KHUYEN_KHICH_MN:
            {
                priority = 5;
                break;
            }
            case GIAI_4:
            {
                priority = 6;
                break;
            }
            case GIAI_5:
            {
                priority = 7;
                break;
            }
            case GIAI_6:
            {
                priority = 8;
                break;
            }
            case GIAI_7:
            {
                priority = 9;
                break;
            }
            case GIAI_8:
            {
                priority = 10;
                break;
            }
            case KHUYEN_KHICH_MB:
            {
                priority = 11;
                break;
            }
            default:
            {
                priority = 0;
            }
        }
        return priority;
    }

    private void checkGiaiKhuyeKhich(List<TraditionalLottery> traditionalLotteryList)
    {
        if(traditionalLotteryList!= null && traditionalLotteryList.size() > 0)
        {
            for (TraditionalLottery traditionalLottery : traditionalLotteryList)
            {

                if(edt_Code.length() == 6)
                {
                    String codeLottery = edt_Code.getText().toString();
                    int positionSoKhongTrung = checkConsolationPrizes(codeLottery, traditionalLottery.getKET_QUA_DB());
                    Log.d("checkcc", "Check = " + positionSoKhongTrung + " K???t qu??? DB = "+ traditionalLottery.getKET_QUA_DB() + " M?? v?? s??? = " + edt_Code.getText().toString());
                    if(positionSoKhongTrung !=0)
                    {

                        String Giai = getColoredSpanned("Gi???i Khuy???n Kh??ch", "#cc0029");
                       /* txt_Results.append(Html.fromHtml("Ch??c m???ng b???n !..." +
                                "\nV?? s??? c???a b???n ???? tr??ng th?????ng " + Giai +
                                " v???i c???p s??? "+ toMauKetQuaDung(splitKetQua[1].trim(), edt_Code.getText().toString().trim())
                                +"\nT???ng gi?? tr??? gi???i th?????ng l??: xxxxxx????\n"
                                + " ????i: "+ traditionalLottery.getTEN_DAI() + " Ng??y: "+ traditionalLottery.getNGAY_XO_SO() + " M?? V?? s???: "+ edt_Code.getText().toString()));*/

                        traditionalLotteryListResults.add(new WinningLottery(traditionalLottery.getROW_ID(),
                                KHUYEN_KHICH_MN,
                                (KHUYEN_KHICH_MN + positionSoKhongTrung),
                                traditionalLottery.getTEN_DAI(),
                                traditionalLottery.getMIEN_XO_SO(),
                                traditionalLottery.getNGAY_XO_SO(),
                                codeLottery,
                                setPriority(KHUYEN_KHICH_MN)));
                    }
                }
            }
        }
    }

    private String toMauKetQuaDung(String point, String codeLottery) {
        String start;
        String position;
        String end;
        int positionCheck = Integer.parseInt(point);
        if(positionCheck == 1)
        {
            start = String.valueOf(codeLottery.charAt(0));
            position = String.valueOf(codeLottery.charAt(1));
            end = codeLottery.substring(2);
        }else if(positionCheck == 2)
        {
            start = codeLottery.substring(0,2);
            position = String.valueOf(codeLottery.charAt(2));
            end = codeLottery.substring(3);
        }
        else if(positionCheck == 3)
        {
            start = codeLottery.substring(0,4);
            position = String.valueOf(codeLottery.charAt(3));
            end = codeLottery.substring(4);
        }
        else if(positionCheck == 4)
        {
            start = codeLottery.substring(0,4);
            position = String.valueOf(codeLottery.charAt(4));
            end = codeLottery.substring(5);
        }
        else
        {
            //657739
            start = codeLottery.substring(0,5);
            position = String.valueOf(codeLottery.charAt(5));
            end = "";
        }
        Log.d("startxxx", "Code= "+ codeLottery +" start= " + start + " position = "+ position + " end= "+ end);
        start = getColoredSpanned(start, "#cc0029");
        end = getColoredSpanned(end, "#cc0029");
        return start + position + end;
    }

    /**
     * Method: checkValidLottery ????y l?? ph????ng th???c ki???m tra xem ng?????i d??ng ???? nh???p m?? s??? c???n d?? c???a mi???n n??o.
     * @return 1: v?? s??? mi???n nam, mi???n trung; 2: v?? s??? mi???n b???c; 3: v?? s??? Vietlott 6/45; 4: v?? s??? Vietlott 6/55
     */
    private int checkValidLottery() {

        int domain = 15;
        if(edt_Code.length()<5){
            txt_Truy_Van_Do_Ve_So.setText("M?? v?? s??? c???a b???n nh???p ???? kh??ng h???p l???! B???n c???n nh???p ????? 6 s??? cho x??? s??? Mi???n Nam v?? Mi???n Trung ho???c nh???p 5 s??? cho x??? s??? Mi???n B???c.");
        }
        else if( edt_Code.length()>6 && edt_Code.length()< 11)
        {
            txt_Truy_Van_Do_Ve_So.setText("M?? v?? s??? c???a b???n nh???p ???? kh??ng h???p l???! B???n c???n nh???p ????? 6 s??? cho x??? s??? Mi???n Nam v?? Mi???n Trung ho???c nh???p 5 s??? cho x??? s??? Mi???n B???c.");
        }
        else if(edt_Code.length() == 11 || edt_Code.length() == 13)
        {
            txt_Truy_Van_Do_Ve_So.setText("M?? v?? s??? c???a b???n nh???p ???? kh??ng h???p l???! B???n c???n nh???p ????? 12 s??? cho x??? s??? Vietlott 6/45 ho???c nh???p 14 s??? cho x??? s??? Vietlott 6/55.");
        }
        else if(edt_Code.length() == 6)
        {
            domain = KET_QUA_MIENNAM_MIENTRUNG;
        }
        else if(edt_Code.length() == 5)
        {
            domain = KET_QUA_MIEN_BAC;
        }
        else if(edt_Code.length() == 12)
        {
            domain = KET_QUA_VIETLOTT_6_45;
        }
        else if(edt_Code.length() == 14)
        {
            domain = KET_QUA_VIETLOTT_6_55;
        }
        return domain;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
                super.onBackPressed();
            }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Method checkConsolationPrizes tr??? v??? k???t qu??? c?? tr??ng hay kh??ng v?? v??? tr?? kh??ng tr??ng l?? v??? tr?? th??? m???y.
     * @param codeLottery m?? v?? s???
     * @param KQ_GDB k???t qu??? c???a gi???i ?????c bi???t
     * @return tr??? v??? k???t qu??? c?? tr??ng hay kh??ng v?? v??? tr?? kh??ng tr??ng l?? v??? tr?? th??? m???y.
     */
    public static int checkConsolationPrizes(String codeLottery, String KQ_GDB)
    {
        int positionSoKhongTrung = 0;
            if(codeLottery.charAt(0) == KQ_GDB.charAt(0))
            {
                int dem = 0;
                if(codeLottery.charAt(1) == KQ_GDB.charAt(1))
                    dem += 1;
                else {
                    positionSoKhongTrung = 1;
                }

                if(codeLottery.charAt(2) == KQ_GDB.charAt(2))
                    dem += 1;
                else {
                    positionSoKhongTrung = 2;
                }

                if(codeLottery.charAt(3) == KQ_GDB.charAt(3))
                    dem += 1;
                else {
                    positionSoKhongTrung = 3;
                }

                if(codeLottery.charAt(4) == KQ_GDB.charAt(4))
                    dem += 1;
                else {
                    positionSoKhongTrung = 4;
                }

                if(codeLottery.charAt(5) == KQ_GDB.charAt(5))
                    dem += 1;
                else {
                    positionSoKhongTrung = 5;
                }

                if(dem == 4)
                    return positionSoKhongTrung;
                else
                {
                    return 0;
                }
            }
            else
            {
                return 0;
            }
    }

    /**
     * Method KiemTraGiaiTrungThuongMN l?? ph????ng th???c ki???m tra xem m?? v?? s??? c?? tr??ng gi???i th?????ng mi???n nam n??o hay kh??ng.
     * @param codeLottery
     * @param traditionalLottery
     * @return
     */
    public static List<String> KiemTraGiaiTrungThuongMN(String codeLottery, TraditionalLottery traditionalLottery)
    {

        List<String> listTongSoKetQuaTrung = new ArrayList<>(); // ????y l?? list l??u l???i danh s??ch c??c gi???i ???? tr??ng trong l???n x??? s??? n??y.

        if(traditionalLottery.getKET_QUA_DB().contains(codeLottery))
        {
            // KET_QUA_DB like '657739' --- 657739
            // b???n ???? tr??ng gi???i ?????c bi???t
            listTongSoKetQuaTrung.add(DAC_BIET + "-" + traditionalLottery.getKET_QUA_DB());
        }
        String ketQua5So = codeLottery.substring(1); // l???y ra 5 s??? cu???i c???a m?? s??? d?? v?? s???.
        if(traditionalLottery.getKET_QUA_DB().contains(ketQua5So))
        {
            // KET_QUA_PDB like '%57739' --- X57739
            if(traditionalLottery.getKET_QUA_DB().charAt(0) != codeLottery.charAt(0))
                listTongSoKetQuaTrung.add(PHU_DAC_BIET + "-" + ketQua5So);
            // b???n ???? tr??ng gi???i ph??? ?????c bi???t

        }
        if(traditionalLottery.getKET_QUA_G1().contains(ketQua5So))
        {
            // KET_QUA_G1 like '41553' --- 41553
            // b???n ???? tr??ng gi???i nh???t
            listTongSoKetQuaTrung.add(GIAI_1 + "-" + traditionalLottery.getKET_QUA_G1());
        }
        if(traditionalLottery.getKET_QUA_G2().contains(ketQua5So))
        {
            //KET_QUA_G2 like '18282' --- 24061
            // b???n ???? tr??ng gi???i nh??
            listTongSoKetQuaTrung.add(GIAI_2 + "-" + traditionalLottery.getKET_QUA_G2());
        }
        if(traditionalLottery.getKET_QUA_G3().contains(ketQua5So))
        {
            //KET_QUA_G3 like '%18282%' --- 58553   15723
            String [] split_G3 = traditionalLottery.getKET_QUA_G3().split("-");
            if(split_G3.length == 2)
            {
                // b???n ???? tr??ng gi???i ba
                if(split_G3[0].contains(ketQua5So))
                    listTongSoKetQuaTrung.add(GIAI_3 + "-" + ketQua5So);
                if(split_G3[1].contains(codeLottery.substring(1)))
                    listTongSoKetQuaTrung.add(GIAI_3 + "-" + ketQua5So);

            }else {
               Log.d("ERORR", "L???i ??? l??u gi???i Nh?? c???a ng??y: "+ traditionalLottery.getNGAY_XO_SO() + " c???a ????i: "+ traditionalLottery.getTEN_DAI());
            }

        }
        if(traditionalLottery.getKET_QUA_G4().contains(codeLottery.substring(1)))
        {
            //KET_QUA_G4 like '%18282%' ---  57852 62404 34542 66602 65869 74052 34481
            // b???n ???? tr??ng gi???i b???n
            String [] split_G4 = traditionalLottery.getKET_QUA_G4().split("-");
            if(split_G4.length == 7)
            {
                // b???n ???? tr??ng gi???i t??
                if(split_G4[0].contains(ketQua5So))
                    listTongSoKetQuaTrung.add(GIAI_4 + "-" + ketQua5So);
                if(split_G4[1].contains(ketQua5So))
                    listTongSoKetQuaTrung.add(GIAI_4 + "-" + ketQua5So);
                if(split_G4[2].contains(ketQua5So))
                    listTongSoKetQuaTrung.add(GIAI_4 + "-" + ketQua5So);
                if(split_G4[3].contains(ketQua5So))
                    listTongSoKetQuaTrung.add(GIAI_4 + "-" + ketQua5So);
                if(split_G4[4].contains(ketQua5So))
                    listTongSoKetQuaTrung.add(GIAI_4 + "-" + ketQua5So);
                if(split_G4[5].contains(ketQua5So))
                    listTongSoKetQuaTrung.add(GIAI_4 + "-" + ketQua5So);
                if(split_G4[6].contains(ketQua5So))
                    listTongSoKetQuaTrung.add(GIAI_4 + "-" + ketQua5So);

            }else {
                Log.d("ERORR", "L???i ??? l??u gi???i t?? c???a ng??y: "+ traditionalLottery.getNGAY_XO_SO() + " c???a ????i: "+ traditionalLottery.getTEN_DAI());
            }
        }
        String ketQua4So = codeLottery.substring(2); // l???y ra 4 s??? cu???i c???a m?? s??? d?? v?? s???.

        if(traditionalLottery.getKET_QUA_G5().contains(ketQua4So))
        {
            //KET_QUA_G5 like '8282' --- 0270
            // b???n ???? tr??ng gi???i n??m
            listTongSoKetQuaTrung.add(GIAI_5 + "-" + traditionalLottery.getKET_QUA_G5());
        }
        if(traditionalLottery.getKET_QUA_G6().contains(ketQua4So))
        {
            //KET_QUA_G6 like '%8282%' --- 5112 1214 7396
            // b???n ???? tr??ng gi???i s??u
            String [] split_G6 = traditionalLottery.getKET_QUA_G6().split("-");
            if(split_G6.length == 3)
            {
                // b???n ???? tr??ng gi???i ba
                if(split_G6[0].contains(ketQua4So))
                    listTongSoKetQuaTrung.add(GIAI_6 + "-" + ketQua4So);
                if(split_G6[1].contains(ketQua4So))
                    listTongSoKetQuaTrung.add(GIAI_6 + "-" + ketQua4So);
                if(split_G6[2].contains(ketQua4So))
                    listTongSoKetQuaTrung.add(GIAI_6 + "-" + ketQua4So);

            }else {
                Log.d("ERORR", "L???i ??? l??u gi???i S??u c???a ng??y: "+ traditionalLottery.getNGAY_XO_SO() + " c???a ????i: "+ traditionalLottery.getTEN_DAI());
            }
        }
        if(traditionalLottery.getKET_QUA_G7().trim().equals(codeLottery.substring(3)))
        {
            //KET_QUA_G7 like '282' --- 262
            // b???n ???? tr??ng gi???i b???y
            listTongSoKetQuaTrung.add(GIAI_7 + "-" + traditionalLottery.getKET_QUA_G7());
        }
        if(traditionalLottery.getKET_QUA_G8().trim().equals(codeLottery.substring(4)))
        {
            // KET_QUA_G8 like '00' --- 82
            // b???n ???? tr??ng gi???i t??m
            listTongSoKetQuaTrung.add(GIAI_8 + "-" + traditionalLottery.getKET_QUA_G8());
        }
       return listTongSoKetQuaTrung;
    }

    public static List<String> KiemTraGiaiTrungThuongMB(String codeLottery, TraditionalLottery traditionalLottery)
    {

        List<String> listTongSoKetQuaTrung = new ArrayList<>(); // ????y l?? list l??u l???i danh s??ch c??c gi???i ???? tr??ng trong l???n x??? s??? n??y.

        /**
         * (KET_QUA_G7 like '%22%'
         * or KET_QUA_G6 like '%828%'
         * or KET_QUA_G5 like '%1234%'
         * or KET_QUA_G4 like '%1838%'
         * or KET_QUA_G3 like '%41759%'
         * or KET_QUA_G2 like '%98750%'
         * or KET_QUA_G1 like '98750'
         * or KET_QUA_DB like '%82'
         * or KET_QUA_DB like '18282')
         */

        if(codeLottery.length() == 5)
        {
            if(traditionalLottery.getKET_QUA_DB().trim().equals(codeLottery))
            {
                // KET_QUA_DB like '65779' --- 65773
                // b???n ???? tr??ng gi???i ?????c bi???t
                listTongSoKetQuaTrung.add(DAC_BIET + "-" + traditionalLottery.getKET_QUA_DB());
            }else if(traditionalLottery.getKET_QUA_DB().trim().substring(3).equals(codeLottery.substring(3)))
            {
                // b???n ???? tr??ng gi???i khuy???n kh??ch
                listTongSoKetQuaTrung.add(KHUYEN_KHICH_MB + "-" + traditionalLottery.getKET_QUA_DB().trim().substring(3));
            }
            if(traditionalLottery.getKET_QUA_G1().trim().equals(codeLottery))
            {
                // KET_QUA_G1 like '41553' --- 41553
                // b???n ???? tr??ng gi???i nh???t
                listTongSoKetQuaTrung.add(GIAI_1 + "-"  + traditionalLottery.getKET_QUA_G1());
            }
            if(traditionalLottery.getKET_QUA_G2().contains(codeLottery.substring(1)))
            {
                //KET_QUA_G2 like 23429 - 38390
                // b???n ???? tr??ng gi???i nh??
                String [] split_G2 = traditionalLottery.getKET_QUA_G2().split("-");
                if(split_G2.length == 2)
                {
                    // b???n ???? tr??ng gi???i ba
                    if(split_G2[0].trim().equals(codeLottery))
                        listTongSoKetQuaTrung.add(GIAI_2 + "-" + codeLottery);
                    if(split_G2[1].trim().equals(codeLottery))
                        listTongSoKetQuaTrung.add(GIAI_2 + "-" + codeLottery);
                }else {
                    Log.d("ERORR", "L???i ??? l??u gi???i Nh?? c???a ng??y: "+ traditionalLottery.getNGAY_XO_SO() + " c???a ????i: "+ traditionalLottery.getTEN_DAI());
                }
            }
            if(traditionalLottery.getKET_QUA_G3().contains(codeLottery.substring(1)))
            {
                //KET_QUA_G3 like 32435 - 42574 - 99911 - 09241 - 21291 - 55658
                String [] split_G3 = traditionalLottery.getKET_QUA_G3().split("-");
                if(split_G3.length == 6)
                {
                    // b???n ???? tr??ng gi???i ba
                    if(split_G3[0].trim().equals(codeLottery))
                        listTongSoKetQuaTrung.add(GIAI_3 + "-" + codeLottery);
                    if(split_G3[1].trim().equals(codeLottery))
                        listTongSoKetQuaTrung.add(GIAI_3 + "-" + codeLottery);
                    if(split_G3[2].trim().equals(codeLottery))
                        listTongSoKetQuaTrung.add(GIAI_3 + "-" + codeLottery);
                    if(split_G3[3].trim().equals(codeLottery))
                        listTongSoKetQuaTrung.add(GIAI_3 + "-" + codeLottery);
                    if(split_G3[4].trim().equals(codeLottery))
                        listTongSoKetQuaTrung.add(GIAI_3 + "-" + codeLottery);
                    if(split_G3[5].trim().equals(codeLottery))
                        listTongSoKetQuaTrung.add(GIAI_3 + "-" + codeLottery);
                }else {
                    Log.d("ERORR", "L???i ??? l??u gi???i Nh?? c???a ng??y: "+ traditionalLottery.getNGAY_XO_SO() + " c???a ????i: "+ traditionalLottery.getTEN_DAI());
                }

            }
            String ketQua4So = codeLottery.substring(1);
            if(traditionalLottery.getKET_QUA_G4().contains(codeLottery.substring(1)))
            {
                //KET_QUA_G4 like 4655 - 9197 - 2481 - 8641
                // b???n ???? tr??ng gi???i b???n
                String [] split_G4 = traditionalLottery.getKET_QUA_G4().split("-");
                if(split_G4.length == 4)
                {
                    // b???n ???? tr??ng gi???i t??
                    if(split_G4[0].trim().equals(ketQua4So))
                        listTongSoKetQuaTrung.add(GIAI_4 + "-" + ketQua4So);
                    if(split_G4[1].trim().equals(codeLottery.substring(1)))
                        listTongSoKetQuaTrung.add(GIAI_4 + "-" + ketQua4So);
                    if(split_G4[2].trim().equals(codeLottery.substring(1)))
                        listTongSoKetQuaTrung.add(GIAI_4 + "-" + ketQua4So);
                    if(split_G4[3].trim().equals(codeLottery.substring(1)))
                        listTongSoKetQuaTrung.add(GIAI_4 + "-" + ketQua4So);
                }else {
                    Log.d("ERORR", "L???i ??? l??u gi???i t?? c???a ng??y: "+ traditionalLottery.getNGAY_XO_SO() + " c???a ????i: "+ traditionalLottery.getTEN_DAI());
                }
            }
            if(traditionalLottery.getKET_QUA_G5().contains(codeLottery.substring(1)))
            {
                //KET_QUA_G5 like 4142 - 1733 - 4329 - 9404 - 6699 - 3373
                // b???n ???? tr??ng gi???i n??m
                String [] split_G5 = traditionalLottery.getKET_QUA_G5().split("-");
                if(split_G5.length == 6)
                {
                    // b???n ???? tr??ng gi???i t??
                    if(split_G5[0].trim().equals(ketQua4So))
                        listTongSoKetQuaTrung.add(GIAI_5 + "-" + ketQua4So);
                    if(split_G5[1].trim().equals(ketQua4So))
                        listTongSoKetQuaTrung.add(GIAI_5 + "-" + ketQua4So);
                    if(split_G5[2].trim().equals(ketQua4So))
                        listTongSoKetQuaTrung.add(GIAI_5 + "-" + ketQua4So);
                    if(split_G5[3].trim().equals(ketQua4So))
                        listTongSoKetQuaTrung.add(GIAI_5 + "-" + ketQua4So);
                    if(split_G5[4].trim().equals(ketQua4So))
                        listTongSoKetQuaTrung.add(GIAI_5 + "-" + ketQua4So);
                    if(split_G5[5].trim().equals(ketQua4So))
                        listTongSoKetQuaTrung.add(GIAI_5 + "-" + ketQua4So);
                }else {
                    Log.d("ERORR", "L???i ??? l??u gi???i N??m c???a ng??y: "+ traditionalLottery.getNGAY_XO_SO() + " c???a ????i: "+ traditionalLottery.getTEN_DAI());
                }
            }
            if(traditionalLottery.getKET_QUA_G6().contains(codeLottery.substring(2)))
            {
                //KET_QUA_G6 like 914 - 344 - 224
                // b???n ???? tr??ng gi???i s??u
                String [] split_G6 = traditionalLottery.getKET_QUA_G6().split("-");
                if(split_G6.length == 3)
                {
                    // b???n ???? tr??ng gi???i ba
                    String ketQua3So = codeLottery.substring(2);
                    if(split_G6[0].trim().equals(ketQua3So))
                        listTongSoKetQuaTrung.add(GIAI_6 + "-" + ketQua3So);
                    if(split_G6[1].trim().equals(ketQua3So))
                        listTongSoKetQuaTrung.add(GIAI_6 + "-" + ketQua3So);
                    if(split_G6[2].trim().equals(ketQua3So))
                        listTongSoKetQuaTrung.add(GIAI_6 + "-" + ketQua3So);

                }else {
                    Log.d("ERORR", "L???i ??? l??u gi???i S??u c???a ng??y: "+ traditionalLottery.getNGAY_XO_SO() + " c???a ????i: "+ traditionalLottery.getTEN_DAI());
                }
            }
            if(traditionalLottery.getKET_QUA_G7().contains(codeLottery.substring(3)))
            {
                // b???n ???? tr??ng gi???i b???y
                //KET_QUA_G7 like 54 - 37 - 36 - 65
                String [] split_G7 = traditionalLottery.getKET_QUA_G7().split("-");
                if(split_G7.length == 4)
                {
                    // b???n ???? tr??ng gi???i ba
                    String ketQua2So = codeLottery.substring(3);
                    if(split_G7[0].trim().equals(ketQua2So))
                        listTongSoKetQuaTrung.add(GIAI_7 + "-" + ketQua2So);
                    if(split_G7[1].trim().equals(ketQua2So))
                        listTongSoKetQuaTrung.add(GIAI_7 + "-" + ketQua2So);
                    if(split_G7[2].trim().equals(ketQua2So))
                        listTongSoKetQuaTrung.add(GIAI_7 + "-" + ketQua2So);
                    if(split_G7[3].trim().equals(ketQua2So))
                        listTongSoKetQuaTrung.add(GIAI_7 + "-" + ketQua2So);
                }else {
                    Log.d("ERORR", "L???i ??? l??u gi???i Nh?? c???a ng??y: "+ traditionalLottery.getNGAY_XO_SO() + " c???a ????i: "+ traditionalLottery.getTEN_DAI());
                }
            }

        }

        return listTongSoKetQuaTrung;
    }
}
