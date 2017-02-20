/*
 * Copyright 2015-2017 GenerallyCloud.com
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
package com.generallycloud.nio.connector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.locks.ReentrantLock;

import com.generallycloud.nio.TimeoutException;
import com.generallycloud.nio.common.CloseUtil;
import com.generallycloud.nio.common.LifeCycleUtil;
import com.generallycloud.nio.common.ThreadUtil;
import com.generallycloud.nio.component.AbstractChannelService;
import com.generallycloud.nio.component.concurrent.Waiter;
import com.generallycloud.nio.configuration.ServerConfiguration;

public abstract class AbstractChannelConnector extends AbstractChannelService implements ChannelConnector {

	protected long			timeout		= 3000;
	
	@Override
	public void close() throws IOException {
		
		Waiter<IOException> waiter = asynchronousClose();
		
		if(waiter.await()){
			//FIXME never timeout
			throw new TimeoutException("timeout to close");
		}
	}
	
	protected void cancelService() {

		ReentrantLock lock = this.activeLock;

		lock.lock();

		try {
			// just close
			this.destroyChannel();

			LifeCycleUtil.stop(getContext());

			shutDownWaiter.setPayload(null);

		} finally {

			active = false;

			lock.unlock();
		}
	}
	
	@Override
	public Waiter<IOException> asynchronousClose() {
		if (getSession() == null) {
			doPhysicalClose();
			return shutDownWaiter;
		}
		CloseUtil.close(getSession());
		return shutDownWaiter;
	}
	
	//FIXME __考虑手动关闭 connector
	public void physicalClose() throws IOException {

		if (canSafeClose()) {
			doPhysicalClose();
			return;
		}

		ThreadUtil.execute(new Runnable() {
			@Override
			public void run() {
				doPhysicalClose();
			}
		});
	}
	
	protected abstract boolean canSafeClose();

	private void doPhysicalClose(){
		cancelService();
	}
	
	private void initService(ServerConfiguration configuration) throws IOException {
		
		String SERVER_HOST = configuration.getSERVER_HOST();
		
		int SERVER_PORT = configuration.getSERVER_PORT();
		
		this.serverAddress = new InetSocketAddress(SERVER_HOST, SERVER_PORT);
		
		this.connect(getServerSocketAddress());
		
	}
	
	protected abstract void connect(InetSocketAddress socketAddress) throws IOException;

	@Override
	public boolean isConnected() {
		return getSession() != null && getSession().isOpened();
	}

	@Override
	public boolean isActive() {
		return isConnected();
	}

	@Override
	public long getTimeout() {
		return timeout;
	}

	@Override
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
	
	protected abstract void initChannel() throws IOException;
	
	protected abstract void destroyChannel();

	protected void service() throws IOException {

		ReentrantLock lock = this.activeLock;

		lock.lock();

		try {

			if (active) {
				return;
			}

			if (getContext() == null) {
				throw new IllegalArgumentException("null nio context");
			}
			
			getContext().setChannelService(this);

			ServerConfiguration configuration = getContext().getServerConfiguration();

			configuration.setSERVER_CORE_SIZE(1);

			LifeCycleUtil.start(getContext());

			this.initChannel();

			this.initService(configuration);

			this.active = true;

		} finally {

			lock.unlock();
		}
	}
	
}
