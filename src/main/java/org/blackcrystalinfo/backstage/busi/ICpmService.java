package org.blackcrystalinfo.backstage.busi;

public interface ICpmService {

	public String gotMacById(String id) throws Exception;
	public String gotIdByMac(String mac) throws Exception;
	public String gotAccountById(String id) throws Exception;
	public String gotIdByAccount(String account) throws Exception;
	public String gotOut() throws Exception;
	public String gotOut2() throws Exception;
	
	
}
