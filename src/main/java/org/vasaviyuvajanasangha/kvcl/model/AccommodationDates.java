package org.vasaviyuvajanasangha.kvcl.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccommodationDates {
    private String date;
    private String numberOfAccommodations;
}
