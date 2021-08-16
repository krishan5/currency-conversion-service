package com.kk.microservice.currencyconversionservice.restController;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.kk.microservice.currencyconversionservice.entity.CurrencyConversion;

/**
 * Name given in @FeignClient is the name of the application which you want to connect.
 * and don't forgot to add @EnableFeignClients annotation on springboot application main class
 * i.e. CurrencyConversionServiceApplication.java in this case.
 */
@FeignClient(name = "currency-exchange-service", url = "http://localhost:8000")
public interface CurrencyExchangeProxy {
	
	/**
	 * This is the endpoint you want to hit on another microservice.
	 * url and path variable should be matched exactly same.
	 */
	@GetMapping("/currency-exchange/from/{from}/to/{to}")
	public CurrencyConversion calculateCurrencyConversionUsingFeign(@PathVariable("from") String from, @PathVariable("to") String to);
	
}
