package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.MyCompanyApp;

import com.mycompany.myapp.domain.Karyawan;
import com.mycompany.myapp.domain.Company;
import com.mycompany.myapp.repository.KaryawanRepository;
import com.mycompany.myapp.service.KaryawanService;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the KaryawanResource REST controller.
 *
 * @see KaryawanResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyCompanyApp.class)
public class KaryawanResourceIntTest {

    private static final String DEFAULT_NAMA = "AAAAAAAAAA";
    private static final String UPDATED_NAMA = "BBBBBBBBBB";

    @Autowired
    private KaryawanRepository karyawanRepository;

    @Autowired
    private KaryawanService karyawanService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restKaryawanMockMvc;

    private Karyawan karyawan;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        KaryawanResource karyawanResource = new KaryawanResource(karyawanService);
        this.restKaryawanMockMvc = MockMvcBuilders.standaloneSetup(karyawanResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Karyawan createEntity(EntityManager em) {
        Karyawan karyawan = new Karyawan()
            .nama(DEFAULT_NAMA);
        // Add required entity
        Company company = CompanyResourceIntTest.createEntity(em);
        em.persist(company);
        em.flush();
        karyawan.setCompany(company);
        return karyawan;
    }

    @Before
    public void initTest() {
        karyawan = createEntity(em);
    }

    @Test
    @Transactional
    public void createKaryawan() throws Exception {
        int databaseSizeBeforeCreate = karyawanRepository.findAll().size();

        // Create the Karyawan
        restKaryawanMockMvc.perform(post("/api/karyawans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(karyawan)))
            .andExpect(status().isCreated());

        // Validate the Karyawan in the database
        List<Karyawan> karyawanList = karyawanRepository.findAll();
        assertThat(karyawanList).hasSize(databaseSizeBeforeCreate + 1);
        Karyawan testKaryawan = karyawanList.get(karyawanList.size() - 1);
        assertThat(testKaryawan.getNama()).isEqualTo(DEFAULT_NAMA);
    }

    @Test
    @Transactional
    public void createKaryawanWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = karyawanRepository.findAll().size();

        // Create the Karyawan with an existing ID
        karyawan.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restKaryawanMockMvc.perform(post("/api/karyawans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(karyawan)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Karyawan> karyawanList = karyawanRepository.findAll();
        assertThat(karyawanList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNamaIsRequired() throws Exception {
        int databaseSizeBeforeTest = karyawanRepository.findAll().size();
        // set the field null
        karyawan.setNama(null);

        // Create the Karyawan, which fails.

        restKaryawanMockMvc.perform(post("/api/karyawans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(karyawan)))
            .andExpect(status().isBadRequest());

        List<Karyawan> karyawanList = karyawanRepository.findAll();
        assertThat(karyawanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllKaryawans() throws Exception {
        // Initialize the database
        karyawanRepository.saveAndFlush(karyawan);

        // Get all the karyawanList
        restKaryawanMockMvc.perform(get("/api/karyawans?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(karyawan.getId().intValue())))
            .andExpect(jsonPath("$.[*].nama").value(hasItem(DEFAULT_NAMA.toString())));
    }

    @Test
    @Transactional
    public void getKaryawan() throws Exception {
        // Initialize the database
        karyawanRepository.saveAndFlush(karyawan);

        // Get the karyawan
        restKaryawanMockMvc.perform(get("/api/karyawans/{id}", karyawan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(karyawan.getId().intValue()))
            .andExpect(jsonPath("$.nama").value(DEFAULT_NAMA.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingKaryawan() throws Exception {
        // Get the karyawan
        restKaryawanMockMvc.perform(get("/api/karyawans/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateKaryawan() throws Exception {
        // Initialize the database
        karyawanService.save(karyawan);

        int databaseSizeBeforeUpdate = karyawanRepository.findAll().size();

        // Update the karyawan
        Karyawan updatedKaryawan = karyawanRepository.findOne(karyawan.getId());
        updatedKaryawan
            .nama(UPDATED_NAMA);

        restKaryawanMockMvc.perform(put("/api/karyawans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedKaryawan)))
            .andExpect(status().isOk());

        // Validate the Karyawan in the database
        List<Karyawan> karyawanList = karyawanRepository.findAll();
        assertThat(karyawanList).hasSize(databaseSizeBeforeUpdate);
        Karyawan testKaryawan = karyawanList.get(karyawanList.size() - 1);
        assertThat(testKaryawan.getNama()).isEqualTo(UPDATED_NAMA);
    }

    @Test
    @Transactional
    public void updateNonExistingKaryawan() throws Exception {
        int databaseSizeBeforeUpdate = karyawanRepository.findAll().size();

        // Create the Karyawan

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restKaryawanMockMvc.perform(put("/api/karyawans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(karyawan)))
            .andExpect(status().isCreated());

        // Validate the Karyawan in the database
        List<Karyawan> karyawanList = karyawanRepository.findAll();
        assertThat(karyawanList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteKaryawan() throws Exception {
        // Initialize the database
        karyawanService.save(karyawan);

        int databaseSizeBeforeDelete = karyawanRepository.findAll().size();

        // Get the karyawan
        restKaryawanMockMvc.perform(delete("/api/karyawans/{id}", karyawan.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Karyawan> karyawanList = karyawanRepository.findAll();
        assertThat(karyawanList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Karyawan.class);
        Karyawan karyawan1 = new Karyawan();
        karyawan1.setId(1L);
        Karyawan karyawan2 = new Karyawan();
        karyawan2.setId(karyawan1.getId());
        assertThat(karyawan1).isEqualTo(karyawan2);
        karyawan2.setId(2L);
        assertThat(karyawan1).isNotEqualTo(karyawan2);
        karyawan1.setId(null);
        assertThat(karyawan1).isNotEqualTo(karyawan2);
    }
}
