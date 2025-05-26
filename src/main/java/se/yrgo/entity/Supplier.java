package se.yrgo.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "suppliers")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String contactName;

    private String email;

    private String phone;

    private String address;

    @ManyToMany(mappedBy = "suppliers")
    private Set<Product> products = new HashSet<>();

    // Constructors
    public Supplier() {}

    public Supplier(String name, String contactName, String email, String phone, String address) {
        this.name = name;
        this.contactName = contactName;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public Supplier(String name, String contactName) {
        this.name = name;
        this.contactName = contactName;
    }

    public Supplier(String name) {
        this.name = name;
    }

    /**
     * Adds a product to this supplier and maintains bidirectional relationship
     * @param product The product to add
     */
    public void addProduct(Product product) {
        if (product != null) {
            this.products.add(product);
            product.getSuppliers().add(this);
        }
    }

    /**
     * Removes a product from this supplier and maintains bidirectional relationship
     * @param product The product to remove
     */
    public void removeProduct(Product product) {
        if (product != null) {
            this.products.remove(product);
            product.getSuppliers().remove(this);
        }
    }

    /**
     * Checks if this supplier supplies a specific product
     * @param product The product to check
     * @return true if this supplier supplies the product
     */
    public boolean supplies(Product product) {
        return products.contains(product);
    }

    /**
     * Gets the number of products supplied by this supplier
     * @return the count of products
     */
    public int getProductCount() {
        return products.size();
    }

    /**
     * Gets all product names as a formatted string
     * @return comma-separated list of product names
     */
    public String getProductNames() {
        return products.stream()
                .map(Product::getName)
                .reduce((name1, name2) -> name1 + ", " + name2)
                .orElse("No products");
    }

    /**
     * Finds a product by name
     * @param name The product name to search for
     * @return the product if found, null otherwise
     */
    public Product findProductByName(String name) {
        return products.stream()
                .filter(product -> product.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets total value of all products supplied by this supplier
     * @return the sum of all product prices
     */
    public double getTotalProductValue() {
        return products.stream()
                .mapToDouble(Product::getPrice)
                .sum();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "Supplier{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", contactName='" + contactName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
