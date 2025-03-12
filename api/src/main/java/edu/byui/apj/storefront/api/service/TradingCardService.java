package edu.byui.apj.storefront.api.service;

import edu.byui.apj.storefront.api.model.TradingCard;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStreamReader;
import java.io.Reader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;



public class TradingCardService {
    public List<TradingCard> tradingCards;
    {
        List<TradingCard> tradingCards = new ArrayList<>();
        try {
            ClassPathResource pioneers = new ClassPathResource("pioneers.csv");
            Reader reader = new InputStreamReader(pioneers.getInputStream());
            Iterable<CSVRecord> cards = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);

            for (CSVRecord card : cards ) {
                tradingCards.add(new TradingCard(
                         Long.parseLong(card.get("id"))
                        ,card.get("name")
                        ,card.get("specialty")
                        ,card.get("contribution")
                        ,BigDecimal.valueOf(Long.parseLong(card.get("price")))
                        ,card.get("imageUrl")
                ));
                this.tradingCards = tradingCards;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private Comparator<TradingCard> getComparator(String sort) {
        if ("name".equalsIgnoreCase(sort)) {
            return Comparator.comparing(TradingCard::getName);
        } else if ("price".equalsIgnoreCase(sort)) {
            return Comparator.comparing(TradingCard::getPrice);
        }
        return Comparator.comparing(TradingCard::getId); // Default sort by ID
    }
    public Page<TradingCard> pagifyCards( int page, int size, ArrayList<TradingCard> cards ) {
        Pageable pageable = PageRequest.of(page, size);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), cards.size());

        List<TradingCard> paginatedCards = cards.subList(start, end);
        return new PageImpl<TradingCard>(paginatedCards, pageable, cards.size());
    }

    public Page<TradingCard> getCards( int page, int size ) {
        Pageable pageable = PageRequest.of(page, size);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), tradingCards.size());

        List<TradingCard> paginatedCards = tradingCards.subList(start, end);
        return new PageImpl<>(paginatedCards, pageable, tradingCards.size());
    }
    public List<TradingCard> getFilteredCards( BigDecimal minPrice, BigDecimal maxPrice, String specialty, String sort) {
        List<TradingCard> filteredCards = tradingCards.stream()
                .filter(card -> card.getPrice().compareTo(minPrice) >= 0)
                .filter(card -> card.getPrice().compareTo(maxPrice) <= 0)
                .filter(card-> specialty == null || card.getSpecialty().equalsIgnoreCase(specialty))
                .sorted(getComparator(sort))
                .collect(Collectors.toList());

        if ("name".equalsIgnoreCase(sort)) {
            filteredCards.sort(Comparator.comparing(TradingCard::getName));
        } else if ("price".equalsIgnoreCase(sort)) {
            filteredCards.sort(Comparator.comparing(TradingCard::getPrice));
        }
        return filteredCards;
    }

    public List<TradingCard> getQueriedCards( String query ){
        List<TradingCard> queriedCards = tradingCards.stream()
                .filter(card-> query == null || card.getContribution().equalsIgnoreCase(query))
                .collect(Collectors.toList());
        return queriedCards;
    }
}
