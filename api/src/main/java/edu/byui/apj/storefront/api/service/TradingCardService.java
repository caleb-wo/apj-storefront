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
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class TradingCardService {
    private List<TradingCard> tradingCards = new ArrayList<>();

    public TradingCardService() {
        try {
            ClassPathResource pioneers = new ClassPathResource("pioneers.csv");
            if (pioneers.exists()) {
                loadCardsFromCSV(pioneers);
            } else {
                System.err.println("Warning: pioneers.csv not found in classpath");
            }
        } catch (IOException e) {
            System.err.println("Error loading trading cards: " + e.getMessage());
        }
    }

    private void loadCardsFromCSV(ClassPathResource resource) throws IOException {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
            Iterable<CSVRecord> cards = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);

            for (CSVRecord card : cards) {
                try {
                    Long id = Long.parseLong(card.get("ID"));
                    String name = card.get("Name");
                    String specialty = card.get("Specialty");
                    String contribution = card.get("Contribution");

                    BigDecimal price;
                    try {
                        price = new BigDecimal(card.get("Price"));
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid price format for card ID " + id + ": " + card.get("Price"));
                        price = BigDecimal.ZERO;
                    }

                    String imageUrl = card.get("ImageUrl");

                    tradingCards.add(new TradingCard(id, name, specialty, contribution, price, imageUrl));
                } catch (Exception e) {
                    System.err.println("Error processing card record: " + e.getMessage());
                }
            }

            System.out.println("Successfully loaded " + tradingCards.size() + " cards");
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

    public List<TradingCard> getFilteredCards(BigDecimal minPrice, BigDecimal maxPrice, String specialty, String sort) {
        BigDecimal effectiveMinPrice = (minPrice != null) ? minPrice : BigDecimal.ZERO;
        BigDecimal effectiveMaxPrice = (maxPrice != null) ? maxPrice : new BigDecimal("999999999"); // Some large value

        List<TradingCard> filteredCards = tradingCards.stream()
                .filter(card -> card.getPrice().compareTo(effectiveMinPrice) >= 0)
                .filter(card -> card.getPrice().compareTo(effectiveMaxPrice) <= 0)
                .filter(card -> specialty == null || card.getSpecialty().equalsIgnoreCase(specialty))
                .sorted(getComparator(sort))
                .collect(Collectors.toList());

        return filteredCards;
    }

    public List<TradingCard> getQueriedCards( String query ){
        List<TradingCard> queriedCards = tradingCards.stream()
                .filter(card-> query == null || card.getContribution().equalsIgnoreCase(query))
                .collect(Collectors.toList());
        return queriedCards;
    }
}
