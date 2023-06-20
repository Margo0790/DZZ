package ru.netology.web;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
public class OrderingCardTest {
    WebDriver driver;
    ChromeOptions options;

    @BeforeAll
    static void setupAll() {

        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setup() {
        options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver();
        driver.get("http://localhost:7777");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver=null;
    }

    @Test
    public void shouldBeSuccessfulForm() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Лев Петров-Водкин");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79219998887");
        driver.findElement(By.tagName("label")).click();
        driver.findElement(By.tagName("button")).click();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    public void shouldShowNameError() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Lev");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79219998887");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.tagName("button")).click();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    public void shouldShowPhoneError() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Лев");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("79219998887");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.tagName("button")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    public void shouldShowAgreementError() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Лев");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79219998887");
        driver.findElement(By.tagName("button")).click();
        boolean actual = driver.findElement(By.cssSelector("[data-test-id='agreement'].input_invalid .checkbox__text")).isDisplayed();
        assertTrue(actual);
    }

    @Test
    public void shouldShowErrorWithEmptyNameField() {
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79219998877");
        driver.findElement(By.tagName("label")).click();
        driver.findElement(By.tagName("button")).click();
        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }

    @Test
    public void shouldShowErrorWithEmptyPhoneField() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Лев");
        driver.findElement(By.tagName("label")).click();
        driver.findElement(By.tagName("button")).click();
        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        assertEquals(expected, actual);
    }
}