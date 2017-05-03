package org.blackcrystalinfo.backstage.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.blackcrystalinfo.backstage.bo.Xaviera;
import org.blackcrystalinfo.backstage.busi.IXavieraSrv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.alibaba.fastjson.JSON;

@Controller
public class XavieraController {
	private static final Logger log = LoggerFactory.getLogger(XavieraController.class);
	@Autowired
	IXavieraSrv xavieraSrv;

	@RequestMapping(value = "/xav/imp", method = RequestMethod.GET)
	public ModelAndView imp(@RequestParam String mac,@RequestParam String chip,@RequestParam String currentItem,@RequestParam String isSuccess) {
		log.info("[xaviera] imp data");
		Xaviera o = new Xaviera();
		o.setMac(mac);
		o.setChip(chip);
		o.setCurrentItem(currentItem);
		o.setIsSuccess(isSuccess);
		xavieraSrv.importData(o);
		Map<String, String> m = new HashMap<String,String>();
		m.put("rt", "y");
		return new ModelAndView(new MappingJackson2JsonView(),m);
	}

	@ResponseBody
	@RequestMapping(value = "/xav/query", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public String q(@RequestParam(required=false) String mac,@RequestParam(required=false) String chip,@RequestParam(required=false) String currentItem) {
		log.info("[xaviera] query");
		Xaviera r = new Xaviera();
		r.setMac(mac);
		r.setChip(chip);
		r.setCurrentItem(currentItem);
		List<Xaviera> d = xavieraSrv.getDate(r);
		String rt = JSON.toJSONString(d);
		return rt;
	}

}
