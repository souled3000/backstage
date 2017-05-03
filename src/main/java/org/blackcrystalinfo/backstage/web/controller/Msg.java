package org.blackcrystalinfo.backstage.web.controller;

import org.apache.commons.io.EndianUtils;
import org.blackcrystalinfo.backstage.utils.AESCoder;
import org.blackcrystalinfo.backstage.utils.MiscUtils;

public class Msg {
	public final static int HEADERCRCIDX = 30;
	public final static int BODYCRCIDX = 31;
	public final static int FrameHeaderLen = 24;
	public final static int DataHeaderLen = 16;
	public final static int HeaderLen = FrameHeaderLen + DataHeaderLen;
	public final static int CIPHERIDX = FrameHeaderLen + 8;
	public final static int GUIDLEN = 16;

	// 密证
	byte[] MZ;
	/** -----------------------FRAME HEADER---------------------------- */

	boolean FHFin;
	boolean FHMask;
	byte FHVer;
	boolean FHShort;
	byte FHOpcode;

	byte FHReserve;

	short FHSequence;

	int FHTime;

	long FHDstId;
	String FHDstMac;

	long FHSrcId;
	String FHSrcMac;
	byte[] FHGuid;

	/** ------------------------DATA HEADER--------------------------- */

	boolean DHRead;
	boolean DHAck;
	byte DHDataFormat;
	byte DHKeyLevel;
	byte DHEncryptType;

	byte DHDataSeq;

	short DHMsgId;

	short DHLength;

	byte DHHeadCheck;

	byte DHCheck;

	/** -------------------------BODY----------------------------------- */
	byte[] DHSessionId;

	/** ------------------------------------------------------------ */
	byte[] datacrc;
	byte[] Text;
	byte[] Final;
	byte[] RK;
	byte[] RKXORMZ;
	byte[] CIPHER;

	public String toString() {
		StringBuilder txt = new StringBuilder();
		txt.append(String.format("length:%d\n", this.Final.length));
		txt.append(String.format("掩码:%b\n", this.FHMask));
		txt.append(String.format("密证:%s\n", MiscUtils.toHex(this.MZ)));
		txt.append(String.format("报文:%s\n", MiscUtils.toHex(this.Final)));
		txt.append(String.format("Opcode:%x\n", this.FHOpcode));
		txt.append(String.format("MsgId:%x\n", this.DHMsgId));
		txt.append(String.format("源Id:%d\n", this.FHSrcId));
		txt.append(String.format("源Mac:%s\n", this.FHSrcMac));
		txt.append(String.format("目的Id:%d\n", this.FHDstId));
		txt.append(String.format("目的MAC:%s\n", this.FHDstMac));
		txt.append(String.format("加密级别:%x\n", this.DHKeyLevel));
		txt.append(String.format("DHSessionId:%s\n", MiscUtils.toHex(this.DHSessionId)));
		txt.append(String.format("FHGuid:%s\n", MiscUtils.toHex(this.FHGuid)));
		txt.append(String.format("FHSequence:%x\n", this.FHSequence));
		txt.append(String.format("FHTime:%x\n", this.FHTime));
		txt.append(String.format("DHLength:%d\n", this.DHLength));
		txt.append(String.format("DHHeadCheck:%x\n", this.DHHeadCheck));
		txt.append(String.format("DHCheck:%x\n", this.DHCheck));
		txt.append(String.format("DHAck:%b\n", this.DHAck));
		txt.append(String.format("正文明文:%s\n", MiscUtils.toHex(this.Text)));
		txt.append(String.format("密文:%s\n", MiscUtils.toHex(this.CIPHER)));
		txt.append(String.format("RK:%s\n", MiscUtils.toHex(this.RK)));
		txt.append(String.format("RKXORMZ:%s\n", MiscUtils.toHex(this.RKXORMZ)));
		txt.append(String.format("datacrc:%s", MiscUtils.toHex(this.datacrc)));
		return txt.toString();
	}

	public void Binary2Msg() throws Exception {

		if (this.Final == null || this.Final.length == 0) {
			throw new Exception("[MSG] The msg is empty.");
		}
		try {

			int msgLen = this.Final.length;

			this.FHOpcode = (byte) (0x07 & this.Final[0]);

			if ((this.Final[0] & 0x80) == 0x80) {
				this.FHFin = true;
			}
			if ((this.Final[0] & 0x40) == 0x40) {
				this.FHMask = true;
			}
			this.FHVer = (byte) ((this.Final[0] & 0x30) >> 4);
			if ((this.Final[0] & 0x08) == 0x08) {
				this.FHShort = true;
			}

			this.FHReserve = this.Final[1];

			this.FHSequence = EndianUtils.readSwappedShort(this.Final, 2);

			this.FHTime = EndianUtils.readSwappedInteger(this.Final, 4);
			if (this.FHMask) {
				switch (this.FHOpcode) {
				case 0x03:
					MiscUtils.MaskContent(this.Final, (short) 8, (short) 40, this.Final, 4);
					break;
				default:
					MiscUtils.MaskContent(this.Final, (short) 8, (short) 32, this.Final, 4);
				}
			}

			this.FHDstId = EndianUtils.readSwappedLong(this.Final, 8);
			this.FHDstMac = MiscUtils.toHex(this.Final, 8, 16);

			this.FHSrcId = EndianUtils.readSwappedLong(this.Final, 16);
			this.FHSrcMac = MiscUtils.toHex(this.Final, 16, 24);
			int offset = FrameHeaderLen;
			if (this.FHOpcode == 3) {
				offset = FrameHeaderLen + GUIDLEN;
				this.FHGuid = new byte[offset - FrameHeaderLen];
				System.arraycopy(this.Final, FrameHeaderLen, this.FHGuid, 0, offset - FrameHeaderLen);
			}

			if ((this.Final[offset] & 0x80) == 0x80) {
				this.DHRead = true;
			}
			if ((this.Final[offset] & 0x40) == 0x40) {
				this.DHAck = true;
			}
			this.DHDataFormat = (byte) ((this.Final[offset] & 0x30) >> 4);
			this.DHKeyLevel = (byte) ((this.Final[offset] & 0x0c) >> 2);
			this.DHEncryptType = (byte) (this.Final[offset] & 0x03);

			this.DHDataSeq = this.Final[offset + 1];
			this.DHMsgId = EndianUtils.readSwappedShort(this.Final, offset + 2);
			this.DHLength = EndianUtils.readSwappedShort(this.Final, offset + 4);

			if (this.DHLength != (this.Final.length - offset - 16)) {
				String er = String.format("[MSG:02] wrong body length : %d !=actual len %d", this.DHLength, this.Final.length - offset - 16);
				throw new Exception(er);
			}
			this.DHHeadCheck = this.Final[offset + 6];
			this.DHCheck = this.Final[offset + 7];
			if (this.DHHeadCheck != MiscUtils.crc(this.Final, offset + 6)) {
				String er = String.format("[MSG:02] checksum header error %x!=%x", this.DHHeadCheck, MiscUtils.crc(this.Final, offset + 6));
				throw new Exception(er);
			}

			switch (this.DHKeyLevel) {
			case 0:
				this.DHSessionId = new byte[8];
				System.arraycopy(this.Final, offset + 8, this.DHSessionId, 0, 8);
				byte[] ctn = new byte[this.Final.length - offset - 8];
				System.arraycopy(this.Final, offset + 8, ctn, 0, ctn.length);
				byte crc = MiscUtils.crc(ctn, ctn.length);
				if (this.DHCheck != crc) {
					String er = String.format("body crc error %x!=%x", this.DHCheck, crc);
					throw new Exception(er);
				}
				if (this.DHLength > 0) {
					this.Text = new byte[this.Final.length - offset - DataHeaderLen];
					System.arraycopy(this.Final, offset + DataHeaderLen, this.Text, 0, this.Text.length);
				}
				break;
			case 1:
				byte[] key = MiscUtils.RandomKeyGet(this.FHSequence, this.FHSrcId, this.FHTime);
				this.RK = key;
				System.out.println(String.format("随机密钥：%s", MiscUtils.toHex(key)));
				byte[] ciphertext = new byte[this.Final.length - offset - 8];
				this.CIPHER = ciphertext;

				System.out.println(ciphertext.length);
				System.arraycopy(this.Final, offset + 8, ciphertext, 0, ciphertext.length);
				byte[] aa = null;

				aa = AESCoder.decrypt(ciphertext, key);
				crc = MiscUtils.crc(aa);

				if (this.DHCheck != crc) {
					throw new Exception(String.format("body crc error %d!=%d|%s", this.DHCheck, crc, MiscUtils.toHex(aa)));
				}
				System.out.println(aa.length);
				this.DHSessionId = new byte[8];
				System.arraycopy(aa, 0, this.DHSessionId, 0, 8);
				if (this.DHLength > 0) {
					this.Text = new byte[aa.length - 8];
					System.arraycopy(aa, 8, this.Text, 0, this.Text.length);
				}
				break;
			case 2:
			case 3:
				if (this.MZ != null) {
					byte[] a = MiscUtils.RandomKeyGet(this.FHSequence, this.FHSrcId, this.FHTime);
					this.RK = a;

					key = new byte[16];
					int n = 0;
					for (byte x : a) {
						key[n] = (byte) (x ^ this.MZ[n]);
						n++;
					}

					byte[] crytograph = new byte[this.Final.length - offset - 8];
					System.arraycopy(this.Final, offset + 8, crytograph, 0, crytograph.length);

					this.RKXORMZ = key;
					this.CIPHER = crytograph;

					aa = AESCoder.decrypt(crytograph, key);
					crc = MiscUtils.crc(aa);
					if (this.DHCheck != crc) {
						throw new Exception(String.format("body crc error %d!=%d|%s|%d", this.DHCheck, crc, MiscUtils.toHex(aa), aa.length));
					}
					this.DHSessionId = new byte[8];
					System.arraycopy(aa, 0, this.DHSessionId, 0, 8);
					if (this.DHLength > 0) {
						this.Text = new byte[aa.length - 8];
						System.arraycopy(aa, 8, this.Text, 0, this.Text.length);
					}
				} else {
					throw new Exception(String.format("MZ is nil but Opcode is 3,%s->%s", this.FHSrcMac, this.FHDstMac));
				}
				break;
			default:
				throw new Exception(String.format("[MSG:02] wrong KeyLevel %d", this.DHKeyLevel));
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (this.FHMask) {
				switch (this.FHOpcode) {
				case 0x03:
					MiscUtils.MaskContent(this.Final, (short) 8, (short) 40, this.Final, 4);
					break;
				default:
					MiscUtils.MaskContent(this.Final, (short) 8, (short) 32, this.Final, 4);
				}
			}
		}
	}

	public void Msg2Binary() throws Exception {
		this.Final = new byte[] {};
		int offset = FrameHeaderLen;
		if (this.FHOpcode == 3) {
			offset = FrameHeaderLen + GUIDLEN;
		}
		byte[] buf = new byte[offset + DataHeaderLen];

		if (this.FHFin) {
			buf[0] |= 0x80; // 1<<7
		}
		if (this.FHMask) {
			buf[0] |= 0x40; // 1<<6
		}
		if (this.FHVer != 0) {
			buf[0] |= (this.FHVer & 0x3) << 4;
		}
		if (this.FHShort) {
			buf[0] |= 0x08;
		}

		buf[0] |= this.FHOpcode & 0x7;

		buf[1] = this.FHReserve;
		// binary.LittleEndian.PutUint16(buf[2:4], this.FHSequence)
		EndianUtils.writeSwappedShort(buf, 2, this.FHSequence);

		if (this.FHTime == 0) {
			long a = System.currentTimeMillis() / (long) 1e3;
			byte[] b = new byte[8];
			// binary.LittleEndian.PutUint64(b, uint64(a))
			EndianUtils.writeSwappedLong(b, 0, a);
			// this.FHTime = binary.BigEndian.Uint32(b[0:4])
			this.FHTime = (int) EndianUtils.readSwappedUnsignedInteger(b, 0);
		}
		// binary.LittleEndian.PutUint32(buf[4:8], this.FHTime)
		EndianUtils.writeSwappedInteger(buf, 4, this.FHTime);

		// binary.LittleEndian.PutUint64(buf[8:16], uint64(this.FHDstId))
		EndianUtils.writeSwappedLong(buf, 8, this.FHDstId);
		// binary.LittleEndian.PutUint64(buf[16:24], uint64(this.FHSrcId))
		EndianUtils.writeSwappedLong(buf, 16, this.FHSrcId);

		// this.FHDstMac = hex.EncodeToString(buf[8:16])
		this.FHDstMac = MiscUtils.toHex(buf, 8, 16);
		// this.FHSrcMac = hex.EncodeToString(buf[16:24])
		this.FHSrcMac = MiscUtils.toHex(buf, 16, 24);

		if (this.DHRead) {
			buf[offset] |= 0x80; // 1<<7
		}
		if (this.DHAck) {
			buf[offset] |= 0x40; // 1<<6
		}
		if (this.DHDataFormat != 0) {
			buf[offset] |= (this.DHDataFormat & 0x3) << 4;
		}
		if (this.DHKeyLevel != 0) {
			buf[offset] |= (this.DHKeyLevel & 0x3) << 2;
		}
		if (this.DHEncryptType != 0) {
			buf[offset] |= this.DHEncryptType & 0x3;
		}

		buf[offset + 1] = this.DHDataSeq;

		// binary.LittleEndian.PutUint16(buf[offset+2:offset+4], this.DHMsgId)
		EndianUtils.writeSwappedShort(buf, offset + 2, this.DHMsgId);

		// 需要crc检验的帧头有30字节

		// copy(buf[offset+8:offset+16], this.DHSessionId)
		System.arraycopy(this.DHSessionId, 0, buf, offset + 8, 8);

		// this.datacrc = append(this.datacrc, buf[offset+8:offset+16]...)
		// this.datacrc = append(this.datacrc, this.Text...)

		this.datacrc = new byte[8 + this.Text.length];
		System.arraycopy(buf, offset + 8, this.datacrc, 0, 8);
		System.arraycopy(this.Text, 0, this.datacrc, 8, this.Text.length);

		// switch this.DHKeyLevel {
		switch (this.DHKeyLevel) {
		case 0:
			this.DHLength = (short) this.Text.length;
			// binary.LittleEndian.PutUint16(buf[offset+4:offset+6], uint16(len(this.Text)))
			EndianUtils.writeSwappedShort(buf, offset + 4, this.DHLength);
			// buf[offset+6] = common.Crc(buf[:offset+6], offset+6)
			buf[offset + 6] = MiscUtils.crc(buf, offset + 6);
			this.DHHeadCheck = buf[offset + 6];
			buf[offset + 7] = MiscUtils.crc(this.datacrc);
			this.DHCheck = buf[offset + 7];
			// this.Final = append(this.Final, buf...)
			// this.Final = append(this.Final, this.Text...)
			this.Final = new byte[buf.length + this.Text.length];
			System.arraycopy(buf, 0, this.Final, 0, buf.length);
			System.arraycopy(this.Text, 0, this.Final, buf.length, this.Text.length);
			break;
		case 1:
			buf[offset + 7] = MiscUtils.crc(this.datacrc);
			this.DHCheck = buf[offset + 7];
			byte[] key = MiscUtils.RandomKeyGet(this.FHSequence, this.FHSrcId, this.FHTime);
			this.RK = key;
			// cipher, err := aes.Encrypt(this.datacrc, key)
			// if err != nil {
			// glog.Errorf("%v:%x", err, this.Final)
			// }
			byte[] cipher = AESCoder.encrypt(this.datacrc, key);
			// binary.LittleEndian.PutUint16(buf[offset+4:offset+6], uint16(len(cipher)-8))
			EndianUtils.writeSwappedShort(buf, offset + 4, (short) (cipher.length - 8));
			this.DHLength = (short) (cipher.length - 8);
			buf[offset + 6] = MiscUtils.crc(buf, offset + 6);
			this.DHHeadCheck = buf[offset + 6];
			// this.Final = append(this.Final, buf[:offset+8]...)
			// this.Final = append(this.Final, cipher...)
			this.Final = new byte[offset + 8 + cipher.length];
			System.arraycopy(buf, 0, this.Final, 0, offset + 8);
			System.arraycopy(cipher, 0, this.Final, offset + 8, cipher.length);
			break;
		case 2:
		case 3:
			if (this.MZ != null) {
				buf[offset + 7] = MiscUtils.crc(this.datacrc);
				this.DHCheck = buf[offset + 7];
				byte[] a = MiscUtils.RandomKeyGet(this.FHSequence, this.FHSrcId, this.FHTime);
				this.RK = a;
				key = new byte[16];
				for (int n = 0; n < a.length; n++) {
					key[n] = (byte) (a[n] ^ this.MZ[n]);
				}
				this.RKXORMZ = key;
				cipher = AESCoder.encrypt(this.datacrc, key);
				this.CIPHER = cipher;
				// binary.LittleEndian.PutUint16(buf[offset+4:offset+6], uint16(len(cipher)-8))
				this.DHLength = (short) (cipher.length - 8);
				EndianUtils.writeSwappedShort(buf, offset + 4, this.DHLength);
				buf[offset + 6] = MiscUtils.crc(buf, offset + 6);
				this.DHHeadCheck = buf[offset + 6];
				// this.Final = append(this.Final, buf[:offset+8]...)
				// this.Final = append(this.Final, cipher...)
				this.Final = new byte[offset + 8 + cipher.length];
				System.arraycopy(buf, 0, this.Final, 0, offset + 8);
				System.arraycopy(cipher, 0, this.Final, offset + 8, cipher.length);
			} else {
				throw new Exception(String.format("MZ is nil but DHKeyLevel is 3,%s", MiscUtils.toHex(this.Final)));
			}
			break;
		}
		if (this.FHMask) {
			switch (this.FHOpcode) {
			case 0x03:
				MiscUtils.MaskContent(this.Final, (short) 8, (short) 40, this.Final, 4);
				break;
			default:
				MiscUtils.MaskContent(this.Final, (short) 8, (short) 32, this.Final, 4);
			}
		}
		return;
	}

	public static void main(String[] args) throws Exception {
		
		// begId("fdffffffffffffff");
		testMsg();
	}
	private static void testMsg() throws Exception {

		Msg m = new Msg();
		m.MZ = MiscUtils.fromHex("fb95ab046904be5a76c1c652667c317a");
		m.Final = MiscUtils.fromHex("c20001001c2b00001c2b00001c2b00001c2bb0d58148f7589a2bc000042ba8842d6e77889658328c9b0e71af2e353a2efdd9935c6ab521e07cc2366b602504b8");
		try {
			m.Binary2Msg();
			System.out.println(m.toString());
			System.out.println();
			System.out.println();
			System.out.println();
			m.Final = null;
			m.DHKeyLevel = 3;
			m.Msg2Binary();
			System.out.println(m.toString());
			System.out.println();
			System.out.println();
			System.out.println();
			Msg p = m;
			m = new Msg();
			m.Final = p.Final;
			m.MZ = p.MZ;
			m.Binary2Msg();
			System.out.println(m.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}