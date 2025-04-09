package es.codeurjc.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a user entity in the system.
 */
@Entity
public class User {

    /** Unique identifier for the user. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Unique username of the user. */
    @NotNull
    private String username;

    /** First name of the user. */
    private String firstName;

    /** Last name of the user. */
    private String lastName;

    /** Date of birth of the user. */
    private LocalDate dateOfBirth;

    /** Contact phone number of the user. */
    private String phoneNumber;

    /** Residential address of the user. */
    private String address;

    /** Email address of the user. */
    private String email;

    /** Encrypted password of the user. */
    private String encodedPassword;

    /** Roles assigned to the user, e.g., "USER", "ADMIN". */
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    /** Indicates whether the user is banned from the system. */
    private boolean banned;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Booking> bookings;

    /** Default constructor for JPA. */
    public User() {}

    /**
     * Constructor with essential fields.
     *
     * @param username The unique username.
     * @param encodedPassword The encrypted password.
     * @param banned Whether the user is banned.
     * @param roles List of roles assigned to the user.
     */
    public User(String username, String encodedPassword, boolean banned, String... roles) {
        this.username = username;
        this.encodedPassword = encodedPassword;
        this.banned = banned;
        this.roles = Arrays.asList(roles);
    }

    /**
     * Constructor with all user details.
     *
     * @param username The unique username.
     * @param encodedPassword The encrypted password.
     * @param firstName First name of the user.
     * @param lastName Last name of the user.
     * @param dateOfBirth Date of birth of the user.
     * @param phoneNumber Contact phone number.
     * @param address Residential address.
     * @param email Email address.
     * @param banned Whether the user is banned.
     * @param roles List of roles assigned to the user.
     */
    public User(String username, String encodedPassword, String firstName, String lastName,
                LocalDate dateOfBirth, String phoneNumber, String address, String email, boolean banned, String... roles) {
        this.username = username;
        this.encodedPassword = encodedPassword;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.email = email;
        this.banned = banned;
        this.roles = Arrays.asList(roles);
    }

    /** @return The unique ID of the user. */
    public Long getId() {
        return id;
    }

    /** @param id The unique ID to set. */
    public void setId(Long id) {
        this.id = id;
    }

    /** @return The username of the user. */
    public String getUsername() {
        return username;
    }

    /** @param username The username to set. */
    public void setUsername(String username) {
        this.username = username;
    }

    /** @return The first name of the user. */
    public String getFirstName() {
        return firstName;
    }

    /** @param firstName The first name to set. */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /** @return The last name of the user. */
    public String getLastName() {
        return lastName;
    }

    /** @param lastName The last name to set. */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /** @return The date of birth of the user. */
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    /** @param dateOfBirth The date of birth to set. */
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /** @return The phone number of the user. */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /** @param phoneNumber The phone number to set. */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /** @return The address of the user. */
    public String getAddress() {
        return address;
    }

    /** @param address The address to set. */
    public void setAddress(String address) {
        this.address = address;
    }

    /** @return The email address of the user. */
    public String getEmail() {
        return email;
    }

    /** @param email The email address to set. */
    public void setEmail(String email) {
        this.email = email;
    }

    /** @return The encrypted password of the user. */
    public String getEncodedPassword() {
        return encodedPassword;
    }

    /** @param encodedPassword The encrypted password to set. */
    public void setEncodedPassword(String encodedPassword) {
        this.encodedPassword = encodedPassword;
    }

    /** @return The roles assigned to the user. */
    public List<String> getRoles() {
        return roles;
    }

    /** @param roles The roles to set. */
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    /** @return Whether the user is banned. */
    public boolean isBanned() {
        return banned;
    }

    /** @param banned The banned status to set. */
    public void setBanned(boolean banned) {
        this.banned = banned;
    }
}
