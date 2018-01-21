package com.ThreadProgram;

/**
 * @Title: Singleton
 * @Description:单例模式-饿汉模式
 * @date 2017年5月21日
 */
class Singleton {
	private Singleton() {
	}

	private static Singleton instance = null;

	public static Singleton getInstnce() {
		if (instance == null) {
			synchronized (Singleton.class) {
				if (instance == null) {
					instance = new Singleton();
				}
			}
		}
		return instance;
	}
}

public class TestSingleton {
	public static void main(String[] args) {
		Singleton s1=Singleton.getInstnce();
		Singleton s2=Singleton.getInstnce();
		System.out.println(s1==s2);
	}
}