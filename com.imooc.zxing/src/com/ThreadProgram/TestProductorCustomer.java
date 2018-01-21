package com.ThreadProgram;

/**
 * @Title: TestProductorCustomer
 * @Description: 
 * 生产者、消费者问题 生产者（Productor）将产品交给店员（Clerk），而消费者(Customer)从店员取走产品
 *店员一次只能持有固定数量的产品（比如：20），如果生产者试图生产更多的产品，店员会叫停一下生产者
 *如果店内有空位置存放产品了再通知生产者生产产品，如果店中没有产品了，店员会告诉消费者等一下
 *如果店中有产品再通知消费者来取走产品
 * 
 *               分析：
 * 
 *               1、是否涉及到多线程问题？是！生产者、消费者 
 *               2、是否涉及到共享数据？是！考虑线程的安全问题
 *               3、此共享数据是谁？即为产品的数量 
 *               4、是否涉及到线程的通信问题？有！存在生产者与消费者的通信
 * @date 2017年5月21日
 */
class Clerk {
	int count=0;

	public synchronized void addProduct() {
		if (count >= 20) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			count++;
			System.out.println(Thread.currentThread() + "生产第：" + count + "个产品");
			notifyAll();
		}
	}

	public synchronized void consumeProduct() {
		if (count <= 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println(Thread.currentThread() + "消费第：" + count + "个产品");
			count--;
			notifyAll();
		}
	}

}

class Productor implements Runnable {
	Clerk clerk;

	public Productor(Clerk clerk) {
		this.clerk = clerk;
	}

	public void run() {
		while (true) {
			try {
				Thread.currentThread().sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			clerk.addProduct();
		}
	}
}

class Cus implements Runnable {
	Clerk clerk;

	public Cus(Clerk clerk) {
		this.clerk = clerk;
	}

	public void run() {
		while (true) {
			try {
				Thread.currentThread().sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			clerk.consumeProduct();
		}
	}
}

public class TestProductorCustomer {

	public static void main(String[] args) {
		Clerk clerk=new Clerk();
		Productor productor=new Productor(clerk);
		Cus cus=new Cus(clerk);
		Thread p1=new Thread(productor);
		Thread p2=new Thread(productor);
		Thread c=new Thread(cus);
		p1.setName("生产者1");
		p2.setName("生产者2");
		c.setName("消费者");
		p1.start();
		p2.start();
		c.start();
	}

}
