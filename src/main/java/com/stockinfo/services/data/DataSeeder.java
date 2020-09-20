package com.stockinfo.services.data;

import com.stockinfo.models.ERole;
import com.stockinfo.models.Role;
import com.stockinfo.models.StockUser;
import com.stockinfo.repository.RoleRepository;
import com.stockinfo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class DataSeeder implements ApplicationRunner {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    PasswordEncoder encoder;

    @Value("${stock-info.app.superAdminUsername}")
    private String superAdminUsername;
    @Value("${stock-info.app.superAdminPassword}")
    private String superAdminPassword;
    @Value("${stock-info.app.superAdminEmail}")
    private String superAdminEmail;

    @Override
    public void run(ApplicationArguments args) {
        seed();
    }

    private void seed() {
        seedRoles();
        seedSuperAdmin();
    }

    private void seedRoles() {
        Arrays.stream(ERole.values()).forEach(eRole -> {
            if (!roleRepository.findByName(eRole).isPresent()) {
                roleRepository.saveAndFlush(new Role(eRole));
            }
        });
    }

    private void seedSuperAdmin() {
        Optional<StockUser> optionalSuperAdmin = userRepository.findByUsername(superAdminUsername);
        if (!optionalSuperAdmin.isPresent()) {
            StockUser superAdmin = new StockUser(superAdminUsername,
                    superAdminEmail,
                    encoder.encode(superAdminPassword));
            superAdmin.setRoles(roleRepository.findAll().stream().collect(Collectors.toSet()));
            userRepository.save(superAdmin);
        } else {
            StockUser superAdmin = optionalSuperAdmin.get();
            superAdmin.setPassword(encoder.encode(superAdminPassword));
            superAdmin.setRoles(roleRepository.findAll().stream().collect(Collectors.toSet()));
            userRepository.save(superAdmin);
        }
    }
}
