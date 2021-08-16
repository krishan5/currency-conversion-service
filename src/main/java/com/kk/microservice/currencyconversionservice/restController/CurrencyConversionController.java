package com.kk.microservice.currencyconversionservice.restController;

import java.math.BigDecimal;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.kk.microservice.currencyconversionservice.entity.CurrencyConversion;

@RestController
public class CurrencyConversionController {
	
	@Autowired
	//org.springframework.core.env.Environment
	private Environment environment;
	
	@GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversion calculateCurrencyConversion(@PathVariable("from") String from,
			@PathVariable("to") String to, @PathVariable("quantity") BigDecimal quantity) {
		
		HashMap<String, String> uriVariables = new HashMap<>();
		uriVariables.put("from", from);
		uriVariables.put("to", to);
		
		/**
		 * Calling currency-exchange-service microservice endpoint
		 */
		ResponseEntity<CurrencyConversion> response = new RestTemplate()
				.getForEntity("http://localhost:8000/currency-exchange/from/{from/to/{to}}", 
				CurrencyConversion.class, uriVariables);
		
		CurrencyConversion ccResponse = response.getBody();
		
		CurrencyConversion currencyConversion = new CurrencyConversion(ccResponse.getId(), 
				ccResponse.getFrom(), ccResponse.getTo(), quantity,
				ccResponse.getConversionMultiple(),
				quantity.multiply(ccResponse.getConversionMultiple()));
		
		currencyConversion.setEnvironment( ccResponse.getEnvironment() );
		
		return currencyConversion;
	}

}
