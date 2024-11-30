package com.home.gvojvoda.domain.util;

import com.home.gvojvoda.domain.exception.GameException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.FieldSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GameTeamNameProcessorTest {

    // ---------------------------------------
    // ---- Validate And Format Team Name ----
    // ---------------------------------------

    @ParameterizedTest(name = "Test invalid name ''{0}''")
    @FieldSource("invalidGameTeamNameList")
    void validateAndFormatTeamName_KO_invalidTeamName(String gameTeamName) {
        // Given
        GameTeamNameProcessor gameTeamNameProcessor = new GameTeamNameProcessor();
        assertThrows(GameException.class, () -> {
            gameTeamNameProcessor.validateAndFormatTeamName(gameTeamName);
        });
    }

    @ParameterizedTest(name = "Test valid name ''{0}'', expected ''{1}''")
    @MethodSource("validGameTeamNamesAndFormatedValues")
    void validateAndFormatTeamName_KO(String providedGameTeamName, String expectedResult) throws GameException {
        // Given
        GameTeamNameProcessor gameTeamNameProcessor = new GameTeamNameProcessor();
        // When
        String result = gameTeamNameProcessor.validateAndFormatTeamName(providedGameTeamName);
        // Then
        assertEquals(expectedResult, result);
    }

    // ----------------------------------
    // ---- Method and Field Sources ----
    // ----------------------------------

    private static List<String> invalidGameTeamNameList =
            Arrays.asList(
                    null, "", "     ", "hello*world", "&/%", "%hello world"
            );

    private static Stream<Arguments> validGameTeamNamesAndFormatedValues() {
        return Stream.of(
                Arguments.of("helloworld", "HELLOWORLD"),
                Arguments.of("hello world", "HELLO_WORLD"),
                Arguments.of("    helloworld    ", "HELLOWORLD"),
                Arguments.of("helloworld6", "HELLOWORLD6"),
                Arguments.of("HELLO_WORLD_123", "HELLO_WORLD_123"),
                Arguments.of("    HelLo    WORld    123    ", "HELLO_WORLD_123")
        );
    }


}
