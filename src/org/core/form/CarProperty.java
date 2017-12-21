package org.core.form;

import java.io.Serializable;

public class CarProperty implements Serializable{

	private static final long serialVersionUID = 1L;

	private String  serverIp;		
	private int m_err;
	private boolean nRunning;
	
	private String execClass;
	private String view;//第一块屏显示，第二块屏显示的是车牌号
	private String voice;//语音播报内容，从数据库里拉
	private String cacrno;
	private String shootTime;
	
	private Boolean isLift;//是否满足条件来抬杆

	
	public CarProperty() {
		super();
	}



	public String getServerIp() {
		return serverIp;
	}



	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public int getM_err() {
		return m_err;
	}



	public void setM_err(int m_err) {
		this.m_err = m_err;
	}


	public void setnRunning(boolean nRunning) {
		this.nRunning = nRunning;
	}



	public boolean isnRunning() {
		return nRunning;
	}



	public String getExecClass() {
		return execClass;
	}



	public void setExecClass(String execClass) {
		this.execClass = execClass;
	}


	public String getView() {
		return view;
	}



	public void setView(String view) {
		this.view = view;
	}



	public String getVoice() {
		return voice;
	}



	public void setVoice(String voice) {
		this.voice = voice;
	}



	public String getCacrno() {
		return cacrno;
	}



	public void setCacrno(String cacrno) {
		this.cacrno = cacrno;
	}



	public String getShootTime() {
		return shootTime;
	}



	public void setShootTime(String shootTime) {
		this.shootTime = shootTime;
	}



	public Boolean getIsLift() {
		return isLift;
	}



	public void setIsLift(Boolean isLift) {
		this.isLift = isLift;
	}

}
