package model;

public class MortgageBean
{
	private String bank;
	private double rate;
	
	public MortgageBean(String bank, double rate) {
		this.setBank(bank);
		this.setRate(rate);
	}

	public void setBank(String bankName) {
		bank = bankName;
	}
	
	public void setRate(double bankRate) {
		rate = bankRate;
	}
	
	public String getBank() {
		return bank;
	}
	
	public double getRate() {
		return rate;
	}
}
