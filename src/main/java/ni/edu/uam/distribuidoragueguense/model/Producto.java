package ni.edu.uam.distribuidoragueguense.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {
    private String codigo;
    private String nombre;
    private String categoria;
    private  Double precio;
    private Integer existencia;
}
