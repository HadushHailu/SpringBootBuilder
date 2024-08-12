package Application.dao;

import Application.domain.Product;
import org.autumframework.annotation.Profile;
import org.autumframework.annotation.Service;

import java.util.HashMap;

@Service
@Profile(value = "production")
public class ProductDAO implements IProductDAO {
    private HashMap<String, Product> productHashMap = new HashMap<>();

    public void addProduct(Product product){
        productHashMap.put(product.getId(), product);
        System.out.println("[****************]Product saved!");
    }
}
