package com.sparta.settlementsystem.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;
import java.time.Instant;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractTimeStamp {

  @Column(updatable = false)
  private Timestamp createdAt;

  private Timestamp updatedAt;

  @PrePersist
  protected void onCreate() {
    Timestamp now = Timestamp.from(Instant.now());
    this.createdAt = now;
    this.updatedAt = now;
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = Timestamp.from(Instant.now());
  }
}
