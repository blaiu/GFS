package com.gome.cloud.protocol;

import com.gome.cloud.exception.GFSException;

/**
 * 协议公用实现
 * @author blaiu
 *
 */
public class ProtocolUtil {

	/** 协议默认超时时间 */
	public final static int DEFAULT_TIMEOUT = 2000;
	
	/** 魔数 唯一区别协议类型 245 */
	public final static int MAGIC = 0xF5;
	
	/** 开始偏移量*/
	public final static int RESERVED = 0x00;
	
	/** 错误标识 255 */
	public final static int ERROR = 0xFF;
	
	/**
	 * 数值型转换字节
	 * @param v
	 * @return
	 */
	public static byte[] writeInt(long v) {
        byte bytes[] = new byte[4];
        bytes[0] = (byte) ((v >>> 0) & 0xff);
        bytes[1] = (byte) ((v >>> 8) & 0xff);
        bytes[2] = (byte) ((v >>> 16) & 0xff);
        bytes[3] = (byte) ((v >>> 24) & 0xff);
        return bytes;
    }
	
	/**
	 * 字节转换int
	 * @param res
	 * @return
	 */
	public static int byte2int(byte... res) {
		return (res[0] & 0xff) 
				| ((res[1] << 8) & 0xff00) 
				| ((res[2] << 16) & 0xff0000)
                | ((res[3] << 24 & 0xff000000));
	}
	
	/**
	 * 指令操作类型
	 * @author blaiu
	 *
	 */
	public static enum Command {
		
		/** 查 */
		GET(0x03), 
		
		/** 写 */
		PUT(0x05), 
		
		/** 删 */
		DEL(0x07);
		
		public final int command;
		
		private Command(int code) {
			command = code;
		}
	}
	
	/**
	 * head中获取操作指令
	 * @param head
	 * @return
	 */
	public static Command getCommand(byte[] head) {
		switch (head[1]) {
		case 0x03:
		case 0x04:
			return Command.GET;
		case 0x05:
		case 0x06:
			return Command.PUT;
		case 0x07:
		case 0x08:
			return Command.DEL;
		default:
			throw new GFSException("Unkown command :" + head[1]);
		}
	}
	
	public static byte[] createHead(final Command cmd, final byte[]... args ) {
		/** 协议head 数组大小规定20 */
		byte[] head = new byte[20];
		
		/** 第一位为魔数 区分协议的唯一标识 */
		head[0] = (byte)MAGIC;
		
		/** 操作指令，增删查 */
		head[1] = (byte)cmd.command;
		
		/** 文件个数 */
		head[2] = (byte)args.length;
		
		/** 开始偏移量 */
		head[3] = RESERVED;
		
		/**
		 * head[] 代表head描述，次循环是8个byte[]的大小
		 */
		for(int i = 1; i <= args.length; i++) {
			for (int j = 1; j <= 4; j++) {
				head[4 * i + j - 1] = args[i - 1][j - 1];
			}
		}
		return head;
	}
	
	/**
	 * head处理,得到body长度
	 * @param head
	 * @return
	 */
	public static int processHead(final byte[] head) {
		if (head[0] != (byte)MAGIC) {
			throw new GFSException(" head[0] magic error!! ");
		}
		
		if (head[1] == (byte) ERROR) {
			throw new GFSException(" head[1] is Command value!! ");
        }
		
//		boolean op = false; 
//		for (Command cmd : Command.values()) {
//			if ((byte)cmd.command == head[1]) {
//				op = true;
//				break;
//			}
//		}
//		
//		if (!op) {
//			throw new GFSException(" head[1] is Command value!! ");
//		}
		
		if (head[2] == 0) {
			throw new GFSException(" head[2] not zero!! ");
		}
		
		return byte2int(head[4], head[5], head[6], head[7]);
	}
}
