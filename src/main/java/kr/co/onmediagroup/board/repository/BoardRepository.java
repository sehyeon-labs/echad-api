package kr.co.onmediagroup.board.repository;

import kr.co.onmediagroup.board.model.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, String> {
  Page<BoardEntity> findAllByUserId(String userId, Pageable pageable);

  Optional<BoardEntity> findByBoardIdAndUserId(String boardId, String userId);

}
