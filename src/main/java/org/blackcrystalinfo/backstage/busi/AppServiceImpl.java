package org.blackcrystalinfo.backstage.busi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.blackcrystalinfo.backstage.utils.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class AppServiceImpl implements IAppService {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Transactional(readOnly = true)
	public List<Map<String, Object>> apps() {
		List<Map<String, Object>> result = jdbcTemplate.queryForList("select * from app");
		return result;
	}

	@Transactional(readOnly = true)
	public Map<?, ?> check(String id, String cur) {
		Map<?, ?> app = jdbcTemplate.queryForMap(
				"select * from app where id=?", id);
		Map<Object, Object> m = new HashMap<Object, Object>();
		int last = MiscUtils.copyright((String) app.get("last"));
		int avail = MiscUtils.copyright((String) app.get("avail"));
		String url = (String) app.get("url");
		m.put("url", url);
		m.put("ver", app.get("last"));
		m.put("msg", app.get("msg"));
		int curt = MiscUtils.copyright(cur);
		if (curt == last) {
			m.put("status", 1);
			m.put("desc", "已经最新");
		}
		if (curt >= avail && curt < last) {
			m.put("status", 2);
			m.put("desc", "不是最新");
		}
		if (curt < avail) {
			m.put("status", 3);
			m.put("desc", "必须更新");
		}
		if (curt > last) {
			m.put("status", 4);
			m.put("desc", "非法");
		}
		return m;
	}

	@Transactional
	public void save(String id, String cur, String min, String msg, String url) {
		String getSql = "select * from app where id=?";

		List<Map<String, Object>> ret = jdbcTemplate.queryForList(getSql, id);

		if (null != ret && !ret.isEmpty()) {
			String updateSql = "Update app set last=?, avail=?, msg=?, url=? where id=?";
			jdbcTemplate.update(updateSql, cur, min, msg, url, id);
		} else {
			String inserSql = "insert into app(id, last, avail, msg, url) values(?,?,?,?,?)";
			jdbcTemplate.update(inserSql, id, cur, min, msg, url);
		}
	}

	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
				"spring/beans.xml");

		ctx.start();

		IAppService app = (IAppService) ctx.getBean("appServiceImpl");

		app.test();
		app.test();
		app.test();
		ctx.close();
	}

	@Override
	public void test() {
		System.out.println("------------------------------------------------------");
		String id = "android";
		String cur = "1.1.1";
		String min = "2.2.2";
		String url = "https://localhsot:111/11.1.11.jp";

		String getSql = "select * from app where id=?";

		List<Map<String, Object>> ret = jdbcTemplate.queryForList(getSql, id);

		if (null != ret && !ret.isEmpty()) {
			String updateSql = "Update app set id=?, last=?, avail=?, url=? ";
			jdbcTemplate.update(updateSql, id, cur, min, url);
		} else {
			String inserSql = "insert into app(id, last, avail, url) values(?,?,?,?)";
			jdbcTemplate.update(inserSql, id, cur, min, url);
		}
		
		System.out.println("------------------------------------------------------\n\n");
	}
}
