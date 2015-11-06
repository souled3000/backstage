package org.blackcrystalinfo.backstage.web.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.io.FileUtils;
import org.blackcrystalinfo.backstage.bo.ChipVersion;
import org.blackcrystalinfo.backstage.bo.User;
import org.blackcrystalinfo.backstage.utils.MiscUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.ContextLoader;

import com.alibaba.fastjson.JSONObject;

@Controller
public class VersionController {

	private static ResourceBundle rb = null;

	static {
		try {
			rb = ResourceBundle.getBundle("sys");
		} finally {

		}
	}

	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public String init(Model model) throws Exception {
		model.addAttribute("chip", new ChipVersion());
		// 获取版本列表
		model.addAttribute("chips", gotChips());
		return "version";
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String upload(HttpServletRequest request, /**@RequestParam MultipartFile file,*/ @ModelAttribute("chip") @Valid ChipVersion chip, BindingResult result, Model model) throws Exception {
		if (result.hasErrors()) {
			// 获取版本列表
			model.addAttribute("chips", gotChips());
			return "version";
		}
		// 写芯片版本文件
		chip.gen();
		String dir0 = request.getServletContext().getRealPath("/");
		String dir1 = rb.getString("upload.dir");
		String dir2 = rb.getString("upload.info.dir");

		StringBuilder fileName = new StringBuilder();
		fileName.append(dir0).append(File.separator).append(dir1).append(File.separator).append(chip.getFile().getOriginalFilename());
		File aim = new File(fileName.toString());
		// 设置md5
		MessageDigest digest = MessageDigest.getInstance("MD5");
		byte[] ctn = chip.getFile().getBytes();
		byte[] md5 = digest.digest(ctn);
		chip.setMd5(MiscUtils.toHex(md5));
		//取设备类型
		int deviceType = (int)ctn[29]<<24|(int)ctn[28]<<16|(int)ctn[27]<<8|(int)ctn[26];
		
		// 设置URL
		chip.setChipType(chip.getChipType()+"_"+deviceType);
		chip.setDeviceType(deviceType+"");
		fileName.delete(0, fileName.length());
		fileName.append(request.getScheme()).append("://").append(request.getServerName()).append(":").append(request.getServerPort()).append(request.getContextPath());
		fileName.append("/").append(dir1).append("/").append(chip.getFile().getOriginalFilename());
		chip.setUrl(fileName.toString());
		// 写芯片版本文件
		fileName.delete(0, fileName.length());
		fileName.append(dir0).append(File.separator).append(dir2).append(File.separator).append(chip.getChipType());
		
		File f = new File(fileName.toString());
		FileUtils.writeStringToFile(f, MiscUtils.writeJSON(chip, "file", "chipType"),false);
//		chip.getFile().transferTo(aim);
		FileUtils.writeByteArrayToFile(aim, chip.getFile().getBytes());
		// 获取所有版本信息
		model.addAttribute("chips", gotChips());
		return "version";
	}
	@RequestMapping(value = "/chips/{device}", method = RequestMethod.GET,produces="application/octet-stream")
	public ResponseEntity<byte[]> download(@PathVariable String device) throws IOException{
		HttpHeaders headers = new HttpHeaders();   
	    MediaType mt=new MediaType("application","octet-stream");   
	    headers.setContentType(mt);
		String dir0 = ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath("/");
		ResourceBundle rb = ResourceBundle.getBundle("sys");
		String dir1 = rb.getString("upload.dir");
		StringBuilder fileName = new StringBuilder();
		fileName.append(dir0).append(File.separator).append(dir1).append(File.separator).append(device);
		File dir = new File(fileName.toString());
		return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(dir), headers, HttpStatus.OK);
	}
	private List<ChipVersion> gotChips() throws Exception {
		List<ChipVersion> chips = new ArrayList<ChipVersion>();
		String dir0 = ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath("/");
		ResourceBundle rb = ResourceBundle.getBundle("sys");
		String dir1 = rb.getString("upload.info.dir");
		StringBuilder fileName = new StringBuilder();
		fileName.append(dir0).append(File.separator).append(dir1);
		File dir = new File(fileName.toString());
		if (!dir.exists())
			dir.mkdir();
		File[] files = dir.listFiles();

		for (File f : files) {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String s = br.readLine();
			ChipVersion c = JSONObject.parseObject(s, ChipVersion.class);
			chips.add(c);
			br.close();
		}
		return chips;
	}

	public static void main(String[] args) throws Exception {
		ChipVersion c = JSONObject.parseObject("{curVer:'怒发冲冠',minVer:'满江红'}", ChipVersion.class);
		User user = new User();
		user.setName("李");
		user.setPwd("lchj");
		System.out.println(c.getCurVer());
		System.out.println(MiscUtils.writeJSON(c, "user", "curVer"));

		StringBuilder sb = new StringBuilder("lchj");
		sb.delete(0, sb.length());
		System.out.println(sb.toString());
		String dir0 = Object.class.getResource("/").getPath();
		System.out.println(dir0);
	}

}
