package model;

import java.io.IOException;
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
		try {
			Connection connection = dataSource.getConnection();
			Statement statement = connection.createStatement();
			statement.executeUpdate("set schema roumani");
			
			String query = "select bank, rate from mortgage where upto >=" 
			+ principle + "and above <" 
			+ principle + "and amort=" 
			+ amort + "and bank='"
			+ bank + "'";
			
			ResultSet rs = statement.executeQuery(query); 
			MortgageBean bean = new MortgageBean("", 0);
			if (rs.next()) {
				 bean.setBank(rs.getString("bank"));
				 bean.setRate(rs.getDouble("rate"));
			} else {
				bean = null;
			}

			connection.close();
			statement.closeOnCompletion();
			rs.close();
			return bean;
		} catch (Exception e) {
			throw new IOException("No offers availble for this bank");
		}
	}
	
	public List<MortgageBean> getBanks() throws Exception {
		try {
			List<MortgageBean> banks = new ArrayList<MortgageBean>();
			Connection connection = dataSource.getConnection();
			Statement statement = connection.createStatement();
			statement.executeUpdate("set schema roumani");
			ResultSet rs = statement.executeQuery("select distinct bank from mortgage");
			
			while(rs.next()) {
				banks.add(new MortgageBean(rs.getString("bank"), 0));
			}
			
			connection.close();
			statement.closeOnCompletion();
			rs.close();
			return banks;
		} catch (Exception e) {
			throw new IOException("No offers availble for this bank");
		}
	}
}
