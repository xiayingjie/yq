import java.io.File;
import java.net.MalformedURLException;

/**
 * @classDescription:
 * @author:xiayingjie
 * @createTime:15/8/4
 */

public class Test {


    public static void main(String[] args) throws MalformedURLException {
        System.out.println(new File("/Users/xiayingjie/.m2/settings.xml").toURI().toURL());
    }

}
