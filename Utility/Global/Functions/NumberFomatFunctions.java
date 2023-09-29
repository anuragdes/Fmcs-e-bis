package eBIS.Utility.Global.Functions;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import org.springframework.stereotype.Component;

@Component
public class NumberFomatFunctions {
	public static String getLocalizedBigDecimalValue(BigDecimal input, Locale locale) {
	    final NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
	    numberFormat.setGroupingUsed(true);
	    numberFormat.setMaximumFractionDigits(2);
	    numberFormat.setMinimumFractionDigits(2);
	    return numberFormat.format(input);
	}
}
