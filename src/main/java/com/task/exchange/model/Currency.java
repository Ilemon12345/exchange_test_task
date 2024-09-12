package com.task.exchange.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(value = AccessLevel.PACKAGE)
@Getter
@ToString
@EqualsAndHashCode
public class Currency {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    private String currency;

    @JdbcTypeCode(SqlTypes.JSON)
    private List<Rate> rates;

    private LocalDate creationDate;
}
