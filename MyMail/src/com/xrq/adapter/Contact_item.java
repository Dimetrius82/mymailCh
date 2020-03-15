package com.xrq.adapter;

public class Contact_item {
  private String user;
  private String email;
  private int imageid;
  public Contact_item(String user,String email,int imageid){
	  this.user=user;
	  this.email=email;
	  this.imageid=imageid;
  }
  public void setUser(String u){
	  user=u;
  }
  public void setimage(int id){
	  imageid=id;
  }
  public void setEmail(String e){
	  email=e;
  }
  public String getUser(){
	  return user;
	  
  }
  public int getImage(){
	  return imageid;
  }
  public String getEmail(){
	  return email;
	  
  }
}
