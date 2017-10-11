package com.sinosoft.entity;


import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.sinosoft.common.BeanFactory;
import com.sinosoft.util.Assert;
import com.sinosoft.util.EncryptService;


/**
 */
@Entity
public class UserInfo implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String INIT_PASSWORD="12345678";
    // Fields    
	 @Id
	 @GeneratedValue(strategy=GenerationType.AUTO)
	 @Column(name="id")
	 private long id;
	 
	 @Column(nullable=false,unique=true)
     private String userCode;
	 @Column(name="userName")
     private String userName;
	 @Column
     private String labCode;
	 @Column
     private String comCode;
	 @Column
     private String deptCode;
	 @Column
     private String password;
	 @Column
     private String idCardNo;
     @Column
     private String email;
     @Column
     private String phone;
     @Column
     private String bankCode;
     @Column
     private String accountCode;
     @Column
     private String bankInfo;
     @Column
     private String useFlag;
     @Column
     private String salt;
     
     
     @OneToMany(mappedBy = "userInfo",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
     private Set<UserRole> userRole= new HashSet<UserRole>();
     
     public UserInfo() {}
     
     public UserInfo(int id) {
    	 this.id=id;
     }
     
     
	public UserInfo(String userCode, String password) {
		checkUserAccount(userCode);
		this.salt = UUID.randomUUID().toString();
		this.password = encryptPassword(INIT_PASSWORD);
		this.userCode = userCode;
	}
	protected static EncryptService passwordEncryptService;

    protected static EncryptService getPasswordEncryptService() {
        if (passwordEncryptService == null) {
            passwordEncryptService = (EncryptService) BeanFactory.getBean("encryptService");
        }
        return passwordEncryptService;
    }

    protected static void setPasswordEncryptService(EncryptService passwordEncryptService) {
        UserInfo.passwordEncryptService = passwordEncryptService;
    }

    protected String encryptPassword(String password) {
        return getPasswordEncryptService().encryptPassword(password, salt+userCode);
    }
	private void checkUserAccount(String userAccount) {
        Assert.notBlank(userAccount, "用户代码不能为空.");
    }
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getLabCode() {
		return labCode;
	}
	public void setLabCode(String labCode) {
		this.labCode = labCode;
	}
	public String getComCode() {
		return comCode;
	}
	public void setComCode(String comCode) {
		this.comCode = comCode;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getIdCardNo() {
		return idCardNo;
	}
	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getAccountCode() {
		return accountCode;
	}
	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}
	public String getBankInfo() {
		return bankInfo;
	}
	public void setBankInfo(String bankInfo) {
		this.bankInfo = bankInfo;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}

	public Set<UserRole> getUserRole() {
		return userRole;
	}

	public void setUserRole(Set<UserRole> userRole) {
		this.userRole = userRole;
	}

	public String getUseFlag() {
		return useFlag;
	}

	public void setUseFlag(String useFlag) {
		this.useFlag = useFlag;
	}

	
}
