/**
 * B_��ͷ�ı���Ϊ��ť����
 * T_��ͷ�ı���Ϊ�ı���ʾ����
 * S_��ͷ�ı���Ϊ�ַ�������
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
    private MixedOperation eq = new MixedOperation(32); //��������࣬���þ���ΪС�����34λ
    private GridBagConstraints gridBagConstraints = new GridBagConstraints(); //ʵ�����ö���������������й���
    private GridBagLayout gridBagLayout = new GridBagLayout(); //ʵ�������ֶ���
    private DecimalFormat df = new DecimalFormat(); //���ַ������и�ʽ������λһ��ָ�

    private boolean _2ndFlag = false; //��¼��ǰ�л������鰴��
    private boolean DegRadFlag = true; //��¼��ǰʹ�ýǶȻ��ǻ���
    private boolean calculateFlag = false; //�����һ�������˼��㣬�ñ�־ΪΪtrue

    //��0��
    private JButton B_interface = new JButton("��ѧ"); //�������汾�л�
    //��1��
    private JTextField T_Eq = new JTextField(""); //��ʾ������ʽ
    //��2��
    private JTextField T_Res = new JTextField("0"); //��ʾ������
    //��3��
    private JButton B_DegRad = new JButton("Deg"); //�Ƕ��뻡��ת��
    private JButton B_F_E = new JButton("F-E"); //��ͨ��ֵ���ѧ������ת��
    private JButton B_M_down = new JButton("M��"); //�ڴ��������ʱ����
    //��4��
    private JButton B_MC = new JButton("MC"); //�ڴ��������ʱ����
    private JButton B_MR = new JButton("MR"); //�ڴ��������ʱ����
    private JButton B_M_add = new JButton("M+"); //�ڴ��������ʱ����
    private JButton B_M_dec = new JButton("M-"); //�ڴ��������ʱ����
    private JButton B_MS = new JButton("MS"); //�ڴ��������ʱ����
    //��5��
    private JButton B_secondFunction = new JButton("2nd"); //�л������ڶ�����
    private JButton B_sin = new JButton("sin"); //���Һ���
    private JButton B_cos = new JButton("cos"); //���Һ���
    private JButton B_tan = new JButton("tan"); //���к���
    private JButton B_Exp = new JButton("Exp"); //ָ������
    //��6��
    private JButton B_sqrt = new JButton("2��x"); //������
    private JButton B_reciprocal = new JButton("1/x"); //����
    private JButton B_Mod = new JButton("Mod"); //ȡ��
    private JButton B_CE = new JButton("CE"); //����������
    private JButton B_backspace = new JButton("��"); //�˸������������һλ
    //��7��
    private JButton B_square = new JButton("x^2"); //ƽ��
    private JButton B_leftBracket = new JButton("("); //��������������
    private JButton B_rightBracket = new JButton(")"); //��������������
    private JButton B_factorial = new JButton("n!"); //�׳�
    private JButton B_div = new JButton("/"); //����
    //��8��
    private JButton B_pow = new JButton("x^n"); //x��n�η�
    private JButton B_7 = new JButton("7"); //����7
    private JButton B_8 = new JButton("8"); //����8
    private JButton B_9 = new JButton("9"); //����9
    private JButton B_mul = new JButton("*"); //�˺�
    //��9��
    private JButton B_log = new JButton("log"); //��10Ϊ�׵Ķ���
    private JButton B_4 = new JButton("4"); //����4
    private JButton B_5 = new JButton("5"); //����5
    private JButton B_6 = new JButton("6"); //����6
    private JButton B_dec = new JButton("-"); //����
    //��10��
    private JButton B_ln = new JButton("ln"); //��eΪ�׵Ķ���
    private JButton B_1 = new JButton("1"); //����1
    private JButton B_2 = new JButton("2"); //����2
    private JButton B_3 = new JButton("3"); //����3
    private JButton B_add = new JButton("+"); //�Ӻ�
    //��11��
    private JButton B_pi_e = new JButton("��"); //Բ���ʦл���Ȼ����e
    private JButton B_negation = new JButton("+/-"); //��/���л���ȡ��
    private JButton B_0 = new JButton("0"); //����0
    private JButton B_point = new JButton("."); //С����
    private JButton B_equal = new JButton("="); //�Ⱥ�
    //��������ַ���
    private String S_input = new String(""); //�����������ʽ
    private BigDecimal S_output = new BigDecimal("0"); //���������

    public CalculatorHMI(){
        df.setMaximumFractionDigits(32); //��ֵ��ʾ��С�����32λ
        df.setRoundingMode(RoundingMode.HALF_EVEN); //���м����뷨
        df.setGroupingSize(3); //�������ַ����С���Խ��3λһ�廮�ֿ�

        T_Eq.setHorizontalAlignment(JTextField.RIGHT); //������ʽ�ı���ʾ�Ҷ���
        T_Res.setHorizontalAlignment(JTextField.RIGHT); //����ı���ʾ�Ҷ���

        this.setSize(277,365); //�����С
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(3);
        this.setResizable(false);
        this.setLayout(gridBagLayout); //�����������ΪGridBagLayout����
        gridBagConstraints.fill = GridBagConstraints.BOTH; //�÷�����Ϊ���������������ڵ�������������Ҫ��ʱ����ʾ��� 
        
        //��0��
        setControl(B_interface,0,0,2,1);
        //��1��
        setControl(T_Eq,0,1,5,1); //��ʽ��ʾ
        //��2��
        setControl(T_Res,0,2,5,1); //�����ʾ
        //��3��
        setControl(B_DegRad,0,3,1,1); //�Ƕ��뻡���л�
        setControl(B_F_E,1,3,1,1); //δ֪���ܣ�����
        setControl(B_M_down,4,3,1,1); //M��
        //��4��
        setControl(B_MC,0,4,1,1); //MC
        setControl(B_MR,1,4,1,1); //MR
        setControl(B_M_add,2,4,1,1); //M+
        setControl(B_M_dec,3,4,1,1); //M-
        setControl(B_MS,4,4,1,1); //MS
        //��5��
        setControl(B_secondFunction,0,5,1,1); //�ڶ������л���ť
        setControl(B_sin,1,5,1,1); //sin,asin
        setControl(B_cos,2,5,1,1); //cos,acos
        setControl(B_tan,3,5,1,1); //tan,atan
        setControl(B_Exp,4,5,1,1); //��ѧ��������ʾ
        //��6��
        setControl(B_sqrt,0,6,1,1); //������
        setControl(B_reciprocal,1,6,1,1); //����
        setControl(B_Mod,2,6,1,1); //ȡ��
        setControl(B_CE,3,6,1,1); //�������
        setControl(B_backspace,4,6,1,1); //������һλ����
        //��7��
        setControl(B_square,0,7,1,1); //ƽ��
        setControl(B_leftBracket,1,7,1,1); //������
        setControl(B_rightBracket,2,7,1,1); //������
        setControl(B_factorial,3,7,1,1); //�׳�
        setControl(B_div,4,7,1,1); //����
        //��8��
        setControl(B_pow,0,8,1,1); //x��n�η�
        setControl(B_7,1,8,1,1); //����7
        setControl(B_8,2,8,1,1); //����8
        setControl(B_9,3,8,1,1); //����9
        setControl(B_mul,4,8,1,1); //�˺�
        //��9��
        setControl(B_log,0,9,1,1); //��10Ϊ�׵Ķ���
        setControl(B_4,1,9,1,1); //����4
        setControl(B_5,2,9,1,1); //����5
        setControl(B_6,3,9,1,1); //����6
        setControl(B_dec,4,9,1,1); //����
        //��10��
        setControl(B_ln,0,10,1,1); //��eΪ�׵Ķ���
        setControl(B_1,1,10,1,1); //����1
        setControl(B_2,2,10,1,1); //����2
        setControl(B_3,3,10,1,1); //����3
        setControl(B_add,4,10,1,1); //�Ӻ�
        //��11��
        setControl(B_pi_e,0,11,1,1); //Բ���ʦл���Ȼ����e
        setControl(B_negation,1,11,1,1); //ȡ��
        setControl(B_0,2,11,1,1); //����0
        setControl(B_point,3,11,1,1); //С����
        setControl(B_equal,4,11,1,1); //�Ⱥ�

        //�ı��ϳ��İ�ť��ʾ������Ϊ0
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

        //��������
        B_0.addActionListener(new MyActionListener()); //����0
        B_1.addActionListener(new MyActionListener()); //����1
        B_2.addActionListener(new MyActionListener()); //����2
        B_3.addActionListener(new MyActionListener()); //����3
        B_4.addActionListener(new MyActionListener()); //����4
        B_5.addActionListener(new MyActionListener()); //����5
        B_6.addActionListener(new MyActionListener()); //����6
        B_7.addActionListener(new MyActionListener()); //����7
        B_8.addActionListener(new MyActionListener()); //����8
        B_9.addActionListener(new MyActionListener()); //����9
        B_point.addActionListener(new MyActionListener()); //С����
        B_add.addActionListener(new MyActionListener()); //�Ӻ�
        B_dec.addActionListener(new MyActionListener()); //����
        B_mul.addActionListener(new MyActionListener()); //�˺�
        B_div.addActionListener(new MyActionListener()); //����
        B_leftBracket.addActionListener(new MyActionListener()); //������
        B_rightBracket.addActionListener(new MyActionListener()); //������
        B_equal.addActionListener(new MyActionListener()); //�Ⱥ�
        //��չ����
        B_pi_e.addActionListener(new MyActionListener()); //Բ���ʦл���Ȼ����e
        B_CE.addActionListener(new MyActionListener()); //����������
        B_backspace.addActionListener(new MyActionListener()); //�˸�
        B_factorial.addActionListener(new MyActionListener()); //�׳�
        B_square.addActionListener(new MyActionListener()); //ƽ��
        B_reciprocal.addActionListener(new MyActionListener()); //����
        B_Exp.addActionListener(new MyActionListener()); //ָ������
        B_Mod.addActionListener(new MyActionListener()); //ȡ��
        B_sqrt.addActionListener(new MyActionListener()); //������
        B_pow.addActionListener(new MyActionListener()); //x��n�η�
        B_log.addActionListener(new MyActionListener()); //��10Ϊ�׵Ķ���
        B_ln.addActionListener(new MyActionListener()); //��eΪ�׵Ķ���
        B_negation.addActionListener(new MyActionListener()); //����ȡ��
        B_sin.addActionListener(new MyActionListener()); //���Һ���
        B_cos.addActionListener(new MyActionListener()); //���Һ���
        B_tan.addActionListener(new MyActionListener()); //���к���
        //��������
        B_secondFunction.addActionListener(new MyActionListener()); //�����ڶ�����
        B_interface.addActionListener(new MyActionListener()); //�����л�
        B_DegRad.addActionListener(new MyActionListener()); //���ȽǶ��л�
        //û�õ��İ�ť��ʧ�ܣ����ð�ť���ɵ��
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

        this.setVisible(true); //����ɼ�
    }

    class MyActionListener implements ActionListener{ //ʵ�ֶ���Listener�ӿڡ�ʵ�������actionPerformed����
        public void actionPerformed(ActionEvent e){
            String str = e.getActionCommand();

            switch(str){
                case "0":case "1":case "2":case "3":case "4":case "5":
                case "6":case "7":case "8":case "9":case ".":case "+":
                case "-":case "*":case "/":case "(":case ")":
                    S_input+=str;
                    break; //�������ּ��������ֱ����ʾ�������ڻ��������ʹ��

                case "=": //������
                    S_output = eq.getMixedOperationRes(S_input);
                    calculateFlag = true; //�ý�����������´μ���
                    break;

                case "CE": //����������
                    S_input = ""; //�������
                    S_output = BigDecimal.ZERO; //�����ʾ"0"
                    break;

                case "��": //�˸����һλ����
                    if(!S_input.isEmpty()){
                        S_input = S_input.substring(0,S_input.length()-1);
                    }
                    break;

                case "Mod": //ȡ�࣬���������Ϊ"%"��������ʾΪ"mod"�������ڻ��������ʹ��
                    S_input += "%";
                    break;

                // case "Exp": //ָ��������������ʱ�����
                //     S_output = "Updating..."; //...................................................................................
                //     break;

                case "n!": //�׳ˣ��������㣬ֱ�ӳ����
                    S_output = eq.bdm.fac(Integer.valueOf(S_input));
                    calculateFlag = true; //�ý�����������´μ���
                    break;

                case "1/x": //������ֱ�ӳ����
                    S_output = BigDecimal.ONE.divide(new BigDecimal(S_input),eq.bdm.getAccuracy(),BigDecimal.ROUND_HALF_EVEN);
                    calculateFlag = true; //�ý�����������´μ���
                    break;

                // case "+/-": //����ȡ��
                //     S_output = "Updating..."; //...................................................................................
                //     break;

                case "Deg":
                    B_DegRad.setText("Rad"); //ʹ�û��Ƚ������Ǻ�������
                    DegRadFlag = false; //���Ǻ�������ĵ�λΪ����
                    break;

                case "Rad":
                    B_DegRad.setText("Deg"); //ʹ�ýǶȽ������Ǻ�������
                    DegRadFlag = true; //���Ǻ�������ĵ�λΪ�Ƕ�
                    break;

                // case "F-E": //��ͨ��ֵ���ѧ������ת��
                //     S_output = "Updating..."; //........................................................................................
                //     break;

                //����Ϊ�๦�ܰ���
                case "2nd": //�л����ְ����ڶ�����
                    // B_secondFunction.setBackground(Color.white); //���ñ�����ɫ
                    if(_2ndFlag){
                        B_sin.setText("sin"); //����
                        B_cos.setText("cos"); //����
                        B_tan.setText("tan"); //����
                        B_sqrt.setText("2��x"); //2�θ���x
                        B_square.setText("x^2"); //x��ƽ��
                        B_pow.setText("x^n"); //x��n�η�
                        B_log.setText("log"); //��10Ϊ��x�Ķ���
                        B_ln.setText("ln"); //��eΪ��x�Ķ���
                        B_pi_e.setText("��"); //Բ���ʦ�
                        _2ndFlag = false;
                    }
                    else{
                        B_sin.setText("asin"); //������
                        B_cos.setText("acos"); //������
                        B_tan.setText("atan"); //������
                        B_sqrt.setText("3��x"); //3�θ���x
                        B_square.setText("x^3"); //x������
                        B_pow.setText("n��x"); //n�θ���x
                        B_log.setText("ylogx"); //��xΪ��y�Ķ���
                        B_ln.setText("e^x"); //e��x�η�
                        B_pi_e.setText("e"); //��Ȼ����e
                        _2ndFlag = true;
                    }
                    break;

                case "sin": //���Һ�����ֱ�ӳ����
                    S_output = eq.bdm.sin(DegRadFlag ? eq.bdm.toRadians(new BigDecimal(S_input)) : new BigDecimal(S_input));
                    calculateFlag = true; //�ý�����������´μ���
                    break;

                case "cos": //���Һ�����ֱ�ӳ����
                    S_output = eq.bdm.cos(DegRadFlag ? eq.bdm.toRadians(new BigDecimal(S_input)) : new BigDecimal(S_input));
                    calculateFlag = true; //�ý�����������´μ���
                    break;

                case "tan": //���к�����ֱ�ӳ����
                    S_output = eq.bdm.tan(DegRadFlag ? eq.bdm.toRadians(new BigDecimal(S_input)) : new BigDecimal(S_input));
                    calculateFlag = true; //�ý�����������´μ���
                    break;

                case "asin": //�����Һ�����ֱ�ӳ����
                    S_output = DegRadFlag ? eq.bdm.toDegrees(eq.bdm.asin(new BigDecimal(S_input))) : eq.bdm.asin(new BigDecimal(S_input));
                    calculateFlag = true; //�ý�����������´μ���
                    break;

                case "acos": //�����Һ�����ֱ�ӳ����
                    S_output = DegRadFlag ? eq.bdm.toDegrees(eq.bdm.acos(new BigDecimal(S_input))) : eq.bdm.acos(new BigDecimal(S_input));
                    calculateFlag = true; //�ý�����������´μ���
                    break;

                case "atan": //�����к�����ֱ�ӳ����
                    S_output = DegRadFlag ? eq.bdm.toDegrees(eq.bdm.atan(new BigDecimal(S_input))) : eq.bdm.atan(new BigDecimal(S_input));
                    calculateFlag = true; //�ý�����������´μ���
                    break;

                case "2��x": //2�θ���x��ֱ�ӳ����
                    S_output = eq.bdm.pow(new BigDecimal(S_input),new BigDecimal("0.5"));
                    calculateFlag = true; //�ý�����������´μ���
                    break;

                case "3��x": //3�θ���x��ֱ�ӳ����
                    S_output = eq.bdm.pow(new BigDecimal(S_input),BigDecimal.ONE.divide(new BigDecimal("3"),eq.bdm.getAccuracy(),BigDecimal.ROUND_HALF_EVEN));
                    calculateFlag = true;
                    break;

                case "x^2": //x��ƽ����ֱ�Ӽ�����
                    S_output = eq.bdm.pow(new BigDecimal(S_input),new BigDecimal("2"));
                    calculateFlag = true; //�ý�����������´μ���
                    break;

                case "x^3": //x��������ֱ�Ӽ�����
                    S_output = eq.bdm.pow(new BigDecimal(S_input),new BigDecimal("3"));
                    calculateFlag = true; //�ý�����������´μ���
                    break;

                case "x^n": //x��n�η�����Ҫ�����=��������������ڻ��������ʹ��
                    S_input += "^"; //���ָ��Ϊ������ʹ��BigDecimal�����ָ��ΪС����ʹ��Math.pow����
                    break;

                case "n��x": //n�θ���x����Ҫ�����=��������������ڻ��������ʹ��
                    S_input += "��"; //ʹ��Math.pow����
                    break;

                case "log": //��10Ϊ�׵Ķ�����ֱ�ӳ����
                    S_output = eq.bdm.log(BigDecimal.TEN,new BigDecimal(S_input));
                    calculateFlag = true; //�ý�����������´μ���
                    break;

                case "ylogx": //��xΪ��y�Ķ�������Ҫ�����=��������������ڻ������
                    S_input += "log";
                    break;

                case "ln": //��eΪ�׵Ķ�����ֱ�ӳ����
                    S_output = eq.bdm.log(new BigDecimal(S_input));
                    calculateFlag = true; //�ý�����������´μ���
                    break;

                case "e^x": //e��x�η���ֱ�ӳ����
                    // S_output = new BigDecimal(String.valueOf(Math.exp(Double.valueOf(S_input))));
                    S_output = eq.bdm.pow(eq.bdm.getE(),new BigDecimal(S_input));
                    calculateFlag = true; //�ý�����������´μ���
                    break;

                case "��": //Բ���ʣ�ֱ�����������ʾ�е�ֵ
                    // S_input = eq.bdm.getPI().toString();
                    S_output = eq.bdm.getPI();
                    calculateFlag = true; //�ý�����������´μ���
                    break;

                case "e": //��Ȼ����e��ֱ�����������ʾe��ֵ
                    // S_input = eq.bdm.getE().toString();
                    S_output = eq.bdm.getE();
                    calculateFlag = true; //�ý�����������´μ���
                    break;

                default:break;
            }
            T_Eq.setText(S_input);
            T_Res.setText(df.format(S_output).toString());

            if(calculateFlag){ //��һ�������˼��㣬�Ѽ���������S_input������������
                S_input = S_output.toString();
                calculateFlag = false;
            }
        }
    }

    private void setControl(JComponent e, int x, int y, int width, int height){ //���ÿؼ�λ�ü���С
        gridBagConstraints.gridx = x;
        gridBagConstraints.gridy = y;
        gridBagConstraints.gridwidth = width;
        gridBagConstraints.gridheight = height;
        gridBagLayout.setConstraints(e, gridBagConstraints);
        this.add(e);
    }
}
