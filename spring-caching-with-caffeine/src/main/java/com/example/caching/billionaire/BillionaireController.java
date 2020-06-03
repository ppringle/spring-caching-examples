package com.example.caching.billionaire;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/billionaires")
@Slf4j
public class BillionaireController {

    private final BillionaireService billionaireService;

    public BillionaireController(BillionaireService billionaireService) {
        this.billionaireService = billionaireService;
    }

    @GetMapping(value = "")
    public List<Billionaire> getAllBillionaires(){
        return billionaireService.retrieveAllBillionaires();
    }

    @GetMapping(value = "/{id}")
    public Billionaire getBillionareById(@PathVariable Long id){
        Billionaire billionaire = billionaireService.retrieveBillionareById(id);
        log.debug("Billionaire with id: <{}> is: <{}>", id, billionaire);
        return billionaire;
    }

}
