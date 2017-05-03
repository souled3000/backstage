package org.blackcrystalinfo.backstage.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;

public final class DBHelper {
	private static final BasicDataSource DATA_SOURCE;

	static {
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://193.168.1.115:3306/hive?useUnicode=true&characterEncoding=UTF-8";
		String username = "hive";
		String passwrod = "hive";

		DATA_SOURCE = new BasicDataSource();
		DATA_SOURCE.setDriverClassName(driver);
		DATA_SOURCE.setUrl(url);
		DATA_SOURCE.setUsername(username);
		DATA_SOURCE.setPassword(passwrod);
		DATA_SOURCE.setMaxActive(10);
		DATA_SOURCE.setInitialSize(5);
		DATA_SOURCE.setMinIdle(8);
		DATA_SOURCE.setMaxIdle(16);
		DATA_SOURCE.setMaxIdle(2 * 10000);
		DATA_SOURCE.setTestOnBorrow(true);
		DATA_SOURCE.setRemoveAbandonedTimeout(180);
		DATA_SOURCE.setRemoveAbandoned(true);
		DATA_SOURCE.setTestOnReturn(true);
		DATA_SOURCE.setTestOnReturn(true);
		DATA_SOURCE.setLogAbandoned(true);
		DATA_SOURCE.setDefaultAutoCommit(true);
	}

	public static Connection get() throws Exception {
		Connection conn = DATA_SOURCE.getConnection();
		return conn;
	}

	public static void close(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
				System.out.println("close connection success");
			} catch (SQLException e) {
				System.out.println("close connection failure:" + e);
			} finally {
			}
		}
	}

	public static long getDevIdByMac(String subMac) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DBHelper.get();
			ps = conn.prepareStatement("select id from s_device where mac_match = ?");
			ps.setString(1, subMac);
			rs = ps.executeQuery();
			rs.next();
			long id = rs.getLong(1);
			return id;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			rs.close();
			ps.close();
			conn.close();
		}
		return 0;
	}

	public static void main(String[] args) throws Exception {
		getDevIdByMac("0000b0d59d63f795");
	}
}
