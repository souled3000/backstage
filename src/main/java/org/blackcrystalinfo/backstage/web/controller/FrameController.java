package org.blackcrystalinfo.backstage.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class FrameController {
	@RequestMapping(value = "/frame",method = RequestMethod.GET)
	public void login(){
	}
}
