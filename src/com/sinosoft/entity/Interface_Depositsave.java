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
@Table(name = "Interface_Depositsave")
public class Interface_Depositsave {
	@EmbeddedId
	@AttributeOverrides({
		    @AttributeOverride(name = "bankName", column = @Column(name = "bankName", nullable = false, length = 50)),
		    @AttributeOverride(name = "bankAccount", column = @Column(name = "bankAccount", nullable = false, length = 40)),
			@AttributeOverride(name = "accountCode", column = @Column(name = "accountCode", nullable = true, length = 30)),
            @AttributeOverride(name = "amount", column = @Column(name = "amount", nullable = true, length = 19)),
            @AttributeOverride(name = "saveType", column = @Column(name = "saveType", nullable = true, length = 40))})
	private Interface_DepositsaveId id;
	@Column
	private BigDecimal interest;
	@Column
	private Date startDate;
	@Column
	private String period;
	@Column
	private BigDecimal dailyInterest;
	@Column
	private String year;
	@Column
	private String month;
	@Column
	private String yearMonth;
	@Column
	private Date dateTime;

	public Interface_DepositsaveId getId() {
		return id;
	}

	public void setId(Interface_DepositsaveId id) {
		this.id = id;
	}

	public BigDecimal getInterest() {
		return interest;
	}

	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public BigDecimal getDailyInterest() {
		return dailyInterest;
	}

	public void setDailyInterest(BigDecimal dailyInterest) {
		this.dailyInterest = dailyInterest;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYearMonth() {
		return yearMonth;
	}

	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

}
