package com.example.caching.billionaire;

import com.example.caching.billionaire.Billionaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillionareRepository extends JpaRepository<Billionaire, Long> {

}
