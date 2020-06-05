package com.example.caching.caffeine.billionaire;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BillionaireServiceTest {

    @Autowired
    private BillionaireService billionaireService;

    @MockBean
    private BillionareRepository billionareRepository;

    @Autowired
    private CacheManager cacheManager;

    private Cache billionaireCache;

    @BeforeEach
    public void init() {
        Mockito.reset(billionareRepository);
        billionaireCache = cacheManager.getCache("billionaires");
        assertThat(billionaireCache).isNotNull();
        billionaireCache.invalidate();
    }

    @Test
    public void retrieveAllBillionaires_shouldReturnAllAvailableEntries() {

        List<Billionaire> billionaireList = new ArrayList();
        billionaireList.add(Billionaire.builder()
                .id(1L)
                .firstName("Steve")
                .lastName("Bezos")
                .build());
        billionaireList.add(Billionaire.builder()
                .id(2L)
                .firstName("Jim")
                .lastName("Walton")
                .build());

        when(billionareRepository.findAll()).thenReturn(billionaireList);

        List<Billionaire> billionaires = billionaireService.retrieveAllBillionaires();
        assertThat(billionaires).isNotEmpty();

        List<String> lastNames = billionaires.stream().map(b -> b.getLastName()).collect(Collectors.toList());
        assertThat(lastNames).containsAnyOf("Bezos", "Walton");

    }

    @Test
    public void retrieveBillionaireById_shouldUseCacheIfBillionaireEntryIsCached_elseRetrieveEntryFromDB() {

        Billionaire cachedBillionaire = billionaireCache.get(1L, Billionaire.class);
        assertThat(cachedBillionaire).isNull();

        when(billionareRepository.findById(eq(1L))).thenReturn(Optional.of(Billionaire.builder()
                .id(1L)
                .firstName("Steve")
                .lastName("Bezos")
                .build()));

        Billionaire billionaireFirstServiceRetrieval = billionaireService.retrieveBillionareById(1L);
        assertThat(billionaireFirstServiceRetrieval).isNotNull();

        cachedBillionaire = billionaireCache.get(1L, Billionaire.class);
        assertThat(cachedBillionaire).isNotNull();

        Billionaire billionaireSecondServiceRetrieval = billionaireService.retrieveBillionareById(1L);
        assertThat(billionaireSecondServiceRetrieval).isNotNull();

        //Verifies that even though there were two service calls to retrieve the billionaire reference, only one
        //call was made to the repository, with the last invocation retrieving its reference from the cache...
        verify(billionareRepository, times(1)).findById(eq(1L));
    }

    @Test
    public void updateBillionaireCareer_withNonExistentBillionaireReference_shouldThrowNotFoundException() {

        when(billionareRepository.findById(eq(1L))).thenReturn(null);

        assertThrows(BillionaireNotFoundException.class, () -> {
            billionaireService.updateBillionaireCareer(10000L, "");
        });

    }

    @Test
    public void updateBillionaireCareer_withValidBillionaireReference_shouldUpdateTheBillionaireReferenceInTheCacheAndDB() throws BillionaireNotFoundException {

        Billionaire cachedBillionaire = billionaireCache.get(1L, Billionaire.class);
        assertThat(cachedBillionaire).isNull();

        when(billionareRepository.findById(eq(1L))).thenReturn(Optional.of(Billionaire.builder()
                .id(1L)
                .firstName("Steve")
                .lastName("Bezos")
                .build()));

        final AtomicInteger saveBillionaireCount = new AtomicInteger(0);
        when(billionareRepository.save(any(Billionaire.class))).thenAnswer(invocationOnMock -> {
            saveBillionaireCount.incrementAndGet();
            Billionaire billionaire = Billionaire.builder()
                    .id(1L)
                    .firstName("Steve")
                    .lastName("Bezos")
                    .build();

            if(saveBillionaireCount.get() == 1){
                billionaire.setCareer("Farmer");
            }

            if(saveBillionaireCount.get() == 2){
                billionaire.setCareer("Dentist");
            }

            return billionaire;
        });

        //Attempts to update the billionaire reference...
        Billionaire updatedBillionaireRef = billionaireService.updateBillionaireCareer(1L, "Farmer");
        assertThat(updatedBillionaireRef).isNotNull();
        assertThat(updatedBillionaireRef.getLastName()).isEqualTo("Bezos");
        assertThat(updatedBillionaireRef.getCareer()).isEqualTo("Farmer");

        //Verifies that the value has been updated in the cache reflecting career value of "Farmer"
        cachedBillionaire = billionaireCache.get(1L, Billionaire.class);
        assertThat(cachedBillionaire).isNotNull();
        assertThat(cachedBillionaire.getCareer()).isEqualTo("Farmer");

        //Verifies that the value has been updated in the cache reflecting career value of "Dentist"
        billionaireService.updateBillionaireCareer(1L, "Dentist");
        cachedBillionaire = billionaireCache.get(1L, Billionaire.class);
        assertThat(cachedBillionaire).isNotNull();
        assertThat(cachedBillionaire.getCareer()).isEqualTo("Dentist");

        verify(billionareRepository, times(2)).findById(eq(1L));
        verify(billionareRepository, times(2)).save(any(Billionaire.class));

    }

    @Test
    public void deleteBillionaire_withNonExistentBillionaireReference_shouldThrowNotFoundException() {

        when(billionareRepository.findById(eq(1L))).thenReturn(null);

        assertThrows(BillionaireNotFoundException.class, () -> {
            billionaireService.deleteBillionaire(10000L);
        });
    }

    @Test
    public void deleteBillionaire_withValidBillionaireReference_shouldDeleteTheBillionaireReferenceInTheCacheAndDB()
            throws BillionaireNotFoundException {

        Billionaire cachedBillionaire = billionaireCache.get(1L, Billionaire.class);
        assertThat(cachedBillionaire).isNull();

        Billionaire billionareReference = Billionaire.builder()
                .id(1L)
                .firstName("Steve")
                .lastName("Bezos")
                .build();

        //Adds the entry to the cache...
        billionaireCache.put(1L, billionareReference);
        cachedBillionaire = billionaireCache.get(1L, Billionaire.class);
        assertThat(cachedBillionaire).isNotNull();

        when(billionareRepository.findById(eq(1L))).thenReturn(Optional.of(billionareReference));
        doNothing().when(billionareRepository).delete(any(Billionaire.class));

        //Attempts to delete the entry...
        billionaireService.deleteBillionaire(1L);

        //Verifies that the cache entry got evicted from the cache...
        cachedBillionaire = billionaireCache.get(1L, Billionaire.class);
        assertThat(cachedBillionaire).isNull();

        verify(billionareRepository, times(1)).findById(eq(1L));
        verify(billionareRepository, times(1)).delete(any(Billionaire.class));
    }

}
