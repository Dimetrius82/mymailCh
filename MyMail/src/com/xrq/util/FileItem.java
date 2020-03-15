package com.xrq.util;

public class FileItem {

	private String filename;
	private String filesize;
	private int imageid;
	public FileItem(String fn,String fs,int ii){
		 filename=fn;
		 filesize=fs;
		 imageid=ii;
	}
	public String getFN(){
		return filename;
	}
}
