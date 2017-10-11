package com.sinosoft.zcfz.service.reportformcompute;  

import com.sinosoft.entity.UserInfo;



public interface CalPreComputeService {  
    public void preCompute(String id,UserInfo user,String reporttype) throws Exception ;
    public void backPreCompute(String id,String reporttype);
}