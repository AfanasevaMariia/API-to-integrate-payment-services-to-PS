package mir.services;

import mir.models.Card;

public interface CardService {
    String registerCard(Card card);

    String transferMoney(Card sender, String recipientCardNumber, Double amountOfMoney);
}
