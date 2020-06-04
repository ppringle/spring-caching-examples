package com.example.caching.redis.billionaire;

import com.example.caching.redis.SpringCachingWithRedisApplication;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = SpringCachingWithRedisApplication.class)
@ContextConfiguration(classes = {TestCachingConfig.class},initializers = BillionaireServiceTest.Initializer.class)
@Testcontainers
public class BillionaireServiceTest {
//
//    @Container
//    public static GenericContainer redis = new GenericContainer<>("redis:buster")
//            .withExposedPorts(6379).withStartupTimeout(Duration.ofSeconds(120));

    @Autowired
    private Environment environment;

    @Autowired
    private BillionaireService billionaireService;

    @Autowired
    private BillionareRepository billionareRepository;

    @Autowired
    private CacheManager cacheManager;

    private Cache billionaireCache;
//
//    @BeforeClass
//    public static void before() {
//        redis.start();
//    }
//
//    @AfterClass
//    public static void after() {
//        redis.stop();
//    }

    @BeforeEach
    public void init() {
        billionaireCache = cacheManager.getCache("billionaires");
        assertThat(billionaireCache).isNotNull();
    }

    @Test
    public void retrieveAllBillionaires_shouldReturnAllAvailableEntries() {

        List<Billionaire> billionaires = billionaireService.retrieveAllBillionaires();
        assertThat(billionaires).isNotEmpty();

        List<String> lastNames = billionaires.stream().map(b -> b.getLastName()).collect(Collectors.toList());
        assertThat(lastNames).containsAnyOf("Bezos", "Yinglin", "Zuckerberg", "Arnault", "Walton", "Dangote",
                "Gates", "Alakija");

    }

    @Test
    public void retrieveBillionaireById_shouldUseCacheIfBillionaireEntryIsCached_elseRetrieveEntryFromDB() {

        Billionaire billionaire = billionaireService.retrieveBillionareById(1L);
        assertThat(billionaire).isNotNull();

        Billionaire billionaireSecondRetrieval = billionaireService.retrieveBillionareById(1L);
        assertThat(billionaire).isEqualTo(billionaireSecondRetrieval);

        billionaireCache.invalidate();

        Billionaire billionaireThirdRetrieval = billionaireService.retrieveBillionareById(1L);
        assertThat(billionaireThirdRetrieval).isNotNull();

        assertThat(billionaireThirdRetrieval).isNotEqualTo(billionaire);

    }

    @Test
    public void updateBillionaireCareer_withNonExistentBillionaireReference_shouldThrowNotFoundException() {

        assertThrows(BillionaireNotFoundException.class, () -> {
            billionaireService.updateBillionaireCareer(10000L, "");
        });

    }

    @Test
    public void updateBillionaireCareer_withValidBillionaireReference_shouldUpdateTheBillionaireReferenceInTheCacheAndDB() throws BillionaireNotFoundException {

        Billionaire originalBillionaireRef = billionaireService.retrieveBillionareById(1L);
        assertThat(originalBillionaireRef).isNotNull();
        assertThat(originalBillionaireRef.getLastName()).isEqualTo("Bezos");
        assertThat(originalBillionaireRef.getCareer()).isEqualTo("Tech Entrepreneur");

        Billionaire cachedBillionaireRef = billionaireCache.get(1L, Billionaire.class);
        assertThat(originalBillionaireRef).isEqualTo(cachedBillionaireRef);

        Billionaire updatedBillionaireRef = billionaireService.updateBillionaireCareer(1L, "Farmer");
        assertThat(updatedBillionaireRef).isNotNull();
        assertThat(updatedBillionaireRef.getLastName()).isEqualTo("Bezos");
        assertThat(updatedBillionaireRef.getCareer()).isEqualTo("Farmer");

        cachedBillionaireRef = billionaireCache.get(1L, Billionaire.class);
        assertThat(originalBillionaireRef).isNotEqualTo(cachedBillionaireRef);

    }

    @Test
    public void deleteBillionaire_withNonExistentBillionaireReference_shouldThrowNotFoundException() {

        assertThrows(BillionaireNotFoundException.class, () -> {
            billionaireService.deleteBillionaire(10000L);
        });

    }

    @Test
    public void deleteBillionaire_withValidBillionaireReference_shouldDeleteTheBillionaireReferenceInTheCacheAndDB() throws BillionaireNotFoundException {

        Billionaire originalBillionaireRef = billionaireService.retrieveBillionareById(1L);
        assertThat(originalBillionaireRef).isNotNull();
        assertThat(originalBillionaireRef.getLastName()).isEqualTo("Bezos");

        Billionaire cachedBillionaireRef = billionaireCache.get(1L, Billionaire.class);
        assertThat(originalBillionaireRef).isEqualTo(cachedBillionaireRef);

        billionaireService.deleteBillionaire(1L);

        cachedBillionaireRef = billionaireCache.get(1L, Billionaire.class);
        assertThat(cachedBillionaireRef).isEqualTo(null);

        assertThat(billionareRepository.findById(1L).orElse(null)).isEqualTo(null);

    }

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
//            TestPropertyValues.of(
////                    "spring.redis.host=" + redis.getHost(),
////                    "spring.redis.port=" + redis.getFirstMappedPort()
//                    "spring.redis.host=localhost",
//                    "spring.redis.port=6379"
//            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}
