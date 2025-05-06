package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private SelenideElement transferHeader = $("h1");
    private SelenideElement reloadButton = $("[data-test-id='action-reload']");

    // пополнение карты
    private SelenideElement cardReplenishmentHeader = $("h1");
    private SelenideElement amountField = $("[data-test-id='amount'] input");
    private SelenideElement payFromField = $("[data-test-id='from'] input");
    private SelenideElement replenishmentButton = $("[data-test-id='action-transfer']");
    private SelenideElement cancelButton = $("[data-test-id='action-cancel']");

    private DataHelper.CardInfo cardFrom;
    private String amount;

    // основной сценарий
    public TransferPage() {
        transferHeader.shouldHave(Condition.exactText("Ваши карты")); // Проверка на точное совпадение
        transferHeader.shouldBe(Condition.visible); // Проверка, что элемент видим
    }

    // сценарий пополнения
    public TransferPage(DataHelper.CardInfo cardFrom, String amount) {
        this.cardFrom = cardFrom;
        this.amount = amount;

        // Проверка, что на странице заголовок "Пополнение карты"
        cardReplenishmentHeader.shouldHave(Condition.exactText("Пополнение карты"))
                .shouldBe(Condition.visible);
    }

    // Статический метод для пополнения карты
    public static void replenishCard(DataHelper.CardInfo cardInfo) {
        $("[data-test-id='" + cardInfo.getTestId() + "'] button").click();
    }

    // Метод для обновления страницы
    public void clickReload() {
        reloadButton.shouldBe(Condition.visible, Condition.enabled).click();
    }

    // Геттеры для тестов
    public SelenideElement getTransferHeader() {
        return transferHeader;
    }

    public SelenideElement getReloadButton() {
        return reloadButton.shouldBe(Condition.visible);
    }

    // Методы для процессов пополнения

    /**
     * Заполняет поле 'откуда' другой картой
     */
    public void fillFromCard(DataHelper.CardInfo otherCard) {
        clearFromCardField();
        payFromField.shouldBe(Condition.visible).setValue(otherCard.getNumber());
    }

    /**
     * Выполняет перевод (нажимает кнопку "Пополнить")
     */
    public void transferFunds() {
        replenishmentButton.shouldBe(Condition.visible).click();
    }

    /**
     * Очищает поле "откуда"
     */
    public void clearFromCardField() {
        payFromField.shouldBe(Condition.visible);
        payFromField.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
    }

    /**
     * Очищает поле суммы
     */
    public void clearAmountField() {
        amountField.shouldBe(Condition.visible);
        amountField.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
    }

    /**
     * Нажимает кнопку "Отмена"
     */
    public void clickCancelButton() {
        cancelButton.shouldBe(Condition.visible).click();
    }

    /**
     * Заполняет поле суммы
     */
    public void fillAmount(String amount) {
        clearAmountField();
        amountField.shouldBe(Condition.visible).setValue(amount);
    }
}
