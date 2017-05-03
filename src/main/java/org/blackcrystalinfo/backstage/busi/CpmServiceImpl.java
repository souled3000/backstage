package org.blackcrystalinfo.backstage.busi;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.blackcrystalinfo.backstage.utils.DataHelper;
import org.blackcrystalinfo.backstage.web.controller.CpmController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import redis.clients.jedis.Jedis;

@Repository
public class CpmServiceImpl implements ICpmService {

	private static final Logger log = LoggerFactory.getLogger(CpmController.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public String gotMacById(String id) throws Exception {
		String sql = "select mac_match from s_device where id like ?";
		String l = jdbcTemplate.queryForObject(sql, new Object[] { id }, String.class);
		l += " " + gotDevMsg(id);
		return l;
	}

	@Override
	public String gotIdByMac(String mac) throws Exception {
		String sql = "select id from s_device where lower(mac_match) like lower(?)";
		String l = String.valueOf(jdbcTemplate.queryForObject(sql, new Object[] { mac }, Long.class));
		l += " " + gotDevMsg(l);
		return l.toString();
	}

	@Override
	public String gotAccountById(String id) throws Exception {
		String sql = "select account from s_customer where id = ?";
		String l = jdbcTemplate.queryForObject(sql, new Object[] { id }, String.class);
		l += " " + gotUsrMsg(id);
		return l;
	}

	@Override
	public String gotIdByAccount(String account) throws Exception {
		String sql = "select id from s_customer where account = ?";
		String l = String.valueOf(jdbcTemplate.queryForObject(sql, new Object[] { account }, Long.class));
		l += " " + gotUsrMsg(l);
		return l.toString();
	}

	private String gotDevMsg(String id) {
		Jedis r = DataHelper.getJedis();
		try {
			String s = r.hget("device:adr", id);
			s += " " + r.hget("d:srv", id);
			return s;
		} catch (Exception e) {

		} finally {
			DataHelper.returnJedis(r);
		}
		return "";
	}

	private String gotUsrMsg(String id) {
		Jedis r = DataHelper.getJedis();
		try {
			String s = r.hget("m:id", id);
			s += " " + r.hget("m:srv", id);
			return s;
		} catch (Exception e) {

		} finally {
			DataHelper.returnJedis(r);
		}
		return "";
	}

	@Override
	public String gotOut() throws Exception {
		StringBuilder out = new StringBuilder("");
		Jedis r = DataHelper.getJedis();
		try {
			String sql = "select mac_match from s_device where id like ?";
			Set<String> m = r.hkeys("d:srv");
			for (String o : m) {
				log.info(o);
				String mac = jdbcTemplate.queryForObject(sql, new Object[] { o.trim() }, String.class);
				out.append(o).append("\t\t").append(mac).append("\r\n");
			}
			out.append("--------------------------------------------------------------------------------------------------------------------").append("\r\n");
			sql = "select account from s_customer where id = ?";
			m = r.hkeys("m:srv");
			for (String o : m) {
				log.info(o);
				String account = jdbcTemplate.queryForObject(sql, new Object[] { o.trim() }, String.class);
				out.append(o).append("\t\t").append(account).append("\r\n");
			}
			return out.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DataHelper.returnJedis(r);
		}
		return null;
	}

	@Override
	public String gotOut2() throws Exception {
		StringBuilder out = new StringBuilder("");
		Jedis r = DataHelper.getJedis();
		try {
			Set<String> m = r.hkeys("d:srv");
			String sql = "select id,mac_match from s_device";
			List<Map<String, Object>> l = jdbcTemplate.queryForList(sql);
			out.append("设备在线数").append(m.size()).append("\r\n");
			out.append("STATUS").append("\t\t").append("ID").append("\t\t").append("MAC").append("\r\n");
			for (Map<String, Object> o : l) {
				if(m.contains(o.get("id").toString())){
					out.append("在线").append("\t\t").append(o.get("id")).append("\t\t").append(o.get("mac_match")).append("\r\n");
				}else{
					out.append("下线").append("\t\t").append(o.get("id")).append("\t\t").append(o.get("mac_match")).append("\r\n");
				}
			}
			out.append("--------------------------------------------------------------------------------------------------------------------").append("\r\n");
			m = r.hkeys("m:srv");
			out.append("用户在线数").append(m.size()).append("\r\n");
			out.append("STATUS").append("\t\t").append("ID").append("\t\t").append("ACCOUNT").append("\r\n");
			sql = "select id,account from s_customer";
			l=jdbcTemplate.queryForList(sql);
			for (Map<String, Object> o : l) {
				if(m.contains(o.get("id").toString())){
					out.append("在线").append("\t\t").append(o.get("id")).append("\t\t").append(o.get("account")).append("\r\n");
				}else{
					out.append("下线").append("\t\t").append(o.get("id")).append("\t\t").append(o.get("account")).append("\r\n");
				}
			}
			return out.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DataHelper.returnJedis(r);
		}
		return null;
	}

	public static void main(String[] args) {
		Jedis r = DataHelper.getJedis();
		try {
			Set<String> m = r.hkeys("d:srv");
			for (String o : m) {
				System.out.println(o);
			}
			m = r.hkeys("m:srv");
			for (String o : m) {
				System.out.println(o);
			}
		} catch (Exception e) {

		} finally {
			DataHelper.returnJedis(r);
		}
	}

}
