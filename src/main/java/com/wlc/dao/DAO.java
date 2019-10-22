package com.wlc.dao;

import com.wlc.po.User;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;

import java.sql.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * describe:
 *
 * @author 王立朝
 * @date 2019/10/21
 */
public class DAO {
    private String url = "jdbc:mysql://127.0.0.1:3306/shiro?characterEncoding=UTF-8";
    private String userName = "scott";
    private String passWord = "tiger";

    //加载驱动
    public DAO() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    //获取连接

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, userName, passWord);
    }

    //根据用户名获取用户密码
    public String getPassword(String userName) {
        String sql = "select password from user where name = ?";
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,userName);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                return resultSet.getString("password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    //根据用户名获取用户的角色
    public Set<String> listRoles(String userName) {
        Set<String> roles = new HashSet<>();
        String sql = "SELECT\n" +
                "\tr.name\n" +
                "FROM\n" +
                "\tUSER u\n" +
                "LEFT JOIN user_role ur ON u.id = ur.uid\n" +
                "LEFT JOIN role r ON ur.rid = r.id\n" +
                "WHERE\n" +
                "\tu. NAME = ?";
        Connection connection = null;
        try {
            connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "li4");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                System.out.println(resultSet.getString("name"));
                roles.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roles;
    }

    public Set<String> listPermissions(String userName) {
        Set<String> permissions = new HashSet<>();
        String sql =
                "select p.name from user u " +
                        "left join user_role ru on u.id = ru.uid " +
                        "left join role r on r.id = ru.rid " +
                        "left join role_permission rp on r.id = rp.rid " +
                        "left join permission p on p.id = rp.pid " +
                        "where u.name =?";

        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setString(1, userName);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                permissions.add(rs.getString(1));
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return permissions;
    }

    public int createUser(String userName,String password){
        int result = 0;
        //随机加盐
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        //加密的次数
        int times = 2;
        //算法的名称
        String algorithmName = "md5";
        String encodePassword = new SimpleHash(algorithmName,password,salt,times).toString();
        String sql = "insert into user(id,name,password,salt) values(null,?,?,?)";
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,userName);
            preparedStatement.setString(2,encodePassword);
            preparedStatement.setString(3,salt);

            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;

    }

    public static void main(String[] args) {
        DAO dao = new DAO();
        /*System.out.println(dao.listRoles("li4").size());
        System.out.println(dao.listPermissions("li4"));
        System.out.println(new DAO().listRoles("zhang3"));
        System.out.println(new DAO().listRoles("li4"));
*/


        System.out.println(dao.getUser("tom").toString());
    }

    public User getUser(String userName) {
        User user = null;
        String sql = "select * from user where name = ?";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql);) {

            ps.setString(1, userName);

            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                user.setSalt(rs.getString("salt"));
            }

            /*if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                user.setSalt(rs.getString("salt"));
            }*/

        } catch (SQLException e) {

            e.printStackTrace();
        }
        return user;
    }
}
