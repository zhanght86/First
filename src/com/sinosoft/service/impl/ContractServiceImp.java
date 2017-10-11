package com.sinosoft.service.impl;


import java.util.Iterator;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sinosoft.common.Page;
import com.sinosoft.dao.ContractDao;
import com.sinosoft.dto.ContractDTO;
import com.sinosoft.entity.Contract;
import com.sinosoft.service.ContractService;

@Service
public class ContractServiceImp implements ContractService{
	@Resource
	private ContractDao contractDao;
	@Transactional
	public void saveConTract(ContractDTO cdt) {
		Contract c=new Contract();
		c.setContractId(cdt.getContractId());
		c.setContractName(cdt.getContractName());
		c.setApplyNo(cdt.getApplyNo());
		c.setRemark(cdt.getRemark());
		contractDao.save(c);
		//contractDao.save();
	}
	@Transactional
	public void deleteConTract(String[] idArr) {
		for (String id : idArr) {
			contractDao.delete(Integer.parseInt(id));
		}
		//contractDao.save();
	}
	public Page<?> qryConTract(int page,int rows,ContractDTO cdt) {
		return contractDao.query(page,rows,ContractDTO.toEntity(cdt));
		//contractDao.save();
	}
	public Contract findConTract(int id) {
		return contractDao.find(id);
		//contractDao.save();
	}
	@Transactional
	public void updateConTract(int id, ContractDTO cdt) {
		Contract c =contractDao.find(id);
		c.setContractId(cdt.getContractId());
		c.setContractName(cdt.getContractName());
		c.setApplyNo(cdt.getApplyNo());
		c.setRemark(cdt.getRemark());
		contractDao.update(c);
		
	}
}
