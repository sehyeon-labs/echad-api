package kr.co.onmediagroup.template.repository;

import kr.co.onmediagroup.template.model.entity.TemplateCustomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TemplateCustomRepository extends JpaRepository<TemplateCustomEntity, Integer> {

  @Query("SELECT DISTINCT tc FROM TemplateCustomEntity tc " +
         "JOIN FETCH tc.template t " +
         "LEFT JOIN FETCH tc.images i " +
         "WHERE tc.custom_id = :customId " +
         "AND tc.deletedAt IS NULL " +
         "AND t.activeYn = 'Y' " +
         "AND t.deletedAt IS NULL " +
         "ORDER BY tc.sortOrder ASC")
  List<TemplateCustomEntity> findByCustomIdWithActiveTemplate(@Param("customId") String customId);
}
