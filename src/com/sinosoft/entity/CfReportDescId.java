package com.sinosoft.entity;

// Generated 2016-1-13 19:05:16 by Hibernate Tools 4.3.1

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * CfReportDescId generated by hbm2java
 */
@Embeddable
public class CfReportDescId implements java.io.Serializable {

	private String reportCode;
	private String ruleNature;
	private String reportMode;

	public CfReportDescId() {
	}

	public CfReportDescId(String reportCode, String ruleNature,
			String reportMode) {
		this.reportCode = reportCode;
		this.ruleNature = ruleNature;
		this.reportMode = reportMode;
	}

	@Column(name = "ReportCode", nullable = false, length = 20)
	public String getReportCode() {
		return this.reportCode;
	}

	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}

	@Column(name = "RuleNature", nullable = false, length = 1)
	public String getRuleNature() {
		return this.ruleNature;
	}

	public void setRuleNature(String ruleNature) {
		this.ruleNature = ruleNature;
	}

	@Column(name = "ReportMode", nullable = false, length = 1)
	public String getReportMode() {
		return this.reportMode;
	}

	public void setReportMode(String reportMode) {
		this.reportMode = reportMode;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof CfReportDescId))
			return false;
		CfReportDescId castOther = (CfReportDescId) other;

		return ((this.getReportCode() == castOther.getReportCode()) || (this
				.getReportCode() != null && castOther.getReportCode() != null && this
				.getReportCode().equals(castOther.getReportCode())))
				&& ((this.getRuleNature() == castOther.getRuleNature()) || (this
						.getRuleNature() != null
						&& castOther.getRuleNature() != null && this
						.getRuleNature().equals(castOther.getRuleNature())))
				&& ((this.getReportMode() == castOther.getReportMode()) || (this
						.getReportMode() != null
						&& castOther.getReportMode() != null && this
						.getReportMode().equals(castOther.getReportMode())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getReportCode() == null ? 0 : this.getReportCode()
						.hashCode());
		result = 37
				* result
				+ (getRuleNature() == null ? 0 : this.getRuleNature()
						.hashCode());
		result = 37
				* result
				+ (getReportMode() == null ? 0 : this.getReportMode()
						.hashCode());
		return result;
	}

}
