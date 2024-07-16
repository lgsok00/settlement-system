package com.sparta.settlementsystem.streaming.repository;

import com.sparta.settlementsystem.streaming.entity.Ad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Ad 엔티티에 대한 데이터 액세스를 제공하는 repository 입니다.
 * JpaRepository 를 상속받아 기본적인 CRUD 기능을 제공합니다.
 */
@Repository
public interface AdRepository extends JpaRepository<Ad, Long> {
  // 기본 CRUD 메서드는 JpaRepository 에서 제공됩니다.
}
