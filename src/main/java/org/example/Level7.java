package org.example;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Level7 {

    enum Card {
        A, K, Q, TEN, NINE, EIGHT, SEVEN, SIX, FIVE, FOUR, THREE, TWO, J;

        public static Card valueOf(char c) {
            return switch (c) {
                case 'A' -> A;
                case 'K' -> K;
                case 'Q' -> Q;
                case 'J' -> J;
                case 'T' -> TEN;
                case '9' -> NINE;
                case '8' -> EIGHT;
                case '7' -> SEVEN;
                case '6' -> SIX;
                case '5' -> FIVE;
                case '4' -> FOUR;
                case '3' -> THREE;
                case '2' -> TWO;
                default -> throw new RuntimeException("unknown card " + c);
            };
        }
    }

    record Hand(List<Card> cards, long bid) {

        private static enum Type {FIVE_OF_A_KIND, FOUR_OF_A_KIND, FULL_HOUSE, THREE_OF_A_KIND, TWO_PAIR, ONE_PAIR, HIGH_CARD}

        ;

        private static record Rank(Type type, Card card, Card secondCard, List<Card> remainingCards, List<Card> originalHand) {
            int compareTo(Rank other) {
                if (type.ordinal() > other.type.ordinal()) {
                    return -1;
                } else if (type.ordinal() < other.type.ordinal()) {
                    return 1;
                } else {

                    for( int i=0; i<originalHand.size(); i++) {
                        Card c = originalHand.get(i);
                        Card oc = other.originalHand.get(i);
                        if (c == oc) {
                            continue;
                        }
                        return oc.ordinal() - c.ordinal();
                    }
                    return 0;
//                    if (other.card == card) {
//                        if (other.secondCard == secondCard) {
//                            return compare( remainingCards, other.remainingCards);
//                        }
//                        return other.secondCard.ordinal() - secondCard.ordinal();
//                    } else {
//                        return other.card.ordinal() - card.ordinal();
//                    }
                }
            }

            private int compare(List<Card> cards, List<Card> otherCards) {
                Collections.sort(cards);
                Collections.sort(otherCards);
                for (int i = 0; i < cards.size(); i++) {
                    Card c = cards.get(i);
                    Card oc = otherCards.get(i);
                    if (c == oc) {
                        continue;
                    }
                    return oc.ordinal() - c.ordinal();
                }
                throw new RuntimeException("erhz comparing too many cards: " + cards + " / " + otherCards);
            }
        }

        private Rank rank() {

            // create all permutations of the cards

            Set<Card> possibleCards = new HashSet<>(this.cards);

//            System.out.println("possibleCards:" + possibleCards);

            Rank best = null;
            List<List<Card>> permutations = permute(cards,possibleCards);
            for( List<Card> p : permutations) {
//                System.out.println(" *  permutation:" + p);
                Rank r = rank(p);
                if (best==null || r.compareTo(best) > 0) {
                    best = r;
                }
            }
            return best;
        }

        private List<List<Card>> permute(List<Card> cards, Set<Card> possibleCards) {
//            System.out.println("permute cards:" + cards + ", possibleCards:" + possibleCards);
            List<List<Card>> oneList = new ArrayList<>();
            oneList.add(new ArrayList<>());
            if (cards.size()==0) return oneList;

            // loop through all cards to permutate the,
            List<List<Card>> perms = new ArrayList<>();

            Card head = cards.get(0);
            if (head ==Card.J) {
//                System.out.println("head is J");
                for (Card c : possibleCards) {
                    List<List<Card>> tails = permute(cards.subList(1, cards.size()), possibleCards);
                    for( List<Card> tail : tails) {
                        List<Card> perm = new ArrayList<>();
                        perm.add(c);
                        perm.addAll(tail);
                        perms.add(perm);
//                        System.out.println(" added perm:" + perm);
                    }
                }
            } else {
//                System.out.println("head is non-J");
                List<List<Card>> tails = permute(cards.subList(1, cards.size()), possibleCards);
                for( List<Card> tail : tails) {
                    List<Card> perm = new ArrayList<>();
                    perm.add(head);
                    perm.addAll(tail);
                    perms.add(perm);
//                    System.out.println(" added perm:" + perm);
                }
            }

//            System.out.println("  returning perms:" + perms.size());
            return perms;
        }

        private Rank rank(List<Card> cardsPermutated) {

            int pairs = 0;
            int sameScore = 0;
            Card highestPair = null;
            Card highestSecondPair = null;
            Card highestSame = null;
            for (Card c : Card.values()) {

                long score = cardsPermutated.stream().filter(c2 -> c2 == c).count();
                if (score == 2) {
                    pairs++;
                    highestSecondPair = c;
                    if (highestPair == null || c.ordinal() < highestPair.ordinal()) {
                        highestPair = c;
                    }
                }
                if (score > sameScore && score>=1) {
                    sameScore = (int) score;
                    highestSame = c;
                }
                if (score == sameScore && score>=1) {
                    if (highestSame == null || c.ordinal() < highestSame.ordinal()) {
                        highestSame = c;
                    }
                }
            }

            final Card bla = highestSame;
            List<Card> remaining = new ArrayList<>(cards);
            if(highestSame!=null)while(remaining.remove(highestSame)){};
            if(highestPair!=null)while(remaining.remove(highestPair)){};
            if(highestSecondPair!=null)while(remaining.remove(highestSecondPair)){};

            if (pairs == 2) {
                remaining = new ArrayList<>(cards);
                if(highestPair!=null)while(remaining.remove(highestPair)){};
                if(highestSecondPair!=null)while(remaining.remove(highestSecondPair)){};
                return new Rank(Type.TWO_PAIR, highestPair, highestSecondPair, remaining,cards);
            } else if (pairs >= 1 && sameScore == 3) {
                return new Rank(Type.FULL_HOUSE, highestSame, highestPair, remaining,cards);
            } else switch (sameScore) {
                case 5:
                    return new Rank(Type.FIVE_OF_A_KIND, highestSame, highestPair, remaining,cards);
                case 4:
                    return new Rank(Type.FOUR_OF_A_KIND, highestSame, highestPair, remaining,cards);
                case 3:
                    return new Rank(Type.THREE_OF_A_KIND, highestSame, highestPair, remaining,cards);
                case 2:
                    return new Rank(Type.ONE_PAIR, highestSame, null, remaining,cards);
                case 1:
                    return new Rank(Type.HIGH_CARD, highestSame, highestPair, remaining,cards);
                default:
                    throw new RuntimeException("unknown score " + sameScore + " for hand " + this);
            }
        }


    }


    public static void main(String[] args) throws URISyntaxException, IOException {

        String file = Files.readString(Paths.get(Level3b.class.getResource("/level7-input.txt").toURI()));

        String[] lines = file.split("\n");
        List<Hand> hands = readHands(lines);


        Collections.sort(hands, (h1, h2) -> h1.rank().compareTo(h2.rank()));
        hands.forEach(h -> System.out.println("\n" + h + "\n" + h.rank() +"\n"));

        long total = 0;
        for (int i = 1; i < hands.size() + 1; i++) {
            Hand h = hands.get(i - 1);
//            System.out.println("Hand " + i + ". " + h + ", score=" + (h.bid * i));
            total += h.bid * i;

        }

        System.out.println(hands.size());
        System.out.println(total);


    }

    private static List<Hand> readHands(String[] lines) {
        List<Hand> hands = new ArrayList<>();
        for (String line : lines) {
            String[] fields = line.split(" ");
            List<Card> cc = new ArrayList<>();
            char[] cards = fields[0].toCharArray();
            for (int i = 0; i < cards.length; i++) {
                cc.add(Card.valueOf(cards[i]));
            }
            long rank = Long.parseLong(fields[1]);
            hands.add(new Hand(cc, rank));
        }

        return hands;
    }

}
