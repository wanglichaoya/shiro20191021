package com.wlc.realm;

import com.wlc.dao.DAO;
import com.wlc.po.User;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.util.Set;

/**
 * describe:不使用shiro默认提供的 jdbcReal 中的sql 语句，因为shiro提供的表不能满足实际的需求，所以还是继承AuthorizingRealm,使用我们自己写的
 * sql比较好一些。
 *
 * @author 王立朝
 * @date 2019/10/21
 */
public class DatabaseRealm extends AuthorizingRealm {
    /**
     * 这个方法执行的时候就表示当前的用户已经验证通过了。
     **/
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //能进入到这里就说明账号已经通过了验证
        String userName = (String) principalCollection.getPrimaryPrincipal();
        //通过DAO获取角色 和权限
        Set<String> roles = new DAO().listRoles(userName);
        Set<String> permissions = new DAO().listPermissions(userName);

        //授权对象
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        //把通过DAO获取到的角色和权限放入
        simpleAuthorizationInfo.setStringPermissions(permissions);
        simpleAuthorizationInfo.setRoles(roles);
        return simpleAuthorizationInfo;
    }

    /**
     * 根据token验证 是否可以通过
     **/
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //获取账号密码
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String userName = token.getPrincipal().toString();
        String password = new String(token.getPassword());
        //获取数据库中的密码



       // String passwordInDb = new DAO().getPassword(userName);

        User user = new DAO().getUser(userName);
        String passwordInDb = user.getPassword();
        String salt = user.getSalt();

        //通过前面生成加密然后再次在这里自己比较
        String passwordEncoded = new SimpleHash("md5",password,salt,2).toString();



        //如果为空就是账号不存在，如果不相同就是密码错误，但是都抛出AuthenticationException，
        // 而不是抛出具体错误原因，免得给破解者提供帮助信息
       /* if (null == passwordInDb || !passwordInDb.equals(password)) {
            throw new AuthenticationException();
        }*/

       /**第一种做法是： 自己加密后的密文自己比较**/

       /* if (null == passwordInDb || !passwordEncoded.equals(passwordInDb)) {
            throw new AuthenticationException();
        }*/



        //认证信息里面存放账号密码，getName() 是当前Realm继承的方法，通常返回当前的类名DatabaseRealm
       /* SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(userName, password, getName());*/

       /**第二种：创建SimpleAuthenticationInfo 的时候，把密文和盐都传进去，让shiro自己去进行比较，
        * 这个时候需要在shiro.ini中进行配置 **/
        SimpleAuthenticationInfo simpleAuthenticationInfo =
                new SimpleAuthenticationInfo(userName, passwordInDb, ByteSource.Util.bytes(salt), getName());

        return simpleAuthenticationInfo;
    }
}
