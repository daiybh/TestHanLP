package com.Fenci;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class TextFileReader {
	public static class FileEncode {
		static String getFileEncode(String path) {
			String charset = "asci";
			byte[] first3Bytes = new byte[3];
			BufferedInputStream bis = null;
			try {
				boolean checked = false;
				bis = new BufferedInputStream(new FileInputStream(path));
				bis.mark(0);
				int read = bis.read(first3Bytes, 0, 3);
				if (read == -1)
					return charset;
				if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
					charset = "Unicode";// UTF-16LE
					checked = true;
				} else if (first3Bytes[0] == (byte) 0xFE && first3Bytes[1] == (byte) 0xFF) {
					charset = "Unicode";// UTF-16BE
					checked = true;
				} else if (first3Bytes[0] == (byte) 0xEF && first3Bytes[1] == (byte) 0xBB
						&& first3Bytes[2] == (byte) 0xBF) {
					charset = "UTF8";
					checked = true;
				}
				bis.reset();
				if (!checked) {
					int len = 0;
					int loc = 0;
					while ((read = bis.read()) != -1) {
						loc++;
						if (read >= 0xF0)
							break;
						if (0x80 <= read && read <= 0xBF) // 单独出现BF以下的，也算是GBK
							break;
						if (0xC0 <= read && read <= 0xDF) {
							read = bis.read();
							if (0x80 <= read && read <= 0xBF)
								// 双字节 (0xC0 - 0xDF) (0x80 - 0xBF),也可能在GB编码内
								continue;
							else
								break;
						} else if (0xE0 <= read && read <= 0xEF) { // 也有可能出错，但是几率较小
							read = bis.read();
							if (0x80 <= read && read <= 0xBF) {
								read = bis.read();
								if (0x80 <= read && read <= 0xBF) {
									charset = "UTF-8";
									break;
								} else
									break;
							} else
								break;
						}
					}
					// TextLogger.getLogger().info(loc + " " + Integer.toHexString(read));
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (bis != null) {
					try {
						bis.close();
					} catch (IOException ex) {
					}
				}
			}
			return charset;
		}

		private static String getEncode(int flag1, int flag2, int flag3) {
			String encode = "";
			// txt文件的开头会多出几个字节，分别是FF、FE（Unicode）,
			// FE、FF（Unicode big endian）,EF、BB、BF（UTF-8）
			if (flag1 == 255 && flag2 == 254) {
				encode = "Unicode";
			} else if (flag1 == 254 && flag2 == 255) {
				encode = "UTF-16";
			} else if (flag1 == 239 && flag2 == 187 && flag3 == 191) {
				encode = "UTF8";
			} else {
				encode = "asci";// ASCII码
			}
			return encode;
		}
	}

	/**
	 * 通过路径获取文件的内容，这个方法因为用到了字符串作为载体，为了正确读取文件（不乱码），只能读取文本文件，安全方法！
	 */
	public static String readFile(String path) {
		String data = null;
		// 判断文件是否存在
		File file = new File(path);
		if (!file.exists()) {
			return data;
		}
		// 获取文件编码格式
		String code = FileEncode.getFileEncode(path);
		InputStreamReader isr = null;
		try {
			// 根据编码格式解析文件
			if ("asci".equals(code)) {
				// 这里采用GBK编码，而不用环境编码格式，因为环境默认编码不等于操作系统编码
				// code = System.getProperty("file.encoding");
				code = "GBK";
			}
			isr = new InputStreamReader(new FileInputStream(file), code);
			// 读取文件内容
			int length = -1;
			char[] buffer = new char[1024];
			StringBuffer sb = new StringBuffer();
			while ((length = isr.read(buffer, 0, 1024)) != -1) {
				sb.append(buffer, 0, length);
			}
			data = new String(sb);
		} catch (Exception e) {
			e.printStackTrace();
			// log.info("getFile IO Exception:"+e.getMessage());
		} finally {
			try {
				if (isr != null) {
					isr.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				// log.info("getFile IO Exception:"+e.getMessage());
			}
		}
		return data;
	}
}
