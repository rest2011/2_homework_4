package ru.example.userservice.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.example.userservice.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("test_db")
            .withUsername("test_user")
            .withPassword("test_password");

    @Autowired
    private UserRepository userRepository;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void saveAndFindById_Success() {
        User user = new User("Тест Контейнеров", "container@test.com", 30);
        userRepository.save(user);

        assertNotNull(user.getId());
        User savedUser = userRepository.findById(user.getId()).orElse(null);

        assertNotNull(savedUser);
        assertEquals("Тест Контейнеров", savedUser.getName());
        assertEquals("container@test.com", savedUser.getEmail());
    }

    @Test
    void findAll_ReturnsAllUsers() {
        userRepository.save(new User("Первый", "first@test.com", 20));
        userRepository.save(new User("Второй", "second@test.com", 22));

        List<User> list = userRepository.findAll();

        assertEquals(2, list.size());
    }

    @Test
    void delete_RemovesUserFromDb() {
        User user = new User("Удаляемый", "delete@test.com", 40);
        userRepository.save(user);

        userRepository.deleteById(user.getId());

        User deletedUser = userRepository.findById(user.getId()).orElse(null);
        assertNull(deletedUser);
    }
}
