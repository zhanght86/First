package com.sinosoft.zcfz.service.reportdataimport;

import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dto.reportdataimport.ItemDataManualDTO;
import com.sinosoft.entity.CfReportDataId;
import com.sinosoft.entity.CfReportItemCodeDesc;

public interface ItemDataManualService {
    public    Page<?>   search(int  page,int rows,ItemDataManualDTO dto);
    public    boolean   add(ItemDataManualDTO dto);
    public    void      eidtor(CfReportDataId id,ItemDataManualDTO dto);
}
