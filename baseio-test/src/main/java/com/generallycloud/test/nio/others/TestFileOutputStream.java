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
package com.generallycloud.test.nio.others;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import com.generallycloud.baseio.common.CloseUtil;

public class TestFileOutputStream {

	
	
	public static void main(String[] args) throws IOException {
		
		
		File file = new File("test.txt");
		
		
		FileOutputStream outputStream = new FileOutputStream(file);
		
		FileChannel channel = outputStream.getChannel();
		
		CloseUtil.close(channel);
		
		CloseUtil.close(outputStream);
		
	}
}
