package com.example.SmartAppointmentBookingSystem.service.ServiceImpl;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.example.SmartAppointmentBookingSystem.dto.tenant.TenantRequestDTO;
import com.example.SmartAppointmentBookingSystem.dto.tenant.TenantResponseDTO;
import com.example.SmartAppointmentBookingSystem.entity.Tenant;
import com.example.SmartAppointmentBookingSystem.exception.ResourceNotFoundException;
import com.example.SmartAppointmentBookingSystem.repository.TenantRepository;
import com.example.SmartAppointmentBookingSystem.service.TenantService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TenantServiceImplementation implements TenantService{

    private final TenantRepository tenantRepo;

    @Override
    public List<TenantResponseDTO> getAllTenants() {
        return tenantRepo.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TenantResponseDTO getTenantById(Long id) {
        Tenant tenant = tenantRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found with id: " + id));
        return toResponseDTO(tenant);
    }

    @Override
    public TenantResponseDTO addTenant(TenantRequestDTO tenantRequestDTO) {
       if (tenantRepo.findByEmail(tenantRequestDTO.getEmail()).isPresent()) {
            throw new ResourceNotFoundException("Tenant with email already exists");
        }

        Tenant tenant = toEntity(tenantRequestDTO);
        Tenant savedTenant = tenantRepo.save(tenant);
        return toResponseDTO(savedTenant);
    }

    @Override
    public TenantResponseDTO updateTenant(Long id, TenantRequestDTO tenantRequestDTO) {
        Tenant tenant = tenantRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found with id: " + id));
        tenant.setName(tenantRequestDTO.getName());
        tenant.setEmail(tenantRequestDTO.getEmail());
        tenant.setContactNumber(tenantRequestDTO.getContactNumber());
        tenant.setAddress(tenantRequestDTO.getAddress()); 

        Tenant updatedTenant = tenantRepo.save(tenant);
        return toResponseDTO(updatedTenant);     
    }
    @Override
    public void deleteTenant(Long id) {
        if (!tenantRepo.existsById(id)) {
            throw new ResourceNotFoundException("Tenant not found with id: " + id);
        }
        tenantRepo.deleteById(id);
    }

    private TenantResponseDTO toResponseDTO(Tenant tenant) {
        TenantResponseDTO dto = new TenantResponseDTO();
        dto.setName(tenant.getName());
        dto.setEmail(tenant.getEmail());
        dto.setAddress(tenant.getAddress());
        dto.setContactNumber(tenant.getContactNumber());
        return dto;
    }
    private Tenant toEntity(TenantRequestDTO dto) {
        Tenant tenant = new Tenant();
        tenant.setName(dto.getName());
        tenant.setAddress(dto.getAddress());
        tenant.setContactNumber(dto.getContactNumber());
        tenant.setEmail(dto.getEmail());
        return tenant;
    }

}
