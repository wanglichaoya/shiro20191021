import com.wlc.po.User;
import com.wlc.util.LoginCheckUtil;
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
        User  zhangsan= new User(1,"张三","12345",null);
        User  wangwu= new User(2,"王五","66666",null);
        User  lisi= new User(3,"李四","123",null);

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
            if(new LoginCheckUtil().login(user)){
                System.out.println(user.getName() + " 登录成功，密码是：" + user.getPassword());
            }else{
                System.out.println(user.getName() + " 登录失败，密码是：" + user.getPassword());
            }
        }

        System.out.println("=================分割线=====================");
        //判断可以登录的用户是否拥有某种角色
        for(User user:userList){
            for(String role:rolesList){
                if(new LoginCheckUtil().login(user)){
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
                if(new LoginCheckUtil().login(user)){
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
        //Subject subject = getSubject(user);
        Subject subject = new LoginCheckUtil().getSubject(user);
        return subject.hasRole(role);
    }

    private static boolean isPermitted(User user, String permit) {
        Subject subject = new LoginCheckUtil().getSubject(user);
        return subject.isPermitted(permit);
    }





}
