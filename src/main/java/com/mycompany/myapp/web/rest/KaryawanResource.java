package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Karyawan;
import com.mycompany.myapp.service.KaryawanService;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.time.Clock;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Karyawan.
 */
@RestController
@RequestMapping("/api")
public class KaryawanResource {

    private final Logger log = LoggerFactory.getLogger(KaryawanResource.class);

    private static final String ENTITY_NAME = "karyawan";

    private final KaryawanService karyawanService;

    public KaryawanResource(KaryawanService karyawanService) {
        this.karyawanService = karyawanService;
    }

    /**
     * POST  /karyawans : Create a new karyawan.
     *
     * @param karyawan the karyawan to create
     * @return the ResponseEntity with status 201 (Created) and with body the new karyawan, or with status 400 (Bad Request) if the karyawan has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/karyawans")
    @Timed
    public ResponseEntity<Karyawan> createKaryawan(@Valid @RequestBody Karyawan karyawan) throws URISyntaxException {
        System.out.println("test");
        log.debug("REST request to save Karyawan : {}", karyawan);
        if (karyawan.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new karyawan cannot already have an ID")).body(null);
        }
        Karyawan result = karyawanService.save(karyawan);
        return ResponseEntity.created(new URI("/api/karyawans/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
           .body(result);
    }

    /**
     * PUT  /karyawans : Updates an existing karyawan.
     *
     * @param karyawan the karyawan to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated karyawan,
     * or with status 400 (Bad Request) if the karyawan is not valid,
     * or with status 500 (Internal Server Error) if the karyawan couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/karyawans")
    @Timed
    public ResponseEntity<Karyawan> updateKaryawan(@Valid @RequestBody Karyawan karyawan) throws URISyntaxException {
        log.debug("REST request to update Karyawan : {}", karyawan);
        if (karyawan.getId() == null) {
            return createKaryawan(karyawan);
        }
        Karyawan result = karyawanService.save(karyawan);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, karyawan.getId().toString()))
            .body(result);
    }

    /**
     * GET  /karyawans : get all the karyawans.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of karyawans in body
     */
    @GetMapping("/karyawans")
    @Timed
    public ResponseEntity<List<Karyawan>> getAllKaryawans(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Karyawans");
        Page<Karyawan> page = karyawanService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/karyawans");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /karyawans/:id : get the "id" karyawan.
     *
     * @param id the id of the karyawan to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the karyawan, or with status 404 (Not Found)
     */
    @GetMapping("/karyawans/{id}")
    @Timed
    public ResponseEntity<Karyawan> getKaryawan(@PathVariable Long id) {
        log.debug("REST request to get Karyawan : {}", id);
        Karyawan karyawan = karyawanService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(karyawan));
    }

    /**
     * DELETE  /karyawans/:id : delete the "id" karyawan.
     *
     * @param id the id of the karyawan to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/karyawans/{id}")
    @Timed
    public ResponseEntity<Void> deleteKaryawan(@PathVariable Long id) {
        log.debug("REST request to delete Karyawan : {}", id);
        karyawanService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
