package com.sinosoft.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Handset_Fund")
public class HandsetFund {
	@EmbeddedId
	@AttributeOverrides({
		    @AttributeOverride(name = "fundcode", column = @Column(name = "fundcode", nullable = false, length = 20)),
			@AttributeOverride(name = "fundname", column = @Column(name = "fundname", nullable = false, length = 20)),
	    	})
	private HandsetFund_Id id;
	@Column
	private String fundsponsor;
	@Column
	private String firstfundtype;
	@Column
	private String gradingfund;
	@Column
	private String gradingfundtype;
	@Column
	private String hisflag;
	@Column
	private String remark;

	public HandsetFund_Id getId() {
		return id;
	}
	public void setId(HandsetFund_Id id) {
		this.id = id;
	}
	public String getFundsponsor() {
		return fundsponsor;
	}
	public void setFundsponsor(String fundsponsor) {
		this.fundsponsor = fundsponsor;
	}
	public String getFirstfundtype() {
		return firstfundtype;
	}
	public void setFirstfundtype(String firstfundtype) {
		this.firstfundtype = firstfundtype;
	}
	public String getGradingfund() {
		return gradingfund;
	}
	public void setGradingfund(String gradingfund) {
		this.gradingfund = gradingfund;
	}
	public String getGradingfundtype() {
		return gradingfundtype;
	}
	public void setGradingfundtype(String gradingfundtype) {
		this.gradingfundtype = gradingfundtype;
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
