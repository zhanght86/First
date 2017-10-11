package com.sinosoft.zcfz.service.reportformcompute;  

import com.sinosoft.entity.UserInfo;



public interface RiskComputeService {  
    public void ComplexCompute(String id,UserInfo user) throws Exception ;
    public void backComplexCompute(String id);
}