package server.repositories;

import server.models.Product;
import java.util.List;

public interface ProductRepository {

    List<Product> findAll();

    Product findById(int id);

    Product save(Product product);

    Product update(Product product);

    boolean delete(int id);

    int getNextId();
}
