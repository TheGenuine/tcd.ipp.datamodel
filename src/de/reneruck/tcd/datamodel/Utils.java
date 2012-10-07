package de.reneruck.tcd.datamodel;

import java.util.Arrays;

public class Utils {

	public static byte[] trimArray(byte[] data) {
		int i = data.length-1;
		for (; i > 0; i--) {
			if(data[i] != 0) {
				break;
			}
		}
		return Arrays.copyOfRange(data, 0, i+1);
	}
}
