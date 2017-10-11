package com.sinosoft.zcfz.service.lianghua.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sinosoft.common.CurrentUser;
import com.sinosoft.common.Page;
import com.sinosoft.dao.UploadDao;
import com.sinosoft.dto.PluploadDTO;
import com.sinosoft.zcfz.dto.reportdataimport.UploadInfoDTO;
import com.sinosoft.entity.CfUploadDocument;
import com.sinosoft.entity.UploadFile;
import com.sinosoft.zcfz.service.lianghua.LianghuaSaveDataService;
import com.sinosoft.zcfz.service.lianghua.LianghuaUploadFileService;
import com.sinosoft.zcfz.service.reportdataimport.UploadFileService;

@Service
public class LianghuaUploadFileServiceImp implements LianghuaUploadFileService {
	@Resource
	private UploadDao uploadDao;

	@Transactional
	public void saveUploadFile(PluploadDTO p) {
		UploadFile uf=new UploadFile();
		uf.setFileName(p.getFileName());
		uf.setFilePath(p.getFilePath());
		uf.setRemark(p.getRemark());
		uploadDao.save(uf);

	}

	@Transactional
	public void deleteUploadFile(int id) {
		uploadDao.delete(id);
	}

	public Page<?> qryUploadFile(int page, int rows, int id) {
		// TODO Auto-generated method stub
		return null;
	}

	public UploadFile findUploadFile(int id) {
		// TODO Auto-generated method stub
		return uploadDao.find(id);
	}

	public void updateUploadFile(int id, PluploadDTO p) {
		// TODO Auto-generated method stub
		
	}

	@Transactional
	public void saveUploadFile(UploadInfoDTO p) {
		CfUploadDocument uf=new CfUploadDocument();
	
		uf.setReportrate(p.getReporttype());
		uf.setReporttype(p.getReportname());
		uf.setYearmonth(p.getYearmonth());
		uf.setQuarter(p.getQuarter());
		uf.setFilename(p.getFileName());
		uf.setFilepath(p.getFilePath() + "/" + p.getNewfileName());
		uf.setIsupflag("1");
		uf.setRemark(p.getRemark());
		uf.setOperator(CurrentUser.getCurrentUser().getUserCode());
		uf.setOperatdate(p.getDate());
		uploadDao.save(uf);
	}

}
