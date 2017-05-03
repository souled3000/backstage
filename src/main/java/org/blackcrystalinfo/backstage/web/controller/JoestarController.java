package org.blackcrystalinfo.backstage.web.controller;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.EndianUtils;
import org.apache.commons.lang.StringUtils;
import org.blackcrystalinfo.backstage.utils.AESCoder;
import org.blackcrystalinfo.backstage.utils.MiscUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class JoestarController {

	private static final Logger log = LoggerFactory.getLogger(JoestarController.class);

	@RequestMapping(value = "/joestar", method = RequestMethod.GET)
	public String q() {
		return "joestar";
	}

	@RequestMapping(value = "/joestar/query", method = RequestMethod.POST)
	public String search(@RequestParam(required = false) String mz, @RequestParam(required = false) String graph, @RequestParam(required = false) String fs, Model m) {
		log.info("[joestar] query {} {} {}", new Object[] { mz, graph, fs });
		m.addAttribute("mz", mz);
		m.addAttribute("graph", graph);
		m.addAttribute("fs", fs);
		if (StringUtils.isNotBlank(fs)) {
			long l;
			try {
				l = begId(fs);
				m.addAttribute("fsmsg", l);
			} catch (Exception e) {
			}
		}
		Msg msg = new Msg();
		if (StringUtils.isNotBlank(mz))
			msg.MZ = MiscUtils.fromHex(mz);
		try {
			msg.Final = MiscUtils.fromHex(graph);
			msg.Binary2Msg();
			m.addAttribute("msg", msg.toString());
		} catch (Exception e) {
			log.error("", e);
			m.addAttribute("msg", e.getMessage() + "\r\n" + msg.toString());
		} finally {
		}
		return "joestar";
	}


	private static long begId(String fs) throws Exception {
		byte[] ary = Hex.decodeHex(fs.toCharArray());
		long l = EndianUtils.readSwappedLong(ary, 0);
		System.out.println(l);
		return l;
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
