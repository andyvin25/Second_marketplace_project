//package com.marketplace.Banner.domain;
//
//import java.util.HashSet;
//import java.util.Set;
//
//import org.hibernate.annotations.GenericGenerator;
//
//import com.marketplace.Util.Auditable;
//
//import jakarta.persistence.CascadeType;
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.FetchType;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import jakarta.persistence.OneToMany;
//import jakarta.persistence.Table;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@Entity(name = "Banner")
//@Table(name = "`Banners`")
//@Getter @Setter @NoArgsConstructor
//public class Banner extends Auditable{
//
//    @SuppressWarnings("deprecation")
//    @Id
//    @GeneratedValue(generator = "custom-generator")
//    @GenericGenerator(name = "custom-generator",
//        strategy = "com.marketplace.Util.CustomGenerator"
//    )
//    private String id;
//
//    @Column(name = "order", nullable = false)
//    private int order;
//
//    @ManyToOne
//    @JoinColumn(name = "store_id", nullable = false, referencedColumnName = "id")
//    private Store store;
//
//    @OneToMany(mappedBy = "banner", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
//    private Set<BannerPost> bannerPosts = new HashSet<BannerPost>();
//
//}
