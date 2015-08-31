package org.blackcrystalinfo.backstage.web.controller;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.blackcrystalinfo.backstage.bo.AppVersion;
import org.blackcrystalinfo.backstage.busi.IAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;

@Controller
public class AppController {

	@Autowired
	IAppService appService;

	@RequestMapping(value = "/check")
	public ResponseEntity<String> check(String id, String cur) throws Exception {
		Map<?, ?> m = appService.check(id, cur);
		HttpHeaders headers = new HttpHeaders();
		MediaType mt = new MediaType("application", "json",
				Charset.forName("UTF-8"));
		headers.setContentType(mt);
		headers.add("charset", "UTF-8");
		return new ResponseEntity<String>(JSON.toJSONString(m), headers,
				HttpStatus.OK);
	}

	@RequestMapping(value = "/app")
	public String applist(Model model) {
		model.addAttribute("applist", getApplist());
		return "app";
	}

	private Object getApplist() {
		List<AppVersion> result = new ArrayList<AppVersion>();
		List<Map<String, Object>> apps = appService.apps();

		for (Map<String, Object> app : apps) {
			AppVersion appVersion = new AppVersion();
			appVersion.setId((String) app.get("id"));
			appVersion.setAvail((String) app.get("avail"));
			appVersion.setLast((String) app.get("last"));
			appVersion.setMsg((String) app.get("msg"));
			appVersion.setUrl((String) app.get("url"));
			result.add(appVersion);
		}

		return result;
	}

	@RequestMapping(value = "/appupload")
	public String appupload(HttpServletRequest request,
			@RequestParam(value = "file", required = false) MultipartFile file,
			ModelMap model) {
		String appType = request.getParameter("appType");
		String curVer = request.getParameter("curVer");
		String minVer = request.getParameter("minVer");
		String msg = request.getParameter("msg");
		String appSaveFolder = "apps";

		// 保存文件名称
		String saveName = appType + "_" + System.currentTimeMillis() + "_"
				+ file.getOriginalFilename();

		// 拼接下载文件路径：http://ip:port/webapp/apps/apptype_appver_timestamp_filename
		String downloadUrl = request.getScheme() + "://"
				+ request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath() + "/" + appSaveFolder + "/"
				+ saveName;

		// 拼接存储绝对路径：absolute_path/apps/apptype_appver_timestamp_filename
		String appSavePath = request.getServletContext().getRealPath("/") + "/"
				+ appSaveFolder + "/" + saveName;

		try {
			FileUtils.writeByteArrayToFile(new File(appSavePath),
					file.getBytes(), false);
			System.out.println("upload success~~~>>> " + appSavePath);
		} catch (IOException e) {
			e.printStackTrace();
		}

		appService.save(appType, curVer, minVer, msg, downloadUrl);

		return "redirect:app";
	}

	@RequestMapping(value = "/apps/{appname:.*}", method = RequestMethod.GET, produces = {"application/octet-stream","application/*",})
	public ResponseEntity<byte[]> download(@PathVariable String appname) throws IOException {
		HttpHeaders headers = new HttpHeaders();
		MediaType mt = new MediaType("application", "octet-stream");
		headers.setContentType(mt);

		String appSaveFolder = "apps";

		String appSavePath = ContextLoader.getCurrentWebApplicationContext()
				.getServletContext().getRealPath("/")
				+ "/" + appSaveFolder + "/" + appname;
		File dir = new File(appSavePath);
		if (!dir.exists()) {
			return new ResponseEntity<byte[]>(null, headers,
					HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(dir),
				headers, HttpStatus.OK);
	}
}
