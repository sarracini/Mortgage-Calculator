package model;

import java.io.IOException;

public class Mortgage
{
	private static double RANGE_STEP = 0.1;
	
	public Mortgage() {
		
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
	
	public double getRange(String s) throws Exception {
		String[] range = s.split("-");
		if (range.length == 2) {
			try {
				double lower = this.validateInterest(range[0]);
				double upper = this.validateInterest(range[1]);
				return (upper - lower + RANGE_STEP)*100;
			} catch (Exception e){
				
			}
		}
		return this.validateInterest(s);
	}
		
	public double computePayment(String p, String a, String r) throws Exception {
		double principle = this.validatePrinciple(p);
		double amortization = this.validateAmortization(a);
		//double interest = this.getRange(r);
		double interest = this.validateInterest(r);
		//System.out.println("INTEREST: " + interest);
		
		interest = this.validateInterest(r);
		interest = interest/12/100;
		amortization = amortization*12;
		double power = Math.pow((1+interest), ((-1)*amortization));
		double monthly = (interest*principle)/(1-power);
		return monthly;
	}
}
