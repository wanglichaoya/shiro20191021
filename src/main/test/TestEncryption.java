import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;

/**
 * describe:
 *
 * @author 王立朝
 * @date 2019/10/21
 */
public class TestEncryption {
    public static void main(String[] args){
      String password = "123";
      String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        System.out.println("salt=>" + salt);
      //加密的次数
      int times = 2;
      //算法的名称
      String algorithmName = "md5";
      String encodePassword = new SimpleHash(algorithmName,password,"Hg/jXAN8ihYgmYvFdpBEWA==",times).toString();
       // 89a479dd66ad75136e274c85a4073b07
      //7eb58d91eecdaf2d57de26ec6cddb466
        System.out.println("原密码是： " + password + "加密2次后的密码是： " + encodePassword);
    }
}
