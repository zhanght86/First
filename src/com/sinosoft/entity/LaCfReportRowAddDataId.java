package com.sinosoft.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class LaCfReportRowAddDataId implements java.io.Serializable{

	private String colCode;
	private String rowNo;
	private String reportId;

	@Column(name = "ColCode", nullable = false, length = 20)
	public String getColCode() {
		return this.colCode;
	}

	public void setColCode(String colCode) {
		this.colCode = colCode;
	}

	@Column(name = "RowNo", nullable = false, length = 8)
	public String getRowNo() {
		return this.rowNo;
	}

	public void setRowNo(String rowNo) {
		this.rowNo = rowNo;
	}

	@Column(name = "ReportId", nullable = false, length = 50)
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

}
