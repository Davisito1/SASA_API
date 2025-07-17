package APISASA.API_sasa.Services;

import APISASA.API_sasa.Entities.VehicleEntity;
import APISASA.API_sasa.Models.DTO.VehicleDTO;
import APISASA.API_sasa.Repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleService {
    @Autowired
    VehicleRepository repo;

    public  List<VehicleDTO> obtenerVehiculos(){
        List<VehicleEntity> lista = repo.findAll();
        return lista.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    private  VehicleDTO convertirADTO (VehicleEntity vehicleEntity){
        //Creando objeto para retornar
        VehicleDTO dto = new VehicleDTO();
        //Transferir los datos del entity al dto
        dto.setId(vehicleEntity.getId());
        dto.setMarca(vehicleEntity.getMarca());
        dto.setModelo(vehicleEntity.getModelo());
        dto.setAnio(vehicleEntity.getAnio());
        dto.setPlaca(vehicleEntity.getPlaca());
        dto.setVin(vehicleEntity.getVin());
        dto.setIdCliente(vehicleEntity.getIdCliente());
        dto.setIdEstado(vehicleEntity.getIdEstado());


        return dto;
    }
}
