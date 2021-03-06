package example

import io.micronaut.transaction.SynchronousTransactionManager
import javax.inject.Singleton
import javax.persistence.EntityManager

@Singleton
class ProductManager {

    private final EntityManager entityManager
    private final SynchronousTransactionManager<EntityManager> transactionManager

    ProductManager(
            EntityManager entityManager,
            SynchronousTransactionManager<EntityManager> transactionManager) { // <1>
        this.entityManager = entityManager
        this.transactionManager = transactionManager
    }

    Product save(String name, Manufacturer manufacturer) {
        return transactionManager.executeWrite { // <2>
            final product = new Product(name, manufacturer)
            entityManager.persist(product)
            return product
        }
    }

    Product find(String name) {
        return transactionManager.executeRead { // <3>
            entityManager.createQuery("from Product p where p.name = :name", Product)
                    .setParameter("name", name)
                    .singleResult
        }
    }
}
