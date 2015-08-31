package org.blackcrystalinfo.backstage.bo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

public class ChipVersion {
	@NotNull
	private MultipartFile file;
	
	@Pattern(regexp = "^\\w+_\\d+_\\d{3}_IMG_\\d{4}$", message = "{file.fmt.error}")
	private String fileName;
	private String curVer;
	// @Pattern(regexp = "^\\d{1,2}\\.\\d{1,2}\\.\\d{1,2}\\.\\d{1,2}$", message = "{minver.fmt.error}")
	@NotEmpty(message="{minVer.notempty}")
	@Pattern(regexp = "^\\d{4}$", message = "{minver.fmt.error}")
	private String minVer;
	private String url;
	private String md5;
	private String chipType;
	private String deviceType;

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getChipType() {
		return chipType;
	}

	public void setChipType(String chipType) {
		this.chipType = chipType;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public String getCurVer() {
		return curVer;
	}

	public void setCurVer(String curVer) {
		this.curVer = curVer;
	}

	public String getMinVer() {
		return minVer;
	}

	public void setMinVer(String minVer) {
		this.minVer = minVer;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public void gen(){
		try {
			String[] fn = file.getOriginalFilename().split("_");
			this.curVer = fn[4];
			this.chipType = fn[1];
			this.fileName = file.getOriginalFilename();
			System.out.println(this.fileName);
		} catch (Exception e) {
		}
	}
}
