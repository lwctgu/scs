package com.zhcs.dao;

import java.util.List;

import com.zhcs.entity.MtdetailEntity;

//*****************************************************************************
/**
 * <p>Title:MtdetailDao</p>
 * <p>Description: 保养明细</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: 深圳市智慧城市管家信息科技有限公司 </p>
 * @author 刘晓东 - Alter
 * @version v1.0 2017年2月23日
 */
//*****************************************************************************
public interface MtdetailDao extends BaseDao<MtdetailEntity> {

	/*
	 * 根据保养id查询保养明细
	 */
	List<MtdetailEntity> queryListByMtid(Long mtid);
	
	/*
	 * 根据保养id删除保养明细中的记录
	 */
	void deleteByMtid(Long id);
	
}
