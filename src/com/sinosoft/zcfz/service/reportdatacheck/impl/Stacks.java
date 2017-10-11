package com.sinosoft.zcfz.service.reportdatacheck.impl;

import java.util.LinkedList;

public class Stacks 
{
	private LinkedList list=new LinkedList();     
	public int top=-1;     
	public void push(Object value)
	{     
      top++;     
      list.addFirst(value);     
    }     
	
   public Object pop(){     
      Object temp=list.getFirst();     
      top--;     
      list.removeFirst();     
      return temp;     
   
   }     
   public Object top(){     
   return list.getFirst();     
   }    

}