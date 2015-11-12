package com.gome.cloud.img.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gome.cloud.img.bean.ImgObject;
import com.gome.cloud.img.dao.GomefsDAO;
import com.gome.cloud.img.util.MetadataUtils;


/**
 * 图片访问
 * */
@Service
public class ImgServer {
//	private static Logger log = LoggerFactory.getLogger(ImgServer.class);
	@Autowired GomefsDAO gomefsDao;
	@Autowired FsTransfer fsTransfer;
	@Autowired FileMapService fileMapService;
	@Autowired
	private ResolveHolder resolveHolder;
	@Autowired private CacheService cacheService;
	/**
	 * 获取图片
	 * */
	public void getImage(ImgObject object){
//        //解析URI
//		resolveUrlServer.resolveUrl(object);
//        //判断gomefsKey是否为null，如果为null 说明没有获取到相关信息，需要转换
//        if(object.gomefsKey != null){
//        	object.zoomData = gomefsDao.read(object.gomefsKey);
//        }else{  
//        	fsTransfer.transferFile(object);
//        	fileMapService.addFileNameMap(object.sourceKey, null, object.gomefsKey);
//        }
		resolveHolder.resolve(object);
        MetadataUtils.process(object);
	}
	
	
	public String writeImge(byte[] data){
		return gomefsDao.write(data);
	}
	
	
}
