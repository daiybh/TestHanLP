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
						if (0x80 <= read && read <= 0xBF) // ��������BF���µģ�Ҳ����GBK
							break;
						if (0xC0 <= read && read <= 0xDF) {
							read = bis.read();
							if (0x80 <= read && read <= 0xBF)
								// ˫�ֽ� (0xC0 - 0xDF) (0x80 - 0xBF),Ҳ������GB������
								continue;
							else
								break;
						} else if (0xE0 <= read && read <= 0xEF) { // Ҳ�п��ܳ������Ǽ��ʽ�С
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
			// txt�ļ��Ŀ�ͷ���������ֽڣ��ֱ���FF��FE��Unicode��,
			// FE��FF��Unicode big endian��,EF��BB��BF��UTF-8��
			if (flag1 == 255 && flag2 == 254) {
				encode = "Unicode";
			} else if (flag1 == 254 && flag2 == 255) {
				encode = "UTF-16";
			} else if (flag1 == 239 && flag2 == 187 && flag3 == 191) {
				encode = "UTF8";
			} else {
				encode = "asci";// ASCII��
			}
			return encode;
		}
	}

	/**
	 * ͨ��·����ȡ�ļ������ݣ����������Ϊ�õ����ַ�����Ϊ���壬Ϊ����ȷ��ȡ�ļ��������룩��ֻ�ܶ�ȡ�ı��ļ�����ȫ������
	 */
	public static String readFile(String path) {
		String data = null;
		// �ж��ļ��Ƿ����
		File file = new File(path);
		if (!file.exists()) {
			return data;
		}
		// ��ȡ�ļ������ʽ
		String code = FileEncode.getFileEncode(path);
		InputStreamReader isr = null;
		try {
			// ���ݱ����ʽ�����ļ�
			if ("asci".equals(code)) {
				// �������GBK���룬�����û��������ʽ����Ϊ����Ĭ�ϱ��벻���ڲ���ϵͳ����
				// code = System.getProperty("file.encoding");
				code = "GBK";
			}
			isr = new InputStreamReader(new FileInputStream(file), code);
			// ��ȡ�ļ�����
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
