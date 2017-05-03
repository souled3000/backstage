package org.blackcrystalinfo.backstage.bo;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Pattern;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.blackcrystalinfo.backstage.utils.DataHelper;
import org.blackcrystalinfo.backstage.utils.MiscUtils;
import org.hibernate.validator.constraints.NotEmpty;

import redis.clients.jedis.Jedis;

public class CfgLd2 implements Comparable<CfgLd2> {
	private String checksum;
	@NotEmpty
	@Pattern(regexp = "^[0-9a-fA-F]{2}$", message = "{ld2.version.error}")
	private String version;
	private String AndOr;
	@NotEmpty
	@Pattern(regexp = "^[0-9a-fA-F]{2}$", message = "{ld2.ld2type.error}")
	private String ld2Type;
	@NotEmpty
	@Pattern(regexp = "^[0-9a-fA-F]{8}$", message = "{ld2.devtype.error}")
	private String devType;
	private int alarmAmount;
	
	private List<String> alarms;
	private int actionLength;
	private String actionOrigin;
	private String actionCtn;
	
	@Pattern(regexp = "^$|^[0-9a-fA-F]{4}(,[0-9a-fA-F]{4})*$", message = "{ld2.alarms.error}")
	private String strAlarms;
	private byte[] end;
	private String strEnd;
	public CfgLd2() {
		this.alarms = new ArrayList<String>();
	}

	public String getChecksum() {
		return checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getAndOr() {
		return AndOr;
	}

	public void setAndOr(String andOr) {
		AndOr = andOr;
	}

	public String getLd2Type() {
		return ld2Type;
	}

	public void setLd2Type(String ld2Type) {
		this.ld2Type = ld2Type;
	}

	public String getDevType() {
		return devType;
	}

	public void setDevType(String devType) {
		this.devType = devType;
	}

	public int getAlarmAmount() {
		return alarmAmount;
	}

	public void setAlarmAmount(int alarmAmount) {
		this.alarmAmount = alarmAmount;
	}

	public List<String> getAlarms() {
		return alarms;
	}

	public void setAlarms(List<String> alarms) {
		this.alarms = alarms;
	}

	public int getActionLength() {
		return actionLength;
	}

	public void setActionLength(int actionLength) {
		this.actionLength = actionLength;
	}

	public String getActionOrigin() {
		return actionOrigin;
	}

	public void setActionOrigin(String actionOrigin) {
		this.actionOrigin = actionOrigin;
	}

	public String getActionCtn() {
		return actionCtn;
	}

	public void setActionCtn(String actionCtn) {
		this.actionCtn = actionCtn;
	}

	public byte[] getEnd() {
		return end;
	}

	public void setEnd(byte[] end) {
		this.end = end;
	}

	public void b2o() {
		int len = this.end.length;
		this.checksum = MiscUtils.toHex(new byte[] { this.end[0] });
		this.version = MiscUtils.toHex(new byte[] { this.end[1] });
		byte b = (byte) (((int) this.end[2] & 0xe0) >> 5);
		this.ld2Type = MiscUtils.toHex(new byte[] { b });
		b = (byte) (((int) this.end[2] & 0x10) >> 4);
		this.AndOr = MiscUtils.toHex(new byte[] { b });
		this.alarmAmount = this.end[2] & 0x0f;
		this.devType = MiscUtils.toHex(MiscUtils.swapBytes(ArrayUtils.subarray(this.end, 4, 8)));
		for (int i = 0; i < this.alarmAmount; i++) {
			this.alarms.add(MiscUtils.toHex(MiscUtils.swapBytes(ArrayUtils.subarray(this.end, 8 + i * 2, 8 + i * 2 + 2))));
		}
		if (len > 8 + this.alarmAmount * 2) {
			this.actionLength = this.end[8 + this.alarmAmount * 2];
			this.actionOrigin = MiscUtils.toHex(new byte[] { this.end[8 + this.alarmAmount * 2 + 1] });
			this.actionCtn = MiscUtils.toHex(ArrayUtils.subarray(this.end, 8 + this.alarmAmount * 2 + 2, len));
		}
	}

	public void o2b() {
		this.end = null;
		byte[] end = null;
		end = ArrayUtils.addAll(end, MiscUtils.fromHex(this.version));
		byte b = 0;
		b |= ((MiscUtils.fromHex(this.ld2Type)[0] & 0x07) << 5);
		b |= ((MiscUtils.fromHex(this.AndOr)[0] & 0x01) << 4);
		b |= (byte) (this.alarms.size());
		end = ArrayUtils.add(end, b);
		end = ArrayUtils.add(end, (byte) 0);
		end = ArrayUtils.addAll(end, MiscUtils.swapBytes(MiscUtils.fromHex(this.devType)));
		for (String alarm : this.alarms) {
			end = ArrayUtils.addAll(end, MiscUtils.swapBytes(MiscUtils.fromHex(alarm)));
		}
		if (StringUtils.isNotBlank(this.actionCtn)) {
			byte[] actionOrigin = MiscUtils.fromHex(this.actionOrigin);
			byte[] actionCtn = MiscUtils.fromHex(this.actionCtn);
			end = ArrayUtils.add(end, (byte) (actionCtn.length));
			end = ArrayUtils.addAll(end, actionOrigin);
			end = ArrayUtils.addAll(end, actionCtn);
		}
		byte checksum = MiscUtils.crc(end);
		this.end = ArrayUtils.add(this.end, checksum);
		this.end = ArrayUtils.addAll(this.end, end);
	}

	public static void f() {
		for (String alarmCode : "".split("[ ,;:]")){
			System.out.println("".split("[ ,;:]").length);
		}
		CfgLd2 o = new CfgLd2();
		o.devType = "111199aa";
		o.version = "22";
		o.ld2Type = "01";
		o.AndOr = "01";
//		o.alarms = new ArrayList<String>();
//		o.alarms.add("1122");
//		o.alarms.add("3344");
//		o.alarms.add("5566");
//		o.alarmAmount = o.alarms.size();
		o.actionCtn = "99999999999999999999999";
//		o.actionLength = o.actionCtn.length() / 2 + 1;
		o.actionOrigin = "66";
		o.o2b();
		System.out.println(MiscUtils.toHex(o.end));
		CfgLd2 q = new CfgLd2();
		q.end = o.end;
		q.b2o();
		System.out.println(q);
		q.o2b();
		System.out.println(MiscUtils.toHex(q.end));
		System.out.println(q);

	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("Checksum:").append(this.checksum).append("\n");
		s.append("DevType:").append(this.devType).append("\n");
		s.append("Ld2Type:").append(this.ld2Type).append("\n");
		s.append("Version:").append(this.version).append("\n");
		s.append("AndOr:").append(this.AndOr).append("\n");
		s.append("AlarmAmount:").append(this.alarmAmount).append("\n");
		s.append("Alarms:").append(this.alarms).append("\n");
		s.append("ActionOrigin:").append(this.actionOrigin).append("\n");
		s.append("ActionCtn:").append(this.actionCtn).append("\n");
		if(this.end!=null)
		s.append("End:").append(MiscUtils.toHex(this.end)).append("\n");
		return s.toString();
	}

	public static void main(String[] args) throws Exception{
		f();
	}

	public String getStrAlarms() {
		return strAlarms;
	}

	public void setStrAlarms(String strAlarms) {
		this.strAlarms = strAlarms;
	}

	
	public static void f2() throws Exception{
		Jedis j = DataHelper.getJedis();
		CfgLd2 o = new CfgLd2();
		o.setEnd(j.hget("ld2cfg".getBytes(), "11111111".getBytes()));
		o.b2o();
		DataHelper.returnJedis(j);
		System.out.println(o);
	}

	public String getStrEnd() {
		return strEnd;
	}

	public void setStrEnd(String strEnd) {
		this.strEnd = strEnd;
	}

	@Override
	public int compareTo(CfgLd2 o) {
		return this.devType.compareTo(o.devType);
	}
}