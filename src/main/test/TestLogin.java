import com.wlc.dao.DAO;
import com.wlc.po.User;
import com.wlc.util.LoginCheckUtil;

/**
 * describe:
 *
 * @author 王立朝
 * @date 2019/10/21
 */
public class TestLogin {
    public static void main(String[] args){
       // new DAO().createUser("tom","123");
        User user = new User();
        user.setName("tom");
        user.setPassword("123");

        if(new LoginCheckUtil().login(user))
            System.out.println("登录成功");
        else
            System.out.println("登录失败");

    }

}
