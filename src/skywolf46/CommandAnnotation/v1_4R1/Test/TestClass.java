package skywolf46.CommandAnnotation.v1_4R1.Test;

import skywolf46.CommandAnnotation.v1_4R1.Data.CommandData;

public class TestClass {
    public static void main(String[] args) {
        CommandData cd = new CommandData("/test 1234 1234",0);
        System.out.println(cd.getCommandArgument(0,cd.length()));
    }


    public static void test(String str1,int a2){
        System.out.println("String variable: " + str1);
        System.out.println("Integer variable: " + a2);
    }
    static class Test1 {

    }

    static class Test2 extends Test1{

    }

    static class Test3 extends Test2{

    }

    static class Test4 extends Test3{

    }
    static class Test5 extends Test4{

    }
}
