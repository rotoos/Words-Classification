package com.kakao.work.verifier;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexBasedVerifier implements StringVerifier {
	private Pattern pattern;
	
	public RegexBasedVerifier(String regex) {
		this.pattern = Pattern.compile(regex);
	}
	
	/**
	 * String ����ǥ���� ����
	 */
	@Override
	public boolean verify(String value) {
		Matcher matcher = pattern.matcher(value);
		return matcher.find();
	}
}
