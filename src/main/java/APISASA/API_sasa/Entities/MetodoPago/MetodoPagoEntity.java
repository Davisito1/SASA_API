package APISASA.API_sasa.Entities.MetodoPago;

import APISASA.API_sasa.Entities.Pagos.PagosEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "METODOPAGO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MetodoPagoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "metodoPago_seq")
    @SequenceGenerator(name = "metodoPago_seq", sequenceName = "SEQ_METODOPAGO", allocationSize = 1)
    @Column(name = "IDMETODOPAGO")
    private Long idMetodoPago;

    @Column(name = "METODO", nullable = false, length = 100)
    private String metodo;

    @OneToMany(mappedBy = "metodoPago", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PagosEntity> pagos = new ArrayList<>();
}
