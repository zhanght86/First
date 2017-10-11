package com.sinosoft.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="cux_sino_interface_all")
public class Cux_Sino_Interface_All {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "interface_id", unique = true, nullable = false)
	private    int          interface_id;      
	@Column(nullable = false)
	private    String       interface_type;    
	@Column(nullable = false)
	private    int          sets_of_book_id;   
	@Column(nullable = false)
	private    String       sets_of_book_name; 
	@Column
	private    String       company_code;      
	@Column
	private    String       company_name;      
	@Column
	private    String       account_code;      
	@Column
	private    String       account_name;      
	@Column
	private    String       dept_code;         
	@Column
	private    String       dept_name;         
	@Column
	private    String       subacc_code;      
	@Column
	private    String       subacc_name;       
	@Column
	private    String       product_code;      
	@Column
	private    String       product_name;      
	@Column
	private    String       business_code;    
	@Column
	private    String       business_name;     
	@Column
	private    String       inter_code;        
	@Column
	private    String       inter_name;        
	@Column
	private    String       backup_code;       
	@Column
	private    String       backup_name;       
	@Column
	private    String       period;           
	@Column
	private    int          period_balance;    
	@Column
	private    String       fa_type;          
	@Column
	private    Date         fa_get_date;       
	@Column
	private    String       fa_location_p;     
	@Column
	private    String       fa_location_c;    
	@Column
	private    String       fa_status;         
	@Column
	private    String       fa_get_methed;     
	@Column
	private    int          fa_cost_value;   
	@Column
	private    int          fa_deprn_amount;   
	@Column
	private    int          fa_book_value;     
	@Column
	private    int          fa_ready;         
	@Column
	private    int          fa_reco_value;   
	@Column
	private    Date         fa_latest_date;    
	@Column
	private    int          fa_latest_value;   
	@Column
	private    int          fa_balance_value;  
	@Column
	private    String       attirbute1;        
	@Column
	private    String       attirbute2;        
	@Column
	private    String       attirbute3;       
	@Column
	private    String       attirbute4;       
	@Column
	private    String       attirbute5;        
	@Column(nullable = false)
	private    int          created_by;       
	@Column(nullable = false)
	private    Date         creation_date;    
	@Column(nullable = false)
	private    int          last_updated_by;  
	@Column(nullable = false)
	private    Date         last_update_date;  
	@Column
	private    int          last_update_login; 
	
	public int getInterface_id() {
		return interface_id;
	}
	public void setInterface_id(int interface_id) {
		this.interface_id = interface_id;
	}
	public String getInterface_type() {
		return interface_type;
	}
	public void setInterface_type(String interface_type) {
		this.interface_type = interface_type;
	}
	public int getSets_of_book_id() {
		return sets_of_book_id;
	}
	public void setSets_of_book_id(int sets_of_book_id) {
		this.sets_of_book_id = sets_of_book_id;
	}
	public String getSets_of_book_name() {
		return sets_of_book_name;
	}
	public void setSets_of_book_name(String sets_of_book_name) {
		this.sets_of_book_name = sets_of_book_name;
	}
	public String getCompany_code() {
		return company_code;
	}
	public void setCompany_code(String company_code) {
		this.company_code = company_code;
	}
	public String getCompany_name() {
		return company_name;
	}
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
	public String getAccount_code() {
		return account_code;
	}
	public void setAccount_code(String account_code) {
		this.account_code = account_code;
	}
	public String getAccount_name() {
		return account_name;
	}
	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}
	public String getDept_code() {
		return dept_code;
	}
	public void setDept_code(String dept_code) {
		this.dept_code = dept_code;
	}
	public String getDept_name() {
		return dept_name;
	}
	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}
	public String getSubacc_code() {
		return subacc_code;
	}
	public void setSubacc_code(String subacc_code) {
		this.subacc_code = subacc_code;
	}
	public String getSubacc_name() {
		return subacc_name;
	}
	public void setSubacc_name(String subacc_name) {
		this.subacc_name = subacc_name;
	}
	public String getProduct_code() {
		return product_code;
	}
	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public String getBusiness_code() {
		return business_code;
	}
	public void setBusiness_code(String business_code) {
		this.business_code = business_code;
	}
	public String getBusiness_name() {
		return business_name;
	}
	public void setBusiness_name(String business_name) {
		this.business_name = business_name;
	}
	public String getInter_code() {
		return inter_code;
	}
	public void setInter_code(String inter_code) {
		this.inter_code = inter_code;
	}
	public String getInter_name() {
		return inter_name;
	}
	public void setInter_name(String inter_name) {
		this.inter_name = inter_name;
	}
	public String getBackup_code() {
		return backup_code;
	}
	public void setBackup_code(String backup_code) {
		this.backup_code = backup_code;
	}
	public String getBackup_name() {
		return backup_name;
	}
	public void setBackup_name(String backup_name) {
		this.backup_name = backup_name;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public int getPeriod_balance() {
		return period_balance;
	}
	public void setPeriod_balance(int period_balance) {
		this.period_balance = period_balance;
	}
	public String getFa_type() {
		return fa_type;
	}
	public void setFa_type(String fa_type) {
		this.fa_type = fa_type;
	}
	public Date getFa_get_date() {
		return fa_get_date;
	}
	public void setFa_get_date(Date fa_get_date) {
		this.fa_get_date = fa_get_date;
	}
	public String getFa_location_p() {
		return fa_location_p;
	}
	public void setFa_location_p(String fa_location_p) {
		this.fa_location_p = fa_location_p;
	}
	public String getFa_location_c() {
		return fa_location_c;
	}
	public void setFa_location_c(String fa_location_c) {
		this.fa_location_c = fa_location_c;
	}
	public String getFa_status() {
		return fa_status;
	}
	public void setFa_status(String fa_status) {
		this.fa_status = fa_status;
	}
	public String getFa_get_methed() {
		return fa_get_methed;
	}
	public void setFa_get_methed(String fa_get_methed) {
		this.fa_get_methed = fa_get_methed;
	}
	public int getFa_cost_value() {
		return fa_cost_value;
	}
	public void setFa_cost_value(int fa_cost_value) {
		this.fa_cost_value = fa_cost_value;
	}
	public int getFa_deprn_amount() {
		return fa_deprn_amount;
	}
	public void setFa_deprn_amount(int fa_deprn_amount) {
		this.fa_deprn_amount = fa_deprn_amount;
	}
	public int getFa_book_value() {
		return fa_book_value;
	}
	public void setFa_book_value(int fa_book_value) {
		this.fa_book_value = fa_book_value;
	}
	public int getFa_ready() {
		return fa_ready;
	}
	public void setFa_ready(int fa_ready) {
		this.fa_ready = fa_ready;
	}
	public int getFa_reco_value() {
		return fa_reco_value;
	}
	public void setFa_reco_value(int fa_reco_value) {
		this.fa_reco_value = fa_reco_value;
	}
	public Date getFa_latest_date() {
		return fa_latest_date;
	}
	public void setFa_latest_date(Date fa_latest_date) {
		this.fa_latest_date = fa_latest_date;
	}
	public int getFa_latest_value() {
		return fa_latest_value;
	}
	public void setFa_latest_value(int fa_latest_value) {
		this.fa_latest_value = fa_latest_value;
	}
	public int getFa_balance_value() {
		return fa_balance_value;
	}
	public void setFa_balance_value(int fa_balance_value) {
		this.fa_balance_value = fa_balance_value;
	}
	public String getAttirbute1() {
		return attirbute1;
	}
	public void setAttirbute1(String attirbute1) {
		this.attirbute1 = attirbute1;
	}
	public String getAttirbute2() {
		return attirbute2;
	}
	public void setAttirbute2(String attirbute2) {
		this.attirbute2 = attirbute2;
	}
	public String getAttirbute3() {
		return attirbute3;
	}
	public void setAttirbute3(String attirbute3) {
		this.attirbute3 = attirbute3;
	}
	public String getAttirbute4() {
		return attirbute4;
	}
	public void setAttirbute4(String attirbute4) {
		this.attirbute4 = attirbute4;
	}
	public String getAttirbute5() {
		return attirbute5;
	}
	public void setAttirbute5(String attirbute5) {
		this.attirbute5 = attirbute5;
	}
	public int getCreated_by() {
		return created_by;
	}
	public void setCreated_by(int created_by) {
		this.created_by = created_by;
	}
	public Date getCreation_date() {
		return creation_date;
	}
	public void setCreation_date(Date creation_date) {
		this.creation_date = creation_date;
	}
	public int getLast_updated_by() {
		return last_updated_by;
	}
	public void setLast_updated_by(int last_updated_by) {
		this.last_updated_by = last_updated_by;
	}
	public Date getLast_update_date() {
		return last_update_date;
	}
	public void setLast_update_date(Date last_update_date) {
		this.last_update_date = last_update_date;
	}
	public int getLast_update_login() {
		return last_update_login;
	}
	public void setLast_update_login(int last_update_login) {
		this.last_update_login = last_update_login;
	}
	
}