package com.sinosoft.zcfz.service.impl.reportform;

import javax.annotation.Resource;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sinosoft.zcfz.service.reportform.ExportXbrlService;

@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations = {"classpath:sys-root.xml"})  
public class Test{
	@Resource
	private ExportXbrlService exsi;
	@org.junit.Test
	public void test() {
		exsi.test();
	}
	@org.junit.Test
	public void test1() {
		System.out.println(exsi.FormatData("12.22", "1"));
	}

}
