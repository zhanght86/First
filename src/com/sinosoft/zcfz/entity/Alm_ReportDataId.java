package com.sinosoft.zcfz.entity;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Alm_ReportDataId implements Serializable {

	private String reportId;
	
	private String itemCode;
	
    private String deptNo;
	
	public Alm_ReportDataId() {
		
	}
	public Alm_ReportDataId(String reportId, String itemCode,String deptNo) {
		this.setReportId(reportId);
		this.setItemCode(itemCode);
		this.setDeptNo(deptNo);
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
		result = 37 * result
				+ (getDeptNo() == null ? 0 : this.getDeptNo().hashCode());
		return result;
	}
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof Alm_ReportDataId))
			return false;
		Alm_ReportDataId castOther = (Alm_ReportDataId) other;

		return ((this.getItemCode() == castOther.getItemCode()) || (this
				.getItemCode() != null && castOther.getItemCode() != null && this
				.getItemCode().equals(castOther.getItemCode())))
				&& ((this.getReportId() == castOther.getReportId()) || (this
						.getReportId() != null && castOther.getReportId() != null && this
						.getReportId().equals(castOther.getReportId())))&&((this.getDeptNo() == castOther.getDeptNo()) || (this
								.getDeptNo() != null && castOther.getDeptNo() != null && this
								.getDeptNo().equals(castOther.getDeptNo())));
	}
	public String getDeptNo() {
		return deptNo;
	}
	public void setDeptNo(String deptNo) {
		this.deptNo = deptNo;
	}


	
}