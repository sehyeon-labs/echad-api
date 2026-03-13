package kr.co.onmediagroup.template.repository;

import kr.co.onmediagroup.template.model.dto.Template;
import kr.co.onmediagroup.template.model.dto.TemplateCustom;
import kr.co.onmediagroup.template.model.entity.TemplateCustomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface TemplateCustomRepository extends JpaRepository<TemplateCustomEntity, Integer> {

  @Query("""
    SELECT DISTINCT tc FROM TemplateCustomEntity tc
      JOIN FETCH tc.template t
      LEFT JOIN FETCH tc.images tci
    WHERE tc.templateCustomId IN :ids
      AND t.activeYn = :activeYn
      AND t.deletedAt IS NULL
  """)
  List<TemplateCustomEntity> findAllWithDetailsByIds(
      @Param("ids") List<Integer> ids,
      @Param("activeYn") Template.ActiveYn activeYn);
}
