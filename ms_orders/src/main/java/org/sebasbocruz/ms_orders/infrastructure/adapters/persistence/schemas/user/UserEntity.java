package org.sebasbocruz.ms_orders.infrastructure.adapters.persistence.schemas.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Table(schema = "users", name ="users")
public class UserEntity {

    @Id
    @Column("user_id")
    private Long userID;

    @Column("client_id")
    private Long clientID;

    @Column("address_id")
    private Long addressID;

    @Column("email")
    private String email;

    @Column("phone")
    private String phone;

}
