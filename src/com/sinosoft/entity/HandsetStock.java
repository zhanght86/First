package com.sinosoft.entity;

import java.math.BigDecimal;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Handset_Stock")
public class HandsetStock {
	@EmbeddedId
	@AttributeOverrides({
	    	@AttributeOverride(name = "exchangemarket", column = @Column(name = "exchangemarket", nullable = false, length = 20)),
		    @AttributeOverride(name = "stockcode", column = @Column(name = "stockcode", nullable = false, length = 20)),
			@AttributeOverride(name = "stockname", column = @Column(name = "stockname", nullable = false, length = 20)),
	    	})
	private HandsetStock_Id id;
	
	@Column
	private String marketboard;
	@Column
	private BigDecimal totalmarketvalue;
	@Column
	private String hisflag;
	@Column
	private String remark;
	public HandsetStock_Id getId() {
		return id;
	}
	public void setId(HandsetStock_Id id) {
		this.id = id;
	}
	public String getMarketboard() {
		return marketboard;
	}
	public void setMarketboard(String marketboard) {
		this.marketboard = marketboard;
	}
	public BigDecimal getTotalmarketvalue() {
		return totalmarketvalue;
	}
	public void setTotalmarketvalue(BigDecimal totalmarketvalue) {
		this.totalmarketvalue = totalmarketvalue;
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
