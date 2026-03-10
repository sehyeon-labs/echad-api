package kr.co.onmediagroup.template.repository;

import kr.co.onmediagroup.template.model.entity.TemplateCustomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TemplateCustomRepository extends JpaRepository<TemplateCustomEntity, Integer> {

  @Query("SELECT tc FROM TemplateCustomEntity tc " +
         "JOIN FETCH tc.template t " +
         "WHERE tc.customId = :customId " +
         "AND tc.deletedAt IS NULL " +
         "AND t.activeYn = 'Y' " +
         "AND t.deletedAt IS NULL " +
         "ORDER BY tc.sortOrder ASC")
  List<TemplateCustomEntity> findByCustomIdWithActiveTemplate(@Param("customId") String customId);
}
