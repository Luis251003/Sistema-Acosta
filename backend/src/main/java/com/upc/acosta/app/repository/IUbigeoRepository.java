package com.upc.acosta.app.repository;

import com.upc.acosta.app.dto.UbigeoDTOOpcion;
import com.upc.acosta.app.entity.Ubigeo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IUbigeoRepository extends JpaRepository<Ubigeo, String> {

    @Query("""
        SELECT DISTINCT new com.upc.acosta.app.dto.UbigeoDTOOpcion(SUBSTRING(u.id, 1, 2), u.departamento)
        FROM Ubigeo u
        ORDER BY u.departamento
        """)
    List<UbigeoDTOOpcion> findDepartamentos();

    @Query("""
        SELECT DISTINCT new com.upc.acosta.app.dto.UbigeoDTOOpcion(SUBSTRING(u.id, 1, 4), u.provincia)
        FROM Ubigeo u
        WHERE u.id LIKE CONCAT(:departamento, '%')
        ORDER BY u.provincia
        """)
    List<UbigeoDTOOpcion> findProvincias(@Param("departamento") String departamento);

    @Query("""
        SELECT new com.upc.acosta.app.dto.UbigeoDTOOpcion(u.id, u.distrito)
        FROM Ubigeo u
        WHERE u.id LIKE CONCAT(:provincia, '%')
        ORDER BY u.distrito
        """)
    List<UbigeoDTOOpcion> findDistritos(@Param("provincia") String provincia);

}
