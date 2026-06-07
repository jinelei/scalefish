package com.jinelei.scalefish.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "calendars")
@Getter
@Setter
@NoArgsConstructor
public class CalendarEntity extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(name = "display_color", length = 7)
    private String displayColor;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
