package com.agrohelper;

import com.agrohelper.controller.ProductController;
import com.agrohelper.controller.UserController;
import com.agrohelper.entity.Product;
import com.agrohelper.entity.Product.ProductCategory;
import com.agrohelper.entity.User;
import com.agrohelper.entity.UserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Classe para testar o fluxo completo da aplicação
 * - Controller -> Service -> DAO -> Banco de dados
 */
@Component
public class FlowTest implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(FlowTest.class);

    @Autowired
    private UserController userController;

    @Autowired
    private ProductController productController;

    @Override
    public void run(String... args) {
        logger.info("====== INICIANDO TESTE DE FLUXO COMPLETO DA APLICAÇÃO ======");
        logger.info("Testando fluxo completo: Controller -> Service -> DAO -> Database");
        
        try {
            // 1. Criar usuário (simulando requisição do frontend)
            logger.info("1. Criando novo usuário vendedor (CONTROLLER)");
            User newUser = new User();
            newUser.setEmail("teste_flow@agrohelper.com");
            newUser.setPassword("senha123");
            newUser.setFullName("Usuário de Teste");
            // Definir como SELLER para poder criar produtos
            newUser.setUserType(UserType.SELLER);
            
            ResponseEntity<Map<String, Object>> registerResponse = userController.register(newUser, UserType.SELLER);
            
            if (!registerResponse.getStatusCode().is2xxSuccessful()) {
                logger.error("Erro ao registrar usuário: {}", registerResponse.getBody());
                return;
            }
            
            Map<String, Object> userData = (Map<String, Object>) registerResponse.getBody().get("user");
            Long userId = ((Number) userData.get("numericId")).longValue();
            
            logger.info("Usuário vendedor criado com sucesso! ID: {}", userId);
            
            // 2. Criar produto associado ao usuário (simulando requisição do frontend)
            logger.info("2. Criando novo produto (CONTROLLER)");
            Product newProduct = new Product();
            newProduct.setTitle("Produto de Teste");
            newProduct.setDescription("Produto criado para testar o fluxo completo da aplicação");
            newProduct.setPrice(new BigDecimal("99.99"));
            newProduct.setCategory(ProductCategory.GRAINS);
            newProduct.setLocation("Belo Horizonte, MG");
            newProduct.setSellerName(newUser.getFullName());
            newProduct.setImageUrl("https://example.com/image.jpg");
            
            ResponseEntity<?> productResponse = productController.createProduct(newProduct, userId);
            
            if (!productResponse.getStatusCode().is2xxSuccessful()) {
                logger.error("Erro ao criar produto: {}", productResponse.getBody());
                return;
            }
            
            logger.info("Produto criado com sucesso!");
            
            // 3. Buscar produtos do usuário (simulando requisição do frontend)
            logger.info("3. Buscando produtos do usuário (CONTROLLER)");
            ResponseEntity<?> userProductsResponse = productController.getProductsByUserId(userId);
            
            if (!userProductsResponse.getStatusCode().is2xxSuccessful()) {
                logger.error("Erro ao buscar produtos do usuário: {}", userProductsResponse.getBody());
                return;
            }
            
            Map<String, Object> productsData = (Map<String, Object>) userProductsResponse.getBody();
            Integer count = (Integer) productsData.get("count");
            
            logger.info("Encontrados {} produtos para o usuário ID: {}", count, userId);
            
            // 4. Testar login (simulando requisição do frontend)
            logger.info("4. Testando login do usuário (CONTROLLER)");
            Map<String, String> loginData = Map.of(
                "email", "teste_flow@agrohelper.com",
                "password", "senha123"
            );
            
            ResponseEntity<Map<String, Object>> loginResponse = userController.login(loginData);
            
            if (!loginResponse.getStatusCode().is2xxSuccessful()) {
                logger.error("Erro ao realizar login: {}", loginResponse.getBody());
                return;
            }
            
            logger.info("Login realizado com sucesso!");
            
            // 5. Criar usuário comprador para testar avaliações
            logger.info("5. Criando usuário comprador para testar avaliações");
            User buyerUser = new User();
            buyerUser.setEmail("comprador_teste@agrohelper.com");
            buyerUser.setPassword("senha123");
            buyerUser.setFullName("Comprador de Teste");
            buyerUser.setUserType(UserType.BUYER);
            
            ResponseEntity<Map<String, Object>> buyerRegisterResponse = userController.register(buyerUser, UserType.BUYER);
            
            if (!buyerRegisterResponse.getStatusCode().is2xxSuccessful()) {
                logger.error("Erro ao registrar usuário comprador: {}", buyerRegisterResponse.getBody());
                return;
            }
            
            logger.info("Usuário comprador criado com sucesso!");
            
            logger.info("====== TESTE DE FLUXO COMPLETO FINALIZADO COM SUCESSO ======");
            logger.info("✅ Todas as camadas estão funcionando corretamente:");
            logger.info("   Frontend -> Controller -> Service -> DAO -> Database");
            
        } catch (Exception e) {
            logger.error("Erro durante o teste de fluxo completo", e);
            e.printStackTrace();
        }
    }
}