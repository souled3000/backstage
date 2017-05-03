package org.blackcrystalinfo.backstage.web.controller;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.blackcrystalinfo.backstage.bo.CfgLd2;
import org.blackcrystalinfo.backstage.utils.Constants;
import org.blackcrystalinfo.backstage.utils.DataHelper;
import org.blackcrystalinfo.backstage.utils.MiscUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import redis.clients.jedis.Jedis;

@Controller
public class Ld2Controller {

	@RequestMapping(value = "/ld2", method = RequestMethod.GET)
	public String init(Model model) throws Exception {
		Jedis r = DataHelper.getJedis();
		Set<CfgLd2> cfgList = new TreeSet<CfgLd2>();
		model.addAttribute("cfgs", cfgList);
		try {
			Map<byte[], byte[]> cfgMap = r.hgetAll("ld2cfg".getBytes());
			for (Map.Entry<byte[], byte[]> e : cfgMap.entrySet()) {
				CfgLd2 o = new CfgLd2();
				o.setEnd(e.getValue());
				o.b2o();
				cfgList.add(o);
			}
		} catch (Exception e) {

		} finally {
			DataHelper.returnJedis(r);
		}
		return "ld2mgr";
	}

	@RequestMapping(value = "/ld2/add", method = RequestMethod.GET)
	public String enterAdd(Model model) throws Exception {
		CfgLd2 cfg = new CfgLd2();
		model.addAttribute("cfg", cfg);
		model.addAttribute("ld2Types", Constants.ld2TypeList);
		return "ld2ea";
	}

	@RequestMapping(value = "/ld2/edit/{devType}", method = RequestMethod.GET)
	public String enterEdit(@PathVariable String devType, Model model) throws Exception {
		Jedis j = DataHelper.getJedis();
		try {
			CfgLd2 o = new CfgLd2();
			o.setEnd(j.hget("ld2cfg".getBytes(), devType.getBytes()));
			o.b2o();
			o.setStrAlarms(StringUtils.join(o.getAlarms().toArray(), ","));
			model.addAttribute("cfg", o);
			model.addAttribute("ld2Types", Constants.ld2TypeList);
		} catch (Exception e) {

		} finally {
			DataHelper.returnJedis(j);
		}
		return "ld2ea";
	}

	@RequestMapping(value = "/ld2/del/{devType}", method = RequestMethod.GET)
	public String del(@PathVariable String devType, Model model) throws Exception {
		Jedis j = DataHelper.getJedis();
		try {
			j.hdel("ld2cfg".getBytes(), devType.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DataHelper.returnJedis(j);
		}
		return "redirect:/ld2";
	}

	@RequestMapping(value = "/ld2/save", method = RequestMethod.POST)
	public String save(@ModelAttribute("cfg") @Valid CfgLd2 cfg, BindingResult result, Model model) throws Exception {
		if (result.hasErrors()) {
			for (ObjectError e : result.getAllErrors()) {
				System.out.println(e.toString());
			}
			model.addAttribute("cfg", cfg);
			model.addAttribute("ld2Types", Constants.ld2TypeList);
			return "ld2ea";
		}
		Jedis j = DataHelper.getJedis();
		try {
			if (StringUtils.isNotBlank(cfg.getStrAlarms())) {
				for (String alarmCode : cfg.getStrAlarms().split("[ ,;:]")) {
					cfg.getAlarms().add(alarmCode);
				}
			}
			cfg.o2b();
			j.hset("ld2cfg".getBytes(), cfg.getDevType().getBytes(), cfg.getEnd());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DataHelper.returnJedis(j);
		}
		return "redirect:/ld2";
	}

	@RequestMapping(value = "/ld2/dtl/{devType}", method = RequestMethod.GET)
	public String dtl(@PathVariable String devType, Model model) throws Exception {

		Jedis j = DataHelper.getJedis();
		try {
			CfgLd2 o = new CfgLd2();
			o.setEnd(j.hget("ld2cfg".getBytes(), devType.getBytes()));
			o.b2o();
			o.setStrEnd(MiscUtils.toHex(o.getEnd()));
			model.addAttribute("c", o);
		} catch (Exception e) {

		} finally {
			DataHelper.returnJedis(j);
		}
		return "ld2dtl";
	}
}
