package org.vasaviyuvajanasangha.kvcl.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccommodationDetails {

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "date", column = @Column(name = "sep26")),
            @AttributeOverride(name = "numberOfAccommodations", column = @Column(name = "sep26_num"))
    })
    private AccommodationDates sept26th;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "date", column = @Column(name = "sep27")),
            @AttributeOverride(name = "numberOfAccommodations", column = @Column(name = "sep27_num"))
    })
    private AccommodationDates sept27th;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "date", column = @Column(name = "sep28")),
            @AttributeOverride(name = "numberOfAccommodations", column = @Column(name = "sep28_num"))
    })
    private AccommodationDates sept28th;

    private String lodge;
    private String poc;

}
