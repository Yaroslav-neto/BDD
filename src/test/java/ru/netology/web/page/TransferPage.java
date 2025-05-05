package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private SelenideElement transferHeader = $("h1");
    private SelenideElement reloadButton = $("[data-test-id='action-reload']");

    public static void replenishCard(DataHelper.CardInfo cardInfo) {
        $("[data-test-id='" + cardInfo.getTestId() + "'] button").click();
    }

    public void clickReload() {
        reloadButton.shouldBe(Condition.visible, Condition.enabled).click();
    }

    public TransferPage() {
        transferHeader.should(Condition.exactText("Ваши карты"));
        transferHeader.shouldBe(Condition.visible);
    }

    // Добавим геттеры для теста
    public SelenideElement getTransferHeader() {
        return transferHeader;
    }
    public SelenideElement getReloadButton() {
        return reloadButton;
    }
}
