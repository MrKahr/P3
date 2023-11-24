package com.proj.controller.Security;

import java.sql.SQLException;

import java.sql.ResultSet;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.proj.model.users.BasicUserInfo;

@Repository
public class UserDAO {
    private JdbcTemplate jdbcTemplate;
    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    public BasicUserInfo getUserInfo(String username){
    	String sql = "SELECT u.username name, u.password pass, a.authority role FROM "+
    			     "comp_users u INNER JOIN comp_authorities a on u.username=a.username WHERE "+
    			     "u.enabled =1 and u.username = ?"; // Replace this with userdbHandler findbyUsername
					 									// User getters on returned user object.
    	BasicUserInfo userInfo = (BasicUserInfo)jdbcTemplate.queryForObject(sql, new Object[]{username},
    		new RowMapper<BasicUserInfo>() {
	            public BasicUserInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
	                BasicUserInfo user = new BasicUserInfo();
	                user.setUserName(rs.getString("userName"));
	                user.setPassword(rs.getString("password"));
	                //user.setRole(rs.getString("role"));
	                return user;
	            }
        });
    	return userInfo;
    }
} 
