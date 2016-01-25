package com.gifisan.mtp.jms.client;

import com.gifisan.mtp.jms.JMSException;

public interface JMSConnection {

	public abstract void login(String username, String password) throws JMSException;

	public abstract void logout();

}
