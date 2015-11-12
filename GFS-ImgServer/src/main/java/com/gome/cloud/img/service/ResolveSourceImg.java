package com.gome.cloud.img.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gome.cloud.img.bean.ImgObject;
import com.gome.cloud.img.common.CommonConstants;
import com.gome.cloud.img.dao.GomefsDAO;


@Service
public class ResolveSourceImg extends UrlResolveAbstract {
	@Autowired FsTransfer fsTransfer;
	@Autowired FileMapService fileMapService;
	@Autowired GomefsDAO gomefsDao;
	@Override
	public void resolveLabelSize(ImgObject object) {
		//原图业务，无此参数，
		
		//读取原key
		object.setSourceKey(object.getNewUrl().substring(object.getNewUrl().lastIndexOf("/")+1));

	}

	@Override
	public void resolveQuality(ImgObject object) {
		//原系统没有该参数，无处理逻辑
	}
	@Override
	public void doTransferAndSetKey(ImgObject object){
		if(object.getIsGomefsKey()){
			object.setZoomData(gomefsDao.read(object.getGomefsKey())); 
		}else{
    	fsTransfer.transferFile(object);
    	fileMapService.addFileNameMap(object.getSourceKey(), CommonConstants.ORIGINAL_BUSINESS, object.getGomefsKey());
	}
	}
}
