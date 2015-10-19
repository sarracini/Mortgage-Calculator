package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;
import javax.naming.InitialContext;

public class MortgageDAO
{
	private DataSource dataSource;
	
	public MortgageDAO() throws Exception {
		this.dataSource = (DataSource) (new InitialContext()).lookup("java:/comp/env/jdbc/EECS");
		
	}
	
	public MortgageBean getRate(double principle, int amort, String bank) throws Exception {
		Connection connection = dataSource.getConnection();
		Statement statement = connection.createStatement();
		statement.execute("set schema roumani");
		ResultSet rs = statement.executeQuery("select distinct bank from mortgage"); 
		MortgageBean bean = new MortgageBean("", 0);
		while (rs.next()) {
			bean.setRate(rs.getDouble(0));;
		}
		connection.close();
		statement.closeOnCompletion();
		return bean;
	}
	
	public List<MortgageBean> getBanks() throws Exception {
		List<MortgageBean> banks = new ArrayList<MortgageBean>();
		Connection connection = dataSource.getConnection();
		Statement statement = connection.createStatement();
		statement.execute("set schema roumani");
		ResultSet rs = statement.executeQuery("select distinct bank from mortgage");
		while(rs.next()) {
			banks.add(new MortgageBean(rs.getString("bank"), 0));
		}
		connection.close();
		statement.closeOnCompletion();
		return banks;
	}
}
