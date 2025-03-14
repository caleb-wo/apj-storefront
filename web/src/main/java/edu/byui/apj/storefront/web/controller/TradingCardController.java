package edu.byui.apj.storefront.web.controller;

import edu.byui.apj.storefront.web.service.TradingCardClientService;
import edu.byui.apj.storefront.web.model.TradingCard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class TradingCardController {
    private final TradingCardClientService tradingCardClientService;

    @Autowired
    public TradingCardController(TradingCardClientService tradingCardClientService) {
        this.tradingCardClientService = tradingCardClientService;
    }

    @GetMapping("/api/cards")
    public List<TradingCard> getCards(
            Model model
            ,@RequestParam(defaultValue = "0") int page
            ,@RequestParam(defaultValue = "20") int size) {
        return tradingCardClientService.getAllCardsPaginated( page, size );
    }

    @GetMapping( "/api/cards/filter" )
    public List<TradingCard> getFilteredCards(
            Model model
            , @RequestParam(required = false) BigDecimal minPrice
            , @RequestParam(required = false) BigDecimal maxPrice
            , @RequestParam(required = false) String specialty
            , @RequestParam(required = false) String sort
    ){ return tradingCardClientService.filterAndSort(minPrice, maxPrice, specialty, sort); }
}

