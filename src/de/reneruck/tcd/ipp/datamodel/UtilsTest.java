package de.reneruck.tcd.ipp.datamodel;

import static org.junit.Assert.*;

import org.junit.Test;

import de.reneruck.tcd.datamodel.Utils;

public class UtilsTest {

	@Test
	public void testTrimArray() {
		byte[] input = new byte[]{60, 60, 91, 0,0,0,0,0,0,0,0,0,0,0};
		byte[] output = Utils.trimArray(input);
		assertArrayEquals(new byte[]{60, 60, 91}, output);
	}
}
