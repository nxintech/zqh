package test.study;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * Created by Administrator on 2016/4/1.
 */
public class TestCacl {
    public static void main(String[] args) {
        String str = "12+(5+(6+(7+(30+8+4)+6*(2+16*4))+8)/6+2)*3";
//        String[] strArray = str.split("\\)|\\(|\\+|\\-|\\*|\\/");
//        int a = 0;
//        Stack test = new Stack();
//        test.push("30");
//        test.push("+");
//        test.push("20");
//        test.push("*");
//        test.push("5");
//        test.push("/");
//        test.push("2");
//        test.push("-");
//        test.push("2");
//        System.out.println(cal(test));


//        String str = "1+((6+((3+4)+6*(2+16))+8)*6+2)*3";
        int len = str.length();
        //总栈
        Stack firstStatck = new Stack();
        //变量栈
        Stack flagStatck = firstStatck;
//
        String flagStr = "";
        char flagChar;
        for (int i = 0; i < len; i++) {
            flagChar = str.charAt(i);
            if (flagChar == '(') {
                //创建栈，并放入总栈
                flagStatck = new Stack();
                firstStatck.push(flagStatck);
                flagStr = "";
            } else if (flagChar == ')') {
                if (flagStr != "") {
                    flagStatck.push(flagStr);
                }
                //出栈计算
                String calRet = cal((Stack)firstStatck.pop());
                //再次出栈
                Object secondPop = firstStatck.pop();
                if (secondPop instanceof  Stack) {
                    flagStatck = (Stack)secondPop;
                    //计算得到的值放入中间变量栈
                    flagStatck.push(calRet);
                    //中间栈放入总栈
                    firstStatck.push(flagStatck);
                } else {
                    flagStatck =  firstStatck;
                    flagStatck.push(secondPop);
                    flagStatck.push(calRet);
                }
                flagStr = "";
            } else if (flagChar == '+' || flagChar == '-' || flagChar == '*' || flagChar == '/') {
                //将数字放入栈
                if (flagStr != "") {
                    flagStatck.push(flagStr);
                }
                flagStatck.push(flagChar);
                flagStr = "";
            } else {
                //拼接字符串中的数字
                flagStr = flagStr + flagChar;
            }
        }
        if (flagStr != "") {
            flagStatck.push(flagStr);
        }
        System.out.println(cal(firstStatck));
        int a = 0;
    }

    /**
     * 计算栈的加减乘除
     * @param stack
     * @return
     */
    public static String cal(Stack stack) {
        //颠倒栈，按照从左到右计算
        Stack flagStack = new Stack();
        while (!stack.empty()) {
            flagStack.push(stack.pop());
        }
        //计算值
        String flag;
        List<String> caclList = new ArrayList<>();
        while (!flagStack.empty()) {
            flag = String.valueOf(flagStack.pop());
            if ("*".equals(flag)) {
                caclList.add(new BigDecimal(caclList.remove(caclList.size() - 1)).multiply(new BigDecimal(String.valueOf(flagStack.pop()))).toPlainString());
            } else if ("/".equals(flag)) {
                caclList.add(new BigDecimal(caclList.remove(caclList.size() - 1)).divide(new BigDecimal(String.valueOf(flagStack.pop())), 2, BigDecimal.ROUND_HALF_UP).toPlainString());
            } else {
                caclList.add(flag);
            }
        }
        //计算剩余的栈
        BigDecimal result = new BigDecimal("0");
        String lastOp = "+";//操作符号
        for (String str: caclList) {
            if ("+".equals(str)) {
                lastOp = "+";
            } else if ("-".equals(str)) {
                lastOp = "-";
            } else {
                if ("+".equals(lastOp)) {
                    result = result.add(new BigDecimal(str));
                } else {
                    result = result.subtract(new BigDecimal(str));
                }
            }
        }
        return  result.toPlainString();

    }
}
