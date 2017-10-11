package com.sinosoft.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Handset_Security")
public class HandsetSecurity {
	@EmbeddedId
	@AttributeOverrides({
	    	@AttributeOverride(name = "exchangemarket", column = @Column(name = "exchangemarket", nullable = false, length = 20)),
		    @AttributeOverride(name = "securitycode", column = @Column(name = "securitycode", nullable = false, length = 20)),
			@AttributeOverride(name = "securityname", column = @Column(name = "securityname", nullable = false, length = 20)),
	    	})
	private HandsetSecurity_Id id;
	@Column
	private String creditlevel;
	@Column
	private BigDecimal duration;
	@Column
	private Date intereststart;
	@Column
	private Date interestend;
	@Column
	private String hisflag;
	@Column
	private String remark;
	public HandsetSecurity_Id getId() {
		return id;
	}
	public void setId(HandsetSecurity_Id id) {
		this.id = id;
	}
	public String getCreditlevel() {
		return creditlevel;
	}
	public void setCreditlevel(String creditlevel) {
		this.creditlevel = creditlevel;
	}
	public BigDecimal getDuration() {
		return duration;
	}
	public void setDuration(BigDecimal duration) {
		this.duration = duration;
	}
	public Date getIntereststart() {
		return intereststart;
	}
	public void setIntereststart(Date intereststart) {
		this.intereststart = intereststart;
	}
	public Date getInterestend() {
		return interestend;
	}
	public void setInterestend(Date interestend) {
		this.interestend = interestend;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getHisflag() {
		return hisflag;
	}
	public void setHisflag(String hisflag) {
		this.hisflag = hisflag;
	}
	
}
