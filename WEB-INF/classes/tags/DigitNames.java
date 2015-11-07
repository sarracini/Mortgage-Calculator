package tags;

import javax.servlet.jsp.tagext.*;
import javax.servlet.jsp.*;

import java.io.*;

public class DigitNames extends SimpleTagSupport {
	private String max;
	
	public void doTag() throws JspException, IOException {
		
		JspWriter os = this.getJspContext().getOut();
		StringWriter sw = new StringWriter();
	    getJspBody().invoke(sw);
	    
	    String payment = String.valueOf(sw);
	    double monthly = Double.parseDouble(payment);
		double num = Double.parseDouble(max);

	    if (monthly <= num) {
	        char period = '.';
	        String res = "[";
	        for(int i = 0; i <= payment.length()-1; i++) {
	        	if(payment.charAt(i) == period){
	        		break;
	        	}

	        	res = res.concat(numberToWord(payment.substring(i, i+1)));
	        	res = res.concat("-");
	        }
	    	os.write(res.substring(0, res.length()-1));	
	    	os.write("]");
		}
	}
	
	private String numberToWord(String number) {
		String[] numbers = {"zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};
		String word = numbers[Integer.parseInt(number)];
		return word;
	}
	
	public void setMax(String max) {
		this.max = max;
    }
}
