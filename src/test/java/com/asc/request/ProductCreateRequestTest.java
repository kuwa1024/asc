package com.asc.request;

import static org.assertj.core.api.Assertions.assertThat;
import java.math.BigDecimal;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import com.asc.domain.request.ProductCreateRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@DisplayName("ProductCreateRequestのユニットテスト")
public class ProductCreateRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Nested
    @DisplayName("getRemarksメソッドのテスト")
    class GetRemarksMethod {

        @Test
        @DisplayName("正常系: remarksがnullの場合に空文字が返されること")
        void testGetRemarks_whenNull_thenReturnEmptyString() {
            ProductCreateRequest request = new ProductCreateRequest("テスト商品", BigDecimal.TEN, null);
            assertThat(request.getRemarks()).isEmpty();
        }

        @Test
        @DisplayName("正常系: remarksがnullでない場合にその値が返されること")
        void testGetRemarks_whenNotNull_thenReturnActualValue() {
            String remarks = "テスト備考";
            ProductCreateRequest request =
                    new ProductCreateRequest("テスト商品", BigDecimal.TEN, remarks);
            assertThat(request.getRemarks()).isEqualTo(remarks);
        }
    }

    @Nested
    @DisplayName("バリデーションのテスト")
    class ValidationTests {

        @Test
        @DisplayName("正常系: 全てのフィールドが有効な場合にバリデーションエラーがないこと")
        void testValidation_Success() {
            ProductCreateRequest request =
                    new ProductCreateRequest("有効な商品", new BigDecimal("1980"), "有効な備考");
            Set<ConstraintViolation<ProductCreateRequest>> violations = validator.validate(request);
            assertThat(violations).isEmpty();
        }

        @ParameterizedTest(name = "異常系: {2}の場合にバリデーションエラーが発生すること")
        @MethodSource("invalidRequestProvider")
        void testValidation_withInvalidData_thenError(ProductCreateRequest request,
                String expectedField, String displayName) {
            Set<ConstraintViolation<ProductCreateRequest>> violations = validator.validate(request);
            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getPropertyPath().toString())
                    .isEqualTo(expectedField);
        }

        private static java.util.stream.Stream<Arguments> invalidRequestProvider() {
            return java.util.stream.Stream.of(
                    Arguments.of(new ProductCreateRequest(null, new BigDecimal("1980"), "備考"),
                            "name", "nameがnull"),
                    Arguments.of(new ProductCreateRequest("", new BigDecimal("1980"), "備考"), "name",
                            "nameが空文字"),
                    Arguments.of(new ProductCreateRequest("  ", new BigDecimal("1980"), "備考"),
                            "name", "nameが空白"),
                    Arguments.of(
                            new ProductCreateRequest("a".repeat(11), new BigDecimal("1980"), "備考"),
                            "name", "nameが長すぎる"),
                    Arguments.of(new ProductCreateRequest("商品", null, "備考"), "price", "priceがnull"),
                    Arguments.of(
                            new ProductCreateRequest("商品", new BigDecimal("1980"), "a".repeat(11)),
                            "remarks", "remarksが長すぎる"));
        }
    }
}
