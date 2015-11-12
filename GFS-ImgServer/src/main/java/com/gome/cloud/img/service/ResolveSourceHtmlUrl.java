package com.gome.cloud.img.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gome.cloud.img.bean.ImgObject;
import com.gome.cloud.img.common.CommonConstants;
import com.gome.cloud.img.dao.GomefsDAO;


/**
 * 处理HTML URL业务，没有参数，只保留原信息读取返回
 * */
@Service
public class ResolveSourceHtmlUrl extends UrlResolveAbstract {
	@Autowired FsTransfer fsTransfer;
	@Autowired FileMapService fileMapService;
	@Autowired GomefsDAO gomefsDao;
	@Override
	public void resolveLabelSize(ImgObject object) {
		//读取原key
		object.setSourceKey(object.getNewUrl().substring(object.getNewUrl().lastIndexOf("/")+1));
	}

	@Override
	public void resolveQuality(ImgObject object) {

	}

	@Override
	public void doTransferAndSetKey(ImgObject object){
		if(object.getIsGomefsKey()){
			object.setZoomData(gomefsDao.read(object.getGomefsKey())); 
		}else{
    	fsTransfer.transferFile(object);
    	fileMapService.addFileNameMap(object.getSourceKey(), CommonConstants.HTML_BUSINESS, object.getGomefsKey());
		}
	}
}
