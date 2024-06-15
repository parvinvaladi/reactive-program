package com.programming.bookservice.service;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "",url = "{com.programming.inventory.service}")
public interface InventoryClientService {

//    @RequestMapping(method = RequestMethod.POST, path = "/api/v1/inventory/get-all")
}
