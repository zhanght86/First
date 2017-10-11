package com.sinosoft.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class LaCfReportDataId implements java.io.Serializable {

	private String outItemCode;
	private String reportId;
	
	@Column(name = "OutItemCode", nullable = false, length = 20)
	public String getOutItemCode() {
		return this.outItemCode;
	}

	public void setOutItemCode(String outItemCode) {
		this.outItemCode = outItemCode;
	}

	@Column(name = "ReportId", nullable = false, length = 50)
	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

}
