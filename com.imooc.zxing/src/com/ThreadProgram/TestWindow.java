package com.ThreadProgram;

/**
 * @Title: TestWindow
 * @Description: 模拟售票窗口，总票数为100PICS
 * @date 2017年5月20日
 */

public class TestWindow {
	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis() + ":售票开始");
		Thread1 t = new Thread1();
		Thread t1 = new Thread(t);
		Thread t2 = new Thread(t);
		Thread t3 = new Thread(t);
		t1.setName("窗口1：");
		t2.setName("窗口2：");
		t3.setName("窗口2：");
		t1.start();
		t2.start();
		t3.start();
	}

}
