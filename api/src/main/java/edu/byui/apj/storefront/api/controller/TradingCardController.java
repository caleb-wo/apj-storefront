package edu.byui.apj.storefront.api.controller;

import edu.byui.apj.storefront.api.model.TradingCard;
import edu.byui.apj.storefront.api.service.TradingCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class TradingCardController {
    private final TradingCardService tradingCardService;

    @Autowired
    public TradingCardController(TradingCardService tradingCardService) {
        this.tradingCardService = tradingCardService;
    }

    @GetMapping("/api/cards")
    public List<TradingCard> getCards(
             Model model
            ,@RequestParam(defaultValue = "0") int page
            ,@RequestParam(defaultValue = "20") int size) {
        return (List<TradingCard>) tradingCardService.getCards( page, size );
    }

    @GetMapping( "/api/cards/filter" )
    public List<TradingCard> getFilteredCards(
            Model model
            , @RequestParam(required = false) BigDecimal minPrice
            , @RequestParam(required = false) BigDecimal maxPrice
            , @RequestParam(required = false) String specialty
            , @RequestParam(required = false) String sort
    ){ return tradingCardService.getFilteredCards(minPrice, maxPrice, specialty, sort) }
}
