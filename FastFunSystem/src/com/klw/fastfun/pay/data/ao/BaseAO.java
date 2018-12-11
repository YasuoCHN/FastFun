package com.klw.fastfun.pay.data.ao;

import com.klw.fastfun.pay.data.dao.AlarmDAO;
import com.klw.fastfun.pay.data.dao.CPInfoDAO;
import com.klw.fastfun.pay.data.dao.CodeHelperDAO;
import com.klw.fastfun.pay.data.dao.CommonDAO;
import com.klw.fastfun.pay.data.dao.MMPayInfoDAO;
import com.klw.fastfun.pay.data.dao.OrderReqDAO;


public class BaseAO {
	protected static CPInfoDAO cpInfoDAO = new CPInfoDAO();
	protected static OrderReqDAO orderReqDAO = new OrderReqDAO();
	protected static CodeHelperDAO codeHelperDAO = new CodeHelperDAO();
	protected static MMPayInfoDAO mmPayInfoDAO = new MMPayInfoDAO();
	protected static AlarmDAO alarmDAO = new AlarmDAO();
	protected static CommonDAO commonDAO = new CommonDAO();
}
