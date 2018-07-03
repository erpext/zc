package com.zc.erpext.entity;

public class SysUser {
  private Long id;
  private String user_no;
  private String user_name;
  private String user_password;
  private String user_flag;
  private String user_level;
  private Long employee_id;
  private String user_note;
  private java.sql.Timestamp last_date;
  private java.sql.Timestamp first_date;
  private Long company_id;
  private String create_user_no;
  private java.sql.Timestamp create_datetime;
  private String update_user_no;
  private java.sql.Timestamp update_datetime;
  private String customer_alert;
  private String wx_open_id_zc;
  private java.sql.Timestamp wx_open_id_datetime;

  public String getWx_bind_password() {
    return wx_bind_password;
  }

  public void setWx_bind_password(String wx_bind_password) {
    this.wx_bind_password = wx_bind_password;
  }

  private String wx_bind_password;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUser_no() {
    return user_no;
  }

  public void setUser_no(String user_no) {
    this.user_no = user_no;
  }

  public String getUser_name() {
    return user_name;
  }

  public void setUser_name(String user_name) {
    this.user_name = user_name;
  }

  public String getUser_password() {
    return user_password;
  }

  public void setUser_password(String user_password) {
    this.user_password = user_password;
  }

  public String getUser_flag() {
    return user_flag;
  }

  public void setUser_flag(String user_flag) {
    this.user_flag = user_flag;
  }

  public String getUser_level() {
    return user_level;
  }

  public void setUser_level(String user_level) {
    this.user_level = user_level;
  }

  public Long getEmployee_id() {
    return employee_id;
  }

  public void setEmployee_id(Long employee_id) {
    this.employee_id = employee_id;
  }

  public String getUser_note() {
    return user_note;
  }

  public void setUser_note(String user_note) {
    this.user_note = user_note;
  }

  public java.sql.Timestamp getLast_date() {
    return last_date;
  }

  public void setLast_date(java.sql.Timestamp last_date) {
    this.last_date = last_date;
  }

  public java.sql.Timestamp getFirst_date() {
    return first_date;
  }

  public void setFirst_date(java.sql.Timestamp first_date) {
    this.first_date = first_date;
  }

  public Long getCompany_id() {
    return company_id;
  }

  public void setCompany_id(Long company_id) {
    this.company_id = company_id;
  }

  public String getCreate_user_no() {
    return create_user_no;
  }

  public void setCreate_user_no(String create_user_no) {
    this.create_user_no = create_user_no;
  }

  public java.sql.Timestamp getCreate_datetime() {
    return create_datetime;
  }

  public void setCreate_datetime(java.sql.Timestamp create_datetime) {
    this.create_datetime = create_datetime;
  }

  public String getUpdate_user_no() {
    return update_user_no;
  }

  public void setUpdate_user_no(String update_user_no) {
    this.update_user_no = update_user_no;
  }

  public java.sql.Timestamp getUpdate_datetime() {
    return update_datetime;
  }

  public void setUpdate_datetime(java.sql.Timestamp update_datetime) {
    this.update_datetime = update_datetime;
  }

  public String getCustomer_alert() {
    return customer_alert;
  }

  public void setCustomer_alert(String customer_alert) {
    this.customer_alert = customer_alert;
  }

  public String getWx_open_id_zc() {
    return wx_open_id_zc;
  }

  public void setWx_open_id_zc(String wx_open_id_zc) {
    this.wx_open_id_zc = wx_open_id_zc;
  }

  public java.sql.Timestamp getWx_open_id_datetime() {
    return wx_open_id_datetime;
  }

  public void setWx_open_id_datetime(java.sql.Timestamp wx_open_id_datetime) {
    this.wx_open_id_datetime = wx_open_id_datetime;
  }
}
