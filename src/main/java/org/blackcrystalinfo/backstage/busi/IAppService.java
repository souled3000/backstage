package org.blackcrystalinfo.backstage.busi;

import java.util.List;
import java.util.Map;

public interface IAppService {
	public List<Map<String, Object>> apps();

	public Map<?, ?> check(String id, String cur);

	public void save(String id, String cur, String min, String msg, String url);

	public void test();
}
