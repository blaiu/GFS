package com.gome.cloud.img.web;

import java.io.IOException;
import java.util.Arrays;
import java.util.zip.CRC32;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.gome.cloud.img.bean.ImgObject;
import com.gome.cloud.img.common.CommonConstants;
import com.gome.cloud.img.exception.MessageEnum;
import com.gome.cloud.img.log.RunLog;
import com.gome.cloud.img.service.AuthService;
import com.gome.cloud.img.service.FileMapService;
import com.gome.cloud.img.service.ImgServer;


@Controller
public class ImageControl {

	String[] extensionArray = { "jpg", "bmp", "gif", "png", "jpeg", "webp", "html" }; // 允许上传的文件后缀名
	
	private int maxLength = 32 * 1024 * 1024; // 允许文件大小

	@Autowired
	AuthService authService;
	
	@Autowired
	ImgServer imgServer;
	
	@Autowired
	FileMapService fileMapService;

	@RequestMapping(value = "/**", method = { RequestMethod.GET, RequestMethod.HEAD })
	public ResponseEntity<byte[]> get(HttpServletRequest request, HttpServletResponse response) {
		ImgObject object = (ImgObject) request.getAttribute(CommonConstants.IMG_CONTEXT);
		imgServer.getImage(object);
		return new ResponseEntity<byte[]>(object.getZoomData(), object.getHeaders(), HttpStatus.OK);
	}

	@RequestMapping(value = "/images", method = RequestMethod.POST)
	@ResponseBody
	public void uploadImage(HttpServletRequest request,	HttpServletResponse response,@RequestParam(value = "file", required = false) MultipartFile file) {
		MessageEnum message = MessageEnum.FAIL;
		String authCode = request.getParameter("authCode");
		String customName = request.getParameter("customName");     //屏蔽自定义名称
		String busiCode = request.getParameter("busiCode");
		String crc = request.getParameter("crc");
		try {	 
			String fileName = file.getOriginalFilename();
		    String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
		    if (!Arrays.asList(extensionArray).contains(suffix.toLowerCase())) {
		    	message = MessageEnum.FILERECOGNIZED;
		    } else if(file.getSize() > maxLength) {
				message = MessageEnum.IMGTOOLARGER;
			} else if(!checkAuth(authCode)) {
				message = MessageEnum.AUTHCODEINVALID;
			} else {
//				屏蔽自定义命名			
				if (StringUtils.isNotEmpty(customName) && fileMapService.checkFileAndBusiNameExist(customName, busiCode)) {
					message = MessageEnum.RENAME;    
					RunLog.LOG.info(" Rename Error :" + customName);
				} else {
					if (!checkCRC(crc,file)) {
						message = MessageEnum.CRCERROR;    
						RunLog.LOG.info(" CRC check error :" + crc);
					} else {
						String imgKey = imgServer.writeImge(file.getBytes());
						RunLog.LOG.info("upload success return key is :" + imgKey);
						//存在自定义名称是增加映射文件
						if(StringUtils.isNotEmpty(customName)) {
							fileMapService.addFileNameMap(customName, busiCode, imgKey + "." + suffix);
						}
						message = MessageEnum.SUCCESS;
						message.setMsg(imgKey);
					}
				}				
			}
		} catch (IOException e) {
			RunLog.LOG.info(" Exception :" + e.getMessage());
			message = MessageEnum.EXCEPTIONERROR;
		}
	 
		this.writeResponse(response, message);
	}
	
	public boolean checkAuth(String authCode){
		if (StringUtils.isNotEmpty(authCode)) {
			return authService.authorization(authCode);
		}else{
			return false;
		}
	}
	public boolean checkCRC(String crc,MultipartFile file) throws IOException{
		if (StringUtils.isNotEmpty(crc)) {
			CRC32 checkCrc = new CRC32();
			checkCrc.update(file.getBytes());
			if(checkCrc.getValue() == Long.valueOf(crc)){
				return true;
			}else{
				return false;
			}
		}else{
			return true;
		}
	}
	
//
//	/**
//	 * 图片上传， 通过http，post方式上传图片信息 1、校验文件不为空 2、校验文件大小 3、校验后缀名 4、写入存储，返回正确信息
//	 * */
//	@RequestMapping(value = "/image", method = RequestMethod.POST)
//	@ResponseBody
//	public void upload(HttpServletRequest request,
//			HttpServletResponse response, @RequestHeader HttpHeaders headers) {
//		MessageEnum message;
//		try {
//			String auth = request.getHeader("authCode"); // 授权码
//			String customName = request.getHeader("customName"); // 自定义名称
//			String imageInfo = request.getHeader("imageInfo"); // 图片信息
//																// crc-大小.后缀名
//			String busiCode = request.getHeader("busiCode"); // 业务名称
//			if (auth == null || customName == null || imageInfo == null) {
//				RunLog.LOG.info(" client info has  null  {}:{}:{} ", auth,
//						customName, imageInfo);
//				message = MessageEnum.EXCEPTIONERROR;
//			}
//			customName = URLEncoder.encode(customName,
//					CommonConstants.DEFAULT_ENCODING); // 重编码
//			if (fileMapService.checkFileAndBusiNameExist(customName, busiCode)) {
//				throw new UploadException(MessageEnum.REPATENAME);
//			}
//
//			if (authService.authorization(auth)) {
//				byte[] imageInfoBytes = getUploadFileInfo(request, imageInfo);
//				String imgKey = imgServer.writeImge(imageInfoBytes);
//				RunLog.LOG.info("upload success return key is :" + imgKey);
//				fileMapService.addFileNameMap(customName, busiCode, imgKey);
//				message = MessageEnum.SUCCESS;
//				message.setMsg(imgKey);
//
//			} else {
//				message = MessageEnum.AUTHCODEINVALID;
//			}
//
//		} catch (UploadException e) {
//			message = e.getMessageEnum();
//		} catch (UnsupportedEncodingException e) {
//			RunLog.LOG.error(" fileName encode error ：{}",
//					request.getHeader("customName"));
//			message = MessageEnum.EXCEPTIONERROR;
//		} catch (Exception e) {
//			RunLog.LOG.error(e.getMessage());
//			message = MessageEnum.EXCEPTIONERROR;
//		}
//		this.writeResponse(response, message);
//	}
//
//	private byte[] getFileInfo(HttpServletRequest request) {
//
//		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
//		System.out.println("####" + isMultipart);
//		if (isMultipart) {
//			// 构造一个文件上传处理对象
//			FileItemFactory factory = new DiskFileItemFactory();
//			ServletFileUpload upload = new ServletFileUpload(factory);
//			Iterator items;
//			// 解析表单中提交的所有文件内容
//			try {
//				items = upload.parseRequest(request).iterator();
//				while (items.hasNext()) {
//					FileItem item = (FileItem) items.next();
//					if (!item.isFormField()) {
//						System.out.println(item.get());
//						return item.get();
//					} else {
//						System.out.println(item.getFieldName());
//					}
//				}
//			} catch (FileUploadException e) {
//				e.printStackTrace();
//				return null;
//			}
//		}
//		return null;
//		// byte[] imgBuffer = null;
//		// InputStream fis;
//		// try {
//		// fis = request.getInputStream();
//		// ByteArrayOutputStream bos = new ByteArrayOutputStream();
//		// byte[] b = new byte[1024];
//		// int n;
//		// while ((n = fis.read(b)) != -1) {
//		// bos.write(b, 0, n);
//		// }
//		// fis.close();
//		// imgBuffer = bos.toByteArray();
//		// System.out.println(imgBuffer.length);
//		// bos.close();
//		// } catch (IOException e) {
//		// e.printStackTrace();
//		// }
//		//
//		// return imgBuffer;
//	}
//
//	private byte[] getUploadFileInfo(HttpServletRequest request,
//			String imageInfo) {
//
//		byte[] imgBuffer = null;
//		Long clientCRC = ImageInfoUtil.getFileCRC(imageInfo);
//		String extension = ImageInfoUtil.getFileExtension(imageInfo);
//		Long fileSize = ImageInfoUtil.getFileSize(imageInfo);
//
//		// 验证后缀名
//		if (!checkExtension(extension)) {
//			throw new UploadException(MessageEnum.EXTENSIONERROR);
//		}
//		try {
//			InputStream fis = request.getInputStream();
//			ByteArrayOutputStream bos = new ByteArrayOutputStream();
//			byte[] b = new byte[1024];
//			int n;
//			while ((n = fis.read(b)) != -1) {
//				bos.write(b, 0, n);
//			}
//			fis.close();
//			imgBuffer = bos.toByteArray();
//			bos.close();
//		} catch (IOException e) {
//			throw new UploadException(MessageEnum.EXCEPTIONERROR);
//		}
//
//		CRC32 crc = new CRC32();
//		crc.update(imgBuffer);
//		if (crc.getValue() != clientCRC) {
//			RunLog.LOG.warn("  CRC {} not equal  clientCRC is {} ", crc,
//					clientCRC);
//			throw new UploadException(MessageEnum.EXCEPTIONERROR);
//		}
//		if (fileSize > maxLength) {
//			throw new UploadException(MessageEnum.IMGTOOLARGER);
//		}
//		if (imgBuffer.length != fileSize) {
//			RunLog.LOG.error("fileSize is not equal :" + fileSize);
//			throw new UploadException(MessageEnum.EXCEPTIONERROR);
//		}
//		return imgBuffer;
//	}

	/**
	 * 校验扩展名是否符合上传条件，
	 * 
	 * @return true or false
	 * */
//	private boolean checkExtension(String extension) {
//		if (extension != null && !extension.equals("")) {
//			for (String ext : extensionArray) {
//				if (ext.equalsIgnoreCase(extension)) {
//					return true;
//				}
//			}
//		}
//		return false;
//	}

	/**
	 * 输出响应数据
	 * 
	 * @return void
	 * */
	private void writeResponse(HttpServletResponse response,
			MessageEnum responseData) {
		response.setCharacterEncoding("UTF-8");
		try {
			response.getWriter().write(responseData.toJson());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 图片上传， 通过http，post方式上传图片信息 1、校验文件不为空 2、校验文件大小 3、校验后缀名 4、写入存储，返回正确信息
	 * */
	// @RequestMapping(value = "/imageUpload", method = RequestMethod.POST)
	// public void imageUpload(@RequestParam(value = "file", required = false)
	// MultipartFile file, HttpServletRequest request, HttpServletResponse
	// response) {
	// String imgKey;
	// if (file == null) {
	// this.writeResponse(response,
	// ReturnInfo.getFailMsg(ReturnInfo.FILERECOGNIZED));
	// return ;
	// }
	// try {
	// //判断文件大小是否在限制范围内
	// if (file.getSize() > maxLength) {
	// this.writeResponse(response,
	// ReturnInfo.getFailMsg(ReturnInfo.IMGTOOLARGER));
	// return ;
	// }
	// String fileName = file.getOriginalFilename();
	// // 获取并校验是否是合法后缀名文件
	// String ext = getAndCheckExtension(fileName);
	// if (!ext.equals("")) {
	// // 写入存储，返回key
	// imgKey = gfs.writeBytes(file.getBytes());
	// this.writeResponse(response, ReturnInfo.getSuccessMsg(imgKey));
	// return ;
	// } else {
	// this.writeResponse(response,
	// ReturnInfo.getFailMsg(ReturnInfo.EXTENSIONERROR));
	// return ;
	// }
	// } catch (Exception e) {
	// this.writeResponse(response,
	// ReturnInfo.getFailMsg(ReturnInfo.EXCEPTIONERROR));
	// return ;
	// }
	//
	// }
	// 下载取消
	// @RequestMapping(value = "/downLoadImage", method = RequestMethod.GET)
	// public ResponseEntity<byte[]> downLoadImage(HttpServletRequest request,
	// HttpServletResponse response) {
	// ImgObject object = (ImgObject)
	// request.getAttribute(CommonConstants.IMG_CONTEXT);
	// imgServer.getImage(object);
	// HttpHeaders headers = new HttpHeaders();
	// headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	// headers.setContentDispositionFormData("attachment", object.sourceKey);
	// //有这句说明下载附件
	// headers.add(HttpHeaders.ETAG, "etag");
	// return new ResponseEntity<byte[]>(object.zoomData, headers,
	// HttpStatus.OK);
	// }
	/**
	 * 表单直接图片上传， 通过http，post方式上传图片信息 1、校验文件不为空 2、校验文件大小 3、校验后缀名 4、写入存储，返回正确信息
	 * 这个方法暂时不能用，后缀名确认有问题
	 * */
	// @RequestMapping(value = "/imageUploadAll", method = RequestMethod.POST)
	// public void imageUploadAll(
	// @RequestParam(value = "file", required = false) CommonsMultipartFile[]
	// files,
	// HttpServletRequest request, HttpServletResponse response) {
	// StringBuilder imgKeys = new StringBuilder();
	// for (MultipartFile file : files) {
	// if (file == null) {
	// this.writeResponse(response,
	// ReturnMessage.getFailMsg(ReturnMessage.FILERECOGNIZED));
	// return;
	// }
	// try {
	// if (file.getBytes().length < maxLength) {
	// this.writeResponse(response,
	// ReturnMessage.getFailMsg(ReturnMessage.IMGTOOLARGER));
	// return;
	// }
	// String fileName = file.getOriginalFilename();
	// // 获取并校验是否是合法后缀名文件
	// if (checkExtension(fileName.substring(fileName.lastIndexOf(".")))) {
	// // 写入存储，返回key
	// // imgKeys.append(gfs.writeBytes(file.getBytes())).append(",");
	// } else {
	// this.writeResponse(response,
	// ReturnMessage.getFailMsg(ReturnMessage.EXTENSIONERROR));
	// return;
	// }
	// } catch (Exception e) {
	// this.writeResponse(response,
	// ReturnMessage.getFailMsg(ReturnMessage.EXCEPTIONERROR));
	// return;
	// }
	// }
	// this.writeResponse(response,
	// ReturnMessage.getSuccessMsg(imgKeys.toString()));
	// }
}
