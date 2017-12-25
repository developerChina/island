package utils;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class FileLockUtil {

	public static void main(String[] args) {
		String data = FileLockUtil.readByLines(System.getProperty("user.dir") + "\\bin\\res\\Sequence.txt");
		System.out.println(data);
		FileUtil.saveAs("﻿1,京B02128,2017-12-24 21:37:24,1",System.getProperty("user.dir") + "\\bin\\res\\Sequence.txt");
		
	}

	public static void saveAs(String content, String path) {
		RandomAccessFile raf = null;
		FileChannel fc = null;
		FileLock fl = null;
		try {
			raf = new RandomAccessFile(path, "rw");
			fc = raf.getChannel();
			fl = fc.tryLock();
			if (content != null) {
				fc.write(getByteBuffer(content));
			}
			fl.release();
			fc.close();
			raf.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fl != null && fl.isValid()) {
				try {
					fl.release();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static String readByLines(String path) {
		RandomAccessFile raf = null;
		FileChannel fc = null;
		FileLock fl = null;
		StringBuffer sb = new StringBuffer();
		try {
			raf = new RandomAccessFile(path, "rw");
			fc = raf.getChannel();
			fl = fc.tryLock(0, Long.MAX_VALUE, true);

			ByteBuffer bb = ByteBuffer.allocate(2048);
			while ((fc.read(bb)) != -1) {
				sb.append(new String(bb.array()));
				bb.clear();
			}
			fl.release();
			fc.close();
			raf.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fl != null && fl.isValid()) {
				try {
					fl.release();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}

	public static ByteBuffer getByteBuffer(String str) {
		return ByteBuffer.wrap(str.getBytes());
	}
}
