package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Karyawan;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Karyawan entity.
 */
@SuppressWarnings("unused")
@Repository
public interface KaryawanRepository extends JpaRepository<Karyawan,Long> {
    
}
