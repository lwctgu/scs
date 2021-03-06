package com.zhcs.service.impl;

import java.util.List;
import java.util.Map;

import org.jeewx.api.core.exception.WexinReqException;
import org.jeewx.api.wxuser.user.JwUserAPI;
import org.jeewx.api.wxuser.user.model.Wxuser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhcs.dao.WechatadminDao;
import com.zhcs.entity.ReceiveData;
import com.zhcs.entity.WechatadminEntity;
import com.zhcs.service.WechatadminService;
import com.zhcs.service.WeixintokenService;
import com.zhcs.utils.BeanUtil;
import com.zhcs.utils.LogUtil;
import com.zhcs.utils.ServiceException;
import com.zhcs.utils.StringUtil;


//*****************************************************************************
/**
 * <p>Title:WechatadminServiceImpl</p>
 * <p>Description: 微信关注列表</p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: 深圳市智慧城市管家信息科技有限公司 </p>
 * @author 刘晓东 - Alter
 * @version v1.0 2017年2月23日
 */
//*****************************************************************************

@Service("wechatadminService")
public class WechatadmintServiceImpl implements WechatadminService {
	@Autowired
	private WechatadminDao wechatadminDao;
	@Autowired
	private WeixintokenService weixintokenService;
	
	@Override
	public WechatadminEntity queryObject(Long id){
		return wechatadminDao.queryObject(id);
	}
	
	@Override
	public WechatadminEntity queryObjectByOpenid(String openid){
		return wechatadminDao.queryObjectByOpenid(openid);
	}
	
	@Override
	public List<WechatadminEntity> queryList(Map<String, Object> map){
		return wechatadminDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return wechatadminDao.queryTotal(map);
	}
	
	@Override
	@Transactional
	public void save(WechatadminEntity wechatadmin){
		wechatadminDao.save(wechatadmin);
	}
	
	@Override
	@Transactional
	public void update(WechatadminEntity wechatadmin){
		wechatadminDao.update(wechatadmin);
	}
	
	@Override
	@Transactional
	public void delete(Long id){
		wechatadminDao.delete(id);
	}
	
	@Override
	@Transactional
	public void deleteBatch(Long[] ids){
		wechatadminDao.deleteBatch(ids);
	}

	
	@Override
	@Transactional
	public void doSubscribe(ReceiveData data) throws ServiceException {
		Wxuser wxuser = null;
		try {
			wxuser = JwUserAPI.getWxuser(weixintokenService.getAccessTokenAdmin(), data.getFromUserName());
		} catch (WexinReqException e) {
			LogUtil.error("根据 user_openid 获取关注用户的基本信息失败：{}", data.getFromUserName());
			throw new ServiceException(e);
		}
		// 应该先判断是否有数据，没有数据进行插入，有数据进行修改。
		WechatadminEntity wechat = queryObjectByOpenid(wxuser.getOpenid());
		if(StringUtil.isValid(wechat)){
			wechat.setCity(wxuser.getCity());
			wechat.setCountry(wxuser.getCountry());
			wechat.setHeadimgurl(wxuser.getHeadimgurl());
			wechat.setLanguage(wxuser.getLanguage());
			wechat.setNickname(wxuser.getNickname());
			wechat.setOpenid(wxuser.getOpenid());
			wechat.setProvince(wxuser.getProvince());
			wechat.setSex(wxuser.getSex());
			wechat.setSubscribe(StringUtil.valueOf(wxuser.getSubscribe()));
			wechat.setSubscribeTime(wxuser.getSubscribe_time());
			BeanUtil.fillCCUUD(wechat, 1L);
			update(wechat);
		} else {
			wechat = new WechatadminEntity();
			wechat.setCity(wxuser.getCity());
			wechat.setCountry(wxuser.getCountry());
			wechat.setHeadimgurl(wxuser.getHeadimgurl());
			wechat.setLanguage(wxuser.getLanguage());
			wechat.setNickname(wxuser.getNickname());
			wechat.setOpenid(wxuser.getOpenid());
			wechat.setProvince(wxuser.getProvince());
			wechat.setSex(wxuser.getSex());
			wechat.setSubscribe(StringUtil.valueOf(wxuser.getSubscribe()));
			wechat.setSubscribeTime(wxuser.getSubscribe_time());
			BeanUtil.fillCCUUD(wechat);
			save(wechat);
		}
	}

	@Override
	@Transactional
	public void doUnsubscribe(ReceiveData data) {
		updateSubscribeState(data.getFromUserName(), "0");
	}

	/**
	 * 根据openId更改订阅状态
	 */
	private void updateSubscribeState(String openId, String subscribe) {
		wechatadminDao.updateSubscribeState(openId, subscribe);
	}
	
	
	
}
