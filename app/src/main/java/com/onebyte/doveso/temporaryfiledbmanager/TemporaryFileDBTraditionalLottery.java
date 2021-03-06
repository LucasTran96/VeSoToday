package com.onebyte.doveso.temporaryfiledbmanager;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.LinearLayout;

import com.onebyte.doveso.model.TraditionalLottery;
import java.util.ArrayList;
import java.util.List;

import static com.onebyte.doveso.api.ApiMethod.convertDateToMillisecond;
import static com.onebyte.doveso.api.ApiMethod.formatDateStart;
import static com.onebyte.doveso.api.ApiMethod.getSubDate;
import static com.onebyte.doveso.contants.Global.DEFAULT_DATE_SELECT_YYYY;
import static com.onebyte.doveso.contants.Global.KET_QUA_MIENNAM_MIENTRUNG;
import static com.onebyte.doveso.contants.Global.KET_QUA_MIEN_BAC;
import static com.onebyte.doveso.contants.Global.KET_QUA_MIEN_NAM;
import static com.onebyte.doveso.contants.Global.KET_QUA_MIEN_TRUNG;
import static com.onebyte.doveso.contants.Global.KET_QUA_VIETLOTT_6_45;
import static com.onebyte.doveso.contants.Global.KET_QUA_VIETLOTT_6_55;


public class TemporaryFileDBTraditionalLottery extends TemporaryFileDBManager {

	private final String TAG = "DBManagerForAppLogNew";
	private final String TABLE_NAME = "XOSOTRUYENTHONG";
	private Context context;
	public TemporaryFileDBTraditionalLottery(Context context) {
		super(context);
		this.context = context;
	}

	public void appendTraditionalLottery(TraditionalLottery traditionalLottery)
	{

		try {
			
			open();
			String sqlQuery = "insert into " + TABLE_NAME +"(MIEN_XO_SO, TEN_DAI, NGAY_XO_SO, KET_QUA_G8, " +
					"KET_QUA_G7, KET_QUA_G6, KET_QUA_G5, KET_QUA_G4, KET_QUA_G3, KET_QUA_G2, KET_QUA_G1, KET_QUA_DB) "
					+" select '"+ traditionalLottery.getMIEN_XO_SO() + "', "
					+ "'" + traditionalLottery.getTEN_DAI() + "', "
					+ "'" + traditionalLottery.getNGAY_XO_SO() + "', "
					+ "'" + traditionalLottery.getKET_QUA_G8() + "', "
					+ "'" + traditionalLottery.getKET_QUA_G7() + "', "
					+ "'" + traditionalLottery.getKET_QUA_G6() + "', "
					+ "'" + traditionalLottery.getKET_QUA_G5() + "', "
					+ "'" + traditionalLottery.getKET_QUA_G4() + "', "
					+ "'" + traditionalLottery.getKET_QUA_G3() + "', "
					+ "'" + traditionalLottery.getKET_QUA_G2() + "', "
					+ "'" + traditionalLottery.getKET_QUA_G1() + "', "
					+ "'" + traditionalLottery.getKET_QUA_DB() + "' "
					+ " where not exists (select 1 from " + TABLE_NAME + " where " +
					"TEN_DAI = " + "'" + traditionalLottery.getTEN_DAI() + "' AND NGAY_XO_SO = " + "'" + traditionalLottery.getNGAY_XO_SO() + "')";
			database.execSQL(sqlQuery);
			close();
		} 
		catch (Exception e)
		{		
			Log.d(TAG,e.getMessage()+"");
		}
	}

	/**
	 * TraditionalLotteryDBReadWithTime l?? ph????ng th???c l???y ra danh s??ch k???t qu??? x??? s??? ph??n bi???t theo lo???i mu???n d??.
	 * @return
	 * @param domain domain = 1: v?? s??? mi???n nam, mi???n trung, mi???n b???c; 2: v?? s??? mi???n b???c; 3: v?? s??? Vietlott 6/45; 4: v?? s??? Vietlott 6/55
	 */
	public List<TraditionalLottery> TraditionalLotteryDBReadWithTime(int domain, String dateSelect) {

		List<TraditionalLottery> traditionalLotteryList = new ArrayList<>();
		try {

			String sqlQuery;

			if(dateSelect.isEmpty())
			{
				if(domain == KET_QUA_MIEN_NAM)
				{
					sqlQuery = "select ROW_ID, MIEN_XO_SO, TEN_DAI, NGAY_XO_SO, KET_QUA_G8," +
							"KET_QUA_G7, KET_QUA_G6, KET_QUA_G5, KET_QUA_G4, KET_QUA_G3, KET_QUA_G2, KET_QUA_G1, KET_QUA_DB " +
							"from "+ TABLE_NAME + " Where MIEN_XO_SO = '2' ORDER BY NGAY_XO_SO DESC LIMIT 4";
				}else if(domain == KET_QUA_MIEN_TRUNG)
				{
					sqlQuery = "select ROW_ID, MIEN_XO_SO, TEN_DAI, NGAY_XO_SO, KET_QUA_G8," +
							"KET_QUA_G7, KET_QUA_G6, KET_QUA_G5, KET_QUA_G4, KET_QUA_G3, KET_QUA_G2, KET_QUA_G1, KET_QUA_DB " +
							"from "+ TABLE_NAME + " Where MIEN_XO_SO = '1' ORDER BY NGAY_XO_SO DESC LIMIT 2";
				}
				else //if(domain == KET_QUA_MIEN_BAC)
				{
					sqlQuery = "select ROW_ID, MIEN_XO_SO, TEN_DAI, NGAY_XO_SO, KET_QUA_G8," +
							"KET_QUA_G7, KET_QUA_G6, KET_QUA_G5, KET_QUA_G4, KET_QUA_G3, KET_QUA_G2, KET_QUA_G1, KET_QUA_DB " +
							"from "+ TABLE_NAME + " Where MIEN_XO_SO = '0' ORDER BY NGAY_XO_SO DESC LIMIT 1";
				}

			}else
			{
				if(domain == KET_QUA_MIEN_NAM)
				{
					sqlQuery = "select ROW_ID, MIEN_XO_SO, TEN_DAI, NGAY_XO_SO, KET_QUA_G8," +
							"KET_QUA_G7, KET_QUA_G6, KET_QUA_G5, KET_QUA_G4, KET_QUA_G3, KET_QUA_G2, KET_QUA_G1, KET_QUA_DB " +
							"from "+ TABLE_NAME + " Where MIEN_XO_SO = '2' AND NGAY_XO_SO like '" + dateSelect + "'";
				}else if(domain == KET_QUA_MIEN_TRUNG)
				{
					sqlQuery = "select ROW_ID, MIEN_XO_SO, TEN_DAI, NGAY_XO_SO, KET_QUA_G8," +
							"KET_QUA_G7, KET_QUA_G6, KET_QUA_G5, KET_QUA_G4, KET_QUA_G3, KET_QUA_G2, KET_QUA_G1, KET_QUA_DB " +
							"from "+ TABLE_NAME + " Where MIEN_XO_SO = '1' AND NGAY_XO_SO like '" + dateSelect + "'";
				}
				else if(domain == KET_QUA_MIEN_BAC)
				{
					sqlQuery = "select ROW_ID, MIEN_XO_SO, TEN_DAI, NGAY_XO_SO, KET_QUA_G8," +
							"KET_QUA_G7, KET_QUA_G6, KET_QUA_G5, KET_QUA_G4, KET_QUA_G3, KET_QUA_G2, KET_QUA_G1, KET_QUA_DB " +
							"from "+ TABLE_NAME + " Where MIEN_XO_SO = '0' AND NGAY_XO_SO like '" + dateSelect + "'";
				}
				else if(domain == KET_QUA_VIETLOTT_6_45)
				{
					sqlQuery = "select ROW_ID, MIEN_XO_SO, TEN_DAI, NGAY_XO_SO, KET_QUA_G8," +
							"KET_QUA_G7, KET_QUA_G6, KET_QUA_G5, KET_QUA_G4, KET_QUA_G3, KET_QUA_G2, KET_QUA_G1, KET_QUA_DB " +
							"from "+ TABLE_NAME + " Where MIEN_XO_SO = '3' AND NGAY_XO_SO like '" + dateSelect + "'";
				}
				else if(domain == KET_QUA_VIETLOTT_6_55)
				{
					sqlQuery = "select ROW_ID, MIEN_XO_SO, TEN_DAI, NGAY_XO_SO, KET_QUA_G8," +
							"KET_QUA_G7, KET_QUA_G6, KET_QUA_G5, KET_QUA_G4, KET_QUA_G3, KET_QUA_G2, KET_QUA_G1, KET_QUA_DB " +
							"from "+ TABLE_NAME + " Where MIEN_XO_SO = '4' AND NGAY_XO_SO like '" + dateSelect + "'";
				}
				else {
					if(dateSelect.isEmpty())
					{
						sqlQuery = "select ROW_ID, MIEN_XO_SO, TEN_DAI, NGAY_XO_SO, KET_QUA_G8," +
								"KET_QUA_G7, KET_QUA_G6, KET_QUA_G5, KET_QUA_G4, KET_QUA_G3, KET_QUA_G2, KET_QUA_G1, KET_QUA_DB " +
								"from "+ TABLE_NAME;
					}else {
						sqlQuery = "select ROW_ID, MIEN_XO_SO, TEN_DAI, NGAY_XO_SO, KET_QUA_G8," +
								"KET_QUA_G7, KET_QUA_G6, KET_QUA_G5, KET_QUA_G4, KET_QUA_G3, KET_QUA_G2, KET_QUA_G1, KET_QUA_DB " +
								"from "+ TABLE_NAME + " Where NGAY_XO_SO like '" + dateSelect.replace("/","-") + "'";
					}

				}
			}


			Log.d("xxzs",sqlQuery);

			open();
			
			Cursor c = database.rawQuery(sqlQuery, null);
			
			if (c.moveToFirst()) {
				do 
				{
					Log.d("TemporaryFileDBManager", "add");
					TraditionalLottery traditionalLottery = this.cursorToApp(c);
					traditionalLotteryList.add(traditionalLottery);
						
				} while (c.moveToNext());
			}
			
			close();
			c.close();
			
		} catch (Exception e) {
			Log.d(TAG,e.getMessage()+"");
		}
		
		return traditionalLotteryList;
	}

	public TraditionalLottery TraditionalLotteryDBReadWithRowID(int rowID) {

		TraditionalLottery traditionalLottery = new TraditionalLottery();
		try {

			String sqlQuery = "select ROW_ID, MIEN_XO_SO, TEN_DAI, NGAY_XO_SO, KET_QUA_G8," +
						"KET_QUA_G7, KET_QUA_G6, KET_QUA_G5, KET_QUA_G4, KET_QUA_G3, KET_QUA_G2, KET_QUA_G1, KET_QUA_DB " +
						"from "+ TABLE_NAME + " Where ROW_ID ="+rowID;

			Log.d("xxzs",sqlQuery);

			open();

			Cursor c = database.rawQuery(sqlQuery, null);

			if (c.moveToFirst()) {
				do
				{
					Log.d("TemporaryFileDBManager", "add");
					traditionalLottery = this.cursorToApp(c);

				} while (c.moveToNext());
			}

			close();
			c.close();

		} catch (Exception e) {
			Log.d(TAG,e.getMessage()+"");
		}

		return traditionalLottery;
	}

    public List<TraditionalLottery>  deleteAllResultAfter30Day() {

		List<TraditionalLottery> traditionalLotteryList = new ArrayList<>();
        try {
			TraditionalLottery traditionalLottery = null;
			String sqlQuery = "select ROW_ID, MIEN_XO_SO, TEN_DAI, NGAY_XO_SO, KET_QUA_G8," +
					"KET_QUA_G7, KET_QUA_G6, KET_QUA_G5, KET_QUA_G4, KET_QUA_G3, KET_QUA_G2, KET_QUA_G1, KET_QUA_DB  FROM " + TABLE_NAME;
			open();
			Cursor c = database.rawQuery(sqlQuery, null);
			if (c.moveToFirst()) {
				do
				{
					traditionalLottery = this.cursorToApp(c);
					traditionalLotteryList.add(traditionalLottery);
					Log.d("xxzsa",traditionalLottery.getROW_ID() +"");
				} while (c.moveToNext());
			}
			Log.d("xxzsa",sqlQuery+ " " + traditionalLotteryList.size());
			close();
			c.close();
			return  traditionalLotteryList;
        } catch (Exception e) {
            Log.d(TAG,e.getMessage()+"");
        }
		return  traditionalLotteryList;
    }

	public void deleteAllResultAfter30DayStep1(List<Integer> listIDDelete) {

		try {

			if(listIDDelete.size() > 0)
			{
				open();
				for (int rowID : listIDDelete)
				{
					String sqlQuery1 = "DELETE FROM " + TABLE_NAME + " WHERE ROW_ID ="+ rowID;
					Log.d("xxzs",sqlQuery1);

					database.execSQL(sqlQuery1);
				}

			}
			close();
		} catch (Exception e) {
			Log.d(TAG,e.getMessage()+"");
		}

	}


	/**
	 * getListNgayXoSo ????y l?? ph????ng th???c truy c???p t???i databases v?? l???y ra c??c ng??y c?? k???t qu??? x??? s??? c???a 3 mi???n chung.
	 * @return
	 */
	public List<Long> getListNgayXoSo() {

		List<Long> listNgayXoSo = new ArrayList<>();
		try {

			String sqlQuery = "select distinct NGAY_XO_SO from " + TABLE_NAME ;

			Log.d("xxzs",sqlQuery);

			open();

			Cursor c = database.rawQuery(sqlQuery, null);

			if (c.moveToFirst()) {
				do
				{
					Log.d("TemporaryFileDBManager", "add");
					long ngay_Xo_So = convertDateToMillisecond(c.getString(0));
					listNgayXoSo.add(ngay_Xo_So);
				} while (c.moveToNext());
			}

			close();
			c.close();

		} catch (Exception e) {
			Log.d(TAG,e.getMessage()+"");
		}
		return listNgayXoSo;
	}


	/**
	 * getListNgayXoSo3Mien ????y l?? ph????ng th???c truy c???p t???i databases v?? l???y ra c??c ng??y c?? k???t qu??? x??? s??? c???a t???ng mi???n ri??ng bi???t.
	 * @return
	 */
	public List<Long> getListNgayXoSo3Mien(int mienXoSo) {

		List<Long> listNgayXoSo = new ArrayList<>();
		try {

			String sqlQuery = "select distinct NGAY_XO_SO from " + TABLE_NAME + " WHERE MIEN_XO_SO like '"+ mienXoSo +"' ORDER BY NGAY_XO_SO DESC" ;

			Log.d("xxzs",sqlQuery);

			open();

			Cursor c = database.rawQuery(sqlQuery, null);

			if (c.moveToFirst()) {
				do
				{
					Log.d("TemporaryFileDBManager", "add");
					long ngay_Xo_So = convertDateToMillisecond(c.getString(0));
					listNgayXoSo.add(ngay_Xo_So);
				} while (c.moveToNext());
			}

			close();
			c.close();

		} catch (Exception e) {
			Log.d(TAG,e.getMessage()+"");
		}
		return listNgayXoSo;
	}

	/**
	 * getTraditionalLotteryDBReadWithDomain ????y l?? ph????ng th???c l???y ra ?????i t?????ng TraditionalLottery t?? ng??y v?? t??n ????i m?? ng?????i d??ng mu???n d?? v?? s???.
	 * @param domainLottery
	 * @param dateLottery
	 * @return
	 */
	public TraditionalLottery getTraditionalLotteryDBReadWithDomain(String domainLottery, String dateLottery, boolean checkMienBac) {

		TraditionalLottery traditionalLottery = null;
		String sqlQuery;
		try {
				sqlQuery = "select ROW_ID, MIEN_XO_SO, TEN_DAI, NGAY_XO_SO, KET_QUA_G8," +
						"KET_QUA_G7, KET_QUA_G6, KET_QUA_G5, KET_QUA_G4, KET_QUA_G3, KET_QUA_G2, KET_QUA_G1, KET_QUA_DB "
						+ "from "+ TABLE_NAME + " Where ";
				if(checkMienBac)
				{
					sqlQuery += "Mien_XO_SO = "+ 0 + " and NGAY_XO_SO like '" + dateLottery + "'";
				}else {
					sqlQuery += "TEN_DAI like '"+ domainLottery + "' and NGAY_XO_SO like '" + dateLottery + "'";
				}

			Log.d("xxzs",sqlQuery);
			open();
			Cursor c = database.rawQuery(sqlQuery, null);

			if (c.moveToFirst()) {
				do
				{
					Log.d("TemporaryFileDBManager", "add");
					traditionalLottery = this.cursorToApp(c);
				} while (c.moveToNext());
			}
			close();
			c.close();

		} catch (Exception e) {
			Log.d(TAG,e.getMessage()+"");
		}

		return traditionalLottery;
	}


	public int getLotteryFromDateSelected(String domainLottery, String dateLottery) {

		TraditionalLottery traditionalLottery = null;
		int cursor = 0;
		String sqlQuery;
		try {
			sqlQuery = "select ROW_ID, MIEN_XO_SO, TEN_DAI, NGAY_XO_SO, KET_QUA_G8," +
					"KET_QUA_G7, KET_QUA_G6, KET_QUA_G5, KET_QUA_G4, KET_QUA_G3, KET_QUA_G2, KET_QUA_G1, KET_QUA_DB "
					+ "from "+ TABLE_NAME + " Where ";
			if(dateLottery == null)
			{
				sqlQuery += "Mien_XO_SO = "+ 0 + " and NGAY_XO_SO like '" + dateLottery + "'";
			}else {
				sqlQuery += "TEN_DAI like '"+ domainLottery + "' and NGAY_XO_SO like '" + dateLottery + "'";
			}

			Log.d("xxzs",sqlQuery);
			open();
			Cursor c = database.rawQuery(sqlQuery, null);
			cursor = c.getCount();

			close();
			c.close();

		} catch (Exception e) {
			Log.d(TAG,e.getMessage()+"");
		}

		return cursor;
	}


	/**
	 * getTraditionalLotteryDBReadWithDomain ????y l?? ph????ng th???c l???y ra ?????i t?????ng TraditionalLottery t?? ng??y v?? t??n ????i m?? ng?????i d??ng mu???n d?? v?? s???.
	 * @param provinceLottery
	 * @param dateLottery
	 * @return
	 */
	public TraditionalLottery getTraditionalLotteryDBReadWithDomain(String provinceLottery, String dateLottery, String Domain) {

		TraditionalLottery traditionalLottery = null;
		String sqlQuery;
		try {
			sqlQuery = "select ROW_ID, MIEN_XO_SO, TEN_DAI, NGAY_XO_SO, KET_QUA_G8," +
					"KET_QUA_G7, KET_QUA_G6, KET_QUA_G5, KET_QUA_G4, KET_QUA_G3, KET_QUA_G2, KET_QUA_G1, KET_QUA_DB "
					+ "from "+ TABLE_NAME + " Where MIEN_XO_SO like '" + Domain + "' and TEN_DAI like '"+ provinceLottery + "' and NGAY_XO_SO like '" + dateLottery + "'";

			Log.d("xxzs",sqlQuery);
			open();
			Cursor c = database.rawQuery(sqlQuery, null);

			if (c.moveToFirst()) {
				do
				{
					Log.d("TemporaryFileDBManager", "add");
					traditionalLottery = this.cursorToApp(c);
				} while (c.moveToNext());
			}
			close();
			c.close();

		} catch (Exception e) {
			Log.d(TAG,e.getMessage()+"");
		}

		return traditionalLottery;
	}


	/**
	 * CheckResultsLotteryDBReadWithTime l???y ra ?????i t?????ng ???? tr??ng v?? s??? c???a m?? s???, ng??y, ????i m?? ng?????i d??ng ch???n.
	 */
	public TraditionalLottery CheckResultsLotteryDBReadWithTime(String codeLottery, int domain, String domainLottery, String dateLottery) {

		TraditionalLottery traditionalLottery = new TraditionalLottery();
		try {

			String sqlQuery;
			String KQ_G8MN_G7MB = codeLottery.substring(codeLottery.length()-2);
			String KQ_G7_G6MB = codeLottery.substring(codeLottery.length()-3);
			String KQ_G65MN_G45MB = codeLottery.substring(codeLottery.length()-4);
			String KQ_G4321MN_G321MB = codeLottery.substring(codeLottery.length()-5);
			String KQ_GDB_MN = codeLottery;
			String KQ_KK_MN = codeLottery.substring(0, codeLottery.length()-5);
			if(domain == KET_QUA_MIEN_NAM || domain == KET_QUA_MIEN_TRUNG)
			{
				int mien_xo_so = KET_QUA_MIEN_NAM;
				if(domain == KET_QUA_MIEN_TRUNG)
				{
					mien_xo_so = KET_QUA_MIEN_TRUNG;
				}

				//MIEN_XO_SO = '1' mi???n trung; MIEN_XO_SO = '2' mi???n nam
				sqlQuery = "SELECT * " +
						"FROM XOSOTRUYENTHONG " +
						"WHERE MIEN_XO_SO like '" + mien_xo_so + "'"+
						" AND NGAY_XO_SO like '" + dateLottery +
						"' AND TEN_DAI like '" + domainLottery +
						"' AND (KET_QUA_G8 like '"+ KQ_G8MN_G7MB + "' " + // ????y l?? gi???i t??m tr??ng 2 s??? cu???i
						"or KET_QUA_G7 like '" + KQ_G7_G6MB + "' " + // ????y l?? gi???i b???y tr??ng 3 s??? cu???i
						"or KET_QUA_G6 like '%" + KQ_G65MN_G45MB + "%' " + // ????y l?? gi???i s??u tr??ng 4 s??? cu???i
						"or KET_QUA_G5 like '" + KQ_G65MN_G45MB + "' " + // ????y l?? gi???i n??m tr??ng 4 s??? cu???i
						"or KET_QUA_G4 like '%" + KQ_G4321MN_G321MB + "%' " + // ????y l?? gi???i t?? tr??ng 5 s??? cu???i
						"or KET_QUA_G3 like '%" + KQ_G4321MN_G321MB + "%' " + // ????y l?? gi???i ba tr??ng 5 s??? cu???i
						"or KET_QUA_G2 like '" + KQ_G4321MN_G321MB + "' " + // ????y l?? gi???i nh?? tr??ng 5 s??? cu???i
						"or KET_QUA_G1 like '" + KQ_G4321MN_G321MB + "' " + // ????y l?? gi???i nh???t tr??ng 5 s??? cu???i
						"or KET_QUA_DB like '%" + KQ_G4321MN_G321MB + "'" +// ????y l?? gi???i ph??? ?????t bi???t tr??ng 5 s??? cu???i
						"or KET_QUA_DB like '" + KQ_GDB_MN + "')";
				//"or KET_QUA_DB like '%" + KQ_G4321MN_G321MB + "' " + // ????y l?? gi???i ph??? ?????t bi???t tr??ng 5 s??? cu???i
				//"or KET_QUA_DB like '" + KQ_KK_MN + "%' " + // ????y l?? gi???i khuy???n kh??ch, ch??ng t??i ch??? ki???m tra s??? ?????u ti??n n???u tr??ng th?? s??? ti???p t???c l???y ra danh s??ch v?? ki???m tra 5 s??? c??n l???i.

			}
			else if(domain == KET_QUA_MIEN_BAC)
			{
				// MIEN_XO_SO = '0' l?? mi???n B???c.
				sqlQuery = "SELECT * " +
						"FROM XOSOTRUYENTHONG " +
						"WHERE MIEN_XO_SO = '"+ KET_QUA_MIEN_BAC + "' " +
						" AND NGAY_XO_SO like '" + dateLottery + "'" +
						" AND " + "(KET_QUA_G7 like '%" + KQ_G8MN_G7MB + "%' " + // ????y l?? gi???i b???y tr??ng 2 s??? cu???i
						"or KET_QUA_G6 like '%" + KQ_G7_G6MB + "%' " + // ????y l?? gi???i s??u tr??ng 3 s??? cu???i
						"or KET_QUA_G5 like '%" + KQ_G65MN_G45MB + "%' " + // ????y l?? gi???i n??m tr??ng 4 s??? cu???i
						"or KET_QUA_G4 like '%" + KQ_G65MN_G45MB + "%' " + // ????y l?? gi???i t?? tr??ng 4 s??? cu???i
						"or KET_QUA_G3 like '%" + KQ_G4321MN_G321MB + "%' " + // ????y l?? gi???i ba tr??ng 5 s??? cu???i
						"or KET_QUA_G2 like '%" + KQ_G4321MN_G321MB + "%' " + // ????y l?? gi???i nh?? tr??ng 5 s??? cu???i
						"or KET_QUA_G1 like '" + KQ_G4321MN_G321MB + "' " + // ????y l?? gi???i nh???t tr??ng 5 s??? cu???i
						"or KET_QUA_DB like '%" + codeLottery.substring(3) + "'" + // ????y l?? gi???i khuy???n kh??ch ?????c bi???t tr??ng 40.000??
						"or KET_QUA_DB like '" + codeLottery + "') "; // ????y l?? gi???i ?????c bi???t tr??ng 5 s??? cu???i
			}
			else if(domain == KET_QUA_VIETLOTT_6_45)
			{

				sqlQuery = "select ROW_ID, MIEN_XO_SO, TEN_DAI, NGAY_XO_SO, KET_QUA_G8," +
						"KET_QUA_G7, KET_QUA_G6, KET_QUA_G5, KET_QUA_G4, KET_QUA_G3, KET_QUA_G2, KET_QUA_G1, KET_QUA_DB " +
						"from "+ TABLE_NAME + " Where MIEN_XO_SO = '1'";

			}
			else
			{
				sqlQuery = "select ROW_ID, MIEN_XO_SO, TEN_DAI, NGAY_XO_SO, KET_QUA_G8," +
						"KET_QUA_G7, KET_QUA_G6, KET_QUA_G5, KET_QUA_G4, KET_QUA_G3, KET_QUA_G2, KET_QUA_G1, KET_QUA_DB " +
						"from "+ TABLE_NAME + " Where MIEN_XO_SO like '1' or MIEN_XO_SO like '2'";
			}


			Log.d("xxzs",sqlQuery);

			open();

			Cursor c = database.rawQuery(sqlQuery, null);

			if (c.moveToFirst()) {
				do
				{
					Log.d("TemporaryFileDBManager", "add");
					traditionalLottery = this.cursorToApp(c);

				} while (c.moveToNext());
			}

			close();
			c.close();

		} catch (Exception e) {
			Log.d(TAG,e.getMessage()+"");
		}

		return traditionalLottery;
	}

	/**
	 * CheckResultsLotteryDBReadWithTime ????y l?? ph????ng th???c l???y danh s??ch ?????i t?????ng ???? tr??ng gi???i v???i d??? li???u truy???n v??o codeLottery, domain
	 * @param codeLottery
	 * @param domain
	 * @return
	 */
	public List<TraditionalLottery> CheckResultsLotteryDBReadWithTime(String codeLottery, int domain) {

		List<TraditionalLottery> traditionalLotteryList = new ArrayList<>();
		try {

			String sqlQuery;
			String KQ_G8MN_G7MB = codeLottery.substring(codeLottery.length()-2);
			String KQ_G7_G6MB = codeLottery.substring(codeLottery.length()-3);
			String KQ_G65MN_G45MB = codeLottery.substring(codeLottery.length()-4);
			String KQ_G4321MN_G321MB = codeLottery.substring(codeLottery.length()-5);
			String KQ_GDB_MN = codeLottery;
			String KQ_KK_MN = codeLottery.substring(0, codeLottery.length()-5);
			if(domain == KET_QUA_MIENNAM_MIENTRUNG)
			{


				//MIEN_XO_SO = '1' mi???n trung; MIEN_XO_SO = '2' mi???n nam
				sqlQuery = "SELECT * " +
							"FROM XOSOTRUYENTHONG " +
							"WHERE (MIEN_XO_SO = '1' or MIEN_XO_SO = '2') " +
							"AND (KET_QUA_G8 like '"+ KQ_G8MN_G7MB + "' " + // ????y l?? gi???i t??m tr??ng 2 s??? cu???i
							"or KET_QUA_G7 like '" + KQ_G7_G6MB + "' " + // ????y l?? gi???i b???y tr??ng 3 s??? cu???i
							"or KET_QUA_G6 like '%" + KQ_G65MN_G45MB + "%' " + // ????y l?? gi???i s??u tr??ng 4 s??? cu???i
							"or KET_QUA_G5 like '" + KQ_G65MN_G45MB + "' " + // ????y l?? gi???i n??m tr??ng 4 s??? cu???i
							"or KET_QUA_G4 like '%" + KQ_G4321MN_G321MB + "%' " + // ????y l?? gi???i t?? tr??ng 5 s??? cu???i
							"or KET_QUA_G3 like '%" + KQ_G4321MN_G321MB + "%' " + // ????y l?? gi???i ba tr??ng 5 s??? cu???i
							"or KET_QUA_G2 like '" + KQ_G4321MN_G321MB + "' " + // ????y l?? gi???i nh?? tr??ng 5 s??? cu???i
							"or KET_QUA_G1 like '" + KQ_G4321MN_G321MB + "' " + // ????y l?? gi???i nh???t tr??ng 5 s??? cu???i
							"or KET_QUA_DB like '%" + KQ_G4321MN_G321MB + "'" +// ????y l?? gi???i ph??? ?????t bi???t tr??ng 5 s??? cu???i
							"or KET_QUA_DB like '" + KQ_GDB_MN + "')";
							//"or KET_QUA_DB like '%" + KQ_G4321MN_G321MB + "' " + // ????y l?? gi???i ph??? ?????t bi???t tr??ng 5 s??? cu???i
							//"or KET_QUA_DB like '" + KQ_KK_MN + "%' " + // ????y l?? gi???i khuy???n kh??ch, ch??ng t??i ch??? ki???m tra s??? ?????u ti??n n???u tr??ng th?? s??? ti???p t???c l???y ra danh s??ch v?? ki???m tra 5 s??? c??n l???i.

			}
			else if(domain == KET_QUA_MIEN_BAC)
			{
				// MIEN_XO_SO = '0' l?? mi???n B???c.
				sqlQuery = "SELECT * " +
							"FROM XOSOTRUYENTHONG " +
							"WHERE MIEN_XO_SO = '0' " + // Mi???n B???c
							"AND " + "(KET_QUA_G7 like '%" + KQ_G8MN_G7MB + "%' " + // ????y l?? gi???i b???y tr??ng 2 s??? cu???i
							"or KET_QUA_G6 like '%" + KQ_G7_G6MB + "%' " + // ????y l?? gi???i s??u tr??ng 3 s??? cu???i
							"or KET_QUA_G5 like '%" + KQ_G65MN_G45MB + "%' " + // ????y l?? gi???i n??m tr??ng 4 s??? cu???i
							"or KET_QUA_G4 like '%" + KQ_G65MN_G45MB + "%' " + // ????y l?? gi???i t?? tr??ng 4 s??? cu???i
							"or KET_QUA_G3 like '%" + KQ_G4321MN_G321MB + "%' " + // ????y l?? gi???i ba tr??ng 5 s??? cu???i
							"or KET_QUA_G2 like '%" + KQ_G4321MN_G321MB + "%' " + // ????y l?? gi???i nh?? tr??ng 5 s??? cu???i
							"or KET_QUA_G1 like '" + KQ_G4321MN_G321MB + "' " + // ????y l?? gi???i nh???t tr??ng 5 s??? cu???i
							"or KET_QUA_DB like '%" + codeLottery.substring(3) + "'" + // ????y l?? gi???i khuy???n kh??ch ?????c bi???t tr??ng 40.000??
							"or KET_QUA_DB like '" + codeLottery + "') "; // ????y l?? gi???i ?????c bi???t tr??ng 5 s??? cu???i
			}
			else if(domain == KET_QUA_VIETLOTT_6_45)
			{

				sqlQuery = "select ROW_ID, MIEN_XO_SO, TEN_DAI, NGAY_XO_SO, KET_QUA_G8," +
						"KET_QUA_G7, KET_QUA_G6, KET_QUA_G5, KET_QUA_G4, KET_QUA_G3, KET_QUA_G2, KET_QUA_G1, KET_QUA_DB " +
						"from "+ TABLE_NAME + " Where MIEN_XO_SO = '1'"; // Mi???n Nam

			}
			else
			{
				sqlQuery = "select ROW_ID, MIEN_XO_SO, TEN_DAI, NGAY_XO_SO, KET_QUA_G8," +
						"KET_QUA_G7, KET_QUA_G6, KET_QUA_G5, KET_QUA_G4, KET_QUA_G3, KET_QUA_G2, KET_QUA_G1, KET_QUA_DB " +
						"from "+ TABLE_NAME + " Where MIEN_XO_SO = '1' or MIEN_XO_SO = '2'"; // Mi???n Trung, Mi???n Nam
			}

			/*String sqlQuery = "select ROW_ID, MIEN_XO_SO, TEN_DAI, NGAY_XO_SO, KET_QUA_G8," +
					"KET_QUA_G7, KET_QUA_G6, KET_QUA_G5, KET_QUA_G4, KET_QUA_G3, KET_QUA_G2, KET_QUA_G1, KET_QUA_DB " +
					"from "+ TABLE_NAME;*/

			Log.d("xxzs",sqlQuery);

			open();

			Cursor c = database.rawQuery(sqlQuery, null);

			if (c.moveToFirst()) {
				do
				{
					Log.d("TemporaryFileDBManager", "add");
					TraditionalLottery traditionalLottery = this.cursorToApp(c);
					traditionalLotteryList.add(traditionalLottery);

				} while (c.moveToNext());
			}

			close();
			c.close();

		} catch (Exception e) {
			Log.d(TAG,e.getMessage()+"");
		}

		return traditionalLotteryList;
	}

	//SELECT distinct NGAY_XO_SO FROM XOSOTRUYENTHONG ORDER BY NGAY_XO_SO  DESC
	
	private TraditionalLottery cursorToApp(Cursor cursor) {


		TraditionalLottery traditionalLottery = new TraditionalLottery();
		traditionalLottery.setROW_ID(cursor.getInt(0));
		traditionalLottery.setMIEN_XO_SO(cursor.getString(1));
		traditionalLottery.setTEN_DAI(cursor.getString(2));
		traditionalLottery.setNGAY_XO_SO(cursor.getString(3));
		traditionalLottery.setKET_QUA_G8(cursor.getString(4));
		traditionalLottery.setKET_QUA_G7(cursor.getString(5));
		traditionalLottery.setKET_QUA_G6(cursor.getString(6));
		traditionalLottery.setKET_QUA_G5(cursor.getString(7));
		traditionalLottery.setKET_QUA_G4(cursor.getString(8));
		traditionalLottery.setKET_QUA_G3(cursor.getString(9));
		traditionalLottery.setKET_QUA_G2(cursor.getString(10));
		traditionalLottery.setKET_QUA_G1(cursor.getString(11));
		traditionalLottery.setKET_QUA_DB(cursor.getString(12));
	    return traditionalLottery;
	 }

}
