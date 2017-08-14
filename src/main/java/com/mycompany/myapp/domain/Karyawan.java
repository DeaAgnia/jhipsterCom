package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Karyawan.
 */
@Entity
@Table(name = "karyawan")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Karyawan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nama", nullable = false)
    private String nama;

    @ManyToOne(optional = false)
    @NotNull
    private Company company;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public Karyawan nama(String nama) {
        this.nama = nama;
        return this;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public Company getCompany() {
        return company;
    }

    public Karyawan company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Karyawan karyawan = (Karyawan) o;
        if (karyawan.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), karyawan.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Karyawan{" +
            "id=" + getId() +
            ", nama='" + getNama() + "'" +
            "}";
    }
}
