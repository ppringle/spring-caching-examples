package com.example.caching.billionaire;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/billionaires")
public class BillionaireController {

    private final BillionaireService billionaireService;

    public BillionaireController(BillionaireService billionaireService) {
        this.billionaireService = billionaireService;
    }

    @GetMapping(value = "")
    public List<Billionaire> getAllBillionaires(){
        return billionaireService.retrieveAllBillionaires();
    }

}
