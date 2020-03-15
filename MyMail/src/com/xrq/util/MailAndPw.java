package com.xrq.util;

public class MailAndPw {

	private String email;
	private String pw;
	private String shoujianren;
	private String fajianren;
	private String zhuti;
	private String neirong;
	public MailAndPw(String email,String pw,String s,String f,String z,String n){
		this.email=email;
		this.pw=pw;
		shoujianren=s;
		fajianren=f;
		zhuti=z;
		neirong=n;
	}
	public String getEmail(){
		return email;
	}
	public String getPw(){
		return pw;
	}
	public String getShoujianren(){
		return shoujianren;
	}
	public String getFajianren(){
		return fajianren;
	}
	public String getZhuti(){
		return zhuti;
	}
	public String getNeirong(){
		return neirong;
	}
}
