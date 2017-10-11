package com.sinosoft.util;


import com.sinosoft.common.BeanFactory;

/**
 * Shiro 默认是16进制加密
 *
 * @author zh
 */
public class MD5Encrypt{

	private static EncryptService passwordEncryptService;

    private static EncryptService getPasswordEncryptService() {
    	if (passwordEncryptService == null) {
            passwordEncryptService = (EncryptService) BeanFactory.getBean("encryptService");
        }
        return passwordEncryptService;
    }


	public static String encryptPassword(String password) {
        return getPasswordEncryptService().encryptPassword(password,"");
    }
}
