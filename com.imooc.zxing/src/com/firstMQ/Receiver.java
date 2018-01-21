package com.firstMQ;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;

public class Receiver {
	
	//默认连接用户名
	private static final String USERNAME=ActiveMQConnection.DEFAULT_USER;
	//默认密码
	private static final String PASSWORD=ActiveMQConnection.DEFAULT_PASSWORD;
	//默认路径
	private static final String URL=ActiveMQConnection.DEFAULT_BROKER_URL;

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
		MessageConsumer messageConsumer;
			
		try {
			//创建工厂连接
			connectionFactory=new org.apache.activemq.ActiveMQConnectionFactory(Receiver.USERNAME, Receiver.PASSWORD, Receiver.URL);
			//创建连接
			connection=connectionFactory.createConnection();
			//打开连接
			connection.start();
			
			//创建会话
			session=connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
			
			//创建一个消息队列为firstQueue
			destination=session.createQueue("firstQueue");
			//创建消息接收者
			messageConsumer= session.createConsumer(destination);
			
			//接收消息
			receiverMessage(messageConsumer);
		
			
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
	
	private static void receiverMessage(MessageConsumer messageConsumer ){
			try {
				while(true){
				TextMessage textMessage= (TextMessage) messageConsumer.receive(1000);
				if (null!=textMessage) {
					System.out.println("ActiveMq接收到的消息:"+textMessage.getText());
				}else {
					return;
				}
				}	
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
}
