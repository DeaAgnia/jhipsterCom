package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Karyawan;
import com.mycompany.myapp.repository.KaryawanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Karyawan.
 */
@Service
@Transactional
public class KaryawanService {

    private final Logger log = LoggerFactory.getLogger(KaryawanService.class);

    private final KaryawanRepository karyawanRepository;

    public KaryawanService(KaryawanRepository karyawanRepository) {
        this.karyawanRepository = karyawanRepository;
    }

    /**
     * Save a karyawan.
     *
     * @param karyawan the entity to save
     * @return the persisted entity
     */
    public Karyawan save(Karyawan karyawan) {
        log.debug("Request to save Karyawan : {}", karyawan);
        return karyawanRepository.save(karyawan);
    }

    /**
     *  Get all the karyawans.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Karyawan> findAll(Pageable pageable) {
        log.debug("Request to get all Karyawans");
        return karyawanRepository.findAll(pageable);
    }

    /**
     *  Get one karyawan by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Karyawan findOne(Long id) {
        log.debug("Request to get Karyawan : {}", id);
        return karyawanRepository.findOne(id);
    }

    /**
     *  Delete the  karyawan by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Karyawan : {}", id);
        karyawanRepository.delete(id);
    }
}
