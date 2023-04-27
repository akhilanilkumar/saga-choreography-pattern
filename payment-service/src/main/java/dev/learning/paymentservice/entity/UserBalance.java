package dev.learning.paymentservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="USER_BALANCE")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserBalance {

    @Id
    private int id;

    private int price;
}
