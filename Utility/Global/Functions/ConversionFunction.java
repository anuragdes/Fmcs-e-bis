package eBIS.Utility.Global.Functions;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class ConversionFunction {
	 public static <T, U> List<U>convertStringListToIntList(List<T> listOfString, Function<T, U> function)
	    {
	        return listOfString.stream()
	            .map(function)
	            .collect(Collectors.toList());
	    }
}
