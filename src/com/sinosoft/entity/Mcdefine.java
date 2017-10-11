package com.sinosoft.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cal_MCDefine")
public class Mcdefine {

	/**  
	 */
	@Id
	private String mcCode;
	/**  
	 */
	@Column
	private String risktype1Code;
	/**  
	 */
	@Column
	private String risktype1Name;
	/**  
	 */
	@Column
	private String risktype2Code;
	/**  
	 */
	@Column
	private String risktype2Name;
	/**  
	 */
	@Column
	private String risktype3Code;
	/**  
	 */
	@Column
	private String risktype3Name;
	/**  
	 */
	@Column
	private String mcDefine;

	public String getMcCode() {
		return mcCode;
	}

	public void setMcCode(String mcCode) {
		this.mcCode = mcCode;
	}

	public String getRisktype1Code() {
		return risktype1Code;
	}

	public void setRisktype1Code(String risktype1Code) {
		this.risktype1Code = risktype1Code;
	}

	public String getRisktype1Name() {
		return risktype1Name;
	}

	public void setRisktype1Name(String risktype1Name) {
		this.risktype1Name = risktype1Name;
	}

	public String getRisktype2Code() {
		return risktype2Code;
	}

	public void setRisktype2Code(String risktype2Code) {
		this.risktype2Code = risktype2Code;
	}

	public String getRisktype2Name() {
		return risktype2Name;
	}

	public void setRisktype2Name(String risktype2Name) {
		this.risktype2Name = risktype2Name;
	}

	public String getRisktype3Code() {
		return risktype3Code;
	}

	public void setRisktype3Code(String risktype3Code) {
		this.risktype3Code = risktype3Code;
	}

	public String getRisktype3Name() {
		return risktype3Name;
	}

	public void setRisktype3Name(String risktype3Name) {
		this.risktype3Name = risktype3Name;
	}

	public String getMcDefine() {
		return mcDefine;
	}

	public void setMcDefine(String mcDefine) {
		this.mcDefine = mcDefine;
	}

}