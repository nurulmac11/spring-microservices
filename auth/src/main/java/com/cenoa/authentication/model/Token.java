package com.cenoa.authentication.model;

import com.cenoa.authentication.dto.TokenType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Token {

  @Id
  @GeneratedValue
  public Integer id;

  @Column(unique = true)
  public String token;

  @Enumerated(EnumType.STRING)
  public TokenType tokenType = TokenType.BEARER;

  public boolean revoked;
  public boolean expired;

  private Date created = new Date();
  private Date updated = new Date();

  @PrePersist
  protected void prePersist() {
    if (this.created == null) created = new Date();
    if (this.updated == null) updated = new Date();
  }

  @PreUpdate
  protected void preUpdate() {
    this.updated= new Date();
  }

  @ManyToOne
  @JoinColumn(name = "user_id")
  public User user;
}
