/**
 * 
 */
package com.gome.cloud.protocol;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.gome.cloud.exception.GFSException;
import com.google.common.io.ByteStreams;

/**
 * @author blaiu
 *
 */
public class CompressUtil {
	public static byte[] compress(byte[] data) {
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            GZIPOutputStream gos = new GZIPOutputStream(output);
            ByteStreams.copy(input, gos);
            gos.finish();
            gos.flush();
            gos.close();
        } catch (Exception e) {
            throw new GFSException(e);
        }
        return output.toByteArray();
    }

    public static byte[] decompress(byte[] data) {
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            GZIPInputStream gis = new GZIPInputStream(input);
            ByteStreams.copy(gis, output);
            gis.close();
        } catch (Exception e) {
            throw new GFSException(e);
        }
        return output.toByteArray();
    }
}
