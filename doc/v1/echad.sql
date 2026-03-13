CREATE TABLE `user` (
  user_id VARCHAR(255) NOT NULL PRIMARY KEY COMMENT 'PK',
  user_password VARCHAR(255) NOT NULL COMMENT '비밀번호',
  user_email VARCHAR(255) NOT NULL COMMENT '이메일',
  phone_number VARCHAR(20) COMMENT '휴대폰 번호 (국가번호 포함)',
  phone_verified_yn CHAR(1) NOT NULL DEFAULT 'N' COMMENT '휴대폰 인증 여부',
  user_level ENUM('ADMIN', 'USER') NOT NULL COMMENT '레벨',
  active_yn CHAR(1) NOT NULL DEFAULT 'Y' COMMENT '활동상태',
  login_fail_count INT DEFAULT 0 NOT NULL COMMENT '로그인 실패 횟수',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '생성 일시',
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시'
) COMMENT='유저';

CREATE TABLE user_info (
  info_id VARCHAR(255) NOT NULL PRIMARY KEY COMMENT 'PK',
  user_id VARCHAR(255) NOT NULL COMMENT '유저 내부 아이디',
  groom_name VARCHAR(100) NOT NULL COMMENT '신랑',
  bride_name VARCHAR(100) NOT NULL COMMENT '신부',
  wedding_date DATETIME COMMENT '결혼식 날짜',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '생성 일시',
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시',
  UNIQUE INDEX uq__info_id__user_id (info_id, user_id) COMMENT '유저별 상세정보 유니크 인덱스',
  INDEX idx__user_id__wedding_date (user_id, wedding_date) COMMENT '유저, 결혼날짜 인덱스'
) COMMENT '유저 상세 정보';

CREATE TABLE `template` (
  template_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT 'PK',
  title VARCHAR(255) NOT NULL COMMENT '템플릿 제목',
  component ENUM('HEADER', 'TEXT', 'BUTTON', 'IMG', 'CONTENT', 'LOCATION') NOT NULL DEFAULT 'TEXT' COMMENT '템플릿 요소',
  component_type INT NOT NULL DEFAULT 1 COMMENT '템플릿 요소 타입',
  preview_url VARCHAR(255) NOT NULL COMMENT '템플릿 미리보기 URL',
  is_premium CHAR(1) NOT NULL DEFAULT 'N' COMMENT '유료 여부',
  template_schema JSON NULL COMMENT '레이아웃, 기본 스타일 정의',
  sort_order INT DEFAULT 0 NOT NULL COMMENT '노출 순서',
  active_yn CHAR(1) NOT NULL DEFAULT 'N' COMMENT '활동상태',
  deleted_at DATETIME NULL DEFAULT NULL COMMENT '삭제 일시',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '생성 일시',
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시'
) COMMENT='템플릿';

CREATE TABLE `custom` (
  custom_id VARCHAR(255) NOT NULL PRIMARY KEY COMMENT 'PK (게시물 ID)',
  user_id VARCHAR(255) NOT NULL COMMENT '유저 아이디',
  active_yn CHAR(1) NOT NULL DEFAULT 'N' COMMENT '활동상태',
  start_date DATETIME NULL COMMENT '시작 일시',
  end_date DATETIME NULL COMMENT '종료 일시',
  deleted_at DATETIME NULL DEFAULT NULL COMMENT '삭제 일시',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '생성 일시',
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시',
  INDEX idx__user_id__deleted_at (user_id, deleted_at)
) COMMENT='사용자 게시물(모바일 청첩장 등)';

CREATE TABLE `template_custom` (
  template_custom_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'PK',
  user_id VARCHAR(255) NOT NULL COMMENT '유저 아이디',
  template_id INT NOT NULL COMMENT '원본 템플릿 ID (FK)',
  custom_schema JSON NULL COMMENT '사용자 커스텀 설정 (색상, 크기, 위치 등)',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '생성 일시',
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시'
) COMMENT='사용자 커스텀 템플릿 요소 저장소';

CREATE TABLE `custom_template` (
  custom_template_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'PK',
  custom_id VARCHAR(255) NOT NULL COMMENT '게시물 ID (FK)',
  template_custom_id INT NOT NULL COMMENT '커스텀 템플릿 요소 ID (FK)',
  sort_order INT NOT NULL DEFAULT 0 COMMENT '정렬 순서 (0부터 시작)',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '생성 일시',
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시',
  INDEX idx__custom_id__sort_order (custom_id, sort_order)
) COMMENT='게시물별 커스텀 템플릿 매핑 및 순서 관리';

CREATE TABLE `template_custom_image` (
  template_custom_image_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'PK',
  template_custom_id INT NOT NULL COMMENT '커스텀 템플릿 ID (FK)',
  image_url VARCHAR(255) NOT NULL COMMENT '이미지 URL',
  sort_order INT NOT NULL DEFAULT 0 COMMENT '정렬 순서 (0부터 시작)',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '생성 일시',
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시',
  INDEX idx__template_custom_id__sort_order (template_custom_id, sort_order) COMMENT '커스텀 템플릿별 이미지 정렬 인덱스'
) COMMENT='사용자 커스텀 템플릿별 이미지 리스트';