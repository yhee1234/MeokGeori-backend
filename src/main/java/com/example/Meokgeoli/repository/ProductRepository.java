package com.example.Meokgeoli.repository;

import com.example.Meokgeoli.entity.Product;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
        select p from Product p
        join fetch p.store s
        where p.baseProductId in :baseIds
    """)
    List<Product> findAllCandidates(@Param("baseIds") List<Long> baseIds);
}

