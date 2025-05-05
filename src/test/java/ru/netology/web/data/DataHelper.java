package ru.netology.web.data;

import lombok.Value;

public class DataHelper {

    private DataHelper() {
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static AuthInfo getOtherAuthInfo(AuthInfo original) {
        return new AuthInfo("petya", "123qwerty");
    }

    public static VerificationCode getVerificationCodeFor(AuthInfo authInfo) {
        return new VerificationCode("12345");
    }

    public static CardInfo getFirstCardInfo() {
        return new CardInfo(Card.FIRST.getNumber(), Card.FIRST.getTestId());
    }

    public static CardInfo getSecondCardInfo() {
        return new CardInfo(Card.SECOND.getNumber(), Card.SECOND.getTestId());
    }

    public static CardInfo getOtherCard(CardInfo card) {
        return card.equals(getFirstCardInfo()) ? getSecondCardInfo() : getFirstCardInfo();
    }

    @Value
    public static class AuthInfo {
        String login;
        String password;
    }

    @Value
    public static class VerificationCode {
        String code;
    }

    @Value
    public static class CardInfo {
        String number;
        String testId;
    }

    public enum Card {
        FIRST("5559 0000 0000 0001", "92df3f1c-a033-48e6-8390-206f6b1f56c0"),
        SECOND("5559 0000 0000 0002", "0f3f5c2a-249e-4c3d-8287-09f7a039391d");

        private final String number;
        private final String testId;

        Card(String number, String testId) {
            this.number = number;
            this.testId = testId;
        }

        public String getNumber() {
            return number;
        }

        public String getTestId() {
            return testId;
        }
    }
}