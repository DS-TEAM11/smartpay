package org.shds.smartpay.repository;

import org.shds.smartpay.entity.BinTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BinTableRepository extends JpaRepository<BinTable, Integer> {

    @Query("SELECT b FROM BinTable b WHERE b.bin = :bin")
    Optional<BinTable> findByBin(@Param("bin") String bin);
}
