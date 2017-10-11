package com.sinosoft.zcfz.service.reportdatacheck.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sinosoft.common.CurrentUser;
import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dao.reportdatacheck.DataCheckDao;
import com.sinosoft.zcfz.dto.reportdatacheck.DataCheckDto;
import com.sinosoft.entity.CfRiskRatingCheck;
import com.sinosoft.entity.CfRiskRatingErrInfo;
import com.sinosoft.entity.UserInfo;
import com.sinosoft.zcfz.service.reportdatacheck.RiskRatingCheckService;

/**
 * 风险综合评级检验
 *
 * @author liucw 2016年9月5日
 */
@Service("RiskRatingCheckService")
public class RiskRatingCheckServiceImpl implements RiskRatingCheckService{
	@Resource
	public DataCheckDao dataCheckDao;
	private static final String ERR_LEVEL = "error";
	private static final String WARN_LEVEL = "warn";
	private DecimalFormat decimalFormat = new DecimalFormat("0.0000"); // 数字转换对象
	private String isNeedChk = ""; // 校验公式标识：1、季度快报； 2、季度报告； 3、临时报告
	private String quarter = "";  //季度
	private String month = ""; // 年度 
	private String reportRate = ""; // 报送频度:1、季度快报； 2、季度报告； 3、临时报告


	private String companyType = com.sinosoft.util.Config.getProperty("CompanyType");;// 校验公司标示：ALL-所有公司、L-寿险公司、P-财险公司、R-再保险公司
	private String comCode ;
	private String companyFlag ; //z为总公司，f为分公司

	private Log log = LogFactory.getLog(RiskRatingCheckServiceImpl.class);
	private Map<String, BigDecimal> HashMap = new HashMap<String, BigDecimal>();// 因子指标数据
	private Map<String, String> decimals_HashMap = new HashMap<String, String>();// 因子指标数据

	@Transactional
	public List<?> getErrorInfo(DataCheckDto dataCheckDto) {
		///		UserInfo userInfo = CurrentUser.getCurrentUser();
		//		comCode = userInfo.getComCode();


		String getRepType = "select codename from cfcodemanage where codetype='reporttype' and codecode= reportType ";
		String comName = "select comName from branchinfo where comcode='"+ dataCheckDto.getComCode()+ "'";

		StringBuilder sql = new StringBuilder();
		sql.append("select ("
				+ getRepType
				+ ") \"reportType\",year \"year\", quarter \"quarter\", ("
				+ comName
				+ " )\"comName\", errLevel \"errLevel\",temp \"temp\",errorInfo \"errorInfo\" , to_char(checkdate,'yyyy-mm-dd') \"checkdate\"  from CfRiskRatingErrInfo  where 1=1 and reportType='2' and comcode='"+ dataCheckDto.getComCode()+ "'");
		
		if (dataCheckDto.getYear() != null
				&& dataCheckDto.getYear().length() > 0) {
			sql.append(" and year='" + dataCheckDto.getYear() + "'");
		}

		if (dataCheckDto.getQuarter() != null
				&& dataCheckDto.getQuarter().length() > 0) {
			sql.append(" and quarter='" + dataCheckDto.getQuarter() + "'");
		}

		return dataCheckDao.queryBysql(sql.toString(), DataCheckDto.class);
	}

	/**
	 * 数据检验具体操作操作
	 * 
	 * @param dataCheckDto
	 *            前端条件参数，包含报告类型、年度、季度
	 */
	@Transactional
	public boolean dataCheckOperator(DataCheckDto dataCheckDto) {
		UserInfo userInfo = CurrentUser.getCurrentUser();
		// comCode = userInfo.getComCode();

		comCode = dataCheckDto.getComCode();

		String sql = "select comRank as value from branchinfo where comcode='"
				+ comCode + "'";
		List<?> comRankList = dataCheckDao.queryBysql(sql);

		String comRank = (String) ((Map) comRankList.get(0)).get("value");

		companyFlag = comRank.equals("1") ? "z" : "f";

		month = dataCheckDto.getYear();
		quarter = dataCheckDto.getQuarter();
		reportRate = "2";
		// 1、季度快报； 2、季度报告； 3、临时报告
		isNeedChk = reportRate;

		// 删除对应的错误信息
		if (!dataCheckDao.deleteRiskRatingErrInfo(dataCheckDto,dataCheckDto.getComCode())) {
			log.error("删除当前条件下的错误信息出错！！！");
			return false;
		}

		// 获取风险评级因子指标数据
		if (!getFactorIndexData()) {
			return false;
		}

		// 检验风险评级
		riskRatingCheck();

		return true;
	}

	private boolean getFactorIndexData() {
		// 每次检验前都清空数据，防止数据存储过大，耗时耗内存
		HashMap.clear();
		// 获取风险评级因子指标数据，并根据精度取值
		List<?> list = dataCheckDao.getRiskRatingDataByDecimals(reportRate,
				month, quarter, comCode);
		if (list == null || list.size() <= 0) {
			log.info("没有当前条件下的的风险评级指标数据！");
			return false;
		}

		// 获取的本期因子指标数据,其中key为 因子代码 ，value为 reportItemValue
		for (int k = 0; k < list.size(); k++) {
			HashMap.put((String) ((Map) list.get(k)).get("outitemcode"),
					(BigDecimal) ((Map) list.get(k)).get("reportitemvalue"));
			decimals_HashMap.put(
					(String) ((Map) list.get(k)).get("outitemcode"),
					(String) ((Map) list.get(k)).get("decimals"));

		}

		/*
		 * // 获取风险评级因子指标数据 List<CfReportData> list =
		 * dataCheckDao.getRiskRatingData(reportRate, month, quarter, comCode);
		 * if (list == null || list.size() <= 0) {
		 * log.info("没有当前条件下的的风险评级指标数据！"); return false; }
		 * 
		 * // 获取的本期因子指标数据,其中key为 因子代码 ，value为 reportItemValue for (int k = 0; k
		 * < list.size(); k++) {
		 * HashMap.put(list.get(k).getId().getOutItemCode(),
		 * list.get(k).getReportItemValue()); }
		 */
		return true;
	}

	/**
	 * 检验风险评级:对应cfRiskRatingCheck
	 */
	private boolean riskRatingCheck() {
		log.info("----------------------开始校验风险综合评估，即cfRiskRatingCheck！---------------------------");
		List<CfRiskRatingCheck> list = dataCheckDao.getRiskRatingChecked(isNeedChk, companyType,companyFlag);
		if (list == null || list.size() <= 0) {
			log.info("提示：当前检验条件下，cfreportdatacheck表里没有检验数据,不进行检验");
			return false;
		}

		for (int i = 0; i < list.size(); i++) {
			// 获取计算公式
			String calculateFormula = list.get(i).getCalFormula();
			if (calculateFormula.contains("3320711_2_00017")) {
				System.out.println(calculateFormula);
			}
			BigDecimal tolerance = list.get(i).getTolerance();  //容差

			// 获取关系运算符
			String relationOperator = getOperator(calculateFormula);
			if (relationOperator == null) {
				log.error("请修改cfRiskRatingCheck表里的检验式： "+ calculateFormula );
				return false;
			}
			//如果连等进入这里
			//计算公式是否为连等
			if (calculateFormula.split("=").length >2) {
				log.info("--------------------------风险综合评估连等检验开始---------------------------");
				// 获取关系运算
				List<Double> list2=new ArrayList<Double>();
				String[] st=calculateFormula.split("=");
				for (int j = 0; j < st.length; j++) {
					//获取值
					double c=getCalResult(st[j]);
					list2.add(c);
				}
				//取第一个值作为标准
				double d=list2.get(0);
				for (int j = 0; j < list2.size(); j++) {
					if (d-list2.get(j)==0) {

					}else {
						// 将错误信息存放到数据库里
						if ( Math.abs(d-list2.get(j)) > tolerance.doubleValue()) {
							log.error("错误：检验风险评级: 请查看cfRiskRatingCheck表里的数据！ "+ calculateFormula);
							PrepareErrLog("检验风险评级数据出错：" , ERR_LEVEL, new BigDecimal(d).toString(),new BigDecimal(list2.get(j)).toString(), d-list2.get(j),
									calculateFormula, list.get(i).getRemark(),tolerance);

							//警告信息
						} else if((Math.abs(d-list2.get(j)) > 0 )&& (Math.abs(d-list2.get(j)) <= tolerance.doubleValue())){
							log.warn("警告：检验风险评级指标数据: 请查看cfRiskRatingCheck表里的数据！ "+ calculateFormula);
							PrepareErrLog("检验风险评级指标数据警告：" ,WARN_LEVEL, new BigDecimal(d).toString(), new BigDecimal(list2.get(j)).toString(), d-list2.get(j),
									calculateFormula, list.get(i).getRemark(),tolerance);
						}



						log.info("--------------------------风险综合评估连等检验结束---------------------------");
						return false;
					}
				}
			}else {
				// 获取公式左边计算公式
				String leftCalculateFormula = calculateFormula.substring(0, calculateFormula.indexOf(relationOperator));
				// 获取左边计算式的值
				double leftValue = getCalResult(leftCalculateFormula.replaceAll(" ", ""));
				int decimals=spiltAndGetOutitemcode(leftCalculateFormula);



				// 获取公式右边和值
				String rightCalculateFormula = calculateFormula.substring(calculateFormula.indexOf(relationOperator) + 1);
				double rightValue = getCalResult(rightCalculateFormula.replaceAll(" ", ""));	

				rightValue = Arith.round(rightValue, decimals);// 四舍五入保留2位小数

				/*			//保留两位小数计算
			String tLeftValue = decimalFormat.format(leftValue);
			String tRightValue = decimalFormat.format(rightValue);
			//得到左值 减 右值 的差值
			double leftMinusRight = Double.parseDouble(decimalFormat.format(leftValue - rightValue));
				 */
				String tLeftValue = String.valueOf(leftValue);
				String tRightValue = String.valueOf(rightValue);
				double leftMinusRight = leftValue - rightValue;

				// 将错误信息存放到数据库里
				if ( Math.abs(leftMinusRight) > tolerance.doubleValue()) {
					log.error("错误：检验风险评级: 请查看cfRiskRatingCheck表里的数据！ "+ calculateFormula);
					PrepareErrLog("检验风险评级数据出错：" , ERR_LEVEL, tLeftValue, tRightValue, leftMinusRight,
							calculateFormula, list.get(i).getRemark(),tolerance);

					//警告信息
				} else if((Math.abs(leftMinusRight) > 0 )&& (Math.abs(leftMinusRight) <= tolerance.doubleValue())){
					log.warn("警告：检验风险评级指标数据: 请查看cfRiskRatingCheck表里的数据！ "+ calculateFormula);
					PrepareErrLog("检验风险评级指标数据警告：" ,WARN_LEVEL, tLeftValue, tRightValue, leftMinusRight,
							calculateFormula, list.get(i).getRemark(),tolerance);
				}

			}
		}
		log.info("--------------------------风险综合评估检验结束---------------------------");
		return true;

	}
	/**
	 * 将错误纪录存放到数据库中
	 */
	private boolean PrepareErrLog(String checkInfo, String errLevel,
			String leftValue, String rightValue, double differ,
			String calculateFormula, String remark, BigDecimal tolerance) {
		String errInfo = checkInfo + "公式：" + remark.trim() + "，" + "等式左边为："
				+ leftValue + ",等式右边为：" + rightValue.trim() + ",差额为："
				+ decimalFormat.format(differ) + "。容差为："
				+ tolerance.doubleValue();

		CfRiskRatingErrInfo cfRiskRatingErrInfo = new CfRiskRatingErrInfo();
		cfRiskRatingErrInfo.setYear(month);
		cfRiskRatingErrInfo.setQuarter(quarter);
		cfRiskRatingErrInfo.setReportType(reportRate);
		cfRiskRatingErrInfo.setErrLevel(errLevel); // 错误级别
		cfRiskRatingErrInfo.setErrorInfo(errInfo.trim());
		cfRiskRatingErrInfo.setCheckdate(new Date());
		cfRiskRatingErrInfo.setTemp(calculateFormula.trim());
		cfRiskRatingErrInfo.setComCode(comCode);
		cfRiskRatingErrInfo.setTolerance(tolerance); // 容差

		if (!dataCheckDao.insertErrorToRiskRating(cfRiskRatingErrInfo)) {
			log.error("插入数据到数据库出错！");
		}
		log.error("检验出错：" + errInfo + "\n");
		return true;
	}

	/**
	 * 获取算术表达式里运算符
	 * 
	 * @param arithmeticExp
	 *            算术表达式
	 */
	private String getOperator(String arithmeticExp) {
		Pattern pattern = Pattern.compile("=|>|<|≥|≤");
		Matcher matcher = pattern.matcher(arithmeticExp);

		if (!matcher.find()) {
			log.error("核验等式里没有运行符=、<、≤、>、≥ ");
			return null;
		}

		// 返回运行体符
		return matcher.group();
	}

	// 去除#号，获取指标代码
	private int spiltAndGetOutitemcode(String outItemCode) {
		// 去除固定因子的#
		outItemCode = outItemCode.substring(1, outItemCode.length() - 1);
		outItemCode = outItemCode.replaceAll(" ", "");

		return Integer.parseInt(decimals_HashMap.get(outItemCode));
	}

	/**
	 * 对表达式进行求值
	 */
	private double getCalResult(String calculateFormula) {
		List<String> expression = new ArrayList<String>();// 存储中序表达式
		List<String> right = new ArrayList<String>();// 存储右序表达式

		StringTokenizer st = new StringTokenizer(calculateFormula, "+-*/()[]",
				true);
		while (st.hasMoreElements()) {
			expression.add(st.nextToken());
		}

		// 将表达式转成后缀表达式
		toPostfixExpression(expression, right);

		Stack<String> stack = new Stack<String>();

		String op1, op2, is = null;
		Iterator<String> it = right.iterator(); // [(, #S01_00004#, #S01_00005#,
		// ), #S01_00008#, /, +]

		while (it.hasNext()) {
			is = (String) it.next();
			if (Calculate.isOperator(is)) {
				if (is.equals("+") || is.equals("-") || is.equals("*")
						|| is.equals("/")) {
					op1 = stack.pop();
					op2 = stack.pop();

					try {
						stack.push(Calculate.twoResult(is, op1, op2));
					} catch (Exception e) {
						log.error("检验出错！计算公式：" + calculateFormula + "，异常为："
								+ e.getMessage());
						// e.printStackTrace();
					}
				}
			} else {// is #s03_00184# Bq
				is = getValueQuerySQL(is);// 解析表达式中的查询条件，并查询返回值
				stack.push(is);
			}
		}

		if (stack.empty()) {
			log.info("公式对应的指标在数据库里没有数据！");
			return 0.00;
		}
		// 获取计算结果
		String calculateResult = stack.pop();

		if ("Infinity".equals(calculateResult) || "NaN".equals(calculateResult)) {
			calculateResult = "0";
		}

		double dSum = 0.0;
		dSum = Double.parseDouble(calculateResult);

		if (dSum == 0) {
			return 0.00;
		}

		return dSum;

	}

	/**
	 * 
	 * @param outItemCode
	 *            固定因子
	 * @param BqOrSqFlag
	 *            本期或上期的的标志，即是"Bq"还是"Sq"字符串
	 */
	private String getValueQuerySQL(String outItemCode) {
		String outItemCodeValue = ""; // 固定因子对应的值
		// 判断outItemCode是否为数字
		boolean isNumber = true;
		try {
			Double.parseDouble(outItemCode);
		} catch (NumberFormatException ex) {
			isNumber = false;
		}

		if (isNumber) {// 是数字，直接返回原值
			return outItemCode;

		} else {
			// 判断是否为百分比值
			if (outItemCode.indexOf("%") != -1) {
				// 去掉百分号
				outItemCode = outItemCode.replaceAll("%", "");
				outItemCode = outItemCode.replaceAll(" ", "");

				double dtemp = Double.parseDouble(outItemCode);
				dtemp = dtemp / 100;
				outItemCodeValue = String.valueOf(dtemp);

			} else {
				BigDecimal value = new BigDecimal(0);

				// 去除固定因子的#
				outItemCode = outItemCode
						.substring(1, outItemCode.length() - 1);
				outItemCode = outItemCode.replaceAll(" ", "");

				value = HashMap.get(outItemCode);
				if (value == null) {
					log.info("getValueQuerySQL方法下，cfreportdata表里没有该因子： "
							+ outItemCode);
					value = new BigDecimal(0);
				}

				outItemCodeValue = String.valueOf(value.doubleValue());
			}
		}

		return outItemCodeValue;
	}

	/**
	 * 将中序表达式转换为后序表达式
	 */
	private void toPostfixExpression(List<String> expression, List<String> right) {
		Stack<String> stack = new Stack<String>();

		String operator;
		int position = 0;
		while (true) {
			if (Calculate.isOperator((String) expression.get(position))) {
				if (stack.isEmpty()
						|| ((String) expression.get(position)).equals("(")) {
					stack.push(expression.get(position));
				} else {
					if (((String) expression.get(position)).equals(")")) {
						if (!(stack.peek()).equals("(")) {
							operator = stack.pop();
							right.add(operator);
						}
						// ---这里一定要注意了，千万不能漏，漏掉就有可能在括号的地方出错
						operator = stack.pop();
						// ---这里一定要注意了，千万不能漏，漏掉就有可能在括号的地方出错
					} else {
						if (Calculate.priority((String) expression
								.get(position)) <= Calculate.priority(stack
										.peek())
										&& !stack.empty()) {
							if (!(stack.peek()).equals("("))
								// 这个条件很重要，看到很多网上面都没有，结果求：(5*8-(6-3))*5 就会出错
							{
								operator = stack.pop();
								right.add(operator);
							}

						}
						stack.push(expression.get(position));
					}
				}
			} else
				right.add(expression.get(position));
			position++;
			if (position >= expression.size())
				break;
		}

		while (!stack.empty()) {
			operator = stack.pop();
			right.add(operator);
		}
	}

	/**
	 *  获取校验错误信息
	 */
	@Transactional
	public Page<?> getErrorInfo(int page, int rows, DataCheckDto dataCheckDto) {


		String getRepType = "select codename from cfcodemanage where codetype='reporttype' and codecode= reportType ";
		String comName = "select comName from branchinfo where comcode='"+ dataCheckDto.getComCode()+ "'";

		StringBuilder sql = new StringBuilder();
		sql.append("select ("
				+ getRepType
				+ ") \"reportType\",year \"year\", quarter \"quarter\", ("
				+ comName
				+ " )\"comCode\", errLevel \"errLevel\",temp \"temp\",errorInfo \"errorInfo\" , to_char(checkdate,'yyyy-mm-dd') \"checkdate\"  from CfRiskRatingErrInfo  where 1=1 and reportType='2' ");

		if (dataCheckDto.getYear() != null
				&& dataCheckDto.getYear().length() > 0) {
			sql.append(" and year='" + dataCheckDto.getYear() + "'");
		}

		if (dataCheckDto.getQuarter() != null
				&& dataCheckDto.getQuarter().length() > 0) {
			sql.append(" and quarter='" + dataCheckDto.getQuarter() + "'");
		}
		if (dataCheckDto.getComCode() != null
				&& dataCheckDto.getComCode().length() > 0) {
			sql.append(" and comCode='" + dataCheckDto.getComCode() + "'");
		}

		return dataCheckDao.queryByPage(sql.toString(), page, rows);
	}
}
