package Application.dao;

import Application.domain.Product;

import java.util.Collection;
import java.util.List;

public interface IProductDAO {
    public void save(Product product);
    public Product find(String name);
    public Collection<Product> getAll();
}
