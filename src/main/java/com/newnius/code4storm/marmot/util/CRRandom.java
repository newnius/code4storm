package com.newnius.code4storm.marmot.util;

import java.util.List;
import java.util.Random;

public class CRRandom {
	public static int nextInt() {
		return new Random().nextInt();
	}

	public static int nextInt(int start, int stop) {
		return new Random().nextInt() % (stop - start) + start;
	}

	public static <T> T select(List<T> list) {
		int index = nextInt(0, list.size() - 1);
		return list.get(index);
	}

}
