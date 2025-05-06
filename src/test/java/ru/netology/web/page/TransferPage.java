package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private SelenideElement transferHeader = $("h1");//"Ваши карты"


    // пополнение карты
    private SelenideElement cardReplenishmentHeader = $("h1");//"Пополнение карты"
    private SelenideElement amountField = $("[data-test-id='amount'] input");//поле ввода суммы
    private SelenideElement payFromField = $("[data-test-id='from'] input");//поле ввода номера карты отправителя
    private SelenideElement replenishmentButton = $("[data-test-id='action-transfer']");//кнопка "Перевести"
    private SelenideElement cancelButton = $("[data-test-id='action-cancel']");//кнопка "Отмена"


    // основной сценарий
    public TransferPage() {
        transferHeader.shouldHave(Condition.exactText("Ваши карты")); // Проверка на точное совпадение
        transferHeader.shouldBe(Condition.visible); // Проверка, что элемент видим
    }

    // сценарий запополнения(номер карты отправителя и суммы)
    public TransferPage(DataHelper.CardInfo cardInfo, String amount) {
        fillAmount(amount);
        fillFromCard(cardInfo);
        // Проверка, что на странице заголовок "Пополнение карты"
        cardReplenishmentHeader.shouldHave(Condition.exactText("Пополнение карты"))
                .shouldBe(Condition.visible);
    }

    public void fillFromCard(DataHelper.CardInfo otherCard) {// заполнает поле с номером карты
        clearFromCardField();
        payFromField.shouldBe(Condition.visible).setValue(otherCard.getNumber());
    }

    public void fillAmount(String amount) {//заполнение поля с суммой
        clearAmountField();
        amountField.shouldBe(Condition.visible).setValue(amount);
    }

    public void transferFunds() {//кнопка "перевести"
        replenishmentButton.shouldBe(Condition.visible).click();
    }

    public void clearFromCardField() {//очистка поля ввода номера карты
        payFromField.shouldBe(Condition.visible);
        payFromField.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
    }

    public void clearAmountField() {//очистка поля ввода суммы
        amountField.shouldBe(Condition.visible);
        amountField.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
    }

    public void clickCancelButton() {//нажимаем кнопку отмена
        cancelButton.shouldBe(Condition.visible).click();
    }


}
