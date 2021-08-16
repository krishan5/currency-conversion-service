package com.kk.microservice.currencyconversionservice.restController;

import java.math.BigDecimal;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.kk.microservice.currencyconversionservice.entity.CurrencyConversion;

@RestController
public class CurrencyConversionController {
	
	@Autowired
	private CurrencyExchangeProxy ceProxy;
	
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
		
		currencyConversion.setEnvironment( ccResponse.getEnvironment() + " rest template" );
		
		return currencyConversion;
	}
	
	/**
	 * We are using feign client i.e. CurrencyExchangeProxy to connect with currency-exchange-proxy
	 * in easy way instead of using RestTemplate as we did above.
	 */
	@GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversion calculateCurrencyConversionUsingFeign(@PathVariable("from") String from,
			@PathVariable("to") String to, @PathVariable("quantity") BigDecimal quantity) {
		
		CurrencyConversion ccResponse = ceProxy.calculateCurrencyConversionUsingFeign(from, to);
		
		CurrencyConversion currencyConversion = new CurrencyConversion(ccResponse.getId(), 
				ccResponse.getFrom(), ccResponse.getTo(), quantity,
				ccResponse.getConversionMultiple(),
				quantity.multiply(ccResponse.getConversionMultiple()));
		
		currencyConversion.setEnvironment( ccResponse.getEnvironment() + " Feign" );
		
		return currencyConversion;
	}

}
