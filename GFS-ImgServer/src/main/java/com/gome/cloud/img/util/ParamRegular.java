package com.gome.cloud.img.util;


public class ParamRegular {
	/** 
     * 判断number参数是否是整型数表示方式 
     * @param number 
     * @return 
     */  
    public static boolean isIntegerNumber(String number){  
        number=number.trim();  
        String intNumRegex="\\d+";//整数的正则表达式  
        if(number.matches(intNumRegex))  
            return true;  
        else  
            return false;  
    }  
      
    /** 
     * 判断number参数是否是浮点数表示方式 
     * @param number 
     * @return 
     */  
    public static boolean isFloatPointNumber(String number){  
        number=number.trim();  
        String pointPrefix="{0,1}\\d*\\.\\d+";//浮点数的正则表达式-小数点在中间与前面  
        String pointSuffix="{0,1}\\d+\\.";//浮点数的正则表达式-小数点在后面  
        if(number.matches(pointPrefix)||number.matches(pointSuffix))  
            return true;  
        else  
            return false;  
    }  
    /**
     * 判断只包含 字母数字 字符串
     * */
    public static boolean isStrOrNumber(String strAndNum){
    	strAndNum=strAndNum.trim();  
    	 String pointPrefix="^[0-9a-zA-Z]+$";
    	 if(strAndNum.matches(pointPrefix)){
    		 return true;
    	 }else{
    		 return false;
    	 }
    }
    /**
     * 判断只包含 字母 字符串
     * */
    public static boolean isString(String str){
    	str=str.trim();  
    	 String pointPrefix="^[0-9a-zA-Z]{6}$";
    	 if(str.matches(pointPrefix)){
    		 return true;
    	 }else{
    		 return false;
    	 }
    }
    /**
     * 判断只包含 字母 字符串
     * 类似   abd12 和  abcd1-23kj2-23  正则限制中间不能包含 单独0
     * */
    public static boolean isStrOrNumAndline(String str){
    	 str=str.trim();  
    	 String pointline="^[0-9a-zA-Z]+(-[0-9a-zA-Z]+)*$";
    	 
    	 //===========正则限制 中间不能包含单独一个0
    	 String pointPreZero="^[0]+(-[0-9a-zA-Z]+)*$";
    	 String pointMidZero="^[0-9a-zA-Z]+(-[0-9a-zA-Z]+)*(-0)+(-[0-9a-zA-Z]+)*$";
    	 if(str.matches(pointPreZero) || str.matches(pointMidZero)){
    		 return false;
    	 }
    	 
    	 if(str.matches(pointline)){
    		 return true;
    	 }else{
    		 return false;
    	 }
    }
    /**
     * 判断只包含 数字1-9 和 中划线链接 字符串
     * 类似 4  和  1-2-3-4-5-6-7-8-9  
     * */
    public static boolean isNumAndline(String str){
    	 str=str.trim();  
    	 String pointline="^[1-9]{1}(-[1-9]{1})*$";
    	 if(str.matches(pointline)){
    		 return true;
    	 }else{
    		 return false;
    	 }
    }
    
    /**
     * 图片尺寸格式校验
     * */
    public static boolean isIntegerMultiInteger(String str){
    	
    	 str=str.trim();  
    	 String pointline="^\\d{1,4}(x\\d{1,4})?$";
    	 if(str.matches(pointline)){
    		 return true;
    	 }else{
    		 return false;
    	 }
    }
    
    
//    /**
//     * 判断0字符串
//     * 类似   abd12 和  abcd1-0-23
//     * */
//    public static boolean isZero(String str){
//    	 str=str.trim();  
//    	 String pointline="^[0-9a-zA-Z]+(-[0-9a-zA-Z]+)*$";
//    	 String pointPreZero="^[0]+(-[0-9a-zA-Z]+)*$";
//    	 String pointMidZero="^[0-9a-zA-Z]+(-[0-9a-zA-Z]+)*(-0)+(-[0-9a-zA-Z]+)*$";
//    	 if(str.matches(pointPreZero) || str.matches(pointMidZero)){
//    		 return false;
//    	 }
//    	 if(str.matches(pointline)){
//    		 return true;
//    	 }else{
//    		 return false;
//    	 }
//    }
    
//  	public final static String GomeFs_Key 	= "^(.+)/gomefs(.+)N(.+)$";	 //标签和尺寸
//	private static Pattern GOMEFS_KEY_PATTREN = Pattern.compile(GomeFs_Key);
   
    public static void main(String[] args){
//    	System.out.println(ParamRegular.isZero("1-02a1-1-1"));
//    	System.out.println(ParamRegular.isIntegerNumber("1111111111111111111111"));
//    	System.out.println(ParamRegular.isFloatPointNumber("11111.23"));
//    	System.out.println(ParamRegular.isStrOrNumber("123"));
//    	System.out.println(ParamRegular.isStrOrNumAndline("qwe"));
//    	System.out.println(ParamRegular.isNumAndline("1-1-9"));
//    	System.out.println(ParamRegular.isIntegerMultiInteger("1x2342"));
//    	
//      	
//    		Matcher sourceKeyMatcher = GOMEFS_KEY_PATTREN.matcher("n4/gomefs/t1/216/54087847/24721/3d9415af129b96b0N.jpg");
//    		System.out.println(sourceKeyMatcher.find());
//    		System.out.println(sourceKeyMatcher.group(2));

    }
}
