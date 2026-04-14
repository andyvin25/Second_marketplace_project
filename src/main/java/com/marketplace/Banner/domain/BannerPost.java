//package com.marketplace.Banner.domain;
//
//import org.hibernate.annotations.GenericGenerator;
//
//import com.marketplace.Util.Auditable;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.EnumType;
//import jakarta.persistence.Enumerated;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import jakarta.persistence.Table;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@Entity
//@Table(name = "`Banner_Posts`")
//@Getter @Setter @NoArgsConstructor
//public class BannerPost extends Auditable {
//
//    @SuppressWarnings("deprecation")
//    @Id
//    @GeneratedValue(generator = "custom-generator")
//    @GenericGenerator(name = "custom-generator",
//        strategy = "com.marketplace.Util.CustomGenerator"
//    )
//    private String id;
//
//    public enum MediaTypeEnum {
//        IMAGE,
//        VIDEO
//    }
//
//    @Column(name = "media_type", length = 10, nullable = false)
//    @Enumerated(EnumType.STRING)
//    private MediaTypeEnum mediaType;
//
//    @Column(name = "media_path", length = 300, nullable = false)
//    private String mediaPath;
//
//    @Column(name = "order", nullable = false)
//    private int order;
//
//    @ManyToOne
//    @JoinColumn(name = "banner_id", nullable = false, referencedColumnName = "id")
//    private Banner banner;
//
//}
