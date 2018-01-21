package com.ThreadProgram;

/**
 * @Title: TestAccount
 * @Description:两个用户同时往一个账户里面存钱，一个用户存完钱以后，打印余额
 * @date 2017年5月20日
 */

class Account {
	double blance;

	public Account() {
	}

	public synchronized void deposit(double amt) {
		notify();
		blance += amt;
		System.out.println(Thread.currentThread().getName() + ":" + blance);
		try {
			wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

class Customer extends Thread {
	Account account;

	public Customer(Account account) {
		this.account = account;
	}

	public void run() {
		for (int i = 1; i < 4; i++) {
			account.deposit(1000);
		}
	}
}

public class TestAccount {
	public static void main(String[] args) {
		Account account = new Account();
		Customer ca = new Customer(account);
		Customer cb = new Customer(account);
		// Thread ca=new Thread(c);
		// Thread cb=new Thread(c);
		ca.setName("客户甲：");
		cb.setName("客户乙：");
		ca.start();
		cb.start();
	}
}
