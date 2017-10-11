package com.sinosoft.zcfz.entity;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.Embeddable;

@Embeddable
public class Alm_ResultId implements Serializable {

	private String reportId;
	
	private String itemCode;
	
	public Alm_ResultId() {
		
	}
	public Alm_ResultId(String reportId, String itemCode) {
		this.setReportId(reportId);
		this.setItemCode(itemCode);
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getItemCode() == null ? 0 : this.getItemCode().hashCode());
		result = 37 * result
				+ (getReportId() == null ? 0 : this.getReportId().hashCode());
		return result;
	}
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof Alm_ResultId))
			return false;
		Alm_ResultId castOther = (Alm_ResultId) other;

		return ((this.getItemCode() == castOther.getItemCode()) || (this
				.getItemCode() != null && castOther.getItemCode() != null && this
				.getItemCode().equals(castOther.getItemCode())))
				&& ((this.getReportId() == castOther.getReportId()) || (this
						.getReportId() != null && castOther.getReportId() != null && this
						.getReportId().equals(castOther.getReportId())));
	}


	
}