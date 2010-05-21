//
// Non-Export Controlled Information
//
//####################################################################
//## The MIT License
//## 
//## Copyright (c) 2010 Harris Corporation
//## 
//## Permission is hereby granted, free of charge, to any person
//## obtaining a copy of this software and associated documentation
//## files (the "Software"), to deal in the Software without
//## restriction, including without limitation the rights to use,
//## copy, modify, merge, publish, distribute, sublicense, and/or sell
//## copies of the Software, and to permit persons to whom the
//## Software is furnished to do so, subject to the following conditions:
//## 
//## The above copyright notice and this permission notice shall be
//## included in all copies or substantial portions of the Software.
//## 
//## THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
//## EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
//## OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
//## NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
//## HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
//## WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
//## FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
//## OTHER DEALINGS IN THE SOFTWARE.
//## 
//####################################################################
//********************************************************************
// FILE: Connector.java
//
// Copyright (C) 2010 Harris Corporation. All rights reserved.
//
// CLASSIFICATION: Unclassified
//
// DESCRIPTION: Connector.java 
//              
//
// LIMITATIONS: None
//
// SOFTWARE HISTORY:
//
//> Feb 24, 2010 PTR#  - R. Robinson
// Initial Coding.
//<
//********************************************************************
package gov.hhs.fha.nhinc.lift.proxy.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class Connector implements Runnable
{
	private final InputStream in;
	private final OutputStream out;
	private final int bufferSize;
	
	public Connector(InputStream in, OutputStream out, int bufferSize) {
		super();
		this.in = in;
		this.out = out;
		this.bufferSize = bufferSize;
	}
	
	public void run()
	{
		System.out.println("Connector started.");
		
		byte[] b = new byte[bufferSize];
		
		try {
			while(true)
			{
				// Very slow, but works
//				int next = in.read();
//				out.write(next);
				
				//For debugging
//				System.out.print((char)next);
				
				// Might be better
				int length = in.read(b);
				
				System.out.println(length);
				
				if(length < 0)
				{
					break;
				}
				
				out.write(b, 0, length);
				
//				if(length >= 0)
//				{
//					out.write(b, 0, length);
//				}else{
//					out.write(-1);
//					break;
//				}
				
//				if(next == -1)
//					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Connector stopping.");
	}
}