/**
 * B_开头的变量为按钮变量
 * T_开头的变量为文本显示变量
 * S_开头的变量为字符串变量
 * 
 */

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.math.RoundingMode;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JFrame;
import javax.swing.JComponent;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

class CalculatorHMI extends JFrame{
    private MixedOperation eq = new MixedOperation(32); //混合运算类，设置精度为小数点后34位
    private GridBagConstraints gridBagConstraints = new GridBagConstraints(); //实例化该对象用来对组件进行管理
    private GridBagLayout gridBagLayout = new GridBagLayout(); //实例化布局对象
    private DecimalFormat df = new DecimalFormat(); //对字符串进行格式化，三位一组分割

    private boolean _2ndFlag = false; //记录当前切换到哪组按键
    private boolean DegRadFlag = true; //记录当前使用角度还是弧度
    private boolean calculateFlag = false; //如果上一步进行了计算，该标志为为true

    //第0行
    private JButton B_interface = new JButton("科学"); //计算器版本切换
    //第1行
    private JTextField T_Eq = new JTextField(""); //显示输入算式
    //第2行
    private JTextField T_Res = new JTextField("0"); //显示输出结果
    //第3行
    private JButton B_DegRad = new JButton("Deg"); //角度与弧度转换
    private JButton B_F_E = new JButton("F-E"); //普通数值与科学计数法转换
    private JButton B_M_down = new JButton("M↓"); //内存操作，暂时不用
    //第4行
    private JButton B_MC = new JButton("MC"); //内存操作，暂时不用
    private JButton B_MR = new JButton("MR"); //内存操作，暂时不用
    private JButton B_M_add = new JButton("M+"); //内存操作，暂时不用
    private JButton B_M_dec = new JButton("M-"); //内存操作，暂时不用
    private JButton B_MS = new JButton("MS"); //内存操作，暂时不用
    //第5行
    private JButton B_secondFunction = new JButton("2nd"); //切换按键第二功能
    private JButton B_sin = new JButton("sin"); //正弦函数
    private JButton B_cos = new JButton("cos"); //余弦函数
    private JButton B_tan = new JButton("tan"); //正切函数
    private JButton B_Exp = new JButton("Exp"); //指数函数
    //第6行
    private JButton B_sqrt = new JButton("2√x"); //开根号
    private JButton B_reciprocal = new JButton("1/x"); //倒数
    private JButton B_Mod = new JButton("Mod"); //取余
    private JButton B_CE = new JButton("CE"); //清空输入输出
    private JButton B_backspace = new JButton("←"); //退格，清除输入的最后一位
    //第7行
    private JButton B_square = new JButton("x^2"); //平方
    private JButton B_leftBracket = new JButton("("); //混合运算的左括号
    private JButton B_rightBracket = new JButton(")"); //混合运算的右括号
    private JButton B_factorial = new JButton("n!"); //阶乘
    private JButton B_div = new JButton("/"); //除号
    //第8行
    private JButton B_pow = new JButton("x^n"); //x的n次方
    private JButton B_7 = new JButton("7"); //数字7
    private JButton B_8 = new JButton("8"); //数字8
    private JButton B_9 = new JButton("9"); //数字9
    private JButton B_mul = new JButton("*"); //乘号
    //第9行
    private JButton B_log = new JButton("log"); //以10为底的对数
    private JButton B_4 = new JButton("4"); //数字4
    private JButton B_5 = new JButton("5"); //数字5
    private JButton B_6 = new JButton("6"); //数字6
    private JButton B_dec = new JButton("-"); //减号
    //第10行
    private JButton B_ln = new JButton("ln"); //以e为底的对数
    private JButton B_1 = new JButton("1"); //数字1
    private JButton B_2 = new JButton("2"); //数字2
    private JButton B_3 = new JButton("3"); //数字3
    private JButton B_add = new JButton("+"); //加号
    //第11行
    private JButton B_pi_e = new JButton("π"); //圆周率π或自然常数e
    private JButton B_negation = new JButton("+/-"); //正/负切换，取反
    private JButton B_0 = new JButton("0"); //数字0
    private JButton B_point = new JButton("."); //小数点
    private JButton B_equal = new JButton("="); //等号
    //输入输出字符串
    private String S_input = new String(""); //保存输入的算式
    private BigDecimal S_output = new BigDecimal("0"); //保存计算结果

    public CalculatorHMI(){
        df.setMaximumFractionDigits(32); //数值显示到小数点后32位
        df.setRoundingMode(RoundingMode.HALF_EVEN); //银行家舍入法
        df.setGroupingSize(3); //设置数字分组大小，对结果3位一体划分开

        T_Eq.setHorizontalAlignment(JTextField.RIGHT); //输入算式文本显示右对齐
        T_Res.setHorizontalAlignment(JTextField.RIGHT); //结果文本显示右对齐

        this.setSize(277,365); //界面大小
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(3);
        this.setResizable(false);
        this.setLayout(gridBagLayout); //窗体对象设置为GridBagLayout布局
        gridBagConstraints.fill = GridBagConstraints.BOTH; //该方法是为了设置如果组件所在的区域比组件本身要大时的显示情况 
        
        //第0行
        setControl(B_interface,0,0,2,1);
        //第1行
        setControl(T_Eq,0,1,5,1); //算式显示
        //第2行
        setControl(T_Res,0,2,5,1); //结果显示
        //第3行
        setControl(B_DegRad,0,3,1,1); //角度与弧度切换
        setControl(B_F_E,1,3,1,1); //未知功能，保留
        setControl(B_M_down,4,3,1,1); //M↓
        //第4行
        setControl(B_MC,0,4,1,1); //MC
        setControl(B_MR,1,4,1,1); //MR
        setControl(B_M_add,2,4,1,1); //M+
        setControl(B_M_dec,3,4,1,1); //M-
        setControl(B_MS,4,4,1,1); //MS
        //第5行
        setControl(B_secondFunction,0,5,1,1); //第二功能切换按钮
        setControl(B_sin,1,5,1,1); //sin,asin
        setControl(B_cos,2,5,1,1); //cos,acos
        setControl(B_tan,3,5,1,1); //tan,atan
        setControl(B_Exp,4,5,1,1); //科学计数法表示
        //第6行
        setControl(B_sqrt,0,6,1,1); //开根号
        setControl(B_reciprocal,1,6,1,1); //倒数
        setControl(B_Mod,2,6,1,1); //取余
        setControl(B_CE,3,6,1,1); //清空输入
        setControl(B_backspace,4,6,1,1); //清除最后一位输入
        //第7行
        setControl(B_square,0,7,1,1); //平方
        setControl(B_leftBracket,1,7,1,1); //左括号
        setControl(B_rightBracket,2,7,1,1); //右括号
        setControl(B_factorial,3,7,1,1); //阶乘
        setControl(B_div,4,7,1,1); //除号
        //第8行
        setControl(B_pow,0,8,1,1); //x的n次方
        setControl(B_7,1,8,1,1); //数字7
        setControl(B_8,2,8,1,1); //数字8
        setControl(B_9,3,8,1,1); //数字9
        setControl(B_mul,4,8,1,1); //乘号
        //第9行
        setControl(B_log,0,9,1,1); //以10为底的对数
        setControl(B_4,1,9,1,1); //数字4
        setControl(B_5,2,9,1,1); //数字5
        setControl(B_6,3,9,1,1); //数字6
        setControl(B_dec,4,9,1,1); //减号
        //第10行
        setControl(B_ln,0,10,1,1); //以e为底的对数
        setControl(B_1,1,10,1,1); //数字1
        setControl(B_2,2,10,1,1); //数字2
        setControl(B_3,3,10,1,1); //数字3
        setControl(B_add,4,10,1,1); //加号
        //第11行
        setControl(B_pi_e,0,11,1,1); //圆周率π或自然常数e
        setControl(B_negation,1,11,1,1); //取反
        setControl(B_0,2,11,1,1); //数字0
        setControl(B_point,3,11,1,1); //小数点
        setControl(B_equal,4,11,1,1); //等号

        //文本较长的按钮显示距离设为0
        B_sin.setMargin(new Insets(0, 0, 0, 0));
        B_cos.setMargin(new Insets(0, 0, 0, 0));
        B_tan.setMargin(new Insets(0, 0, 0, 0));
        B_log.setMargin(new Insets(0, 0, 0, 0));
        B_Mod.setMargin(new Insets(0, 0, 0, 0));
        B_Exp.setMargin(new Insets(0, 0, 0, 0));
        B_backspace.setMargin(new Insets(0, 0, 0, 0));
        B_M_down.setMargin(new Insets(0, 0, 0, 0));
        B_sqrt.setMargin(new Insets(0, 0, 0, 0));
        B_reciprocal.setMargin(new Insets(0, 0, 0, 0));
        B_square.setMargin(new Insets(0, 0, 0, 0));
        B_pow.setMargin(new Insets(0, 0, 0, 0));
        B_ln.setMargin(new Insets(0, 0, 0, 0));
        B_negation.setMargin(new Insets(0, 0, 0, 0));
        B_DegRad.setMargin(new Insets(0, 0, 0, 0));

        //基本功能
        B_0.addActionListener(new MyActionListener()); //数字0
        B_1.addActionListener(new MyActionListener()); //数字1
        B_2.addActionListener(new MyActionListener()); //数字2
        B_3.addActionListener(new MyActionListener()); //数字3
        B_4.addActionListener(new MyActionListener()); //数字4
        B_5.addActionListener(new MyActionListener()); //数字5
        B_6.addActionListener(new MyActionListener()); //数字6
        B_7.addActionListener(new MyActionListener()); //数字7
        B_8.addActionListener(new MyActionListener()); //数字8
        B_9.addActionListener(new MyActionListener()); //数字9
        B_point.addActionListener(new MyActionListener()); //小数点
        B_add.addActionListener(new MyActionListener()); //加号
        B_dec.addActionListener(new MyActionListener()); //减号
        B_mul.addActionListener(new MyActionListener()); //乘号
        B_div.addActionListener(new MyActionListener()); //除号
        B_leftBracket.addActionListener(new MyActionListener()); //左括号
        B_rightBracket.addActionListener(new MyActionListener()); //右括号
        B_equal.addActionListener(new MyActionListener()); //等号
        //扩展功能
        B_pi_e.addActionListener(new MyActionListener()); //圆周率π或自然常数e
        B_CE.addActionListener(new MyActionListener()); //清除输入输出
        B_backspace.addActionListener(new MyActionListener()); //退格
        B_factorial.addActionListener(new MyActionListener()); //阶乘
        B_square.addActionListener(new MyActionListener()); //平方
        B_reciprocal.addActionListener(new MyActionListener()); //倒数
        B_Exp.addActionListener(new MyActionListener()); //指数函数
        B_Mod.addActionListener(new MyActionListener()); //取余
        B_sqrt.addActionListener(new MyActionListener()); //开根号
        B_pow.addActionListener(new MyActionListener()); //x的n次方
        B_log.addActionListener(new MyActionListener()); //以10为底的对数
        B_ln.addActionListener(new MyActionListener()); //以e为底的对数
        B_negation.addActionListener(new MyActionListener()); //符号取反
        B_sin.addActionListener(new MyActionListener()); //正弦函数
        B_cos.addActionListener(new MyActionListener()); //余弦函数
        B_tan.addActionListener(new MyActionListener()); //正切函数
        //其他功能
        B_secondFunction.addActionListener(new MyActionListener()); //按键第二功能
        B_interface.addActionListener(new MyActionListener()); //界面切换
        B_DegRad.addActionListener(new MyActionListener()); //弧度角度切换
        //没用到的按钮先失能，设置按钮不可点击
        B_interface.setEnabled(false);
        B_F_E.setEnabled(false);
        B_MC.setEnabled(false);
        B_MR.setEnabled(false);
        B_M_add.setEnabled(false);
        B_M_dec.setEnabled(false);
        B_MS.setEnabled(false);
        B_M_down.setEnabled(false);
        B_Exp.setEnabled(false);
        B_negation.setEnabled(false);

        this.setVisible(true); //窗体可见
    }

    class MyActionListener implements ActionListener{ //实现动作Listener接口。实现里面的actionPerformed方法
        public void actionPerformed(ActionEvent e){
            String str = e.getActionCommand();

            switch(str){
                case "0":case "1":case "2":case "3":case "4":case "5":
                case "6":case "7":case "8":case "9":case ".":case "+":
                case "-":case "*":case "/":case "(":case ")":
                    S_input+=str;
                    break; //常用数字及运算符，直接显示，可以在混合运算中使用

                case "=": //计算结果
                    S_output = eq.getMixedOperationRes(S_input);
                    calculateFlag = true; //该结果可以用于下次计算
                    break;

                case "CE": //清空输入输出
                    S_input = ""; //输入清空
                    S_output = BigDecimal.ZERO; //输出显示"0"
                    break;

                case "←": //退格，清除一位输入
                    if(!S_input.isEmpty()){
                        S_input = S_input.substring(0,S_input.length()-1);
                    }
                    break;

                case "Mod": //取余，运算符符号为"%"，按键显示为"mod"，可以在混合运算中使用
                    S_input += "%";
                    break;

                // case "Exp": //指数函数，功能暂时不清楚
                //     S_output = "Updating..."; //...................................................................................
                //     break;

                case "n!": //阶乘，单独计算，直接出结果
                    S_output = eq.bdm.fac(Integer.valueOf(S_input));
                    calculateFlag = true; //该结果可以用于下次计算
                    break;

                case "1/x": //倒数，直接出结果
                    S_output = BigDecimal.ONE.divide(new BigDecimal(S_input),eq.bdm.getAccuracy(),BigDecimal.ROUND_HALF_EVEN);
                    calculateFlag = true; //该结果可以用于下次计算
                    break;

                // case "+/-": //符号取反
                //     S_output = "Updating..."; //...................................................................................
                //     break;

                case "Deg":
                    B_DegRad.setText("Rad"); //使用弧度进行三角函数计算
                    DegRadFlag = false; //三角函数计算的单位为弧度
                    break;

                case "Rad":
                    B_DegRad.setText("Deg"); //使用角度进行三角函数计算
                    DegRadFlag = true; //三角函数计算的单位为角度
                    break;

                // case "F-E": //普通数值与科学计数法转换
                //     S_output = "Updating..."; //........................................................................................
                //     break;

                //以下为多功能按键
                case "2nd": //切换部分按键第二功能
                    // B_secondFunction.setBackground(Color.white); //设置背景颜色
                    if(_2ndFlag){
                        B_sin.setText("sin"); //正弦
                        B_cos.setText("cos"); //余弦
                        B_tan.setText("tan"); //正切
                        B_sqrt.setText("2√x"); //2次根下x
                        B_square.setText("x^2"); //x的平方
                        B_pow.setText("x^n"); //x的n次方
                        B_log.setText("log"); //以10为底x的对数
                        B_ln.setText("ln"); //以e为底x的对数
                        B_pi_e.setText("π"); //圆周率π
                        _2ndFlag = false;
                    }
                    else{
                        B_sin.setText("asin"); //反正弦
                        B_cos.setText("acos"); //反余弦
                        B_tan.setText("atan"); //反正切
                        B_sqrt.setText("3√x"); //3次根下x
                        B_square.setText("x^3"); //x的立方
                        B_pow.setText("n√x"); //n次根下x
                        B_log.setText("ylogx"); //以x为底y的对数
                        B_ln.setText("e^x"); //e的x次方
                        B_pi_e.setText("e"); //自然常数e
                        _2ndFlag = true;
                    }
                    break;

                case "sin": //正弦函数，直接出结果
                    S_output = eq.bdm.sin(DegRadFlag ? eq.bdm.toRadians(new BigDecimal(S_input)) : new BigDecimal(S_input));
                    calculateFlag = true; //该结果可以用于下次计算
                    break;

                case "cos": //余弦函数，直接出结果
                    S_output = eq.bdm.cos(DegRadFlag ? eq.bdm.toRadians(new BigDecimal(S_input)) : new BigDecimal(S_input));
                    calculateFlag = true; //该结果可以用于下次计算
                    break;

                case "tan": //正切函数，直接出结果
                    S_output = eq.bdm.tan(DegRadFlag ? eq.bdm.toRadians(new BigDecimal(S_input)) : new BigDecimal(S_input));
                    calculateFlag = true; //该结果可以用于下次计算
                    break;

                case "asin": //反正弦函数，直接出结果
                    S_output = DegRadFlag ? eq.bdm.toDegrees(eq.bdm.asin(new BigDecimal(S_input))) : eq.bdm.asin(new BigDecimal(S_input));
                    calculateFlag = true; //该结果可以用于下次计算
                    break;

                case "acos": //反余弦函数，直接出结果
                    S_output = DegRadFlag ? eq.bdm.toDegrees(eq.bdm.acos(new BigDecimal(S_input))) : eq.bdm.acos(new BigDecimal(S_input));
                    calculateFlag = true; //该结果可以用于下次计算
                    break;

                case "atan": //反正切函数，直接出结果
                    S_output = DegRadFlag ? eq.bdm.toDegrees(eq.bdm.atan(new BigDecimal(S_input))) : eq.bdm.atan(new BigDecimal(S_input));
                    calculateFlag = true; //该结果可以用于下次计算
                    break;

                case "2√x": //2次根下x，直接出结果
                    S_output = eq.bdm.pow(new BigDecimal(S_input),new BigDecimal("0.5"));
                    calculateFlag = true; //该结果可以用于下次计算
                    break;

                case "3√x": //3次根下x，直接出结果
                    S_output = eq.bdm.pow(new BigDecimal(S_input),BigDecimal.ONE.divide(new BigDecimal("3"),eq.bdm.getAccuracy(),BigDecimal.ROUND_HALF_EVEN));
                    calculateFlag = true;
                    break;

                case "x^2": //x的平方，直接计算结果
                    S_output = eq.bdm.pow(new BigDecimal(S_input),new BigDecimal("2"));
                    calculateFlag = true; //该结果可以用于下次计算
                    break;

                case "x^3": //x的立方，直接计算结果
                    S_output = eq.bdm.pow(new BigDecimal(S_input),new BigDecimal("3"));
                    calculateFlag = true; //该结果可以用于下次计算
                    break;

                case "x^n": //x的n次方，需要点击“=”出结果，可以在混合运算中使用
                    S_input += "^"; //如果指数为整数，使用BigDecimal，如果指数为小数，使用Math.pow函数
                    break;

                case "n√x": //n次根下x，需要点击“=”出结果，可以在混合运算中使用
                    S_input += "√"; //使用Math.pow函数
                    break;

                case "log": //以10为底的对数，直接出结果
                    S_output = eq.bdm.log(BigDecimal.TEN,new BigDecimal(S_input));
                    calculateFlag = true; //该结果可以用于下次计算
                    break;

                case "ylogx": //以x为底y的对数，需要点击“=”出结果，可用于混合运算
                    S_input += "log";
                    break;

                case "ln": //以e为底的对数，直接出结果
                    S_output = eq.bdm.log(new BigDecimal(S_input));
                    calculateFlag = true; //该结果可以用于下次计算
                    break;

                case "e^x": //e的x次方，直接出结果
                    // S_output = new BigDecimal(String.valueOf(Math.exp(Double.valueOf(S_input))));
                    S_output = eq.bdm.pow(eq.bdm.getE(),new BigDecimal(S_input));
                    calculateFlag = true; //该结果可以用于下次计算
                    break;

                case "π": //圆周率，直接在输入框显示π的值
                    // S_input = eq.bdm.getPI().toString();
                    S_output = eq.bdm.getPI();
                    calculateFlag = true; //该结果可以用于下次计算
                    break;

                case "e": //自然常数e，直接在输入框显示e的值
                    // S_input = eq.bdm.getE().toString();
                    S_output = eq.bdm.getE();
                    calculateFlag = true; //该结果可以用于下次计算
                    break;

                default:break;
            }
            T_Eq.setText(S_input);
            T_Res.setText(df.format(S_output).toString());

            if(calculateFlag){ //上一步进行了计算，把计算结果赋给S_input进行连续运算
                S_input = S_output.toString();
                calculateFlag = false;
            }
        }
    }

    private void setControl(JComponent e, int x, int y, int width, int height){ //设置控件位置及大小
        gridBagConstraints.gridx = x;
        gridBagConstraints.gridy = y;
        gridBagConstraints.gridwidth = width;
        gridBagConstraints.gridheight = height;
        gridBagLayout.setConstraints(e, gridBagConstraints);
        this.add(e);
    }
}
