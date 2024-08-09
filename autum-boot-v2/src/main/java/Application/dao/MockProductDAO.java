package Application.dao;

import Application.domain.Product;
import org.autumframework.annotation.Service;

import java.util.HashMap;

@Service
public class MockProductDAO implements IProductDAO{
    private HashMap<String, Product> productHashMap = new HashMap<>();

    public void addProduct(Product product){
        productHashMap.put(product.getId(), product);
        System.out.println("MockProduct saved!");
    }
}
