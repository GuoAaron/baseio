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
package com.generallycloud.baseio.container.http11;

import java.io.IOException;

import com.generallycloud.baseio.component.SocketSession;
import com.generallycloud.baseio.protocol.ReadFuture;

public interface HttpSession {

	public abstract void active(SocketSession ioSession);

	public abstract void flush(ReadFuture future) throws IOException;

	public abstract long getCreateTime();

	public abstract SocketSession getIoSession();

	public abstract long getLastAccessTime();

	public abstract String getSessionID();
	
	public abstract boolean isValidate();
	
	public abstract HttpContext getContext();

}
