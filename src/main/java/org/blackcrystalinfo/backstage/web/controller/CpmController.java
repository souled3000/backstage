package org.blackcrystalinfo.backstage.web.controller;

import org.apache.commons.lang.StringUtils;
import org.blackcrystalinfo.backstage.busi.ICpmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CpmController {
	private static final Logger log = LoggerFactory.getLogger(CpmController.class);
	@Autowired
	ICpmService cpmService;

	@RequestMapping(value = "/cpm", method = RequestMethod.GET)
	public String q() {
		return "cpm";
	}
	@RequestMapping(value = "/menu", method = RequestMethod.GET)
	public String m() {
		return "menu";
	}

	@RequestMapping(value = "/cpm/query", method = RequestMethod.POST)
	public String search(@RequestParam(required = false) String in, Model m) {
		log.info(in);
		in = in.trim();
		m.addAttribute("in", in);
		String out = "";
		if (StringUtils.isNotBlank(in)) {
			try {
				if (in.matches("[0-9a-fA-F]{16}")) {
					log.info("mac");
					out = cpmService.gotIdByMac(in);
				} else if (in.matches("\\-\\d+")) {
					log.info("device");
					out = cpmService.gotMacById(in);
				} else if (in.matches("\\d+")) {
					log.info("customer");
					out = cpmService.gotAccountById(in);
				} else if (in.matches("\\w+")) {
					log.info("account");
					out = cpmService.gotIdByAccount(in);
				}
			} catch (Exception e) {
				out = e.getMessage();
				if (in.matches("\\w+")) {
					log.info("account");
					try {
						out = cpmService.gotIdByAccount(in);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		}else{
			try {
				out=cpmService.gotOut2();
			} catch (Exception e) {
				e.printStackTrace();
				out = e.getMessage();
			}
		}
		m.addAttribute("out", out);
		log.info("RESULT: {} {}", in, out);
		return "cpm";
	}

	public static void main(String[] args) {
		String in = "-3011";
		if (in.matches("\\d+")) {
			System.out.println("是正数");
		}
		if (in.matches("\\-\\d+")) {
			System.out.println("是负数");
		}
		if (in.matches("[0-9a-fA-F]{16}")) {
			System.out.println("是MAC");
		}
		if (in.matches("\\w+")) {
			System.out.println("是Account");
		}
	}

}
