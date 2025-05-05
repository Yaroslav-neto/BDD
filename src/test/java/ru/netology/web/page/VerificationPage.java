package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {
    private SelenideElement verificationHeader = $(".paragraph");
    private SelenideElement codeField = $("[data-test-id=code] input");
    private SelenideElement verifyButton = $("[data-test-id=action-verify]");

    public VerificationPage() {
        verifyHeader();
    }

    private void verifyHeader() {
        verificationHeader.shouldBe(Condition.visible);
        verificationHeader.shouldHave(Condition.exactText("Необходимо подтверждение"));
    }

    public DashBoardPage validVerify(DataHelper.VerificationCode verificationCode) {
        enterVerificationCode(verificationCode);
        clickVerifyButton();
        return new DashBoardPage();
    }

    private void enterVerificationCode(DataHelper.VerificationCode verificationCode) {
        codeField.setValue(verificationCode.getCode());
    }

    private void clickVerifyButton() {
        verifyButton.click();
    }
}
