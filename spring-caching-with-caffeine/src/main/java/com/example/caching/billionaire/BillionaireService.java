package com.example.caching.billionaire;

import com.example.caching.billionaire.Billionaire;
import com.example.caching.billionaire.BillionareRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillionaireService {


    private final BillionareRepository billionaireRepository;

    public BillionaireService(BillionareRepository billionaireRepository) {
        this.billionaireRepository = billionaireRepository;
    }

    public List<Billionaire> retrieveAllBillionaires() {
        return billionaireRepository.findAll();
    }


}
