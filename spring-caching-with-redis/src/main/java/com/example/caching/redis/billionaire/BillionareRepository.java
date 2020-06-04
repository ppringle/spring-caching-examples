package com.example.caching.redis.billionaire;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillionareRepository extends JpaRepository<Billionaire, Long> {

}
