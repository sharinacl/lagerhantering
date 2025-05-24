package se.yrgo.entity;



import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Product> products = new HashSet<Product>();

    public Category(String name) {
        this.name = name;
    }

    public void addProduct(Product product) {
        products.add(product);
        product.getCategories().add(this); // sync reverse side
    }

    public void removeProduct(Product product) {
        products.remove(product);
        product.getCategories().remove(this);
    }

    public Category() {}

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }

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

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Product> getProducts() {
        return products;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", productCount=" + (products != null ? products.size() : 0) +
                '}';
    }

    public void add(Category category) {
        category.add(category);
    }

}
