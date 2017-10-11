package com.sinosoft.zcfz.service.lianghua;  

import com.sinosoft.entity.UserInfo;



public interface LianghuaComputeService {  
    public void ComplexCompute(String id,UserInfo user) throws Exception ;
    public void backComplexCompute(String id);
}