package org.blackcrystalinfo.backstage.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.blackcrystalinfo.backstage.bo.StrStr;

public final class Constants {
	public static ResourceBundle rb = null;

	static {
		try {
			rb = ResourceBundle.getBundle("sys");
		} finally {

		}
	}
	public static List<StrStr> ld2TypeList;
	static{
		ld2TypeList= new ArrayList<StrStr>();
		ld2TypeList.add(new StrStr("00","无效数据"));
		ld2TypeList.add(new StrStr("01","源报警类型设备"));
		ld2TypeList.add(new StrStr("02","预留"));
		ld2TypeList.add(new StrStr("03","预留"));
		ld2TypeList.add(new StrStr("04","动作执行数据完整数据"));
		ld2TypeList.add(new StrStr("05","局域控制数据类型"));
	}
	public final static String DRPC_IP = rb.getString("storm.drpc.ip");
	public final static int DRPC_PORT = Integer.parseInt(rb.getString("storm.drpc.port")); 
}
