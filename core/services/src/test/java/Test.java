import com.google.common.primitives.UnsignedLong;

public class Test {

    @org.junit.jupiter.api.Test
    public void myTest(){
        UnsignedLong temp = UnsignedLong.valueOf("18446744073709551615");
        System.out.println(temp);
    }
}
