package com.asc.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.asc.domain.model.Product;
import com.asc.domain.repository.ProductRepository;
import com.asc.exception.NotFoundException;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductServiceのユニットテスト")
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Nested
    @DisplayName("createProductメソッド")
    class CreateProduct {

        @Test
        @DisplayName("正常系: 商品が作成され、リポジトリに保存されること")
        void testCreateProduct_Success() {
            // Arrange
            String name = "テスト商品";
            BigDecimal price = new BigDecimal("1980");
            String remarks = "テスト用の備考です。";

            // Act
            Product createdProduct = productService.createProduct(name, price, remarks);

            // Assert
            ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
            verify(productRepository, times(1)).save(productCaptor.capture());

            Product savedProduct = productCaptor.getValue();
            assertThat(savedProduct.getId()).isNotNull();
            assertThat(savedProduct.getName()).isEqualTo(name);
            assertThat(savedProduct.getPrice()).isEqualTo(price);
            assertThat(savedProduct.getRemarks()).isEqualTo(remarks);

            assertThat(createdProduct).isEqualTo(savedProduct);
        }
    }

    @Nested
    @DisplayName("getProductByIdメソッド")
    class GetProductById {

        @Test
        @DisplayName("正常系: 指定したIDの商品が取得できること")
        void testGetProductById_Success() {
            // Arrange
            String id = UUID.randomUUID().toString();
            Product mockProduct = new Product(id, "既存の商品", BigDecimal.TEN, "");
            when(productRepository.findById(id)).thenReturn(Optional.of(mockProduct));

            // Act
            Product result = productService.getProductById(id);

            // Assert
            assertThat(result).isEqualTo(mockProduct);
            verify(productRepository, times(1)).findById(id);
        }

        @Test
        @DisplayName("異常系: 商品が存在しない場合にNotFoundExceptionがスローされること")
        void testGetProductById_NotFound() {
            // Arrange
            String id = UUID.randomUUID().toString();
            when(productRepository.findById(id)).thenReturn(Optional.empty());

            // Act & Assert
            NotFoundException exception =
                    assertThrows(NotFoundException.class, () -> productService.getProductById(id));
            assertEquals("商品が見つかりません。", exception.getMessage());
            verify(productRepository, times(1)).findById(id);
        }
    }

    @Nested
    @DisplayName("updateProductメソッド")
    class UpdateProduct {

        @Test
        @DisplayName("正常系: 商品が正常に更新されること")
        void testUpdateProduct_Success() {
            // Arrange
            String id = UUID.randomUUID().toString();
            Product existingProduct = new Product(id, "古い名前", new BigDecimal("100"), "古い備考");
            when(productRepository.findById(id)).thenReturn(Optional.of(existingProduct));

            // Act
            Product updatedProduct =
                    productService.updateProduct(id, "新しい名前", new BigDecimal("200"), "新しい備考");

            // Assert
            verify(productRepository, times(1)).findById(id);

            ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
            verify(productRepository, times(1)).update(productCaptor.capture());
            Product savedProduct = productCaptor.getValue();

            assertThat(updatedProduct.getId()).isEqualTo(id);
            assertThat(updatedProduct.getName()).isEqualTo("新しい名前");
            assertThat(savedProduct).isEqualTo(updatedProduct);
        }

        @Test
        @DisplayName("異常系: 更新対象の商品が存在しない場合にNotFoundExceptionがスローされること")
        void testUpdateProduct_NotFound() {
            // Arrange
            String id = UUID.randomUUID().toString();
            when(productRepository.findById(id)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(NotFoundException.class, () -> productService.updateProduct(id, "新しい名前",
                    new BigDecimal("200"), "新しい備考"));
            verify(productRepository, never()).update(any(Product.class));
        }
    }

    @Nested
    @DisplayName("deleteProductメソッド")
    class DeleteProduct {

        @Test
        @DisplayName("正常系: 商品が正常に削除されること")
        void testDeleteProduct_Success() {
            // Arrange
            String id = UUID.randomUUID().toString();
            Product existingProduct = new Product(id, "削除対象商品", BigDecimal.ONE, "");
            when(productRepository.findById(id)).thenReturn(Optional.of(existingProduct));

            // Act
            productService.deleteProduct(id);

            // Assert
            verify(productRepository, times(1)).findById(id);
            verify(productRepository, times(1)).delete(id);
        }

        @Test
        @DisplayName("異常系: 削除対象の商品が存在しない場合にNotFoundExceptionがスローされること")
        void testDeleteProduct_NotFound() {
            // Arrange
            String id = UUID.randomUUID().toString();
            when(productRepository.findById(id)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(NotFoundException.class, () -> productService.deleteProduct(id));
            verify(productRepository, never()).delete(anyString());
        }
    }
}
