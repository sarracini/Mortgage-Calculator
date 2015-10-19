package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Mortgage
{
	private MortgageDAO dao; 
	
	public Mortgage() throws Exception {
		try {
			this.dao = new MortgageDAO();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public double validatePrinciple(String p) throws Exception {
		try {
			double result = Double.parseDouble(p);
			if (result < 0) {
				throw new IOException("Principle cannot be negative!");
			}
			return result;
		} catch (NumberFormatException e) {
			throw new NumberFormatException("Principle is not numeric!");
		}
	}
	
	public double validateInterest(String r) throws Exception {
		try {
			double result = Double.parseDouble(r);
			if (result <= 0 || result > 100) {
				throw new IOException("Interest out of range!");
			}
			return result;
		} catch (NumberFormatException e) {
			throw new NumberFormatException("Interest is not numeric!");
		}
	}
	
	public double validateAmortization(String a) throws Exception {
		try {
			double result = Double.parseDouble(a);
			if (result == 20.0 || result == 25.0 || result == 30.0) {
				return result;
			}
			else  {
				throw new IOException("Amortization is invalid!");
			}
		} catch (NumberFormatException e) {
			throw new NumberFormatException("Amortization is not numeric!");
		}

	}
	
	public List<String> getBanks() {
		List<String> allBanks = new ArrayList<String>();
		try {
			List<MortgageBean> banks = dao.getBanks();
			for (int i = 0; i < banks.size(); i++) {
				String bankName = banks.get(i).getBank();
				allBanks.add(bankName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return allBanks;
	}
		
	public double computePayment(String p, String a, String r) throws Exception {
		double principle = this.validatePrinciple(p);
		double amortization = this.validateAmortization(a);
		double interest = this.validateInterest(r);
		
		interest = interest/12/100;
		amortization = amortization*12;
		double power = Math.pow((1+interest), ((-1)*amortization));
		double monthly = (interest*principle)/(1-power);
		return monthly;
	}
}
