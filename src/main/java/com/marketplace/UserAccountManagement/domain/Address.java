package com.marketplace.UserAccountManagement.domain;

import com.marketplace.Util.Auditable;
import com.marketplace.Util.GeneratedId;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "`Addresses`")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Builder
public class Address extends Auditable {
    @Id
    @GeneratedId
    private String id;

    @Column(name = "recipient_name", length = 30, nullable = false)
    private String recipientName;

    @Column(name = "recipient_number", length = 15, nullable = false)
    private String recipientNumber;

    public enum AddressLabelEnum {
        RUMAH,
        APARTEMENT,
        KANTOR,
        KOSAN
    }

    @Column(name = "address_label", length = 100, nullable = false)
    @Enumerated(EnumType.STRING)
    private AddressLabelEnum addressLabel;

    @Column(name = "city_&_subsidiary", length =100, nullable = false)
    private String cityAndSubsidiary;

    @Column(name = "complete_address", length = 100, nullable = false)
    private String completeAddress;

    @Column(name = "is_picked", nullable = false)
    private Boolean isPicked;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    private User user;

}
