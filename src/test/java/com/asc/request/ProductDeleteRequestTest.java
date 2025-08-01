package com.asc.request;

import static org.assertj.core.api.Assertions.assertThat;
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
import com.asc.domain.request.ProductDeleteRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@DisplayName("ProductDeleteRequestのユニットテスト")
public class ProductDeleteRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Nested
    @DisplayName("バリデーションのテスト")
    class ValidationTests {

        @Test
        @DisplayName("正常系: IDが有効なUUIDの場合にバリデーションエラーがないこと")
        void testValidation_Success() {
            ProductDeleteRequest request = new ProductDeleteRequest(UUID.randomUUID().toString());
            Set<ConstraintViolation<ProductDeleteRequest>> violations = validator.validate(request);
            assertThat(violations).isEmpty();
        }

        @ParameterizedTest(name = "異常系: IDが{1}の場合にバリデーションエラーが発生すること")
        @MethodSource("invalidIdProvider")
        void testValidation_withInvalidId_thenError(String invalidId, String displayName) {
            ProductDeleteRequest request = new ProductDeleteRequest(invalidId);
            Set<ConstraintViolation<ProductDeleteRequest>> violations = validator.validate(request);
            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("id");
        }

        private static Stream<Arguments> invalidIdProvider() {
            return Stream.of(Arguments.of(null, "null"), Arguments.of("", "空文字"),
                    Arguments.of("  ", "空白"), Arguments.of("not-a-uuid", "不正な形式"));
        }
    }
}
