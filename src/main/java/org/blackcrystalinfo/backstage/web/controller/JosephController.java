package org.blackcrystalinfo.backstage.web.controller;

import org.apache.commons.lang.StringUtils;
import org.blackcrystalinfo.backstage.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONArray;

import backtype.storm.utils.DRPCClient;

@Controller
public class JosephController {
	private static final Logger log = LoggerFactory.getLogger(JosephController.class);

	@RequestMapping(value = "/joseph", method = RequestMethod.GET)
	public String q() {
		return "joseph";
	}

	@RequestMapping(value = "/joseph/query", method = RequestMethod.POST)
	public String search(@RequestParam(required = false) String dt, Model m) {
		log.info("[joseph] query {}", dt);
		DRPCClient client = new DRPCClient(Constants.DRPC_IP, Constants.DRPC_PORT);
		Long ammount = 0L;
		try {
			if (StringUtils.isNotBlank(dt)) {
				String s = client.execute("joseph", dt);
				JSONArray ary = JSONArray.parseArray(s);
				JSONArray record = ary.getJSONArray(0);
				ammount = record.getLong(1);
			}
		} catch (Exception e) {
			log.error("", e);
		} finally {
			client.close();
		}
		log.info("[joseph] {}:{}", dt, ammount);
		m.addAttribute("msg", ammount == 0L ? "" : dt + ":" + String.valueOf(ammount));
		return "joseph";
	}
}
