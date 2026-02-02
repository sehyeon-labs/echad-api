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