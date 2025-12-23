package com.back.entity;

import com.back.jpa.entity.BaseIdAndTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Post extends BaseIdAndTime {
    @ManyToOne(fetch = FetchType.LAZY)
    Member author;
    String title;
    @Column(columnDefinition = "LONGTEXT")
    String content;

    public Post(Member member, String title, String content) {
        this.author = member;
        this.title = title;
        this.content = content;
    }
}
