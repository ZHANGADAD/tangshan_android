package com.tangshan.gui.bean;

import java.io.Serializable;

public class MCityInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String sName, sNum;

	private String id, content, tianqiYubao;

	private String category, feng, tianqi, jiangshui;

	public String getId() {
		return id;
	}

	public String getTianqi() {
		return tianqi;
	}

	public void setTianqi(String tianqi) {
		this.tianqi = tianqi;
	}

	public String getJiangshui() {
		return jiangshui;
	}

	public void setJiangshui(String jiangshui) {
		this.jiangshui = jiangshui;
	}

	public String getFeng() {
		return feng;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getsName() {
		return sName;
	}

	public void setsName(String sName) {
		this.sName = sName;
	}

	public String getsNum() {
		return sNum;
	}

	public void setsNum(String sNum) {
		this.sNum = sNum;
	}

	public String getCategory() {
		// TODO Auto-generated method stub
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setFeng(String feng) {
		// TODO Auto-generated method stub
		this.feng = feng;
	}

	public void setTianqiYubao(String tianqiYubao) {
		// TODO Auto-generated method stub
		this.tianqiYubao = tianqiYubao;
	}

	public String getTianqiYubao() {
		return tianqiYubao;
	}

}
