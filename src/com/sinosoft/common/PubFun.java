package com.sinosoft.common;

import java.util.ArrayList;
import java.util.List;




import com.sinosoft.entity.CfReportData;
import com.sinosoft.util.EmailUtil;

/**
 * @author Administrator
 *
 */
public class PubFun {
	
	
	/**
	 * 根据用户ID取得用户信息
	 * @param userCode
	 * @return UserInfo
	 */
	public static CfReportData getCfReportData(String outItemCode){
		BeanFactory b=new BeanFactory();
		@SuppressWarnings("static-access")
		Dao dao=(Dao) b.getBean("dao");
        String hsql = " from CfReportData u "
                    + " where u.id.outItemCode='" + outItemCode + "'";
        List<?> list = dao.query(hsql);
        if(list.size()==0){
            System.out.println(outItemCode+"不存在！");
            return null;
        }
        return (CfReportData)list.get(0);
	}
	
	/**
	 * 单发邮件
	 */
	public static void sendOneMail(String msg, String recieverAddress, boolean quickFlag) {
		List<String> recipients = new ArrayList<String>();
		recipients.add(recieverAddress);
		try{
			sendMoreMail(msg, recipients, quickFlag);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		sendOneMail("111","shiyongfeng@sinosoft.com.cn",true);
	}

	/**
	 * 群发邮件
	 */
	public static void sendMoreMail(String message, List<String> recipients, boolean quickFlag) {
		String senderName = "偿二代";
		String subject = "偿二代";
		message += "\n\n\n\t偿二代邮件测试";
		message += "\n\t"
				+ new java.sql.Date(System.currentTimeMillis()).toString();
		
		try{
			sendMail(senderName, recipients, subject, message);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 发送邮件的公共方法
	 * @param senderName 收件人姓名
	 * @param recipients 收件人邮件地址列表
	 * @param subject 邮件主题
	 * @param message 邮件消息
	 */
	public static void sendMail(String senderName, List<String> recipients,
			String subject, String message) {
		//实际业务从数据库中去取
		String cSmtpHost = "XXX"; //邮箱服务器 比如mail.sinosoft.com.cn
		String cSenderAddress = "XXX"; //格式XXX@XXX.com
		String cAccountName = "XXX"; //名称
		String cPassword = "XXX"; //密码
		String cUseFlag = "1";


		if ("1".equals(cUseFlag)) {
			System.out.println("使用用户验证，用户名：" + cAccountName + ";密码：" + cPassword
					+ ";");
			EmailUtil.sendEmail(cSmtpHost, cSenderAddress, senderName,
					cAccountName, cPassword, recipients, subject, message);
		} else {
			System.out.println("未使用用户验证策略，如果邮件服务需要验证用户名密码，则会发送失败！");
			EmailUtil.sendEmail(cSmtpHost, cSenderAddress, senderName,
					recipients, subject, message);
		}
	}
	/**
	 * 字符串中某个字符出现的个数
	 * @param string
	 * @param subs
	 * @return
	 */
	public static int subCount(String string, String subs){
        int count = 0;
        String temp = string;
        while(temp.indexOf(subs)!=-1){//当temp中无subs子串时返回-1
            count++;
            temp = temp.substring(temp.indexOf(subs)+subs.length());//将第一次匹配后的剩下的字符串赋值给tenp
        }
        return count;
    }
	
}
