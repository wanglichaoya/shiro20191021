import com.wlc.po.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

import java.util.ArrayList;
import java.util.List;


/**
 * describe:测试用户角色是否起作用
 *
 * @author 王立朝
 * @date 2019/10/21
 */
public class TestUserRoles {

    public static void main(String[] args){

        //定义了3个用户
        User  zhangsan= new User("张三","12345");
        User  wangwu= new User("王五","66666");
        User  lisi= new User("李四","123");

        //把用户放入到list中
        List<User> userList = new ArrayList<>();
        userList.add(zhangsan);
        userList.add(wangwu);
        userList.add(lisi);

        //定义角色
        //admin:管理员角色
        String roleAdmin = "admin";
        //productManager:产品经理 角色
        String roleProductManager = "productManager";

        List<String> rolesList = new ArrayList<>();
        rolesList.add(roleAdmin);
        rolesList.add(roleProductManager);

        //定义权限
        //定义添加产品的权限
        String permitAddProduct = "addProduct";
        String permitDeleteProduct = "deleteProduct";
        //定义添加订单的权限
        String permitAddOrder = "addOrder";
        List<String> permitsList = new ArrayList<>();
        permitsList.add(permitAddProduct);
        permitsList.add(permitDeleteProduct);
        permitsList.add(permitAddOrder);


        //判断用户是否可以登录
        for(User user:userList){
            if(login(user)){
                System.out.printf("%s \t登录成功，用的密码是： %s\t %n",user.getPassword(),user.getPassword());
            }else{
                System.out.printf("%s \t登录失败，用的密码是： %s\t %n",user.getPassword(),user.getPassword());
            }
        }

        System.out.println("=================分割线=====================");
        //判断可以登录的用户是否拥有某种角色
        for(User user:userList){
            for(String role:rolesList){
                if(login(user)){
                    if(hasRole(user,role)){
                        System.out.println(user.getName() + " 登录成功，拥有的角色是：" + role);
                    }else{
                        System.out.println(user.getName() + " 登录成功，没有拥有的角色是：" + role);
                    }
                }
            }

        }

        System.out.println("======分割线==========");

        //判断用户是否拥有某种权限
        for(User user: userList){
            for(String permit: permitsList){
                if(login(user)){
                    if(isPermitted(user,permit)){
                        System.out.println("登录成功的用户名是： " + user.getName() + "拥有的权限是： " + permit);
                    }else{
                        System.out.println("登录成功的用户名是： " + user.getName() + "未拥有的权限是： " + permit);
                    }
                }
            }
        }


    }

    private static boolean hasRole(User user, String role) {
        Subject subject = getSubject(user);
        return subject.hasRole(role);
    }

    private static boolean isPermitted(User user, String permit) {
        Subject subject = getSubject(user);
        return subject.isPermitted(permit);
    }


    private static Subject getSubject(User user) {
        //加载配置文件，并获取工厂
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        //获取安全管理者实例
        SecurityManager sm = factory.getInstance();
        //将安全管理者放入全局对象
        SecurityUtils.setSecurityManager(sm);
        //全局对象通过安全管理者生成Subject对象
        Subject subject = SecurityUtils.getSubject();

        return subject;
    }

    private static boolean login(User user) {
        Subject subject= getSubject(user);
        //如果已经登录过了，退出
        if(subject.isAuthenticated())
            subject.logout();

        //封装用户的数据
        UsernamePasswordToken token = new UsernamePasswordToken(user.getName(), user.getPassword());
        try {
            //将用户的数据token 最终传递到Realm中进行对比
            subject.login(token);
        } catch (AuthenticationException e) {
            //验证错误
            return false;
        }

        return subject.isAuthenticated();
    }

}
