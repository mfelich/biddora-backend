package com.example.biddora_backend.rating.entity;

import com.example.biddora_backend.product.entity.Product;
import com.example.biddora_backend.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "ratings")
public class Rating {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id",nullable = false)
    private Product product;

    @Column(name = "comment")
    private String comment;

    @Column(name = "rating_stars")
    private Integer ratingStars;

    @Column(name = "rating_date")
    private LocalDateTime ratingDate;
}
