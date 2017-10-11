package com.sinosoft.zcfz.service.reportformcompute;  

import com.sinosoft.entity.UserInfo;



public interface CalComplexComputeService {  
    public void ComplexCompute(String id,UserInfo user,String reporttype) throws Exception ;
    public void backComplexCompute(String id,String reporttype );
}