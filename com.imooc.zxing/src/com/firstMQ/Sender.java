package com.firstMQ;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.spring.ActiveMQConnectionFactory;

public class Sender {
	//默认连接用户名
	private static final String USERNAME=ActiveMQConnection.DEFAULT_USER;
	//默认密码
	private static final String PASSWORD=ActiveMQConnection.DEFAULT_PASSWORD;
	//默认路径
	private static final String URL=ActiveMQConnection.DEFAULT_BROKER_URL;
	
	private static final int SUMNUM=10;
	
	public static void main(String[] args) {
		//连接工厂
		ConnectionFactory connectionFactory;
		//连接
		Connection connection=null;
		//会话 发消息或者接受消息的线程
		Session session;
		//消息目的地
		Destination destination;
		//消息生产者
		MessageProducer messageProducer;
		
			
		try {
			//创建工厂连接
			connectionFactory=new org.apache.activemq.ActiveMQConnectionFactory(Sender.USERNAME, Sender.PASSWORD, Sender.URL);
			//创建连接
			connection=connectionFactory.createConnection();
			//打开连接
			connection.start();
			
			//创建会话
			session=connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
			
			//创建一个消息队列为firstQueue
			destination=session.createQueue("firstQueue");
			//创建消息生产者
			messageProducer= session.createProducer(destination);
			
			//设置消息不持久
			messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			
			//发送消息
			sendMessage(session,messageProducer);
		
			session.commit();
			
		} catch (JMSException e) {
			e.printStackTrace();
		}finally{
			if (connection!=null)
				try {
					connection.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
		}

	}
	
	private static void sendMessage(Session session,MessageProducer messageProducer ){
		for (int i = 0; i < SUMNUM; i++) {
			try {
				TextMessage textMessage= session.createTextMessage("Hello World"+i);
				System.out.println("ActiveMq 发送的消息"+i);
				messageProducer.send(textMessage);
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}

}
