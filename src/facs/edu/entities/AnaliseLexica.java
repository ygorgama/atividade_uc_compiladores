package facs.edu.entities;

import facs.edu.entities.enums.TokensTypes;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnaliseLexica {
    private final String regexKeyWords = "\\b(to|this|or|loop|Print|digint|bool|letstring)\\b";
    private final String operators = "[+\\-*/]";
    private final BufferedReader reader;
    private List<Token> tokenList = new LinkedList<>();

    private List<Character> characterList = new LinkedList<>();
    private Integer firstPosition = null;

    public AnaliseLexica(String fileDirectory) throws FileNotFoundException {
        this.reader = new BufferedReader(new FileReader(fileDirectory));
    }

    public void run() throws IOException {
        List<String> lines = reader.lines().toList();

        int presentLine = 0;

        for (String l : lines) {
            for (int i = 0; i < l.length(); i++) {
                char charAt = l.charAt(i);

                if (isSpace(charAt, presentLine, i)){
                    System.out.println("SPACE");
                } else if (isInteger(charAt, presentLine, i)) {
                    System.out.println("INTEGER");
                }else if(hasSpecialToken(charAt, presentLine, i)){
                    System.out.println("SPECIAL TOKEN");
                }
                else if (isReservedWord(charAt,presentLine, i)) {
                    System.out.println("RESERVED");
                }
            }


            presentLine++;
        }

        tokenList.sort(Comparator.comparing(Token::getTokenPosition));
        this.removeWhiteSpacesFromList();
    }

    private boolean isAnArithmeticOperator(Character character, int foundLine, int foundPosition){
        Pattern pattern = Pattern.compile(operators);
        Matcher matcher = pattern.matcher(character.toString());

        if (matcher.find()){
            TokensTypes tokensTypes = switch (matcher.group()) {
                case "+" -> TokensTypes.OPERATOR_PLUS;
                case "-" -> TokensTypes.OPERATOR_MINUS;
                default -> throw new IllegalStateException("Unexpected value: " + matcher.group());
            };

            Token token = new Token(
                    tokensTypes,
                    matcher.group(),
                    foundLine,
                    foundPosition
            );

            tokenList.add(token);

            return true;
        }

        return false;
    }

    private boolean isSpace(Character character, int foundLine, int foundPosition){
        if (character == ' '){
            Token token = new Token(
                    TokensTypes.SPACE,
                    " ",
                    foundLine,
                    foundPosition
            );

            tokenList.add(token);
            return true;
        }

        return false;
    }

    private boolean isInteger(Character character, int foundLine, int foundPosition){
        Pattern pattern = Pattern.compile("[0-9]");
        Matcher matcher = pattern.matcher(character.toString());

        if (matcher.find()){
            Token token = new Token(
                TokensTypes.INTEGER,
                character.toString(),
                foundLine,
                foundPosition
            );


            tokenList.add(token);

            return true;
        }

        return false;
    }

    private boolean isReservedWord(Character character, int foundLine, int foundPosition) {

        if (characterList.isEmpty()){
            this.firstPosition = foundPosition;
        }

        if (characterList.size() > 1){
            String stringToCheck = "";

            for (Character c : characterList){
                stringToCheck = stringToCheck + c;
            }

            Pattern pattern = Pattern.compile(this.regexKeyWords);
            Matcher matcher = pattern.matcher(stringToCheck);

            if(matcher.find()){
                Token token = new Token(
                        TokensTypes.valueOf(matcher.group().toUpperCase()),
                        matcher.group(),
                        foundLine,
                        this.firstPosition
                );


                tokenList.add(token);

                this.characterList = new ArrayList<>();
                this.firstPosition = null;
                return true;
            }
        }

        characterList.add(character);

        return false;
    };

    private boolean hasSpecialToken(Character character, int foundLine, int foundPosition){
        boolean functionReturn = false;
        Token token = null;
        switch (character){
            case '(':
                token = new Token(
                        TokensTypes.LPAREN,
                        "(",
                        foundLine,
                        foundPosition
                );
                functionReturn = true;
                break;
            case ')':
                token = new Token(
                        TokensTypes.RPAREN,
                        ")",
                        foundLine,
                        foundPosition
                );
                functionReturn = true;
                break;
            case '{':
                token = new Token(
                        TokensTypes.RKEY,
                        "{",
                        foundLine,
                        foundPosition
                );
                functionReturn = true;
                break;
            case '}':
                token = new Token(
                        TokensTypes.LKEY,
                        "}",
                        foundLine,
                        foundPosition
                );
                functionReturn = true;
                break;
            default:
                functionReturn = false;
                break;
        }

        if (functionReturn){
            tokenList.add(token);
        }

        return functionReturn;
    }

    private void removeWhiteSpacesFromList(){
        this.tokenList = this.tokenList.stream().filter(token -> token.getTokenType() != TokensTypes.SPACE).toList();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();


        for (Token token: this.tokenList){
            stringBuilder.append(token + "\n");
        }

        return  stringBuilder.toString();
    }
}
