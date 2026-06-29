package com.jinelei.scalefish.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "external_links")
@Getter
@Setter
@NoArgsConstructor
public class ExternalLink extends BaseEntity {

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 2048)
    private String url;

    @Column(length = 50)
    private String icon;

    @Column(name = "sort_order")
    private Integer sortOrder;

    public ExternalLink(String name, String url, String icon, Integer sortOrder) {
        this.name = name;
        this.url = url;
        this.icon = icon;
        this.sortOrder = sortOrder;
    }
}
