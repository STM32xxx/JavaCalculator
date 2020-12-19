/**
 * 混合运算类
 */

import java.util.ArrayList;
import java.util.Stack;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.math.BigDecimal;

class MixedOperation{ //混合运算类
    private final int accuracy; //保存设置的运算精度
    // private final Pattern p = Pattern.compile("(^-|(?<=\\D)-)?[\\d.]+|[+\\-*\\/%!()√^πe]|log|ln|a?sin|a?cos|a?tan"); //用于匹配运算符和参数的正则表达式
    private final Pattern p = Pattern.compile("(^-|(?<=\\D)-)?[\\d.]+|[+\\-*\\/%!()√^]|log"); //用于匹配运算符和参数的正则表达式
    private final HashMap<String,Integer> prio = new HashMap<String,Integer>(); //存放运算符对应的优先级，查字典判断运算符优先级
    private ArrayList<String> eq1 = new ArrayList<String>(); //保存算式的列表
    private ArrayList<String> eq2 = new ArrayList<String>(); //保存算式的中间列表
    private Stack<String> stk = new Stack<String>(); //使用该堆栈将中缀表达式转为后缀表达式，这儿的Stack类型可以改成使用成BigDecimal
    // public BigDecimalMath bdm = new BigDecimalMath(34); //精确到小数点后34位
    public BigDecimalMath bdm;

    MixedOperation(int accuracy){ //构造函数，初始化运算符优先级及精度
        this.accuracy = accuracy;
        bdm = new BigDecimalMath(accuracy); //调用BigDecimal的构造函数设置运算精度

        prio.put("(",0); //左括号优先级最低，但是遇到左括号直接入栈
        prio.put(")",0); //不会用到右括号的优先级，因为右括号不可能入栈，在此只把右括号当作一个运算符
        
        //双目运算符
        prio.put("+",1); //加法优先级为1
        prio.put("-",1); //减法优先级为1
        prio.put("*",2); //乘法优先级为2
        prio.put("/",2); //除法优先级为2
        prio.put("%",2); //取余优先级为2
        prio.put("^",3); //n次方优先级为3
        prio.put("√",4); //开根号优先级为4
        prio.put("log",4); //log优先级为4
    }

    //预处理，将运算参数提取出来保存为单独一个字符串
    //比如输入"1.23+4.56"，整体为一个字符串，经过处理后被分割成三个字符串"1.23","+","4.56"
    private void pretreatment(String s){ 
        Matcher m = p.matcher(s); //将输入的算式s与正则表达式进行匹配

        while(m.find()){
            eq1.add(m.group()); //保存算式中运算符的位置
        }
    }

    //中缀表达式转后缀表达式
    //比如输入为1+5*7，转为后缀表达式为157*+
    private void Infix2Postfix(){ 
        for(int i=0;i<eq1.size();i++){
            if(prio.get(eq1.get(i)) != null){ //对运算符进行处理
                String c = eq1.get(i);
                if(stk.empty() || c.equals("(")){ //如果栈为空或者是左括号，直接入栈。为什么左括号也要直接入栈？考虑到括号嵌套问题，如6+((1+2)*3+4)*5
                    stk.push(c);
                }
                else if(c.equals(")")){ //遇到右括号，将栈顶元素一直出栈直到遇到左括号为止，并将该左括号出栈
                    while(!stk.peek().equals("(")) //栈顶元素不为左括号，将该运算符出栈并添加到列表中
                        eq2.add(stk.pop());
                    stk.pop(); //将左括号出栈
                }
                else if(prio.get(c) > prio.get(stk.peek())){//如果该运算符优先级高于栈顶运算符优先级,直接入栈
                    stk.push(c);
                }
                else{ //该运算符优先级低于或等于栈顶运算符优先级，分四种情况
                    while(!stk.empty() && !stk.peek().equals("(") && prio.get(stk.peek())>=prio.get(c)){
                        eq2.add(stk.pop());
                    }
                    stk.push(c);
                }
            }
            else{ //对参数进行处理，参数直接添加进列表
                eq2.add(eq1.get(i));
            }
        }
        while(!stk.empty()){
            eq2.add(stk.pop());
        }
    }

    //对后缀表达式计算
    private String postfixCalculate(){ 
        BigDecimal p1,p2;
        BigDecimal p3 = new BigDecimal("0");

        for(int i=0;i<eq2.size();i++){
            if(prio.get(eq2.get(i)) == null){ //对参数进行处理
                stk.push(eq2.get(i)); //参数直接入栈
            }
            else{ //对运算符进行处理
                String c = eq2.get(i);
                p1 = new BigDecimal(stk.pop());
                p2 = new BigDecimal(stk.pop());

                switch(c){
                    case "+":p3 = p2.add(p1);break;

                    case "-":p3 = p2.subtract(p1);break;

                    case "*":p3 = p2.multiply(p1);break;

                    case "/":
                        p3 = p2.divide(p1,32,BigDecimal.ROUND_HALF_EVEN);
                        break;

                    case "%":p3 = p2.remainder(p1);break;

                    case "^":
                        p3 = bdm.pow(p2,p1);
                        break;

                    case "√":
                        p3 = bdm.pow(p1,BigDecimal.ONE.divide(p2,bdm.getAccuracy(),BigDecimal.ROUND_HALF_EVEN));
                        break;
                        
                    case "log":
                        p3 = bdm.log(p1,p2);
                        break;

                    default:p3=BigDecimal.ZERO;break;
                }
                stk.push(p3.toString());
            }
        }
        return stk.pop();
    }

    //调用该函数计算结果
    public BigDecimal getMixedOperationRes(String s){
        //运算前进行一些初始化操作，清空所用的列表和堆栈
        eq1.clear();
        eq2.clear();
        while(!stk.empty()){
            stk.pop();
        }

        pretreatment(s);
        Infix2Postfix();
        return new BigDecimal(postfixCalculate());
    }
}
