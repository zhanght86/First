package com.sinosoft.entity;

// Generated 2016-1-13 19:05:16 by Hibernate Tools 4.3.1

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * CfReportDataId generated by hbm2java
 */
@Embeddable
public class CfReportDataId implements java.io.Serializable {
	private String reportId;
	private String outItemCode;

	public CfReportDataId() {
	}

	public CfReportDataId(String outItemCode, String reportId) {
		this.reportId = reportId;
		this.outItemCode = outItemCode;
	}

	@Column(name = "OutItemCode", nullable = false, length = 20)
	public String getOutItemCode() {
		return this.outItemCode;
	}

	public void setOutItemCode(String outItemCode) {
		this.outItemCode = outItemCode;
	}

	@Column(name = "reportId", nullable = false, length = 255)
	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof CfReportDataId))
			return false;
		CfReportDataId castOther = (CfReportDataId) other;

		return ((this.getOutItemCode() == castOther.getOutItemCode()) || (this
				.getOutItemCode() != null && castOther.getOutItemCode() != null && this
				.getOutItemCode().equals(castOther.getOutItemCode())))
				&& ((this.getReportId() == castOther.getReportId()) || (this
						.getReportId() != null
						&& castOther.getReportId() != null && this
						.getReportId().equals(castOther.getReportId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getOutItemCode() == null ? 0 : this.getOutItemCode()
						.hashCode());
		result = 37
				* result
				+ (getReportId() == null ? 0 : this.getReportId()
						.hashCode());
		return result;
	}

}
