package ua.com.foxminded.school.viewprovider;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;

@ExtendWith(MockitoExtension.class)
class ViewProviderTest {
    @InjectMocks
    private ViewProvider viewProvider;

    @Mock
    private Scanner scanner;
    
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    
    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }
    
    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    void printMessageShouldReturnMessageFromConsole() throws Exception {  
        viewProvider.printMessage("word");
        
        assertThat("word").isEqualTo(outputStreamCaptor.toString().trim());
    }

    @Test
    void readStringShouldReturnStringFromScanner() {
        String expected = "word";
        when(scanner.next()).thenReturn("word");

        String actual = viewProvider.readString();
        assertThat(actual).isEqualTo(expected);
    }
    
    @Test
    void readIntShouldReturnIntegerFromScanner() {
        int expected = 5;
        when(scanner.nextInt()).thenReturn(5);

        int actual = viewProvider.readInt();
        assertThat(actual).isEqualTo(expected);
    }
}
