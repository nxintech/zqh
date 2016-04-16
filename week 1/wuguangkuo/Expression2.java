package com.nxin.znt.train.alg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Expression2 {
	
	private static Map<String, Integer> operatorPriorityMap = new HashMap<String, Integer>();
	
	static {
		operatorPriorityMap.put("(", 100);
		operatorPriorityMap.put("+", 1);
		operatorPriorityMap.put("-", 1);
		operatorPriorityMap.put("*", 2);
		operatorPriorityMap.put("/", 2);
	}

	public static void main(String[] args) {
		
		String expressionStr = "21 + 6 * (13 - 4/2)";
		
		//解析表达式
		Pattern expPattern = Pattern.compile("[0-9]+|\\+|\\-|\\*|/|\\(|\\)");
		
		//操作数和操作符入栈
		Matcher expMatcher = expPattern.matcher(expressionStr);
		Stack<String> operatorStack = new Stack<String>();
		List<String> tempList = new ArrayList<String>();
		Stack<String> tempStack = new Stack<String>();
		while (expMatcher.find()) {
			String element = expMatcher.group();
			
			if (")".equals(element)) {
				
				String operator;
				while (operatorStack.size() > 0 && !"(".equals((operator = operatorStack.pop()))) {
					tempList.add(operator);
				}
				
				for (int i = tempList.size() - 1; i >= 0; i--) {
					tempStack.push(tempList.get(i));
				}
				
				while (tempStack.size() > 1) {
					String num1 = tempStack.pop();
					String opt = tempStack.pop();
					String num2 = tempStack.pop();
					
					//System.out.println(num1 + ":" + num2 + ":" + opt);
					
					String tempResult = caculate(Integer.valueOf(num1), Integer.valueOf(num2), opt);
					tempStack.push(tempResult);
				}
				
				operatorStack.push(tempStack.pop());
			} else {
				operatorStack.push(element);
			}
		}
		
		while (operatorStack.size() > 1) {
			String num1 = operatorStack.pop();
			String opt = operatorStack.pop();
			String num2 = operatorStack.pop();
			
			String tempResult = caculate(Integer.valueOf(num1), Integer.valueOf(num2), opt);
			operatorStack.push(tempResult);
		}
		
		System.out.println("result=" + operatorStack.pop());
	}
	
	static String caculate(Integer num1, Integer num2, String opt) {
		String resultNum = "";
		if ("+".equals(opt)) {
			resultNum = (num1 + num2) + "";
		} else if ("-".equals(opt)) {
			resultNum = (num2 - num1) + "";
		} else if ("*".equals(opt)) {
			resultNum = (num1 * num2) + "";
		} else if ("/".equals(opt)) {
			resultNum = (num2 / num1) + "";
		}
		
		return resultNum;
	}

}
