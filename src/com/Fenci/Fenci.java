package com.Fenci;

import java.io.BufferedReader;

import java.io.File;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;

public class Fenci {

	Map<String, Integer> map = new HashMap<String, Integer>();

	

	void pushToMap(String str) {
		if (map.get(str) == null) {
			map.put(str, 1);
		}
		else {
			map.put(str, map.get(str) + 1);
		}
	}

	private void doFenciByHanlp(String filePath) {
		String xx = TextFileReader.readFile(filePath);
		List<Term> vRet = HanLP.segment(xx);

		for (Term term : vRet) {

			String str = term.word;
			pushToMap(str.toString());
		}
	}

	public void doFenci(String filePath) throws IOException {
		map.clear();
		doFenciByHanlp(filePath);

	}

	private void doFenci_byLocal(String filePath) throws IOException {

		String str = "";

		int tempbyte = 0;

		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath)), "utf-8"));

		while ((tempbyte = in.read()) != -1) {

			if ((tempbyte >= 65 && tempbyte <= 90) || (tempbyte >= 97 && tempbyte <= 122)) {

				str += (char) tempbyte;

			}

			else if (!str.equals("")) {

				System.out.println("--->[" + str + "]");

				if (map.get(str) == null) {

					map.put(str, 1);

				}

				else {

					map.put(str, map.get(str) + 1);

				}

				str = "";

			}

		}

	}

	public String getResult() {
		String result = "";
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			result += "\t" + entry.getKey() + "\t : \t" + entry.getValue() + "\n";
		}
		return result;
	}

}
