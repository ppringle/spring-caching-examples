package com.example.caching.billionaire;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "billionaires")
public class BillionaireService {


    private final BillionareRepository billionaireRepository;
    private final CacheManager cacheManager;

    public BillionaireService(BillionareRepository billionaireRepository, CacheManager cacheManager) {
        this.billionaireRepository = billionaireRepository;
        this.cacheManager = cacheManager;
    }

    public List<Billionaire> retrieveAllBillionaires() {
        return billionaireRepository.findAll();
    }

    @Cacheable(key = "#id")
    public Billionaire retrieveBillionareById(Long id){
        return billionaireRepository.findById(id).orElse(null);
    }

    @CachePut(key = "#id")
    public Billionaire updateBillionaireCareer(Long id, String career) throws BillionaireNotFoundException {

        Billionaire billionaire = retrieveBillionareById(id);

        if(billionaire == null){
            throw new BillionaireNotFoundException("Failed to find Billionaire with id: " + id);
        }

        billionaire.setCareer(career);
        return billionaireRepository.save(billionaire);

    }

    @CacheEvict(key = "#id")
    public void deleteBillionaire(Long id) throws BillionaireNotFoundException {

        Billionaire billionaire = retrieveBillionareById(id);

        if(billionaire == null){
            throw new BillionaireNotFoundException("Failed to find Billionaire with id: " + id);
        }

        billionaireRepository.delete(billionaire);
    }

}
