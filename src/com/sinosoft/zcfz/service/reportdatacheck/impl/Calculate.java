package com.sinosoft.zcfz.service.reportdatacheck.impl;

public class Calculate {
    // 判断是否为操作符号    
   public static boolean isOperator(String operator) {    
       if (operator.equals("+") || operator.equals("-")    
               || operator.equals("*") || operator.equals("/")    
               || operator.equals("(") || operator.equals(")")
               // || operator.equals("T") || operator.equals("S")
               // || operator.equals("C") || operator.equals("L")
               
          )    
           return true;    
       else   
           return false;    
   }    
  
   // 设置操作符号的优先级别
   //这里要记住，操作数越少，优先级就越高，一点小心得，给大家分享一下吧
   //还有，这里的“）”优先级是最低的，虽然没写明，但是能看的出来
  public static int priority(String operator) 
   {    
       if (operator.equals("+") || operator.equals("-")    
               || operator.equals("("))    
           return 1;    
       else if (operator.equals("*") || operator.equals("/"))    
           return 2;    
//       else if (operator.equals("S") || operator.equals("C")||operator.equals("T") || operator.equals("L"))    
//           return 3; 
       else
           return 0;    
   }    
  
   // 有两个操作数的函数运算    
   public static String twoResult(String operator, String a, String b) {    
       try {    
           String op = operator;    
           String rs = new String();    
           double x = Double.parseDouble(b);    
           double y = Double.parseDouble(a);    
           double z = 0;    
           if (op.equals("+"))    
               z = x + y;    
           else if (op.equals("-"))    
               z = x - y;    
           else if (op.equals("*"))    
               z = x * y;    
           else if (op.equals("/"))    
               z = x / y;    
           else   
               z = 0;    
           return rs + z;    
       } catch (NumberFormatException e) {    
       	e.printStackTrace();
       	return "Error";  
       }    
   } 
  /* // 有一个操作数的函数运算
   public static String oneResult(String operator, String a) {    
       try {    
           String op = operator;    
           String rs = new String();    
           double x = Double.parseDouble(a);
           double z = 0;    
           if (op.equals("S"))    
               z = Math.sin(x); 
           else if (op.equals("C"))    
               z = Math.cos(x) ;    
           else if (op.equals("T"))    
               z =Math.tan(x) ;    
           else if (op.equals("L"))    
               z = Math.log(x);    
           else   
               z = 0;    
           return rs + z;    
       } catch (NumberFormatException e) {    
       	e.printStackTrace();     
           return "Error";    
       }    
   }
*/

}