package com.ThreadProgram;

class Thread1 implements Runnable {
	int ticket = 100;
	public void run() {
		while (true) {
			synchronized (this) {
				if (ticket > 0) {
					// try {
					// Thread.currentThread().sleep(10);
					// } catch (InterruptedException e) {
					// e.printStackTrace();
					// }
					System.out.println(Thread.currentThread().getName() + ":票号" + ticket--);
				} else {
					break;
				}
			}
		}
	}
}

class Thread2 extends Thread {
	static int ticket = 1;

	public void run() {
		while (true) {
			if (ticket <= 10) {
				System.out.println(Thread.currentThread().getName() + ":票号" + ticket++);
			} else {
				break;
			}
		}
	}
}

public class Test {

	public static void main(String[] args) {
		/*
		 * Thread2 t1=new Thread2(); Thread2 t2=new Thread2(); Thread2 t3=new
		 * Thread2(); Thread2 t4=new Thread2(); Thread2 t5=new Thread2();
		 * Thread2 t6=new Thread2(); Thread2 t7=new Thread2(); Thread2 t8=new
		 * Thread2(); Thread2 t9=new Thread2(); Thread2 t10=new Thread2();
		 * t10.start(); t9.start(); t1.start(); t2.start(); t3.start();
		 * t4.start(); t5.start(); t6.start(); t7.start(); t8.start();
		 * thread1.setName("线程1："); thread1.start();
		 * Thread.currentThread().setName("=========主线程"); for(int
		 * i=0;i<10;i++){ if(i%2==0){ Thread.currentThread().yield(); }
		 * System.out.println(Thread.currentThread().getName()+":"+i); }
		 */
	}
}
