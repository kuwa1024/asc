package com.asc.request;

import static org.assertj.core.api.Assertions.assertThat;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import com.asc.domain.request.ProductUpdateRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@DisplayName("ProductUpdateRequestのユニットテスト")
public class ProductUpdateRequestTest {

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
                        ProductUpdateRequest request =
                                        new ProductUpdateRequest(UUID.randomUUID().toString(),
                                                        "テスト商品", BigDecimal.TEN, null);
                        assertThat(request.getRemarks()).isEmpty();
                }

                @Test
                @DisplayName("正常系: remarksがnullでない場合にその値が返されること")
                void testGetRemarks_whenNotNull_thenReturnActualValue() {
                        String remarks = "テスト備考";
                        ProductUpdateRequest request =
                                        new ProductUpdateRequest(UUID.randomUUID().toString(),
                                                        "テスト商品", BigDecimal.TEN, remarks);
                        assertThat(request.getRemarks()).isEqualTo(remarks);
                }
        }

        @Nested
        @DisplayName("バリデーションのテスト")
        class ValidationTests {

                @Test
                @DisplayName("正常系: 全てのフィールドが有効な場合にバリデーションエラーがないこと")
                void testValidation_Success() {
                        ProductUpdateRequest request =
                                        new ProductUpdateRequest(UUID.randomUUID().toString(),
                                                        "有効な商品", new BigDecimal("1980"), "有効な備考");
                        Set<ConstraintViolation<ProductUpdateRequest>> violations =
                                        validator.validate(request);
                        assertThat(violations).isEmpty();
                }

                @ParameterizedTest(name = "異常系: {2}の場合にバリデーションエラーが発生すること")
                @MethodSource("invalidRequestProvider")
                void testValidation_withInvalidData_thenError(ProductUpdateRequest request,
                                String expectedField, String displayName) {
                        Set<ConstraintViolation<ProductUpdateRequest>> violations =
                                        validator.validate(request);
                        assertThat(violations).hasSize(1);
                        assertThat(violations.iterator().next().getPropertyPath().toString())
                                        .isEqualTo(expectedField);
                }

                private static Stream<Arguments> invalidRequestProvider() {
                        String validId = UUID.randomUUID().toString();
                        BigDecimal validPrice = new BigDecimal("1980");
                        String validName = "商品";
                        String validRemarks = "備考";

                        return Stream.of(
                                        Arguments.of(new ProductUpdateRequest("", validName,
                                                        validPrice, validRemarks), "id", "idが空文字"),
                                        Arguments.of(new ProductUpdateRequest(" ", validName,
                                                        validPrice, validRemarks), "id", "idが空白"),
                                        Arguments.of(new ProductUpdateRequest("not-a-uuid",
                                                        validName, validPrice, validRemarks), "id",
                                                        "idが不正な形式"),
                                        Arguments.of(new ProductUpdateRequest(validId, null,
                                                        validPrice, validRemarks), "name",
                                                        "nameがnull"),
                                        Arguments.of(new ProductUpdateRequest(validId, "  ",
                                                        validPrice, validRemarks), "name",
                                                        "nameが空白"),
                                        Arguments.of(new ProductUpdateRequest(validId,
                                                        "a".repeat(11), validPrice, validRemarks),
                                                        "name", "nameが長すぎる"),
                                        Arguments.of(new ProductUpdateRequest(validId, validName,
                                                        null, validRemarks), "price", "priceがnull"),
                                        Arguments.of(new ProductUpdateRequest(validId, validName,
                                                        validPrice, "a".repeat(11)), "remarks",
                                                        "remarksが長すぎる"));
                }
        }
}
