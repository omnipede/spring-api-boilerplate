package io.omnipede.boilerplate.domain.user;

import io.omnipede.boilerplate.domain.order.Order;
import io.omnipede.boilerplate.domain.product.Product;
import io.omnipede.boilerplate.system.utils.AESCryptoConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "t_user")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    @Convert(converter = AESCryptoConverter.class)
    private String password;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Date updatedAt;

    public Order order(Product product) {
        return Order.builder()
            .user(this)
            .product(product)
            .build();
    }

    public Boolean login(String password) {
        return Objects.equals(this.password, password);
    }
}
