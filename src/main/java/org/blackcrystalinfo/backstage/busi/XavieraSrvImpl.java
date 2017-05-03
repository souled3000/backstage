package org.blackcrystalinfo.backstage.busi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.blackcrystalinfo.backstage.bo.Xaviera;
import org.blackcrystalinfo.backstage.web.controller.XavieraController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class XavieraSrvImpl implements IXavieraSrv {
	private static final Logger log = LoggerFactory.getLogger(XavieraSrvImpl.class);
	private final static String ISQL = "insert into xaviera (mac,chip,currentItem,isSuccess,ts) values (?,?,?,?,?)";
	private final static String QSQL1 = "select * from xaviera where mac = ? and chip = ? order by ts desc";
	private final static String QSQL2 = "select * from xaviera where mac = ? and chip = ? and currentItem = ? order by ts desc";
	private final static String QSQL3 = "select * from xaviera order by ts desc";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Transactional
	public void importData(Xaviera o) {
		try{
			jdbcTemplate.update(ISQL, o.getMac(), o.getChip(), o.getCurrentItem(), o.getIsSuccess(), System.currentTimeMillis());
		}catch (Exception e){
			log.info("", e);
		}
	}

	@Transactional
	public List<Xaviera> getDate(Xaviera r) {
		List<Xaviera> rt = null;
		if (StringUtils.isNotBlank(r.getMac()) && StringUtils.isNotBlank(r.getChip()) && StringUtils.isNotBlank(r.getCurrentItem())) {
			rt = jdbcTemplate.query(QSQL2, new Object[] { r.getMac(), r.getChip(), r.getCurrentItem() }, new RowMapperResultSetExtractor<Xaviera>(new RowMapper<Xaviera>() {
				public Xaviera mapRow(ResultSet rs, int rowNum) throws SQLException {
					Xaviera o = new Xaviera();
					o.setMac(rs.getString("mac"));
					o.setChip(rs.getString("chip"));
					o.setCurrentItem(rs.getString("currentItem"));
					o.setIsSuccess(rs.getString("isSuccess"));
					o.setTs(rs.getLong("ts"));
					return o;
				}
			}));
		}
		if (StringUtils.isNotBlank(r.getMac()) && StringUtils.isNotBlank(r.getChip()) && StringUtils.isBlank(r.getCurrentItem())) {
			rt = jdbcTemplate.query(QSQL1, new Object[] { r.getMac(), r.getChip()}, new RowMapperResultSetExtractor<Xaviera>(new RowMapper<Xaviera>() {
				public Xaviera mapRow(ResultSet rs, int rowNum) throws SQLException {
					Xaviera o = new Xaviera();
					o.setMac(rs.getString("mac"));
					o.setChip(rs.getString("chip"));
					o.setCurrentItem(rs.getString("currentItem"));
					o.setIsSuccess(rs.getString("isSuccess"));
					o.setTs(rs.getLong("ts"));
					return o;
				}
			}));
		}
		if (StringUtils.isBlank(r.getMac()) && StringUtils.isBlank(r.getChip()) && StringUtils.isBlank(r.getCurrentItem())) {
			rt = jdbcTemplate.query(QSQL3, new RowMapperResultSetExtractor<Xaviera>(new RowMapper<Xaviera>() {
				public Xaviera mapRow(ResultSet rs, int rowNum) throws SQLException {
					Xaviera o = new Xaviera();
					o.setMac(rs.getString("mac"));
					o.setChip(rs.getString("chip"));
					o.setCurrentItem(rs.getString("currentItem"));
					o.setIsSuccess(rs.getString("isSuccess"));
					o.setTs(rs.getLong("ts"));
					return o;
				}
			}));
		}
		return rt;
	}
}
