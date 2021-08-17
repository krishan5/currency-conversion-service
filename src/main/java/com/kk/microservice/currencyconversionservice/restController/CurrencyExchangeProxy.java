package com.kk.microservice.currencyconversionservice.restController;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.kk.microservice.currencyconversionservice.entity.CurrencyConversion;

/**
 * Name given in @FeignClient is the name of the application which you want to connect.
 * and don't forgot to add @EnableFeignClients annotation on springboot application main class
 * i.e. CurrencyConversionServiceApplication.java in this case.
 * 
 * Now there is a problem here with feign clients that we have to pass hard coded url in it 
 * which is not a good practice. Feign also provide to pass multiple urls like "http://localhost:8000;http://localhost:8001"
 * but this not gonna help us because whenever port get change on currency-exchange-service, we will also have
 * to change it here. To get rid of it, use Load balancer and Naming server / Service registry approach.
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
