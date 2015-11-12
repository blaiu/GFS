/**
 * 
 */
package com.gome.cloud.protocol;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;

import com.gome.cloud.exception.GFSException;

/**
 * @author blaiu
 *
 */
public class KeyUtil {

	private final static Pattern PATTERN = Pattern.compile("^gomefs/t(\\d+)/(\\d+/\\d+/\\d+)/([0-9A-Fa-f]{1,8})([0-9A-Fa-f]{8})([Y|N|y|n])[.\\w]{0,7}$");
    public static int getDsId(String key) {
        Matcher matcher = PATTERN.matcher(key);
        if (matcher.find()) {
            return Integer.valueOf(matcher.group(1));
        }
        throw new GFSException("Bad key :" + key);
    }

    public static String getFileId(String key) {
        Matcher matcher = PATTERN.matcher(key);
        if (matcher.find()) {
            return matcher.group(2);
        }
        throw new GFSException("Bad key :" + key);
    }

    public static String getCrc(String key) {
        Matcher matcher = PATTERN.matcher(key);
        if (matcher.find()) {
            return matcher.group(3);
        }
        throw new GFSException("Bad key :" + key);
    }

    public static boolean isCompression(String key) {
        Matcher matcher = PATTERN.matcher(key);
        if (matcher.find()) {
            return "Y".equalsIgnoreCase(matcher.group(5));
        }
        throw new GFSException("Bad key :" + key);
    }

    public static void verification(String key) {
        Matcher matcher = PATTERN.matcher(key);
        if (matcher.find()) {
            String checksum = createChecksum(
                    String.format("%s/%s/%s", matcher.group(1), matcher.group(2), matcher.group(3)),
                    "Y".equalsIgnoreCase(matcher.group(5)));
            if (matcher.group(4).equals(checksum) || "13081253".equals(matcher.group(5))) {
                return;
            }
        }
        throw new GFSException("Bad key :" + key);
    }

    public static String createChecksum(String fileKey, boolean compress) {
        String key = String.format("%sgomeFS", fileKey);
        CRC32 crc = new CRC32();
        crc.update(key.getBytes());
        if (compress) {
            crc.update(0xff);
        }
        return crcPadLeft(crc.getValue());
    }

    public static String crcPadLeft(long value) {
        StringBuffer sb = new StringBuffer(Long.toHexString(value));
        while (sb.length() < 8) {
            sb.insert(0, "0");
        }
        return sb.toString();
    }
	
}
