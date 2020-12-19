/**
 * BigDecimalMath类中的方法除了阶乘其他所有方法都必须有设定的精度后才能计算，
 * 所以不使用静态函数，必须要先初始化一个对象并设置精度，然后再调用方法
 * 
 * 所有的提示信息应在文本框中显示，待优化..................................................................................................................................................................
 */

import java.math.BigDecimal;
import java.math.BigInteger;

public class BigDecimalMath{
    private final int accuracy; //精度，计算到小数点后几位，考虑到结果四舍五入要比设定精度高2位
    private final BigDecimal accuracyNum; //=10^-accuracy，根据精度设置的数值，当级数的项小于该值说明达到设定精度，退出迭代

    private final BigDecimal _105_095; //用于对数计算的基准值，1.05/0.95的精确数值
    private final BigDecimal log_105_095; //用于对数计算的基准值，ln(1.05/0.95)的精确数值，在后面简称ln(1.1)
    private BigDecimal log10; //用于对数计算的基准值，log10是要根据精度实时计算的，直接计算速度较慢，拆分成较小的数累加
    private final BigDecimal atan05; //用于反三角函数计算的基准值，arctan(0.5)的精确数值
    private final BigDecimal PI2; //PI/2的值

    //圆周率PI的值，应在设置精度后实时计算，此处暂时使用该固定值，精确到小数点后100位
    private final static BigDecimal PI = new BigDecimal("3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679");

    //自然常数e的值，应在设置精度后实时计算，此处暂时使用该固定值，精确到小数点后100位
    private final static BigDecimal E = new BigDecimal("2.7182818284590452353602874713526624977572470936999595749669676277240766303535475945713821785251664274");

    BigDecimalMath(int ac){ //构造函数，初始化计算过程使用的基准值
        accuracy = ac + 2; //计算时的精度要比设置精度高2位

        accuracyNum = BigDecimal.ONE.divide(BigDecimal.TEN.pow(accuracy)); // =1/10^-accuracy
        _105_095 = new BigDecimal("1.05").divide(new BigDecimal("0.95"),accuracy,BigDecimal.ROUND_HALF_EVEN); //计算1.05/0.95的精确数值
        log_105_095 = log_095_105(_105_095); //计算ln(1.05/0.95)的精确数值，log_095_105()函数的参数在接近1时收敛较快，远离1的数不要使用该函数
        atan05 = atan(new BigDecimal("0.5")); //用于反三角函数计算的基准值，可以重载一个更精确的函数，限制为private类型，只用于初始化，待优化........................................................
        PI2 = PI.divide(new BigDecimal("2"),accuracy,BigDecimal.ROUND_HALF_EVEN); //PI/2，经常用

        //找到合适的一组数，尽量接近1，相乘结果等于10，不要出现无限小数，用来计算基准值ln10，待优化.............................................................................................................
        log10 = new BigDecimal("0");
        for(int i=0;i<10;i++){
            log10 = log10.add(log_095_105(new BigDecimal("1.25")));
        }
        log10 = log10.add(log_095_105(new BigDecimal("1.073741824")));

        //初始化PI的精确数值..................................................................................................................................................................................
        //初始化E的精确数值...................................................................................................................................................................................
    }

    // 计算反余切函数，计算器中用不到
    // public BigDecimal acot(BigDecimal x){
    //     return PI2.subtract(atan(x)).setScale(accuracy,BigDecimal.ROUND_HALF_EVEN);
    // }

    // 计算反正切函数，参数范围为实数集
    public BigDecimal atan(BigDecimal x){
        //泰勒展开式中x在接近或>1时收敛特别慢
        //反正切函数参数取值范围为实数集
        //根据arctan(-x) = -arctan(x),将参数范围限制到正实数
        //如果x>1,根据arctan(x) = PI/2-arctan(1/x),求arctan(1/x)，将参数范围转换到[0, 1]区间
        //但x在接近1时(如0.999)收敛仍然很慢，接下来将参数限制到[0, 0.5]区间
        //如果x>0.5，根据arctan(x)=arctan(y)+arctan((x-y)/(1+xy))，此处取y=0.5，将arctan的参数限制到0.5以下，此处可以选更小的限制值达到更快的速度，待优化......................................................................

        boolean isMinus = false; //参数是否为负标志位
        boolean isGt1 = false; //参数是否大于1标志位
        boolean isGt05 = false; //参数是否大于0.5标志位

        if(x.signum() == -1){ //参数为负数，根据arctan(x)=-arctan(-x)将参数与结果取反
            x = x.negate();
            isMinus = true;
        }

        if(x.compareTo(BigDecimal.ONE) > 0){ //参数大于1，取其倒数
            x = BigDecimal.ONE.divide(x,accuracy,BigDecimal.ROUND_HALF_EVEN);
            isGt1 = true;
        }

        if(x.compareTo(new BigDecimal("0.5")) > 0){ //参数大于0.5，按照上面转换
            BigDecimal fm = x.multiply(new BigDecimal("0.5")).add(BigDecimal.ONE);
            x = x.subtract(new BigDecimal("0.5")).divide(fm,accuracy,BigDecimal.ROUND_HALF_EVEN);
            isGt05 = true;
        }

        BigDecimal res = new BigDecimal("0"); //保存结果
        BigDecimal term = new BigDecimal("0"); //保存计算的每一项
        int i = 0; //迭代次数

        do{ //计算过程根据arctan(x)的泰勒展开式来
            term = x.pow(4*i+1).divide(new BigDecimal(4*i+1),accuracy,BigDecimal.ROUND_HALF_EVEN);
            term = term.subtract(x.pow(4*i+3).divide(new BigDecimal(4*i+3),accuracy,BigDecimal.ROUND_HALF_EVEN));
            res = res.add(term);
            i++;
        }while(term.compareTo(accuracyNum) > 0); //累加的项小于设定精度数值时退出

        if(isGt05){ //如果参数大于0.5，结果加上arctan(0.5)
            res = atan05.add(res);
        }
        if(isGt1){ //如果参数大于1，使用PI/2减去结果
            res = PI2.subtract(res);
        }
        if(isMinus){ //如果参数为负，将结果取反
            res = res.negate();
        }

        return res.setScale(accuracy,BigDecimal.ROUND_HALF_EVEN); //将结果更改为设定精度返回
    }

    //计算反余弦函数，参数范围为[-1,1]
    public BigDecimal acos(BigDecimal x){
        //限制区间为[-1, 1]
        //arcsin(x)+arccos(x)=PI/2;

        return PI2.subtract(asin(x)).setScale(accuracy,BigDecimal.ROUND_HALF_EVEN); //返回PI2-arcsin(x)
    }

    //计算反正弦函数，参数范围为[-1,1]
    public BigDecimal asin(BigDecimal x){
        //反正弦函数使用反正切函数计算
        //arcsin(x) = arctan(x/sqrt(1-x^2))

        boolean isMinus = false;

        if(x.signum() == -1){ //参数为负，根据arcsin(x)=-arcsin(-x)，对参数和结果取反
            x = x.negate();
            isMinus = true;
        }

        if(x.compareTo(BigDecimal.ONE) > 0){ //限制区间为[-1, 1]
            System.out.println("该值无意义,定义域为[-1,1]");
            return BigDecimal.ZERO;
        }
        else if(x.compareTo(BigDecimal.ONE) == 0){ //arcsin(1)，直接返回PI/2，1为参数无法计算
            return PI2;
        }

        //利用上面公式计算arcsin(x)
        BigDecimal res = pow(x,new BigDecimal("2"));
        res = BigDecimal.ONE.subtract(res);
        res = pow(res,new BigDecimal("0.5"));
        res = x.divide(res,accuracy,BigDecimal.ROUND_HALF_EVEN);
        res = atan(res);

        return (isMinus ? res.negate() : res).setScale(accuracy,BigDecimal.ROUND_HALF_EVEN);
    }

    //计算正切函数，单位为弧度
    public BigDecimal tan(BigDecimal x){
        //tan(x) = sin(x)/cos(x)
        //当结果大于10^accuracy说明是无穷大,加个判断条件.....................................................................................................................................................................

        if(x.abs().compareTo(PI2) >= 0){ //tan(x)的定义域为(-PI/2,PI/2)
            System.out.println("该值无意义,定义域为(-PI/2,PI/2)");
            return BigDecimal.ZERO;
        }

        return sin(x).divide(cos(x),accuracy,BigDecimal.ROUND_HALF_EVEN);
    }

    //计算余弦函数，单位为弧度
    public BigDecimal cos(BigDecimal x){
        //先将参数转换到[0,2PI]区间
        //再将参数转换到[0,PI/2]区间
        //对于任意参数都使用[0,PI/2]区间的值等效计算

        boolean isMinus = false;

        x = x.abs(); //全取正值，因为cos(x) = cos(-x)
        int quotient = x.divideToIntegralValue(PI2).intValue(); //计算参数除以PI/2的商，舍去余数
        x = x.remainder(PI); //取x/(PI/2)的余数

        switch(quotient % 4){ //判断参数等效于[0,PI/2],[PI/2,PI],[PI,3PI/2],[3PI/2,2PI]哪个区间
            case 1:x = PI.subtract(x);isMinus = true;break; //[PI/2,PI]区间
            case 2:isMinus = true;break; //[PI,3PI/2]区间
            case 3:x = PI.subtract(x);break; //[3PI/2,2PI]区间
        }

        BigDecimal res = new BigDecimal("0");
        BigDecimal term = new BigDecimal("0");
        int i = 0;

        do{ //根据cos(x)的泰勒展开式来
            term = x.pow(2*i).divide(fac(2*i),accuracy,BigDecimal.ROUND_HALF_EVEN);
            res = res.add(i%2==1 ? term.negate() : term);
            i++;
        }while(term.compareTo(accuracyNum) > 0);

        return (isMinus ? res.negate() : res).setScale(accuracy,BigDecimal.ROUND_HALF_EVEN);
    }

    //计算正弦函数，单位为弧度
    public BigDecimal sin(BigDecimal x){
        //计算过程参考cos(x)，只是泰勒展开式不一样

        boolean isMinus = false;

        if(x.compareTo(BigDecimal.ZERO) < 0){
            x = x.negate();
            isMinus = !isMinus;
        }

        int quotient = x.divideToIntegralValue(PI2).intValue();
        x = x.remainder(PI);

        switch(quotient % 4){
            case 1:x = PI.subtract(x);break;
            case 2:isMinus = !isMinus;break;
            case 3:x = PI.subtract(x);isMinus = !isMinus;break;
        }

        BigDecimal res = new BigDecimal("0");
        BigDecimal term = new BigDecimal("0");
        int i = 0;

        do{
            term = x.pow(2*i+1).divide(fac(2*i+1),accuracy,BigDecimal.ROUND_HALF_EVEN);
            res = res.add(i%2==1 ? term.negate() : term);
            i++;
        }while(term.compareTo(accuracyNum) > 0);

        return (isMinus ? res.negate() : res).setScale(accuracy,BigDecimal.ROUND_HALF_EVEN);
    }

    //角度转弧度
    public BigDecimal toRadians(BigDecimal deg){
        deg = deg.divide(new BigDecimal("180"),accuracy,BigDecimal.ROUND_HALF_EVEN);
        return deg.multiply(PI).setScale(accuracy,BigDecimal.ROUND_HALF_EVEN);
    }

    //弧度转角度
    public BigDecimal toDegrees(BigDecimal rad){
        rad = rad.multiply(new BigDecimal("180"));
        return rad.divide(PI,accuracy,BigDecimal.ROUND_HALF_EVEN);
    }

    //计算a的x次方
    public BigDecimal pow(BigDecimal a,BigDecimal x){
        //首先检查参数合法性，待优化..............................................................................................................................................................................
        //0^0无意义
        //a^0=1,a!=0
        //当a为负数时，x不能为偶数
        //当a为非负数时，x可为任意数
        //.........................

        boolean isMinus = false;

        if(x.signum() == -1){ //x为负数，先将x取反然后对结果取倒数
            x = x.negate();
            isMinus = true;
        }

        try{ //判断x是否为整数，为整数的话直接使用BigDecimal的pow函数
            x.intValueExact();
        }catch(Exception e){ //x不为整数，使用a^x的泰勒展开式计算，以下计算过程参考a^x的泰勒展开式
            BigDecimal xlna = x.multiply(log(a)).setScale(accuracy,BigDecimal.ROUND_HALF_EVEN); //计算x*ln(a)
            BigDecimal res = new BigDecimal("0");
            BigDecimal term = new BigDecimal("0");
            int i = 0;
    
            do{
                term = xlna.pow(i).divide(fac(i),accuracy,BigDecimal.ROUND_HALF_EVEN);
                res = res.add(term);
                i++;
            }while(term.abs().compareTo(accuracyNum) > 0); //此处必须用绝对值比较，因为xlna可能为负数
    
            return isMinus ? BigDecimal.ONE.divide(res,accuracy,BigDecimal.ROUND_HALF_EVEN) : res.setScale(accuracy,BigDecimal.ROUND_HALF_EVEN);
        }

        BigDecimal res = a.pow(x.intValue()).setScale(accuracy,BigDecimal.ROUND_HALF_EVEN); //x为整数，直接调用BigDecimal的pow函数
        return isMinus ? BigDecimal.ONE.divide(res,accuracy,BigDecimal.ROUND_HALF_EVEN) : res.setScale(accuracy,BigDecimal.ROUND_HALF_EVEN);
    }

    //以a为底x的对数，函数重载
    public BigDecimal log(BigDecimal a,BigDecimal x){
        //以a为底x的对数=log(x)/log(a)
        BigDecimal res = log(x).divide(log(a),accuracy,BigDecimal.ROUND_HALF_EVEN);
        return res.setScale(accuracy,BigDecimal.ROUND_HALF_EVEN);
    }

    //计算以e为底x的对数
    public BigDecimal log(BigDecimal x){
        //该函数调用log_095_105函数实现对(0,+无穷)范围的参数求对数
        //参数x必须大于0
        //先缩放到(0.5, 5]，用到ln(10)
        //再缩放到(0.95, 1.05]，用到ln(1.1........)

        if(x.compareTo(BigDecimal.ZERO) <= 0){
            System.out.println("参数必须大于0");
            return BigDecimal.ZERO;
        }

        BigDecimal res = new BigDecimal("0");
        int ln10Count = 0; //10倍缩放累加变量
        int ln1_1Count = 0; //1.1倍缩放累加变量

        //ln(x)=a*ln(10)+b*ln(1.1)+ln(y)
        //其中x=10a*1.1b*y
        while(x.compareTo(new BigDecimal("5")) > 0){ //参数大于5，10倍缩小，该循环执行xlog10次，即(以10为底x的对数)次
            x = x.divide(BigDecimal.TEN,accuracy,BigDecimal.ROUND_HALF_EVEN); //将该参数除以10
            ln10Count++; //累加一次代表进行了一次10倍缩小
        }
        while(x.compareTo(new BigDecimal("0.5")) < 0){ //参数小于0.5,10倍放大，该循环执行xlog0.1，即(以0.1为底x的对数)次
            x = x.multiply(BigDecimal.TEN); //将参数乘以10
            ln10Count--; //累加一次代表进行了一次10倍放大
        }

        while(x.compareTo(new BigDecimal("1.05")) > 0){ //参数大于1.05，1.1倍缩小，该循环最多执行24次，即10/(1.1^24)<1.05
            x = x.divide(_105_095,accuracy,BigDecimal.ROUND_HALF_EVEN);
            ln1_1Count++; //累加一次代表进行了一次1.1倍缩小
        }
        while(x.compareTo(new BigDecimal("0.95")) < 0){ //参数小于0.95，1.1倍放大，该循环最多执行7次，即0.5*(1.1^7)>0.95
            x = x.multiply(_105_095);
            ln1_1Count--; //累加一次代表进行了一次1.1倍放大
        }
        x = x.setScale(accuracy,BigDecimal.ROUND_HALF_EVEN); //重新设置舍入位

        res = log10.multiply(new BigDecimal(ln10Count)); //计算ln10Count*ln(10)的值
        res = res.add(log_105_095.multiply(new BigDecimal(ln1_1Count))); //计算ln1_1Count*ln(1.1)的值

        return res.add(log_095_105(x)).setScale(accuracy,BigDecimal.ROUND_HALF_EVEN); //计算缩放到[0.95, 1.05]区间的ln(x)的值
    }

    //主要用于计算[0.95,1.05]区间内的ln(x)的值，计算区间外的数也可以，但越远离“1”收敛越慢，粗略测试计算ln(10)大概耗时3s
    private BigDecimal log_095_105(BigDecimal x){
        if(x.compareTo(BigDecimal.ZERO) <= 0){ //ln(x)中参数x必须大于0
            System.out.println("参数必须大于0");
            return BigDecimal.ZERO;
        }

        //ln(x)的泰勒展开：ln(x)=2y(y^0/1 + y^2/3 + y^4/5 + y^6/7 + ...)
        //其中：y=(x-1)/(x+1)
        BigDecimal y = (x.subtract(BigDecimal.ONE)).divide(x.add(BigDecimal.ONE),accuracy,BigDecimal.ROUND_HALF_EVEN); //先计算出y
        BigDecimal res = new BigDecimal("0"); //累加的结果
        BigDecimal term = new BigDecimal("0"); //在循环中计算每一项，累加
        int i = 0; // 累加变量

        do{
            term = y.pow(2*i).divide(new BigDecimal(2*i+1),accuracy,BigDecimal.ROUND_HALF_EVEN); //按照展开的通项公式计算第i项
            res = res.add(term); //每项累加
            i++; //第i项
        }while(term.compareTo(accuracyNum) > 0); //当累加的项小于精度要求时退出，代表已达到设定精度

        res = res.multiply(y);
        res = res.multiply(new BigDecimal("2")); //乘以前面的2y
        
        return res.setScale(accuracy,BigDecimal.ROUND_HALF_EVEN); //将结果精度设为accuracy
    }

    //计算阶乘，应传入值为非负整数的BigDecimal，传入小数会自动去尾，传入负数输出1
    public BigDecimal fac(int n){
        if(n < 0){//负数没有阶乘
            System.out.println("负数没有阶乘");
            return BigDecimal.ZERO;
        }

        BigInteger res = new BigInteger("1");
        BigInteger Bn = BigInteger.valueOf(n); //将n转为BigInteger类型

        while(Bn.compareTo(BigInteger.ONE) > 0){ //大于1的时候累乘
            res = res.multiply(Bn);
            Bn = Bn.subtract(BigInteger.ONE); //自减1
        }

        return new BigDecimal(res); //阶乘结果一定为整数，不需要规定精度
    }

    public int getAccuracy(){ //返回设定精度
        return accuracy;
    }

    public BigDecimal getPI(){ //返回圆周率PI的值，不能将PI定义为类变量，因为PI的值随着设定的精度而改变
        return PI.setScale(accuracy,BigDecimal.ROUND_HALF_EVEN);
    }

    public BigDecimal getE(){ //返回自然常数E的值，不能将E定义为类变量，因为E的值随着设定的精度而改变
        return E.setScale(accuracy,BigDecimal.ROUND_HALF_EVEN);
    }
}
